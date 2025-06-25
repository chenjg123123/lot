<template>
  <div class="tickets-container">
    <!-- 搜索和操作栏 -->
    <div class="operation-bar">
      <el-form :inline="true"
               :model="searchForm"
               class="search-form">
        <el-form-item label="工单编号">
          <el-input v-model="searchForm.ticketNo"
                    placeholder="请输入工单编号"
                    clearable />
        </el-form-item>
        <el-form-item label="工单状态">
          <el-select v-model="searchForm.status"
                     placeholder="请选择状态"
                     clearable
                     style="width: 180px;">
            <el-option label="待处理"
                       value="pending" />
            <el-option label="处理中"
                       value="processing" />
            <el-option label="已完成"
                       value="completed" />
            <el-option label="已关闭"
                       value="closed" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="searchForm.priority"
                     placeholder="请选择优先级"
                     clearable
                     style="width: 180px;">
            <el-option label="低"
                       value="low" />
            <el-option label="中"
                       value="medium" />
            <el-option label="高"
                       value="high" />
            <el-option label="紧急"
                       value="urgent" />
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
        <el-button type="primary"
                   @click="handleCreate">
          <el-icon>
            <Plus />
          </el-icon>
          创建工单
        </el-button>
      </div>
    </div>

    <!-- 工单列表 -->
    <el-table v-loading="loading"
              :data="ticketList"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection"
                       width="55" />
      <el-table-column prop="ticketNo"
                       label="工单编号"
                       width="180" />
      <el-table-column prop="title"
                       label="工单标题"
                       min-width="200" />
      <el-table-column prop="status"
                       label="状态"
                       width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="priority"
                       label="优先级"
                       width="100">
        <template #default="{ row }">
          <el-tag :type="getPriorityType(row.priority)">
            {{ getPriorityText(row.priority) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="assignee"
                       label="处理人"
                       width="120" />
      <el-table-column prop="createTime"
                       label="创建时间"
                       width="180" />
      <el-table-column prop="updateTime"
                       label="更新时间"
                       width="180" />
      <el-table-column label="操作"
                       width="200"
                       fixed="right">
        <template #default="{ row }">
          <el-button-group>
            <el-button type="primary"
                       link
                       @click="handleEdit(row)">
              <el-icon>
                <Edit />
              </el-icon>
              编辑
            </el-button>
            <el-button type="primary"
                       link
                       @click="handleView(row)">
              <el-icon>
                <View />
              </el-icon>
              查看
            </el-button>
            <el-button v-if="row.status === 'pending'"
                       type="success"
                       link
                       @click="handleProcess(row)">
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

    <!-- 创建/编辑工单对话框 -->
    <el-dialog v-model="dialogVisible"
               :title="dialogType === 'create' ? '创建工单' : '编辑工单'"
               width="600px">
      <el-form ref="ticketFormRef"
               :model="ticketForm"
               :rules="ticketRules"
               label-width="100px">
        <el-form-item label="工单标题"
                      prop="title">
          <el-input v-model="ticketForm.title"
                    placeholder="请输入工单标题" />
        </el-form-item>
        <el-form-item label="工单类型"
                      prop="type">
          <el-select v-model="ticketForm.type"
                     placeholder="请选择工单类型">
            <el-option label="设备故障"
                       value="device_fault" />
            <el-option label="设备维护"
                       value="device_maintenance" />
            <el-option label="系统问题"
                       value="system_issue" />
            <el-option label="其他"
                       value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级"
                      prop="priority">
          <el-select v-model="ticketForm.priority"
                     placeholder="请选择优先级">
            <el-option label="低"
                       value="low" />
            <el-option label="中"
                       value="medium" />
            <el-option label="高"
                       value="high" />
            <el-option label="紧急"
                       value="urgent" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理人"
                      prop="assignee">
          <el-select v-model="ticketForm.assignee"
                     placeholder="请选择处理人">
            <el-option label="张三"
                       value="zhangsan" />
            <el-option label="李四"
                       value="lisi" />
            <el-option label="王五"
                       value="wangwu" />
          </el-select>
        </el-form-item>
        <el-form-item label="工单描述"
                      prop="description">
          <el-input v-model="ticketForm.description"
                    type="textarea"
                    rows="4"
                    placeholder="请输入工单描述" />
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
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { Search, Refresh, Plus, Edit, View, Check } from '@element-plus/icons-vue'

// 搜索表单
const searchForm = reactive({
  ticketNo: '',
  status: '',
  priority: '',
})

// 工单列表数据
const loading = ref(false)
const ticketList = ref([
  {
    id: 1,
    ticketNo: 'T202403150001',
    title: '温度传感器异常报警',
    status: 'pending',
    priority: 'high',
    assignee: '张三',
    createTime: '2024-03-15 10:30:00',
    updateTime: '2024-03-15 10:30:00',
    type: 'device_fault',
    description: '车间A区温度传感器显示异常，需要检查',
  },
])

// 分页
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)

// 选中的工单
const selectedTickets = ref([])

// 对话框
const dialogVisible = ref(false)
const dialogType = ref<'create' | 'edit'>('create')
const ticketFormRef = ref<FormInstance>()
const ticketForm = reactive({
  title: '',
  type: '',
  priority: '',
  assignee: '',
  description: '',
})

// 表单验证规则
const ticketRules: FormRules = {
  title: [
    { required: true, message: '请输入工单标题', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' },
  ],
  type: [{ required: true, message: '请选择工单类型', trigger: 'change' }],
  priority: [{ required: true, message: '请选择优先级', trigger: 'change' }],
  assignee: [{ required: true, message: '请选择处理人', trigger: 'change' }],
  description: [{ required: true, message: '请输入工单描述', trigger: 'blur' }],
}

// 获取状态类型
const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    pending: 'warning',
    processing: 'primary',
    completed: 'success',
    closed: 'info',
  }
  return map[status] || 'info'
}

