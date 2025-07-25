package com.mali.flight.kafka.config;

import com.mali.flight.opensky.dto.AirplaneData;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import static com.mali.flight.kafka.constants.KafkaConstants.AIRPLANE_DATA_TOPIC;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public NewTopic airplaneDataTopic(){
        return new NewTopic(AIRPLANE_DATA_TOPIC,1,(short) 1);
    }
}
