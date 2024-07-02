import request from '@/utils/request'

// 查询账号账户信息列表
export function listInfo(query) {
  return request({
    url: '/account/info/list',
    method: 'get',
    params: query
  })
}

// 查询账号账户信息详细
export function getInfo(keyId) {
  return request({
    url: '/account/info/' + keyId,
    method: 'get'
  })
}

// 新增账号账户信息
export function addInfo(data) {
  return request({
    url: '/account/info',
    method: 'post',
    data: data
  })
}

// 修改账号账户信息
export function updateInfo(data) {
  return request({
    url: '/account/info',
    method: 'put',
    data: data
  })
}
// 修改账号账户信息
export function openAds(row) {
  return request({
    url: '/account/info/openAds',
    method: 'put',
    data: row
  })
}

// 删除账号账户信息
export function delInfo(keyId) {
  return request({
    url: '/account/info/' + keyId,
    method: 'delete'
  })
}
