package com.example.demo.controller;


import com.example.demo.service.OcrService;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    OcrService service;

    @GetMapping
    public String demo() throws TesseractException, IOException {
        service.extractTextFromImage();
        return "Success";
    }
}
