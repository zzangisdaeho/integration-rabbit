package com.example.springintegrationrabbit.integration;


import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;

@MessagingGateway(
        defaultRequestChannel = "scheduler.admin.request",
        defaultReplyChannel = "scheduler.admin.response"
)
public interface TestGateWay {

    @Payload("new java.util.Date()")
    Object schedulerInfo();
}
