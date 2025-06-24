<template>
  <div class="home-container">
    <!-- 数据概览卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover"
                 class="data-card">
          <template #header>
            <div class="card-header">
              <span>设备总数</span>
              <el-tag type="success">在线</el-tag>
            </div>
          </template>
          <div class="card-content">
            <div class="number">{{ deviceAll }}</div>
            <div class="trend">
              <span :class="deviceRate.type">{{deviceRate.type == 'up'?'↑':'↓'}} {{deviceRate.value * 100}}%</span>
              <span class="text">较上周</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover"
                 class="data-card">
          <template #header>
            <div class="card-header">
              <span>工单总数</span>
              <el-tag type="warning">待处理</el-tag>
            </div>
          </template>
          <div class="card-content">
            <div class="number">{{ ticketAll }}</div>
            <div class="trend">
              <span :class=ticketRate.type>{{ticketRate.type == 'up'?'↑':'↓'}} {{ticketRate.value * 100}}%</span>
              <span class="text">较上周</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover"
                 class="data-card">
          <template #header>
            <div class="card-header">
              <span>告警总数</span>
              <el-tag type="danger">未处理</el-tag>
            </div>
          </template>
          <div class="card-content">
            <div class="number">{{notificationAll}}</div>
            <div class="trend">
              <span :class=notificationRate.type>{{notificationRate.type == 'up'?'↑':'↓'}} {{notificationRate.value * 100}}%</span>
              <span class="text">较上周</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover"
                 class="data-card">
          <template #header>
            <div class="card-header">
              <span>设备在线率</span>
              <el-tag type="info">实时</el-tag>
            </div>
          </template>
          <div class="card-content">
            <div class="number">{{deviceOnlineRate * 100}}%</div>
            <div class="trend">
              <span class="up"></span>
              <span class="text"></span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20"
            class="chart-row">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>工单趋势</span>
            </div>
          </template>
          <div ref="ticketChartRef"
               class="chart"></div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>设备状态分布</span>
            </div>
          </template>
          <div ref="deviceChartRef"
               class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 图表区域 -->

    <el-row :gutter="20"
            class="chart-row">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>告警趋势</span>
            </div>
          </template>
          <div ref="alertTrendChartRef"
               class="chart"></div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>设备在线趋势</span>
            </div>
          </template>
          <div ref="deviceOnlineChartRef"
               class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20"
            class="chart-row">

      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>工单类型分布</span>
            </div>
          </template>
          <div ref="ticketTypeChartRef"
               class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20"
            class="chart-row">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>工单处理效率</span>
            </div>
          </template>
          <div ref="ticketEfficiencyChartRef"
               class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { getTwoweek, getOnline, getStatusCount, getDeviceOline } from '@/api/deviceApi'
