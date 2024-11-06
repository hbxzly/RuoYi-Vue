import request from '@/utils/request'

// 查询卖号列表
export function listSell(query) {
  return request({
    url: '/account/sell/list',
    method: 'get',
    params: query
  })
}

// 查询卖号详细
export function getSell(keyId) {
  return request({
    url: '/account/sell/' + keyId,
    method: 'get'
  })
}

// 新增卖号
export function addSell(data) {
  return request({
    url: '/account/sell',
    method: 'post',
    data: data
  })
}

// 修改卖号
export function updateSell(data) {
  return request({
    url: '/account/sell',
    method: 'put',
    data: data
  })
}

// 删除卖号
export function delSell(keyId) {
  return request({
    url: '/account/sell/' + keyId,
    method: 'delete'
  })
}

// 检测
export function checkAccount(ids) {
  return request({
    url: '/account/sell/checkAccount/' + ids,
    method: 'get'
  })
}
