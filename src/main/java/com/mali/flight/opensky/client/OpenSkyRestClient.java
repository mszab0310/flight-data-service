package com.mali.flight.opensky.client;

import com.mali.flight.opensky.dto.AirplaneData;
import com.mali.flight.opensky.dto.AirplaneState;
import com.mali.flight.opensky.dto.OpenSkyApiResponse;
import com.mali.flight.opensky.dto.OpenSkyToken;
import com.mali.flight.opensky.util.MappingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class OpenSkyRestClient {

    private static final int EXPIRY_THRESHOLD = 30;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String ALL_STATES_PATH = "/states/all";

    private final WebClient webClient;

    @Value("${OPENSKY_CLIENT_ID}")
    private String clientId;

    @Value("${OPENSKY_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${OPENSKY_TOKEN_URI}")
    private String tokenURI;

    @Value("${opensky.api.baseurl}")
    private String apiBaseUrl;

    private OpenSkyToken bearerToken;

    public OpenSkyRestClient() {
        webClient = WebClient.builder().codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)).build();
    }

    /**
     * Obtains a JWT access token based on the client id and client secret from the OpenSky API
     * The token expires in 30 minutes as of now
     * The expiry date can be found in the response object
     *
     * @return OpenSkyToken object
     */
    public OpenSkyToken getAccessToken() {
        log.info("Obtaining new JWT token for OpenSky API");
        OpenSkyToken token = webClient.post().uri(tokenURI)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters
                        .fromFormData("grant_type", "client_credentials")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret))
                .retrieve().bodyToMono(OpenSkyToken.class).block();
        if (token != null) {
            log.info("Obtained JWT token at {}", Instant.now());
            token.setExpiresAt(Instant.now().plusSeconds(token.getExpiresIn()));
        }
        return token;
    }

    /**
     * Fetches all available airplane data
     * Processes the response using an instance of {@link OpenSkyApiResponse} for the raw API response
     *
     * @return An object of type {@link AirplaneData}
     */
    public AirplaneData getAllAirplanes() {
        log.info("Fetching all airplane data");
        return webClient.get().uri(apiBaseUrl + ALL_STATES_PATH)
                .header(HttpHeaders.AUTHORIZATION, getOrRefreshToken()).retrieve().bodyToMono(OpenSkyApiResponse.class)
                .map(data -> {
                    AirplaneData res = new AirplaneData();
                    if (data.getSourceStates() != null && !data.getSourceStates().isEmpty()) {
                        List<AirplaneState> mappedStates = data.getSourceStates().stream().map(MappingUtil::getAirplaneStates).toList();
                        res.setAirplaneStates(mappedStates);
                    }
                    res.setTimeStamp(Instant.ofEpochSecond(data.getTimeStamp()));
                    return res;
                })
                .block();
    }

    /**
     * Returns the existing access token with the Bearer prefix if one exists and is not almost expired
     * Refetches the token otherwise
     *
     * @return The jwt with the Bearer prefix
     */
    private String getOrRefreshToken() {
        if (bearerToken == null || bearerToken.getExpiresAt().minusSeconds(EXPIRY_THRESHOLD).isBefore(Instant.now())) {
            log.info("Token is null or expired, refresh token procedure started");
            bearerToken = getAccessToken();
        }
        return BEARER_PREFIX + bearerToken.getAccessToken();
    }
}
