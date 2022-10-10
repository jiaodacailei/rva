import request from '@/utils/request'

// 登录方法
export function loginRvaDingtalk(authCode) {
  return request({
    url: '/rva/dingtalk/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: {
      authCode
    }
  })
}
