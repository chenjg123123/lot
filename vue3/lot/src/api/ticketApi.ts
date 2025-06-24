import request from '@/util/request'

//获取两周全部工单
export const getTicketAll = () => {
  return request.get('/ticket/twoweek')
}
export const getticketOneWeek = () => {
  return request.get('/ticket/weekuse')
}
export const getWeekStatus = () => {
  return request.get('/ticket/weekstatus')
}
export const getWeekeffient = () => {
  return request.get('/ticket/weekticketeffiency')
}
