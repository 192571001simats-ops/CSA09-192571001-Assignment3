import java.util.*;import java.util.concurrent.*;import java.lang.management.*;
public class ConcurrencyInvestigation {
 static class Account{final int id;int balance=1000;Account(int id){this.id=id;}}
 static void transfer(Account a,Account b,int amount){if(a==b)return;Account first=a.id<b.id?a:b,second=a.id<b.id?b:a;synchronized(first){synchronized(second){if(a.balance>=amount){a.balance-=amount;b.balance+=amount;}}}}
 static class Counter {
  int count=0,min=0,max=0;final boolean loop;
  Counter(boolean loop){this.loop=loop;}
  synchronized void take()throws InterruptedException{if(loop){while(count==0)wait();}else{if(count==0)wait();}count--;min=Math.min(min,count);}
  synchronized void put(){count++;max=Math.max(max,count);notifyAll();}
 }
 public static void main(String[] args)throws Exception{
  Object a=new Object(),b=new Object();CyclicBarrier gate=new CyclicBarrier(2);
  Runnable left=()->{synchronized(a){try{gate.await();}catch(Exception e){throw new RuntimeException(e);}synchronized(b){}}};
  Runnable right=()->{synchronized(b){try{gate.await();}catch(Exception e){throw new RuntimeException(e);}synchronized(a){}}};
  Thread t1=new Thread(left,"deadlock-A"),t2=new Thread(right,"deadlock-B");t1.setDaemon(true);t2.setDaemon(true);t1.start();t2.start();
  long[] deadlocked=null;for(int i=0;i<100&&deadlocked==null;i++){Thread.sleep(2);deadlocked=ManagementFactory.getThreadMXBean().findDeadlockedThreads();}
  Bench.require(deadlocked!=null&&deadlocked.length>=2,"two-lock circular wait detected by ThreadMXBean");
  for(long id:deadlocked)System.out.println(ManagementFactory.getThreadMXBean().getThreadInfo(id));
  Account north=new Account(1),south=new Account(2);Thread n=new Thread(()->{for(int i=0;i<1000;i++)transfer(north,south,1);}),s=new Thread(()->{for(int i=0;i<1000;i++)transfer(south,north,1);});n.start();s.start();n.join();s.join();
  Bench.require(north.balance+south.balance==2000,"ordered transfers preserve money and finish");
  for(boolean loop:new boolean[]{false,true}){
   Counter c=new Counter(loop);Thread u=new Thread(()->{try{c.take();}catch(InterruptedException e){Thread.currentThread().interrupt();}}),v=new Thread(()->{try{c.take();}catch(InterruptedException e){Thread.currentThread().interrupt();}});u.start();v.start();
   for(int i=0;i<100&&(u.getState()!=Thread.State.WAITING||v.getState()!=Thread.State.WAITING);i++)Thread.sleep(1);
   c.put();Thread.sleep(20);c.put();u.join(1000);v.join(1000);if(u.isAlive()||v.isAlive())throw new AssertionError("waiters stuck");
   System.out.println("while-loop="+loop+" observed min="+c.min+" max="+c.max+" final="+c.count);if(loop)Bench.require(c.min>=0,"guard recheck prevents underflow");
  }
  BlockingQueue<Integer> q=new ArrayBlockingQueue<>(5);for(int i=1;i<=5;i++)q.put(i);Bench.require(q.size()==5,"ArrayBlockingQueue capacity");
 }
}
