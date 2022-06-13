package com.sapient.backend.fixures;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.sapient.backend.model.WeatherApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class WeatherPredictionDataFixture {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherPredictionDataFixture.class);

    public static Optional<WeatherApiResponse> getWeatherApiResponseStub() {

        String sampleRawApiDataContent = readFile("src/test/resources/sample-data.json");
        return convertJsonToClass(sampleRawApiDataContent, WeatherApiResponse.class);
    }

    public static String readFile(String location) {

        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream
                     = Files.lines(Paths.get(location),
                StandardCharsets.UTF_8)) {
            //Read the content with Stream
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileContent = contentBuilder.toString();
        return fileContent;
    }

    public static <T> Optional<T> convertJsonToClass(String jsonString, Class<T> clazz) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

            return Optional.of(mapper.readValue(jsonString, clazz));
        } catch (JsonProcessingException e) {
            LOG.error("Unable to parse the JSON:{}", e.getMessage());
            return Optional.empty();
        }
    }
}
