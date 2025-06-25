package com.example.logservice.controller;

import com.example.model.common.R;
import com.example.logservice.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/log")
@RestController
public class LogController {
    @Autowired
    private LogService logService;

} 