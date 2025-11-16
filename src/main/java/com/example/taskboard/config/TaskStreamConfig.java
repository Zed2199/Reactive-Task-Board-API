package com.example.taskboard.config;

import com.example.taskboard.web.event.TaskEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class TaskStreamConfig {

    @Bean
    public Sinks.Many<TaskEvent> taskEventSink() {
        // multicast = many subscribers, each gets all events from subscription time
        // onBackpressureBuffer = buffer if subscribers are slower than producers
        return Sinks.many().multicast().onBackpressureBuffer();
    }

}
