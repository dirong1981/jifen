package com.gljr.jifen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class EchoController {

    @GetMapping
    public String ok() {
        return "Status: OK";
    }

}
