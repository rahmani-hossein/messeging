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

    static Object lockWrite = new Object();

    Transaction(TopicWriter topicWriter, String producerName) {
        this.topicWriter = topicWriter;
        this.producerName = producerName;
        values = new LinkedList<>();
    }

    void put(int value) {
        values.add(value);
    }

    void commit() {
            synchronized (Color.BLACK) {
                topicWriter.writeValue(0);
                while (!values.isEmpty()) {
                    System.out.println(values);
                    topicWriter.writeValue(values.remove());
                }
                topicWriter.writeValue(-1);
            }
    }

    void read(){

    }
}
