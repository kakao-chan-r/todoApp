package com.kakaoix.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TodoController {
    @GetMapping("/")
    public String index() {return "index";}
}
