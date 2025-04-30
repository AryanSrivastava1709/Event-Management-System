package com.event.payment_service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class WebController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/payment-success")
    public String paymentSuccess() {
        return "payment-success";
    }
    
    
    @GetMapping("/test")
    public String test(Model model) {
        model.addAttribute("message", "This is a test");
        return "test";
    }
}
