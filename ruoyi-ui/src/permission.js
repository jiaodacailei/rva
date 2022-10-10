import router from './router'
import store from './store'
import {Message} from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {getToken} from '@/utils/auth'
import * as dd from 'dingtalk-jsapi';

NProgress.configure({showSpinner: false})

const whiteList = ['/login', '/auth-redirect', '/bind', '/register']

function getUserInfoAndRoutes(to, from, next) {
  // 判断当前用户是否已拉取完user_info信息
  store.dispatch('GetInfo').then(() => {
    console.log('GetInfo ok', store)
    store.dispatch('GenerateRoutes').then(accessRoutes => {
      console.log('GenerateRoutes ok')
      // 根据roles权限生成可访问的路由表
      router.addRoutes(accessRoutes) // 动态添加可访问路由表
      next({...to, replace: true}) // hack方法 确保addRoutes已完成
    }).catch(err => {
      console.log('GenerateRoutes', err)
    })
  }).catch(err => {
    console.log('GetInfo', err)
    store.dispatch('LogOut').then(() => {
      Message.error(err)
      next({path: '/login'})
    })
  })
}

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
    /* has token*/
    if (to.path === '/login') {
      next({path: '/'})
      NProgress.done()
    } else {
      if (store.getters.roles.length === 0) {
        getUserInfoAndRoutes(to, from, next)
      } else {
        next()
      }
    }
  } else {
    // Message.info(dingtalkCorporationId)
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else if (process.env.VUE_APP_DINGTALK_CORP_ID && process.env.VUE_APP_DINGTALK_CORP_ID != '' && dd.env.platform!="notInDingTalk") {
      dd.runtime.permission.requestAuthCode({
        corpId: process.env.VUE_APP_DINGTALK_CORP_ID,
        onSuccess: function (result) {
          console.log('dingtalk', result);
          store.dispatch("LoginDingtalk", result.code).then(() => {
            getUserInfoAndRoutes(to, from, next)
          }).catch(err => {
            Message.error('登录钉钉失败：' + err)
            console.error('dingtalk', err);
          });
        },
        onFail: function (err) {
          Message.error('登录钉钉失败：' + err)
          console.error('dingtalk', err);
        }
      })
    } else {
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
