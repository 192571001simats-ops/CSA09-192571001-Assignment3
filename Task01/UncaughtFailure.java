public class UncaughtFailure {
 static void service(){throw new IllegalArgumentException("Amount must be positive");}
 public static void main(String[] args){service();}
}
