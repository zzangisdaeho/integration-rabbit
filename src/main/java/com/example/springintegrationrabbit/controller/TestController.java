package com.example.springintegrationrabbit.controller;

import com.example.springintegrationrabbit.integration.TestGateWay;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestGateWay testGateWay;

    @GetMapping("/test")
    public Object testController(){
        Object o = testGateWay.schedulerInfo();

        System.out.println("o = " + o);

        return o;
    }
}
