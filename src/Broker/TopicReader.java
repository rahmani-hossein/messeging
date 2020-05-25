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

    TopicReader(Topic topic, String groupName) {
        this.topic = topic;
        this.groupName = groupName;
        transactions = new HashMap<>();
        myWaitNotify= new MyWaitNotify(this.topic);
        //To Do - Generate topicFile
        try {
            topicFile = new RandomAccessFile(topic.getTopicFile().getPath(), "rws");
        } catch (FileNotFoundException e) {
            Logger.myLogger("file not found \n", true);
            e.printStackTrace();
        }
    }

    public int get(String consumerName) {
        int value = 0;
        //TopicWriter.myWaitNotify.doWait();
        //To Do - Read next value from topicFile and return the value
        try {
            synchronized (topic) {
                // topicFile.seek(0);
                System.out.println(topicFile.length() - topicFile.getFilePointer() + "asdfgh");
                if (topicFile.getFilePointer() == topicFile.length()) {
                    System.out.println("i am waiting");
                    myWaitNotify.doWait();
                }
                if (topicFile.getFilePointer() != topicFile.length()) {
                    //myWaitNotify.doNotify();
                    System.out.println("i am notified");
                    value = topicFile.readInt();
                    System.out.println(value);
                }
            }

        } catch (IOException e) {
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
