package com.mali.flight.ingest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mali.flight.kafka.producer.AirplaneDataProducer;
import com.mali.flight.opensky.client.OpenSkyRestClient;
import com.mali.flight.opensky.dto.AirplaneData;
import com.mali.flight.opensky.dto.AirplaneState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mali.flight.kafka.constants.KafkaConstants.AIRPLANE_DATA_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightDataIngestionService {
    private static final int BATCH_SIZE = 1000;

    private final OpenSkyRestClient openSkyClient;
    private final AirplaneDataProducer kafkaProducer;

    /**
     * Polls the opensky API every 6 seconds and publishes it to the corresponding KAFKA topic
     */
    @Scheduled(fixedRate = 6000L)
    public void pollAndProduce() throws JsonProcessingException {
        AirplaneData data = openSkyClient.getAllAirplanes();
        if (data != null) {
            batchAndSendData(data);
        } else {
            log.warn("Received null data from OpenSky API, nothing to publish to topic");
        }
    }

    /**
     * Splits and publishes airplane data on corresponding kafka topic
     */
    private void batchAndSendData(AirplaneData data) {
        List<AirplaneState> states = data.getAirplaneStates();
        if (states == null || states.isEmpty()) {
            return;
        }
        for (int i = 0; i < states.size(); i += BATCH_SIZE) {
            List<AirplaneState> chunk = states.subList(i, Math.min(i + BATCH_SIZE, states.size()));
            AirplaneData chunkedData = new AirplaneData();
            chunkedData.setTimeStamp(data.getTimeStamp());
            chunkedData.setAirplaneStates(chunk);
            log.info("Sending batch {}/{} with {} airplane states",
                    (i / BATCH_SIZE) + 1, (states.size() + BATCH_SIZE - 1) / BATCH_SIZE, chunk.size());
            kafkaProducer.send(AIRPLANE_DATA_TOPIC, chunkedData);
        }
    }
}