// 获取状态文本
const getStatusText = (status: string) => {
  const map: Record<string, string> = {
    pending: '待处理',
    processing: '处理中',
    completed: '已完成',
    closed: '已关闭',
  }
  return map[status] || '未知'
}

// 获取优先级类型
const getPriorityType = (priority: string) => {
  const map: Record<string, string> = {
    low: 'info',
    medium: 'warning',
    high: 'danger',
    urgent: 'danger',
  }
  return map[priority] || 'info'
}

// 获取优先级文本
const getPriorityText = (priority: string) => {
  const map: Record<string, string> = {
    low: '低',
    medium: '中',
    high: '高',
    urgent: '紧急',
  }
  return map[priority] || '未知'
}

// 搜索
const handleSearch = () => {
  // TODO: 实现搜索逻辑
  console.log('搜索条件：', searchForm)
}

// 重置搜索
const resetSearch = () => {
  searchForm.ticketNo = ''
  searchForm.status = ''
  searchForm.priority = ''
  handleSearch()
}

// 选择变化
const handleSelectionChange = (selection: any[]) => {
  selectedTickets.value = selection
}

// 创建工单
const handleCreate = () => {
  dialogType.value = 'create'
  dialogVisible.value = true
  ticketForm.title = ''
  ticketForm.type = ''
  ticketForm.priority = ''
  ticketForm.assignee = ''
  ticketForm.description = ''
}

// 编辑工单
const handleEdit = (row: any) => {
  dialogType.value = 'edit'
  dialogVisible.value = true
  Object.assign(ticketForm, row)
}

// 查看工单
const handleView = (row: any) => {
  // TODO: 实现查看详情逻辑
  console.log('查看工单：', row)
}

// 处理工单
const handleProcess = (row: any) => {
  // TODO: 实现处理工单逻辑
  console.log('处理工单：', row)
}

// 提交表单
const handleSubmit = async () => {
  if (!ticketFormRef.value) return

  await ticketFormRef.value.validate((valid) => {
    if (valid) {
      // TODO: 实现提交逻辑
      ElMessage.success(dialogType.value === 'create' ? '创建成功' : '更新成功')
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
.tickets-container {
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