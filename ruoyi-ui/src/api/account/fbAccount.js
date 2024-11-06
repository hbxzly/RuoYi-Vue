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
export function getAccount(id) {
  return request({
    url: '/account/fbAccount/' + id,
    method: 'get'
  })
}

// 修改账号信息
export function updateAccount(data) {
  return request({
    url: '/account/fbAccount/',
    method: 'put',
    data: data
  })
}

// 添加账号信息
export function addAccount(account) {
  return request({
    url: '/account/fbAccount/add',
    method: 'post',
    data: account
  })
}

// 打开浏览器
export function openBrowser(id) {
  return request({
    url: '/account/fbAccount/openBrowser/' + id,
    timeout: 1000000,
    method: 'get'
  })
}

// 创BM
export function createBM(id) {
  return request({
    url: '/account/fbAccount/createBM/' + id,
    method: 'get'
  })
}

// 进入BM
export function checkBM(id) {
  var ids = [];
  ids.push(id);
  return request({
    url: '/account/fbAccount/checkBM/',
    data: ids,
    method: 'post'
  })
}

// 查看账号品质
export function checkAccount(id) {
  var ids = [];
  ids.push(id);
  return request({
    url: '/account/fbAccount/checkAccount/',
    data: ids,
    method: 'post'
  })
}

// 收集广告信息
export function collectAdsInfo(id) {
  return request({
    url: '/account/fbAccount/collectAdsInfo/' + id,
    method: 'get'
  })
}

// 查看账号品质
export function openAds(id) {
  return request({
    url: '/account/fbAccount/openAds/' + id,
    method: 'get'
  })
}

// 显示浏览器
export function showBrowser(id) {
  return request({
    url: '/account/fbAccount/showBrowser/' + id,
    method: 'get'
  })
}

// 关闭浏览器
export function closeBrowser(id) {
  return request({
    url: '/account/fbAccount/closeBrowser/' + id,
    method: 'get'
  })
}

//批量打开
export function multipleOpenBrowser(ids) {
  return request({
    url: '/account/fbAccount/multipleOpenBrowser/' + ids,
    method: 'get'
  })
}

//批量关闭
export function closeAllBrowser() {
  return request({
    url: '/account/fbAccount/closeAllBrowser',
    method: 'get'
  })
}

//删除账号
export function delFbaccount(fbaccountIds) {
  return request({
    url: '/account/fbAccount/' + fbaccountIds,
    method: 'delete'
  })
}

//更多操作
export function moreOperate(ids,operateId){
  const data = {
    ids,
    operateId
  }
  return request({
    url: '/account/fbAccount/moreOperate/',
    data: data,
    method: 'post'
  })
}

//批量添加好友
export function batchAddFriend(operationAccount,friendAccount){
  const data = {
    operationAccount,
    friendAccount
  }
  return request({
    url: '/account/fbAccount/batchAddFriend/',
    data: data,
    method: 'post'
  })
}

//查看账号状态信息
export function checkAccountInfo(ids) {
  return request({
    url: '/account/fbAccount/checkAccountInfo/' + ids,
    method: 'get'
  })
}

//修改密码
export function changePassword(id){
  return request({
    url: '/account/fbAccount/changePassword/' + id,
    method: 'get'
  })
}

//修改密码
export function unlockAccount(id){
  return request({
    url: '/account/fbAccount/unlockAccount/' + id,
    method: 'get'
  })
}

//创建主页
export function createPage(ids,pageName) {
  const data = {
    ids,
    pageName
  }
  return request({
    url: '/account/fbAccount/createPage/',
    data: data,
    method: 'post'
  })
}


