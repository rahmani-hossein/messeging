package Consumer;

import Broker.MessageBroker;
import Broker.NoSuchTopicException;

public class Consumer extends Thread {

    private ConsumerGroup consumerGroup;
    private String consumerName;

    Consumer(ConsumerGroup consumerGroup, String consumerName) {
        this.consumerGroup = consumerGroup;
        this.consumerName = consumerName;
    }

    public int get() throws NoSuchTopicException {
        return getMessageBroker().get(getTopicName(), consumerGroup.getGroupName(), consumerName);

    }

    public void run() {
        while (true) {
            try {
                int i=get();
                if (i==-3){
                    System.out.println(this.consumerName+" finished");
                    return;
                }
                consumerGroup.performAction(this, i);
            } catch (NoSuchTopicException e) {
                e.printStackTrace();
            }
        }
    }

    public MessageBroker getMessageBroker() {
        return consumerGroup.getMessageBroker();
    }

    public String getConsumerName() {
        return consumerName;
    }

    public String getTopicName() {
        return consumerGroup.getTopicName();
    }
}
