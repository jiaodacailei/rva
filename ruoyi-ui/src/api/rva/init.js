import request from '@/utils/request'
import store from "@/store";
import cache from "@/plugins/cache";

// 加载系统初始化数据
export function loadInitData() {
  return request({
    url: '/init',
    method: 'get'
  })
}

//store 加载系统初始化数据
export function loadInitStoreData() {
  let layoutSetting = cache.local.get('layout-setting');
  loadInitData().then(res => {
    console.log('loadInitData', res.data);
    res.data.config.forEach(cfg => {
      let val = cfg.configValue;
      if (cfg.configName.indexOf('@') >= 0) {
        val = eval(`(${val})`);
      }
      if (cfg.configKey == 'rva.ui.showSidebar') {
        if (val) {
         store.dispatch('app/openSideBar', {withoutAnimation: false})
        } else {
          store.dispatch('app/closeSideBar', {withoutAnimation: false})
        }
      } else if (cfg.configKey.indexOf('rva.layout.') == 0) {
        if (!layoutSetting) {
          store.dispatch('settings/changeSetting', {
            key: cfg.configKey.split('.').slice(-1)[0],
            value: val
          })
        }
      } else {
        store.dispatch('settings/changeSetting', {
          key: cfg.configKey,
          value: val
        })
      }
    });
  })
}
