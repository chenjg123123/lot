import request from '@/util/request'

export const getDeviceList = () => {
  return request.get('/device/list')
}
