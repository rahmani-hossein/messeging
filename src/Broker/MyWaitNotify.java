package Broker;

public class MyWaitNotify {

    Object monitorObject;
    boolean isSignalled;
    int numSignal=0;

    MyWaitNotify() {
        this(new Object(),false);
    }

    MyWaitNotify(Object monitorObject) {
        this(monitorObject,false);
    }
    MyWaitNotify(Object monitorObject,boolean wasSignalled) {
        this.monitorObject = monitorObject;
        this.isSignalled=wasSignalled;
    }

    public void doWait() {
        synchronized (monitorObject) {
            while (!isSignalled) {
                try {
                    monitorObject.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isSignalled=false;
        }
    }

    public void doNotify(){
        synchronized (monitorObject){
            isSignalled=true;
            monitorObject.notify();
        }
    }
}
