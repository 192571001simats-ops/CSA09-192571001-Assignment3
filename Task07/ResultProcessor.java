import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class ResultProcessor {
  record Result(String name, boolean valid, String detail) {}

  static Result process(Path p) {
    try {
      String value = Files.readString(p).trim();
      int mark = Integer.parseInt(value);
      if (mark < 0 || mark > 100) throw new IllegalArgumentException("score out of range");
      return new Result(p.getFileName().toString(), true, mark >= 50 ? "PASS" : "FAIL");
    } catch (IOException | IllegalArgumentException e) {
      return new Result(
          p.getFileName().toString(), false, e.getClass().getSimpleName() + ": " + e.getMessage());
    }
  }

  static List<Result> run(List<Path> paths, int threads) {
    ConcurrentLinkedQueue<Result> results = new ConcurrentLinkedQueue<>();
    ExecutorService pool = Executors.newFixedThreadPool(threads);
    List<Future<Result>> futures = new ArrayList<>();
    try {
      for (Path p : paths)
        futures.add(
            pool.submit(
                () -> {
                  Result result = process(p);
                  results.add(result);
                  return result;
                }));
      for (int i = 0; i < futures.size(); i++)
        try {
          futures.get(i).get();
        } catch (ExecutionException e) {
          results.add(
              new Result(
                  paths.get(i).getFileName().toString(), false, "Worker failure: " + e.getCause()));
        }
    } catch (InterruptedException e) {
      for (Future<?> f : futures) f.cancel(true);
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Processing interrupted; no complete summary published", e);
    } finally {
      pool.shutdownNow();
    }
    return List.copyOf(results);
  }

  public static void main(String[] args) throws Exception {
    Path dir = Path.of("inputs");
    Files.createDirectories(dir);
    List<Path> small = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Path p = dir.resolve("small" + i + ".txt");
      small.add(p);
      if (i != 4) Files.writeString(p, i == 3 ? "CORRUPT" : Integer.toString(50 + i));
    }
    var checked = run(small, 2);
    Bench.require(
        checked.stream().filter(Result::valid).count() == 3 && checked.size() == 5,
        "hand checked 3 successful files and 2 rejected files");
    List<Path> paths = new ArrayList<>();
    for (int i = 0; i < 10000; i++) {
      Path p = dir.resolve("score" + i + ".txt");
      paths.add(p);
      if (i % 100 != 0) Files.writeString(p, i % 100 == 1 ? "BAD" : Integer.toString(i % 101));
    }
    for (int threads : new int[] {1, 2, 4, 8})
      Bench.measure(
          "pool_" + threads,
          10000,
          () -> {
            List<Result> results = run(paths, threads);
            long good = results.stream().filter(Result::valid).count();
            if (good != 9800 || results.size() != 10000)
              throw new AssertionError("incomplete summary");
            Bench.sink = good;
          });
    List<Result> results = run(paths, 4);
    StringBuilder failures = new StringBuilder("file,error\n");
    for (var r : results)
      if (!r.valid())
        failures.append(r.name()).append(',').append(r.detail().replace(',', ';')).append('\n');
    Files.writeString(Path.of("failures.csv"), failures);
    System.out.println("Files=10000 success=9800 failure=200; one result per submitted path");
  }
}
