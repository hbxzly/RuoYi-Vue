import request from '@/utils/request'

// 查询创建设备列表
export function listDevice(query) {
  return request({
    url: '/account/device/list',
    method: 'get',
    params: query
  })
}

// 查询创建设备详细
export function getDevice(keyId) {
  return request({
    url: '/account/device/' + keyId,
    method: 'get'
  })
}

// 新增创建设备
export function addDevice(data) {
  return request({
    url: '/account/device',
    method: 'post',
    data: data
  })
}

// 修改创建设备
export function updateDevice(data) {
  return request({
    url: '/account/device',
    method: 'put',
    data: data
  })
}

// 删除创建设备
export function delDevice(keyId) {
  return request({
    url: '/account/device/' + keyId,
    method: 'delete'
  })
}

export function getDevices(){
  return request({
    url:  '/account/device/getDevices',
    method: 'get'
  })
}

export function openDevice(keyId){
  return request({
    url:  '/account/device/openDevice/'+keyId,
    method: 'get'
  })
}
