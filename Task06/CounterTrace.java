import java.nio.file.*;
import java.util.*;

public class CounterTrace {
  static class TracedCounter extends TokenCounter {
    int live, min, max;
    final List<String> history = new ArrayList<>();

    @Override
    public synchronized void put(int token) throws InterruptedException {
      super.put(token);
      live++;
      record("put", token);
    }

    @Override
    public synchronized int take() throws InterruptedException {
      int token = super.take();
      live--;
      record("take", token);
      return token;
    }

    private void record(String action, int token) {
      if (live < 0 || live > 5) throw new AssertionError("capacity invariant");
      min = Math.min(min, live);
      max = Math.max(max, live);
      history.add(action + "," + token + "," + live);
    }
  }

  public static void main(String[] args) throws Exception {
    TracedCounter counter = new TracedCounter();
    Thread[] producers = new Thread[2], consumers = new Thread[3];
    int[] sums = new int[3];
    for (int k = 0; k < 2; k++) {
      final int id = k;
      producers[k] =
          new Thread(
              () -> {
                try {
                  for (int i = 1; i <= 30; i++) counter.put(id * 30 + i);
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                }
              });
    }
    for (int k = 0; k < 3; k++) {
      final int id = k;
      consumers[k] =
          new Thread(
              () -> {
                try {
                  for (int i = 0; i < 20; i++) sums[id] += counter.take();
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                }
              });
    }
    for (Thread t : producers) t.start();
    for (Thread t : consumers) t.start();
    for (Thread t : producers) t.join();
    for (Thread t : consumers) t.join();
    Bench.require(
        Arrays.stream(sums).sum() == 1830 && counter.live == 0 && counter.history.size() == 120,
        "60 unique tokens collected with complete occupancy trace");
    Files.writeString(
        Path.of("buffer-trace.csv"),
        "action,token,occupancy\n" + String.join("\n", counter.history) + "\n");
    System.out.println(
        "Observed occupancy min=" + counter.min + " max=" + counter.max + " final=" + counter.live);
  }
}
