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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { getDeviceList } from '@/api/deviceApi'
import type { ResponseDTO } from '@/common/responseDTO'
const ticketChartRef = ref<HTMLElement>()
const deviceChartRef = ref<HTMLElement>()
const deviceList = ref()

onMounted(async () => {
  deviceList.value = await getDeviceList().then((res) => {
    return (res as ResponseDTO).data.data
  })
  // 初始化工单趋势图表
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
        data: [12, 15, 10, 14, 9, 6, 8],
        areaStyle: {
          opacity: 0.1,
        },
      },
    ],
  })

  // 初始化设备状态分布图表
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

  // 监听窗口大小变化，重绘图表
  window.addEventListener('resize', () => {
    ticketChart.resize()
    deviceChart.resize()
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