import java.util.*;

public class ScannerThreads {
  static class Station extends Thread {
    final int id;
    int scanned;
    final List<Integer> order;

    Station(int id, List<Integer> order) {
      this.id = id;
      this.order = order;
      setName("Station-" + id);
    }

    @Override
    public void run() {
      try {
        for (int i = 0; i < 5; i++) {
          Thread.sleep(1);
          scanned++;
        }
        order.add(id);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public static void main(String[] args) throws Exception {
    for (int run = 1; run <= 10; run++) {
      List<Integer> order = Collections.synchronizedList(new ArrayList<>());
      Station[] stations = new Station[4];
      for (int i = 0; i < 4; i++) {
        stations[i] = new Station(i, order);
        stations[i].setPriority(
            run % 2 == 0
                ? (i == 0 ? Thread.MAX_PRIORITY : Thread.NORM_PRIORITY)
                : Thread.NORM_PRIORITY);
      }
      System.out.println("RUN " + run + " before start=" + stations[0].getState());
      for (Thread t : stations) t.start();
      System.out.println("Alive after start=" + stations[0].isAlive());
      for (Thread t : stations) t.join();
      int total = Arrays.stream(stations).mapToInt(s -> s.scanned).sum();
      Bench.require(total == 20, "joined total run " + run);
      System.out.println("Finish order=" + order + " final=" + stations[0].getState());
    }
    Runnable task =
        () -> System.out.println("Runnable executing in " + Thread.currentThread().getName());
    Thread worker = new Thread(task, "new-worker");
    worker.run();
    worker.start();
    worker.join();
  }
}
