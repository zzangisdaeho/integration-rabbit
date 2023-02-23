package com.example.springintegrationrabbit.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ScheduleGatherProcess {

    public final static String REQUEST_QUEUE = "queueInfo";
    public final static String REQUEST_SCHEDULE = "scheduleInfo";


    @Splitter(inputChannel = "scheduler.admin.request", outputChannel = "scheduler.admin.route")
    public List<String> splitRequest(Message<?> message){
        return List.of(REQUEST_QUEUE, REQUEST_SCHEDULE);
    }

    @Router(inputChannel = "scheduler.admin.route")
    public String routeRequest(Message<?> message){
        Object requestTarget = message.getPayload();
        if(requestTarget.equals(REQUEST_QUEUE)){
            return "scheduler.admin.info.rabbit";
        }else if(requestTarget.equals(REQUEST_SCHEDULE)){
            return "scheduler.admin.info.schedule";
        }else{
            return "error.schedule.search";
        }
    }

    @Aggregator(inputChannel = "scheduler.admin.aggregator", outputChannel = "scheduler.admin.response")
    public Object aggregateOrderSearch(List<Message<?>> messages) {

        Map<String, Object> resultMap = new HashMap<>();

        for ( Message message: messages) {
            //
            if (message.getHeaders().get("from").equals("queue")) {
                resultMap.put("queue", message.getPayload());
            }
            else if (message.getHeaders().get("from").equals("schedule")) {
                resultMap.put("schedule", message.getPayload());
            }
        }
        return resultMap;
    }

    @Bean("scheduler.admin.response")
    public DirectChannel schedulerResponseChannel() {
        return new DirectChannel();
    }




}
