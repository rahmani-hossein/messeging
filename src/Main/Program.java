package Main;

import Broker.MessageBroker;
import Broker.Topic;
import Consumer.ConsumerGroup;
import Logger.Logger;
import Producer.ProducerGroup;

import java.io.File;

public class Program {

    private String[] args;
    private MessageBroker messageBroker;

    Program(String args[]) {
        this.args = args;
        messageBroker = new MessageBroker();
    }

    private File getProducerGroupDirectory(String sourceName) {
        File producerDirectory = new File(sourceName);
        if(args.length>0) {
            producerDirectory = new File(args[0]);
        }

        return producerDirectory;
    }

    void run() {
        String address ="allTopics/";
        File fileAddress =new File(address);
        for (File file : fileAddress.listFiles()) {
            String mySugar=address+file.getName();
            File producerGroupDirectory = getProducerGroupDirectory(mySugar);
            System.out.println(producerGroupDirectory);
            String topicName = producerGroupDirectory.getName();
            String consumerGroupName1 = topicName + "Readers1";
            String consumerGroupName2 = topicName + "Readers2";
            File consumerGroupFile1 = new File(consumerGroupName1+".txt");
            File consumerGroupFile2 = new File(consumerGroupName2+".txt");
            int numberOfConsumers = 15;
            ProducerGroup producerGroup = new ProducerGroup(messageBroker, producerGroupDirectory, topicName);
            ConsumerGroup consumerGroup1 = new ConsumerGroup(messageBroker, topicName, consumerGroupName1, consumerGroupFile1, numberOfConsumers);
            ConsumerGroup consumerGroup2 = new ConsumerGroup(messageBroker, topicName, consumerGroupName2, consumerGroupFile2, numberOfConsumers);
            producerGroup.start();
            consumerGroup1.start();
            consumerGroup2.start();
                    while(producerGroup.isAlive() || consumerGroup1.isAlive()||consumerGroup2.isAlive()) {
            try {
                producerGroup.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        }






//        Logger.myLogger("game Started! +\n",true);
//        File producerGroupDirectory = getProducerGroupDirectory("allTopics/data/");
//        String topicName = producerGroupDirectory.getName();
//
//        File consumerGroupFile = new File(topicName+".txt");
//        String consumerGroupName = topicName + "Readers";
//        int numberOfConsumers = 10;
//        Topic topic=new Topic(topicName);
//        ProducerGroup producerGroup = new ProducerGroup(messageBroker, producerGroupDirectory, topicName);
//        ConsumerGroup consumerGroup = new ConsumerGroup(messageBroker, topicName, consumerGroupName, consumerGroupFile, numberOfConsumers);
//        messageBroker.getTopics().put(topicName,topic);
//        producerGroup.start();
//        consumerGroup.start();
//        while(producerGroup.isAlive() || consumerGroup.isAlive()) {
//            try {
//                producerGroup.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }


        //messageBroker.getTopics().put(topicName,topic);



    }
}

