import request from '@/utils/request'

// 查询账号列表
export function listFbAccount(query) {
  return request({
    url: '/account/fbAccount/list',
    method: 'get',
    params: query
  })
}

// 查询账号详细
export function getFbAccount(keyId) {
  return request({
    url: '/account/fbAccount/' + keyId,
    method: 'get'
  })
}

// 新增账号
export function addFbAccount(data) {
  return request({
    url: '/account/fbAccount',
    method: 'post',
    data: data
  })
}

// 修改账号
export function updateFbAccount(data) {
  return request({
    url: '/account/fbAccount',
    method: 'put',
    data: data
  })
}

// 删除账号
export function delFbAccount(keyId) {
  return request({
    url: '/account/fbAccount/' + keyId,
    method: 'delete'
  })
}

// 打开账号
export function openBrowser(keyId) {
  return request({
    url: '/account/fbAccount/openBrowser/' + keyId,
    method: 'get'
  })
}

// 关闭账号
export function closeBrowser(keyId) {
  return request({
    url: '/account/fbAccount/closeBrowser/' + keyId,
    method: 'get'
  })
}

// 添加好友
export function addFriend(keyIds,id) {
  return request({
    url: `/account/fbAccount/addFriend`,
    method: 'get',
    params: {
      keyIds: keyIds.join(','),
      id: id
    }
  })
}

//检测账号
export function checkAccount(keyIds){
  return request({
    url: '/account/fbAccount/checkAccount/' + keyIds,
    method: "get"
  })
}


//账号发帖
export function accountPost(keyIds){
  return request({
    url: '/account/fbAccount/accountPost/' + keyIds,
    method: "get"
  })
}
