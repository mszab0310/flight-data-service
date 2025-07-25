package com.mali.flight.kafka.producer;

import com.mali.flight.opensky.dto.AirplaneData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AirplaneDataProducer {

    @Autowired
    private KafkaTemplate<String, AirplaneData> kafkaTemplate;

    public void send(String topic, AirplaneData payload) {
        log.info("Publishing data to topic {} with data length of {} and timestamp of {}", topic, payload.getAirplaneStates().size(), payload.getTimeStamp());
        kafkaTemplate.send(topic, payload);
    }
}
