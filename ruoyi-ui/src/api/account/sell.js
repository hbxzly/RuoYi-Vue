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
    url: '/account/sell/'+keyId ,
    method: 'get'
  })
}

// 查询卖号展示
export function getSellForShow(ids) {
  return request({
    url: '/account/sell/getSellForShow' ,
    method: 'get',
    params: {
      ids: ids.join(',')
    }
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
export function openBitBrowser(keyId){
  return request({
    url: "/account/sell/openBitBrowser/"+keyId,
    method: 'get'
  })
}

//打开浏览器
export function openHubBrowser(keyId){
  return request({
    url: "/account/sell/openHubBrowser/"+keyId,
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
export function addFriend(keyIds,id,startKeyId,endKeyId,region,note,number) {
  return request({
    url: "/account/sell/addFriend",
    method: 'get',
    params: {
      keyIds: keyIds.join(','),
      id: id,
      startKeyId: startKeyId,
      endKeyId: endKeyId,
      region: region,
      note: note
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

//创建主页
export function createPage(keyId, pageName){
  return request({
    url: "/account/sell/createPage",
    method: 'get',
    params: {
      keyId: keyId.join(','),
      pageName: pageName
    }
  })
}

//修改名字
export function changeAccountName(keyId, accountName){
  return request({
    url: "/account/sell/changeAccountName",
    method: 'get',
    params: {
      keyId: keyId.join(','),
      accountName: accountName
    }
  })
}

//修改备注
export function changeAccountNote(keyId, accountNote){
  return request({
    url: "/account/sell/changeAccountNote",
    method: 'get',
    params: {
      id: keyId.join(','),
      accountNote: accountNote
    }
  })
}

export function getEmail(keyId){
  return request({
    url: "/account/sell/getEmail/"+keyId,
    method: "get",
  })
}

export function unlockEmail(keyIds){
  return request({
    url: "/account/sell/unlockEmail/"+keyIds,
    method: "get",
  })
}

export function unlockWSVerify(keyIds){
  return request({
    url: "/account/sell/unlockWSVerify/"+keyIds,
    method: "get",
  })
}

export function confirmAddFriend(keyId){
  return request({
    url: "/account/sell/confirmAddFriend/"+keyId,
    method: "get",
  })
}


export function get2FACode(id){
  return request({
    url: "/account/sell/get2FACode/"+id,
    method: "get",
  })
}

//获取账号信息
export function getAccountInfo(payload) {
  return request({
    url: '/account/sell/getAccountInfo',
    method: 'post',
    data: payload
  })
}


// 批量搜索
export function batchSearch(data) {
  return request({
    url: '/account/sell/batchSearch',
    method: 'post',
    data: data
  })
}


export function loginEmail(keyId){
  return request({
    url: "/account/sell/loginEmail/"+keyId,
    method: "get",
  })
}

export function loginInPhone(keyId){
  return request({
    url: "/account/sell/loginInPhone/"+keyId,
    method: "get",
  })
}
