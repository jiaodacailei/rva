import request from '@/utils/request'

export function getData(id, params) {
  return request({
    url: '/rva/kpi/' + id,
    data: params,
    method: 'post'
  })
}
