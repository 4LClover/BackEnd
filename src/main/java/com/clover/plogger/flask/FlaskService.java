package com.clover.plogger.flask;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class FlaskService {

    //For translation
    private final ObjectMapper objectMapper;

    @Transactional
    public String sendToFlask(RequestSendToFlaskDto dto) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        //Set headers JSON
        HttpHeaders headers = new HttpHeaders();

        //dto to JSON
        headers.setContentType(MediaType.APPLICATION_JSON);

        String param = objectMapper.writeValueAsString(dto);

        HttpEntity<String> entity = new HttpEntity<>(param, headers);

        //Flask server url
        String url = "http://awesome-ai/image_verify";

        //Return response from flask server
        return restTemplate.postForObject(url, entity, String.class);
    }
}