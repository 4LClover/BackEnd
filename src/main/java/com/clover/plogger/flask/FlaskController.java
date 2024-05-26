package com.clover.plogger.flask;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FlaskController {

    private final FlaskService flaskService;

    @PostMapping("/flask")
    public String sendToFlask(@RequestBody RequestSendToFlaskDto dto) throws JsonProcessingException {
        return flaskService.sendToFlask(dto);
    }
}