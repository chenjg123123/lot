import request from '@/util/request'
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
//查询设备列表
export const getDeviceList = (page: number, pageSize: number, status: string, name: string) => {
  const requestId = localStorage.getItem("requestId");
  if (!requestId) return Promise.reject("连接失败");
  return request.post('/device/getlist', {
    requestId: requestId,
    payload: { page, pageSize, status, name }
  });
};
//添加设备
export const addDevice = (deviceForm: any) => {
  const requestId = localStorage.getItem("requestId");
  const { name, type, location, companyId, firmwareVersion, model, warrantyPeriod } = deviceForm;
  if (!requestId) return Promise.reject("连接失败");
  return request.post('/device/operation/add', {
    requestId: requestId,
    payload: { name, type, location, companyId, firmwareVersion, model, warrantyPeriod }
  });
};
//删除设备
export const deleteDevice = (deviceId: [{ id: string, name: string }]) => {
  const requestId = localStorage.getItem("requestId");
  return request.post('/device/operation/delete', {
    requestId: requestId,
    payload: { deviceId: deviceId }
  });
};
