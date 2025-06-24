package com.example.ticketservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.ticket.Tickets;

import java.util.List;
import java.util.Map;


public interface TicketService extends IService<Tickets> {
    Integer[] twoweek();
    List<Map<String,Object>> weekUse();
    List<Map<String,Object>> weekStatus();
    List<Map<String,Object>> weekTicketEffiency();
}