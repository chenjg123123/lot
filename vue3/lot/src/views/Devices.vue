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
                     @click="handleSearchPromise">
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
    <!-- 设备详情 -->
    <el-dialog v-model="detailDialogVisible"
               title="设备详情"
               width="500px">
      <el-form :model="device"
               label-width="100px"
               disabled="true">
        <el-form-item label="设备名称"
                      prop="name">
          <el-input v-model="device.name"></el-input>
        </el-form-item>
        <el-form-item label="设备编号"
                      prop="code">
          <el-input v-model="device.id"></el-input>
        </el-form-item>
        <el-form-item label="设备状态"
                      prop="status">
          <el-input v-model="device.status"></el-input>
        </el-form-item>
        <el-form-item label="设备描述"
                      prop="description">
          <el-input v-model="device.description"></el-input>
        </el-form-item>
        <el-form-item label="设备位置"
                      prop="location">
          <el-input v-model="device.location"></el-input>
        </el-form-item>
        <el-form-item label="设备状态"
                      prop="status">
          <el-select v-model="device.status">
            <el-option label="正常"
                       value="online"></el-option>
            <el-option label="异常"
                       value="error"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="设备种类"
                      prop="description">
          <el-input v-model="device.type">
          </el-input>
        </el-form-item>
        <el-form-item label="InstallTime"
                      prop="installationDate">
          <el-input v-model="parseTimestamp(device.installationDate).fullDate">
          </el-input>
        </el-form-item>
        <el-form-item label="Version"
                      prop="firmwareVersion">
          <el-input v-model="device.firmwareVersion">
          </el-input>
        </el-form-item>
      </el-form>
    </el-dialog>

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
      <el-table-column label="最后在线时间"
                       width="180">
        <template #default="{ row }">
          {{ parseTimestamp(row.updatedAt).yearMonth }}
        </template>
      </el-table-column>
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
                       value="传感器" />
            <el-option label="控制器"
                       value="控制器" />
            <el-option label="网关"
                       value="网关" />
          </el-select>
        </el-form-item>
        <el-form-item label="位置"
                      prop="location">
          <el-input v-model="deviceForm.location"
                    placeholder="请输入设备位置" />
        </el-form-item>
        <el-form-item label="公司"
                      prop="companyId">
          <el-select v-model="deviceForm.companyId"
                     @change="getCompanies"
                     placeholder="请选择公司">
            <el-option v-for="company in companyIDReflect"
                       :label=company.name
                       :key="company.id"
                       :value="company.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="版本"
                      prop="firmwareVersion">
          <el-input v-model="deviceForm.firmwareVersion"
                    placeholder="请输入设备版本" />
        </el-form-item>
        <el-form-item label="model"
                      prop="model">
          <el-input v-model="deviceForm.model"
                    placeholder="请输入设备model" />
        </el-form-item>
        <el-form-item label="保修期"
                      value="warrantyPeriod">
          <el-input v-model="deviceForm.warrantyPeriod">
          </el-input>
        </el-form-item>
        <!-- <el-form-item label="
                     描述"
                     prop="description">
            <el-input v-model="deviceForm.description"
                      type="textarea"
                      rows="3"
                      placeholder="请输入设备描述" />
        </el-form-item> -->
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
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { Search, Refresh, Plus, Delete, Edit, View } from '@element-plus/icons-vue'
import { getDeviceList, addDevice } from '@/api/deviceApi'
import { wsClient } from '@/util/websocket'
import { parseTimestamp } from '@/util/timeTransform'
import { number } from 'echarts'
import { getCompanies } from '@/api/userApi'
// 搜索表单
const searchForm = reactive({
  name: '',
  status: '',
})
const getCompanies = () => {
  console.log(deviceForm.companyId)
}
// 设备列表数据
const loading = ref(false)
const deviceList = ref([])
const companyIDReflect = reactive([
  { id: 1, name: '公司1' },
  { id: 2, name: '公司2' },
])
const device = reactive({
  companyId: 1,
  createdAt: 1750063954000,
  firmwareVersion: 'v1.0.0',
  id: 1,
  installationDate: 1704038400000,
  location: '车间A',
  model: 'T100',
  name: '温度传感器1',
  serialNumber: 'SN000001',
  status: 'online',
  type: '传感器',
  updatedAt: 1750293701000,
  warrantyPeriod: 12,
})
// 分页
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)
// 选中的设备
const selectedDevices = ref([])
const detailDialogVisible = ref(false)
// 对话框
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const deviceFormRef = ref<FormInstance>()
const deviceForm = reactive({
  name: '',
  type: '',
  location: '',
  companyId: number,
  createdAt: number,
  firmwareVersion: '',
  installationDate: number,
  model: '',
  serialNumber: '',
  status: '',
  updatedAt: number,
  warrantyPeriod: 12,
})

// 表单验证规则
const deviceRules: FormRules = {
  name: [
    { required: true, message: '请输入设备名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' },
  ],
  type: [{ required: true, message: '请选择设备类型', trigger: 'change' }],
  location: [{ required: true, message: '请输入设备位置', trigger: 'blur' }],
  compantId: [{ required: true, message: '请选择公司名称', trigger: 'change' }],
  model: [{ required: true, message: '请输入设备型号', trigger: 'blur' }],
  firmwareVersion: [{ required: true, message: '请输入设备固件版本', trigger: 'blur' }],
  warrantyPeriod: [{ required: true, message: '请输入设备保修期限', trigger: 'blur' }],
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
const handleSearchPromise = async () => {
  await getDeviceList(currentPage.value, pageSize.value, searchForm.status, searchForm.name)
}
// 重置搜索
const resetSearch = () => {
  searchForm.name = ''
  searchForm.status = ''
  handleSearchPromise()
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
}
// 编辑设备
const handleEdit = (row: any) => {
  dialogType.value = 'edit'
  dialogVisible.value = true
  Object.assign(deviceForm, row)
}
// 查看设备
const handleView = (row: any) => {
  detailDialogVisible.value = true
  device.value = row
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
      //提交逻辑
      addDevice(deviceForm)
      dialogVisible.value = false
    }
  })
}
// 分页大小变化
const handleSizeChange = (val: number) => {
  pageSize.value = val
  handleSearchPromise()
}
// 页码变化
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  handleSearchPromise()
}
onMounted(async () => {
  wsClient.connect()

  await wsClient.waitUntilConnected() // ✅ 等待 requestId 绑定完成
  companyIDReflect.value = await getCompanies()
  wsClient.on('select:device:getlist', (msg) => {
    console.log('select:device:getlist', msg.payload.pageInfo)
    total.value = msg.payload.pageInfo.total
    deviceList.value = msg.payload.pageInfo.list
  })

  handleSearchPromise() // ✅ 确保是在绑定成功后执行
})

onBeforeUnmount(() => {
  localStorage.removeItem('requestId')
  wsClient.close()
})
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