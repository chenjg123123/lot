package com.example.ticketservice.controller;

import com.example.model.common.R;
import com.example.model.ticket.Ticket;
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

    @PostMapping("/add")
    public R add(@RequestBody Ticket ticket) {
        return R.success(ticketService.save(ticket));
    }

    @PutMapping("/update")
    public R update(@RequestBody Ticket ticket) {
        return R.success(ticketService.updateById(ticket));
    }

    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable Long id) {
        return R.success(ticketService.removeById(id));
    }
} 