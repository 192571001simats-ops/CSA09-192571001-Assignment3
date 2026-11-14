import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class BookingExperiments {
  static class Screen {
    final boolean[] seats = new boolean[10000];
    int sold;

    boolean plain(int s) {
      if (seats[s]) return false;
      Thread.yield();
      seats[s] = true;
      sold++;
      return true;
    }

    synchronized boolean method(int s) {
      if (seats[s]) return false;
      seats[s] = true;
      sold++;
      return true;
    }

    boolean block(int s) {
      if (s < 0 || s >= seats.length) throw new IllegalArgumentException();
      synchronized (this) {
        if (seats[s]) return false;
        seats[s] = true;
        sold++;
        return true;
      }
    }
  }

  static void load(int mode) {
    Screen screen = new Screen();
    Thread[] threads = new Thread[4];
    AtomicInteger wins = new AtomicInteger();
    for (int k = 0; k < 4; k++) {
      threads[k] =
          new Thread(
              () -> {
                for (int i = 0; i < 2500; i++) {
                  int seat = i % 1000;
                  boolean success =
                      mode == 0
                          ? screen.plain(seat)
                          : mode == 1 ? screen.method(seat) : screen.block(seat);
                  if (success) wins.incrementAndGet();
                }
              });
      threads[k].start();
    }
    for (Thread t : threads)
      try {
        t.join();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }
    if (mode > 0 && wins.get() != 1000) throw new AssertionError("double booking");
    Bench.sink = wins.get();
  }

  public static void main(String[] args) throws Exception {
    for (int run = 1; run <= 10; run++) {
      boolean[] booked = {false};
      CyclicBarrier afterCheck = new CyclicBarrier(2);
      AtomicInteger sold = new AtomicInteger();
      Runnable unsafe =
          () -> {
            if (!booked[0]) {
              try {
                afterCheck.await();
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
              booked[0] = true;
              sold.incrementAndGet();
            }
          };
      Thread a = new Thread(unsafe), b = new Thread(unsafe);
      a.start();
      b.start();
      a.join();
      b.join();
      Bench.require(sold.get() == 2, "controlled race run " + run + " sells one seat twice");
      Screen natural = new Screen();
      Thread[] users = new Thread[100];
      for (int i = 0; i < 100; i++) {
        int seat = i % 10;
        users[i] = new Thread(() -> natural.plain(seat));
        users[i].start();
      }
      for (Thread t : users) t.join();
      System.out.println(
          "Uncontrolled unsynchronized run "
              + run
              + " counter="
              + natural.sold
              + " (counter can also lose updates)");
    }
    for (int mode = 0; mode < 3; mode++) {
      final int m = mode;
      Bench.measure(
          new String[] {"unsafe_10000_requests", "method_10000_requests", "block_10000_requests"}
              [m],
          10000,
          () -> load(m));
    }
    AtomicBoolean seat = new AtomicBoolean();
    Bench.require(
        seat.compareAndSet(false, true) && !seat.compareAndSet(false, true),
        "CAS alternative permits one owner");
  }
}
