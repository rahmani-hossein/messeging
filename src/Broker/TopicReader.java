package Broker;

import Logger.Logger;

import java.awt.*;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class TopicReader {

    RandomAccessFile topicFile;

    private Topic topic;
    private String groupName;
    MyWaitNotify myWaitNotify;
    private boolean inTransaction = false;
    private String currentConsumer;
    private int consumersNumbers;
    private Object lock = new Object();

    public String getCurrentConsumer() {
        return currentConsumer;
    }

    public void setCurrentConsumer(String currentConsumer) {
        this.currentConsumer = currentConsumer;
    }

    TopicReader(Topic topic, String groupName) {
        this.topic = topic;
        this.groupName = groupName;
        myWaitNotify = new MyWaitNotify();
        //To Do - Generate topicFile
        try {
            topicFile = new RandomAccessFile(topic.getTopicFile(), "rws");
        } catch (FileNotFoundException e) {
            Logger.myLogger("file not found \n", true);
            e.printStackTrace();
        }
    }

    public int get(String consumerName) {

        if (!consumerName.equals(this.currentConsumer)) {
          //  System.out.println("consumerName "+ consumerName+"  waited");
            myWaitNotify.doWait();
           // System.out.println("consumerName "+ consumerName+"  notified");
        }
       if (!notEnd()){
           myWaitNotify.doNotify();
           myWaitNotify.addToSignal(10);
          return -3;
       }
       //mibare jelo
       int next=getValue();
       if (next==0){
           currentConsumer=consumerName;
           next=getValue();
       }
       if (currentConsumer!=null){
           if (getValueWithoutMove()==-1){
               getValue();
               currentConsumer=null;
               myWaitNotify.doNotify();
               return next;
           }
           return next;
       }
            //To Do - Read next value from topicFile and return the value

//            synchronized (topic) {
//
//                // topicFile.seek(0);
//                System.out.println(topicFile.length() - topicFile.getFilePointer() + "asdfgh");
//                if (topicFile.getFilePointer() == topicFile.length()) {
////                    System.out.println("i am waiting");
//                    System.out.println("before check end of file");
//                  //  myWaitNotify.doWait();
//                    topic.wait();
//                }
//                if (topicFile.getFilePointer() != topicFile.length()) {
//                    //myWaitNotify.doNotify();
//                    //System.out.println("i am notified");
//                    // avalesh ke esmesh barabar nabod
//
//                        if (currentConsumer >= 0) {
//                            System.out.println("before check current consumer");
//                            topic.wait();
//                        }
//
//
//                    //read from file
//                    System.out.println(System.currentTimeMillis());
//                    value = topicFile.readInt();
//                    System.out.println(value);
//                    if ((value <= 0)) {
//                        if (value==0){
//                            inTransaction=true;
//                            currentConsumer=Thread.currentThread().getId();
//                        }
//                        if (value==-1){
//                            currentConsumer=-1;
//                            inTransaction=false;
//                           topic.notify();
//
//                        }
//
//                    } else {
//                        System.out.println(value+" with transaction  "+ inTransaction);
//                        if (!inTransaction){
//                           topic.notify();
//                        }
//
//
//                    }
//                }
//            }
//
//        } catch (IOException e) {
//            Logger.myLogger("problem from reading binary file \n", true);
//            e.printStackTrace();
//
//            //To Do - Handle the transaction constraints
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        myWaitNotify.doNotify();
            return next;

        }

        public void myNotify () {
            myWaitNotify.addToSignal(1);
        }

        private int getValueWithoutMove () {
            try {
                long currentPointer = topicFile.getFilePointer();
                int nextValue = topicFile.readInt();
                topicFile.seek(currentPointer);
                return nextValue;
            } catch (EOFException e) {
                return -3;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -3;
        }
        private int getValue () {
            try {
                int nextValue = topicFile.readInt();
                return nextValue;
            } catch (EOFException e) {
                return -3;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -3;
        }

       private boolean notEnd() {
            try {
                if (topicFile.getFilePointer() < topicFile.length()) {
                    return true;
                }

            } catch (IOException e) {
                e.printStackTrace();

            }
            return false;
        }
    }

