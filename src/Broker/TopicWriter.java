package Broker;

import Logger.Logger;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class TopicWriter {
    RandomAccessFile buffer;
    FileWriter fileWriter;
    private Topic topic;
    private HashMap<String, Transaction> transactions;

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    TopicWriter(Topic topic) {
        this.topic = topic;
        transactions = new HashMap<>();
        try {
            buffer = new RandomAccessFile(topic.getTopicFile(), "rws");
            fileWriter = new FileWriter("debug.txt", true);
        } catch (FileNotFoundException e) {
            Logger.myLogger("file not found \n", true);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(String producerName, int value) {
        if (value <= 0) {
            handleTransactionOperation(producerName, value);
        } else {
            handleInsertOperation(producerName, value);
        }
    }


    private void handleTransactionOperation(String producerName, int value) {
        switch (value) {
            case 0:
                startTransaction(producerName);
                break;
            case -1:
                commitTransaction(producerName);
                break;
            case -2:
                cancelTransaction(producerName);
        }
    }

    private void handleInsertOperation(String producerName, int value) {
        if (transactions.containsKey(producerName)) {
            transactions.get(producerName).put(value);
        } else {
            synchronized (this) {
                writeValue(value);
                topic.notifyToAll();
            }
        }
    }

    private void addTransaction(String producerName) {
        transactions.put(producerName, new Transaction(this, producerName));
    }

    /**
     * This method is used to start a transaction for putting a transaction of values inside the buffer.
     *
     * @return Nothing.
     */
    private void startTransaction(String producerName) {
        if (transactions.containsKey(producerName) && !transactions.get(producerName).getValues().isEmpty()) {
           // System.out.println(transactions.get(producerName).getValues().toString());
            //To Do - Log the problem in finalizing previous transaction.
            commitTransaction(producerName);
            transactions.remove(producerName);

            Logger.myLogger("we have a problem during finalizing previous transaction " + producerName + "\n", true);
        }
        addTransaction(producerName);
        Logger.myLogger("start the transaction successfully \n", true);

    }

    /**
     * This method is used to end the transaction for putting a its values inside the file.
     *
     * @return Nothing.
     */
    private void commitTransaction(String producerName) {
        if (transactions.containsKey(producerName)) {
            // System.out.println(producerName);
            transactions.get(producerName).commit();
            transactions.remove(producerName);

        } else {
            //To Do - Log the problem in committing a non-existing transaction.
            Logger.myLogger("one problem during commiting ocuured because we dont have the key \n", true);
        }
    }

    /**
     * This method is used to cancel a transaction.
     *
     * @return Nothing.
     */
    private void cancelTransaction(String producerName) {
        if (transactions.containsKey(producerName)) {
            transactions.remove(producerName);
            Logger.myLogger(" we cancel successfully \n", true);

        } else {
            //To Do - Log the problem in canceling a non-existing transaction.
            Logger.myLogger("one problem during  cancelTransaction occured  because we dont have the key\n", true);
        }
    }

    public synchronized void writeValue(int value) {
        //To Do - Put the given value at the end of the topicFile
        try {

            //  buffer.seek(buffer.length());

            buffer.writeInt(value);
            fileWriter.write(value + "\n");
            fileWriter.flush();

            System.out.println("complete write " + value);
            // TopicReader.myWaitNotify.doNotify();


        } catch (IOException e) {
            Logger.myLogger("problem in writing \n", true);
            e.printStackTrace();
        }
    }
}

