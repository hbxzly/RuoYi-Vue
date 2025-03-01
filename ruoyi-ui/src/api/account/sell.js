import request from '@/utils/request'

// 查询卖号列表
export function listSell(query) {
  return request({
    url: '/account/sell/list',
    method: 'get',
    params: query
  })
}

// 查询卖号详细
export function getSell(keyId) {
  return request({
    url: '/account/sell/' + keyId,
    method: 'get'
  })
}

// 新增卖号
export function addSell(data) {
  return request({
    url: '/account/sell',
    method: 'post',
    data: data
  })
}

// 修改卖号
export function updateSell(data) {
  return request({
    url: '/account/sell',
    method: 'put',
    data: data
  })
}

// 删除卖号
export function delSell(keyId) {
  return request({
    url: '/account/sell/' + keyId,
    method: 'delete'
  })
}

// 检测
export function checkAccount(ids) {
  return request({
    url: '/account/sell/checkAccount/' + ids,
    method: 'get'
  })
}

// 卖号标记
export function updateSellForSell(keyId) {
  return request({
    url: '/account/sell/updateSellForSell/'+keyId,
    method: 'get'
  })
}

//打开浏览器
export function openBrowser(keyId){
  return request({
    url: "/account/sell/openBrowser/"+keyId,
    method: 'get'
  })
}

//打开浏览器
export function closeBrowser(keyId){
  return request({
    url: "/account/sell/closeBrowser/"+keyId,
    method: 'get'
  })
}

//发帖
export function accountPost(keyIds){
  return request({
    url: "account/sell/accountPost/"+keyIds,
    method: "get"
  })
}


// 添加好友
export function addFriend(keyIds,id) {
  return request({
    url: "/account/sell/addFriend",
    method: 'get',
    params: {
      keyIds: keyIds.join(','),
      id: id
    }
  })
}

//跳至页面
export function jumpPage(query){
  return request({
    url: "account/sell/jumpPage",
    method: "get",
    params: query
  })
}

//检测
export function checkAccountActive(keyIds){
  return request({
    url: "account/sell/checkAccountActive/"+keyIds,
    method: "get"
  })
}

