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

    private void addTopic(String name) {
        topics.put(name, new Topic(name));
    }

    public void put(String topic, String producerName, int value) {

        if (!topics.containsKey(topic)) {
            addTopic(topic);
        }
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

}
