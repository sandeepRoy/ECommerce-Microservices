package com.ecms.configserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    public AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    public Environment environment;

    @GetMapping("/see")
    public ResponseEntity<String> get() {
        return new ResponseEntity<>("DATABASE_USERNAME: " + environment.getProperty("spring.datasource.username"), HttpStatus.OK);
    }
}
