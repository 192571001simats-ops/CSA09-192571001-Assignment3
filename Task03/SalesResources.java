import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SalesResources {
  static int open;
  static List<TrackedReader> leaked = new ArrayList<>();

  static class TrackedReader extends BufferedReader {
    private boolean closed;

    TrackedReader(Path p) throws IOException {
      super(Files.newBufferedReader(p));
      open++;
    }

    @Override
    public void close() throws IOException {
      if (!closed) {
        closed = true;
        open--;
        super.close();
      }
    }
  }

  static double leaky(List<Path> files) throws IOException {
    double sum = 0;
    for (Path p : files) {
      TrackedReader r = new TrackedReader(p);
      leaked.add(r);
      String line;
      while ((line = r.readLine()) != null) sum += Double.parseDouble(line);
      r.close();
    }
    return sum;
  }

  static double manual(List<Path> files) throws IOException {
    double sum = 0;
    for (Path p : files) {
      TrackedReader r = null;
      try {
        r = new TrackedReader(p);
        String line;
        while ((line = r.readLine()) != null) sum += Double.parseDouble(line);
      } finally {
        if (r != null) r.close();
      }
    }
    return sum;
  }

  static double safe(List<Path> files) throws IOException {
    double sum = 0;
    for (Path p : files)
      try (TrackedReader r = new TrackedReader(p)) {
        String line;
        while ((line = r.readLine()) != null) sum += Double.parseDouble(line);
      } catch (IOException | NumberFormatException e) {
        System.out.println("FAILED FILE " + p.getFileName() + " " + e);
        throw e;
      }
    return sum;
  }

  static class ClosingFailure implements AutoCloseable {
    public void close() throws IOException {
      throw new IOException("close failed");
    }
  }

  public static void main(String[] args) throws Exception {
    Path dir = Path.of("inputs");
    Files.createDirectories(dir);
    Path a = dir.resolve("store-a.txt"), b = dir.resolve("store-b.txt");
    Files.writeString(a, "100\n200\n");
    Files.writeString(b, "50\n");
    List<Path> files = List.of(a, b);
    double expected = 350;
    Bench.require(
        manual(files) == expected && safe(files) == expected,
        "manual finally and try-with-resources same sales report");
    Files.writeString(b, "50\nBAD\n");
    try {
      leaky(files);
    } catch (NumberFormatException e) {
      System.out.println("Leaky version open readers after failure=" + open);
      Bench.require(open == 1, "reproduced leaked handle");
    }
    for (var r : leaked) r.close();
    try {
      manual(files);
    } catch (NumberFormatException e) {
      Bench.require(open == 0, "manual finally closes on failure");
    }
    try {
      safe(files);
    } catch (NumberFormatException e) {
      Bench.require(open == 0, "try-with-resources closes on failure");
    }
    try (ClosingFailure r = new ClosingFailure()) {
      throw new IOException("body failed");
    } catch (IOException e) {
      System.out.println(
          "Primary: " + e.getMessage() + "; suppressed: " + e.getSuppressed()[0].getMessage());
    }
    System.out.println(
        "Atomic publication of the report is still required to avoid publishing a partially written"
            + " result");
  }
}
