package Broker;

public class MyWaitNotify {

    Object monitorObject;

    int numSignal=0;

    MyWaitNotify() {
        this(new Object(),0);
    }

    MyWaitNotify(Object monitorObject) {
        this(monitorObject,0);
    }
    MyWaitNotify(Object monitorObject,int  wasSignalled) {
        this.monitorObject = monitorObject;
        this.numSignal=wasSignalled;
    }

    public void doWait() {
        synchronized (monitorObject) {
            while (numSignal>=0) {
                try {
                    monitorObject.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            numSignal--;
        }
    }

    public void doNotify(){
        synchronized (monitorObject){
            numSignal++;
            monitorObject.notify();
        }
    }
}
