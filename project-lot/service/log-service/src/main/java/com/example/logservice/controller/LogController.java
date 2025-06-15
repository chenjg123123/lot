package com.example.logservice.controller;

import com.example.model.common.R;
import com.example.model.log.Log;
import com.example.logservice.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/log")
@RestController
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping("/list")
    public R list() {
        return R.success(logService.list());
    }

    @PostMapping("/add")
    public R add(@RequestBody Log log) {
        return R.success(logService.save(log));
    }

    @PutMapping("/update")
    public R update(@RequestBody Log log) {
        return R.success(logService.updateById(log));
    }

    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable Long id) {
        return R.success(logService.removeById(id));
    }
} 