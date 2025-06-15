package com.example.ticketservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.ticket.Ticket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TicketMapper extends BaseMapper<Ticket> {
} 