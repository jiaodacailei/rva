import request from '@/utils/request'

// 查询chart元数据
export function loadChart(id) {
  return request({
    url: '/rva/chart/' + id,
    method: 'get'
  })
}

export function getDataset(id, params) {
  return request({
    url: '/rva/chart/dataset/' + id,
    data: params,
    method: 'post'
  })
}
