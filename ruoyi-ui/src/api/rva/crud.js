import request from '@/utils/request'
import {RvaUtils} from "@/api/rva/util";

// 查询crud元数据
export function loadCrud(id) {
  return request({
    url: '/rva/app/' + id,
    method: 'get'
  })
}

// 查询列表数据
export function listCrud(url, params) {
  return request({
    url: url,
    method: 'post',
    data: params
  })
}

// 查询列表数据
export function updateObject(objId, keyPropValue, fieldValues) {
  return request({
    url: '/rva/metaapp/object/' + objId + '/update',
    method: 'post',
    data: {keyPropValue, fieldValues}
  })
}

// 加载多个表单视图（元）数据.
export function loadForms(createView, updateView, params) {
  return request({
    url: `/rva/view/${createView}/${updateView}/load/forms`,
    method: 'post',
    data: params
  })
}

// 异步更新某列数据
export function listColumn(url, vals) {
  return request({
    url: url,
    method: 'get',
    params: {ids: vals}
  })
}

export function handleRvaButtonClick(component, actions) {
  console.log('handleRvaButtonClick', component)
  for (var i = actions.length - 1; i >= 0; i--) {
    var action = actions[i];
    if (component[action.action]) {
      component[action.action](action);
      actions.splice(i, 1);
      console.log('handleRvaButtonClick-doAction', action.action)
    }
  }
  if (actions.length > 0) {
    component.$emit('rva-button-click', actions);
  }
}

export function getRvaViewData(id, params, context, callback) {
  params = params || {};
  if (id.indexOf('/') >= 0) {
    request({
      url: id,
      method: 'post',
      data: params
    }).then(response2 => {
      context.viewData = response2.data.viewData;
      context.formData = response2.data.formData;
      if (callback) {
        callback(response2.data, 1);
      }
    }).catch(err => {
      console.log('getViewData failed:', err);
      if (callback) {
        callback(err, -1);
      }
    });
    return;
  }
  request({
    url: '/rva/view/' + id,
    method: 'post',
    data: params
  }).then(response => {
    context.viewData = response.data;
    let loadUrl = response.data.loadUrl;
    if (response.data.type == 'form' && loadUrl) {
      request({
        url: loadUrl,
        method: 'post',
        data: params
      }).then(response2 => {
        context.viewData = response2.data.viewData;
        context.formData = response2.data.formData;
        if (callback) {
          callback(response2.data, 1);
        }
      }).catch(err2 => {
        console.log('getViewData failed:', err2, loadUrl);
        if (callback) {
          callback(err2, -2);
        }
      });
    } else {
      if (callback) {
        console.log('getViewData not loadFormData', response.data);
        callback(response.data, 0);
      }
    }
  }).catch(err => {
    console.log('getViewData failed:', id, err);
    if (callback) {
      callback(err, -1);
    }
  });
}

// rowsPropData数据结构：
//      [{
//         "rowSize": 1,
//         "cols": [{
//           "rowSpan": 1,
//           "colSpan": 24,
//           "rows": [{
//             "rowSpan": 1,
//             "cols": [{
//               "prop": {},
//               "span": 24
//             }]
//           }]
//         }]
//       }]
// 新增模块
export function createRowsPropData(viewData, rowsPropData) {
  var columns = RvaUtils.isPC() ? viewData.formColumns : 1;
  // formColSpan配置的是1、2，需要转化为elementUI的24栅格
  var avgSpan = 24 / columns;

  if (!rowsPropData) {
    rowsPropData = [];
  }

  function createNewRow(rowsPropData, p) {
    // 列span
    var span = avgSpan * p.formColSpan;
    if (span > 24) {
      span = 24;
    }
    rowsPropData.push({
      // 每行的rowSize取其第一个prop的formRowSpan，作为其中所有列的最大行数限制
      rowSize: p.formRowSpan,
      cols: [{
        // 每列的colSpan，作为其中所有列的最大行数
        colSpan: span,
        rows: [{
          // 每列的实际rowSpan
          rowSpan: p.hide ? 0 : p.formRowSpan,
          cols: [{
            prop: p,
            span: 24
          }]
        }]
      }]
    });
  }

  function sum(arr, prop) {
    var sum = 0;
    arr.forEach(function (a) {
      sum += a[prop];
    });
    return sum;
  }

  function addProp(rowsPropData, p) {
    var lastRow = rowsPropData[rowsPropData.length - 1];
    var lastCol = lastRow.cols[lastRow.cols.length - 1];
    var rowDiffer = lastRow.rowSize - sum(lastCol.rows, 'rowSpan');
    var colSum = sum(lastRow.cols, 'colSpan');
    // 判断lastCol里面的行是否排满
    if (rowDiffer > 0) {// 没有排满，lastCol中增加行
      lastCol.rows.push({
        rowSpan: p.formRowSpan > rowDiffer ? rowDiffer : p.formRowSpan,
        cols: [{
          prop: p,
          span: 24
        }]
      });
    } else if (colSum < 24) {// lastRow中增加新列
      lastRow.cols.push({
        colSpan: p.formColSpan * avgSpan > (24 - colSum) ? (24 - colSum) : p.formColSpan * avgSpan,
        rows: [{
          rowSpan: p.formRowSpan,
          cols: [{
            prop: p,
            span: 24
          }]
        }]
      });
    } else {// 创建新行
      createNewRow(rowsPropData, p);
    }
  }

  console.log('viewData', viewData);
  let props = viewData.properties.filter(p => {
    let uiType = RvaUtils.parseValue(viewData.data, 'uiType', 'form');
    if (uiType == 'tabs' && p.type == 'crud') {
      return false;
    }
    return true;
  });
  for (var i in props) {
    var p = props[i];
    if (p.type == 'hidden') {
      continue;
    }
    if (rowsPropData.length == 0) {
      createNewRow(rowsPropData, p)
    } else {
      addProp(rowsPropData, p);
    }
  }
  return rowsPropData;
}
