import request from '@/utils/request'

// 上传文件
export function upload(data) {
  return request({
    url: '/common/upload',
    method: 'post',
    data: data
  })
}
