import request from '@/utils/request'

/**
 * 查询流程当前任务对应的名称、表单视图loadUrl等信息
 * @param appId 流程对应的应用ID
 * @param bizKey 流程业务横表记录ID
 * @returns {AxiosPromise} then入参res，则res.data = {name: '任务名称', url: '表单视图loadUrl'}
 */
export function getTaskViewData(appId, bizKey) {
  return request({
    url: `/rva/activiti/${appId}/${bizKey}/load/view`,
    method: 'post'
  })
}

/**
 * 查询流程日志数据
 * @param appId 流程对应的应用ID
 * @param bizKey 流程业务横表记录ID
 * @returns {AxiosPromise} then入参res，则res.data = [{name: '任务名称', url: '表单视图loadUrl', time: '2001-09-08 11:12:14'}]
 */
export function getTaskLogData(appId, bizKey) {
  return request({
    url: `/rva/activiti/${appId}/${bizKey}/load/logs`,
    method: 'post'
  })
}
