package com.incede.nbfc.gateway.config;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnErrorEvent;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that registers a listener for CircuitBreaker lifecycle events.
 * <p>
 * This class integrates with Resilience4j's {@link CircuitBreaker} registry and listens to:
 * <ul>
 *     <li><b>Entry Added</b>: When a new CircuitBreaker is created and registered, we attach an event listener
 *     to log important events such as state transitions and errors.</li>
 *     <li><b>Entry Removed</b>: Logs when a CircuitBreaker is explicitly removed from the registry.</li>
 *     <li><b>Entry Replaced</b>: Logs when a CircuitBreaker is replaced with a new instance in the registry.</li>
 * </ul>
 *
 * Purpose: Provides centralized logging and monitoring of CircuitBreaker activity in the application.
 */
@Slf4j
@Configuration
public class CircuitBreakerEventLogging {


    /**
     * Creates a {@link RegistryEventConsumer} bean for {@link CircuitBreaker} events.
     * <p>
     * The consumer reacts to three types of lifecycle changes:
     * <ul>
     *     <li>When a new CircuitBreaker is added, subscribes to its event publisher and logs state changes, errors,
     *     and other events.</li>
     *     <li>When a CircuitBreaker is removed, logs its removal.</li>
     *     <li>When a CircuitBreaker is replaced, logs its replacement.</li>
     * </ul>
     *
     * @return a configured {@link RegistryEventConsumer} for CircuitBreaker
     */
    @Bean
    public RegistryEventConsumer<CircuitBreaker> circuitBreakerEventConsumer() {
        return new RegistryEventConsumer<CircuitBreaker>() {

            /**
             * Triggered when a new CircuitBreaker is added to the registry.
             * Attaches an event listener to log state transitions, errors, and generic events.
             *
             * @param entryAddedEvent event representing the addition of a new CircuitBreaker
             */
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
                CircuitBreaker circuitBreaker = entryAddedEvent.getAddedEntry();
                circuitBreaker.getEventPublisher()
                        .onEvent(event -> {
                            if (event instanceof CircuitBreakerOnStateTransitionEvent) {
                                CircuitBreakerOnStateTransitionEvent stateEvent = (CircuitBreakerOnStateTransitionEvent) event;
                                log.info("CircuitBreaker '{}' state changed from {} to {}",
                                        circuitBreaker.getName(),
                                        stateEvent.getStateTransition().getFromState(),
                                        stateEvent.getStateTransition().getToState());
                            } else if (event instanceof CircuitBreakerOnErrorEvent) {
                                CircuitBreakerOnErrorEvent errorEvent = (CircuitBreakerOnErrorEvent) event;
                                log.warn("CircuitBreaker '{}' recorded error: {}",
                                        circuitBreaker.getName(),
                                        errorEvent.getThrowable().toString());
                            } else {
                                // Logs other less-specific events
                                log.info("CircuitBreaker '{}' event: {}", circuitBreaker.getName(), event.getEventType());
                            }
                        });
            }

            /**
             * Triggered when a CircuitBreaker is removed from the registry.
             *
             * @param entryRemovedEvent event representing the removal of a CircuitBreaker
             */
            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemovedEvent) {
                log.info("CircuitBreaker '{}' removed", entryRemovedEvent.getRemovedEntry().getName());
            }

            /**
             * Triggered when a CircuitBreaker is replaced with a new instance in the registry.
             *
             * @param entryReplacedEvent event representing the replacement of a CircuitBreaker
             */
            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
                log.info("CircuitBreaker '{}' replaced", entryReplacedEvent.getNewEntry().getName());
            }
        };
    }
}
