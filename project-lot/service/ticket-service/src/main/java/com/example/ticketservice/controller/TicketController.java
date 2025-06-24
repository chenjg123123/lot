package com.example.ticketservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.model.common.R;
import com.example.model.ticket.Tickets;
import com.example.ticketservice.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/ticket")
@RestController
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/list")
    public R list() {
        return R.success(ticketService.list());
    }

    @GetMapping("/twoweek")
    public R twoweek() {
        return R.success(ticketService.twoweek());
    }

    @GetMapping("/weekuse")
    public R weekUse() {
        return R.success(ticketService.weekUse());
    }

    @GetMapping("/weekstatus")
    public R weekStatus() {
        return R.success(ticketService.weekStatus());
    }
    @GetMapping("/weekticketeffiency")
    public R weekTicketEffiency() {
        return R.success(ticketService.weekTicketEffiency());
    }

} 