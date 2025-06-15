<template>
  <div class="dashboard-container">
    <!-- 顶部数据卡片 -->
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
            <div class="number">128</div>
            <div class="trend">
              <span class="up">↑ 12%</span>
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
            <div class="number">24</div>
            <div class="trend">
              <span class="down">↓ 8%</span>
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
            <div class="number">5</div>
            <div class="trend">
              <span class="up">↑ 3%</span>
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
            <div class="number">98.5%</div>
            <div class="trend">
              <span class="up">↑ 0.5%</span>
              <span class="text">较上周</span>
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
              <span>设备状态分布</span>
            </div>
          </template>
          <div ref="deviceStatusChartRef"
               class="chart"></div>
        </el-card>
      </el-col>

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

// 图表引用
const deviceStatusChartRef = ref<HTMLElement>()
const ticketTypeChartRef = ref<HTMLElement>()
const alertTrendChartRef = ref<HTMLElement>()
const deviceOnlineChartRef = ref<HTMLElement>()
const ticketEfficiencyChartRef = ref<HTMLElement>()

onMounted(() => {
  // 初始化设备状态分布图表
  const deviceStatusChart = echarts.init(deviceStatusChartRef.value!)
  deviceStatusChart.setOption({
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
        data: [
          { value: 120, name: '在线' },
          { value: 5, name: '离线' },
          { value: 3, name: '故障' },
        ],
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

  // 初始化工单类型分布图表
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
        data: [
          { value: 35, name: '设备故障' },
          { value: 25, name: '设备维护' },
          { value: 15, name: '系统问题' },
          { value: 5, name: '其他' },
        ],
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

  // 初始化告警趋势图表
  const alertTrendChart = echarts.init(alertTrendChartRef.value!)
  alertTrendChart.setOption({
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
        name: '告警数',
        type: 'line',
        smooth: true,
        data: [5, 7, 3, 6, 4, 2, 3],
        areaStyle: {
          opacity: 0.1,
        },
      },
    ],
  })

  // 初始化设备在线趋势图表
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
        data: [98, 97, 99, 98, 97, 98, 98.5],
        areaStyle: {
          opacity: 0.1,
        },
      },
    ],
  })

  // 初始化工单处理效率图表
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
      data: ['设备故障', '设备维护', '系统问题', '其他'],
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
        data: [4, 2, 6, 3],
      },
      {
        name: '工单数量',
        type: 'line',
        yAxisIndex: 1,
        data: [35, 25, 15, 5],
      },
    ],
  })

  // 监听窗口大小变化，重绘图表
  window.addEventListener('resize', () => {
    deviceStatusChart.resize()
    ticketTypeChart.resize()
    alertTrendChart.resize()
    deviceOnlineChart.resize()
    ticketEfficiencyChart.resize()
  })
})
</script>

<style scoped lang="scss">
.dashboard-container {
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