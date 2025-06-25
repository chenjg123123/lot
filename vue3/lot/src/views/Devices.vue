<template>
  <div class="devices-container">
    <!-- 搜索和操作栏 -->
    <div class="operation-bar">
      <el-form :inline="true"
               :model="searchForm"
               class="search-form">
        <el-form-item label="设备名称">
          <el-input v-model="searchForm.name"
                    placeholder="请输入设备名称"
                    clearable />
        </el-form-item>
        <el-form-item label="设备状态">
          <el-select v-model="searchForm.status"
                     placeholder="请选择状态"
                     clearable
                     style="width: 180px;">
            <el-option label="在线"
                       value="online" />
            <el-option label="离线"
                       value="offline" />
            <el-option label="故障"
                       value="fault" />
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
                   @click="handleAdd">
          <el-icon>
            <Plus />
          </el-icon>
          添加设备
        </el-button>
        <el-button type="danger"
                   :disabled="!selectedDevices.length"
                   @click="handleBatchDelete">
          <el-icon>
            <Delete />
          </el-icon>
          批量删除
        </el-button>
      </div>
    </div>

    <!-- 设备列表 -->
    <el-table v-loading="loading"
              :data="deviceList"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection"
                       width="55" />
      <el-table-column prop="name"
                       label="设备名称"
                       min-width="120" />
      <el-table-column prop="type"
                       label="设备类型"
                       width="120" />
      <el-table-column prop="status"
                       label="状态"
                       width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="lastOnlineTime"
                       label="最后在线时间"
                       width="180" />
      <el-table-column prop="location"
                       label="位置"
                       min-width="150" />
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
            <el-button type="danger"
                       link
                       @click="handleDelete(row)">
              <el-icon>
                <Delete />
              </el-icon>
              删除
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

    <!-- 添加/编辑设备对话框 -->
    <el-dialog v-model="dialogVisible"
               :title="dialogType === 'add' ? '添加设备' : '编辑设备'"
               width="500px">
      <el-form ref="deviceFormRef"
               :model="deviceForm"
               :rules="deviceRules"
               label-width="100px">
        <el-form-item label="设备名称"
                      prop="name">
          <el-input v-model="deviceForm.name"
                    placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="设备类型"
                      prop="type">
          <el-select v-model="deviceForm.type"
                     placeholder="请选择设备类型">
            <el-option label="传感器"
                       value="sensor" />
            <el-option label="控制器"
                       value="controller" />
            <el-option label="网关"
                       value="gateway" />
          </el-select>
        </el-form-item>
        <el-form-item label="位置"
                      prop="location">
          <el-input v-model="deviceForm.location"
                    placeholder="请输入设备位置" />
        </el-form-item>
        <el-form-item label="描述"
                      prop="description">
          <el-input v-model="deviceForm.description"
                    type="textarea"
                    rows="3"
                    placeholder="请输入设备描述" />
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
import { Search, Refresh, Plus, Delete, Edit, View } from '@element-plus/icons-vue'

// 搜索表单
const searchForm = reactive({
  name: '',
  status: '',
})

// 设备列表数据
const loading = ref(false)
const deviceList = ref([
  {
    id: 1,
    name: '温度传感器-001',
    type: 'sensor',
    status: 'online',
    lastOnlineTime: '2024-03-15 10:30:00',
    location: '车间A区-01',
    description: '用于监测车间温度',
  },
  // 更多测试数据...
])

// 分页
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)

// 选中的设备
const selectedDevices = ref([])

// 对话框
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const deviceFormRef = ref<FormInstance>()
const deviceForm = reactive({
  name: '',
  type: '',
  location: '',
  description: '',
})

// 表单验证规则
const deviceRules: FormRules = {
  name: [
    { required: true, message: '请输入设备名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' },
  ],
  type: [{ required: true, message: '请选择设备类型', trigger: 'change' }],
  location: [{ required: true, message: '请输入设备位置', trigger: 'blur' }],
}

// 获取状态类型
const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    online: 'success',
    offline: 'info',
    fault: 'danger',
  }
  return map[status] || 'info'
}

// 获取状态文本
const getStatusText = (status: string) => {
  const map: Record<string, string> = {
    online: '在线',
    offline: '离线',
    fault: '故障',
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
  searchForm.name = ''
  searchForm.status = ''
  handleSearch()
}

// 选择变化
const handleSelectionChange = (selection: any[]) => {
  selectedDevices.value = selection
}

// 添加设备
const handleAdd = () => {
  dialogType.value = 'add'
  dialogVisible.value = true
  deviceForm.name = ''
  deviceForm.type = ''
  deviceForm.location = ''
  deviceForm.description = ''
}

// 编辑设备
const handleEdit = (row: any) => {
  dialogType.value = 'edit'
  dialogVisible.value = true
  Object.assign(deviceForm, row)
}

// 查看设备
const handleView = (row: any) => {
  // TODO: 实现查看详情逻辑
  console.log('查看设备：', row)
}

// 删除设备
const handleDelete = (row: any) => {
  ElMessageBox.confirm('确认删除该设备吗？', '提示', {
    type: 'warning',
  }).then(() => {
    // TODO: 实现删除逻辑
    ElMessage.success('删除成功')
  })
}

// 批量删除
const handleBatchDelete = () => {
  if (selectedDevices.value.length === 0) {
    ElMessage.warning('请选择要删除的设备')
    return
  }

  ElMessageBox.confirm(`确认删除选中的 ${selectedDevices.value.length} 个设备吗？`, '提示', {
    type: 'warning',
  }).then(() => {
    // TODO: 实现批量删除逻辑
    ElMessage.success('删除成功')
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!deviceFormRef.value) return

  await deviceFormRef.value.validate((valid) => {
    if (valid) {
      // TODO: 实现提交逻辑
      ElMessage.success(dialogType.value === 'add' ? '添加成功' : '更新成功')
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
.devices-container {
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