import type { ResponseDTO } from '@/common/responseDTO'
import { getTicketAll, getticketOneWeek, getWeekStatus, getWeekeffient } from '@/api/ticketApi'
import { getNotificationTwoweek, getNotificationOneWeek } from '@/api/notificationApi'
const ticketChartRef = ref<HTMLElement>()
const deviceChartRef = ref<HTMLElement>()
const deviceAll = ref(0)
const ticketAll = ref(0)
const notificationAll = ref(0)
const deviceRate = ref({ value: 0, type: 'up' })
const ticketRate = ref({ value: 0, type: 'up' })
const notificationRate = ref({ value: 0, type: 'up' })
const ticketTypeChartRef = ref<HTMLElement>()
const alertTrendChartRef = ref<HTMLElement>()
const deviceOnlineChartRef = ref<HTMLElement>()
const ticketEfficiencyChartRef = ref<HTMLElement>()
const deviceOnlineRate = ref(0)
onMounted(async () => {
  deviceAll.value = await getTwoweek().then((res) => {
    deviceRate.value.value = (res.data.data[0] / (res.data.data[1] - res.data.data[0])).toFixed(2)
    return (res as ResponseDTO).data.data[1]
  })

  ticketAll.value = await getTicketAll().then((res) => {
    ticketRate.value.value =
      res.data.data[0] > res.data.data[1]
        ? 1
        : (res.data.data[1] / (res.data.data[0] - res.data.data[1])).toFixed(2)
    ticketRate.value.type = res.data.data[0] - res.data.data[1] ? 'down' : 'up'
    return (res as ResponseDTO).data.data[1]
  })
  notificationAll.value = await getNotificationTwoweek().then((res) => {
    notificationRate.value.value =
      res.data.data[0] > res.data.data[1]
        ? 1
        : (res.data.data[1] / (res.data.data[0] - res.data.data[1])).toFixed(2)
    notificationRate.value.type = res.data.data[0] - res.data.data[1] ? 'down' : 'up'
    return (res as ResponseDTO).data.data[1]
  })
  deviceOnlineRate.value = await getOnline().then((res) => {
    const onlineCount = (res as ResponseDTO).data.data
    const rate = onlineCount / deviceAll.value
    return parseFloat(rate.toFixed(2))
  })

  // 初始化工单趋势图表
  const ticketChartData = (await getticketOneWeek()) as ResponseDTO
  const ticketChart = echarts.init(ticketChartRef.value!)
  ticketChart.setOption({
    tooltip: {
      trigger: 'axis',
    },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
    },
    yAxis: {
      type: 'value',
    },
    series: [
      {
        name: '工单数',
        type: 'line',
        smooth: true,
        data: ticketChartData.data.data.map((res) => res.order_count),
        areaStyle: {
          opacity: 0.1,
        },
      },
    ],
  })

  // 初始化设备状态分布图表
  const deviceChartData = await getStatusCount()
  const handleOnline = (param: string) => {
    return param === 'error' ? '故障' : param === 'online' ? '在线' : '离线'
  }
  const deviceChart = echarts.init(deviceChartRef.value!)
  deviceChart.setOption({
    tooltip: {
      trigger: 'item',
    },
    legend: {
      orient: 'vertical',
      left: 'left',
    },
    series: [
      {
        name: '设备状态',
        type: 'pie',
        radius: '50%',
        data: deviceChartData.data.data.map((res) => ({
          value: res.num,
          name: handleOnline(res.status),
        })),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)',
          },
        },
      },
    ],
  })
  //工单类型
  const ticketTypeData = (await getWeekStatus()) as ResponseDTO
  const ticketTypeChart = echarts.init(ticketTypeChartRef.value!)
  ticketTypeChart.setOption({
    tooltip: {
      trigger: 'item',
    },
    legend: {
      orient: 'vertical',
      left: 'left',
    },
    series: [
      {
        name: '工单类型',
        type: 'pie',
        radius: '50%',
        data: ticketTypeData.data.data.map((res) => ({
          value: res.count,
          name: res.type,
        })),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)',
          },
        },
      },
    ],
  })
  const notificationData = (await getNotificationOneWeek()) as ResponseDTO
  const alertTrendChart = echarts.init(alertTrendChartRef.value!)
  alertTrendChart.setOption({
    tooltip: {
      trigger: 'axis',
    },
    xAxis: {
      type: 'category',
      data: notificationData.data.data.map((res) => res.week),
    },
    yAxis: {
      type: 'value',
    },
    series: [
      {
        name: '告警数',
        type: 'line',
        smooth: true,
        data: notificationData.data.data.map((res) => res.order_count),
        areaStyle: {
          opacity: 0.1,
        },
      },
    ],
  })

  // 初始化设备在线趋势图表
  const deviceOnlineData = (await getDeviceOline()) as ResponseDTO
  const deviceOnlineChart = echarts.init(deviceOnlineChartRef.value!)
  deviceOnlineChart.setOption({
    tooltip: {
      trigger: 'axis',
    },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '{value}%',
      },
    },
    series: [
      {
        name: '在线率',
        type: 'line',
        smooth: true,
        data: deviceOnlineData.data.data.map((res) => res.online_rate),
        areaStyle: {
          opacity: 0.1,
        },
      },
    ],
  })

  // 初始化工单处理效率图表
  const ticketEfficientData = (await getWeekeffient()) as ResponseDTO
  const ticketEfficiencyChart = echarts.init(ticketEfficiencyChartRef.value!)
  ticketEfficiencyChart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow',
      },
    },
    legend: {
      data: ['平均处理时间', '工单数量'],
    },
    xAxis: {
      type: 'category',
      data: ticketEfficientData.data.data.map((res) => res.type),
    },
    yAxis: [
      {
        type: 'value',
        name: '处理时间',
        axisLabel: {
          formatter: '{value} 小时',
        },
      },
      {
        type: 'value',
        name: '工单数',
        axisLabel: {
          formatter: '{value} 个',
        },
      },
    ],
    series: [
      {
        name: '平均处理时间',
        type: 'bar',
        data: ticketEfficientData.data.data.map((res) => res.avgTime),
      },
      {
        name: '工单数量',
        type: 'line',
        yAxisIndex: 1,
        data: ticketEfficientData.data.data.map((res) => res.num),
      },
    ],
  })

  // 监听窗口大小变化，重绘图表
  window.addEventListener('resize', () => {
    alertTrendChart.resize()
    deviceOnlineChart.resize()
    ticketChart.resize()
    deviceChart.resize()
    ticketEfficiencyChart.resize()
  })
})
</script>

<style scoped lang="scss">
.home-container {
  .data-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .card-content {
      text-align: center;

      .number {
        font-size: 28px;
        font-weight: bold;
        color: #303133;
        margin: 10px 0;
      }

      .trend {
        font-size: 14px;
        color: #909399;

        .up {
          color: #67c23a;
          margin-right: 8px;
        }

        .down {
          color: #f56c6c;
          margin-right: 8px;
        }
      }
    }
  }

  .chart-row {
    margin-top: 20px;

    .chart {
      height: 300px;
    }
  }
}
</style> 