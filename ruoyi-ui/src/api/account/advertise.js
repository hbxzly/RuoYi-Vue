import request from '@/utils/request'

// 查询广告信息列表
export function listAdvertise(query) {
  return request({
    url: '/account/advertise/list',
    method: 'get',
    params: query
  })
}

// 查询广告信息详细
export function getAdvertise(keyId) {
  return request({
    url: '/account/advertise/' + keyId,
    method: 'get'
  })
}

// 新增广告信息
export function addAdvertise(data) {
  return request({
    url: '/account/advertise',
    method: 'post',
    data: data
  })
}

// 修改广告信息
export function updateAdvertise(data) {
  return request({
    url: '/account/advertise',
    method: 'put',
    data: data
  })
}

// 删除广告信息
export function delAdvertise(keyId) {
  return request({
    url: '/account/advertise/' + keyId,
    method: 'delete'
  })
}

// 显示浏览器
export function openAdvertise(adAccountId) {
  return request({
    url: '/account/advertise/openAd/' + adAccountId,
    method: 'post'
  })
}

export function openScreenshotPage(keyId) {
  console.log(keyId)
  var keyIds = [];
  keyIds.push(keyId);
  return request({
    url: '/account/advertise/multipleOpenScreenshotPage/' + keyId,
    method: 'get'
  })
}


export function multipleOpenScreenshotPage(keyIds) {
  return request({
    url: '/account/advertise/multipleOpenScreenshotPage/' + keyIds,
    method: 'get'
  })
}
