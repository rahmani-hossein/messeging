package Broker;

import java.util.HashMap;
import java.util.Map;

public class MessageBroker {

    private Map<String, Topic> topics = new HashMap<>();

    public Map<String, Topic> getTopics() {
        return topics;
    }

    public void setTopics(Map<String, Topic> topics) {
        this.topics = topics;
    }

    public void addTopic(String name) {
        topics.put(name, new Topic(name));
    }

    public void addConsumer(String consumerGroup, String topicName) throws NoSuchTopicException {
        if (!topics.containsKey(topicName)) {
            throw new NoSuchTopicException(topicName);
        }
        topics.get(topicName).addGroup(consumerGroup);
    }

    public void put(String topic, String producerName, int value) {

        // checkForAdd(topic);
        topics.get(topic).put(producerName, value);
        //   monitor.doNotify();

    }

    public int get(String topic, String groupName, String consumerName) throws NoSuchTopicException {
        //wait for put something in the message broker and then  get it

//        if (!topics.containsKey(topic)){
//            System.out.println("stop");
//            monitor.doWait();
//        }

        if (!topics.containsKey(topic)) {
            throw new NoSuchTopicException(topic);
        }
        return topics.get(topic).get(groupName, consumerName);

    }

    private void checkForAdd(String topic) {
        if (!topics.containsKey(topic)) {
            addTopic(topic);
        }
    }

}
