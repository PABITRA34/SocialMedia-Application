package com.example.SocialMedia.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
//@Tag(name = "Public APIs")
public class PublicController {

    @GetMapping("/health-check")
    public String healthCheck(){
        return "ok";
    }

}
