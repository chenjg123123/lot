<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '200px'"
              class="aside">
      <div class="logo">
        <img src="@/assets/logo.svg"
             alt="Logo" />
        <span v-show="!isCollapse">智能工单系统</span>
      </div>
      <el-menu :default-active="route.path"
               class="el-menu-vertical"
               :collapse="isCollapse"
               router>
        <el-menu-item index="/">
          <el-icon>
            <HomeFilled />
          </el-icon>
          <template #title>首页</template>
        </el-menu-item>
        <el-menu-item index="/devices">
          <el-icon>
            <Monitor />
          </el-icon>
          <template #title>设备管理</template>
        </el-menu-item>
        <el-menu-item index="/tickets">
          <el-icon>
            <Tickets />
          </el-icon>
          <template #title>工单管理</template>
        </el-menu-item>
        <el-menu-item index="/alerts">
          <el-icon>
            <Bell />
          </el-icon>
          <template #title>告警中心</template>
        </el-menu-item>
        <el-menu-item index="/dashboard">
          <el-icon>
            <DataLine />
          </el-icon>
          <template #title>数据大屏</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-button type="text"
                     @click="toggleCollapse">
            <el-icon>
              <component :is="isCollapse ? Expand : Fold" />
            </el-icon>
          </el-button>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              管理员
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人信息</el-dropdown-item>
                <el-dropdown-item>修改密码</el-dropdown-item>
                <el-dropdown-item divided
                                  @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main>
        <router-view v-slot="{ Component }">
          <transition name="fade"
                      mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  HomeFilled,
  Monitor,
  Tickets,
  Bell,
  DataLine,
  Fold,
  Expand,
  ArrowDown,
} from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const isCollapse = ref(false)

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    localStorage.removeItem('token')
    router.push('/login')
  })
}
</script>

<style scoped lang="scss">
.layout-container {
  height: 100vh;

  .aside {
    background-color: #304156;
    transition: width 0.3s;
    overflow: hidden;

    .logo {
      height: 60px;
      display: flex;
      align-items: center;
      padding: 0 20px;
      color: #fff;

      img {
        width: 32px;
        height: 32px;
        margin-right: 12px;
      }

      span {
        font-size: 16px;
        font-weight: bold;
        white-space: nowrap;
      }
    }

    .el-menu {
      border-right: none;
      background-color: transparent;

      :deep(.el-menu-item) {
        color: #bfcbd9;

        &:hover,
        &.is-active {
          color: #fff;
          background-color: #263445;
        }

        .el-icon {
          color: inherit;
        }
      }
    }
  }

  .header {
    background-color: #fff;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;

    .header-right {
      .user-info {
        display: flex;
        align-items: center;
        cursor: pointer;
        color: #606266;

        .el-icon {
          margin-left: 4px;
        }
      }
    }
  }

  .el-main {
    background-color: #f0f2f5;
    padding: 20px;
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style> 