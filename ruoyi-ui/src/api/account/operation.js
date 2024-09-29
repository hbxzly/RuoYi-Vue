import request from '@/utils/request'

// 查询操作记录列表
export function listOperation(query) {
  return request({
    url: '/account/operation/list',
    method: 'get',
    params: query
  })
}

// 查询操作记录详细
export function getOperation(keyId) {
  return request({
    url: '/account/operation/' + keyId,
    method: 'get'
  })
}

// 新增操作记录
export function addOperation(data) {
  return request({
    url: '/account/operation',
    method: 'post',
    data: data
  })
}

// 修改操作记录
export function updateOperation(data) {
  return request({
    url: '/account/operation',
    method: 'put',
    data: data
  })
}

// 删除操作记录
export function delOperation(keyId) {
  return request({
    url: '/account/operation/' + keyId,
    method: 'delete'
  })
}
