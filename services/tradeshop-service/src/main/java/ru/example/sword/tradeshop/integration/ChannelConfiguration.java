package ru.example.sword.tradeshop.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;

@Configuration
public class ChannelConfiguration {

    @Bean
    public DirectChannel inputOrder() {
        return new DirectChannel();
    }

    @Bean
    public PublishSubscribeChannel publishOrder() {
        return new PublishSubscribeChannel();
    }
}
