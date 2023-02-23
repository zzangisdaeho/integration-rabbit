package com.example.springintegrationrabbit.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitOutbound {

    private final RabbitTemplate rabbitTemplate;

    @ServiceActivator(inputChannel = "scheduler.admin.info.rabbit", outputChannel = "scheduler.admin.aggregator")
    public Message<?> processRabbitInfoMessage(Message<?> message) {
        String requestMessage = (String) message.getPayload();

        // Send the request message to RabbitMQ and receive the response message
        org.springframework.amqp.core.Message returnMessage = rabbitTemplate.sendAndReceive(
                "ws.exchange-topic.schedule.v0", "rabbit.exchange.binding",
                MessageBuilder.withBody(requestMessage.getBytes()).build()
        );

        String responseMessage = new String(returnMessage.getBody());

        // Create a new message with the response payload and headers
        Message<String> response = org.springframework.messaging.support.MessageBuilder
                .withPayload(responseMessage)
                .copyHeaders(message.getHeaders())
                .setHeader("from", "queue")
                .build();

        return response;
    }

    @ServiceActivator(inputChannel = "scheduler.admin.info.schedule", outputChannel = "scheduler.admin.aggregator")
    public Message<?> processSchedulInfoMessage(Message<?> message) {
        String requestMessage = (String) message.getPayload();

        // Send the request message to RabbitMQ and receive the response message
        org.springframework.amqp.core.Message returnMessage = rabbitTemplate.sendAndReceive(
                "ws.exchange-topic.schedule.v0", "schedule.list",
                org.springframework.amqp.core.MessageBuilder.withBody(requestMessage.getBytes()).build()
        );

        String responseMessage = new String(returnMessage.getBody());

        // Create a new message with the response payload and headers
        Message<String> response = org.springframework.messaging.support.MessageBuilder
                .withPayload(responseMessage)
                .copyHeaders(message.getHeaders())
                .setHeader("from", "schedule")
                .build();

        return response;
    }
}
