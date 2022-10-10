import {getDicts} from "@/api/system/dict/data";
import store from '@/store';
import {getConfigKey} from "@/api/system/config";

export const RvaUtils = {
  clone(src, target, deep) {
    if (!target) {
      target = {};
    }
    if (!src) {
      return target;
    }
    if (deep) {
      src = JSON.parse(JSON.stringify(src));
    }
    for (var key in src) {
      target[key] = src[key];
    }
    return target;
  },
  cloneDeep(obj) {
    if (RvaUtils.isEmpty(obj)) {
      return null;
    }
    return JSON.parse(JSON.stringify(obj));
  },
  isEmpty(obj) {
    return obj == null || obj == undefined || obj == '' || obj.length == 0;
  },
  isNotEmpty(obj) {
    return !RvaUtils.isEmpty(obj);
  },
  isObject(obj) {
    return (typeof obj === 'object');
  },
  isString(obj) {
    return (typeof obj === 'string');
  },
  parseValue(jsonStringOrObject, key, defaultValue) {
    if (jsonStringOrObject) {
      var obj;
      if (typeof jsonStringOrObject === Object) {
        obj = jsonStringOrObject;
      } else {
        jsonStringOrObject = jsonStringOrObject.replace(/\s+/g, ' ')
        obj = JSON.parse(jsonStringOrObject);
      }
      if (key in obj) {
        return RvaUtils.isEmpty(obj[key]) ? defaultValue : obj[key];
      }
    }
    return defaultValue;
  },
  parseInt(val, defaultVal) {
    if (isNaN(val)) {
      return defaultVal;
    }
    return parseInt(val);
  },
  parseFloat(val, defaultVal) {
    if (isNaN(val)) {
      return defaultVal;
    }
    return parseFloat(val);
  },
  parseJsValue(script, $$, bsonType) {
    $$ = {...$$}
    $$.state = store.state;
    let value = script;
    if (RvaUtils.isEmpty(value)) {
      return value;
    }
    // console.log('parseJsValue', value, $$, bsonType)
    let func = /^function[ ]*[(][ ]*[)]/.test(script);
    if (value.indexOf('$$') >= 0 || func) {
      try {
        value = eval('(' + script + ')')
        if (func) {
          value = value();
        }
      } catch (e) {
        console.log('parseJsValue err', script, e);
        value = ''
      }
    }
    // console.log('parseJsValue', value, bsonType)
    return RvaUtils.parseValueByType(value, bsonType);
  },
  parseValueByType(value, bsonType) {
    if (RvaUtils.isEmpty(value)) {
      return value;
    }
    bsonType = bsonType || 'string'
    if (bsonType == 'number') {
      value = RvaUtils.parseFloat(value)
    } else if (bsonType == 'int' || bsonType == 'timestamp') {
      value = RvaUtils.parseInt(value)
    }
    return value;
  },
  getValidateRule(rule) {
    let rules = {
      IDCard: /^\d{6}(18|19|20)\d{2}(0\d|10|11|12)([0-2]\d|30|31)\d{3}(\d|X|x)$/,
      mobile: /^(((13[0-9]{1})|(15[0-9]{1})|(16[0-9]{1})|(17[3-8]{1})|(18[0-9]{1})|(19[0-9]{1})|(14[5-7]{1}))+\d{8})$/
    };
    if (rules[rule]) {
      return rules[rule];
    }
    return eval(rule);
  },
  endsWith(string, suffix) {
    return new RegExp(suffix + "$").test(string);
  },
  getKeyBySuffix(obj, suffix) {
    for (var key in obj) {
      if (RvaUtils.endsWith(key, suffix)) {
        return key;
      }
    }
  },
  getValueBySuffix(obj, suffix) {
    let key = this.getKeyBySuffix(obj, suffix);
    if (key) {
      return obj[key];
    }
  },
  trigger(formData, formDataOld, viewData, propRefs, rowIndex) {
    let triggerPropId = undefined;
    if (formDataOld && !RvaUtils.isObject(formDataOld)) {
      triggerPropId = formDataOld;
      formDataOld = undefined;
    }

    // 使用说明请参考：https://gitee.com/jiaodacailei/RuoYi-Vue-Auto/issues/I47HU1
    function getSuffix(suffix) {
      if (rowIndex >= 0) {
        suffix += '_' + rowIndex;
      }
      return suffix;
    }

    function getPropBySuffix(suffix) {
      return viewData.properties.filter(p => RvaUtils.endsWith(p.id, getSuffix(suffix)))[0];
    }

    function hide(suffix) {
      let prop = getPropBySuffix(suffix);
      if (prop) {
        prop.originalType = prop.type;
        prop.type = 'hidden';
      }
    }

    function show(suffix) {
      let prop = getPropBySuffix(suffix);
      if (prop) {
        prop.type = prop.originalType;
        rerenderForm()
      }
    }

    function getPropRef(suffix) {
      return RvaUtils.getValueBySuffix(propRefs, getSuffix(suffix))[0];
    }


    let triggers = viewData.triggers;
    if (!triggers || triggers.length == 0) {
      return;
    }
    for (var i in triggers) {
      let trigger = triggers[i];
      console.log('trigger-' + i, trigger);
      // 获取参数值
      let params = [];
      let paramsOld = [];
      for (var j = 0; j < trigger.params.split(',').length; j++) {
        let key = RvaUtils.getKeyBySuffix(formData, trigger.params.split(',')[j]);
        // console.log (key);
        if (key) {
          if (RvaUtils.isNotEmpty(formData[key])) {
            params.push(formData[key]);
          } else {
            params.push(null);
          }
          if (formDataOld) {
            if (RvaUtils.isNotEmpty(formDataOld[key])) {
              paramsOld.push(formDataOld[key]);
            } else {
              paramsOld.push(null);
            }
          }
        } else {
          params.push(null);
          if (formDataOld) {
            paramsOld.push(null);
          }
        }
      }
      // console.log ('trigger params', params, paramsOld);
      // params是否有值变化，有变化时，才触发action
      let change = false;
      if (triggerPropId) {
        if (trigger.params.split(',').indexOf(triggerPropId) >= 0) {
          change = true;
        }
      } else if (params.length == paramsOld.length) {
        for (var j = 0; j < params.length; j++) {
          if (RvaUtils.isEmpty(params[j]) !== RvaUtils.isEmpty(paramsOld[j])) {
            change = true;
            continue;
          }
          if (RvaUtils.isEmpty(params[j]) && RvaUtils.isEmpty(paramsOld[j])) {
            continue;
          }
          if (typeof params[j].sort == "function") {
            if (params[j].sort().toString() !== paramsOld[j].sort().toString()) {
              change = true;
            }
          } else if (params[j].toString() != paramsOld[j].toString()) {
            change = true;
          }
        }
      } else {
        change = true;
      }
      if (!change) {
        continue;
      }
      if (!eval(trigger.triggerIf)) {
        continue;
      }
      console.log('trigger-' + i + '-action', trigger);
      // 触发action
      for (var a = 0; a < trigger.actions.length; a++) {
        let action = trigger.actions[a];
        let propRef = getPropRef(action.actionSubject);
        if (RvaUtils.isEmpty(action.actionParams)) {
          propRef[action.action]();
        } else {
          let $$ = {
            ...formData,
            viewData,
            params
          }
          let val = RvaUtils.parseJsValue(action.actionParams, $$);
          console.log('action....', action.action, val, $$);
          propRef[action.action](val);
        }
      }
    }
  },
  parseValidateRules(prop, rules,requiredIf) {
    if (!rules) {
      rules = [];
    }
    //
    if (prop.formRequired == 'Y' && requiredIf) {
      rules.push({required: true, message: prop.name + '为必需'});
    }
    if (prop.type == 'textfield') {
      let min = RvaUtils.parseInt(prop.formValueMin, 0);
      if (min < 0) {
        min = 0;
      }
      let max = RvaUtils.parseInt(prop.formValueMax, 20);
      rules.push({
        pattern: eval('/^.{' + min + ',' + max + '}$/'),
        message: '只能输入' + min + '-' + max + '个字符'
      });
      let validateRule = RvaUtils.parseValue(prop.data, 'validateRule');
      if (!validateRule) {
        return rules;
      }
      if (validateRule == 'email') {
        rules.push({
          type: 'email',
          message: 'email格式不正确'
        });
      } else {
        let regex = RvaUtils.getValidateRule(validateRule);
        rules.push({
          pattern: regex,
          message: RvaUtils.parseValue(prop.data, 'validateErrorMessage')
        });
      }
    }
    return rules;
  },
  getDicts(dictType, obj, objProp, fn) {
    if (RvaUtils.isEmpty(dictType)) {
      return;
    }
    if (!objProp) {
      objProp = 'dictData';
    }
    let key = 'rva.dict:' + dictType;
    let val = sessionStorage.getItem(key)
    if (val && val.indexOf('processing:') == 0) {
      let time = parseInt(val.split(':')[1]);
      if (new Date().getTime() - time > 5000) {
        sessionStorage.removeItem(key);
        RvaUtils.getDicts(dictType, obj, objProp, fn)
        return
      }
      setTimeout(() => RvaUtils.getDicts(dictType, obj, objProp, fn), 100);
      return;
    }
    if (sessionStorage.hasOwnProperty(key)) {
      obj[objProp] = JSON.parse(sessionStorage.getItem(key));
      if (fn) {
        fn(dictType, obj, objProp)
      }
    } else {
      sessionStorage.setItem(key, 'processing:' + new Date().getTime());
      getDicts(dictType).then(response => {
        sessionStorage.setItem(key, JSON.stringify(response.data));
        obj[objProp] = response.data;
        if (fn) {
          fn(dictType, obj, objProp)
        }
      });
    }
  },
  isDevMode() {
    return store.state.settings["rva.dev.mode"] == 'Y';
    // return store.state.user.roles.indexOf('admin') >= 0 && store.state.settings["rva.dev.mode"] == 'Y';
  },
  getDropdownShowTimeout() {
    return parseInt(store.state.settings['rva.dropdown.showTimeout']);
  },
  getFormLabelWidth() {
    return parseInt(store.state.settings['rva.form.labelWidth']);
  },
  getSearchLabelWidth() {
    return parseInt(store.state.settings['rva.search.labelWidth']);
  },
  getColor(color) {
    let data = {
      primary: '#409EFF',
      info: '#909399',
      success: '#67C23A',
      warning: '#E6A23C',
      danger: '#F56C6C'
    };
    return data[color];
  },
  isPC() {
    var userAgentInfo = navigator.userAgent;
    var Agents = new Array("Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod");
    var flag = true;
    for (var v = 0; v < Agents.length; v++) {
      if (userAgentInfo.indexOf(Agents[v]) > 0) {
        flag = false;
        break;
      }
    }
    if (flag) {
      if (window.innerWidth < 800) {
        flag = false;
      }
    }
    return flag;
  },
  getDialogWidth(width) {
    return RvaUtils.isPC() ? width : window.innerWidth * 0.96;
  },
  getMenuPath(path) {
    let routes = store.getters.permission_routes;
    console.log("getMenuPath routes", routes)

    let fullPath = "";
    let result = getFullPathByPath(routes, path);
    console.log('getMenuPath result ', result)

    return fullPath

    // 利用递归，将tree转化成数组结构来操作
    function getFullPathByPath(tree, id, path) {
      tree = Array.isArray(tree) ? tree : [tree]
      if (!path) {
        path = []
      }
      for (let i = 0, len = tree.length; i < len; i++) {
        let tempPath = [...path]
        tempPath.push(tree[i].path)
        if (tree[i].path === id) {
          for (let j = 0; j < tempPath.length; j++) {
            if (j == 0) {
              fullPath += tempPath[j]
            } else {
              fullPath += "/" + tempPath[j]
            }
          }
        }
        if (tree[i].children) {
          getFullPathByPath(tree[i].children, id, tempPath)
        }
      }
    }


  }

};
