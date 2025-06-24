import request from '@/util/request'

//获取两周警告总数
export const getNotificationTwoweek = () => {
  return request.get('/notification/twoweek')
}
//获取一周警告数量
export const getNotificationOneWeek = () => {
  return request.get('/notification/weekuse')
}
