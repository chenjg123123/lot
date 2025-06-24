import request from '@/util/request'
//获取全部设备信息
export const getDeviceList = () => {
  return request.get('/device/list')
}
//获取两周设备总数量
export const getTwoweek = () => {
  return request.get('/device/twoweek')
}
//获取在线设备数量
export const getOnline = () => {
  return request.get('/device/getonline')
}
//设备状态分布
export const getStatusCount = () => {
  return request.get('/device/getstatus')
}
//获取设备在线率
export const getDeviceOline = () => {
  return request.get('/device/weekuserate')
}
