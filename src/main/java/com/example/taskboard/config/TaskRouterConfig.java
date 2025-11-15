package com.example.taskboard.config;

import com.example.taskboard.web.functional.TaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class TaskRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> taskRoutes(TaskHandler handler) {
        return RouterFunctions.nest(
                path("/fn/tasks"),
                RouterFunctions.route()
                        .GET("", handler::getAllTasks)
                        .GET("/paged", handler::getAllTasksPaged)
                        .GET("/{id}", handler::getTaskById)
                        .POST("", handler::createTask)
                        .PUT("/{id}", handler::updateTask)
                        .DELETE("/{id}", handler::deleteTask)
                        .build()
        );
    }
}
