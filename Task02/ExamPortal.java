import java.io.*;
import java.util.*;

public class ExamPortal {
  static class ExamClosed extends ExamException {
    ExamClosed() {
      super("Exam window closed");
    }
  }

  static class NotRegistered extends ExamException {
    NotRegistered() {
      super("Student not registered");
    }
  }

  static class TooLarge extends ExamException {
    TooLarge() {
      super("Answer exceeds 500 KB");
    }
  }

  static void service(boolean open, boolean registered, long size, boolean diskFailure)
      throws ExamException {
    if (!open) throw new ExamClosed();
    if (!registered) throw new NotRegistered();
    if (size > 500) throw new TooLarge();
    try {
      if (diskFailure) throw new IOException("Storage unavailable");
      System.out.println("Submission accepted");
    } catch (IOException cause) {
      throw new SubmissionStorageException("Submission could not be stored", cause);
    }
  }

  public static void main(String[] args) {
    for (int i = 0; i < 5; i++)
      try {
        service(i != 1, i != 2, i == 3 ? 800 : 120, i == 4);
      } catch (ExamException e) {
        System.out.println(
            "Controller handled " + e.getClass().getSimpleName() + ": " + e.getMessage());
        if (e.getCause() != null) {
          System.out.println("getCause=" + e.getCause());
          e.printStackTrace(System.out);
        }
      }
    var lost = new SubmissionStorageException("No cause retained", null);
    Bench.require(lost.getCause() == null, "omitting cause loses lower-layer diagnostics");
    System.out.println(
        "Calling getCause().getMessage() with a null cause would throw NullPointerException");
  }
}
