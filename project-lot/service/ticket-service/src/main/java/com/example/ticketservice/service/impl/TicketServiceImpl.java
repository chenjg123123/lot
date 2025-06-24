package com.example.ticketservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.ticket.Tickets;
import com.example.service.util.DateTransUtil;
import com.example.ticketservice.mapper.TicketMapper;
import com.example.ticketservice.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Tickets> implements TicketService {
    @Autowired
    private DateTransUtil dateTransUtil;

    /**
     * 获取两周的总订单数
     * @return
     */
    @Override
    public Integer[] twoweek() {
        return new Integer[]{this.baseMapper.countWeekBeforeLastDevices(), this.baseMapper.countThisDevicesWeek()};
    }
    /**
     * 获取周的工单数
     * @return
     */
    @Override
    public List<Map<String, Object>> weekUse() {
        return dateTransUtil.convertDateToWeek(this.baseMapper.countOrdersByDayThisWeek());
    }

    /**
     * 周工单状态分布
     * @return
     */
    @Override
    public List<Map<String, Object>> weekStatus() {
        return this.baseMapper.weekStatus();
    }

    @Override
    public List<Map<String, Object>> weekTicketEffiency() {
        return this.baseMapper.weekCompletedStatus();
    }


}