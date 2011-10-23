package com.rabbitmq.examples.stock.consumer;

import com.rabbitmq.client.*;

/**
 * @author Rob Harrop
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("stock.prices", false, false, true, null);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume("stock.prices", consumer);

        Thread thread = new Thread(new ConsumerTask(consumer));
        thread.start();

        System.in.read();

        thread.interrupt();
        thread.join();

        channel.close();
        connection.close();
    }

}
