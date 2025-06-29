import axios from 'axios'
import type {
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建响应数据的接口
interface ResponseData<T = unknown> {
  code: number
  data: T
  message: string
}

// 创建请求配置接口
interface RequestConfig extends AxiosRequestConfig {
  retry?: number
  retryDelay?: number
}

class Request {
  private instance: AxiosInstance
  private retryCount: number = 1
  private retryDelay: number = 1000

  constructor(config: RequestConfig) {
    this.instance = axios.create({
      baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
      timeout: 10000,
      headers: {
        'Content-Type': 'application/json',
      },
      ...config,
    })

    this.setupInterceptors()
  }

  // 设置拦截器
  private setupInterceptors() {
    // 请求拦截器
    this.instance.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        // 从localStorage获取token
        const token = localStorage.getItem('token')
        if (token) {
          config.headers.Authorization = `Bearer ${token}`
        }
        return config
      },
      (error) => {
        return Promise.reject(error)
      },
    )

    // 响应拦截器
    this.instance.interceptors.response.use(
      (response: AxiosResponse<ResponseData>) => {
        if (response.code === 500) router.push('/login')
        const { code, message } = response.data
        // 根据业务状态码处理
        if (code === 200) {
          return response
        } else if (code === 886) {
          router.push('/login')
          localStorage.removeItem('token')
          return Promise.reject(new Error('登陆异常'))
        } else {
          ElMessage.error(message || '请求失败')
          return Promise.reject(new Error(message || '请求失败'))
        }
      },
      async (error) => {
        if (!error.response) {
          ElMessage.error('网络错误，请检查您的网络连接')
          return Promise.reject(error)
        }

        const { config } = error
        if (!config || !config.retry) {
          return Promise.reject(error)
        }

        // 设置重试次数
        config.retryCount = config.retryCount || 0

        if (config.retryCount >= this.retryCount) {
          return Promise.reject(error)
        }

        // 重试次数自增
        config.retryCount += 1

        // 延迟重试
        const delay = new Promise((resolve) => {
          setTimeout(() => {
            resolve(null)
          }, config.retryDelay || this.retryDelay)
        })

        await delay
        return this.instance(config)
      },
    )
  }

  // 通用请求方法
  public request<T = unknown>(config: RequestConfig): Promise<T> {
    return this.instance.request(config)
  }

  // GET请求
  public get<T = unknown>(
    url: string,
    params?: Record<string, unknown>,
    config?: RequestConfig,
  ): Promise<T> {
    return this.instance.get(url, { params, ...config })
  }

  // POST请求
  public post<T = unknown>(
    url: string,
    data?: Record<string, unknown>,
    config?: RequestConfig,
  ): Promise<T> {
    return this.instance.post(url, data, config)
  }

  // PUT请求
  public put<T = unknown>(
    url: string,
    data?: Record<string, unknown>,
    config?: RequestConfig,
  ): Promise<T> {
    return this.instance.put(url, data, config)
  }

  // DELETE请求
  public delete<T = unknown>(url: string, config?: RequestConfig): Promise<T> {
    return this.instance.delete(url, config)
  }
}

// 创建请求实例
const request = new Request({
  retry: 3,
  retryDelay: 3000,
})

export default request
