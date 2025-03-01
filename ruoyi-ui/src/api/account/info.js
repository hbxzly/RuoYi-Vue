import request from '@/utils/request'

// 查询创建信息列表
export function listInfo(query) {
  return request({
    url: '/account/info/list',
    method: 'get',
    params: query
  })
}

// 查询创建信息详细
export function getInfo(keyId) {
  return request({
    url: '/account/info/' + keyId,
    method: 'get'
  })
}

// 新增创建信息
export function addInfo(data) {
  return request({
    url: '/account/info',
    method: 'post',
    data: data
  })
}

// 修改创建信息
export function updateInfo(data) {
  return request({
    url: '/account/info',
    method: 'put',
    data: data
  })
}

// 删除创建信息
export function delInfo(keyId) {
  return request({
    url: '/account/info/' + keyId,
    method: 'delete'
  })
}

// 打开账号
export function openBrowser(keyId) {
  return request({
    url: '/account/info/openBrowser/' + keyId,
    method: 'get'
  })
}

// 更新账号
export function updateAccountInfo(payload) {
  return request({
    url: '/account/info/updateAccountInfo',
    method: 'post', // 使用 POST 请求以发送更复杂的数据
    data: payload, // 将参数作为请求体发送
  });
}


// 创建
export function createInfo(keyId) {
  return request({
    url: '/account/info/create/' + keyId,
    method: 'get'
  })
}

// 打开账号
export function checkAccountActive(keyIds) {
  return request({
    url: '/account/info/checkAccountActive/' + keyIds,
    method: 'get'
  })
}



