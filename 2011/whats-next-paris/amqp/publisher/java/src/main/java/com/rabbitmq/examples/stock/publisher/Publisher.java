package com.rabbitmq.examples.stock.publisher;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Random;

/**
 * @author Rob Harrop
 */
public class Publisher {

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private static final String[] TICKER_SYMBOLS = {"GOOG", "VMW", "AAPL"};

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        Thread thread = new Thread(new PublishTask(channel));
        thread.start();
        System.in.read();
        thread.interrupt();

    }

    private static final class PublishTask implements Runnable {

        private final Channel channel;

        public PublishTask(Channel channel) {
            this.channel = channel;
        }

        public void run() {
            while (!Thread.interrupted()) {

                try {
                    String price = nextPrice();
                    System.out.println(price);
                    channel.basicPublish("", "stock.prices", null, price.getBytes());
                    Thread.sleep(500);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static String nextPrice() {
        String ticker = TICKER_SYMBOLS[RANDOM.nextInt(TICKER_SYMBOLS.length)];
        float price = RANDOM.nextFloat() * 5;
        return String.format("%s,%2" +
                ".2f", ticker, price);
    }
}
