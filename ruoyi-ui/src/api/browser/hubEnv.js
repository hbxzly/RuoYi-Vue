import request from '@/utils/request'

// 查询Hubstudio 环境列表
export function listHubEnv(query) {
  return request({
    url: '/browser/hubEnv/list',
    method: 'get',
    params: query
  })
}

// 查询Hubstudio 环境详细
export function getHubEnv(id) {
  return request({
    url: '/browser/hubEnv/' + id,
    method: 'get'
  })
}

// 新增Hubstudio 环境
export function addHubEnv(data) {
  return request({
    url: '/browser/hubEnv',
    method: 'post',
    data: data
  })
}

// 修改Hubstudio 环境
export function updateHubEnv(data) {
  return request({
    url: '/browser/hubEnv',
    method: 'put',
    data: data
  })
}

// 删除Hubstudio 环境
export function delHubEnv(id) {
  return request({
    url: '/browser/hubEnv/' + id,
    method: 'delete'
  })
}

export function getHubEnvList(){
  return request({
    url: '/browser/hubEnv/getHubEnvList',
    method: "get"
  })
}
