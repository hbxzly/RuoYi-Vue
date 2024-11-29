import request from '@/utils/request'

// 查询email列表
export function listEmail(query) {
  return request({
    url: '/account/email/list',
    method: 'get',
    params: query
  })
}

// 查询email详细
export function getEmail(keyId) {
  return request({
    url: '/account/email/' + keyId,
    method: 'get'
  })
}

// 新增email
export function addEmail(data) {
  return request({
    url: '/account/email',
    method: 'post',
    data: data
  })
}

// 修改email
export function updateEmail(data) {
  return request({
    url: '/account/email',
    method: 'put',
    data: data
  })
}

// 删除email
export function delEmail(keyId) {
  return request({
    url: '/account/email/' + keyId,
    method: 'delete'
  })
}

export function getMessage(requestData){
  return request({
    url: '/account/email/getMessage',
    method: 'put',
    data: requestData
  })
}

export function unlockEmail(keyId){
  return request({
    url: '/account/email/unlockEmail/'+keyId,
    method: 'get'
  })
}
