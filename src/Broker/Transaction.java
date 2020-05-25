package Broker;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class Transaction {

    private TopicWriter topicWriter;
    private String producerName;
    private Queue<Integer> values;

    public Queue<Integer> getValues() {
        return values;
    }

    public void setValues(Queue<Integer> values) {
        this.values = values;
    }

    Transaction(TopicWriter topicWriter, String producerName) {
        this.topicWriter = topicWriter;
        this.producerName = producerName;
        values = new LinkedList<>();
    }

    void put(int value) {
        values.add(value);
    }

    void commit() {
            synchronized (topicWriter) {
                topicWriter.writeValue(0);
                while (!values.isEmpty()) {
                    int value=values.remove();
                    topicWriter.writeValue(value);
                }
                topicWriter.writeValue(-1);
                //for every signal is for one consumer
                topicWriter.getTopic().notifyToAll();
            }
    }


}
