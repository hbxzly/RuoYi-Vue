import request from '@/utils/request'

// 查询头像列表
export function listAvatar(query) {
  return request({
    url: '/account/avatar/list',
    method: 'get',
    params: query
  })
}

// 查询头像详细
export function getAvatar(keyId) {
  return request({
    url: '/account/avatar/' + keyId,
    method: 'get'
  })
}

// 新增头像
export function addAvatar(data) {
  return request({
    url: '/account/avatar',
    method: 'post',
    data: data
  })
}

// 修改头像
export function updateAvatar(data) {
  return request({
    url: '/account/avatar',
    method: 'put',
    data: data
  })
}

// 删除头像
export function delAvatar(keyId) {
  return request({
    url: '/account/avatar/' + keyId,
    method: 'delete'
  })
}
