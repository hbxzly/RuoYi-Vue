
import request from '@/utils/request'

// 查询帖子列表
export function listPosts(query) {
  return request({
    url: '/account/posts/list',
    method: 'get',
    params: query
  })
}

// 查询帖子详细
export function getPosts(keyId) {
  return request({
    url: '/account/posts/' + keyId,
    method: 'get'
  })
}

// 新增帖子
export function addPosts(data) {
  return request({
    url: '/account/posts',
    method: 'post',
    data: data
  })
}

// 修改帖子
export function updatePosts(data) {
  return request({
    url: '/account/posts',
    method: 'put',
    data: data
  })
}

// 删除帖子
export function delPosts(keyId) {
  return request({
    url: '/account/posts/' + keyId,
    method: 'delete'
  })
}
