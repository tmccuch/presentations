package com.rabbitmq.examples.stock.consumer;

import com.rabbitmq.client.QueueingConsumer;

final class ConsumerTask implements Runnable {
    private final QueueingConsumer consumer;

    public ConsumerTask(QueueingConsumer consumer) {
        this.consumer = consumer;
    }

    public void run() {
        while(!Thread.interrupted()) {
            try {
                QueueingConsumer.Delivery delivery = this.consumer.nextDelivery();
                String message = new String(delivery.getBody());
                System.out.println(message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
