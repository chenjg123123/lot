<template>
  <div class="alerts-container">
    <!-- 搜索和操作栏 -->
    <div class="operation-bar">
      <el-form :inline="true"
               :model="searchForm"
               class="search-form">
        <el-form-item label="告警编号">
          <el-input v-model="searchForm.alertNo"
                    placeholder="请输入告警编号"
                    clearable />
        </el-form-item>
        <el-form-item label="告警级别">
          <el-select v-model="searchForm.level"
                     placeholder="请选择级别"
                     clearable>
            <el-option label="一般"
                       value="normal" />
            <el-option label="重要"
                       value="important" />
            <el-option label="紧急"
                       value="urgent" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="searchForm.status"
                     placeholder="请选择状态"
                     clearable>
            <el-option label="未处理"
                       value="unhandled" />
            <el-option label="处理中"
                       value="handling" />
            <el-option label="已处理"
                       value="handled" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"
                     @click="handleSearch">
            <el-icon>
              <Search />
            </el-icon>
            搜索
          </el-button>
          <el-button @click="resetSearch">
            <el-icon>
              <Refresh />
            </el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <div class="operation-buttons">
        <el-button type="danger"
                   :disabled="!selectedAlerts.length"
                   @click="handleBatchHandle">
          <el-icon>
            <Check />
          </el-icon>
          批量处理
        </el-button>
      </div>
    </div>

    <!-- 告警列表 -->
    <el-table v-loading="loading"
              :data="alertList"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection"
                       width="55" />
      <el-table-column prop="alertNo"
                       label="告警编号"
                       width="180" />
      <el-table-column prop="title"
                       label="告警标题"
                       min-width="200" />
      <el-table-column prop="level"
                       label="告警级别"
                       width="100">
        <template #default="{ row }">
          <el-tag :type="getLevelType(row.level)">
            {{ getLevelText(row.level) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status"
                       label="状态"
                       width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="deviceName"
                       label="设备名称"
                       width="150" />
      <el-table-column prop="createTime"
                       label="告警时间"
                       width="180" />
      <el-table-column prop="handleTime"
                       label="处理时间"
                       width="180" />
      <el-table-column label="操作"
                       width="200"
                       fixed="right">
        <template #default="{ row }">
          <el-button-group>
            <el-button type="primary"
                       link
                       @click="handleView(row)">
              <el-icon>
                <View />
              </el-icon>
              查看
            </el-button>
            <el-button v-if="row.status === 'unhandled'"
                       type="success"
                       link
                       @click="handleAlert(row)">
              <el-icon>
                <Check />
              </el-icon>
              处理
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination v-model:current-page="currentPage"
                     v-model:page-size="pageSize"
                     :total="total"
                     :page-sizes="[10, 20, 50, 100]"
                     layout="total, sizes, prev, pager, next, jumper"
                     @size-change="handleSizeChange"
                     @current-change="handleCurrentChange" />
    </div>

    <!-- 处理告警对话框 -->
    <el-dialog v-model="dialogVisible"
               title="处理告警"
               width="500px">
      <el-form ref="handleFormRef"
               :model="handleForm"
               :rules="handleRules"
               label-width="100px">
        <el-form-item label="处理方式"
                      prop="handleType">
          <el-select v-model="handleForm.handleType"
                     placeholder="请选择处理方式">
            <el-option label="远程处理"
                       value="remote" />
            <el-option label="现场处理"
                       value="onsite" />
            <el-option label="忽略"
                       value="ignore" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理说明"
                      prop="handleDesc">
          <el-input v-model="handleForm.handleDesc"
                    type="textarea"
                    rows="4"
                    placeholder="请输入处理说明" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary"
                     @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { Search, Refresh, Check, View } from '@element-plus/icons-vue'

// 搜索表单
const searchForm = reactive({
  alertNo: '',
  level: '',
  status: '',
})

// 告警列表数据
const loading = ref(false)
const alertList = ref([
  {
    id: 1,
    alertNo: 'A202403150001',
    title: '温度传感器异常',
    level: 'urgent',
    status: 'unhandled',
    deviceName: '温度传感器-001',
    createTime: '2024-03-15 10:30:00',
    handleTime: '',
    description: '温度超过阈值，当前温度：45℃',
  },
])

// 分页
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)

// 选中的告警
const selectedAlerts = ref([])

// 对话框
const dialogVisible = ref(false)
const handleFormRef = ref<FormInstance>()
const handleForm = reactive({
  handleType: '',
  handleDesc: '',
})

// 表单验证规则
const handleRules: FormRules = {
  handleType: [{ required: true, message: '请选择处理方式', trigger: 'change' }],
  handleDesc: [{ required: true, message: '请输入处理说明', trigger: 'blur' }],
}

// 获取告警级别类型
const getLevelType = (level: string) => {
  const map: Record<string, string> = {
    normal: 'info',
    important: 'warning',
    urgent: 'danger',
  }
  return map[level] || 'info'
}

// 获取告警级别文本
const getLevelText = (level: string) => {
  const map: Record<string, string> = {
    normal: '一般',
    important: '重要',
    urgent: '紧急',
  }
  return map[level] || '未知'
}

// 获取状态类型
const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    unhandled: 'danger',
    handling: 'warning',
    handled: 'success',
  }
  return map[status] || 'info'
}

// 获取状态文本
const getStatusText = (status: string) => {
  const map: Record<string, string> = {
    unhandled: '未处理',
    handling: '处理中',
    handled: '已处理',
  }
  return map[status] || '未知'
}

// 搜索
const handleSearch = () => {
  // TODO: 实现搜索逻辑
  console.log('搜索条件：', searchForm)
}

// 重置搜索
const resetSearch = () => {
  searchForm.alertNo = ''
  searchForm.level = ''
  searchForm.status = ''
  handleSearch()
}

// 选择变化
const handleSelectionChange = (selection: any[]) => {
  selectedAlerts.value = selection
}

// 查看告警
const handleView = (row: any) => {
  // TODO: 实现查看详情逻辑
  console.log('查看告警：', row)
}

// 处理告警
const handleAlert = (row: any) => {
  dialogVisible.value = true
  handleForm.handleType = ''
  handleForm.handleDesc = ''
}

// 批量处理
const handleBatchHandle = () => {
  if (selectedAlerts.value.length === 0) {
    ElMessage.warning('请选择要处理的告警')
    return
  }

  ElMessageBox.confirm(`确认处理选中的 ${selectedAlerts.value.length} 个告警吗？`, '提示', {
    type: 'warning',
  }).then(() => {
    // TODO: 实现批量处理逻辑
    ElMessage.success('处理成功')
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!handleFormRef.value) return

  await handleFormRef.value.validate((valid) => {
    if (valid) {
      // TODO: 实现提交逻辑
      ElMessage.success('处理成功')
      dialogVisible.value = false
    }
  })
}

// 分页大小变化
const handleSizeChange = (val: number) => {
  pageSize.value = val
  // TODO: 重新加载数据
}

// 页码变化
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  // TODO: 重新加载数据
}
</script>

<style scoped lang="scss">
.alerts-container {
  .operation-bar {
    margin-bottom: 20px;
    display: flex;
    justify-content: space-between;
    align-items: flex-start;

    .search-form {
      flex: 1;
    }

    .operation-buttons {
      margin-left: 20px;
    }
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style> 