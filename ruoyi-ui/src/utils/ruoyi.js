/**
 * 通用js方法封装处理
 * Copyright (c) 2019 ruoyi
 */
import router from './../router'
import store from "@/store";

import {getConfigKey} from "@/api/system/config";

// 日期格式化
export function parseTime(time, pattern) {
  if (arguments.length === 0 || !time) {
    return null
  }
  const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
      time = parseInt(time)
    } else if (typeof time === 'string') {
      time = time.replace(new RegExp(/-/gm), '/').replace('T', ' ').replace(new RegExp(/\.[\d]{3}/gm), '');
    }
    if ((typeof time === 'number') && (time.toString().length === 10)) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') {
      return ['日', '一', '二', '三', '四', '五', '六'][value]
    }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}

// 表单重置
export function resetForm(refName) {
  if (this.$refs[refName]) {
    this.$refs[refName].resetFields();
  }
}

// 添加日期范围
export function addDateRange(params, dateRange, propName) {
  let search = params;
  search.params = typeof (search.params) === 'object' && search.params !== null && !Array.isArray(search.params) ? search.params : {};
  dateRange = Array.isArray(dateRange) ? dateRange : [];
  if (typeof (propName) === 'undefined') {
    search.params['beginTime'] = dateRange[0];
    search.params['endTime'] = dateRange[1];
  } else {
    search.params['begin' + propName] = dateRange[0];
    search.params['end' + propName] = dateRange[1];
  }
  return search;
}

// 回显数据字典
export function selectDictLabel(datas, value) {
  var actions = [];
  Object.keys(datas).some((key) => {
    if (datas[key].value == ('' + value)) {
      actions.push(datas[key].label);
      return true;
    }
  })
  return actions.join('');
}

// 回显数据字典（字符串数组）
export function selectDictLabels(datas, value, separator) {
  var actions = [];
  var currentSeparator = undefined === separator ? "," : separator;
  var temp = value.split(currentSeparator);
  Object.keys(value.split(currentSeparator)).some((val) => {
    Object.keys(datas).some((key) => {
      if (datas[key].value == ('' + temp[val])) {
        actions.push(datas[key].label + currentSeparator);
      }
    })
  })
  return actions.join('').substring(0, actions.join('').length - 1);
}

// 字符串格式化(%s )
export function sprintf(str) {
  var args = arguments, flag = true, i = 1;
  str = str.replace(/%s/g, function () {
    var arg = args[i++];
    if (typeof arg === 'undefined') {
      flag = false;
      return '';
    }
    return arg;
  });
  return flag ? str : '';
}

// 转换字符串，undefined,null等转化为""
export function praseStrEmpty(str) {
  if (!str || str == "undefined" || str == "null") {
    return "";
  }
  return str;
}

// 数据合并
export function mergeRecursive(source, target) {
  for (var p in target) {
    try {
      if (target[p].constructor == Object) {
        source[p] = mergeRecursive(source[p], target[p]);
      } else {
        source[p] = target[p];
      }
    } catch (e) {
      source[p] = target[p];
    }
  }
  return source;
};

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export function handleTree(data, id, parentId, children) {
  let config = {
    id: id || 'id',
    parentId: parentId || 'parentId',
    childrenList: children || 'children'
  };

  var childrenListMap = {};
  var nodeIds = {};
  var tree = [];

  for (let d of data) {
    let parentId = d[config.parentId];
    if (childrenListMap[parentId] == null) {
      childrenListMap[parentId] = [];
    }
    nodeIds[d[config.id]] = d;
    childrenListMap[parentId].push(d);
  }

  for (let d of data) {
    let parentId = d[config.parentId];
    if (nodeIds[parentId] == null) {
      tree.push(d);
    }
  }

  for (let t of tree) {
    adaptToChildrenList(t);
  }

  function adaptToChildrenList(o) {
    if (childrenListMap[o[config.id]] !== null) {
      o[config.childrenList] = childrenListMap[o[config.id]];
    }
    if (o[config.childrenList]) {
      for (let c of o[config.childrenList]) {
        adaptToChildrenList(c);
      }
    }
  }

  return tree;
}

/**
 * 参数处理
 * @param {*} params  参数
 */
export function tansParams(params) {
  let result = ''
  for (const propName of Object.keys(params)) {
    const value = params[propName];
    var part = encodeURIComponent(propName) + "=";
    if (value !== null && typeof (value) !== "undefined") {
      if (typeof value === 'object') {
        for (const key of Object.keys(value)) {
          if (value[key] !== null && typeof (value[key]) !== 'undefined') {
            let params = propName + '[' + key + ']';
            var subPart = encodeURIComponent(params) + "=";
            let val = value[key];
            if (typeof val === 'object') {
              val = JSON.stringify(val)
            }
            result += subPart + encodeURIComponent(val) + "&";
          }
        }
      } else {
        result += part + encodeURIComponent(value) + "&";
      }
    }
  }
  return result
}

