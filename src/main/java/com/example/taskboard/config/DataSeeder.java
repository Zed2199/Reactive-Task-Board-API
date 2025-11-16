package com.example.taskboard.config;

import com.example.taskboard.domain.Priority;
import com.example.taskboard.domain.Task;
import com.example.taskboard.domain.TaskStatus;
import com.example.taskboard.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final TaskRepository taskRepository;
    private final Random random = new Random();

    private static final List<String> TASK_TITLES = List.of(
            "Implement user authentication",
            "Fix login page responsiveness",
            "Add unit tests for service layer",
            "Update API documentation",
            "Refactor database queries",
            "Optimize application performance",
            "Design new dashboard layout",
            "Integrate payment gateway",
            "Review pull requests",
            "Deploy to production",
            "Set up CI/CD pipeline",
            "Migrate to microservices",
            "Add monitoring and logging",
            "Implement caching strategy",
            "Create user onboarding flow",
            "Fix security vulnerabilities",
            "Update dependencies",
            "Write integration tests",
            "Improve error handling",
            "Add email notifications",
            "Implement search functionality",
            "Create admin panel",
            "Add data validation",
            "Optimize database indexes",
            "Implement rate limiting",
            "Add API versioning",
            "Create backup strategy",
            "Implement WebSocket support",
            "Add internationalization",
            "Create mobile app version",
            "Implement SSO integration",
            "Add analytics tracking",
            "Optimize bundle size",
            "Implement lazy loading",
            "Add accessibility features",
            "Create user documentation",
            "Implement data export",
            "Add social media sharing",
            "Create landing page",
            "Implement dark mode"
    );

    private static final List<String> DESCRIPTION_TEMPLATES = List.of(
            "This task requires careful attention to detail and should be completed by the end of the sprint.",
            "High priority item that blocks other work. Need to address ASAP.",
            "Optional enhancement that would improve user experience.",
            "Technical debt that needs to be addressed when time permits.",
            "Customer requested feature with potential business impact.",
            "Security-related task that should be prioritized.",
            "Performance optimization to improve system responsiveness.",
            "Bug fix reported by multiple users in production.",
            "Refactoring task to improve code maintainability.",
            "Documentation update to help new team members.",
            "Infrastructure improvement for better scalability.",
            "Testing task to ensure quality standards.",
            "Design task requiring collaboration with UX team.",
            "Integration task requiring coordination with external systems.",
            "Maintenance task for keeping dependencies up to date."
    );

    @Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
            taskRepository.count()
                    .flatMapMany(count -> {
                        if (count > 0) {
                            log.info("Database already contains {} tasks. Skipping seeding.", count);
                            return Flux.empty();
                        }

                        log.info("Database is empty. Starting data seeding...");

                        List<Task> tasks = generateRandomTasks(50);

                        return taskRepository.saveAll(tasks)
                                .doOnComplete(() -> log.info("Successfully seeded {} tasks", tasks.size()))
                                .doOnError(error -> log.error("Error seeding database", error));
                    })
                    .subscribe();
        };
    }

    private List<Task> generateRandomTasks(int count) {
        return Flux.range(0, count)
                .map(i -> createRandomTask())
                .collectList()
                .block();
    }

    private Task createRandomTask() {
        TaskStatus status = randomEnum(TaskStatus.class);
        Priority priority = randomEnum(Priority.class);
        String title = randomElement(TASK_TITLES);
        String description = randomElement(DESCRIPTION_TEMPLATES);
        Instant dueDate = generateRandomDueDate();

        return Task.builder()
                .title(title)
                .description(description)
                .status(status)
                .priority(priority)
                .dueDate(dueDate)
                .build();
    }

    private <T extends Enum<T>> T randomEnum(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        return values[random.nextInt(values.length)];
    }

    private <T> T randomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    private Instant generateRandomDueDate() {
        // Generate due dates between 30 days ago and 60 days in the future
        int daysOffset = random.nextInt(91) - 30;
        return Instant.now().plus(daysOffset, ChronoUnit.DAYS);
    }
}
