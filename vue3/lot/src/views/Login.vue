<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <img src="../assets/logo.png"
               alt="Logo"
               class="logo" />
          <h2>智能工单系统</h2>
        </div>
      </template>

      <el-form ref="loginFormRef"
               :model="loginForm"
               :rules="loginRules"
               label-position="top">
        <el-form-item label="用户名"
                      prop="username">
          <el-input v-model="loginForm.username"
                    placeholder="请输入用户名"
                    prefix-icon="User" />
        </el-form-item>

        <el-form-item label="密码"
                      prop="password">
          <el-input v-model="loginForm.password"
                    type="password"
                    placeholder="请输入密码"
                    prefix-icon="Lock"
                    show-password />
        </el-form-item>

        <el-form-item>
          <el-button type="primary"
                     :loading="loading"
                     class="login-button"
                     @click="handleLogin">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { login } from '@/api/authApi'

const router = useRouter()
const loginFormRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: '',
})

const loginRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
  ],
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      loading.value = true
      try {
        const res = await login({
          username: loginForm.username,
          password: loginForm.password,
        })
        localStorage.setItem('token', res.data.data)
        console.log(res.data.data)
        ElMessage.success('登录成功')
        router.push('/')
      } catch (error) {
        console.error('登录失败:', error)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped lang="scss">
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;

  .login-card {
    width: 400px;

    .card-header {
      text-align: center;

      .logo {
        width: 64px;
        height: 64px;
        margin-bottom: 16px;
      }

      h2 {
        margin: 0;
        font-size: 24px;
        color: #303133;
      }
    }

    .login-button {
      width: 100%;
    }
  }
}
</style> 