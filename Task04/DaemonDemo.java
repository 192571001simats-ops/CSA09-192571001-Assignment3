public class DaemonDemo {
 public static void main(String[] args){
  Thread daemon=new Thread(()->{try{Thread.sleep(200);System.out.println("DAEMON COMPLETED");}catch(InterruptedException e){Thread.currentThread().interrupt();}});
  daemon.setDaemon(true);daemon.start();System.out.println("Main ends without joining daemon");
 }
}
