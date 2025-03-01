import request from '@/utils/request'

// 查询代理列表
export function listIp(query) {
  return request({
    url: '/account/ip/list',
    method: 'get',
    params: query
  })
}

// 查询代理详细
export function getIp(keyId) {
  return request({
    url: '/account/ip/' + keyId,
    method: 'get'
  })
}

// 新增代理
export function addIp(data) {
  return request({
    url: '/account/ip',
    method: 'post',
    data: data
  })
}

// 修改代理
export function updateIp(data) {
  return request({
    url: '/account/ip',
    method: 'put',
    data: data
  })
}

// 删除代理
export function delIp(keyId) {
  return request({
    url: '/account/ip/' + keyId,
    method: 'delete'
  })
}
