package com.mali.flight.ingest;

import com.mali.flight.kafka.producer.AirplaneDataProducer;
import com.mali.flight.opensky.client.OpenSkyRestClient;
import com.mali.flight.opensky.dto.AirplaneData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static com.mali.flight.kafka.constants.KafkaConstants.AIRPLANE_DATA_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightDataIngestionService {

    private final OpenSkyRestClient openSkyClient;
    private final AirplaneDataProducer kafkaProducer;

    /**
     * Polls the opensky API every 6 seconds and publishes it to the corresponding KAFKA topic
     * */
    @Scheduled(fixedRate = 6000L)
    public void pollAndProduce() {
        AirplaneData data = openSkyClient.getAllAirplanes();
        if (data != null) {
            kafkaProducer.send(AIRPLANE_DATA_TOPIC, data);
        } else {
            log.warn("Received null data from OpenSky API, nothing to publish to topic");
        }
    }
}
