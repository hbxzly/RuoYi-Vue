import request from '@/utils/request'

// 查询背景图列表
export function listBackground(query) {
  return request({
    url: '/account/background/list',
    method: 'get',
    params: query
  })
}

// 查询背景图详细
export function getBackground(keyId) {
  return request({
    url: '/account/background/' + keyId,
    method: 'get'
  })
}

// 新增背景图
export function addBackground(data) {
  return request({
    url: '/account/background',
    method: 'post',
    data: data
  })
}

// 修改背景图
export function updateBackground(data) {
  return request({
    url: '/account/background',
    method: 'put',
    data: data
  })
}

// 删除背景图
export function delBackground(keyId) {
  return request({
    url: '/account/background/' + keyId,
    method: 'delete'
  })
}
