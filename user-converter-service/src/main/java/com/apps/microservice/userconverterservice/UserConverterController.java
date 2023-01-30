package com.apps.microservice.userconverterservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(value = "/api")
public class UserConverterController {

    @Autowired
    private Proxy proxy;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping(path = "/transformUsers", produces = MediaType.APPLICATION_XML_VALUE)
    @CircuitBreaker(name = "user-converter", fallbackMethod = "defaultMethod")
    public List<Root> converter() throws IOException {
        String response = proxy.retrieveExchangeValue();
        return objectMapper.readValue(response, new TypeReference<List<Root>>() {
        });
    }

    public String defaultMethod(Exception exception) {
        return "API is currently down!!";
    }
}
