package ru.example.sword.workshop.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.SlidingWindows;
import org.apache.kafka.streams.kstream.Suppressed;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.example.sword.delivery.event.DeliveryEvent;
import ru.example.sword.delivery.model.DeliveryState;
import ru.example.sword.tradeshop.event.TradeshopEvent;
import ru.example.sword.workshop.event.WorkshopEvent;
import ru.example.sword.workshop.model.ProductState;
import ru.example.sword.workshop.service.Blacksmith;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
@Slf4j
public class StreamConfiguration {

    private final Blacksmith blacksmith;

    public StreamConfiguration(Blacksmith blacksmith) {
        this.blacksmith = blacksmith;
    }

    @Bean
    @SuppressWarnings("unchecked")
    public Function<KStream<UUID, TradeshopEvent>, KStream<UUID, WorkshopEvent>[]> processOrder() {
        return input -> input
                .peek((key, value) -> log.info("Incoming order event {} {}",
                        value.getOrder().getId(), value.getOrder().getName()))
                .mapValues(TradeshopEvent::getOrder)
                .mapValues(blacksmith::processOrder)
                .mapValues(WorkshopEvent::new)
                .branch(
                        (key, value) -> true,
                        //просто для примера, что мы можем сразу отправлять в несколько топиков разные сообщения
                        (key, value) -> ProductState.BROKEN == value.getProduct().getState());
    }

    @Bean
    public Consumer<KStream<UUID, DeliveryEvent>> processDelivery() {
        return input -> input
                .filter((key, value) -> value.getProduct().getState() == DeliveryState.LOST)
                .peek((key, value) -> blacksmith.decreaseStatistic());
    }

    @Bean
    public Consumer<KStream<UUID, TradeshopEvent>> statistic() {
        return input -> input
                .groupBy((key, value) -> value.getOrder().getName())
                .windowedBy(SlidingWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(30)))
                .count(Materialized.with(Serdes.String(), Serdes.Long()))
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .toStream()
                .peek((key, value) -> log.info("Statistic received {} {} times last 30 seconds", key, value));
    }

}
