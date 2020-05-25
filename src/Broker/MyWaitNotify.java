package Broker;

public class MyWaitNotify {

   private final Object monitorObject;
//for insist
   volatile int numSignal;

   boolean free;

    MyWaitNotify() {
      monitorObject=new Object();
      free=true;
      numSignal=0;
    }

//    MyWaitNotify(Object monitorObject) {
//        this(monitorObject,0);
//    }
//    MyWaitNotify(Object monitorObject,int  wasSignalled) {
//        this.monitorObject = monitorObject;
//        this.numSignal=wasSignalled;
//    }

    public void doWait() {
        synchronized (monitorObject) {
            // for we dont have any signal
            while (numSignal<=0||!free) {
                try {
                    monitorObject.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            numSignal--;
            free=false;
        }
    }
// for handle waiting statement
    public void doNotify(){
        synchronized (monitorObject){
           free=true;
            monitorObject.notifyAll();
        }
    }
    public void addToSignal(int num) {
        synchronized (monitorObject) {
            numSignal+=num;
            monitorObject.notifyAll();
        }
    }
}
