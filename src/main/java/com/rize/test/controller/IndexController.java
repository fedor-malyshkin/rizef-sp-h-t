package com.rize.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "<a href='/swagger-ui.html'>swagger is here</a>" +
                "<br/>" +
                "<a href='/actuator/health'>'health' actuator</a>";
    }
}