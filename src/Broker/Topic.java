package Broker;

import java.io.File;
import java.util.HashMap;

public class Topic {
    private String name;
     static Object lock1 = new Object();



    private File topicFile;
    private TopicWriter topicWriter;
    private HashMap<String, TopicReader> topicReaders;

    public HashMap<String, TopicReader> getTopicReaders() {
        return topicReaders;
    }

    public void setTopicReaders(HashMap<String, TopicReader> topicReaders) {
        this.topicReaders = topicReaders;
    }



    public Topic(String name) {
        this.name = name;
        topicFile = new File(name + ".dat");
        topicWriter = new TopicWriter(this);
        topicReaders = new HashMap<>();
    }

    public File getTopicFile() {
        return topicFile;
    }

    public void addGroup(String groupName) {
        topicReaders.put(groupName, new TopicReader(this, groupName));
    }

    /**
     * This method is used to get the first value in the topic file which is not read in the given group yet, and serve it for the appropriate consumer.
     *
     * @return the value of the first remained item.
     */
    public int get(String groupName, String consumerName) {

//            if (!topicReaders.containsKey(groupName)) {
//                addGroup(groupName);
//            }
            return topicReaders.get(groupName).get(consumerName);

    }
    // synchronize get for manage accessing readers to a topic

    /**
     * This method is used to put the given value at the tail of the topic file.
     *
     * @param value the value to be put at the end of the topic file
     * @return Nothing.
     */
    public void put(String producerName, int value) {

        topicWriter.put(producerName, value);


    }
    public void notifyToAll(){
        for (TopicReader value : topicReaders.values()) {
            value.myNotify();
        }
    }
}
