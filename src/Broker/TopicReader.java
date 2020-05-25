package Broker;

import Logger.Logger;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class TopicReader {

    RandomAccessFile topicFile;

    private Topic topic;
    private String groupName;
    private HashMap<String, Transaction> transactions;
    static MyWaitNotify myWaitNotify ;
    private boolean inTransaction=false;
    private long currentConsumer=-1;
    private Object lock=new Object();

    public long getCurrentConsumer() {
        return currentConsumer;
    }

    public void setCurrentConsumer(long  currentConsumer) {
        this.currentConsumer = currentConsumer;
    }
    TopicReader(Topic topic, String groupName) {
        this.topic = topic;
        this.groupName = groupName;
        transactions = new HashMap<>();
        myWaitNotify= new MyWaitNotify(new Object());
        //To Do - Generate topicFile
        try {
            topicFile = new RandomAccessFile(topic.getTopicFile().getPath(), "rws");
        } catch (FileNotFoundException e) {
            Logger.myLogger("file not found \n", true);
            e.printStackTrace();
        }
    }

    public synchronized int get(String consumerName) {
        int value = -3;

        //TopicWriter.myWaitNotify.doWait();
        //To Do - Read next value from topicFile and return the value
        try {
            synchronized (topic) {

                // topicFile.seek(0);
                System.out.println(topicFile.length() - topicFile.getFilePointer() + "asdfgh");
                if (topicFile.getFilePointer() == topicFile.length()) {
//                    System.out.println("i am waiting");
                    System.out.println("before check end of file");
                  //  myWaitNotify.doWait();
                    topic.wait();
                }
                if (topicFile.getFilePointer() != topicFile.length()) {
                    //myWaitNotify.doNotify();
                    //System.out.println("i am notified");
                    // avalesh ke esmesh barabar nabod

                        if (currentConsumer >= 0) {
                            try {
                                System.out.println("before check current consumer");
                                topic.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }


                    //read from file
                    System.out.println(System.currentTimeMillis());
                    value = topicFile.readInt();
                    System.out.println(value);
                    if ((value <= 0)) {
                        if (value==0){
                            inTransaction=true;
                            currentConsumer=Thread.currentThread().getId();
                        }
                        if (value==-1){
                            currentConsumer=-1;
                            inTransaction=false;
                           topic.notify();

                        }

                    } else {
                        System.out.println(value+" with transaction  "+ inTransaction);
                        if (!inTransaction){
                           topic.notify();
                        }


                    }
                }
            }

        } catch (IOException | InterruptedException e) {
            Logger.myLogger("problem from reading binary file \n", true);
            e.printStackTrace();

            //To Do - Handle the transaction constraints

        }

        return value;
    }
//    public int readValue(){
//        int value = 0;
//        //To Do - Read next value from topicFile and return the value
//        try {
//            topicFile.seek(0);
//            value=topicFile.readInt();
//        } catch (IOException e) {
//            Logger.myLogger("problem from reading binary file \n",true);
//            e.printStackTrace();
//        }
//        return value;
//    }


}
