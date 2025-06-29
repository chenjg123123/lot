import request from '@/util/request'

export const getCompanies = () => {
  request.get("/user/operation/companylist")
}