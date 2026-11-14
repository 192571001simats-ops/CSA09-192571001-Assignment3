import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Atm {
  static int balance = 1000;

  static void withdraw(String account, String amount, Path reader) {
    try {
      Files.readString(reader);
      int number = Integer.parseInt(account);
      int value = Integer.parseInt(amount);
      if (number < 1 || value <= 0)
        throw new IllegalArgumentException("Account and amount must be positive");
      if (value > balance) throw new IllegalStateException("Insufficient balance");
      balance -= value;
      System.out.println("DISPENSED " + value + " balance=" + balance);
    } catch (NumberFormatException e) {
      System.out.println("CATCH NumberFormatException: enter numbers");
    } catch (IllegalArgumentException | IllegalStateException e) {
      System.out.println("CATCH business validation: " + e.getMessage());
    } catch (NoSuchFileException e) {
      System.out.println("CATCH NoSuchFileException: card reader unavailable");
    } catch (IOException e) {
      System.out.println("CATCH IOException: reader failure");
    } finally {
      System.out.println("FINALLY session closed");
    }
  }

  public static void main(String[] args) throws IOException {
    Path p = Files.createTempFile("card-reader", ".txt");
    Files.writeString(p, "ready");
    String[][] tests = {
      {"1001", "100"}, {"abc", "100"}, {"1001", "-1"}, {"1001", "2000"}, {"1001", "0"}
    };
    for (String[] test : tests) {
      System.out.println("CASE " + Arrays.toString(test));
      withdraw(test[0], test[1], p);
    }
    Files.delete(p);
    withdraw("1001", "100", p);
    Bench.require(balance == 900, "only the valid withdrawal changed balance");
  }
}
