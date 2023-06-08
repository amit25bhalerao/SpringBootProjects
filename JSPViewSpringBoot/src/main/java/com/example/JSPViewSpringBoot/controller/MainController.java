package com.example.JSPViewSpringBoot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping("/home")
    public String welcomePage(){
        System.out.println("Welcome to Home Page!");
        return "home";
    }

    @RequestMapping("/inner")
    public String innerPage(){
        System.out.println("Welcome to Inner Page!");
        return "inner";
    }
}
