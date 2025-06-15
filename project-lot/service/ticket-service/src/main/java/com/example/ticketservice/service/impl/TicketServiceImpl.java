package com.example.ticketservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.ticket.Ticket;
import com.example.ticketservice.mapper.TicketMapper;
import com.example.ticketservice.service.TicketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements TicketService {
} 