// 验证是否为blob格式
export async function blobValidate(data) {
  try {
    const text = await data.text();
    JSON.parse(text);
    return false;
  } catch (error) {
    return true;
  }
}

/**
 * 获取真实url
 * @param url
 * @returns {string|*}
 * router设置了base 会作为前缀
 * ruoyi-ui/src/router/index.js  router.base
 */

export function getRealUrl(url) {
  console.log('router', router)
  let base = router.options.base
  if (base) {
    let nUrl = url.indexOf('/') == 0 ? url : '/' + url;
    return base.indexOf('/') == 0 ? base + nUrl : '/' + base + nUrl;
  }
  return url;
}

/**
 * 获取文件路径
 *
 * @param url
 * @returns {string|*}
 */
export function getFileUrl(url, prefix, params) {
  if (!url) {
    return
  }
  if (!prefix) {
    prefix = getSysFilePrefix();
  }
  if (url.startsWith("http://") || url.startsWith("https://")) {
    return url;
  }
  if (params) {
    var tansParams1 = tansParams(params);
    if (url.indexOf('?') >= 0) {
      url = url + tansParams1;
    } else {
      url = url + '?' + tansParams1;
    }
  }
  if (!prefix.endsWith('/') && !url.startsWith('/')) {
    prefix = prefix + '/'
  }

  return prefix + url;
}

export function getFileName(url, prefix) {
  if (!prefix) {
    prefix = getSysFilePrefix();
  }
  if (url.indexOf(prefix) == 0) {
    return url.replace(prefix, "");
  }
  return url;
}


function _addUrlPrefix(html, prefix) {
  if (!html) {
    return;
  }
  var el = document.createElement('html');
  el.innerHTML = html;

  function each(e) {
    let src = e.attributes.src
    if (src && src.nodeValue && src.nodeValue.indexOf(prefix) != 0 && !isFullUrl(src.nodeValue)) {
      e.src = prefix + src.nodeValue;
      e.style['width'] = '98%'
    }
  }

  Array.from(el.getElementsByTagName('img')).forEach(each);
  Array.from(el.getElementsByTagName('video')).forEach(each);
  return el.getElementsByTagName('body')[0].innerHTML;
}

function isFullUrl(url) {
  if (url && (url.toLowerCase().indexOf("http://") == 0 || url.toLowerCase().indexOf("https://") == 0)) {
    return true
  }
  return false
}

export function _removePrefix(html, prefix) {
  if (!html) {
    return;
  }
  var el = document.createElement('html');
  el.innerHTML = html;

  function each(e) {
    let src = e.attributes.src
    if (src && src.nodeValue && src.nodeValue.indexOf(prefix) == 0) {
      e.src = getFileName(src.nodeValue)

    }
  }

  Array.from(el.getElementsByTagName('img')).forEach(each);
  Array.from(el.getElementsByTagName('video')).forEach(each);
  return el.getElementsByTagName('body')[0].innerHTML;

}

export function addUrlPrefix(html) {
  return _addUrlPrefix(html, process.env.VUE_APP_BASE_API)
}

export function removePrefix(html) {
  return _removePrefix(html, process.env.VUE_APP_BASE_API)
}

/**
 * 系统名称
 * @returns {string}
 */
export function getSysTitle() {
  let setttings = store.state.settings
  let sysName = setttings["sys.config.name"];
  return sysName || process.env.VUE_APP_TITLE
}


/**
 * 系统图标
 * @returns {string|*|string}
 */
export function getSysLogo() {
  const logoImg = require("@/assets/logo/logo.png")
  let sysLogo = store.state.settings["sys.config.logo"];
  return getFileUrl(sysLogo) || logoImg
}

/**
 * 系统文件前缀
 * @returns {string|*|string}
 */
export function getSysFilePrefix() {
  let result = process.env.VUE_APP_BASE_API;
  let prefix = store.state.settings["sys.file.prefix"];
  if (prefix && !prefix.startsWith("/")) {
    result = prefix;
  }
  return result;
}

/**
 * 角色是否单选
 * @returns {boolean}
 */
export function getSysRoleSingle() {

  let val = store.state.settings["rva.role.single"];
  if (!val) {
    val = "Y"
  }
  return val == 'Y'
}


/**
 *  默认登录名
 * @returns {String}
 */
export function getSysLoginUsername() {
  return store.state.settings["sys.login.username"];
}

/**
 * 默认登录密码
 * @returns {String}
 */
export function getSysLoginPassword() {
  return store.state.settings["sys.login.password"];
}

/**
 *  启用默认登录账号
 ** @returns {boolean}
 */
export function getSysLoginUserEnable() {
  return 'Y' == store.state.settings["sys.login.user.enable"];
}



/**
 *  是否开启base64编码
 ** @returns {boolean}
 */
export function isBase64Mode() {
  return 'Y' == store.state.settings["rva.req.body.base64.mode"];
}


