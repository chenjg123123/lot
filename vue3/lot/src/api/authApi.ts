import request from '@/util/request'
import rsaUtil from '@/util/rsa'

interface LoginParams {
  username: string
  password: string
}
//登录
export const login = (params: LoginParams) => {
  // 对密码进行RSA加密
  const encryptedPassword = rsaUtil.encryptData(params.password)
  const encryptedUsername = rsaUtil.encryptData(params.username)
  return request.post('/auth/login', {
    username: encryptedUsername,
    password: encryptedPassword,
  })
}
