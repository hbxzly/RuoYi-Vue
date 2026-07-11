# Hubstudio API 本地整理文档

- 来源：https://api-docs.hubstudio.cn/
- 文档页数：70
- API 接口数：66
- 默认服务地址：`http://127.0.0.1:6873`

## 使用说明

Local API 开启安全校验时，请在请求头传入 `Authorization`，API Key 来自客户端。多数接口还支持 `Accept-Language: zh-CN`。

## 接口速查

| 模块 | 接口 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- | --- |
| 环境管理 | 获取环境列表 | POST | `/api/v1/env/list` | 查询环境的信息。用户仅能查询自己有权限的环境信息 |
| 环境管理 | 创建环境 | POST | `/api/v1/env/create` | 创建环境，支持配置环境的名称、备注、分组和代理信息。创建成功后返回环境ID |
| 环境管理 | 更新环境 | POST | `/api/v1/env/update` | 修改环境参数，包括备注信息和分组名称。更新成功返回true。 |
| 环境管理 | 更新环境代理 | POST | `/api/v1/env/proxy/update` | 修改指定环境的代理信息，包括代理主机、端口、帐号、密码等。更新成功返回true |
| 环境管理 | 导入Cookie | POST | `/api/v1/env/import-cookie` | 向指定环境导入cookie，导入成功返回true。 |
| 环境管理 | 导出Cookie | POST | `/api/v1/env/export-cookie` | 导出指定环境的cookie，导出成功返回cookie的json串 |
| 环境管理 | 删除环境 | POST | `/api/v1/env/del` | 删除指定环境。删除成功返回true。一次性最多支持删除环境1000个。 |
| 环境管理 | 获取随机UA | POST | `/api/v1/env/random-ua` | 获取随机UA，获取成功返回UA |
| 环境管理 | 清除环境本地缓存 | POST | `/api/v1/cache/clear` | 清除环境本地缓存 |
| 环境管理 | 清理环境内插件缓存 | POST | `/api/v1/browser/reset-extension` | 清理环境内插件缓存，清理成功后插件的所有数据均会被删除 |
| 环境管理 | 刷新指纹 | POST | `/api/v1/env/refresh-fingerprint` | - 刷新指纹 |
| 环境管理 | 查询webglVendor和webglRenderer | POST | `/api/v1/container/webgl-renderer-list` | 查询WebglInfo信息 |
| 环境管理 | 批量修改备注 | POST | `/api/v1/container/batch-update-remark` | 批量修改多个环境的备注 |
| 环境管理 | 下载内核 | POST | `/api/v1/browser/download-core` | 下载环境内核 |
| 环境管理 | 更新环境基础信息 | POST | `/api/v1/container/update-container-base` | 默认请求语言 |
| 浏览器环境 | 打开环境 | POST | `/api/v1/browser/start` | - 用于启动指定的环境，启动成功后可以获取浏览器debug端口用于执行selenium和puppeteer自动化脚本 |
| 浏览器环境 | 关闭环境 | POST | `/api/v1/browser/stop` | 关闭指定环境 |
| 浏览器环境 | 关闭所有环境 | POST | `/api/v1/browser/stop-all` | 支持关闭所有环境，接口参数传 true 会清空启动环境队列。 |
| 浏览器环境 | 获取浏览器状态 | POST | `/api/v1/browser/all-browser-status` | 获取浏览器状态的接口，返回浏览器环境code以及状态 |
| 浏览器环境 | 切换浏览器窗口 | POST | `/api/v1/browser/foreground` | 切换浏览器窗口，将窗口置顶显示 |
| 浏览器环境 | 获取全部屏幕（物理机的屏幕） | POST | `/api/v1/display/all` | 用于获取全部屏幕，获取到的屏幕id可用于浏览器环境窗口自定义排列。 |
| 浏览器环境 | 浏览器窗口自定义排列 | POST | `/api/v1/browser/arrange` | 用于已打开浏览器窗口自定义排列，不传值则使用默认值进行浏览器窗口排列 |
| 云手机 > 应用管理 | APP列表(分页)\查询可安装应用列表 | POST | `/api/v1/cloud-mobile/app/page` | 获取APP应用列表 |
| 云手机 > 应用管理 | 已安装应用列表查询 | POST | `/api/v1/cloud-mobile/app/installedList` | 查询云手机已安装应用列表查询 |
| 云手机 > 应用管理 | 新增团队应用 | POST | `/api/v1/cloud-mobile/group/app/create` | 新增团队应用，可以在客户端-云手机-云手机应用-团队应用中查看 |
| 云手机 > 应用管理 | app应用安装 | POST | `/api/v1/cloud-mobile/app/install` | 安装指定应用APP |
| 云手机 > 应用管理 | APP启动 | POST | `/api/v1/cloud-mobile/app/start` | 启动云手机中安装的app |
| 云手机 > 应用管理 | APP重启 | POST | `/api/v1/cloud-mobile/app/restart` | 重启云手机中安装的app |
| 云手机 > 应用管理 | APP停止 | POST | `/api/v1/cloud-mobile/app/stop` | 停止云手机运行中的app |
| 云手机 > 应用管理 | APP卸载 | POST | `/api/v1/cloud-mobile/app/uninstall` | 卸载云手机已安装的app |
| 云手机 > 文件管理 | 选择本地上传文件到云手机 | POST | `/api/v2/cloud-mobile/upload-file` | 上传文件到云手机 |
| 云手机 > 文件管理 | 公网文件上传文件到云手机 | POST | `/api/v1/cloud-mobile/upload-file` | 上传文件到云手机 |
| 云手机 > 文件管理 | 设置keyBox | POST | `/api/v1/cloud-mobile/setKeyBox` | 设置云手机keybox，只有在云手机开机的情况下才能进行设置，同步keybox的xml文件已经上传到云手机上 |
| 云手机 > RPA | RPA-计划分页查询 | POST | `/api/v1/cloud-mobile/rpa/task/page` | 查询计划任务数据 |
| 云手机 > RPA | RPA-个人模板分页查询 | POST | `/api/v1/cloud-mobile/rpa/template/personal/page` | 个人模板数据查询 |
| 云手机 > RPA | RPA-市场模板分页查询 | POST | `/api/v1/cloud-mobile/rpa/template/market/page` | 默认请求语言 |
| 云手机 > RPA | RPA-保存计划 | POST | `/api/v1/cloud-mobile/rpa/task/save` | 默认请求语言 |
| 云手机 > RPA | RPA-取消计划 | POST | `/api/v1/cloud-mobile/rpa/task/cancel` | 默认请求语言 |
| 云手机 > RPA | RPA-执行记录分页查询 | POST | `/api/v1/cloud-mobile/rpa/subTask/page` | 默认请求语言 |
| 云手机 > RPA | RPA-取消记录 | POST | `/api/v1/cloud-mobile/rpa/subTask/cancel` | 默认请求语言 |
| 云手机 > RPA | RPA-记录详情查询 | POST | `/api/v1/cloud-mobile/rpa/subTask/detail` | 默认请求语言 |
| 云手机 > RPA | RPA-快速保存一次性计划 | POST | `/api/v1/cloud-mobile/rpa/onceTask/save` | 默认请求语言 |
| 云手机 | 云手机商品列表 | POST | `/api/v1/cloud-mobile/mobile-product-list` | 获取云手机商品列表 |
| 云手机 | 云手机分页列表 | POST | `/api/v1/cloud-mobile/mobile-page` | 获取云手机分页列表 |
| 云手机 | 添加云手机 | POST | `/api/v1/cloud-mobile/add-mobile` | 可创建新的按需云手机（暂不支持创建按月使用的云手机） |
| 云手机 | 国家时区语言列表 | POST | `/api/v1/cloud-mobile/get-country-time-zone-language-list` | 获取云手机的国家时区语言列表 |
| 云手机 | 批量开启云手机 | POST | `/api/v1/cloud-mobile/power-on-mobile` | 批量开启云手机 |
| 云手机 | 批量关闭云手机 | POST | `/api/v1/cloud-mobile/shutdown-mobile` | 批量关闭云手机 |
| 云手机 | 更新代理 | POST | `/api/v1/cloud-mobile/update-proxy` | 更新云手机代理 |
| 云手机 | 批量获取云手机ADB状态 | POST | `/api/v1/cloud-mobile/list-adb` | 批量获取云手机的ADB状态 |
| 云手机 | 批量更新云手机ADB状态 | POST | `/api/v1/cloud-mobile/batch-update-adb` | 批量更新云手机的ADB状态 |
| 云手机 | 一键新机 | POST | `/api/v1/cloud-mobile/new-machine` | 执行云手机一键新机操作 |
| 云手机 | 获取一键新机状态及可用数量 | POST | `/api/v1/cloud-mobile/new-machine-status` | 获取云手机一键新机的状态及可用数量 |
| 云手机 | 查询品牌机型 | POST | `/api/v1/cloud-mobile/brand/models` | 查询对应安卓版本的可用品牌机型 |
| 云手机 | 执行shell命令 | POST | `/api/v1/cloud-mobile/exe-command` | 云手机执行shell命令 |
| 云手机 | 修改云手机信息 | POST | `/api/v1/cloud-mobile/update` | 修改云手机名称、备注、序号 |
| 云手机 | 发送短信到云手机 | POST | `/api/v1/cloud-mobile/simulateSendSms` | 发送短信到云手机 |
| 云手机 | 批量删除云手机 | POST | `/api/v1/cloud-mobile/del-mobile-batch` | 批量删除云手机 |
| 云手机 | 批量修改云手机分组 | POST | `/api/v1/cloud-mobile/set-tag` | 批量设置云手机的分组名 |
| 平台账号管理 | 账号分页列表 | POST | `/api/v1/account/list` | - 查询平台账号的信息。用户仅能查询自己有权限的平台账号信息 |
| 平台账号管理 | 账号更新 | POST | `/api/v1/account/update` | 修改账号信息 |
| 平台账号管理 | 添加环境账号 | POST | `/api/v1/container/add-account` | 为环境添加账号信息 |
| 平台账号管理 | 账号删除 | POST | `/api/v1/account/del` | 删除账号信息 |
| 分组管理 | 获取环境分组列表 | POST | `/api/v1/group/list` | 查询当前团队内的浏览器环境分组名称。查询成功返回分组名称和分组ID |
| 分组管理 | 新建环境分组 | POST | `/api/v1/group/create` | 添加环境的分组，名称不能重复 |
| 分组管理 | 删除环境分组 | POST | `/api/v1/group/del` | 删除指定名称的环境分组。删除成功后返回true |

## 非接口文档

- [API 使用说明文档](https://api-docs.hubstudio.cn/8331450m0.md)
- [Linux Server 部署与自动化指南](https://api-docs.hubstudio.cn/8945881m0.md)
- [Hubstudio CLI 命令行操作指南](https://api-docs.hubstudio.cn/8982261m0.md)
- 云手机 > RPA [RPA模板参数填写规范](https://api-docs.hubstudio.cn/8441493m0.md)

## API 详细定义


### 环境管理

#### 获取环境列表 `POST /api/v1/env/list`

查询环境的信息。用户仅能查询自己有权限的环境信息

来源：https://api-docs.hubstudio.cn/380052376e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/env/list:
    post:
      summary: 获取环境列表
      deprecated: false
      description: 查询环境的信息。用户仅能查询自己有权限的环境信息
      tags:
        - 环境管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                containerCodes:
                  type: array
                  items:
                    type: string
                  title: 指定环境ID查询环境
                containerName:
                  type: string
                  title: 指定环境名称查询环境
                createEndTime:
                  description: 'example: yyyy-MM-dd HH:mm:ss'
                  type: string
                  title: 创建时间-截止时间
                createStartTime:
                  description: 'example: yyyy-MM-dd HH:mm:ss'
                  type: string
                  title: 创建时间-起始时间
                ipAddress:
                  type: string
                  title: IP地址查询
                proxyTypeNames:
                  type: array
                  items:
                    type: string
                  description: >-
                    HTTP、HTTPS、SSH、Socks5、Oxylabsauto、Lumauto 、Luminati、
                    smartproxy、IPIDEA、Iphtml、不使用代理
                  title: 代理类型
                remark:
                  type: string
                  title: 指定环境备注信息查询环境
                tagNames:
                  type: array
                  items:
                    type: string
                  description: 查询指定分组的环境
                  title: 环境分组名称数组
                current:
                  type: integer
                  title: 当前页面
                size:
                  description: 最多200条。
                  type: integer
                  title: 分页条数
                serviceProvider:
                  description: ROLA_IP、922S5、通用api
                  type: string
                  title: 环境内代理所属服务商
                tagCode:
                  description: 默认不传参，若需要查询“未分组”的环境，传任意
                  type: integer
                  title: 分组编号
                serialNumbers:
                  type: array
                  items:
                    type: integer
                  title: 环境序号
                  description: 最多不超过200个
              x-apifox-orders:
                - containerCodes
                - serialNumbers
                - containerName
                - createEndTime
                - createStartTime
                - ipAddress
                - proxyTypeNames
                - remark
                - tagCode
                - tagNames
                - current
                - size
                - serviceProvider
            example: {}
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  msg:
                    type: string
                    title: 业务消息
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: object
                    properties:
                      list:
                        type: array
                        items:
                          type: object
                          properties:
                            allOpenTime:
                              type: string
                              title: 环境最后打开时间
                            asDynamicType:
                              description: 1-静态，2-动态
                              type: integer
                              title: 代理使用方式
                            containerCode:
                              type: integer
                              title: 环境ID
                            serialNumber:
                              type: integer
                              title: 环境序号
                            containerName:
                              type: string
                              title: 环境名称
                            createTime:
                              type: string
                              title: 创建时间
                            lastCity:
                              type: string
                              title: 上一次IP的城市
                            lastCountry:
                              type: string
                              title: 上一次IP的国家
                            lastRegion:
                              type: string
                              title: 洲或省的名称
                            lastUsedIp:
                              type: string
                              title: 上一次使用的IP
                            openTime:
                              type: string
                              title: 打开时间
                            proxyHost:
                              type: string
                              title: 代理主机
                            proxyPort:
                              type: integer
                              title: 代理端口号
                            proxyTypeName:
                              type: string
                              title: 代理类型
                            proxyAccount:
                              type: string
                              title: 代理ip的账号
                            proxyPassword:
                              type: string
                              title: 代理ip的密码
                            refreshUrl:
                              type: string
                              title: 刷新URL
                            tagName:
                              type: string
                              title: 环境分组名称
                            tagCode:
                              type: string
                              title: 环境名ID
                            ua:
                              description: 仅支持3.39.0及以上客户端版本
                              type: string
                              title: 环境ua
                            referenceCountryCode:
                              type: string
                            01KS2DEAW8XQ1ZJXC6GE3FPAAN:
                              type: string
                          required:
                            - proxyPassword
                            - refreshUrl
                            - ua
                          x-apifox-orders:
                            - containerCode
                            - 01KS2DEAW8XQ1ZJXC6GE3FPAAN
                            - allOpenTime
                            - asDynamicType
                            - containerName
                            - createTime
                            - lastCity
                            - lastCountry
                            - lastRegion
                            - lastUsedIp
                            - openTime
                            - proxyHost
                            - proxyPort
                            - proxyTypeName
                            - tagName
                            - tagCode
                            - referenceCountryCode
                            - serialNumber
                            - proxyAccount
                            - proxyPassword
                            - refreshUrl
                            - ua
                      total:
                        type: integer
                        title: 统计数量
                    x-apifox-orders:
                      - list
                      - total
                    title: 业务数据
                x-apifox-orders:
                  - msg
                  - code
                  - data
              example:
                msg: Success
                code: 0
                data:
                  list:
                    - allOpenTime: 10-19 17:51:11
                      asDynamicType: 1
                      containerCode: 8256337
                      containerName: 带cookie的阿基3
                      createTime: '2022-09-14 17:12:20'
                      lastCity: London
                      lastCountry: United Kingdom
                      lastRegion: England
                      lastUsedIp: 8.208.80.219
                      openTime: '2022-10-19 17:51:12'
                      proxyHost: 8.208.80.219
                      proxyPort: 32080
                      proxyTypeName: Socks5
                      tagName: 小九九的分组
                      tagCode: XXXX
                    - allOpenTime: 09-14 17:10:35
                      asDynamicType: 1
                      containerCode: 8256321
                      containerName: 带cookie的阿基2
                      createTime: '2022-09-14 17:04:11'
                      openTime: '2022-09-14 17:10:36'
                      proxyHost: '8.208.80.219 '
                      proxyPort: 32080
                      proxyTypeName: Socks5
                      referenceCountryCode: smartproxy
                      tagName: 小九九的分组
                      tagCode: XXXX
                  total: 2
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 环境管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052376-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 创建环境 `POST /api/v1/env/create`

创建环境，支持配置环境的名称、备注、分组和代理信息。创建成功后返回环境ID

来源：https://api-docs.hubstudio.cn/380052377e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/env/create:
    post:
      summary: 创建环境
      deprecated: false
      description: 创建环境，支持配置环境的名称、备注、分组和代理信息。创建成功后返回环境ID
      tags:
        - 环境管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                containerName:
                  description: 限制60字以内
                  type: string
                  title: 环境名
                remark:
                  type: string
                  title: 环境备注信息
                tagName:
                  description: 若分组名称不存在，将默认环境未分组。
                  type: string
                  title: 指定环境所属分组的名称
                cookie:
                  description: 支持JSON格式的cookie
                  type: string
                  title: 设置cookie
                asDynamicType:
                  description: 0-关闭提醒(默认)1-开启提醒
                  type: integer
                  title: IP变更提醒
                proxyTypeName:
                  type: string
                  description: >-
                    HTTP、HTTPS、SSH、Socks5、Oxylabsauto、Lumauto_HTTP
                    、Lumauto_HTTPS 、Luminati_HTTP、Luminati_HTTPS、
                    smartproxy、Iphtml_HTTP、Iphtml_Socks5、IPIDEA、不使用代理             
                    2、API提取代理类型：  Socks5_ROLA_IP、HTTPS_ROLA_IP、
                    Socks5_922S5、HTTP_922S5、HTTPS_922S5、
                    Socks5_通用api、HTTP_通用api、HTTPS_通用api、Socks5_IPIDEA-API、HTTP_IPIDEA-API、HTTPS_IPIDEA-API
                  title: 自定义代理类型
                ipGetRuleType:
                  description: 1-IP失效时提取新IP ，2-，每次打开环境时提取新IP。API提取代理时必填
                  type: integer
                  title: IP提取方式
                linkCode:
                  description: API提取代理时必填
                  type: string
                  title: 提取链接
                proxyServer:
                  description: 自定义代理时必填
                  type: string
                  title: 代理主机
                proxyPort:
                  description: 自定义代理时必填
                  type: integer
                  title: 代理端口
                proxyAccount:
                  type: string
                  title: 代理帐号
                proxyPassword:
                  type: string
                  title: 代理密码
                referenceCountryCode:
                  description: Oxylabsauto、Lumauto、Smartproxy必须填写国家或者IP
                  type: string
                  title: 环境内帐号需要登录的指定的国家
                referenceIp:
                  description: Oxylabsauto、Lumauto、Smartproxy必须填写国家或者IP
                  type: string
                  title: 根据IP自动填充环境内帐号需要登录的指定的国家
                referenceCity:
                  type: string
                  title: 参考城市
                referenceRegionCode:
                  type: string
                  title: 参考州
                ipDatabaseChannel:
                  description: 用户未指定时使用全局默认值。支持设置查询渠道选项，1-IP2Location   2-DB-IP   3-MaxMind
                  type: integer
                  title: 代理查询渠道
                ipProtocolType:
                  description: >-
                    支持设置IP协议，新环境默认使用速度优先                                                                           
                    1.速度优先  2.IPv4  3.IPv6
                  type: integer
                  title: IP协议选项
                type:
                  description: 参数：windows、android、ios、macos（不传参数默认windows）
                  type: string
                  title: 操作系统
                phoneModel:
                  description: >-
                    type选择Android和IOS时，机型必填。机型参数包括：“google Pixel
                    4、红米8、红米7、google Pixel 5a、三星Galaxy Note8、小米10、三星Galaxy
                    S9+、小米9、iPhone 6 Plus、iPhone 8 Plus、iPhone SE 2、iPhone 7
                    Plus、iPhone X、iPhone13 Pro、iPhone XS、iPhone 13 Pro
                    Max、iPhone 12 mini、iPhone 8、iPhone 13 mini、iPhone 6、iPhone
                    12 Pro Max、iPhone 7、iPhone 12 、iPhone 12 Pro、iPhone 11
                    Pro、iPhone 13、iPhone 14、iPhone 14 Pro、iPhone 14 Pro
                    Max、iPhone 15、iPhone 15 Pro、iPhone 15 Pro Max、google Pixel
                    6、google Pixel 6a、google Pixel 6 Pro、google Pixel 7、google
                    Pixel 7 Pro、google Pixel 7a、google Pixel 8、google Pixel 8
                    Pro、google Pixel 8a、Samsung Galaxy S20、Samsung Galaxy S20
                    +、Samsung Galaxy S21、Samsung Galaxy S21 +、Samsung Galaxy S21
                    Ultra、Samsung Galaxy S22、Samsung Galaxy S22 +、Samsung Galaxy
                    S22 Ultra ””
                  type: string
                  title: 手机机型
                browser:
                  description: firefox/chrome，不填默认创建谷歌环境，
                  type: string
                  title: 浏览器类型
                coreVersion:
                  description: 支持100~126。用selenium时，可以根据这个字段来判断驱动chromedriver的版本号。
                  type: integer
                  title: 内核版本号
                videoThrottle:
                  description: ' 0关闭 1开启 2跟随团队。不传参默认跟随团队。'
                  type: integer
                  title: 视频限流
                imgThrottle:
                  description: ' 0关闭 1自定义 2跟随团队。不传参默认跟随团队。'
                  type: integer
                  title: 图片限流
                imgThrottleSize:
                  type: integer
                  title: 图片尺寸大小
                advancedBo:
                  type: object
                  properties:
                    uaVersion:
                      type: string
                      title: ua版本
                    ua:
                      description: >-
                        要求传参格式符合标准。举例：Mozilla/5.0 (Windows NT 10.0; WOW64)
                        AppleWebKit/537.36 (KHTML, like Gecko)
                        Chrome/101.0.4951.67 Safari/537.36
                      type: string
                      title: 自定义UA
                    languageType:
                      description: 0-跟随IP，1-自定义，2-跟随电脑
                      type: integer
                      title: 语言
                    languages:
                      type: array
                      items:
                        type: string
                      description: 默认使用第一个传入的语言作为渲染语言
                      title: 语言列表
                    gmt:
                      description: timezone时区，不传参默认使用系统默认。自定义时格式举例：GMT-12:00
                      type: string
                      title: 时区
                    geography:
                      description: timezone地理，不传参默认使用系统默认。自定义时格式举例： Etc/GMT + 12
                      type: string
                      title: 地理位置
                    geoTips:
                      description: 0-ask（询问）、2-block（禁止）
                      type: integer
                      title: 网站请求获取您当前地理位置时的选择
                    geoRule:
                      description: 不传参默认使用系统默认。0-基于IP生成对应位置，1-使用自定义设置的位置
                      type: integer
                      title: 地理位置规则
                    longitude:
                      description: 自定义时必填，格式如“-40.123”（范围-180到180）
                      type: string
                      title: 地理位置
                    latitude:
                      description: 地理位置自定义时必填，格式如“30.123”（范围-90到90）
                      type: string
                      title: 纬度
                    radius:
                      description: 地理位置自定义时必填，，格式如“10“（范围10-5000）
                      type: string
                      title: 经度
                    height:
                      description: type为Android或IOS时，不支持设置分辨率。分辨率高、宽都传-1时，分辨率随机
                      type: string
                      title: 分辨率-高
                    width:
                      description: type为Android或IOS时，不支持设置分辨。分辨率高、宽都传-1时，分辨率随机
                      type: string
                      title: 分辨率-宽
                    fontsType:
                      description: 0-隐私，1-真实
                      type: integer
                      title: 字体设置规则
                    fonts:
                      type: array
                      items:
                        type: string
                      description: 按照字体的英文传入（编辑环境时，请将所有的字体传入。若传入的字体过少，可能会导致网页数据显示不全）
                      title: 字体
                    fontFingerprint:
                      description: 0-开启ClientRects隐私保护，1-使用电脑默认的ClientRects
                      type: integer
                      title: 字体指纹
                    webRtc:
                      description: >-
                        0-开启WebRTC，但禁止获取IP，1-开启WebRTC，将公网IP替换为代理IP，2-开启WebRTC，跟随电脑真实IP，3-禁用WebRTC，网站会检测到您关闭了WebRTC，4-转发WebRTC，将公网IP替换为代理IP
                      type: integer
                      title: webrtc设置规则
                    webRtcLocalIp:
                      description: >-
                        内网IP。10.0.0.0/8；10.0.0.0 -
                        10.255.255.255；172.16.0.0/12；172.16.0.0 -
                        172.31.255.255；192.168.0.0/16；192.168.0.0 -
                        192.168.255.255
                      type: string
                    canvas:
                      description: 0-开启Canvas隐私保护，1-跟随电脑的Canvas
                      type: integer
                      title: canvas设置规则
                    webgl:
                      description: 0-开启WebGL隐私保护，1-跟随电脑的WebGL
                      type: integer
                      title: webgl设置规则
                    hardwareAcceleration:
                      description: 0-关闭硬件加速，1-开启硬件加速
                      type: integer
                      title: webgl参数
                    webglInfo:
                      description: >-
                        开启硬件加速时可传参，不传参默认使用系统默认。0-webglvendor和webglRenderer信息将根据ua进行匹配，1-跟随电脑的WebGL
                        Info
                      type: integer
                    audioContext:
                      description: 0-开启AudioContext隐私保护，1-跟随电脑的AudioContext
                      type: integer
                      title: 音频设置规则
                    speechVoices:
                      description: 0-开启SpeechVoices，1-关闭SpeechVoicess
                      type: integer
                      title: 讲述人设置规则
                    media:
                      description: 0-开启媒体设备隐私保护，1-使用Chrome原生隐私保护（不授权则不会暴露真实媒体设备数量）
                      type: integer
                      title: 媒体设置规则
                    cpu:
                      description: 2,4,6,8，10,12,16,0（0代表真实）
                      type: integer
                      title: cpu设置规则
                    memory:
                      description: 2,4,6,8,0（0代表真实）
                      type: integer
                      title: 内存设置规则
                    doNotTrack:
                      description: 0-默认不设置，1-默认不允许追踪，2-默认允许追踪
                      type: integer
                      title: donottrack设置规则
                    battery:
                      description: 0-开启电池隐私保护，1-使用电脑真实的电池信息,2-禁止访问电池信息
                      type: integer
                      title: 电池设置规则
                    portScan:
                      description: 0-不允许网站检测您使用的本地网络端口，1-允许网站检测您使用的本地网络端口
                      type: integer
                      title: 端口扫描保护设置规则
                    whiteList:
                      type: string
                      title: 端口扫描保护白名单
                      description: 端口扫描保护开启后，设置的本地端口可以访问，多个端口逗号隔开
                  x-apifox-orders:
                    - uaVersion
                    - ua
                    - languageType
                    - languages
                    - gmt
                    - geography
                    - geoTips
                    - geoRule
                    - longitude
                    - latitude
                    - radius
                    - height
                    - width
                    - fontsType
                    - fonts
                    - fontFingerprint
                    - webRtc
                    - webRtcLocalIp
                    - canvas
                    - webgl
                    - hardwareAcceleration
                    - webglInfo
                    - audioContext
                    - speechVoices
                    - media
                    - cpu
                    - memory
                    - doNotTrack
                    - battery
                    - portScan
                    - whiteList
                  title: Hubstudio浏览器高级指纹参数配置
              x-apifox-orders:
                - containerName
                - remark
                - tagName
                - cookie
                - asDynamicType
                - proxyTypeName
                - ipGetRuleType
                - linkCode
                - proxyServer
                - proxyPort
                - proxyAccount
                - proxyPassword
                - referenceCountryCode
                - referenceIp
                - referenceCity
                - referenceRegionCode
                - ipDatabaseChannel
                - ipProtocolType
                - type
                - phoneModel
                - browser
                - coreVersion
                - videoThrottle
                - imgThrottle
                - imgThrottleSize
                - advancedBo
              required:
                - containerName
                - asDynamicType
                - proxyTypeName
            example:
              asDynamicType: '0'
              containerName: 新建环境
              proxyTypeName: 不使用代理
              coreVersion: 117
              advancedBo:
                uaVersion: 117
                uiLanguage: en
                languages:
                  - en
                  - en-US
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  requestId:
                    type: string
                    x-apifox-mock: 请求ID
                  msg:
                    type: string
                    x-apifox-mock: 业务消息
                  code:
                    type: integer
                    x-apifox-mock: 业务状态码
                  data:
                    type: object
                    properties:
                      containerCode:
                        type: integer
                        x-apifox-mock: 环境ID
                      coreVersion:
                        type: integer
                        x-apifox-mock: 内核版本
                    x-apifox-orders:
                      - containerCode
                      - coreVersion
                x-apifox-orders:
                  - requestId
                  - msg
                  - code
                  - data
              example:
                requestId: 8b558e5c5d1c437183c34aa03a09a368
                msg: Success
                code: 0
                data:
                  containerCode: 50591754
                  coreVersion: 100
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 环境管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052377-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 更新环境 `POST /api/v1/env/update`

修改环境参数，包括备注信息和分组名称。更新成功返回true。

来源：https://api-docs.hubstudio.cn/380052378e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/env/update:
    post:
      summary: 更新环境
      deprecated: false
      description: 修改环境参数，包括备注信息和分组名称。更新成功返回true。
      tags:
        - 环境管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                containerCode:
                  type: integer
                  title: 环境ID
                containerName:
                  description: 若无需更改环境名称传入原有名称即可
                  type: string
                  title: 环境名
                remark:
                  description: 不传视为留空，会覆盖原备注
                  type: string
                  title: 环境备注信息
                tagName:
                  description: 若分组名称不存在，将默认不修改环境分组
                  type: string
                  title: 环境所属分组信息
                coreVersion:
                  description: 支持100~126。用selenium时，可以根据这个字段来判断驱动chromedriver的版本号。
                  type: integer
                  title: 内核版本号
                videoThrottle:
                  description: ' 0关闭 1开启 2跟随团队。不传参默认跟随团队。'
                  type: integer
                  title: 视频限流
                imgThrottle:
                  description: ' 0关闭 1自定义 2跟随团队。不传参默认跟随团队。'
                  type: integer
                  title: 图片限流
                imgThrottleSize:
                  type: integer
                  title: 图片尺寸大小
                advancedBo:
                  type: object
                  properties:
                    uaVersion:
                      type: string
                      title: ua版本
                    ua:
                      description: >-
                        举例：Mozilla/5.0 (Windows NT 10.0; WOW64)
                        AppleWebKit/537.36 (KHTML, like Gecko)
                        Chrome/101.0.4951.67 Safari/537.36
                      type: string
                      title: 自定义UA要求传参格式符合标准
                    languageType:
                      description: 0-跟随IP，1-自定义，2-跟随电脑
                      type: integer
                      title: 界面语言类型
                    languages:
                      type: array
                      items:
                        type: string
                      title: 默认使用第一个传入的语言作为渲染语言
                    gmt:
                      description: 不传参默认使用系统默认。自定义时格式举例：GMT-12:00
                      type: string
                      title: timezone时区
                    geography:
                      description: 不传参默认使用系统默认。自定义时格式举例： Etc/GMT + 12
                      type: string
                      title: timezone地理
                    geoTips:
                      description: ，0-ask（询问）、2-block（禁止）
                      type: integer
                      title: 网站请求获取您当前地理位置
                    geoRule:
                      description: 不传参默认使用系统默认。0-基于IP生成对应位置，1-使用自定义设置的位置
                      type: integer
                      title: 地理位置规则
                    longitude:
                      description: 地理位置自定义时必填，格式如“-40.123”（范围-180到180）
                      type: string
                    latitude:
                      description: 地理位置自定义时必填，格式如“30.123”（范围-90到90）
                      type: string
                    radius:
                      description: 地理位置自定义时必填，，格式如“10“（范围10-5000）
                      type: string
                    height:
                      description: 分辨率-高，type为Android或IOS时，不支持设置分辨率。分辨率高、宽都传-1时，分辨率随机
                      type: string
                    width:
                      description: 分辨率-宽，type为Android或IOS时，不支持设置分辨。分辨率高、宽都传-1时，分辨率随机
                      type: string
                    fontsType:
                      description: 字体列表保护，0-隐私，1-真实
                      type: integer
                    fonts:
                      type: array
                      items:
                        type: string
                      description: 按照字体的英文传入（编辑环境时，请将所有的字体传入。若传入的字体过少，可能会导致网页数据显示不全）
                    fontFingerprint:
                      description: 字体指纹，0-开启ClientRects隐私保护，1-使用电脑默认的ClientRects
                      type: integer
                    webRtc:
                      description: >-
                        0-开启WebRTC，但禁止获取IP，1-开启WebRTC，将公网IP替换为代理IP，2-开启WebRTC，跟随电脑真实IP，3-禁用WebRTC，网站会检测到您关闭了WebRTC，4-转发WebRTC，将公网IP替换为代理IP
                      type: integer
                    webRtcLocalIp:
                      description: >-
                        内网IP。10.0.0.0/8；10.0.0.0 -
                        10.255.255.255；172.16.0.0/12；172.16.0.0 -
                        172.31.255.255；192.168.0.0/16；192.168.0.0 -
                        192.168.255.255
                      type: string
                    canvas:
                      description: 0-开启Canvas隐私保护，1-跟随电脑的Canvas
                      type: integer
                    webgl:
                      description: 0-开启WebGL隐私保护，1-跟随电脑的WebGL
                      type: integer
                    hardwareAcceleration:
                      description: 0-关闭硬件加速，1-开启硬件加速
                      type: integer
                    webglInfo:
                      description: >-
                        开启硬件加速时可传参，不传参默认使用系统默认。0-webglvendor和webglRenderer信息将根据ua进行匹配，1-跟随电脑的WebGL
                        Info
                      type: integer
                    audioContext:
                      description: 0-开启AudioContext隐私保护，1-跟随电脑的AudioContext
                      type: integer
                    speechVoices:
                      description: 0-开启SpeechVoices，1-关闭SpeechVoicess
                      type: integer
                    media:
                      description: 0-开启媒体设备隐私保护，1-使用Chrome原生隐私保护（不授权则不会暴露真实媒体设备数量）
                      type: integer
                    cpu:
                      description: 2,4,6,8，10,12,16,0（0代表真实）
                      type: integer
                    memory:
                      description: 2,4,6,8,0（0代表真实）
                      type: integer
                    doNotTrack:
                      description: 0-默认不设置，1-默认不允许追踪，2-默认允许追踪
                      type: integer
                    battery:
                      description: 0-开启电池隐私保护，1-使用电脑真实的电池信息,2-禁止访问电池信息
                      type: integer
                    portScan:
                      description: 0-不允许网站检测您使用的本地网络端口，1-允许网站检测您使用的本地网络端口
                      type: integer
                    whiteList:
                      type: string
                  x-apifox-orders:
                    - uaVersion
                    - ua
                    - languageType
                    - languages
                    - gmt
                    - geography
                    - geoTips
                    - geoRule
                    - longitude
                    - latitude
                    - radius
                    - height
                    - width
                    - fontsType
                    - fonts
                    - fontFingerprint
                    - webRtc
                    - webRtcLocalIp
                    - canvas
                    - webgl
                    - hardwareAcceleration
                    - webglInfo
                    - audioContext
                    - speechVoices
                    - media
                    - cpu
                    - memory
                    - doNotTrack
                    - battery
                    - portScan
                    - whiteList
                  title: Hubstudio浏览器高级指纹参数配置
                type:
                  type: string
                  title: 操作系统类型
                  description: 操作系统类型传值:windows/android/ios/macos 四个中的一个
                  x-apifox-mock: windows
              x-apifox-orders:
                - containerCode
                - containerName
                - remark
                - type
                - tagName
                - coreVersion
                - videoThrottle
                - imgThrottle
                - imgThrottleSize
                - advancedBo
              required:
                - containerCode
                - containerName
                - tagName
                - coreVersion
                - type
            example:
              containerCode: 189603333
              containerName: 修改环境名
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  requestId:
                    type: string
                    title: 请求ID
                  msg:
                    type: string
                    title: 响应消息
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: boolean
                    title: 业务数据载体
                x-apifox-orders:
                  - requestId
                  - msg
                  - code
                  - data
              example:
                requestId: 68cb72ff72e9441da4fb84760937ead3
                msg: Success
                code: 0
                data: true
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 环境管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052378-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 更新环境代理 `POST /api/v1/env/proxy/update`

修改指定环境的代理信息，包括代理主机、端口、帐号、密码等。更新成功返回true

来源：https://api-docs.hubstudio.cn/380052379e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/env/proxy/update:
    post:
      summary: 更新环境代理
      deprecated: false
      description: 修改指定环境的代理信息，包括代理主机、端口、帐号、密码等。更新成功返回true
      tags:
        - 环境管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                containerCode:
                  type: integer
                  title: 环境ID
                asDynamicType:
                  description: 0-关闭提醒(默认)1-开启提醒
                  type: integer
                  title: IP变更提醒
                proxyTypeName:
                  description: >-
                    HTTP、HTTPS、SSH、Socks5、Oxylabsauto、Lumauto_HTTP
                    、Lumauto_HTTPS 、Luminati_HTTP、Luminati_HTTPS、
                    smartproxy、Iphtml_HTTP、Iphtml_Socks5、IPIDEA、不使用代理             
                    2、API提取代理类型：  Socks5_ROLA_IP、HTTP_ROLA_IP、HTTPS_ROLA_IP、
                    Socks5_922S5、HTTP_922S5、HTTPS_922S5、
                    Socks5_通用api、HTTP_通用api、HTTPS_通用api、Socks5_IPIDEA-API、HTTP_IPIDEA-API、HTTPS_IPIDEA-API
                  type: string
                  title: 自定义代理类型
                ipGetRuleType:
                  description: 1-IP失效时提取新IP ，2-，每次打开环境时提取新IP。API提取代理时必填
                  type: integer
                  title: IP提取方式
                linkCode:
                  description: API提取代理时必填
                  type: string
                  title: 提取链接
                proxyHost:
                  type: string
                  title: 代理主机
                proxyPort:
                  type: integer
                  title: 代理端口
                proxyAccount:
                  type: string
                  title: 代理帐号
                proxyPassword:
                  type: string
                  title: 代理密码
                referenceCountryCode:
                  description: Oxylabsauto、Lumauto、Smartproxy必须填写国家或者IP
                  type: string
                  title: 环境内帐号需要登录的指定的国家
                referenceCity:
                  type: string
                  title: 参考城市
                referenceRegionCode:
                  type: string
                  title: 参考州
                ipDatabaseChannel:
                  description: 支持设置查询渠道选项，1-IP2Location  2-DB-IP   3-MaxMind
                  type: integer
                  title: 代理查询渠道
                ipProtocolType:
                  description: 支持设置IP协议 1.速度优先 2.IPv4   3.IPv6
                  type: integer
                  title: IP协议选项
              x-apifox-orders:
                - containerCode
                - asDynamicType
                - proxyTypeName
                - ipGetRuleType
                - linkCode
                - proxyHost
                - proxyPort
                - proxyAccount
                - proxyPassword
                - referenceCountryCode
                - referenceCity
                - referenceRegionCode
                - ipDatabaseChannel
                - ipProtocolType
              required:
                - containerCode
                - asDynamicType
                - proxyTypeName
            examples: {}
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  requestId:
                    type: string
                    title: 请求ID
                  msg:
                    type: string
                    title: 响应消息
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: boolean
                    title: 业务数据载体
                x-apifox-orders:
                  - requestId
                  - msg
                  - code
                  - data
              example:
                requestId: 0a5ad560424947d8a831d0980cb59e7d
                msg: Success
                code: 0
                data: true
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 环境管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052379-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 导入Cookie `POST /api/v1/env/import-cookie`

向指定环境导入cookie，导入成功返回true。

来源：https://api-docs.hubstudio.cn/380052380e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/env/import-cookie:
    post:
      summary: 导入Cookie
      deprecated: false
      description: 向指定环境导入cookie，导入成功返回true。
      tags:
        - 环境管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                containerCode:
                  type: string
                  title: 环境ID
                cookie:
                  type: string
                  title: 设置cookie
                  description: 值为空字符串是清空cookie
              required:
                - containerCode
                - cookie
              x-apifox-orders:
                - containerCode
                - cookie
            example:
              containerCode: '132823764'
              cookie: ''
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  requestId:
                    type: string
                    title: 请求ID
                  msg:
                    type: string
                    title: 响应消息
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: boolean
                    title: 业务数据载体
                x-apifox-orders:
                  - requestId
                  - msg
                  - code
                  - data
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 环境管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052380-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 导出Cookie `POST /api/v1/env/export-cookie`

导出指定环境的cookie，导出成功返回cookie的json串

来源：https://api-docs.hubstudio.cn/380052381e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/env/export-cookie:
    post:
      summary: 导出Cookie
      deprecated: false
      description: 导出指定环境的cookie，导出成功返回cookie的json串
      tags:
        - 环境管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                containerCode:
                  type: integer
                  title: 环境ID
              required:
                - containerCode
              x-apifox-orders:
                - containerCode
            example:
              containerCode: '118969108'
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  requestId:
                    type: string
                    title: 请求ID
                  msg:
                    type: string
                    title: 响应消息
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: string
                    title: 业务数据载体
                x-apifox-orders:
                  - requestId
                  - msg
                  - code
                  - data
              example:
                requestId: 0b1ef5b88e5942eb933c97982ba8492a
                msg: Success
                code: 0
                data: >-
                  [{"Name":"CONSENT","Value":"PENDING+571","Domain":".google.com","Path":"/","Secure":true,"HttpOnly":false,"Persistent":"1","Creation":"2022-09-14T15:18:07.389+08:00","LastAccess":"2024-09-13T15:18:07.389+08:00","Expires":"2024-09-13T15:18:07.389+08:00","Priority":"1","HasExpires":"1","Samesite":"-1","SourceScheme":"2","Firstpartyonly":"","schemeMap":false,"isSelf":false}]
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 环境管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052381-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 删除环境 `POST /api/v1/env/del`

删除指定环境。删除成功返回true。一次性最多支持删除环境1000个。

来源：https://api-docs.hubstudio.cn/380052382e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/env/del:
    post:
      summary: 删除环境
      deprecated: false
      description: 删除指定环境。删除成功返回true。一次性最多支持删除环境1000个。
      tags:
        - 环境管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                containerCodes:
                  type: array
                  items:
                    type: integer
                  title: 环境ID列表
              required:
                - containerCodes
              x-apifox-orders:
                - containerCodes
            example:
              containerCodes:
                - 132725138
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  requestId:
                    type: string
                    title: 请求ID
                  msg:
                    type: string
                    title: 响应消息
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: boolean
                    title: 业务数据载体
                x-apifox-orders:
                  - requestId
                  - msg
                  - code
                  - data
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 环境管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052382-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 获取随机UA `POST /api/v1/env/random-ua`

获取随机UA，获取成功返回UA

来源：https://api-docs.hubstudio.cn/380052383e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/env/random-ua:
    post:
      summary: 获取随机UA
      deprecated: false
      description: 获取随机UA，获取成功返回UA
      tags:
        - 环境管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                type:
                  description: windows、android、ios（不传参数默认windows）
                  type: string
                  title: 操作系统参数
                phoneModel:
                  description: >-
                    type选择android和ios时，机型必填。机型参数包括：“google Pixel
                    4、红米8、红米7、google Pixel 5a、三星Galaxy Note8、小米10、三星Galaxy
                    S9+、小米9、iPhone 6 Plus、iPhone 8 Plus、iPhone SE 2、iPhone 7
                    Plus、iPhone X、iPhone 13 Pro、iPhone XS、iPhone 13 Pro
                    Max、iPhone 12 mini、iPhone 8、iPhone 13 mini、iPhone 6、iPhone
                    12 Pro Max、iPhone 7、iPhone 12 、iPhone 12 Pro、iPhone 11
                    Pro、iPhone 13、iPhone 14、iPhone 14 Pro、iPhone 14 Pro
                    Max、iPhone 15、iPhone 15 Pro、iPhone 15 Pro Max、google Pixel
                    6、google Pixel 6a、google Pixel 6 Pro、google Pixel 7、google
                    Pixel 7 Pro、google Pixel 7a、google Pixel 8、google Pixel 8
                    Pro、google Pixel 8a、Samsung Galaxy S20、Samsung Galaxy S20
                    +、Samsung Galaxy S21、Samsung Galaxy S21 +、Samsung Galaxy S21
                    Ultra、Samsung Galaxy S22、Samsung Galaxy S22 +、Samsung Galaxy
                    S22 Ultra ”
                  type: string
                  title: 手机机型
                version:
                  type: array
                  items:
                    type: integer
                  description: 支持数组，不传参默认随机。
                  title: 内核版本
              x-apifox-orders:
                - type
                - phoneModel
                - version
            example:
              version:
                - 112
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  requestId:
                    type: string
                    title: 请求ID
                  msg:
                    type: string
                    title: 响应消息
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: string
                    title: 业务数据载体
                x-apifox-orders:
                  - requestId
                  - msg
                  - code
                  - data
              example:
                requestId: 59e685a3775944e188545a10fb07e0e1
                msg: Success
                code: 0
                data: >-
                  Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36
                  (KHTML, like Gecko) Chrome/112.0.5615.87 Safari/537.36
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 环境管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052383-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 清除环境本地缓存 `POST /api/v1/cache/clear`

清除环境本地缓存

来源：https://api-docs.hubstudio.cn/380052384e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cache/clear:
    post:
      summary: 清除环境本地缓存
      deprecated: false
      description: 清除环境本地缓存
      tags:
        - 环境管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                browserOauths:
                  type: array
                  items:
                    type: string
                  description: 参数不传则删除所有环境的本地缓存
                  title: 打开环境时返回的browserID
                'localStorage ':
                  description: 默认为否
                  type: boolean
                  title: 是否清除 LocalStorage
                indexedDB:
                  description: 默认为否
                  type: boolean
                  title: 是否清除 IndexedDB
                cookie:
                  description: 默认为否
                  type: boolean
                  title: 是否清除 cookie
                extension:
                  description: 默认为否，不清除
                  type: boolean
                  title: 是否清除扩展数据
                extensionFile:
                  description: 默认为否，不清除
                  type: boolean
                  title: 是否清除扩展
              x-apifox-orders:
                - browserOauths
                - 'localStorage '
                - indexedDB
                - cookie
                - extension
                - extensionFile
            example: "{\r\n    \"browserOauths\": [\r\n        \"373247\"\r\n    ], // 打开环境时返回的browserID，参数不传则删除所有环境的本地缓存\r\n    \"localStorage \": true, // 是否清除 LocalStorage，默认为否\r\n    \"indexedDB\": true,  // 是否清除 IndexedDB，默认为否\r\n    \"cookie\": true,  // 是否清除 cookie，默认为否 \r\n    \"extension\": true,  // 是否清除扩展数据，默认为否，不清除\r\n    \"extensionFile\": true  // 是否清除扩展，默认为否，不清除\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  requestId:
                    type: string
                    title: 请求ID
                  msg:
                    type: string
                    title: 响应消息
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: object
                    properties:
                      action:
                        type: string
                        title: 执行的动作/命令名称
                      err:
                        type: string
                        title: 执行结果
                      requestId:
                        type: string
                        title: 请求ID
                      statusCode:
                        type: string
                        title: 操作状态码
                      failIds:
                        type: array
                        items:
                          type: integer
                        title: 失败的环境ID
                      info:
                        type: string
                        title: 消息
                      successIds:
                        type: array
                        items:
                          type: integer
                        title: 成功的环境ID
                    x-apifox-orders:
                      - action
                      - err
                      - failIds
                      - info
                      - requestId
                      - statusCode
                      - successIds
                    title: 业务数据载体
                x-apifox-orders:
                  - requestId
                  - msg
                  - code
                  - data
              example:
                requestId: d2feae4d-bc25-43d9-bfdd-8e1e9a56f618
                msg: Success
                code: 0
                data:
                  action: ClearCache
                  err: 成功
                  failIds:
                    - 11509459
                  info: 清理缓存成功，11509427已清理，11509459已打开 无法清理
                  requestId: d2feae4d-bc25-43d9-bfdd-8e1e9a56f618
                  statusCode: '0'
                  successIds:
                    - 11509427
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 环境管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052384-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 清理环境内插件缓存 `POST /api/v1/browser/reset-extension`

清理环境内插件缓存，清理成功后插件的所有数据均会被删除

来源：https://api-docs.hubstudio.cn/380052385e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/browser/reset-extension:
    post:
      summary: 清理环境内插件缓存
      deprecated: false
      description: 清理环境内插件缓存，清理成功后插件的所有数据均会被删除
      tags:
        - 环境管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                browserOauth:
                  type: integer
                  title: 打开环境时返回的browserID
                pluginIds:
                  type: array
                  items:
                    type: string
                  description: 可通过chrome://extensions/查看环境内所有插件的ID
                  title: 指定要清除的插件的ID
              required:
                - browserOauth
                - pluginIds
              x-apifox-orders:
                - browserOauth
                - pluginIds
            example:
              browserOauth: 373247
              pluginIds:
                - nkbihfbeogaeaoehlefnkodbefgpgknn
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  msg:
                    type: string
                    title: 响应消息
                  data:
                    type: object
                    properties:
                      info:
                        type: string
                        title: 信息
                      statusCode:
                        type: string
                        title: 操作状态码
                      err:
                        type: string
                        title: 执行结果
                      action:
                        type: string
                        title: 执行的动作/命令名称
                    x-apifox-orders:
                      - info
                      - statusCode
                      - err
                      - action
                    title: 业务数据载体
                x-apifox-orders:
                  - code
                  - msg
                  - data
              example:
                code: 0
                msg: Success
                data:
                  info: 插件ID:jhegmncopobmnnmcdaobcepcamekoomb清理成功
                  statusCode: '0'
                  err: 成功(Success)
                  action: resetExtension
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 环境管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052385-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 刷新指纹 `POST /api/v1/env/refresh-fingerprint`

- 刷新指纹

来源：https://api-docs.hubstudio.cn/380052386e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/env/refresh-fingerprint:
    post:
      summary: 刷新指纹
      deprecated: false
      description: |-
        - 刷新指纹
        - 仅支持v3.37以上版本，请前往官网下载客户端最新版本【下载Hubstudio最新版】
      tags:
        - 环境管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                containerCode:
                  type: integer
                  title: 环境ID
                uaVersion:
                  description: 不传 uaVersion ，默认随机最新UA
                  type: integer
                  title: UA版本
                coreVersion:
                  description: 不传，不会改变
                  type: integer
                  title: 客户端内核版本
                type:
                  description: 不传默认为windows
                  type: string
                  title: 操作系统类型
              required:
                - containerCode
              x-apifox-orders:
                - containerCode
                - uaVersion
                - coreVersion
                - type
            example: "{\r\n    \"containerCode\": 11732471,  // 环境ID\r\n    \"uaVersion\": 131 ,  // UA版本。不传 uaVersion ，默认随机最新UA\r\n    \"coreVersion\": 131,   // 客户端内核版本，不传，不会改变\r\n    \"type\": \"windows\"  // 操作系统类型，不传默认为windows\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  msg:
                    type: string
                    title: 响应消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  data:
                    type: 'null'
                    title: 业务数据载体
                x-apifox-orders:
                  - code
                  - msg
                  - requestId
                  - timestamp
                  - data
              example:
                code: 0
                msg: Success
                requestId: a5a2465555edad295e5342265c12b779
                timestamp: 1740726236985
                data: null
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 环境管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052386-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 查询webglVendor和webglRenderer `POST /api/v1/container/webgl-renderer-list`

查询WebglInfo信息

来源：https://api-docs.hubstudio.cn/380052387e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/container/webgl-renderer-list:
    post:
      summary: 查询webglVendor和webglRenderer
      deprecated: false
      description: 查询WebglInfo信息
      tags:
        - 环境管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                browser:
                  description: chrome、firefox。默认chrome
                  type: string
                  title: 浏览器
                hardwareAcceleration:
                  description: ，0-关闭硬件加速。默认:1
                  type: integer
                  title: 开启硬件加速
                type:
                  description: windows，android，ios， macos。 默认windows
                  type: string
                  title: 操作系统类型
                uaVersion:
                  description: 例如：117
                  type: string
                  title: ua版本
              required:
                - uaVersion
              x-apifox-orders:
                - browser
                - hardwareAcceleration
                - type
                - uaVersion
            example: "{\r\n  \"browser\": \"chrome\", // 浏览器：chrome、firefox。默认chrome\r\n  \"hardwareAcceleration\": 0,  // 1-开启硬件加速，0-关闭硬件加速。默认:1\r\n  \"type\": \"windows\",  // 操作系统类型：windows，android，ios， macos。 默认windows\r\n  \"uaVersion\": \"\" // ua版本，例如：117\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: array
                    items:
                      type: object
                      properties:
                        renderer:
                          type: string
                          title: WebGL厂商
                        vendor:
                          type: string
                          title: WebGL渲染
                      x-apifox-orders:
                        - renderer
                        - vendor
                    title: 业务数据载体
                  message:
                    type: string
                    title: 消息
                  requestId:
                    type: string
                    title: 请求ID
                  success:
                    type: boolean
                  timestamp:
                    type: integer
                    title: 时间戳
                x-apifox-orders:
                  - code
                  - data
                  - message
                  - requestId
                  - success
                  - timestamp
              example:
                code: 0
                msg: Success
                requestId: e287a6c1-83d0-4ecd-8de9-4305f09a364d
                timestamp: 1773405442755
                data:
                  - vendor: Google Inc. (Google)
                    renderer: >-
                      ANGLE (Google, Vulkan 1.2.0 (SwiftShader Device (Subzero)
                      (0x0000C0DE)), SwiftShader driver-5.0.0)
                  - vendor: Google Inc. (Google)
                    renderer: >-
                      ANGLE (Google, Vulkan 1.2.0 (SwiftShader Device (Subzero)
                      (0x0000C0DE)), SwiftShader driver)
                  - vendor: Google Inc. (Google)
                    renderer: >-
                      ANGLE (Google, Vulkan 1.3.0 (SwiftShader Device (Subzero)
                      (0x0000C0DE)), SwiftShader driver)
                  - vendor: Googlee Inc. (Intel)
                    renderer: >-
                      ANGLE (Intel, Intel(R) HD Graphics Direct3D11 vs_5_0
                      ps_5_0)
                  - vendor: Googlee Inc. (Intel)
                    renderer: >-
                      ANGLE (Intel, Intel(R) HD Graphics 400 Direct3D11 vs_5_0
                      ps_5_0)
                  - vendor: Google Inc. (Microsoft)
                    renderer: >-
                      ANGLE (Microsoft, Microsoft Basic Render Driver Direct3D11
                      vs_5_0 ps_5_0)
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 环境管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052387-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 批量修改备注 `POST /api/v1/container/batch-update-remark`

批量修改多个环境的备注

来源：https://api-docs.hubstudio.cn/380052388e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/container/batch-update-remark:
    post:
      summary: 批量修改备注
      deprecated: false
      description: 批量修改多个环境的备注
      tags:
        - 环境管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                containerCodes:
                  type: array
                  items:
                    type: string
                  title: 环境ID
                remark:
                  type: string
                  title: 备注
                type:
                  description: ' 1覆盖 2追加'
                  type: integer
                  title: 修改类型
              x-apifox-orders:
                - containerCodes
                - remark
                - type
              required:
                - type
                - containerCodes
            example:
              containerCodes:
                - 11732471
              remark: 备注
              type: 1
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: boolean
                    title: 业务数据载体
                  message:
                    type: string
                    title: 消息
                  requestId:
                    type: string
                    title: 请求ID
                  success:
                    type: boolean
                  timestamp:
                    type: integer
                    title: 时间戳
                x-apifox-orders:
                  - code
                  - data
                  - message
                  - requestId
                  - success
                  - timestamp
              example:
                code: 0
                data: true
                message: ''
                requestId: ''
                success: true
                timestamp: 0
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 环境管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052388-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 下载内核 `POST /api/v1/browser/download-core`

下载环境内核

来源：https://api-docs.hubstudio.cn/380052389e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/browser/download-core:
    post:
      summary: 下载内核
      deprecated: false
      description: 下载环境内核
      tags:
        - 环境管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                Cores:
                  type: array
                  items:
                    type: object
                    properties:
                      BrowserType:
                        type: integer
                        description: 1-Chrome，2-Firefox
                        title: 浏览器内核类型
                      Version:
                        type: string
                        description: 仅支持hub客户端支持的版本下载。
                        title: 内核版本
                    required:
                      - BrowserType
                      - Version
                    x-apifox-orders:
                      - BrowserType
                      - Version
                  title: 内核列表
              required:
                - Cores
              x-apifox-orders:
                - Cores
            example:
              Cores:
                - BrowserType: 3
                  Version: '144'
                - BrowserType: 2
                  Version: '110'
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  requestId:
                    type: string
                    title: 请求ID
                  msg:
                    type: string
                    title: 响应消息
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: object
                    properties:
                      action:
                        type: string
                        title: 执行的动作/命令名称
                      err:
                        type: string
                        title: 执行结果
                      info:
                        type: string
                        title: 消息
                      requestId:
                        type: string
                        title: 请求ID
                      statusCode:
                        type: string
                        title: 操作状态码
                    x-apifox-orders:
                      - action
                      - err
                      - info
                      - requestId
                      - statusCode
                    title: 业务数据载体
                x-apifox-orders:
                  - requestId
                  - msg
                  - code
                  - data
              example:
                requestId: c37602ed-d79d-4b39-b31d-00b82e19ae5c
                msg: Success
                code: 0
                data:
                  action: DownloadBrowserCore
                  err: 成功(Success)
                  info: 内核下载成功
                  requestId: c37602ed-d79d-4b39-b31d-00b82e19ae5c
                  statusCode: '0'
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 环境管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052389-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 更新环境基础信息 `POST /api/v1/container/update-container-base`

来源：https://api-docs.hubstudio.cn/439846791e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/container/update-container-base:
    post:
      summary: 更新环境基础信息
      deprecated: false
      description: ''
      tags:
        - 环境管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                containerCode:
                  type: integer
                  title: 环境ID
                containerName:
                  type: string
                  title: 环境名称
                  description: 支持空字符串：不进行修改
                remark:
                  type: string
                  title: 备注
                  description: 支持空字符串：清空备注数据，不传则不进行修改
                tagName:
                  type: string
                  title: 分组名称
                  description: 限制中英文、数字、常用符号，不支持空格、空字符串，不修改可以不传
              required:
                - containerCode
              x-apifox-orders:
                - containerCode
                - containerName
                - remark
                - tagName
            example:
              containerCode: 11732471
              containerName: 环境名称
              remark: 备注
              tagName: 分组名称
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties: {}
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 环境管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-439846791-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```


### 浏览器环境

#### 打开环境 `POST /api/v1/browser/start`

- 用于启动指定的环境，启动成功后可以获取浏览器debug端口用于执行selenium和puppeteer自动化脚本

来源：https://api-docs.hubstudio.cn/380052361e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/browser/start:
    post:
      summary: 打开环境
      deprecated: false
      description: >-
        - 用于启动指定的环境，启动成功后可以获取浏览器debug端口用于执行selenium和puppeteer自动化脚本

        - 目前Hubstudio采用100版Chrome内核

        -
        Selenium需要使用到匹配的Webdriver，需更新到应用版本2.4.2及以上版本。返回的debuggingPort参数可用于自动化工具连接。
      tags:
        - 浏览器环境
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                containerCode:
                  type: string
                  title: 环境ID
                cdpHide:
                  description: 默认false（true代表屏蔽）仅支持ChroBrowser133及以上内核版本
                  type: boolean
                  title: 是否屏蔽cdp检测
                shouldCloseTabsOnOpen:
                  type: boolean
                  title: 是否打开历史标签页
                  description: >-
                    需要在客户端 "偏好设置-个人设置-启动环境时" 选项中，选择 "打开上次"，此参数才会生效。当
                    shouldCloseTabsOnOpen 为 true
                    时，会同步服务端的标签页数据到客户端，即打开上次关闭时的标签页。当 shouldCloseTabsOnOpen 为
                    false 时，不会同步服务端的标签页数据，客户端的现有标签页数据不会被覆盖。
                pageZoom:
                  type: integer
                  title: 缩放比例
                  description: >-
                    只能传原生支持的比例，原生支持 50%，75%，
                    100%，125%，150%，175%，200%，其它参数不支持，150% 传
                    1.5。此参数仅支持3.46.0及以上版本
                containerTabs:
                  type: array
                  items:
                    type: string
                  title: 启动url
                  description: >-
                    举例：  "containerTabs": ["https://www.hubstudio.cn/",
                    "https://www.baidu.com/"]  
                isHeadless:
                  description: '默认false，设置无头后如无法连接，请使用用"args"参数进行设置: ["--headless=new"]'
                  type: boolean
                  title: 浏览器无头模式
                isWebDriverReadOnlyMode:
                  description: 默认false。（true代表只读模式，不会保存cookie等数据
                  type: boolean
                  title: 是否只读模式
                skipSystemResourceCheck:
                  description: 默认false不跳过系统可用资源检测(仅支持v3.6.0及以上版本)
                  type: boolean
                  title: 跳过系统可用资源检测
                args:
                  type: array
                  items:
                    type: string
                  title: 启动参数
                  description: >-
                    举例  "args": [         "--kiosk",        
                    "--blink-settings=imagesEnabled=false"     ] 
                serialNumber:
                  type: string
                  title: 序号
                  description: >-
                    containerCode和serialNumber都传，以containerCode为准，支持客户端版本3.55.0版本
              required:
                - containerCode
              x-apifox-orders:
                - containerCode
                - cdpHide
                - shouldCloseTabsOnOpen
                - pageZoom
                - containerTabs
                - isHeadless
                - isWebDriverReadOnlyMode
                - skipSystemResourceCheck
                - args
                - serialNumber
            example: "{\r\n    \"containerCode\": \"855195083\",  // 环境ID\r\n    \"isHeadless\": false,  // 浏览器无头模式。默认false，设置无头后如无法连接，请使用用\"args\"参数进行设置: [\"--headless=new\"]\r\n    \"shouldCloseTabsOnOpen\": \"true\"\r\n\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  msg:
                    type: string
                    title: 业务消息
                  code:
                    type: integer
                    title: 业务状态码
                    description: 0表示成功，其余为失败
                  data:
                    type: object
                    properties:
                      accountId:
                        type: 'null'
                      action:
                        type: string
                        title: 动作
                      backgroundPluginId:
                        type: string
                        title: 插件ID
                      browserID:
                        type: integer
                        title: 浏览器id
                        description: 用于清理缓存接口
                      browserPath:
                        type: string
                        title: 内核存放本地位置
                      debuggingPort:
                        type: string
                        title: 浏览器调试端口
                        description: 用于自动化工具连接
                      downloadPath:
                        type: string
                        title: 下载内容存在地址
                      duplicate:
                        type: integer
                      ip:
                        type: string
                        title: IP地址
                      isDynamicIp:
                        type: boolean
                      launcherPage:
                        type: string
                        title: 启动页
                        description: API启动打开的页面
                      proxyTag:
                        type: string
                      proxyType:
                        type: string
                        title: 代理类型
                      reportPluginId:
                        type: string
                      runMode:
                        type: integer
                        title: 运行模式
                      webdriver:
                        description: 根据当前打开环境的内核返回对应内核webdriver驱动路径
                        type: string
                        title: webdriver驱动存放位置
                      statusCode:
                        type: string
                        title: 状态
                        description: 0为启动成功
                      containerCode:
                        type: string
                        title: 环境ID
                      containerId:
                        type: integer
                      err:
                        type: string
                        title: 动作结果信息
                      requestId:
                        type: string
                        title: 请求ID
                    x-apifox-orders:
                      - action
                      - backgroundPluginId
                      - browserID
                      - browserPath
                      - containerCode
                      - containerId
                      - debuggingPort
                      - downloadPath
                      - duplicate
                      - err
                      - ip
                      - isDynamicIp
                      - launcherPage
                      - proxyType
                      - requestId
                      - runMode
                      - statusCode
                      - webdriver
                      - accountId
                      - proxyTag
                      - reportPluginId
                    title: 业务数据
                  requestId:
                    type: string
                    title: 请求id
                x-apifox-orders:
                  - requestId
                  - msg
                  - code
                  - data
              example:
                requestId: 48e54351-2bcc-42e0-945c-1866aa9a17f0
                msg: Success
                code: 0
                data:
                  action: startBrowserByCode
                  backgroundPluginId: hbefgchohflbmmnecchdioangfpcgfjn
                  browserID: 332034
                  browserPath: >-
                    C:\Users\Administrator\AppData\Local\env-kit\Core\chrome_64_130_202411271729\hubstudio
                  containerCode: '11320340'
                  containerId: 332034
                  debuggingPort: '59591'
                  downloadPath: C:\Users\Administrator\Desktop\Hubstudio\万前般清
                  duplicate: 4739
                  err: 成功(Success)
                  ip: 103.136.249.48
                  isDynamicIp: false
                  launcherPage: about:blank
                  proxyType: socks5
                  requestId: 48e54351-2bcc-42e0-945c-1866aa9a17f0
                  runMode: 2
                  statusCode: '0'
                  webdriver: >-
                    C:\Users\Administrator\AppData\Local\env-kit\Core\chrome_64_130_202411271729\webdriver.exe
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 浏览器环境
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052361-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 关闭环境 `POST /api/v1/browser/stop`

关闭指定环境

来源：https://api-docs.hubstudio.cn/380052362e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/browser/stop:
    post:
      summary: 关闭环境
      deprecated: false
      description: 关闭指定环境
      tags:
        - 浏览器环境
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                containerCode:
                  type: string
                  title: 环境ID
                serialNumber:
                  type: string
                  description: >-
                    containerCode和serialNumber都传，以containerCode为准，支持客户端版本3.55.0版本
                  title: 序号
              required:
                - containerCode
              x-apifox-orders:
                - containerCode
                - serialNumber
            example: "{\r\n    \"containerCode\":\"1617094733\"  //  环境ID\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  requestId:
                    type: string
                    title: 请求ID
                  msg:
                    type: string
                    title: 业务消息
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: object
                    properties:
                      action:
                        type: string
                        title: 动作
                      err:
                        type: string
                        title: 动作结果信息
                      requestId:
                        type: string
                        title: 请求ID
                      statusCode:
                        type: string
                        description: 0：关闭成功
                        title: 状态码
                    x-apifox-orders:
                      - action
                      - err
                      - requestId
                      - statusCode
                    title: 业务数据
                  containerCode:
                    type: string
                    title: 环境ID
                x-apifox-orders:
                  - containerCode
                  - requestId
                  - msg
                  - code
                  - data
              example:
                requestId: a815fffe-93d7-4286-baf9-d93298f1b450
                msg: Success
                code: 0
                data:
                  action: stopBrowserByCode
                  err: 成功(Success)
                  requestId: a815fffe-93d7-4286-baf9-d93298f1b450
                  statusCode: '0'
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 浏览器环境
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052362-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 关闭所有环境 `POST /api/v1/browser/stop-all`

支持关闭所有环境，接口参数传 true 会清空启动环境队列。

来源：https://api-docs.hubstudio.cn/380052366e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/browser/stop-all:
    post:
      summary: 关闭所有环境
      deprecated: false
      description: |-
        支持关闭所有环境，接口参数传 true 会清空启动环境队列。
        ```text 
        备注：`因Firebrowser原生内核原因，该类环境不支持通过API打开
        ```
      tags:
        - 浏览器环境
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                clearOpening:
                  type: boolean
                  description: 传 true 会清空启动环境队列
              x-apifox-orders:
                - clearOpening
            example: "{\r\n\"clearOpening\": true  // 传 true 会清空启动环境队列\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  requestId:
                    type: string
                    title: 请求ID
                  msg:
                    type: string
                    title: 业务消息
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: object
                    properties:
                      action:
                        type: string
                        title: 动作
                      err:
                        type: string
                        title: 动作结果信息
                      requestId:
                        type: string
                        title: 请求ID
                      statusCode:
                        type: string
                        title: 状态码
                    x-apifox-orders:
                      - action
                      - err
                      - statusCode
                      - requestId
                    title: 业务数据
                x-apifox-orders:
                  - msg
                  - code
                  - data
                  - requestId
              example:
                requestId: 904fe8f3-c668-4a3f-b75e-8ee2ea5918d2
                msg: Success
                code: 0
                data:
                  action: CloseAllBrowser
                  err: 成功(Success)
                  requestId: 904fe8f3-c668-4a3f-b75e-8ee2ea5918d2
                  statusCode: '0'
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 浏览器环境
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052366-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 获取浏览器状态 `POST /api/v1/browser/all-browser-status`

获取浏览器状态的接口，返回浏览器环境code以及状态

来源：https://api-docs.hubstudio.cn/380052363e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/browser/all-browser-status:
    post:
      summary: 获取浏览器状态
      deprecated: false
      description: 获取浏览器状态的接口，返回浏览器环境code以及状态
      tags:
        - 浏览器环境
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                containerCodes:
                  type: array
                  items:
                    type: string
                  description: 环境id列表
              required:
                - containerCodes
              x-apifox-orders:
                - containerCodes
            example: "{\r\n    \"containerCodes\": [\"123094597\"]  // 环境ID\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  requestId:
                    type: string
                    title: 请求ID
                  msg:
                    type: string
                    title: 业务消息
                  code:
                    type: integer
                    title: 业务状态码
                    description: 0表示成功，其余为失败
                  data:
                    type: object
                    properties:
                      action:
                        type: string
                        title: 动作
                      containers:
                        type: array
                        items:
                          type: object
                          properties:
                            containerCode:
                              type: integer
                              title: 环境ID
                            status:
                              type: integer
                              description: '1-开启中: 0-已开启: 2-关闭中: 3-已关闭'
                              title: 浏览器状态码
                            pid:
                              type: string
                              title: 进程ID
                          x-apifox-orders:
                            - containerCode
                            - status
                            - pid
                          required:
                            - pid
                        title: 环境数据
                      err:
                        type: string
                        title: 动作信息
                      requestId:
                        type: string
                        title: 请求ID
                      statusCode:
                        type: string
                        title: 状态
                        description: 0为启动成功
                    x-apifox-orders:
                      - statusCode
                      - containers
                      - err
                      - action
                      - requestId
                    title: 业务数据
                x-apifox-orders:
                  - code
                  - msg
                  - data
                  - requestId
              example:
                requestId: 61184f72-b1dc-4800-8011-dd1b494684ec
                msg: Success
                code: 0
                data:
                  action: GetAllBrowserStatus
                  containers:
                    - containerCode: 11320692
                      status: 3
                    - containerCode: 11320646
                      status: 0
                    - containerCode: 11320630
                      status: 3
                  err: 成功(Success)
                  requestId: 61184f72-b1dc-4800-8011-dd1b494684ec
                  statusCode: '0'
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 浏览器环境
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052363-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 切换浏览器窗口 `POST /api/v1/browser/foreground`

切换浏览器窗口，将窗口置顶显示

来源：https://api-docs.hubstudio.cn/380052364e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/browser/foreground:
    post:
      summary: 切换浏览器窗口
      deprecated: false
      description: 切换浏览器窗口，将窗口置顶显示
      tags:
        - 浏览器环境
        - 浏览器环境
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                containerCode:
                  description: 环境ID
                  type: string
              required:
                - containerCode
              x-apifox-orders:
                - containerCode
            example: "{\r\n    \"containerCode\":\"223012801\"  //  环境ID\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  containerCodes:
                    type: string
                    title: 环境ID
                  requestId:
                    type: string
                    title: 请求ID
                  msg:
                    type: string
                    title: 业务消息
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: object
                    properties:
                      action:
                        type: string
                        title: 执行的动作/命令名称
                      err:
                        type: string
                        title: 执行结果描述
                      requestId:
                        type: string
                        title: 请求ID
                      statusCode:
                        type: string
                        title: 操作状态码
                    x-apifox-orders:
                      - action
                      - err
                      - requestId
                      - statusCode
                    title: 业务数据
                x-apifox-orders:
                  - containerCodes
                  - requestId
                  - msg
                  - code
                  - data
              example:
                requestId: d273d041-eb67-4207-80f3-078dd6aee5b4
                msg: Success
                code: 0
                data:
                  action: ForegroundBrowser
                  err: 成功(Success)
                  requestId: d273d041-eb67-4207-80f3-078dd6aee5b4
                  statusCode: '0'
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 浏览器环境
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052364-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 获取全部屏幕（物理机的屏幕） `POST /api/v1/display/all`

用于获取全部屏幕，获取到的屏幕id可用于浏览器环境窗口自定义排列。

来源：https://api-docs.hubstudio.cn/380052367e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/display/all:
    post:
      summary: 获取全部屏幕（物理机的屏幕）
      deprecated: false
      description: 用于获取全部屏幕，获取到的屏幕id可用于浏览器环境窗口自定义排列。
      tags:
        - 浏览器环境
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties: {}
              x-apifox-orders: []
            examples: {}
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: object
                    properties:
                      action:
                        type: string
                        title: 动作
                      err:
                        type: string
                        title: 动作信息
                      requestId:
                        type: string
                        title: 请求ID
                      screens:
                        type: array
                        items:
                          type: object
                          properties:
                            current:
                              type: boolean
                              title: 当前屏幕
                              description: true为操作屏幕
                            height:
                              type: integer
                              title: 窗口高度
                            id:
                              type: integer
                              title: 屏幕id
                            internal:
                              type: boolean
                            isPrimaryScreen:
                              type: boolean
                              title: 是否主要屏幕
                            realHeight:
                              type: integer
                              title: 真实高
                            realWidth:
                              type: integer
                              title: 真实宽
                            scaleFactor:
                              type: integer
                            width:
                              type: integer
                              title: 窗口宽度
                            x:
                              type: integer
                              title: 起始位置x坐标
                            'y':
                              type: integer
                              title: 起始位置y坐标
                          x-apifox-orders:
                            - current
                            - height
                            - id
                            - internal
                            - isPrimaryScreen
                            - realHeight
                            - realWidth
                            - scaleFactor
                            - width
                            - x
                            - 'y'
                        title: 屏幕信息
                      statusCode:
                        type: string
                        title: 状态码
                    x-apifox-orders:
                      - action
                      - err
                      - requestId
                      - screens
                      - statusCode
                    title: 业务数据
                  msg:
                    type: string
                    title: 业务消息
                  requestId:
                    type: string
                    title: 请求ID
                x-apifox-orders:
                  - requestId
                  - msg
                  - code
                  - data
              example:
                requestId: df260aa7-2789-46c8-aaaf-a2f2a12c68c1
                msg: Success
                code: 0
                data:
                  action: getAllDisplay
                  err: 成功(Success)
                  requestId: df260aa7-2789-46c8-aaaf-a2f2a12c68c1
                  screens:
                    - current: true
                      height: 1080
                      id: 2528732444
                      internal: false
                      isPrimaryScreen: true
                      realHeight: 1080
                      realWidth: 1920
                      scaleFactor: 1
                      width: 1920
                      x: 0
                      'y': 0
                    - current: false
                      height: 1080
                      id: 2779098405
                      internal: false
                      isPrimaryScreen: false
                      realHeight: 1080
                      realWidth: 1920
                      scaleFactor: 1
                      width: 1920
                      x: 1920
                      'y': 0
                  statusCode: '0'
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 浏览器环境
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052367-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 浏览器窗口自定义排列 `POST /api/v1/browser/arrange`

用于已打开浏览器窗口自定义排列，不传值则使用默认值进行浏览器窗口排列

来源：https://api-docs.hubstudio.cn/380052368e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/browser/arrange:
    post:
      summary: 浏览器窗口自定义排列
      deprecated: false
      description: 用于已打开浏览器窗口自定义排列，不传值则使用默认值进行浏览器窗口排列
      tags:
        - 浏览器环境
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                x:
                  description: 默认为10，取值可为0~9999之间的整数
                  type: integer
                  title: 起始位置x坐标
                'y':
                  description: 默认为10，取值可为0~9999之间的整数
                  type: integer
                  title: 起始位置y坐标
                width:
                  description: 默认为600，取值范围500~9999之间的整数
                  type: integer
                  title: 窗口宽度
                height:
                  description: 默认为500，取值范围200~9999之间的整数
                  type: integer
                  title: 窗口高度
                gapX:
                  description: 默认为20，取值范围-9999~9999之间的整数
                  type: integer
                  title: 窗口横向间距
                gapY:
                  description: 默认为20，取值范围-9999~9999之间的整数
                  type: integer
                  title: 窗口纵向间距
                colNum:
                  description: 默认为3，取值范围1~99之间的整数
                  type: integer
                  title: 每行展示窗口数量
                screenId:
                  type: integer
                  title: 屏幕ID
              x-apifox-orders:
                - x
                - 'y'
                - width
                - height
                - gapX
                - gapY
                - colNum
                - screenId
            example: "{\r\n    \"x\": 22,  // 起始位置x坐标；默认为10，取值可为0~9999之间的整数\r\n    \"y\": 0,  //  起始位置y坐标；默认为10，取值可为0~9999之间的整数\r\n    \"width\": 500,  // 窗口宽度；默认为600，取值范围500~9999之间的整数\r\n    \"height\": 500,  // 窗口高度；默认为500，取值范围200~9999之间的整数\r\n    \"gapX\": 0, // 窗口横向间距；默认为20，取值范围-9999~9999之间的整数\r\n    \"gapY\":0,  // 窗口纵向间距；默认为20，取值范围-9999~9999之间的整数\r\n    \"colNum\": 2,  // 每行展示窗口数量；默认为3，取值范围1~99之间的整数 \r\n    \"screenId\": 41795266 // 屏幕id\r\n\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  requestId:
                    type: string
                    title: 请求ID
                    nullable: true
                  msg:
                    type: string
                    title: 业务消息
                    nullable: true
                  code:
                    type: integer
                    title: 业务状态码
                    nullable: true
                  data:
                    type: object
                    properties:
                      action:
                        type: string
                        title: 动作
                        nullable: true
                      err:
                        type: string
                        title: 动作信息
                        nullable: true
                      requestId:
                        type: string
                        title: 请求ID
                        nullable: true
                      statusCode:
                        type: string
                        title: 状态码
                        nullable: true
                    x-apifox-orders:
                      - action
                      - err
                      - requestId
                      - statusCode
                    title: 业务数据
                    nullable: true
                x-apifox-orders:
                  - requestId
                  - msg
                  - code
                  - data
              example:
                requestId: b7d293ea-b70f-46db-bc38-0fbd76cfd44d
                msg: Success
                code: 0
                data:
                  action: customArrange
                  err: 成功(Success)
                  requestId: b7d293ea-b70f-46db-bc38-0fbd76cfd44d
                  statusCode: '0'
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 浏览器环境
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052368-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```


### 云手机 > 应用管理

#### APP列表(分页)\查询可安装应用列表 `POST /api/v1/cloud-mobile/app/page`

获取APP应用列表

来源：https://api-docs.hubstudio.cn/380052350e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/app/page:
    post:
      summary: APP列表(分页)\查询可安装应用列表
      deprecated: false
      description: 获取APP应用列表
      tags:
        - 云手机/应用管理
        - 云手机/应用管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                appName:
                  description: 模糊查询
                  type: string
                  title: 应用名称
                pageNum:
                  description: 从1开始
                  type: integer
                  title: 当前页数
                pageSize:
                  type: integer
                  title: 每页显示记录数
                  description: 默认为10
                productId:
                  type: integer
                  title: 云手机商品ID
                  description: 通过 /api/v1/cloud-mobile/mobile-product-list接口获得
              required:
                - productId
              x-apifox-orders:
                - appName
                - pageNum
                - pageSize
                - productId
            example:
              pageNum: 2
              pageSize: 2
              productId: 178
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  msg:
                    type: string
                    title: 响应消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  data:
                    type: object
                    properties:
                      records:
                        type: array
                        items:
                          type: object
                          properties:
                            id:
                              type: string
                              title: 应用ID
                            appName:
                              type: string
                              title: 应用名称
                            appIcon:
                              type: string
                              title: 应用图标
                            packageName:
                              type: string
                              title: 应用包名
                            categoryValue:
                              type: integer
                              title: 应用分类ID
                            appVersionList:
                              type: array
                              items:
                                type: object
                                properties:
                                  id:
                                    type: string
                                    title: 应用版本ID
                                  versionCode:
                                    type: string
                                    title: 应用版本编号
                                  versionName:
                                    type: string
                                    title: 应用版本名称
                                  installStatus:
                                    type: integer
                                    title: 安装状态
                                  groupId:
                                    type: string
                                x-apifox-orders:
                                  - id
                                  - versionCode
                                  - versionName
                                  - installStatus
                                  - groupId
                              title: 版本列表
                          x-apifox-orders:
                            - id
                            - appName
                            - appIcon
                            - packageName
                            - categoryValue
                            - appVersionList
                        title: 应用信息列表
                      total:
                        type: integer
                        title: 应用信息列表总数
                      size:
                        type: integer
                        title: 每页显示记录数
                      current:
                        type: integer
                        title: 当前页数
                      orders:
                        type: array
                        items:
                          type: string
                      optimizeCountSql:
                        type: boolean
                      hitCount:
                        type: boolean
                      countId:
                        type: 'null'
                      maxLimit:
                        type: 'null'
                      searchCount:
                        type: boolean
                      pages:
                        type: integer
                    x-apifox-orders:
                      - records
                      - total
                      - size
                      - current
                      - orders
                      - optimizeCountSql
                      - hitCount
                      - countId
                      - maxLimit
                      - searchCount
                      - pages
                    title: 业务数据载体
                x-apifox-orders:
                  - code
                  - msg
                  - requestId
                  - timestamp
                  - data
              example:
                code: 0
                msg: Success
                requestId: 2ed165b5-7715-477d-9486-7a78dc70697e
                timestamp: 1772612247534
                data:
                  records:
                    - id: '15'
                      appName: SHEIN
                      appIcon: >-
                        https://hubstudio-site-test.s3.cn-north-1.amazonaws.com.cn/xxxlllyyy/123/appIcon/be0084537c3f467f8729eeec4a53a53e.png
                      packageName: com.zzkko
                      categoryValue: 11
                      appVersionList:
                        - id: '101041954191573205'
                          versionCode: '1241'
                          versionName: 11.3.4
                          installStatus: -1
                          groupId: ''
                    - id: '117'
                      appName: 'Girls'' Frontline: Fire Control'
                      appIcon: >-
                        https://hubstudio-site-test.s3.cn-north-1.amazonaws.com.cn/xxxlllyyy/123/appIcon/aba495ba5611409f857de49d07be26e5.png
                      packageName: com.sunborn.net
                      categoryValue: 0
                      appVersionList:
                        - id: '1983109510426611713'
                          versionCode: '3101'
                          versionName: 0.5.1
                          installStatus: -1
                          groupId: ''
                    - id: '106'
                      appName: ROMWE
                      appIcon: >-
                        https://hubstudio-site-test.s3.cn-north-1.amazonaws.com.cn/xxxlllyyy/123/appIcon/a5ccfc77bc2d4f55963eb779008aa75c.png
                      packageName: com.romwe
                      categoryValue: 7
                      appVersionList:
                        - id: '1956286086236688385'
                          versionCode: '1058'
                          versionName: 12.7.0
                          installStatus: -1
                          groupId: ''
                    - id: '109'
                      appName: 貓咪大戰爭
                      appIcon: >-
                        https://hubstudio-site-test.s3.cn-north-1.amazonaws.com.cn/xxxlllyyy/123/appIcon/1b0a2ef3e4f04f6a8eadb5aeddc6a0f0.png
                      packageName: jp.co.ponos.battlecatstw
                      categoryValue: 0
                      appVersionList:
                        - id: '1956190408831496194'
                          versionCode: '1405000'
                          versionName: 14.5.0
                          installStatus: -1
                          groupId: ''
                    - id: '20'
                      appName: WhatsApp
                      appIcon: >-
                        http://wuin.oss-cn-shenzhen.aliyuncs.com/apps/icon/1821817619204227074/836faf63b411b913ece7227cbd4c0ad9.png
                      packageName: com.whatsapp
                      categoryValue: 11
                      appVersionList:
                        - id: '1971062120974745601'
                          versionCode: '252704002'
                          versionName: 2.25.27.4
                          installStatus: -1
                          groupId: ''
                        - id: '1965263899673198593'
                          versionCode: '252501000'
                          versionName: 2.25.25.1
                          installStatus: -1
                          groupId: ''
                        - id: '1963814363721355265'
                          versionCode: '252422000'
                          versionName: 2.25.24.22
                          installStatus: -1
                          groupId: ''
                        - id: '1956566604983324674'
                          versionCode: '252311006'
                          versionName: 2.25.23.11
                          installStatus: -1
                          groupId: ''
                        - id: '1956178928543297537'
                          versionCode: '252306000'
                          versionName: 2.25.23.6
                          installStatus: -1
                          groupId: ''
                        - id: '1956143815423926274'
                          versionCode: '252305006'
                          versionName: 2.25.23.5
                          installStatus: -1
                          groupId: ''
                        - id: '101041954191573274'
                          versionCode: '242179003'
                          versionName: 2.24.21.79
                          installStatus: -1
                          groupId: '-1'
                        - id: '101041954191573158'
                          versionCode: '241922005'
                          versionName: 2.24.19.22
                          installStatus: -1
                          groupId: '-1'
                        - id: '101041954191573159'
                          versionCode: '241876004'
                          versionName: 2.24.18.76
                          installStatus: -1
                          groupId: ''
                    - id: '21'
                      appName: "WhatsApp\_Business"
                      appIcon: >-
                        https://hubstudio-site-test.s3.cn-north-1.amazonaws.com.cn/xxxlllyyy/123/appIcon/6ad67c60dffe4b67a0c7c2937e459206.png
                      packageName: com.whatsapp.w4b
                      categoryValue: 1
                      appVersionList:
                        - id: '2008382810841366529'
                          versionCode: '253776000'
                          versionName: 2.25.37.76
                          installStatus: -1
                          groupId: ''
                        - id: '1924789682024656898'
                          versionCode: '251608003'
                          versionName: 2.25.16.8
                          installStatus: -1
                          groupId: ''
                    - id: '22'
                      appName: X
                      appIcon: >-
                        https://hubstudio-site-test.s3.cn-north-1.amazonaws.com.cn/xxxlllyyy/123/appIcon/2ffb9184c22f421eadb53a8ad9bf070a.png
                      packageName: com.twitter.android
                      categoryValue: 2
                      appVersionList:
                        - id: '1970699722048638978'
                          versionCode: '311240101'
                          versionName: 11.24.0-beta.1
                          installStatus: -1
                          groupId: ''
                        - id: '1964539128568455170'
                          versionCode: '311200101'
                          versionName: 11.20.0-beta.1
                          installStatus: -1
                          groupId: ''
                        - id: '1963089575440678913'
                          versionCode: '311180101'
                          versionName: 11.18.0-beta.1
                          installStatus: -1
                          groupId: ''
                        - id: '1955916093204549633'
                          versionCode: '311110104'
                          versionName: 11.11.0-beta.4
                          installStatus: -1
                          groupId: ''
                        - id: '101041954191573157'
                          versionCode: '310490000'
                          versionName: 10.49.0-release.0
                          installStatus: -1
                          groupId: '-1'
                    - id: '5'
                      appName: Facebook
                      appIcon: >-
                        https://hubstudio-site-test.s3.cn-north-1.amazonaws.com.cn/xxxlllyyy/123/appIcon/e8f02a65a77e4b8e97ecb1787a726ed4.png
                      packageName: com.facebook.katana
                      categoryValue: 11
                      appVersionList:
                        - id: '1969250168996741121'
                          versionCode: '465218036'
                          versionName: 531.0.0.47.70
                          installStatus: -1
                          groupId: ''
                        - id: '1967075851450011649'
                          versionCode: '465017258'
                          versionName: 530.0.0.48.74
                          installStatus: -1
                          groupId: ''
                        - id: '1963814361099915266'
                          versionCode: '464816426'
                          versionName: 529.0.0.44.73
                          installStatus: -1
                          groupId: ''
                        - id: '1962002412233949186'
                          versionCode: '464618862'
                          versionName: 528.0.0.62.74
                          installStatus: -1
                          groupId: ''
                        - id: '1953632831853572098'
                          versionCode: '463813471'
                          versionName: 524.0.0.52.44
                          installStatus: -1
                          groupId: ''
                        - id: '1950803111650885634'
                          versionCode: '463615101'
                          versionName: 523.0.0.39.61
                          installStatus: -1
                          groupId: ''
                        - id: '1956293035581853698'
                          versionCode: '458034702'
                          versionName: 495.0.0.45.201
                          installStatus: -1
                          groupId: ''
                        - id: '101041954191573293'
                          versionCode: '457006754'
                          versionName: 490.0.0.0.49
                          installStatus: -1
                          groupId: '-1'
                        - id: '101041954191573194'
                          versionCode: '455016331'
                          versionName: 480.0.0.54.88
                          installStatus: -1
                          groupId: '-1'
                        - id: '101041954191573298'
                          versionCode: '454415720'
                          versionName: 476.0.0.49.74
                          installStatus: -1
                          groupId: '-1'
                        - id: '101041954191573150'
                          versionCode: '453204196'
                          versionName: 471.0.0.0.42
                          installStatus: -1
                          groupId: '-1'
                        - id: '101041954191573149'
                          versionCode: '452812439'
                          versionName: 469.0.0.39.80
                          installStatus: -1
                          groupId: '-1'
                        - id: '101041954191573126'
                          versionCode: '452615712'
                          versionName: 468.1.0.56.78
                          installStatus: -1
                          groupId: '-1'
                    - id: '69'
                      appName: AliExpress
                      appIcon: >-
                        http://wuin.oss-cn-shenzhen.aliyuncs.com/apps/icon/1821817619204227074/795f12054c00bc49d0bbaea8f92d8d87.png
                      packageName: com.alibaba.aliexpresshd
                      categoryValue: 7
                      appVersionList:
                        - id: '1969250175229476865'
                          versionCode: '80005675'
                          versionName: 8.142.2
                          installStatus: -1
                          groupId: ''
                        - id: '1965626292307726338'
                          versionCode: '80005656'
                          versionName: 8.141.2
                          installStatus: -1
                          groupId: ''
                        - id: '1956143820792754178'
                          versionCode: '80005636'
                          versionName: 8.138.2
                          installStatus: -1
                          groupId: ''
                        - id: '101041954191573199'
                          versionCode: '80004068'
                          versionName: 8.101.6
                          installStatus: -1
                          groupId: ''
                    - id: '67'
                      appName: Alibaba.com
                      appIcon: >-
                        http://wuin-cc.oss-cn-shenzhen.aliyuncs.com/apps/icon/1821817619204227074/ee58a3f10e22d8d89bb1435414cbec76.png
                      packageName: com.alibaba.intl.android.apps.poseidon
                      categoryValue: 7
                      appVersionList:
                        - id: '101041954191573255'
                          versionCode: '86301'
                          versionName: 8.63.1
                          installStatus: -1
                          groupId: '-1'
                        - id: '101041954191573215'
                          versionCode: '86201'
                          versionName: 8.62.1
                          installStatus: -1
                          groupId: '-1'
                        - id: '101041954191573214'
                          versionCode: '86200'
                          versionName: 8.62.0
                          installStatus: -1
                          groupId: '-1'
                        - id: '101041954191573218'
                          versionCode: '85900'
                          versionName: 8.59.0
                          installStatus: -1
                          groupId: '-1'
                        - id: '101041954191573208'
                          versionCode: '73700'
                          versionName: 7.37.0
                          installStatus: -1
                          groupId: '-1'
                  total: 10
                  size: 10
                  current: 1
                  orders: []
                  optimizeCountSql: true
                  hitCount: false
                  countId: null
                  maxLimit: null
                  searchCount: true
                  pages: 1
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机/应用管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052350-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 已安装应用列表查询 `POST /api/v1/cloud-mobile/app/installedList`

查询云手机已安装应用列表查询

来源：https://api-docs.hubstudio.cn/380052353e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/app/installedList:
    post:
      summary: 已安装应用列表查询
      deprecated: false
      description: 查询云手机已安装应用列表查询
      tags:
        - 云手机/应用管理
        - 云手机/应用管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                mobileId:
                  description: 云手机ID
                  type: integer
              required:
                - mobileId
              x-apifox-orders:
                - mobileId
            example: "{\r\n  \"mobileId\": 36644// 云手机ID\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  msg:
                    type: string
                    title: 响应消息
                  data:
                    type: array
                    title: 业务数据载体
                    items:
                      type: object
                      properties:
                        appName:
                          type: string
                          title: 应用名称
                        createTime:
                          type: string
                          title: 创建时间或者安装时间
                        packageName:
                          type: string
                          title: 应用包名
                          description: Android应用的唯一标识
                        status:
                          type: integer
                          title: 应用状态
                        versionCode:
                          type: integer
                          title: 应用版本编号
                        versionName:
                          type: string
                          title: 应用版本名称
                      x-apifox-orders:
                        - appName
                        - createTime
                        - packageName
                        - status
                        - versionCode
                        - versionName
                  requestId:
                    type: string
                    title: 请求ID
                  success:
                    type: boolean
                    title: 操作是否成功
                  timestamp:
                    type: integer
                    title: 时间戳
                x-apifox-orders:
                  - code
                  - msg
                  - data
                  - requestId
                  - success
                  - timestamp
              example:
                code: 0
                msg: Success
                requestId: 8ab304fa-cd40-40a8-b6df-62026d972c59
                timestamp: 1772604916212
                data:
                  - appName: null
                    packageName: com.android.provider.apt
                    versionCode: 878
                    versionName: 1.0.0
                    status: 1
                    createTime: '2025-11-27 19:28:36'
                  - appName: null
                    packageName: com.android.provider.av
                    versionCode: 825
                    versionName: 1.0.0
                    status: 1
                    createTime: '2025-11-27 19:28:36'
                  - appName: null
                    packageName: com.android.provider.kernel
                    versionCode: 835
                    versionName: 1.0.0
                    status: 1
                    createTime: '2025-11-27 19:28:36'
                  - appName: null
                    packageName: com.android.provider.mock
                    versionCode: 877
                    versionName: '3.0'
                    status: 1
                    createTime: '2025-11-27 19:28:36'
                  - appName: null
                    packageName: com.android.provider.proxy
                    versionCode: 863
                    versionName: 1.0.0
                    status: 1
                    createTime: '2025-11-27 19:28:36'
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机/应用管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052353-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 新增团队应用 `POST /api/v1/cloud-mobile/group/app/create`

新增团队应用，可以在客户端-云手机-云手机应用-团队应用中查看

来源：https://api-docs.hubstudio.cn/395424141e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/group/app/create:
    post:
      summary: 新增团队应用
      deprecated: false
      description: 新增团队应用，可以在客户端-云手机-云手机应用-团队应用中查看
      tags:
        - 云手机/应用管理
        - 云手机/应用管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                packageName:
                  type: string
                  title: 应用包名
                versionCode:
                  type: string
                  title: 应用版本号
              x-apifox-orders:
                - packageName
                - versionCode
              required:
                - packageName
                - versionCode
            example: "{\r\n  \"packageName\": \"com.zzkko\", // 应用包名\r\n  \"versionCode\": 1241   // 应用版本号\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: object
                    properties:
                      id:
                        type: integer
                        title: 团队应用ID
                    title: 业务数据载体
                    x-apifox-orders:
                      - id
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  msg:
                    type: string
                x-apifox-orders:
                  - code
                  - msg
                  - requestId
                  - timestamp
                  - data
              example:
                code: 0
                msg: Success
                requestId: fda1584e-d67f-4653-ad20-fa3d884b6acf
                timestamp: 1772606157592
                data:
                  id: 431
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机/应用管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-395424141-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### app应用安装 `POST /api/v1/cloud-mobile/app/install`

安装指定应用APP

来源：https://api-docs.hubstudio.cn/380052354e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/app/install:
    post:
      summary: app应用安装
      deprecated: false
      description: |-
        安装指定应用APP
        ```text
        备注：
        1、API安装应用需要手机处于开机状态下才可以安装
        2、暂时无法回传云手机内的APP应用是否安装成功，需在云手机内强刷页面，才能显示
        3、只支持客户端共存的API调用方式使用
        4、每秒只能安装一个应用APP
        ```
      tags:
        - 云手机/应用管理
        - 云手机/应用管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                appVersionId:
                  type: string
                  title: APP版本ID
                  description: 通过/api/v1/cloud-mobile/app/page获得
                isEnablePermission:
                  type: boolean
                  title: 是否开启应用权限
                  description: 默认:false
                isEnableRoot:
                  description: 默认：false
                  type: boolean
                  title: 是否开启ROOT权限
                packageName:
                  description: 包名与应用版本号组合使用，如果传appVersionId参数，该字段可以不传
                  type: string
                  title: 包名
                versionCode:
                  description: 包名与应用版本号组合使用，如果传appVersionId参数，该字段可以不传
                  type: string
                  title: 应用版本号
                mobileId:
                  type: integer
                  title: 云手机ID
                  description: 通过/api/v1/cloud-mobile/mobile-product-list获取
              required:
                - mobileId
              x-apifox-orders:
                - appVersionId
                - isEnablePermission
                - isEnableRoot
                - packageName
                - versionCode
                - mobileId
            example: "{\r\n  \"appVersionId\": \"1983109510426611713\", // APP版本ID\r\n  \"mobileId\": 36644 // 云手机ID\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  msg:
                    type: string
                    title: 响应消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  data:
                    type: boolean
                    title: 业务数据载体
                x-apifox-orders:
                  - code
                  - msg
                  - requestId
                  - timestamp
                  - data
              example:
                code: 0
                msg: Success
                requestId: 07ece6731a5e4450b9dee2fdb09cb6ee
                timestamp: 1742289013309
                data: true
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机/应用管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052354-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### APP启动 `POST /api/v1/cloud-mobile/app/start`

启动云手机中安装的app

来源：https://api-docs.hubstudio.cn/380052356e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/app/start:
    post:
      summary: APP启动
      deprecated: false
      description: |-
        启动云手机中安装的app
        ```
        云手机必须是开启的状态
        ```
      tags:
        - 云手机/应用管理
        - 云手机/应用管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                mobileId:
                  type: integer
                  title: 云手机ID
                  description: |
                    通过/api/v1/cloud-mobile/mobile-product-list获取
                packageName:
                  type: string
                  title: 应用包名
                  description: |
                    通过/api/v1/cloud-mobile/app/installedList获取
              required:
                - mobileId
                - packageName
              x-apifox-orders:
                - mobileId
                - packageName
            example: "{\r\n  \"mobileId\": 0,  // 云手机ID\r\n  \"packageName\": \"\"  // 应用包名\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  msg:
                    type: string
                    title: 响应消息
                  data:
                    type: boolean
                    title: 业务数据载体
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                x-apifox-orders:
                  - code
                  - msg
                  - data
                  - requestId
                  - timestamp
              example:
                code: 0
                msg: Success
                requestId: 07ece6731a5e4450b9dee2fdb09cb6ee
                timestamp: 1742289013309
                data: true
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机/应用管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052356-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### APP重启 `POST /api/v1/cloud-mobile/app/restart`

重启云手机中安装的app

来源：https://api-docs.hubstudio.cn/380052355e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/app/restart:
    post:
      summary: APP重启
      deprecated: false
      description: |-
        重启云手机中安装的app
        ```
        云手机必须是开启的状态
        ```
      tags:
        - 云手机/应用管理
        - 云手机/应用管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                mobileId:
                  type: integer
                  title: 云手机ID
                  description: 通过/api/v1/cloud-mobile/mobile-product-list获取
                packageName:
                  type: string
                  title: 应用包名
                  description: 通过/api/v1/cloud-mobile/app/installedList获取
              required:
                - mobileId
                - packageName
              x-apifox-orders:
                - mobileId
                - packageName
            example: "{\r\n  \"mobileId\": 0,  // 云手机ID\r\n  \"packageName\": \"\"  // 应用包名\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  msg:
                    type: string
                    title: 响应消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  data:
                    type: boolean
                    title: 业务数据载体
                x-apifox-orders:
                  - code
                  - msg
                  - requestId
                  - timestamp
                  - data
              example:
                code: 0
                msg: Success
                requestId: 07ece6731a5e4450b9dee2fdb09cb6ee
                timestamp: 1742289013309
                data: true
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机/应用管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052355-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### APP停止 `POST /api/v1/cloud-mobile/app/stop`

停止云手机运行中的app

来源：https://api-docs.hubstudio.cn/380052357e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/app/stop:
    post:
      summary: APP停止
      deprecated: false
      description: |-
        停止云手机运行中的app
        ```
        云手机必须是开启的状态
        ```
      tags:
        - 云手机/应用管理
        - 云手机/应用管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                mobileId:
                  type: integer
                  title: 云手机ID
                  description: 通过/api/v1/cloud-mobile/mobile-product-list获取
                packageName:
                  type: string
                  title: 应用包名
                  description: 通过/api/v1/cloud-mobile/app/installedList获取
              required:
                - mobileId
                - packageName
              x-apifox-orders:
                - mobileId
                - packageName
            example: "{\r\n  \"mobileId\": 0,  // 云手机ID\r\n  \"packageName\": \"\"  // 应用包名\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  msg:
                    type: string
                    title: 响应消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  data:
                    type: boolean
                    title: 业务数据载体
                x-apifox-orders:
                  - code
                  - msg
                  - requestId
                  - timestamp
                  - data
              example:
                code: 0
                msg: Success
                requestId: 07ece6731a5e4450b9dee2fdb09cb6ee
                timestamp: 1742289013309
                data: true
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机/应用管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052357-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### APP卸载 `POST /api/v1/cloud-mobile/app/uninstall`

卸载云手机已安装的app

来源：https://api-docs.hubstudio.cn/380052358e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/app/uninstall:
    post:
      summary: APP卸载
      deprecated: false
      description: |-
        卸载云手机已安装的app
        ```
        云手机必须是开启的状态
        ```
      tags:
        - 云手机/应用管理
        - 云手机/应用管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                mobileId:
                  type: integer
                  title: 云手机ID
                  description: 通过/api/v1/cloud-mobile/mobile-product-list获取
                packageName:
                  type: string
                  title: 应用包名
                  description: |
                    通过/api/v1/cloud-mobile/app/installedList获取
              required:
                - mobileId
                - packageName
              x-apifox-orders:
                - mobileId
                - packageName
            example: "{\r\n  \"mobileId\": 0,  // 云手机ID\r\n  \"packageName\": \"\"  // 应用包名\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  msg:
                    type: string
                    title: 响应消息
                  data:
                    type: boolean
                    title: 业务数据载体
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                x-apifox-orders:
                  - code
                  - msg
                  - data
                  - requestId
                  - timestamp
              example:
                code: 0
                msg: Success
                requestId: 07ece6731a5e4450b9dee2fdb09cb6ee
                timestamp: 1742289013309
                data: true
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机/应用管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052358-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```


### 云手机 > 文件管理

#### 选择本地上传文件到云手机 `POST /api/v2/cloud-mobile/upload-file`

上传文件到云手机

来源：https://api-docs.hubstudio.cn/380052349e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v2/cloud-mobile/upload-file:
    post:
      summary: 选择本地上传文件到云手机
      deprecated: false
      description: 上传文件到云手机
      tags:
        - 云手机/文件管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
      requestBody:
        content:
          multipart/form-data:
            schema:
              type: object
              properties:
                downloadDest:
                  description: 上传至云手机中的目录，如果仅一层目录不存在，将自动创建；如果多层目录不存在，无法自动创建
                  example: /Download
                  type: string
                fileUrl:
                  format: binary
                  type: string
                  description: 文件地址
                  example: file://C:\Users\Administrator\Desktop\API接口.txt
                mobileId:
                  description: 云手机ID
                  example: '3279080'
                  type: string
                fileName:
                  description: 文件名称，文件上传到云手机的名字，注意包括后缀
                  example: 文件名
                  type: string
              required:
                - fileUrl
                - mobileId
            examples: {}
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    description: 0表示成功，其余为失败
                    type: string
                    title: 结果码
                  data:
                    type: boolean
                    title: 业务数据
                  msg:
                    type: string
                    title: 响应消息
                x-apifox-orders:
                  - code
                  - data
                  - msg
              example:
                code: ''
                data: true
                msg: ''
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机/文件管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052349-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 公网文件上传文件到云手机 `POST /api/v1/cloud-mobile/upload-file`

上传文件到云手机

来源：https://api-docs.hubstudio.cn/395427282e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/upload-file:
    post:
      summary: 公网文件上传文件到云手机
      deprecated: false
      description: 上传文件到云手机
      tags:
        - 云手机/文件管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                downloadDest:
                  type: string
                  description: 如果仅一层目录不存在，将自动创建；如果多层目录不存在，无法自动创建
                  title: 上传至云手机中的目录
                fileUrl:
                  type: string
                  title: 文件地址
                mobileId:
                  type: integer
                  title: 云手机ID
                fileName:
                  type: string
                  title: 文件名称
                  description: 上传后文件叫什么名字
              x-apifox-orders:
                - downloadDest
                - fileUrl
                - mobileId
                - fileName
              required:
                - mobileId
            example:
              downloadDest: ''
              fileName: ''
              fileUrl: ''
              mobileId: 0
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    description: 0表示成功，其余为失败
                    type: string
                    title: 业务状态码
                  data:
                    type: boolean
                    title: 业务数据
                  msg:
                    type: string
                    title: 结果信息
                x-apifox-orders:
                  - code
                  - data
                  - msg
              example:
                code: ''
                data: true
                msg: ''
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机/文件管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-395427282-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 设置keyBox `POST /api/v1/cloud-mobile/setKeyBox`

设置云手机keybox，只有在云手机开机的情况下才能进行设置，同步keybox的xml文件已经上传到云手机上

来源：https://api-docs.hubstudio.cn/380407020e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/setKeyBox:
    post:
      summary: 设置keyBox
      deprecated: false
      description: 设置云手机keybox，只有在云手机开机的情况下才能进行设置，同步keybox的xml文件已经上传到云手机上
      tags:
        - 云手机/文件管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                mobileId:
                  type: integer
                  title: 云手机ID
                filePath:
                  description: 例如：/sdcard/Download/xxx.xml
                  type: string
                  title: 文件路径
              required:
                - mobileId
                - filePath
              x-apifox-orders:
                - mobileId
                - filePath
            example: "{\r\n  \"mobileId\": 0,  // 云手机ID\r\n  \"filePath\": \"\"  // 文件路径，例如：/sdcard/Download/xxx.xml\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties: {}
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机/文件管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380407020-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```


### 云手机 > RPA

#### RPA-计划分页查询 `POST /api/v1/cloud-mobile/rpa/task/page`

查询计划任务数据

来源：https://api-docs.hubstudio.cn/437761335e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/rpa/task/page:
    post:
      summary: RPA-计划分页查询
      deprecated: false
      description: 查询计划任务数据
      tags:
        - 云手机/RPA
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                enabled:
                  type: boolean
                  title: 是否启用
                  description: true为启动，false为关闭
                searchKey:
                  description: 模糊搜索计划名称、计划描述、模板标题
                  type: string
                  title: 搜索关键词
                taskState:
                  type: integer
                  description: 0：等待执行；1：正在执行；2：执行完成；3：取消
                  title: 任务状态
                current:
                  type: string
                  title: 当前页
                  description: 默认值为第1页
                size:
                  type: string
                  title: 每页条数
                  description: 默认值为10条数据/页
              x-apifox-orders:
                - enabled
                - searchKey
                - taskState
                - current
                - size
            example:
              enabled: true
              current: 1
              size: 10
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  msg:
                    type: string
                    title: 业务消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  data:
                    type: object
                    properties:
                      list:
                        type: array
                        items:
                          type: object
                          properties:
                            id:
                              type: integer
                              title: 计划编号
                            taskName:
                              type: string
                              title: 计划名称
                            taskState:
                              type: integer
                              title: 计划状态
                            templateTitle:
                              type: string
                              title: 模板标题
                            templateId:
                              type: integer
                              title: 模板编号
                            scheduleConfig:
                              type: string
                              title: 计划调度配置
                            enabled:
                              type: boolean
                              title: 是否启用
                            notes:
                              type: string
                              title: 计划描述
                              nullable: true
                            createMember:
                              type: string
                              title: 计划创建者
                            handleTime:
                              type: string
                              title: 计划开始时间
                              nullable: true
                            endTime:
                              type: string
                              title: 计划结束时间
                              nullable: true
                            taskPreviewTimeTable:
                              type: array
                              items:
                                type: object
                                properties:
                                  mobileName:
                                    type: string
                                    title: 云手机名称
                                  mobileId:
                                    type: integer
                                    title: 云手机ID
                                  mobileSerialNumber:
                                    type: integer
                                    title: 云手机序号
                                  mobileBillingType:
                                    type: integer
                                    title: 计费方式
                                    description: 0 免费 1 按需 2 包月
                                  times:
                                    type: array
                                    items:
                                      type: string
                                    title: 预估执行时间列表
                                x-apifox-orders:
                                  - mobileName
                                  - mobileId
                                  - mobileSerialNumber
                                  - mobileBillingType
                                  - times
                              title: 计划记录预览的调度时间表
                            successCount:
                              type: integer
                              title: 执行成功数量
                              nullable: true
                            failCount:
                              type: integer
                              title: 计划记录失败运行次数
                              nullable: true
                          x-apifox-orders:
                            - id
                            - taskName
                            - taskState
                            - templateTitle
                            - templateId
                            - scheduleConfig
                            - enabled
                            - notes
                            - createMember
                            - handleTime
                            - endTime
                            - taskPreviewTimeTable
                            - successCount
                            - failCount
                      total:
                        type: integer
                        title: 返回数组总数
                    title: 业务数据
                    x-apifox-orders:
                      - list
                      - total
                x-apifox-orders:
                  - code
                  - msg
                  - requestId
                  - timestamp
                  - data
              example:
                code: 0
                msg: Success
                requestId: 3eddab52-05f5-4165-bfdb-176df2942d36
                timestamp: 1774942318141
                data:
                  list:
                    - id: 300000013
                      taskName: 测试
                      taskState: 2
                      templateTitle: >-
                        测试标题和模板过长，UI样式问题长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长
                      templateId: 4
                      scheduleConfig: '{"scheduleType":"ONCE"}'
                      enabled: true
                      notes: ''
                      createMember: BOSS
                      handleTime: '2026-03-31T15:25:52'
                      endTime: null
                      taskPreviewTimeTable: null
                      successCount: 1
                      failCount: 0
                    - id: 300000012
                      taskName: null
                      taskState: 2
                      templateTitle: >-
                        测试标题和模板过长，UI样式问题长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长
                      templateId: 4
                      scheduleConfig: '{"scheduleType":"ONCE","endTime":1774941885123}'
                      enabled: true
                      notes: null
                      createMember: BOSS
                      handleTime: '2026-03-31T15:24:52'
                      endTime: '2026-03-31T15:24:45'
                      taskPreviewTimeTable: null
                      successCount: 0
                      failCount: 1
                  total: 2
          headers: {}
          x-apifox-name: ''
      security: []
      x-apifox-folder: 云手机/RPA
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-437761335-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### RPA-个人模板分页查询 `POST /api/v1/cloud-mobile/rpa/template/personal/page`

个人模板数据查询

来源：https://api-docs.hubstudio.cn/437761336e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/rpa/template/personal/page:
    post:
      summary: RPA-个人模板分页查询
      deprecated: false
      description: 个人模板数据查询
      tags:
        - 云手机/RPA
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                searchKey:
                  description: 可模糊搜索标题和描述
                  type: string
                  title: 搜索关键字
                current:
                  type: string
                  title: 当前页
                  description: 默认值为第1页
                size:
                  type: string
                  title: 每页条数
                  description: 默认值为10条数据/页
              x-apifox-orders:
                - searchKey
                - current
                - size
            example:
              current: 1
              size: 10
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  data:
                    type: object
                    properties:
                      list:
                        type: array
                        items:
                          type: object
                          properties:
                            id:
                              type: integer
                              title: ID
                            sort:
                              type: integer
                              title: 排序
                            title:
                              type: string
                              title: 模板标题
                            description:
                              type: string
                              title: 描述
                              nullable: true
                            parameter:
                              type: 'null'
                            updateDate:
                              type: string
                              title: 更新时间
                            updateMember:
                              type: string
                              title: 更新人
                          x-apifox-orders:
                            - id
                            - sort
                            - title
                            - description
                            - parameter
                            - updateDate
                            - updateMember
                      total:
                        type: integer
                        title: 返回数组总数
                    title: 响应数据
                    x-apifox-orders:
                      - list
                      - total
                  code:
                    type: string
                    title: 业务状态码
                  msg:
                    type: string
                    title: 业务消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: string
                    title: 当前时间戳
                x-apifox-orders:
                  - data
                  - code
                  - msg
                  - requestId
                  - timestamp
              example:
                code: 0
                msg: Success
                requestId: da57b972-9250-4f57-a82e-62a2acaaabdc
                timestamp: 1774958213897
                data:
                  list:
                    - id: 50
                      sort: 0
                      title: 花 鸟
                      description: null
                      parameter: null
                      updateDate: '2026-03-31T19:56:39'
                      updateMember: BOSS
                    - id: 39
                      sort: 0
                      title: 花粉管花粉管
                      description: 华国锋
                      parameter: null
                      updateDate: '2026-03-31T17:45:11'
                      updateMember: BOSS
                  total: 2
          headers: {}
          x-apifox-name: ''
      security: []
      x-apifox-folder: 云手机/RPA
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-437761336-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### RPA-市场模板分页查询 `POST /api/v1/cloud-mobile/rpa/template/market/page`

来源：https://api-docs.hubstudio.cn/437761337e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/rpa/template/market/page:
    post:
      summary: RPA-市场模板分页查询
      deprecated: false
      description: ''
      tags:
        - 云手机/RPA
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                current:
                  type: string
                  title: 当前页
                  description: 默认值为第1页
                size:
                  type: string
                  title: 每页条数
                  description: 默认值为10条数据/页
                searchKey:
                  type: string
                  title: 搜索关键词
              x-apifox-orders:
                - current
                - size
                - searchKey
            example:
              current: '1'
              size: '10'
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  data:
                    type: object
                    properties:
                      list:
                        type: array
                        items:
                          type: object
                          properties:
                            id:
                              type: integer
                              title: ID
                            sort:
                              type: integer
                              title: 排序
                            title:
                              type: string
                              title: 模板标题
                            type:
                              type: integer
                              title: 市场模板类型
                              description: 0：JS模板；1：编排模板
                            description:
                              type: string
                              title: 描述
                            parameter:
                              type: string
                              title: 模板内容
                            updateDate:
                              type: string
                              title: 更新时间
                            appIconUrls:
                              type: array
                              items:
                                type: string
                              title: 应用图标URL列表
                            author:
                              type: string
                              title: 作者
                          x-apifox-orders:
                            - id
                            - sort
                            - title
                            - type
                            - description
                            - parameter
                            - updateDate
                            - appIconUrls
                            - author
                        title: 返回数组
                      total:
                        type: integer
                        title: 返回数组总数
                    title: 响应数据
                    x-apifox-orders:
                      - list
                      - total
                  code:
                    type: string
                    title: 业务状态码
                  msg:
                    type: string
                    title: 业务消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: string
                    title: 当前时间戳
                x-apifox-orders:
                  - data
                  - code
                  - msg
                  - requestId
                  - timestamp
              example:
                code: 0
                msg: Success
                requestId: d8068c6d-e352-42bf-be4a-bcd1ec4fdf4c
                timestamp: 1775117889854
                data:
                  list:
                    - id: 7
                      sort: 0
                      title: TikTok 智能养号
                      type: 1
                      description: 支持自定义编排流程
                      parameter: >-
                        {"点赞概率":{"extra":{"type":"integer","required":false,"name":"点赞概率","index":3,"numberRange":{"enabled":true,"min":0,"max":95},"defaultValue":{"enabled":true,"value":25}},"type":"integer","default":25},"关注概率":{"extra":{"type":"integer","required":false,"name":"关注概率","index":2,"numberRange":{"enabled":true,"min":0,"max":95},"defaultValue":{"enabled":true,"value":0}},"type":"integer","default":0},"查看评论概率":{"extra":{"type":"integer","required":false,"name":"查看评论概率","index":4,"numberRange":{"enabled":true,"min":0,"max":95},"defaultValue":{"enabled":true,"value":5}},"type":"integer","default":5},"预估浏览视频数量":{"extra":{"type":"integer","required":true,"name":"预估浏览视频数量","index":1,"defaultValue":{"enabled":true,"value":130},"numberRange":{"enabled":true,"min":1,"max":1000}},"type":"integer","default":130}}
                      updateDate: '2026-03-26T10:24:39'
                      appIconUrls:
                        - >-
                          http://wuin.oss-cn-shenzhen.aliyuncs.com/apps/icon/3f86a248e7726c8b05369322584ee087.png
                      author: 赵
                    - id: 6
                      sort: 0
                      title: TikTok 发布视频
                      type: 1
                      description: 支持自定义编排流程
                      parameter: >-
                        {"选择视频":{"extra":{"type":"video","required":true,"sizeLimit":{"value":300,"unit":"MB"},"formatType":["mp4","mov"],"name":"选择视频","index":1},"type":"video"},"视频文案":{"extra":{"type":"string","required":false,"multiline":{"enabled":true,"value":3},"name":"视频文案","charValid":{"enabled":true,"value":1000},"index":2,"placeholder":"为视频添加描述性的文案。"},"type":"string"},"封面图片":{"extra":{"type":"image","required":false,"sizeLimit":{"value":5,"unit":"MB"},"formatType":["jpg","png","jpeg"],"name":"封面图片","index":3},"type":"image"},"AI标签":{"extra":{"type":"boolean","required":false,"name":"AI标签","index":4,"placeholder":"添加后，您的内容将显示一条标签，表明它是
                        AI 生成或 AI
                        编辑的内容。"},"type":"boolean"},"获取线索":{"extra":{"type":"boolean","required":false,"name":"获取线索","index":5,"placeholder":"在视频中添加“获取线索”组件？需要已启用“获取线索”功能的商业账号。"},"type":"boolean"},"商品ID":{"extra":{"type":"string","required":false,"multiline":{"enabled":true,"value":2},"name":"商品ID","charValid":{"enabled":true,"value":50},"index":6,"placeholder":"选填。输入商品
                        ID
                        以在视频中挂载商品。如果账号不支持此功能，发布将会失败。"},"type":"string"},"商品标题":{"extra":{"type":"string","required":false,"multiline":{"enabled":true,"value":2},"name":"商品标题","charValid":{"enabled":true,"value":30},"index":7,"placeholder":"选填。输入小黄车对应的商品标题或描述，最多
                        30
                        个字符。"},"type":"string"},"评论":{"extra":{"type":"string","required":false,"multiline":{"enabled":true,"value":3},"name":"评论","charValid":{"enabled":true,"value":200},"index":8,"placeholder":"视频发布成功后自动发布一条评论，最多
                        200 个字符。"},"type":"string"}}
                      updateDate: '2026-03-25T20:24:13'
                      appIconUrls:
                        - >-
                          http://wuin.oss-cn-shenzhen.aliyuncs.com/apps/icon/3f86a248e7726c8b05369322584ee087.png
                      author: 赵
                    - id: 4
                      sort: 0
                      title: Instagram 智能养号
                      type: 1
                      description: 支持自定义编排流程
                      parameter: >-
                        {"Probability of
                        liking":{"extra":{"type":"integer","required":false,"name":"Probability
                        of
                        liking","index":3,"numberRange":{"enabled":true,"min":0,"max":90},"defaultValue":{"enabled":true,"value":5}},"type":"integer","default":5},"Probability
                        of
                        following":{"extra":{"type":"integer","required":false,"name":"Probability
                        of
                        following","index":2,"numberRange":{"enabled":true,"min":0,"max":90},"defaultValue":{"enabled":true,"value":5}},"type":"integer","default":5},"Probability
                        of viewing
                        comments":{"extra":{"type":"integer","required":false,"name":"Probability
                        of viewing
                        comments","index":4,"numberRange":{"enabled":true,"min":0,"max":90},"defaultValue":{"enabled":true,"value":5}},"type":"integer","default":5},"Estimated
                        number of videos
                        browsed":{"extra":{"type":"integer","required":true,"name":"Estimated
                        number of videos
                        browsed","index":1,"defaultValue":{"enabled":true,"value":15},"numberRange":{"enabled":true,"min":1,"max":1000}},"type":"integer","default":15}}
                      updateDate: '2026-03-25T10:56:10'
                      appIconUrls:
                        - >-
                          https://hubstudio-site-test.s3.cn-north-1.amazonaws.com.cn/xxxlllyyy/123/appIcon/2e477dd8f50a42d3b116083247e7806d.png
                      author: Owen
                    - id: 5
                      sort: 1
                      title: Instagram发布视频-Plus
                      type: 1
                      description: 支持自定义编排流程
                      parameter: >-
                        {"videoDownloadUrl":{"type":"video","extra":{"name":"Select
                        Video","type":"video","required":true,"defaultValue":{"enabled":false},"index":1,"sizeLimit":{"value":200,"unit":"MB"},"formatType":["mp4","mov"]}},"videoDescription":{"type":"string","extra":{"name":"Video
                        Caption","type":"string","required":false,"charValid":{"enabled":true,"value":1000},"multiline":{"enabled":true,"value":3},"defaultValue":{"enabled":false},"index":2}},"enableAddAILabel":{"type":"boolean","extra":{"name":"AI
                        Label","type":"boolean","required":false,"defaultValue":{"enabled":false},"index":4}}}
                      updateDate: '2026-04-01T17:07:44'
                      appIconUrls:
                        - >-
                          https://hubstudio-site-test.s3.cn-north-1.amazonaws.com.cn/xxxlllyyy/123/appIcon/2e477dd8f50a42d3b116083247e7806d.png
                      author: Owen
                  total: 4
          headers: {}
          x-apifox-name: ''
      security: []
      x-apifox-folder: 云手机/RPA
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-437761337-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### RPA-保存计划 `POST /api/v1/cloud-mobile/rpa/task/save`

来源：https://api-docs.hubstudio.cn/437761338e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/rpa/task/save:
    post:
      summary: RPA-保存计划
      deprecated: false
      description: ''
      tags:
        - 云手机/RPA
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                cloudPhoneConfigs:
                  type: array
                  items:
                    type: object
                    properties:
                      cloudPhoneId:
                        type: integer
                        title: 云手机ID
                      templateParameter:
                        description: 如果没有参数可不传
                        type: string
                        title: 模板参数
                      triggerTime:
                        type: string
                        title: 触发时间
                    x-apifox-orders:
                      - cloudPhoneId
                      - templateParameter
                      - triggerTime
                  title: 云手机配置集合
                notes:
                  type: string
                  title: 计划描述
                scheduleConfig:
                  type: object
                  properties:
                    endTime:
                      description: 结束时间
                      type: string
                    scheduleType:
                      description: ONCE-一次，DAILY-每天执行,可用值:ONCE,DAILY
                      type: string
                      title: 计划类型
                  title: 计划配置
                  x-apifox-orders:
                    - endTime
                    - scheduleType
                taskName:
                  type: string
                  title: 计划名称
                templateId:
                  type: integer
                  title: 模板ID
                templateType:
                  description: PERSONAL：个人；MARKET：市场
                  type: string
                  title: 模板类型
              required:
                - templateType
                - templateId
                - taskName
                - cloudPhoneConfigs
                - scheduleConfig
              x-apifox-orders:
                - cloudPhoneConfigs
                - notes
                - scheduleConfig
                - taskName
                - templateId
                - templateType
            example:
              mobileConfigs:
                - mobileId: 1400913
                  templateParameter: >-
                    {"__Extra__":{"videoDownloadUrl":{"name":"第11期陈翔六点半：请做出你的选择.mp4","size":18432000},"coverUrl":{"name":"live.png","size":4000}},"videoDownloadUrl":"https://xxxxx.com/%E7%AC%AC11%E6%9C%9F%20%E9%99%88%E7%BF%94%E5%85%AD%E7%82%B9%E5%8D%8A%EF%BC%9A%E8%AF%B7%E5%81%9A%E5%87%BA%E4%BD%A0%E7%9A%84%E9%80%89%E6%8B%A9.mp4","coverUrl":"https://get.morelogin.com/prod/cloudPhoneTaskVideo/1557704228896821/e1b8fe8251a441aca0d8638108615d80/newzhibo-a6a0831ecd.png"}
                  triggerTime: 2026-04-01 15:44
              notes: 计划描述
              scheduleConfig:
                endTime: 2026-04-01 23:44
                scheduleType: ONCE
              taskName: 计划名称
              templateId: 1
              templateType: MARKET
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  data:
                    type: integer
                    title: 响应数据
                    description: 计划ID
                  code:
                    type: string
                    title: 业务状态码
                  msg:
                    type: string
                    title: 业务消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: string
                    title: 当前时间戳
                x-apifox-orders:
                  - data
                  - code
                  - msg
                  - requestId
                  - timestamp
              example:
                code: 0
                msg: Success
                requestId: 64a3d294-cbdf-4aea-b28b-8da75786dc9b
                timestamp: 1775118548523
                data: 300380626
          headers: {}
          x-apifox-name: ''
      security: []
      x-apifox-folder: 云手机/RPA
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-437761338-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### RPA-取消计划 `POST /api/v1/cloud-mobile/rpa/task/cancel`

来源：https://api-docs.hubstudio.cn/437761339e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/rpa/task/cancel:
    post:
      summary: RPA-取消计划
      deprecated: false
      description: ''
      tags:
        - 云手机/RPA
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                id:
                  description: 计划ID
                  type: integer
              required:
                - id
              x-apifox-orders:
                - id
            example:
              id: 300380637
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  data:
                    type: 'null'
                    title: 响应数据
                  code:
                    type: string
                    title: 业务状态码
                  msg:
                    type: string
                    title: 业务消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: string
                    title: 当前时间戳
                x-apifox-orders:
                  - data
                  - code
                  - msg
                  - requestId
                  - timestamp
              example:
                code: 0
                msg: Success
                requestId: ada2baed-ce5b-403a-bc14-1eb401254436
                timestamp: 1775118872354
                data: null
          headers: {}
          x-apifox-name: ''
      security: []
      x-apifox-folder: 云手机/RPA
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-437761339-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### RPA-执行记录分页查询 `POST /api/v1/cloud-mobile/rpa/subTask/page`

来源：https://api-docs.hubstudio.cn/437761340e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/rpa/subTask/page:
    post:
      summary: RPA-执行记录分页查询
      deprecated: false
      description: ''
      tags:
        - 云手机/RPA
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                beginTime:
                  type: string
                  title: 开始时间
                endTime:
                  type: string
                  title: 结束时间
                mobileId:
                  type: integer
                  title: 云手机ID
                mobileName:
                  type: string
                  title: 云手机名称
                searchKey:
                  description: 模糊搜索计划名称、模板标题
                  type: string
                  title: 搜索关键词
                taskId:
                  type: integer
                  title: 计划ID
                taskState:
                  description: 0：等待执行；1：正在执行；2：排队中；3：成功；4：失败；5：已取消
                  type: integer
                  title: 任务状态
                templateId:
                  type: integer
                  title: 模板ID
                current:
                  type: string
                  title: 当前页
                  description: 默认值为第1页
                size:
                  type: string
                  title: 每页条数
                  description: 默认值为10条数据/页
              x-apifox-orders:
                - beginTime
                - endTime
                - mobileId
                - mobileName
                - searchKey
                - taskId
                - taskState
                - templateId
                - current
                - size
            example:
              mobileId: 1400913
              mobileName: android12
              current: 1
              size: 10
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  data:
                    type: object
                    properties:
                      list:
                        type: array
                        items:
                          type: object
                          properties:
                            id:
                              type: integer
                              title: 任务ID
                            taskName:
                              type: string
                              title: 计划名称
                            templateName:
                              type: string
                              title: 模板名称
                            mobileId:
                              type: integer
                              title: 云手机ID
                            mobileName:
                              type: string
                              title: 云手机名称
                            mobileGroupName:
                              type: 'null'
                              title: 云手机分组名称
                            mobileSerialNumber:
                              type: integer
                              title: 云手机序列号
                            taskState:
                              type: integer
                              description: 0：等待执行；1：正在执行；2：排队中；3：成功；4：失败；5：已取消
                              title: 计划状态
                            triggerTime:
                              type: string
                              title: 计划调度时间
                            triggerNextTime:
                              type: string
                              title: 实际调度时间
                            powerOnTime:
                              type: string
                              title: 云手机开机时间
                              nullable: true
                            delayReason:
                              type: string
                              title: 延期原因
                            handleResult:
                              type: integer
                              description: 0：执行失败；1：执行成功
                              title: 记录结果
                              nullable: true
                            handleFailCode:
                              type: string
                              title: 记录失败编码
                              nullable: true
                            handleFailReason:
                              type: string
                              title: 记录失败原因
                              nullable: true
                            handleResultAttachment:
                              type: string
                              title: 记录执行结果附件
                              nullable: true
                            handleTime:
                              type: string
                              title: 记录执行开始时间
                              nullable: true
                            endTime:
                              type: string
                              title: 记录执行开始时间
                              nullable: true
                            createUser:
                              type: string
                              title: 创建用户
                            createDate:
                              type: 'null'
                              title: 创建时间
                            updateUser:
                              type: string
                              title: 修改用户
                            updateDate:
                              type: string
                              title: 修改时间
                          x-apifox-orders:
                            - id
                            - taskName
                            - templateName
                            - mobileId
                            - mobileName
                            - mobileGroupName
                            - mobileSerialNumber
                            - taskState
                            - triggerTime
                            - triggerNextTime
                            - powerOnTime
                            - delayReason
                            - handleResult
                            - handleFailCode
                            - handleFailReason
                            - handleResultAttachment
                            - handleTime
                            - endTime
                            - createUser
                            - createDate
                            - updateUser
                            - updateDate
                        title: 返回数组
                      total:
                        type: integer
                        title: 返回数组总数
                    title: 响应数据
                    x-apifox-orders:
                      - list
                      - total
                  code:
                    type: string
                    title: 业务状态码
                  msg:
                    type: string
                    title: 业务消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: string
                    title: 当前时间戳
                x-apifox-orders:
                  - data
                  - code
                  - msg
                  - requestId
                  - timestamp
              example:
                code: 0
                msg: Success
                requestId: cd802f80-1ba4-43e0-922c-19b316d79935
                timestamp: 1775119172664
                data:
                  list:
                    - id: 500347048
                      taskName: 计划名称
                      templateName: TikTok 发布视频
                      mobileId: 1400913
                      mobileName: android12
                      mobileGroupName: null
                      mobileSerialNumber: 890552344
                      taskState: 5
                      triggerTime: '2026-04-01T15:44:00'
                      triggerNextTime: '2026-04-01T15:44:00'
                      powerOnTime: null
                      delayReason: ''
                      handleResult: null
                      handleFailCode: null
                      handleFailReason: null
                      handleResultAttachment: null
                      handleTime: null
                      endTime: null
                      createUser: BOSS
                      updateUser: BOSS
                      createDate: null
                      updateDate: '2026-04-02T16:34:32'
                    - id: 500347047
                      taskName: 计划名称
                      templateName: TikTok 发布视频
                      mobileId: 1400913
                      mobileName: android12
                      mobileGroupName: null
                      mobileSerialNumber: 890552344
                      taskState: 4
                      triggerTime: '2026-04-01T15:44:00'
                      triggerNextTime: '2026-04-01T15:44:00'
                      powerOnTime: null
                      delayReason: ''
                      handleResult: 0
                      handleFailCode: E114104
                      handleFailReason: BOSS(name)正在使用，请稍候再试！
                      handleResultAttachment: null
                      handleTime: '2026-04-02T16:29:22'
                      endTime: '2026-04-02T16:29:22'
                      createUser: BOSS
                      updateUser: BOSS
                      createDate: null
                      updateDate: '2026-04-02T16:29:22'
                    - id: 500347032
                      taskName: ces
                      templateName: TikTok 智能养号
                      mobileId: 1400913
                      mobileName: android12
                      mobileGroupName: null
                      mobileSerialNumber: 890552344
                      taskState: 3
                      triggerTime: '2026-04-02T11:13:00'
                      triggerNextTime: '2026-04-02T11:13:00'
                      powerOnTime: '2026-04-02T11:16:11'
                      delayReason: ''
                      handleResult: 1
                      handleFailCode: '0'
                      handleFailReason: null
                      handleResultAttachment: >-
                        https://hubstudio-site-pre.s3.cn-north-1.amazonaws.com.cn/pre/taskResultScreenshot/20260402/16cxvk7ouy-2039542379071610881.webp
                      handleTime: '2026-04-02T11:14:37'
                      endTime: '2026-04-02T11:43:06'
                      createUser: BOSS
                      updateUser: BOSS
                      createDate: null
                      updateDate: '2026-04-02T11:43:05'
                    - id: 500347009
                      taskName: API发送
                      templateName: TikTok 发布视频
                      mobileId: 1400913
                      mobileName: android12
                      mobileGroupName: null
                      mobileSerialNumber: 890552344
                      taskState: 4
                      triggerTime: '2026-04-02T10:44:00'
                      triggerNextTime: '2026-04-02T10:44:00'
                      powerOnTime: '2026-04-02T10:55:41'
                      delayReason: ''
                      handleResult: 0
                      handleFailCode: '1'
                      handleFailReason: '工作流执行失败: [DOWNLOAD_MEDIA/URL_REQUIRED] 下载 URL 不能为空'
                      handleResultAttachment: >-
                        https://hubstudio-site-pre.s3.cn-north-1.amazonaws.com.cn/pre/taskResultScreenshot/20260402/qts9u1fxc6-2039537221000564738.webp
                      handleTime: '2026-04-02T10:54:37'
                      endTime: '2026-04-02T10:56:38'
                      createUser: BOSS
                      updateUser: BOSS
                      createDate: null
                      updateDate: '2026-04-02T10:56:37'
                    - id: 500347007
                      taskName: API发送
                      templateName: TikTok 发布视频
                      mobileId: 1400913
                      mobileName: android12
                      mobileGroupName: null
                      mobileSerialNumber: 890552344
                      taskState: 5
                      triggerTime: '2026-04-02T10:44:00'
                      triggerNextTime: '2026-04-02T10:44:00'
                      powerOnTime: null
                      delayReason: ''
                      handleResult: null
                      handleFailCode: null
                      handleFailReason: null
                      handleResultAttachment: null
                      handleTime: '2026-04-02T10:52:07'
                      endTime: null
                      createUser: BOSS
                      updateUser: BOSS
                      createDate: null
                      updateDate: '2026-04-02T10:52:20'
                    - id: 500347006
                      taskName: API发送
                      templateName: TikTok 发布视频
                      mobileId: 1400913
                      mobileName: android12
                      mobileGroupName: null
                      mobileSerialNumber: 890552344
                      taskState: 4
                      triggerTime: '2026-04-02T10:44:00'
                      triggerNextTime: '2026-04-02T10:44:00'
                      powerOnTime: null
                      delayReason: ''
                      handleResult: 0
                      handleFailCode: E114104
                      handleFailReason: BOSS(name)正在使用，请稍候再试！
                      handleResultAttachment: null
                      handleTime: '2026-04-02T10:51:22'
                      endTime: '2026-04-02T10:51:22'
                      createUser: BOSS
                      updateUser: BOSS
                      createDate: null
                      updateDate: '2026-04-02T10:51:22'
                    - id: 500346995
                      taskName: 市场发tk
                      templateName: TikTok 发布视频
                      mobileId: 1400913
                      mobileName: android12
                      mobileGroupName: null
                      mobileSerialNumber: 890552344
                      taskState: 4
                      triggerTime: '2026-04-01T20:03:00'
                      triggerNextTime: '2026-04-01T20:03:00'
                      powerOnTime: null
                      delayReason: ''
                      handleResult: 0
                      handleFailCode: E114105
                      handleFailReason: 当前设备已开机
                      handleResultAttachment: null
                      handleTime: '2026-04-01T21:15:37'
                      endTime: '2026-04-01T21:15:37'
                      createUser: BOSS
                      updateUser: BOSS
                      createDate: null
                      updateDate: '2026-04-01T21:15:37'
                    - id: 500346992
                      taskName: 市场发tk
                      templateName: TikTok 发布视频
                      mobileId: 1400913
                      mobileName: android12
                      mobileGroupName: null
                      mobileSerialNumber: 890552344
                      taskState: 4
                      triggerTime: '2026-04-01T20:03:00'
                      triggerNextTime: '2026-04-01T20:03:00'
                      powerOnTime: '2026-04-01T20:43:09'
                      delayReason: ''
                      handleResult: 0
                      handleFailCode: '1'
                      handleFailReason: '工作流执行失败: [DOWNLOAD_MEDIA/URL_REQUIRED] 下载 URL 不能为空'
                      handleResultAttachment: >-
                        https://hubstudio-site-pre.s3.cn-north-1.amazonaws.com.cn/pre/taskResultScreenshot/20260401/sq8a26orid-2039322676659167233.webp
                      handleTime: '2026-04-01T20:42:07'
                      endTime: '2026-04-01T20:44:11'
                      createUser: BOSS
                      updateUser: BOSS
                      createDate: null
                      updateDate: '2026-04-01T20:44:11'
                    - id: 500346991
                      taskName: 多次-未开始
                      templateName: 花 鸟
                      mobileId: 1400913
                      mobileName: android12
                      mobileGroupName: null
                      mobileSerialNumber: 890552344
                      taskState: 3
                      triggerTime: '2026-04-01T20:30:00'
                      triggerNextTime: '2026-04-01T20:30:00'
                      powerOnTime: '2026-04-01T20:31:52'
                      delayReason: ''
                      handleResult: 1
                      handleFailCode: '0'
                      handleFailReason: null
                      handleResultAttachment: >-
                        https://hubstudio-site-pre.s3.cn-north-1.amazonaws.com.cn/pre/taskResultScreenshot/20260401/vjw8i22maq-2039319837018890241.webp
                      handleTime: '2026-04-01T20:30:37'
                      endTime: '2026-04-01T20:32:21'
                      createUser: BOSS
                      updateUser: BOSS
                      createDate: null
                      updateDate: '2026-04-01T20:32:20'
                    - id: 500346989
                      taskName: 市场发tk
                      templateName: TikTok 发布视频
                      mobileId: 1400913
                      mobileName: android12
                      mobileGroupName: null
                      mobileSerialNumber: 890552344
                      taskState: 3
                      triggerTime: '2026-04-01T20:03:00'
                      triggerNextTime: '2026-04-01T20:03:00'
                      powerOnTime: '2026-04-01T20:23:45'
                      delayReason: ''
                      handleResult: 1
                      handleFailCode: '0'
                      handleFailReason: null
                      handleResultAttachment: >-
                        https://hubstudio-site-pre.s3.cn-north-1.amazonaws.com.cn/pre/taskResultScreenshot/20260401/8f293czixg-2039317792614584321.webp
                      handleTime: '2026-04-01T20:23:22'
                      endTime: '2026-04-01T20:28:23'
                      createUser: BOSS
                      updateUser: BOSS
                      createDate: null
                      updateDate: '2026-04-01T20:28:22'
                  total: 24
          headers: {}
          x-apifox-name: ''
      security: []
      x-apifox-folder: 云手机/RPA
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-437761340-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### RPA-取消记录 `POST /api/v1/cloud-mobile/rpa/subTask/cancel`

来源：https://api-docs.hubstudio.cn/437761341e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/rpa/subTask/cancel:
    post:
      summary: RPA-取消记录
      deprecated: false
      description: ''
      tags:
        - 云手机/RPA
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                id:
                  type: integer
                  title: 任务ID
              required:
                - id
              x-apifox-orders:
                - id
            example:
              id: 500347061
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  data:
                    type: 'null'
                    title: 响应数据
                  code:
                    type: string
                    title: 业务状态码
                  msg:
                    type: string
                    title: 业务消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: string
                    title: 当前时间戳
                required:
                  - data
                x-apifox-orders:
                  - data
                  - code
                  - msg
                  - requestId
                  - timestamp
              example:
                code: 0
                msg: Success
                requestId: 578e0a49-c016-4e0e-ac07-c602f37adbaa
                timestamp: 1775120308981
                data: null
          headers: {}
          x-apifox-name: ''
      security: []
      x-apifox-folder: 云手机/RPA
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-437761341-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### RPA-记录详情查询 `POST /api/v1/cloud-mobile/rpa/subTask/detail`

来源：https://api-docs.hubstudio.cn/437761342e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/rpa/subTask/detail:
    post:
      summary: RPA-记录详情查询
      deprecated: false
      description: ''
      tags:
        - 云手机/RPA
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                id:
                  type: string
                  title: 记录编号
              required:
                - id
              x-apifox-orders:
                - id
            example:
              id: '500000071'
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  data:
                    type: object
                    properties:
                      id:
                        type: integer
                        title: 记录ID
                      templateId:
                        type: integer
                        title: 模板ID
                      templateName:
                        type: string
                        title: 模板名称
                      templateType:
                        type: string
                        description: PERSONAL：个人；MARKET：市场
                        title: 模板类型
                      taskParameter:
                        type: string
                        title: 记录参数
                      mobileId:
                        type: integer
                        title: 云手机ID
                      mobileName:
                        type: string
                        title: 云手机名称
                      mobileGroupName:
                        type: 'null'
                        title: 云手机分组名称
                      mainTaskId:
                        type: integer
                        title: 计划ID
                      mainTaskName:
                        type: string
                        title: 计划名称
                      taskState:
                        type: integer
                        title: 记录状态
                        description: 0：等待执行；1：正在执行；2：排队中；3：成功；4：失败；5：已取消
                      triggerTime:
                        type: string
                        title: 计划调度时
                      triggerNextTime:
                        type: string
                        title: 实际调度时间
                      powerOnTime:
                        type: 'null'
                        title: 云手机开机时间
                      delayReason:
                        type: string
                        title: 延期原因
                      handleResult:
                        type: 'null'
                        title: 记录执行结果
                        description: 0：执行失败；1：执行成功
                      handleFailCode:
                        type: 'null'
                        title: 记录执行失败编码
                      handleFailReason:
                        type: 'null'
                        title: 记录执行失败原因
                      handleResultAttachment:
                        type: 'null'
                        title: 记录执行结果附件
                      handleTime:
                        type: string
                        title: 记录开始时间
                      endTime:
                        type: 'null'
                        title: 记录结束时间
                      createUser:
                        type: string
                        title: 创建用户
                      updateUser:
                        type: string
                        title: 修改用户
                      createDate:
                        type: 'null'
                        title: 创建时间
                      updateDate:
                        type: 'null'
                        title: 修改时间
                    title: 响应数据
                    x-apifox-orders:
                      - id
                      - templateId
                      - templateName
                      - templateType
                      - taskParameter
                      - mobileId
                      - mobileName
                      - mobileGroupName
                      - mainTaskId
                      - mainTaskName
                      - taskState
                      - triggerTime
                      - triggerNextTime
                      - powerOnTime
                      - delayReason
                      - handleResult
                      - handleFailCode
                      - handleFailReason
                      - handleResultAttachment
                      - handleTime
                      - endTime
                      - createUser
                      - updateUser
                      - createDate
                      - updateDate
                x-apifox-orders:
                  - data
              example:
                code: 0
                msg: Success
                requestId: 77d09b9b-a932-4abc-8bc0-9743147eddee
                timestamp: 1775121775212
                data:
                  id: 500000071
                  templateId: 12
                  templateName: 测试
                  templateType: PERSONAL
                  taskParameter: >-
                    {"__Extra__":{"__SerialNumber__":890552363},"test_package":"com.qq.reader"}
                  mobileId: 1400915
                  mobileName: android14
                  mobileGroupName: null
                  mainTaskId: 300000040
                  mainTaskName: 运行中模板
                  taskState: 5
                  triggerTime: '2026-03-31T20:30:00'
                  triggerNextTime: '2026-03-31T20:30:00'
                  powerOnTime: null
                  delayReason: ''
                  handleResult: null
                  handleFailCode: null
                  handleFailReason: null
                  handleResultAttachment: null
                  handleTime: '2026-03-31T20:31:52'
                  endTime: null
                  createUser: BOSS
                  updateUser: BOSS
                  createDate: null
                  updateDate: null
          headers: {}
          x-apifox-name: ''
      security: []
      x-apifox-folder: 云手机/RPA
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-437761342-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### RPA-快速保存一次性计划 `POST /api/v1/cloud-mobile/rpa/onceTask/save`

来源：https://api-docs.hubstudio.cn/437761343e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/rpa/onceTask/save:
    post:
      summary: RPA-快速保存一次性计划
      deprecated: false
      description: ''
      tags:
        - 云手机/RPA
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          required: false
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                mobileId:
                  type: integer
                  title: 云手机ID
                description:
                  type: string
                  title: 计划描述
                scheduleName:
                  type: string
                  title: 计划名称
                templateId:
                  description: 个人模板或市场模板ID
                  type: integer
                  title: 模板ID
                templateParameter:
                  description: JSON格式
                  type: string
                  title: 模板参数
              required:
                - mobileId
                - scheduleName
                - templateId
              x-apifox-orders:
                - mobileId
                - description
                - scheduleName
                - templateId
                - templateParameter
            example:
              mobileId: 1400914
              description: 计划描述
              scheduleName: 计划名称
              templateId: 50
              templateParameter: >-
                {  "__Extra__": {    "vidoe": {      "name":
                "e2e6da7042bc4222a4a3d3a00f41c1ff.mp4",      "size": 597851   
                }  },  "vidoe":
                "https://xxxxx.com/e2e6da7042bc4222a4a3d3a00f41c1ff.mp4"}
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  data:
                    type: integer
                    title: 响应数据
                    description: 计划ID
                  code:
                    type: string
                    title: 业务状态码
                  msg:
                    type: string
                    title: 业务消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: string
                    title: 当前时间戳
                required:
                  - data
                x-apifox-orders:
                  - data
                  - code
                  - msg
                  - requestId
                  - timestamp
              example:
                code: 0
                msg: Success
                requestId: ff6bc99f-acd4-455a-9516-a7ef529f68c3
                timestamp: 1775122199872
                data: 300380632
          headers: {}
          x-apifox-name: ''
      security: []
      x-apifox-folder: 云手机/RPA
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-437761343-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```


### 云手机

#### 云手机商品列表 `POST /api/v1/cloud-mobile/mobile-product-list`

获取云手机商品列表

来源：https://api-docs.hubstudio.cn/380052338e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/mobile-product-list:
    post:
      summary: 云手机商品列表
      deprecated: false
      description: 获取云手机商品列表
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties: {}
              x-apifox-orders: []
            examples: {}
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                    nullable: true
                  data:
                    type: array
                    items:
                      type: object
                      properties:
                        billingType:
                          type: integer
                          description: 1-按需付费
                          title: 扣费方式
                          nullable: true
                        id:
                          type: integer
                          title: 产品ID
                          nullable: true
                        origRunPrice:
                          type: number
                          title: 锚定运行费用
                          nullable: true
                        origStoragePrice:
                          type: number
                          title: 锚定存储费用
                          nullable: true
                        productName:
                          type: string
                          title: 产品名称
                          nullable: true
                        runPrice:
                          type: number
                          title: 运行费用
                          nullable: true
                        showCpu:
                          type: string
                          title: CPU
                          nullable: true
                        showMemory:
                          type: string
                          title: 内存
                          nullable: true
                        showRegionId:
                          type: integer
                          title: 区域ID
                          nullable: true
                        showRegionName:
                          type: string
                          title: 区域
                          nullable: true
                        showStorage:
                          type: string
                          title: 存储
                          nullable: true
                        showSystem:
                          type: string
                          title: 系统
                          nullable: true
                        storagePrice:
                          type: number
                          title: 存储费用
                          nullable: true
                      x-apifox-orders:
                        - billingType
                        - id
                        - origRunPrice
                        - origStoragePrice
                        - productName
                        - runPrice
                        - showCpu
                        - showMemory
                        - showRegionId
                        - showRegionName
                        - showStorage
                        - showSystem
                        - storagePrice
                      nullable: true
                    title: 业务数据
                    nullable: true
                  msg:
                    type: string
                    title: 结果信息
                    nullable: true
                  requestId:
                    type: string
                    title: 请求ID
                    nullable: true
                  timestamp:
                    type: integer
                    title: 时间戳
                    nullable: true
                x-apifox-orders:
                  - code
                  - data
                  - msg
                  - requestId
                  - timestamp
                nullable: true
              example: "{\n\t\"code\": 0, //结果码。0表示成功，其余为失败\n\t\"data\": [ //业务数据\n\t\t{\n\t\t\t\"billingType\": 1, //扣费方式：1-按需付费\n\t\t\t\"id\": 20, //产品ID\n\t\t\t\"origRunPrice\": 0.18, //锚定运行费用\n\t\t\t\"origStoragePrice\": 0.38, //锚定存储费用\n\t\t\t\"productName\": \"基础版\", //产品名称\n\t\t\t\"runPrice\": 0.18, //运行费用\n\t\t\t\"showCpu\": \"2\", //CPU\n\t\t\t\"showMemory\": \"4\", //内存\n\t\t\t\"showRegionId\": 18, //区域ID\n\t\t\t\"showRegionName\": \"香港\", //区域\n\t\t\t\"showStorage\": \"64\", //存储\n\t\t\t\"showSystem\": \"Android 11.0\", //系统\n\t\t\t\"storagePrice\": 0.38 //存储费用\n\t\t}\n\t],\n\t\"msg\": \"\" //结果信息\n}"
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052338-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 云手机分页列表 `POST /api/v1/cloud-mobile/mobile-page`

获取云手机分页列表

来源：https://api-docs.hubstudio.cn/380052339e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/mobile-page:
    post:
      summary: 云手机分页列表
      deprecated: false
      description: 获取云手机分页列表
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                adbStatus:
                  type: integer
                  description: 1-开启 0-关闭
                  title: ADB状态
                current:
                  type: integer
                  description: 默认为1
                  title: 当前页数
                mobileIds:
                  type: array
                  items:
                    type: string
                  title: 手机IDS
                name:
                  type: string
                  title: 云手机名称
                size:
                  type: integer
                  description: 默认10，最多200条
                  title: 每页显示记录数
                state:
                  type: boolean
                  description: true-开机，false-关机
                  title: 云手机状态
              x-apifox-orders:
                - adbStatus
                - current
                - mobileIds
                - name
                - size
                - state
            example:
              current: 1
              size: 10
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: string
                    description: 0表示成功，其余表示失败
                    title: 业务状态码
                    nullable: true
                  data:
                    type: object
                    properties:
                      list:
                        type: array
                        items:
                          type: object
                          properties:
                            adbStatus:
                              description: ADB状态。0-关闭，1-开启
                              type: integer
                            billingStatus:
                              description: 计费状态。1-正常，2-即将过期，3-已过期
                              type: integer
                            billingType:
                              description: 计费方式：1-按需付费，2-包月
                              type: integer
                            createTime:
                              description: 创建时间
                              type: string
                            expiredTime:
                              description: 过期时间
                              type: string
                            lastPowerOnTime:
                              description: 最后开机时间
                              type: string
                            mobileId:
                              description: 主键
                              type: integer
                            name:
                              description: 手机名称
                              type: string
                            number:
                              description: 序号
                              type: integer
                            proxyTypeId:
                              description: 代理类型id
                              type: integer
                            remark:
                              description: 备注
                              type: string
                            serialNumber:
                              description: 编号
                              type: integer
                            status:
                              description: >-
                                状态。-2-创建失败，-1-初始化状态，0-关机，1-开机使用中，2-设备占用中，3-设备开机未有人使用，4-开机中，5-恢复出厂中，6-重启中
                              type: integer
                            tagId:
                              description: 分组id
                              type: integer
                            tagName:
                              description: 分组
                              type: string
                          x-apifox-orders:
                            - adbStatus
                            - billingStatus
                            - billingType
                            - createTime
                            - expiredTime
                            - lastPowerOnTime
                            - mobileId
                            - name
                            - number
                            - proxyTypeId
                            - remark
                            - serialNumber
                            - status
                            - tagId
                            - tagName
                        description: 返回数组
                        nullable: true
                      total:
                        type: integer
                        description: 返回数组总数
                        nullable: true
                    x-apifox-orders:
                      - list
                      - total
                    title: 业务数据
                    nullable: true
                  msg:
                    type: string
                    title: 响应消息
                    nullable: true
                x-apifox-orders:
                  - code
                  - data
                  - msg
              example:
                code: 0
                data:
                  list:
                    - adbStatus: 0
                      billingStatus: 0
                      billingType: 0
                      createTime: ''
                      expiredTime: ''
                      lastPowerOnTime: ''
                      mobileId: 0
                      name: ''
                      number: 0
                      proxyTypeId: 0
                      remark: ''
                      serialNumber: 0
                      status: 0
                      tagId: 0
                      tagName: ''
                  total: 0
                msg: ''
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052339-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 添加云手机 `POST /api/v1/cloud-mobile/add-mobile`

可创建新的按需云手机（暂不支持创建按月使用的云手机）

来源：https://api-docs.hubstudio.cn/380052340e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/add-mobile:
    post:
      summary: 添加云手机
      deprecated: false
      description: 可创建新的按需云手机（暂不支持创建按月使用的云手机）
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                count:
                  type: integer
                  description: 默认为1
                  title: 创建按需云手机的数量
                productId:
                  type: integer
                  title: 云手机商品ID
                  description: 通过/api/v1/cloud-mobile/mobile-product-list获取
              x-apifox-orders:
                - count
                - productId
              required:
                - productId
            example:
              count: 1
              productId: 1856
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    description: 0表示成功，其余为失败
                    type: string
                    title: 业务状态码
                  data:
                    type: array
                    items:
                      type: string
                    title: 业务数据
                  msg:
                    type: string
                    title: 响应消息
                  requestId:
                    type: string
                    title: 请求ID
                    nullable: true
                  timestamp:
                    type: integer
                    title: 时间戳
                    nullable: true
                x-apifox-orders:
                  - code
                  - data
                  - msg
                  - requestId
                  - timestamp
              example:
                code: 0
                msg: Success
                requestId: 49ca584c8feab922dc734fb26962a2eb
                timestamp: 1735889426558
                data:
                  - 24135
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052340-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 国家时区语言列表 `POST /api/v1/cloud-mobile/get-country-time-zone-language-list`

获取云手机的国家时区语言列表

来源：https://api-docs.hubstudio.cn/380052341e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/get-country-time-zone-language-list:
    post:
      summary: 国家时区语言列表
      deprecated: false
      description: 获取云手机的国家时区语言列表
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties: {}
              x-apifox-orders: []
            examples: {}
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    description: 0表示成功，其余为失败
                    title: 业务状态码
                  data:
                    type: object
                    properties:
                      countryList:
                        type: array
                        items:
                          type: object
                          properties:
                            country:
                              description: 填代理时用的值
                              type: string
                              title: 国家key
                            id:
                              type: integer
                              title: id
                            showCountry:
                              type: string
                              title: 国家显示值
                            timeZoneVoList:
                              type: array
                              items:
                                type: object
                                properties:
                                  showTimeZone:
                                    type: string
                                    title: 时区显示值
                                  timeZone:
                                    description: 填代理时用的值
                                    type: string
                                    title: 时区key
                                x-apifox-orders:
                                  - showTimeZone
                                  - timeZone
                              title: 国家关联的时区
                          x-apifox-orders:
                            - country
                            - id
                            - showCountry
                            - timeZoneVoList
                        title: 国家列表
                      languageList:
                        type: array
                        items:
                          type: object
                          properties:
                            language:
                              description: 填代理时用的值
                              type: string
                              title: 语言key
                            showLanguage:
                              type: string
                              title: 语言显示值
                          x-apifox-orders:
                            - language
                            - showLanguage
                        title: 语言列表
                    x-apifox-orders:
                      - countryList
                      - languageList
                    title: 业务数据载体
                  msg:
                    type: string
                    title: 响应消息
                x-apifox-orders:
                  - code
                  - data
                  - msg
              example:
                requestId: db265a8f87a440ed9785d1ebfb74cdb8
                msg: Success
                code: 0
                data:
                  countryList:
                    - country: al
                      id: 302
                      showCountry: 阿尔巴尼亚
                      timeZoneVoList:
                        - showTimeZone: Europe/Tirane(GMT+02:00)
                          timeZone: Europe/Tirane
                    - country: dz
                      id: 339
                      showCountry: 阿尔及利亚
                      timeZoneVoList:
                        - showTimeZone: Africa/Algiers(GMT+01:00)
                          timeZone: Africa/Algiers
                    - country: af
                      id: 300
                      showCountry: 阿富汗
                      timeZoneVoList:
                        - showTimeZone: Asia/Kabul(GMT+04:30)
                          timeZone: Asia/Kabul
                    - country: ar
                      id: 55
                      showCountry: 阿根廷
                      timeZoneVoList:
                        - showTimeZone: America/Argentina/Catamarca(GMT-03:00)
                          timeZone: America/Argentina/Catamarca
                        - showTimeZone: America/Argentina/Cordoba(GMT-03:00)
                          timeZone: America/Argentina/Cordoba
                        - showTimeZone: America/Argentina/Jujuy(GMT-03:00)
                          timeZone: America/Argentina/Jujuy
                        - showTimeZone: America/Argentina/Salta(GMT-03:00)
                          timeZone: America/Argentina/Salta
                        - showTimeZone: America/Argentina/La_Rioja(GMT-03:00)
                          timeZone: America/Argentina/La_Rioja
                        - showTimeZone: America/Argentina/Mendoza(GMT-03:00)
                          timeZone: America/Argentina/Mendoza
                        - showTimeZone: America/Argentina/San_Juan(GMT-03:00)
                          timeZone: America/Argentina/San_Juan
                        - showTimeZone: America/Argentina/Rio_Gallegos(GMT-03:00)
                          timeZone: America/Argentina/Rio_Gallegos
                        - showTimeZone: America/Argentina/Ushuaia(GMT-03:00)
                          timeZone: America/Argentina/Ushuaia
                        - showTimeZone: America/Argentina/Tucuman(GMT-03:00)
                          timeZone: America/Argentina/Tucuman
                        - showTimeZone: America/Argentina/San_Luis(GMT-03:00)
                          timeZone: America/Argentina/San_Luis
                        - showTimeZone: America/Argentina/Buenos_Aires(GMT-03:00)
                          timeZone: America/Argentina/Buenos_Aires
                    - country: ae
                      id: 60
                      showCountry: 阿联酋
                      timeZoneVoList:
                        - showTimeZone: Asia/Dubai(GMT+04:00)
                          timeZone: Asia/Dubai
                    - country: aw
                      id: 306
                      showCountry: 阿鲁巴
                      timeZoneVoList:
                        - showTimeZone: America/Aruba(GMT-04:00)
                          timeZone: America/Aruba
                    - country: om
                      id: 413
                      showCountry: 阿曼
                      timeZoneVoList:
                        - showTimeZone: Asia/Muscat(GMT+04:00)
                          timeZone: Asia/Muscat
                    - country: az
                      id: 308
                      showCountry: 阿塞拜疆
                      timeZoneVoList:
                        - showTimeZone: Asia/Baku(GMT+04:00)
                          timeZone: Asia/Baku
                    - country: eg
                      id: 29
                      showCountry: 埃及
                      timeZoneVoList:
                        - showTimeZone: Africa/Cairo(GMT+02:00)
                          timeZone: Africa/Cairo
                    - country: et
                      id: 343
                      showCountry: 埃塞俄比亚
                      timeZoneVoList:
                        - showTimeZone: Africa/Addis_Ababa(GMT+03:00)
                          timeZone: Africa/Addis_Ababa
                    - country: ie
                      id: 35
                      showCountry: 爱尔兰
                      timeZoneVoList:
                        - showTimeZone: Europe/Dublin(GMT+00:00)
                          timeZone: Europe/Dublin
                    - country: ee
                      id: 30
                      showCountry: 爱沙尼亚
                      timeZoneVoList:
                        - showTimeZone: Europe/Tallinn(GMT+02:00)
                          timeZone: Europe/Tallinn
                    - country: ad
                      id: 299
                      showCountry: 安道尔
                      timeZoneVoList:
                        - showTimeZone: Europe/Andorra(GMT+02:00)
                          timeZone: Europe/Andorra
                    - country: ao
                      id: 304
                      showCountry: 安哥拉
                      timeZoneVoList:
                        - showTimeZone: Africa/Luanda(GMT+01:00)
                          timeZone: Africa/Luanda
                    - country: ag
                      id: 301
                      showCountry: 安提瓜和巴布达
                      timeZoneVoList:
                        - showTimeZone: America/Antigua(GMT-04:00)
                          timeZone: America/Antigua
                    - country: at
                      id: 20
                      showCountry: 奥地利
                      timeZoneVoList:
                        - showTimeZone: Europe/Vienna(GMT+01:00)
                          timeZone: Europe/Vienna
                    - country: ax
                      id: 307
                      showCountry: 奥兰群岛
                      timeZoneVoList:
                        - showTimeZone: Europe/Mariehamn(GMT+03:00)
                          timeZone: Europe/Mariehamn
                    - country: au
                      id: 1
                      showCountry: 澳大利亚
                      timeZoneVoList:
                        - showTimeZone: Australia/Perth(GMT+08:00)
                          timeZone: Australia/Perth
                        - showTimeZone: Australia/Lindeman(GMT+10:00)
                          timeZone: Australia/Lindeman
                        - showTimeZone: Australia/Brisbane(GMT+10:00)
                          timeZone: Australia/Brisbane
                        - showTimeZone: Australia/Broken_Hill(GMT+10:30)
                          timeZone: Australia/Broken_Hill
                        - showTimeZone: Australia/Adelaide(GMT+10:30)
                          timeZone: Australia/Adelaide
                        - showTimeZone: Australia/Hobart(GMT+11:00)
                          timeZone: Australia/Hobart
                        - showTimeZone: Australia/Currie(GMT+11:00)
                          timeZone: Australia/Currie
                        - showTimeZone: Australia/Melbourne(GMT+11:00)
                          timeZone: Australia/Melbourne
                        - showTimeZone: Australia/Sydney(GMT+11:00)
                          timeZone: Australia/Sydney
                        - showTimeZone: Australia/Lord_Howe(GMT+11:00)
                          timeZone: Australia/Lord_Howe
                        - showTimeZone: Australia/Eucla(GMT+8:45)
                          timeZone: Australia/Eucla
                        - showTimeZone: Australia/Darwin(GMT+9:30)
                          timeZone: Australia/Darwin
                    - country: bb
                      id: 310
                      showCountry: 巴巴多斯
                      timeZoneVoList:
                        - showTimeZone: America/Barbados(GMT-04:00)
                          timeZone: America/Barbados
                    - country: pg
                      id: 416
                      showCountry: 巴布亚新几内亚
                      timeZoneVoList:
                        - showTimeZone: Pacific/Port_Moresby(GMT+10:00)
                          timeZone: Pacific/Port_Moresby
                        - showTimeZone: Pacific/Bougainville(GMT+11:00)
                          timeZone: Pacific/Bougainville
                    - country: bs
                      id: 320
                      showCountry: 巴哈马
                      timeZoneVoList:
                        - showTimeZone: America/Nassau(GMT-04:00)
                          timeZone: America/Nassau
                    - country: pk
                      id: 54
                      showCountry: 巴基斯坦
                      timeZoneVoList:
                        - showTimeZone: Asia/Karachi(GMT+05:00)
                          timeZone: Asia/Karachi
                    - country: py
                      id: 421
                      showCountry: 巴拉圭
                      timeZoneVoList:
                        - showTimeZone: America/Asuncion(GMT-03:00)
                          timeZone: America/Asuncion
                    - country: ps
                      id: 419
                      showCountry: 巴勒斯坦
                      timeZoneVoList:
                        - showTimeZone: Asia/Hebron(GMT+03:00)
                          timeZone: Asia/Hebron
                        - showTimeZone: Asia/Gaza(GMT+03:00)
                          timeZone: Asia/Gaza
                    - country: bh
                      id: 313
                      showCountry: 巴林
                      timeZoneVoList:
                        - showTimeZone: Asia/Bahrain(GMT+03:00)
                          timeZone: Asia/Bahrain
                    - country: pa
                      id: 414
                      showCountry: 巴拿马
                      timeZoneVoList:
                        - showTimeZone: America/Panama(GMT-05:00)
                          timeZone: America/Panama
                    - country: br
                      id: 22
                      showCountry: 巴西
                      timeZoneVoList:
                        - showTimeZone: America/Noronha(GMT-02:00)
                          timeZone: America/Noronha
                        - showTimeZone: America/Maceio(GMT-03:00)
                          timeZone: America/Maceio
                        - showTimeZone: America/Belem(GMT-03:00)
                          timeZone: America/Belem
                        - showTimeZone: America/Fortaleza(GMT-03:00)
                          timeZone: America/Fortaleza
                        - showTimeZone: America/Araguaina(GMT-03:00)
                          timeZone: America/Araguaina
                        - showTimeZone: America/Santarem(GMT-03:00)
                          timeZone: America/Santarem
                        - showTimeZone: America/Recife(GMT-03:00)
                          timeZone: America/Recife
                        - showTimeZone: America/Sao_Paulo(GMT-03:00)
                          timeZone: America/Sao_Paulo
                        - showTimeZone: America/Bahia(GMT-03:00)
                          timeZone: America/Bahia
                        - showTimeZone: America/Campo_Grande(GMT-04:00)
                          timeZone: America/Campo_Grande
                        - showTimeZone: America/Porto_Velho(GMT-04:00)
                          timeZone: America/Porto_Velho
                        - showTimeZone: America/Boa_Vista(GMT-04:00)
                          timeZone: America/Boa_Vista
                        - showTimeZone: America/Manaus(GMT-04:00)
                          timeZone: America/Manaus
                        - showTimeZone: America/Cuiaba(GMT-04:00)
                          timeZone: America/Cuiaba
                        - showTimeZone: America/Eirunepe(GMT-05:00)
                          timeZone: America/Eirunepe
                        - showTimeZone: America/Rio_Branco(GMT-05:00)
                          timeZone: America/Rio_Branco
                    - country: by
                      id: 61
                      showCountry: 白俄罗斯
                      timeZoneVoList:
                        - showTimeZone: Europe/Minsk(GMT+03:00)
                          timeZone: Europe/Minsk
                    - country: bm
                      id: 316
                      showCountry: 百慕大
                      timeZoneVoList:
                        - showTimeZone: Atlantic/Bermuda(GMT-03:00)
                          timeZone: Atlantic/Bermuda
                    - country: bg
                      id: 23
                      showCountry: 保加利亚
                      timeZoneVoList:
                        - showTimeZone: Europe/Sofia(GMT+02:00)
                          timeZone: Europe/Sofia
                    - country: mp
                      id: 398
                      showCountry: 北马里亚纳群岛
                      timeZoneVoList:
                        - showTimeZone: Pacific/Saipan(GMT+10:00)
                          timeZone: Pacific/Saipan
                    - country: bj
                      id: 315
                      showCountry: 贝宁
                      timeZoneVoList:
                        - showTimeZone: Africa/Porto-Novo(GMT+01:00)
                          timeZone: Africa/Porto-Novo
                    - country: be
                      id: 21
                      showCountry: 比利时
                      timeZoneVoList:
                        - showTimeZone: Europe/Brussels(GMT+01:00)
                          timeZone: Europe/Brussels
                    - country: is
                      id: 34
                      showCountry: 冰岛
                      timeZoneVoList:
                        - showTimeZone: Atlantic/Reykjavik(GMT+00:00)
                          timeZone: Atlantic/Reykjavik
                    - country: pr
                      id: 418
                      showCountry: 波多黎各
                      timeZoneVoList:
                        - showTimeZone: America/Puerto_Rico(GMT-04:00)
                          timeZone: America/Puerto_Rico
                    - country: ba
                      id: 309
                      showCountry: 波黑
                      timeZoneVoList: []
                    - country: pl
                      id: 43
                      showCountry: 波兰
                      timeZoneVoList:
                        - showTimeZone: Europe/Warsaw(GMT+01:00)
                          timeZone: Europe/Warsaw
                    - country: bo
                      id: 318
                      showCountry: 玻利维亚
                      timeZoneVoList:
                        - showTimeZone: America/La_Paz(GMT-04:00)
                          timeZone: America/La_Paz
                    - country: bz
                      id: 324
                      showCountry: 伯利兹
                      timeZoneVoList:
                        - showTimeZone: America/Belize(GMT-06:00)
                          timeZone: America/Belize
                    - country: bw
                      id: 323
                      showCountry: 博茨瓦纳
                      timeZoneVoList:
                        - showTimeZone: Africa/Gaborone(GMT+02:00)
                          timeZone: Africa/Gaborone
                    - country: bt
                      id: 321
                      showCountry: 不丹
                      timeZoneVoList:
                        - showTimeZone: Asia/Thimphu(GMT+06:00)
                          timeZone: Asia/Thimphu
                    - country: bf
                      id: 312
                      showCountry: 布基纳法索
                      timeZoneVoList:
                        - showTimeZone: Africa/Ouagadougou(GMTZ)
                          timeZone: Africa/Ouagadougou
                    - country: bi
                      id: 314
                      showCountry: 布隆迪
                      timeZoneVoList:
                        - showTimeZone: Africa/Bujumbura(GMT+02:00)
                          timeZone: Africa/Bujumbura
                    - country: bv
                      id: 322
                      showCountry: 布韦岛
                      timeZoneVoList: []
                    - country: kp
                      id: 375
                      showCountry: 朝鲜
                      timeZoneVoList:
                        - showTimeZone: Asia/Pyongyang(GMT+09:00)
                          timeZone: Asia/Pyongyang
                    - country: gq
                      id: 357
                      showCountry: 赤道几内亚
                      timeZoneVoList:
                        - showTimeZone: Africa/Malabo(GMT+01:00)
                          timeZone: Africa/Malabo
                    - country: dk
                      id: 28
                      showCountry: 丹麦
                      timeZoneVoList:
                        - showTimeZone: Europe/Copenhagen(GMT+01:00)
                          timeZone: Europe/Copenhagen
                    - country: de
                      id: 5
                      showCountry: 德国
                      timeZoneVoList:
                        - showTimeZone: Europe/Berlin(GMT+01:00)
                          timeZone: Europe/Berlin
                    - country: tl
                      id: 445
                      showCountry: 东帝汶
                      timeZoneVoList:
                        - showTimeZone: Asia/Dili(GMT+09:00)
                          timeZone: Asia/Dili
                    - country: tg
                      id: 442
                      showCountry: 多哥
                      timeZoneVoList:
                        - showTimeZone: Africa/Lome(GMTZ)
                          timeZone: Africa/Lome
                    - country: do
                      id: 338
                      showCountry: 多米尼加
                      timeZoneVoList:
                        - showTimeZone: America/Santo_Domingo(GMT-04:00)
                          timeZone: America/Santo_Domingo
                    - country: dm
                      id: 337
                      showCountry: 多米尼克
                      timeZoneVoList:
                        - showTimeZone: America/Dominica(GMT-04:00)
                          timeZone: America/Dominica
                    - country: ru
                      id: 10
                      showCountry: 俄罗斯
                      timeZoneVoList:
                        - showTimeZone: Europe/Kaliningrad(GMT+02:00)
                          timeZone: Europe/Kaliningrad
                        - showTimeZone: Europe/Volgograd(GMT+03:00)
                          timeZone: Europe/Volgograd
                        - showTimeZone: Europe/Moscow(GMT+03:00)
                          timeZone: Europe/Moscow
                        - showTimeZone: Europe/Kirov(GMT+03:00)
                          timeZone: Europe/Kirov
                        - showTimeZone: Europe/Saratov(GMT+04:00)
                          timeZone: Europe/Saratov
                        - showTimeZone: Europe/Samara(GMT+04:00)
                          timeZone: Europe/Samara
                        - showTimeZone: Asia/Yekaterinburg(GMT+05:00)
                          timeZone: Asia/Yekaterinburg
                        - showTimeZone: Asia/Omsk(GMT+06:00)
                          timeZone: Asia/Omsk
                        - showTimeZone: Asia/Novosibirsk(GMT+07:00)
                          timeZone: Asia/Novosibirsk
                        - showTimeZone: Asia/Krasnoyarsk(GMT+07:00)
                          timeZone: Asia/Krasnoyarsk
                        - showTimeZone: Asia/Barnaul(GMT+07:00)
                          timeZone: Asia/Barnaul
                        - showTimeZone: Asia/Irkutsk(GMT+08:00)
                          timeZone: Asia/Irkutsk
                        - showTimeZone: Asia/Chita(GMT+09:00)
                          timeZone: Asia/Chita
                        - showTimeZone: Asia/Vladivostok(GMT+10:00)
                          timeZone: Asia/Vladivostok
                        - showTimeZone: Asia/Srednekolymsk(GMT+11:00)
                          timeZone: Asia/Srednekolymsk
                        - showTimeZone: Asia/Sakhalin(GMT+11:00)
                          timeZone: Asia/Sakhalin
                        - showTimeZone: Asia/Magadan(GMT+11:00)
                          timeZone: Asia/Magadan
                        - showTimeZone: Asia/Kamchatka(GMT+12:00)
                          timeZone: Asia/Kamchatka
                        - showTimeZone: Asia/Anadyr(GMT+12:00)
                          timeZone: Asia/Anadyr
                    - country: ec
                      id: 340
                      showCountry: 厄瓜多尔
                      timeZoneVoList:
                        - showTimeZone: America/Guayaquil(GMT-05:00)
                          timeZone: America/Guayaquil
                        - showTimeZone: Pacific/Galapagos(GMT-06:00)
                          timeZone: Pacific/Galapagos
                    - country: er
                      id: 342
                      showCountry: 厄立特里亚
                      timeZoneVoList:
                        - showTimeZone: Africa/Asmara(GMT+03:00)
                          timeZone: Africa/Asmara
                    - country: fr
                      id: 4
                      showCountry: 法国
                      timeZoneVoList:
                        - showTimeZone: Europe/Paris(GMT+01:00)
                          timeZone: Europe/Paris
                    - country: fo
                      id: 347
                      showCountry: 法罗群岛
                      timeZoneVoList:
                        - showTimeZone: Atlantic/Faroe(GMT+01:00)
                          timeZone: Atlantic/Faroe
                    - country: pf
                      id: 415
                      showCountry: 法属波利尼西亚
                      timeZoneVoList:
                        - showTimeZone: Pacific/Gambier(GMT-09:00)
                          timeZone: Pacific/Gambier
                        - showTimeZone: Pacific/Marquesas(GMT-09:30)
                          timeZone: Pacific/Marquesas
                        - showTimeZone: Pacific/Tahiti(GMT-10:00)
                          timeZone: Pacific/Tahiti
                    - country: gf
                      id: 351
                      showCountry: 法属圭亚那
                      timeZoneVoList:
                        - showTimeZone: America/Cayenne(GMT-03:00)
                          timeZone: America/Cayenne
                    - country: va
                      id: 457
                      showCountry: 梵蒂冈
                      timeZoneVoList:
                        - showTimeZone: Europe/Vatican(GMT+02:00)
                          timeZone: Europe/Vatican
                    - country: ph
                      id: 9
                      showCountry: 菲律宾
                      timeZoneVoList:
                        - showTimeZone: Asia/Manila(GMT+08:00)
                          timeZone: Asia/Manila
                    - country: fj
                      id: 344
                      showCountry: 斐济群岛
                      timeZoneVoList:
                        - showTimeZone: Pacific/Fiji(GMT+12:00)
                          timeZone: Pacific/Fiji
                    - country: fi
                      id: 31
                      showCountry: 芬兰
                      timeZoneVoList:
                        - showTimeZone: Europe/Helsinki(GMT+02:00)
                          timeZone: Europe/Helsinki
                    - country: cv
                      id: 334
                      showCountry: 佛得角
                      timeZoneVoList:
                        - showTimeZone: Atlantic/Cape_Verde(GMT-01:00)
                          timeZone: Atlantic/Cape_Verde
                    - country: cg
                      id: 328
                      showCountry: 刚果（布）
                      timeZoneVoList:
                        - showTimeZone: Africa/Brazzaville(GMT+01:00)
                          timeZone: Africa/Brazzaville
                    - country: cd
                      id: 326
                      showCountry: 刚果（金）
                      timeZoneVoList:
                        - showTimeZone: Africa/Kinshasa(GMT+01:00)
                          timeZone: Africa/Kinshasa
                        - showTimeZone: Africa/Lubumbashi(GMT+02:00)
                          timeZone: Africa/Lubumbashi
                    - country: co
                      id: 53
                      showCountry: 哥伦比亚
                      timeZoneVoList:
                        - showTimeZone: America/Bogota(GMT-05:00)
                          timeZone: America/Bogota
                    - country: cr
                      id: 332
                      showCountry: 哥斯达黎加
                      timeZoneVoList:
                        - showTimeZone: America/Costa_Rica(GMT-06:00)
                          timeZone: America/Costa_Rica
                    - country: gd
                      id: 349
                      showCountry: 格林纳达
                      timeZoneVoList:
                        - showTimeZone: America/Grenada(GMT-04:00)
                          timeZone: America/Grenada
                    - country: gl
                      id: 354
                      showCountry: 格陵兰
                      timeZoneVoList:
                        - showTimeZone: America/Nuuk(GMT-02:00)
                          timeZone: America/Nuuk
                        - showTimeZone: America/Godthab(GMT-02:00)
                          timeZone: America/Godthab
                        - showTimeZone: America/Thule(GMT-03:00)
                          timeZone: America/Thule
                        - showTimeZone: America/Danmarkshavn(GMTZ)
                          timeZone: America/Danmarkshavn
                    - country: ge
                      id: 350
                      showCountry: 格鲁吉亚
                      timeZoneVoList:
                        - showTimeZone: Asia/Tbilisi(GMT+04:00)
                          timeZone: Asia/Tbilisi
                    - country: gg
                      id: 352
                      showCountry: 根西岛
                      timeZoneVoList:
                        - showTimeZone: Europe/Guernsey(GMT+01:00)
                          timeZone: Europe/Guernsey
                    - country: cu
                      id: 333
                      showCountry: 古巴
                      timeZoneVoList:
                        - showTimeZone: America/Havana(GMT-04:00)
                          timeZone: America/Havana
                    - country: gp
                      id: 356
                      showCountry: 瓜德罗普
                      timeZoneVoList:
                        - showTimeZone: America/Guadeloupe(GMT-04:00)
                          timeZone: America/Guadeloupe
                    - country: gu
                      id: 359
                      showCountry: 关岛
                      timeZoneVoList:
                        - showTimeZone: Pacific/Guam(GMT+10:00)
                          timeZone: Pacific/Guam
                    - country: gy
                      id: 361
                      showCountry: 圭亚那
                      timeZoneVoList:
                        - showTimeZone: America/Guyana(GMT-04:00)
                          timeZone: America/Guyana
                    - country: kz
                      id: 59
                      showCountry: 哈萨克斯坦
                      timeZoneVoList:
                        - showTimeZone: Asia/Aqtobe(GMT+05:00)
                          timeZone: Asia/Aqtobe
                        - showTimeZone: Asia/Oral(GMT+05:00)
                          timeZone: Asia/Oral
                        - showTimeZone: Asia/Aqtau(GMT+05:00)
                          timeZone: Asia/Aqtau
                        - showTimeZone: Asia/Qyzylorda(GMT+05:00)
                          timeZone: Asia/Qyzylorda
                        - showTimeZone: Asia/Qostanay(GMT+06:00)
                          timeZone: Asia/Qostanay
                        - showTimeZone: Asia/Almaty(GMT+06:00)
                          timeZone: Asia/Almaty
                    - country: ht
                      id: 363
                      showCountry: 海地
                      timeZoneVoList:
                        - showTimeZone: America/Port-au-Prince(GMT-04:00)
                          timeZone: America/Port-au-Prince
                    - country: kr
                      id: 12
                      showCountry: 韩国
                      timeZoneVoList:
                        - showTimeZone: Asia/Seoul(GMT+09:00)
                          timeZone: Asia/Seoul
                    - country: nl
                      id: 41
                      showCountry: 荷兰
                      timeZoneVoList:
                        - showTimeZone: Europe/Amsterdam(GMT+01:00)
                          timeZone: Europe/Amsterdam
                    - country: bq
                      id: 319
                      showCountry: 荷兰加勒比区
                      timeZoneVoList:
                        - showTimeZone: America/Kralendijk(GMT-04:00)
                          timeZone: America/Kralendijk
                    - country: me
                      id: 390
                      showCountry: 黑山
                      timeZoneVoList:
                        - showTimeZone: Europe/Podgorica(GMT+02:00)
                          timeZone: Europe/Podgorica
                    - country: hn
                      id: 362
                      showCountry: 洪都拉斯
                      timeZoneVoList:
                        - showTimeZone: America/Tegucigalpa(GMT-06:00)
                          timeZone: America/Tegucigalpa
                    - country: ki
                      id: 373
                      showCountry: 基里巴斯
                      timeZoneVoList:
                        - showTimeZone: Pacific/Tarawa(GMT+12:00)
                          timeZone: Pacific/Tarawa
                        - showTimeZone: Pacific/Enderbury(GMT+13:00)
                          timeZone: Pacific/Enderbury
                        - showTimeZone: Pacific/Kiritimati(GMT+14:00)
                          timeZone: Pacific/Kiritimati
                    - country: dj
                      id: 336
                      showCountry: 吉布提
                      timeZoneVoList:
                        - showTimeZone: Africa/Djibouti(GMT+03:00)
                          timeZone: Africa/Djibouti
                    - country: kg
                      id: 372
                      showCountry: 吉尔吉斯斯坦
                      timeZoneVoList:
                        - showTimeZone: Asia/Bishkek(GMT+06:00)
                          timeZone: Asia/Bishkek
                    - country: gn
                      id: 355
                      showCountry: 几内亚
                      timeZoneVoList:
                        - showTimeZone: Africa/Conakry(GMTZ)
                          timeZone: Africa/Conakry
                    - country: gw
                      id: 360
                      showCountry: 几内亚比绍
                      timeZoneVoList:
                        - showTimeZone: Africa/Bissau(GMTZ)
                          timeZone: Africa/Bissau
                    - country: ca
                      id: 2
                      showCountry: 加拿大
                      timeZoneVoList:
                        - showTimeZone: America/Moncton(GMT-03:00)
                          timeZone: America/Moncton
                        - showTimeZone: America/Goose_Bay(GMT-03:00)
                          timeZone: America/Goose_Bay
                        - showTimeZone: America/Glace_Bay(GMT-03:00)
                          timeZone: America/Glace_Bay
                        - showTimeZone: America/St_Johns(GMT-03:30)
                          timeZone: America/St_Johns
                        - showTimeZone: America/Iqaluit(GMT-04:00)
                          timeZone: America/Iqaluit
                        - showTimeZone: America/Montreal(GMT-04:00)
                          timeZone: America/Montreal
                        - showTimeZone: America/Nipigon(GMT-04:00)
                          timeZone: America/Nipigon
                        - showTimeZone: America/Thunder_Bay(GMT-04:00)
                          timeZone: America/Thunder_Bay
                        - showTimeZone: America/Halifax(GMT-04:00)
                          timeZone: America/Halifax
                        - showTimeZone: America/Blanc-Sablon(GMT-04:00)
                          timeZone: America/Blanc-Sablon
                        - showTimeZone: America/Rankin_Inlet(GMT-05:00)
                          timeZone: America/Rankin_Inlet
                        - showTimeZone: America/Rainy_River(GMT-05:00)
                          timeZone: America/Rainy_River
                        - showTimeZone: America/Atikokan(GMT-05:00)
                          timeZone: America/Atikokan
                        - showTimeZone: America/Toronto(GMT-05:00)
                          timeZone: America/Toronto
                        - showTimeZone: America/Yellowknife(GMT-06:00)
                          timeZone: America/Yellowknife
                        - showTimeZone: America/Inuvik(GMT-06:00)
                          timeZone: America/Inuvik
                        - showTimeZone: America/Swift_Current(GMT-06:00)
                          timeZone: America/Swift_Current
                        - showTimeZone: America/Regina(GMT-06:00)
                          timeZone: America/Regina
                        - showTimeZone: America/Winnipeg(GMT-06:00)
                          timeZone: America/Winnipeg
                        - showTimeZone: America/Creston(GMT-07:00)
                          timeZone: America/Creston
                        - showTimeZone: America/Fort_Nelson(GMT-07:00)
                          timeZone: America/Fort_Nelson
                        - showTimeZone: America/Whitehorse(GMT-07:00)
                          timeZone: America/Whitehorse
                        - showTimeZone: America/Dawson_Creek(GMT-07:00)
                          timeZone: America/Dawson_Creek
                        - showTimeZone: America/Edmonton(GMT-07:00)
                          timeZone: America/Edmonton
                        - showTimeZone: America/Vancouver(GMT-08:00)
                          timeZone: America/Vancouver
                    - country: gh
                      id: 58
                      showCountry: 加纳
                      timeZoneVoList:
                        - showTimeZone: Africa/Accra(GMT+00:00)
                          timeZone: Africa/Accra
                    - country: ga
                      id: 348
                      showCountry: 加蓬
                      timeZoneVoList:
                        - showTimeZone: Africa/Libreville(GMT+01:00)
                          timeZone: Africa/Libreville
                    - country: kh
                      id: 19
                      showCountry: 柬埔寨
                      timeZoneVoList:
                        - showTimeZone: Asia/Phnom_Penh(GMT+07:00)
                          timeZone: Asia/Phnom_Penh
                    - country: cz
                      id: 27
                      showCountry: 捷克
                      timeZoneVoList:
                        - showTimeZone: Europe/Prague(GMT+01:00)
                          timeZone: Europe/Prague
                    - country: zw
                      id: 468
                      showCountry: 津巴布韦
                      timeZoneVoList:
                        - showTimeZone: Africa/Harare(GMT+02:00)
                          timeZone: Africa/Harare
                    - country: cm
                      id: 331
                      showCountry: 喀麦隆
                      timeZoneVoList:
                        - showTimeZone: Africa/Douala(GMT+01:00)
                          timeZone: Africa/Douala
                    - country: qa
                      id: 422
                      showCountry: 卡塔尔
                      timeZoneVoList:
                        - showTimeZone: Asia/Qatar(GMT+03:00)
                          timeZone: Asia/Qatar
                    - country: ky
                      id: 377
                      showCountry: 开曼群岛
                      timeZoneVoList:
                        - showTimeZone: America/Cayman(GMT-05:00)
                          timeZone: America/Cayman
                    - country: cc
                      id: 325
                      showCountry: 科科斯群岛
                      timeZoneVoList:
                        - showTimeZone: Indian/Cocos(GMT+06:30)
                          timeZone: Indian/Cocos
                    - country: ci
                      id: 329
                      showCountry: 科特迪瓦
                      timeZoneVoList:
                        - showTimeZone: Africa/Abidjan(GMTZ)
                          timeZone: Africa/Abidjan
                    - country: kw
                      id: 376
                      showCountry: 科威特
                      timeZoneVoList:
                        - showTimeZone: Asia/Kuwait(GMT+03:00)
                          timeZone: Asia/Kuwait
                    - country: hr
                      id: 25
                      showCountry: 克罗地亚
                      timeZoneVoList:
                        - showTimeZone: Europe/Zagreb(GMT+01:00)
                          timeZone: Europe/Zagreb
                    - country: ke
                      id: 371
                      showCountry: 肯尼亚
                      timeZoneVoList:
                        - showTimeZone: Africa/Nairobi(GMT+03:00)
                          timeZone: Africa/Nairobi
                    - country: ck
                      id: 330
                      showCountry: 库克群岛
                      timeZoneVoList:
                        - showTimeZone: Pacific/Rarotonga(GMT-10:00)
                          timeZone: Pacific/Rarotonga
                    - country: lv
                      id: 38
                      showCountry: 拉脱维亚
                      timeZoneVoList:
                        - showTimeZone: Europe/Riga(GMT+02:00)
                          timeZone: Europe/Riga
                    - country: ls
                      id: 384
                      showCountry: 莱索托
                      timeZoneVoList:
                        - showTimeZone: Africa/Maseru(GMT+02:00)
                          timeZone: Africa/Maseru
                    - country: la
                      id: 378
                      showCountry: 老挝
                      timeZoneVoList:
                        - showTimeZone: Asia/Vientiane(GMT+07:00)
                          timeZone: Asia/Vientiane
                    - country: lb
                      id: 379
                      showCountry: 黎巴嫩
                      timeZoneVoList:
                        - showTimeZone: Asia/Beirut(GMT+03:00)
                          timeZone: Asia/Beirut
                    - country: lt
                      id: 39
                      showCountry: 立陶宛
                      timeZoneVoList:
                        - showTimeZone: Europe/Vilnius(GMT+02:00)
                          timeZone: Europe/Vilnius
                    - country: lr
                      id: 383
                      showCountry: 利比里亚
                      timeZoneVoList:
                        - showTimeZone: Africa/Monrovia(GMTZ)
                          timeZone: Africa/Monrovia
                    - country: ly
                      id: 386
                      showCountry: 利比亚
                      timeZoneVoList:
                        - showTimeZone: Africa/Tripoli(GMT+02:00)
                          timeZone: Africa/Tripoli
                    - country: li
                      id: 381
                      showCountry: 列支敦士登
                      timeZoneVoList:
                        - showTimeZone: Europe/Vaduz(GMT+02:00)
                          timeZone: Europe/Vaduz
                    - country: re
                      id: 423
                      showCountry: 留尼汪
                      timeZoneVoList:
                        - showTimeZone: Indian/Reunion(GMT+04:00)
                          timeZone: Indian/Reunion
                    - country: lu
                      id: 385
                      showCountry: 卢森堡
                      timeZoneVoList:
                        - showTimeZone: Europe/Luxembourg(GMT+02:00)
                          timeZone: Europe/Luxembourg
                    - country: rw
                      id: 425
                      showCountry: 卢旺达
                      timeZoneVoList:
                        - showTimeZone: Africa/Kigali(GMT+02:00)
                          timeZone: Africa/Kigali
                    - country: ro
                      id: 45
                      showCountry: 罗马尼亚
                      timeZoneVoList:
                        - showTimeZone: Europe/Bucharest(GMT+02:00)
                          timeZone: Europe/Bucharest
                    - country: mg
                      id: 391
                      showCountry: 马达加斯加
                      timeZoneVoList:
                        - showTimeZone: Indian/Antananarivo(GMT+03:00)
                          timeZone: Indian/Antananarivo
                    - country: im
                      id: 364
                      showCountry: 马恩岛
                      timeZoneVoList:
                        - showTimeZone: Europe/Isle_of_Man(GMT+01:00)
                          timeZone: Europe/Isle_of_Man
                    - country: mv
                      id: 404
                      showCountry: 马尔代夫
                      timeZoneVoList:
                        - showTimeZone: Indian/Maldives(GMT+05:00)
                          timeZone: Indian/Maldives
                    - country: fk
                      id: 345
                      showCountry: 马尔维纳斯群岛（ 福克兰）
                      timeZoneVoList:
                        - showTimeZone: Atlantic/Stanley(GMT-03:00)
                          timeZone: Atlantic/Stanley
                    - country: mt
                      id: 402
                      showCountry: 马耳他
                      timeZoneVoList:
                        - showTimeZone: Europe/Malta(GMT+02:00)
                          timeZone: Europe/Malta
                    - country: mw
                      id: 405
                      showCountry: 马拉维
                      timeZoneVoList:
                        - showTimeZone: Africa/Blantyre(GMT+02:00)
                          timeZone: Africa/Blantyre
                    - country: my
                      id: 8
                      showCountry: 马来西亚
                      timeZoneVoList:
                        - showTimeZone: Asia/Kuching(GMT+08:00)
                          timeZone: Asia/Kuching
                        - showTimeZone: Asia/Kuala_Lumpur(GMT+08:00)
                          timeZone: Asia/Kuala_Lumpur
                    - country: ml
                      id: 394
                      showCountry: 马里
                      timeZoneVoList:
                        - showTimeZone: Africa/Bamako(GMTZ)
                          timeZone: Africa/Bamako
                    - country: mk
                      id: 393
                      showCountry: 马其顿
                      timeZoneVoList:
                        - showTimeZone: Europe/Skopje(GMT+02:00)
                          timeZone: Europe/Skopje
                    - country: mh
                      id: 392
                      showCountry: 马绍尔群岛
                      timeZoneVoList:
                        - showTimeZone: Pacific/Majuro(GMT+12:00)
                          timeZone: Pacific/Majuro
                        - showTimeZone: Pacific/Kwajalein(GMT+12:00)
                          timeZone: Pacific/Kwajalein
                    - country: mq
                      id: 399
                      showCountry: 马提尼克
                      timeZoneVoList:
                        - showTimeZone: America/Martinique(GMT-04:00)
                          timeZone: America/Martinique
                    - country: yt
                      id: 466
                      showCountry: 马约特
                      timeZoneVoList:
                        - showTimeZone: Indian/Mayotte(GMT+03:00)
                          timeZone: Indian/Mayotte
                    - country: mu
                      id: 403
                      showCountry: 毛里求斯
                      timeZoneVoList:
                        - showTimeZone: Indian/Mauritius(GMT+04:00)
                          timeZone: Indian/Mauritius
                    - country: mr
                      id: 400
                      showCountry: 毛里塔尼亚
                      timeZoneVoList:
                        - showTimeZone: Africa/Nouakchott(GMTZ)
                          timeZone: Africa/Nouakchott
                    - country: us
                      id: 15
                      showCountry: 美国
                      timeZoneVoList:
                        - showTimeZone: America/Indiana/Indianapolis(GMT-04:00)
                          timeZone: America/Indiana/Indianapolis
                        - showTimeZone: America/Kentucky/Louisville(GMT-04:00)
                          timeZone: America/Kentucky/Louisville
                        - showTimeZone: America/Indiana/Marengo(GMT-04:00)
                          timeZone: America/Indiana/Marengo
                        - showTimeZone: America/Indiana/Vincennes(GMT-04:00)
                          timeZone: America/Indiana/Vincennes
                        - showTimeZone: America/Indiana/Petersburg(GMT-04:00)
                          timeZone: America/Indiana/Petersburg
                        - showTimeZone: America/Indiana/Vevay(GMT-04:00)
                          timeZone: America/Indiana/Vevay
                        - showTimeZone: America/Indiana/Winamac(GMT-04:00)
                          timeZone: America/Indiana/Winamac
                        - showTimeZone: America/Detroit(GMT-04:00)
                          timeZone: America/Detroit
                        - showTimeZone: America/Kentucky/Monticello(GMT-04:00)
                          timeZone: America/Kentucky/Monticello
                        - showTimeZone: America/Indiana/Tell_City(GMT-05:00)
                          timeZone: America/Indiana/Tell_City
                        - showTimeZone: America/Indiana/Knox(GMT-05:00)
                          timeZone: America/Indiana/Knox
                        - showTimeZone: America/Menominee(GMT-05:00)
                          timeZone: America/Menominee
                        - showTimeZone: America/North_Dakota/Beulah(GMT-05:00)
                          timeZone: America/North_Dakota/Beulah
                        - showTimeZone: America/North_Dakota/Center(GMT-05:00)
                          timeZone: America/North_Dakota/Center
                        - showTimeZone: America/North_Dakota/New_Salem(GMT-05:00)
                          timeZone: America/North_Dakota/New_Salem
                        - showTimeZone: America/New_York(GMT-05:00)
                          timeZone: America/New_York
                        - showTimeZone: America/Boise(GMT-06:00)
                          timeZone: America/Boise
                        - showTimeZone: America/Chicago(GMT-06:00)
                          timeZone: America/Chicago
                        - showTimeZone: America/Phoenix(GMT-07:00)
                          timeZone: America/Phoenix
                        - showTimeZone: America/Denver(GMT-07:00)
                          timeZone: America/Denver
                        - showTimeZone: America/Nome(GMT-08:00)
                          timeZone: America/Nome
                        - showTimeZone: America/Juneau(GMT-08:00)
                          timeZone: America/Juneau
                        - showTimeZone: America/Sitka(GMT-08:00)
                          timeZone: America/Sitka
                        - showTimeZone: America/Los_Angeles(GMT-08:00)
                          timeZone: America/Los_Angeles
                        - showTimeZone: America/Metlakatla(GMT-09:00)
                          timeZone: America/Metlakatla
                        - showTimeZone: America/Anchorage(GMT-09:00)
                          timeZone: America/Anchorage
                        - showTimeZone: Pacific/Honolulu(GMT-10:00)
                          timeZone: Pacific/Honolulu
                        - showTimeZone: America/Adak(GMT-10:00)
                          timeZone: America/Adak
                    - country: um
                      id: 454
                      showCountry: 美国本土外小岛屿
                      timeZoneVoList:
                        - showTimeZone: Pacific/Wake(GMT+12:00)
                          timeZone: Pacific/Wake
                        - showTimeZone: Pacific/Johnston(GMT-10:00)
                          timeZone: Pacific/Johnston
                        - showTimeZone: Pacific/Midway(GMT-11:00)
                          timeZone: Pacific/Midway
                    - country: as
                      id: 305
                      showCountry: 美属萨摩亚
                      timeZoneVoList:
                        - showTimeZone: US/Samoa(GMT-11:00)
                          timeZone: US/Samoa
                        - showTimeZone: Pacific/Pago_Pago(GMT-11:00)
                          timeZone: Pacific/Pago_Pago
                    - country: vi
                      id: 461
                      showCountry: 美属维尔京群岛
                      timeZoneVoList:
                        - showTimeZone: America/St_Thomas(GMT-04:00)
                          timeZone: America/St_Thomas
                    - country: mn
                      id: 396
                      showCountry: 蒙古国
                      timeZoneVoList:
                        - showTimeZone: Asia/Hovd(GMT+07:00)
                          timeZone: Asia/Hovd
                        - showTimeZone: Asia/Ulaanbaatar(GMT+08:00)
                          timeZone: Asia/Ulaanbaatar
                        - showTimeZone: Asia/Choibalsan(GMT+08:00)
                          timeZone: Asia/Choibalsan
                    - country: ms
                      id: 401
                      showCountry: 蒙塞拉特岛
                      timeZoneVoList:
                        - showTimeZone: America/Montserrat(GMT-04:00)
                          timeZone: America/Montserrat
                    - country: bd
                      id: 311
                      showCountry: 孟加拉国
                      timeZoneVoList:
                        - showTimeZone: Asia/Dhaka(GMT+06:00)
                          timeZone: Asia/Dhaka
                    - country: pe
                      id: 56
                      showCountry: 秘鲁
                      timeZoneVoList:
                        - showTimeZone: America/Lima(GMT-05:00)
                          timeZone: America/Lima
                    - country: fm
                      id: 346
                      showCountry: 密克罗尼西亚联邦
                      timeZoneVoList:
                        - showTimeZone: Pacific/Chuuk(GMT+10:00)
                          timeZone: Pacific/Chuuk
                        - showTimeZone: Pacific/Kosrae(GMT+11:00)
                          timeZone: Pacific/Kosrae
                        - showTimeZone: Pacific/Pohnpei(GMT+11:00)
                          timeZone: Pacific/Pohnpei
                    - country: mm
                      id: 395
                      showCountry: 缅甸
                      timeZoneVoList:
                        - showTimeZone: Asia/Rangoon(GMT+06:30)
                          timeZone: Asia/Rangoon
                    - country: md
                      id: 389
                      showCountry: 摩尔多瓦
                      timeZoneVoList:
                        - showTimeZone: Europe/Chisinau(GMT+03:00)
                          timeZone: Europe/Chisinau
                    - country: ma
                      id: 387
                      showCountry: 摩洛哥
                      timeZoneVoList:
                        - showTimeZone: Africa/Casablanca(GMT+01:00)
                          timeZone: Africa/Casablanca
                    - country: mc
                      id: 388
                      showCountry: 摩纳哥
                      timeZoneVoList:
                        - showTimeZone: Europe/Monaco(GMT+02:00)
                          timeZone: Europe/Monaco
                    - country: mz
                      id: 406
                      showCountry: 莫桑比克
                      timeZoneVoList:
                        - showTimeZone: Africa/Maputo(GMT+02:00)
                          timeZone: Africa/Maputo
                    - country: mx
                      id: 40
                      showCountry: 墨西哥
                      timeZoneVoList:
                        - showTimeZone: America/Cancun(GMT-05:00)
                          timeZone: America/Cancun
                        - showTimeZone: America/Merida(GMT-06:00)
                          timeZone: America/Merida
                        - showTimeZone: America/Monterrey(GMT-06:00)
                          timeZone: America/Monterrey
                        - showTimeZone: America/Bahia_Banderas(GMT-06:00)
                          timeZone: America/Bahia_Banderas
                        - showTimeZone: America/Mexico_City(GMT-06:00)
                          timeZone: America/Mexico_City
                        - showTimeZone: America/Matamoros(GMT-06:00)
                          timeZone: America/Matamoros
                        - showTimeZone: America/Santa_Isabel(GMT-07:00)
                          timeZone: America/Santa_Isabel
                        - showTimeZone: America/Mazatlan(GMT-07:00)
                          timeZone: America/Mazatlan
                        - showTimeZone: America/Hermosillo(GMT-07:00)
                          timeZone: America/Hermosillo
                        - showTimeZone: America/Chihuahua(GMT-07:00)
                          timeZone: America/Chihuahua
                        - showTimeZone: America/Ojinaga(GMT-07:00)
                          timeZone: America/Ojinaga
                        - showTimeZone: America/Tijuana(GMT-08:00)
                          timeZone: America/Tijuana
                    - country: za
                      id: 52
                      showCountry: 南非
                      timeZoneVoList:
                        - showTimeZone: Africa/Johannesburg(GMT+02:00)
                          timeZone: Africa/Johannesburg
                    - country: ss
                      id: 435
                      showCountry: 南苏丹
                      timeZoneVoList:
                        - showTimeZone: Africa/Juba(GMT+02:00)
                          timeZone: Africa/Juba
                    - country: nr
                      id: 411
                      showCountry: 瑙鲁
                      timeZoneVoList:
                        - showTimeZone: Pacific/Nauru(GMT+12:00)
                          timeZone: Pacific/Nauru
                    - country: np
                      id: 410
                      showCountry: 尼泊尔
                      timeZoneVoList:
                        - showTimeZone: Asia/Kathmandu(GMT+05:45)
                          timeZone: Asia/Kathmandu
                    - country: ni
                      id: 409
                      showCountry: 尼加拉瓜
                      timeZoneVoList:
                        - showTimeZone: America/Managua(GMT-06:00)
                          timeZone: America/Managua
                    - country: ng
                      id: 57
                      showCountry: 尼日利亚
                      timeZoneVoList:
                        - showTimeZone: Africa/Lagos(GMT+01:00)
                          timeZone: Africa/Lagos
                    - country: nu
                      id: 412
                      showCountry: 纽埃
                      timeZoneVoList:
                        - showTimeZone: Pacific/Niue(GMT-11:00)
                          timeZone: Pacific/Niue
                    - country: 'no'
                      id: 42
                      showCountry: 挪威
                      timeZoneVoList:
                        - showTimeZone: Europe/Oslo(GMT+01:00)
                          timeZone: Europe/Oslo
                    - country: nf
                      id: 408
                      showCountry: 诺福克岛
                      timeZoneVoList:
                        - showTimeZone: Pacific/Norfolk(GMT+12:00)
                          timeZone: Pacific/Norfolk
                    - country: pw
                      id: 420
                      showCountry: 帕劳
                      timeZoneVoList:
                        - showTimeZone: Pacific/Palau(GMT+09:00)
                          timeZone: Pacific/Palau
                    - country: pn
                      id: 417
                      showCountry: 皮特凯恩群岛
                      timeZoneVoList:
                        - showTimeZone: Pacific/Pitcairn(GMT-08:00)
                          timeZone: Pacific/Pitcairn
                    - country: pt
                      id: 44
                      showCountry: 葡萄牙
                      timeZoneVoList:
                        - showTimeZone: Europe/Lisbon(GMT+00:00)
                          timeZone: Europe/Lisbon
                        - showTimeZone: Atlantic/Madeira(GMT+01:00)
                          timeZone: Atlantic/Madeira
                        - showTimeZone: Atlantic/Azores(GMT-01:00)
                          timeZone: Atlantic/Azores
                    - country: jp
                      id: 7
                      showCountry: 日本
                      timeZoneVoList:
                        - showTimeZone: Asia/Tokyo(GMT+09:00)
                          timeZone: Asia/Tokyo
                    - country: se
                      id: 49
                      showCountry: 瑞典
                      timeZoneVoList:
                        - showTimeZone: Europe/Stockholm(GMT+01:00)
                          timeZone: Europe/Stockholm
                    - country: ch
                      id: 50
                      showCountry: 瑞士
                      timeZoneVoList:
                        - showTimeZone: Europe/Zurich(GMT+01:00)
                          timeZone: Europe/Zurich
                    - country: sv
                      id: 437
                      showCountry: 萨尔瓦多
                      timeZoneVoList:
                        - showTimeZone: America/El_Salvador(GMT-06:00)
                          timeZone: America/El_Salvador
                    - country: ws
                      id: 464
                      showCountry: 萨摩亚
                      timeZoneVoList:
                        - showTimeZone: Pacific/Apia(GMT+13:00)
                          timeZone: Pacific/Apia
                    - country: rs
                      id: 424
                      showCountry: 塞尔维亚
                      timeZoneVoList:
                        - showTimeZone: Europe/Belgrade(GMT+02:00)
                          timeZone: Europe/Belgrade
                    - country: sl
                      id: 430
                      showCountry: 塞拉利昂
                      timeZoneVoList:
                        - showTimeZone: Africa/Freetown(GMTZ)
                          timeZone: Africa/Freetown
                    - country: sn
                      id: 432
                      showCountry: 塞内加尔
                      timeZoneVoList:
                        - showTimeZone: Africa/Dakar(GMTZ)
                          timeZone: Africa/Dakar
                    - country: cy
                      id: 26
                      showCountry: 塞浦路斯
                      timeZoneVoList:
                        - showTimeZone: Asia/Nicosia(GMT+02:00)
                          timeZone: Asia/Nicosia
                        - showTimeZone: Asia/Famagusta(GMT+02:00)
                          timeZone: Asia/Famagusta
                    - country: sc
                      id: 427
                      showCountry: 塞舌尔
                      timeZoneVoList:
                        - showTimeZone: Indian/Mahe(GMT+04:00)
                          timeZone: Indian/Mahe
                    - country: sa
                      id: 62
                      showCountry: 沙特阿拉伯
                      timeZoneVoList:
                        - showTimeZone: Asia/Riyadh(GMT-05:00)
                          timeZone: Asia/Riyadh
                    - country: cx
                      id: 335
                      showCountry: 圣诞岛
                      timeZoneVoList:
                        - showTimeZone: Indian/Christmas(GMT+07:00)
                          timeZone: Indian/Christmas
                    - country: st
                      id: 436
                      showCountry: 圣多美和普林西比
                      timeZoneVoList:
                        - showTimeZone: Africa/Sao_Tome(GMTZ)
                          timeZone: Africa/Sao_Tome
                    - country: sh
                      id: 429
                      showCountry: 圣赫勒拿
                      timeZoneVoList:
                        - showTimeZone: Atlantic/St_Helena(GMTZ)
                          timeZone: Atlantic/St_Helena
                    - country: kn
                      id: 374
                      showCountry: 圣基茨和尼维斯
                      timeZoneVoList:
                        - showTimeZone: America/St_Kitts(GMT-04:00)
                          timeZone: America/St_Kitts
                    - country: lc
                      id: 380
                      showCountry: 圣卢西亚
                      timeZoneVoList:
                        - showTimeZone: America/St_Lucia(GMT-04:00)
                          timeZone: America/St_Lucia
                    - country: sm
                      id: 431
                      showCountry: 圣马力诺
                      timeZoneVoList:
                        - showTimeZone: Europe/San_Marino(GMT+02:00)
                          timeZone: Europe/San_Marino
                    - country: vc
                      id: 458
                      showCountry: 圣文森特和格林纳丁斯
                      timeZoneVoList:
                        - showTimeZone: America/St_Vincent(GMT-04:00)
                          timeZone: America/St_Vincent
                    - country: lk
                      id: 382
                      showCountry: 斯里兰卡
                      timeZoneVoList:
                        - showTimeZone: Asia/Colombo(GMT+05:30)
                          timeZone: Asia/Colombo
                    - country: sk
                      id: 46
                      showCountry: 斯洛伐克
                      timeZoneVoList:
                        - showTimeZone: Europe/Bratislava(GMT+01:00)
                          timeZone: Europe/Bratislava
                    - country: si
                      id: 47
                      showCountry: 斯洛文尼亚
                      timeZoneVoList:
                        - showTimeZone: Europe/Ljubljana(GMT+01:00)
                          timeZone: Europe/Ljubljana
                    - country: sz
                      id: 439
                      showCountry: 斯威士兰
                      timeZoneVoList:
                        - showTimeZone: Africa/Mbabane(GMT+02:00)
                          timeZone: Africa/Mbabane
                    - country: sd
                      id: 428
                      showCountry: 苏丹
                      timeZoneVoList:
                        - showTimeZone: Africa/Khartoum(GMT+02:00)
                          timeZone: Africa/Khartoum
                    - country: sr
                      id: 434
                      showCountry: 苏里南
                      timeZoneVoList:
                        - showTimeZone: America/Paramaribo(GMT-03:00)
                          timeZone: America/Paramaribo
                    - country: sb
                      id: 426
                      showCountry: 所罗门群岛
                      timeZoneVoList:
                        - showTimeZone: Pacific/Guadalcanal(GMT+11:00)
                          timeZone: Pacific/Guadalcanal
                    - country: so
                      id: 433
                      showCountry: 索马里
                      timeZoneVoList:
                        - showTimeZone: Africa/Mogadishu(GMT+03:00)
                          timeZone: Africa/Mogadishu
                    - country: tj
                      id: 443
                      showCountry: 塔吉克斯坦
                      timeZoneVoList:
                        - showTimeZone: Asia/Dushanbe(GMT+05:00)
                          timeZone: Asia/Dushanbe
                    - country: tw
                      id: 13
                      showCountry: 台湾
                      timeZoneVoList:
                        - showTimeZone: Asia/Taipei(GMT+08:00)
                          timeZone: Asia/Taipei
                    - country: th
                      id: 17
                      showCountry: 泰国
                      timeZoneVoList:
                        - showTimeZone: Asia/Bangkok(GMT+07:00)
                          timeZone: Asia/Bangkok
                    - country: tz
                      id: 451
                      showCountry: 坦桑尼亚
                      timeZoneVoList:
                        - showTimeZone: Africa/Dar_es_Salaam(GMT+03:00)
                          timeZone: Africa/Dar_es_Salaam
                    - country: to
                      id: 448
                      showCountry: 汤加
                      timeZoneVoList:
                        - showTimeZone: Pacific/Tongatapu(GMT+13:00)
                          timeZone: Pacific/Tongatapu
                    - country: tc
                      id: 440
                      showCountry: 特克斯和凯科斯群岛
                      timeZoneVoList:
                        - showTimeZone: America/Grand_Turk(GMT-04:00)
                          timeZone: America/Grand_Turk
                    - country: tt
                      id: 449
                      showCountry: 特立尼达和多巴哥
                      timeZoneVoList:
                        - showTimeZone: America/Port_of_Spain(GMT-04:00)
                          timeZone: America/Port_of_Spain
                    - country: tn
                      id: 447
                      showCountry: 突尼斯
                      timeZoneVoList:
                        - showTimeZone: Africa/Tunis(GMT+01:00)
                          timeZone: Africa/Tunis
                    - country: tv
                      id: 450
                      showCountry: 图瓦卢
                      timeZoneVoList:
                        - showTimeZone: Pacific/Funafuti(GMT+12:00)
                          timeZone: Pacific/Funafuti
                    - country: tr
                      id: 51
                      showCountry: 土耳其
                      timeZoneVoList:
                        - showTimeZone: Europe/Istanbul(GMT+03:00)
                          timeZone: Europe/Istanbul
                        - showTimeZone: Asia/Istanbul(GMT+03:00)
                          timeZone: Asia/Istanbul
                    - country: tm
                      id: 446
                      showCountry: 土库曼斯坦
                      timeZoneVoList:
                        - showTimeZone: Asia/Ashgabat(GMT+05:00)
                          timeZone: Asia/Ashgabat
                    - country: tk
                      id: 444
                      showCountry: 托克劳
                      timeZoneVoList:
                        - showTimeZone: Pacific/Fakaofo(GMT+13:00)
                          timeZone: Pacific/Fakaofo
                    - country: wf
                      id: 463
                      showCountry: 瓦利斯和富图纳
                      timeZoneVoList:
                        - showTimeZone: Pacific/Wallis(GMT+12:00)
                          timeZone: Pacific/Wallis
                    - country: vu
                      id: 462
                      showCountry: 瓦努阿图
                      timeZoneVoList:
                        - showTimeZone: Pacific/Efate(GMT+11:00)
                          timeZone: Pacific/Efate
                    - country: gt
                      id: 358
                      showCountry: 危地马拉
                      timeZoneVoList:
                        - showTimeZone: America/Guatemala(GMT-06:00)
                          timeZone: America/Guatemala
                    - country: ve
                      id: 459
                      showCountry: 委内瑞拉
                      timeZoneVoList:
                        - showTimeZone: America/Caracas(GMT-04:00)
                          timeZone: America/Caracas
                    - country: bn
                      id: 317
                      showCountry: 文莱
                      timeZoneVoList:
                        - showTimeZone: Asia/Brunei(GMT+08:00)
                          timeZone: Asia/Brunei
                    - country: ug
                      id: 453
                      showCountry: 乌干达
                      timeZoneVoList:
                        - showTimeZone: Africa/Kampala(GMT+03:00)
                          timeZone: Africa/Kampala
                    - country: ua
                      id: 452
                      showCountry: 乌克兰
                      timeZoneVoList:
                        - showTimeZone: Europe/Simferopol(GMT+03:00)
                          timeZone: Europe/Simferopol
                        - showTimeZone: Europe/Kiev(GMT+03:00)
                          timeZone: Europe/Kiev
                        - showTimeZone: Europe/Zaporozhye(GMT+03:00)
                          timeZone: Europe/Zaporozhye
                        - showTimeZone: Europe/Uzhgorod(GMT+03:00)
                          timeZone: Europe/Uzhgorod
                    - country: uy
                      id: 455
                      showCountry: 乌拉圭
                      timeZoneVoList:
                        - showTimeZone: America/Montevideo(GMT-03:00)
                          timeZone: America/Montevideo
                    - country: uz
                      id: 456
                      showCountry: 乌兹别克斯坦
                      timeZoneVoList:
                        - showTimeZone: Asia/Tashkent(GMT+05:00)
                          timeZone: Asia/Tashkent
                        - showTimeZone: Asia/Samarkand(GMT+05:00)
                          timeZone: Asia/Samarkand
                    - country: es
                      id: 48
                      showCountry: 西班牙
                      timeZoneVoList:
                        - showTimeZone: Atlantic/Canary(GMT+00:00)
                          timeZone: Atlantic/Canary
                        - showTimeZone: Europe/Madrid(GMT+01:00)
                          timeZone: Europe/Madrid
                        - showTimeZone: Africa/Ceuta(GMT+02:00)
                          timeZone: Africa/Ceuta
                    - country: eh
                      id: 341
                      showCountry: 西撒哈拉
                      timeZoneVoList:
                        - showTimeZone: Africa/El_Aaiun(GMT+01:00)
                          timeZone: Africa/El_Aaiun
                    - country: gr
                      id: 32
                      showCountry: 希腊
                      timeZoneVoList:
                        - showTimeZone: Europe/Athens(GMT+02:00)
                          timeZone: Europe/Athens
                    - country: hk
                      id: 6
                      showCountry: 香港
                      timeZoneVoList:
                        - showTimeZone: Asia/Hong_Kong(GMT+08:00)
                          timeZone: Asia/Hong_Kong
                    - country: sg
                      id: 11
                      showCountry: 新加坡
                      timeZoneVoList:
                        - showTimeZone: Asia/Singapore(GMT+08:00)
                          timeZone: Asia/Singapore
                    - country: nc
                      id: 407
                      showCountry: 新喀里多尼亚
                      timeZoneVoList:
                        - showTimeZone: Pacific/Noumea(GMT+11:00)
                          timeZone: Pacific/Noumea
                    - country: nz
                      id: 64
                      showCountry: 新西兰
                      timeZoneVoList:
                        - showTimeZone: Pacific/Auckland(GMT+12:00)
                          timeZone: Pacific/Auckland
                        - showTimeZone: Pacific/Chatham(GMT+13:45)
                          timeZone: Pacific/Chatham
                    - country: hu
                      id: 33
                      showCountry: 匈牙利
                      timeZoneVoList:
                        - showTimeZone: Europe/Budapest(GMT+01:00)
                          timeZone: Europe/Budapest
                    - country: sy
                      id: 438
                      showCountry: 叙利亚
                      timeZoneVoList:
                        - showTimeZone: Asia/Damascus(GMT+03:00)
                          timeZone: Asia/Damascus
                    - country: jm
                      id: 369
                      showCountry: 牙买加
                      timeZoneVoList:
                        - showTimeZone: America/Jamaica(GMT-05:00)
                          timeZone: America/Jamaica
                    - country: am
                      id: 303
                      showCountry: 亚美尼亚
                      timeZoneVoList:
                        - showTimeZone: Asia/Yerevan(GMT+04:00)
                          timeZone: Asia/Yerevan
                    - country: ye
                      id: 465
                      showCountry: 也门
                      timeZoneVoList:
                        - showTimeZone: Asia/Aden(GMT+03:00)
                          timeZone: Asia/Aden
                    - country: iq
                      id: 366
                      showCountry: 伊拉克
                      timeZoneVoList:
                        - showTimeZone: Asia/Baghdad(GMT+03:00)
                          timeZone: Asia/Baghdad
                    - country: ir
                      id: 367
                      showCountry: 伊朗
                      timeZoneVoList:
                        - showTimeZone: Asia/Tehran(GMT+03:30)
                          timeZone: Asia/Tehran
                    - country: il
                      id: 36
                      showCountry: 以色列
                      timeZoneVoList:
                        - showTimeZone: Asia/Jerusalem(GMT+02:00)
                          timeZone: Asia/Jerusalem
                    - country: it
                      id: 37
                      showCountry: 意大利
                      timeZoneVoList:
                        - showTimeZone: Europe/Rome(GMT+01:00)
                          timeZone: Europe/Rome
                    - country: in
                      id: 63
                      showCountry: 印度
                      timeZoneVoList:
                        - showTimeZone: Asia/Kolkata(GMT+05:30)
                          timeZone: Asia/Kolkata
                    - country: id
                      id: 16
                      showCountry: 印度尼西亚
                      timeZoneVoList:
                        - showTimeZone: Asia/Pontianak(GMT+07:00)
                          timeZone: Asia/Pontianak
                        - showTimeZone: Asia/Jakarta(GMT+07:00)
                          timeZone: Asia/Jakarta
                        - showTimeZone: Asia/Makassar(GMT+08:00)
                          timeZone: Asia/Makassar
                        - showTimeZone: Asia/Jayapura(GMT+09:00)
                          timeZone: Asia/Jayapura
                    - country: gb
                      id: 14
                      showCountry: 英国
                      timeZoneVoList:
                        - showTimeZone: Europe/London(GMT+00:00)
                          timeZone: Europe/London
                    - country: vg
                      id: 460
                      showCountry: 英属维尔京群岛
                      timeZoneVoList:
                        - showTimeZone: America/Tortola(GMT-04:00)
                          timeZone: America/Tortola
                    - country: io
                      id: 365
                      showCountry: 英属印度洋领地
                      timeZoneVoList:
                        - showTimeZone: Indian/Chagos(GMT+06:00)
                          timeZone: Indian/Chagos
                    - country: jo
                      id: 370
                      showCountry: 约旦
                      timeZoneVoList:
                        - showTimeZone: Asia/Amman(GMT+03:00)
                          timeZone: Asia/Amman
                    - country: vn
                      id: 18
                      showCountry: 越南
                      timeZoneVoList:
                        - showTimeZone: Asia/Bangkok(GMT+07:00)
                          timeZone: Asia/Bangkok
                        - showTimeZone: Asia/Ho_Chi_Minh(GMT+07:00)
                          timeZone: Asia/Ho_Chi_Minh
                    - country: zm
                      id: 467
                      showCountry: 赞比亚
                      timeZoneVoList:
                        - showTimeZone: Africa/Lusaka(GMT+02:00)
                          timeZone: Africa/Lusaka
                    - country: je
                      id: 368
                      showCountry: 泽西岛
                      timeZoneVoList:
                        - showTimeZone: Europe/Jersey(GMT+01:00)
                          timeZone: Europe/Jersey
                    - country: td
                      id: 441
                      showCountry: 乍得
                      timeZoneVoList:
                        - showTimeZone: Africa/Ndjamena(GMT+01:00)
                          timeZone: Africa/Ndjamena
                    - country: gi
                      id: 353
                      showCountry: 直布罗陀
                      timeZoneVoList:
                        - showTimeZone: Europe/Gibraltar(GMT+02:00)
                          timeZone: Europe/Gibraltar
                    - country: cl
                      id: 24
                      showCountry: 智利
                      timeZoneVoList:
                        - showTimeZone: Antarctica/Rothera(GMT-03:00)
                          timeZone: Antarctica/Rothera
                        - showTimeZone: America/Santiago(GMT-03:00)
                          timeZone: America/Santiago
                        - showTimeZone: America/Punta_Arenas(GMT-03:00)
                          timeZone: America/Punta_Arenas
                        - showTimeZone: Chile/EasterIsland(GMT-05:00)
                          timeZone: Chile/EasterIsland
                        - showTimeZone: Pacific/Easter(GMT-05:00)
                          timeZone: Pacific/Easter
                    - country: cf
                      id: 327
                      showCountry: 中非
                      timeZoneVoList:
                        - showTimeZone: Africa/Bangui(GMT+01:00)
                          timeZone: Africa/Bangui
                    - country: cn
                      id: 3
                      showCountry: 中国
                      timeZoneVoList:
                        - showTimeZone: Asia/Kashgar(GMT+06:00)
                          timeZone: Asia/Kashgar
                        - showTimeZone: Asia/Urumqi(GMT+06:00)
                          timeZone: Asia/Urumqi
                        - showTimeZone: Asia/Chongqing(GMT+08:00)
                          timeZone: Asia/Chongqing
                        - showTimeZone: Asia/Harbin(GMT+08:00)
                          timeZone: Asia/Harbin
                        - showTimeZone: Asia/Macao(GMT+08:00)
                          timeZone: Asia/Macao
                        - showTimeZone: Asia/Shanghai(GMT+08:00)
                          timeZone: Asia/Shanghai
                    - country: mo
                      id: 397
                      showCountry: 中国澳门
                      timeZoneVoList: []
                  languageList:
                    - language: af-ZA
                      showLanguage: Afrikaans (Suid Africa)
                    - language: agq-CM
                      showLanguage: Aghem (Cameroon)
                    - language: ak-GH
                      showLanguage: Akan (Ghana)
                    - language: sq-AL
                      showLanguage: Albanian (Albania)
                    - language: am-ET
                      showLanguage: Amharic (Ethiopia)
                    - language: ar-DZ
                      showLanguage: Arabic (Algeria)
                    - language: ar-AE
                      showLanguage: Arabic (Arab Emirates)
                    - language: ar-BH
                      showLanguage: Arabic (Bahrain)
                    - language: ar-DJ
                      showLanguage: Arabic (Djibouti)
                    - language: ar-EG
                      showLanguage: Arabic (Egypt)
                    - language: ar-ER
                      showLanguage: Arabic (Eritrea)
                    - language: ar-IQ
                      showLanguage: Arabic (Iraq)
                    - language: ar-IL
                      showLanguage: Arabic (Israel)
                    - language: ar-JO
                      showLanguage: Arabic (Jordan)
                    - language: ar-KW
                      showLanguage: Arabic (Kuwait)
                    - language: ar-LB
                      showLanguage: Arabic (Lebanon)
                    - language: ar-LY
                      showLanguage: Arabic (Libya)
                    - language: ar-MR
                      showLanguage: Arabic (Mauritania)
                    - language: ar-MA
                      showLanguage: Arabic (Morocco)
                    - language: ar-OM
                      showLanguage: Arabic (Oman)
                    - language: ar-PS
                      showLanguage: Arabic (Palestine)
                    - language: ar-QA
                      showLanguage: Arabic (Qatar)
                    - language: ar-SA
                      showLanguage: Arabic (Saudi Arabia)
                    - language: ar-SO
                      showLanguage: Arabic (Somalia)
                    - language: ar-SD
                      showLanguage: Arabic (Sudan)
                    - language: ar-SY
                      showLanguage: Arabic (Syria)
                    - language: ar-TN
                      showLanguage: Arabic (Tunisia)
                    - language: ar-EH
                      showLanguage: Arabic (Western sahara)
                    - language: ar-YE
                      showLanguage: Arabic (Yemen)
                    - language: hy-AM
                      showLanguage: Armenian (Armenia)
                    - language: as-IN
                      showLanguage: Assamese (India)
                    - language: az-AZ
                      showLanguage: Azerbaijani (Azerbaijan)
                    - language: ksf-CM
                      showLanguage: Bafia (Cameroon)
                    - language: bas-CM
                      showLanguage: Basaa (Cameroon)
                    - language: eu-ES
                      showLanguage: Basque (Spain)
                    - language: be-BY
                      showLanguage: Belarusian (Belarus)
                    - language: bn-BD
                      showLanguage: Bengali (Bangladesh)
                    - language: bn-IN
                      showLanguage: Bengali (India)
                    - language: brx-IN
                      showLanguage: Bodo (India)
                    - language: br-FR
                      showLanguage: Breton (France)
                    - language: bg-BG
                      showLanguage: Bulgarian (Bulgaria)
                    - language: my-MM
                      showLanguage: Burmese (Myanmar)
                    - language: ca-FR
                      showLanguage: Catalan (France)
                    - language: ca-IT
                      showLanguage: Catalan (Italy)
                    - language: ca-ES
                      showLanguage: Catalan (Spain)
                    - language: ca-AD
                      showLanguage: Catalan(Andorra)
                    - language: tzm-MA
                      showLanguage: Central Atlas Tamazight (Morocco)
                    - language: ce-RU
                      showLanguage: Chechen (Russia)
                    - language: chr-US
                      showLanguage: Cherokee (United States)
                    - language: zh-CN
                      showLanguage: Chinese (Simplified Han,China)
                    - language: zh-Hans-CN
                      showLanguage: Chinese (Simplified Han,China)
                    - language: zh-Hans-HK
                      showLanguage: Chinese (Simplified Han,Hong Kong)
                    - language: zh-SG
                      showLanguage: Chinese (Simplified Han,Singapore)
                    - language: zh-Hans-SG
                      showLanguage: Chinese (Simplified Han,Singapore)
                    - language: zh-HK
                      showLanguage: Chinese (Traditional Han,Hong Kong)
                    - language: zh-Hant-HK
                      showLanguage: Chinese (Traditional Han,Hong Kong)
                    - language: zh-Hant-MO
                      showLanguage: Chinese (Traditional Han,Macao)
                    - language: zh-TW
                      showLanguage: Chinese (Traditional Han,Taiwan)
                    - language: zh-Hant-TW
                      showLanguage: Chinese (Traditional Han,Taiwan)
                    - language: ksh-DE
                      showLanguage: Colognian (Germany)
                    - language: kw-GB
                      showLanguage: Cornish (United Kingdom)
                    - language: hr-HR
                      showLanguage: Croatian (Croatia)
                    - language: cs-CZ
                      showLanguage: Czech (Czech Republic)
                    - language: da-DK
                      showLanguage: Danish (Denmark)
                    - language: da-GL
                      showLanguage: Danish (Greenland)
                    - language: dua-CM
                      showLanguage: Duala (Cameroon)
                    - language: nl-AW
                      showLanguage: Dutch (Aruba)
                    - language: nl-BE
                      showLanguage: Dutch (Belgium)
                    - language: nl-NL
                      showLanguage: Dutch (Netherlands)
                    - language: nl-SR
                      showLanguage: Dutch (Suriname)
                    - language: dz-BT
                      showLanguage: Dzongkha (Bhutan)
                    - language: en-AG
                      showLanguage: English (Antigua)
                    - language: en-AU
                      showLanguage: English (Australia)
                    - language: en-AT
                      showLanguage: English (Austria)
                    - language: en-BS
                      showLanguage: English (Bahamas)
                    - language: en-BB
                      showLanguage: English (Barbados)
                    - language: en-BE
                      showLanguage: English (Belgium)
                    - language: en-BZ
                      showLanguage: English (Belize)
                    - language: en-BM
                      showLanguage: English (Bermuda)
                    - language: en-BT
                      showLanguage: English (Bhutan)
                    - language: en-BW
                      showLanguage: English (Botsmana)
                    - language: en-BV
                      showLanguage: English (Bouvetisland)
                    - language: en-IO
                      showLanguage: English (Briti shindian ocean)
                    - language: en-VG
                      showLanguage: English (British virgin islands)
                    - language: en-BI
                      showLanguage: English (Burundi)
                    - language: en-CM
                      showLanguage: English (Cameroon)
                    - language: en-CA
                      showLanguage: English (Canada)
                    - language: en-BQ
                      showLanguage: English (Caribisch Nederland)
                    - language: en-KY
                      showLanguage: English (Cayman Islands)
                    - language: en-CX
                      showLanguage: English (Chrismasisland)
                    - language: en-CC
                      showLanguage: English (Cocos islands)
                    - language: en-CK
                      showLanguage: English (Cookislands)
                    - language: en-CY
                      showLanguage: English (Cyprus)
                    - language: en-DK
                      showLanguage: English (Denmark)
                    - language: en-DM
                      showLanguage: English (Doninica)
                    - language: en-ER
                      showLanguage: English (Eritrea)
                    - language: en-FJ
                      showLanguage: English (Fiji)
                    - language: en-FI
                      showLanguage: English (Finland)
                    - language: en-GM
                      showLanguage: English (Gambia)
                    - language: en-DE
                      showLanguage: English (Germany)
                    - language: en-GH
                      showLanguage: English (Ghana)
                    - language: en-GI
                      showLanguage: English (Gibraltar)
                    - language: en-GD
                      showLanguage: English (Grenada)
                    - language: en-GU
                      showLanguage: English (Guam)
                    - language: en-GG
                      showLanguage: English (Guernsey)
                    - language: en-GY
                      showLanguage: English (Guyana)
                    - language: en-HK
                      showLanguage: English (Hong Kong)
                    - language: en-IN
                      showLanguage: English (India)
                    - language: en-IE
                      showLanguage: English (Ireland)
                    - language: en-IM
                      showLanguage: English (Isle of Man)
                    - language: en-IL
                      showLanguage: English (Israel)
                    - language: en-JM
                      showLanguage: English (Jamaica)
                    - language: en-JE
                      showLanguage: English (Jersey)
                    - language: en-KI
                      showLanguage: English (Kiribati)
                    - language: en-LS
                      showLanguage: English (Lesotho)
                    - language: en-LR
                      showLanguage: English (Liberia)
                    - language: en-MW
                      showLanguage: English (Malawi)
                    - language: en-MY
                      showLanguage: English (Malaysia)
                    - language: en-MV
                      showLanguage: English (Maldives)
                    - language: en-FK
                      showLanguage: English (Malvinas islands)
                    - language: en-MH
                      showLanguage: English (Marshall islands)
                    - language: en-MU
                      showLanguage: English (Mauritius)
                    - language: en-FM
                      showLanguage: English (Micronesia)
                    - language: en-MD
                      showLanguage: English (Moldova)
                    - language: en-ME
                      showLanguage: English (Montenegro)
                    - language: en-MS
                      showLanguage: English (Montserrat)
                    - language: en-NR
                      showLanguage: English (Nauru)
                    - language: en-NL
                      showLanguage: English (Netherlands)
                    - language: en-NZ
                      showLanguage: English (New Zealand)
                    - language: en-NG
                      showLanguage: English (Nigeria)
                    - language: en-NU
                      showLanguage: English (Niue)
                    - language: en-NF
                      showLanguage: English (Norfolk island)
                    - language: en-MP
                      showLanguage: English (Northern marianas)
                    - language: en-PK
                      showLanguage: English (Pakistan)
                    - language: en-PW
                      showLanguage: English (Palau)
                    - language: en-PG
                      showLanguage: English (Papua new guinea)
                    - language: en-PH
                      showLanguage: English (Philippines)
                    - language: en-PN
                      showLanguage: English (Pitcairn islands)
                    - language: en-PR
                      showLanguage: English (Puerto_Rico)
                    - language: en-VC
                      showLanguage: English (Saint vincentaldthe)
                    - language: en-SH
                      showLanguage: English (Sainthelena)
                    - language: en-KN
                      showLanguage: English (Saintkittsandnevis)
                    - language: en-LC
                      showLanguage: English (Saintlucia)
                    - language: en-SL
                      showLanguage: English (Sierraleone)
                    - language: en-SG
                      showLanguage: English (Singapore)
                    - language: en-SI
                      showLanguage: English (Slovenia)
                    - language: en-SB
                      showLanguage: English (Solomon islands)
                    - language: en-ZA
                      showLanguage: English (South Africa)
                    - language: en-SS
                      showLanguage: English (South Sudan)
                    - language: en-SZ
                      showLanguage: English (Swaziland)
                    - language: en-SE
                      showLanguage: English (Sweden)
                    - language: en-CH
                      showLanguage: English (Switzerland)
                    - language: en-TZ
                      showLanguage: English (Tanzania)
                    - language: en-TK
                      showLanguage: English (Tokelau)
                    - language: en-TT
                      showLanguage: English (Trinid adand tobago)
                    - language: en-TC
                      showLanguage: English (Turksand caicos islands)
                    - language: en-TV
                      showLanguage: English (Tuvalu)
                    - language: en-UG
                      showLanguage: English (Uganda)
                    - language: en-GB
                      showLanguage: English (United Kingdom)
                    - language: en-US
                      showLanguage: English (United States)
                    - language: en-VI
                      showLanguage: English (United states virgin islands)
                    - language: en-UM
                      showLanguage: English (United statesmiscella)
                    - language: en-VU
                      showLanguage: English (Vanuatu)
                    - language: en-WS
                      showLanguage: English (Western samoa)
                    - language: en-ZM
                      showLanguage: English (Zambia)
                    - language: en-ZW
                      showLanguage: English (Zimbabwe)
                    - language: en-AS
                      showLanguage: English (americansamoa)
                    - language: et-EE
                      showLanguage: Estonian (Estonia)
                    - language: ee-GH
                      showLanguage: Ewe (Ghana)
                    - language: ewo-CM
                      showLanguage: Ewondo (Cameroon)
                    - language: fo-DK
                      showLanguage: Faroese (Denmark)
                    - language: fo-FO
                      showLanguage: Faroese (Faroe Islands)
                    - language: fil-PH
                      showLanguage: Filipino (Philippines)
                    - language: fi-FI
                      showLanguage: Finnish (Finland)
                    - language: fi-AX
                      showLanguage: Finnish(Åland islands)
                    - language: fr-DZ
                      showLanguage: French (Algeria)
                    - language: fr-BE
                      showLanguage: French (Belgium)
                    - language: fr-BJ
                      showLanguage: French (Benin)
                    - language: fr-BF
                      showLanguage: French (Burkinafaso)
                    - language: fr-BI
                      showLanguage: French (Burundi)
                    - language: fr-CM
                      showLanguage: French (Cameroon)
                    - language: fr-CA
                      showLanguage: French (Canada)
                    - language: fr-CF
                      showLanguage: French (Centralafrica)
                    - language: fr-TD
                      showLanguage: French (Chad)
                    - language: fr-CD
                      showLanguage: French (Congo)
                    - language: fr-CI
                      showLanguage: French (Coted ivoere)
                    - language: fr-DJ
                      showLanguage: French (Djibouti)
                    - language: fr-FR
                      showLanguage: French (France)
                    - language: fr-GF
                      showLanguage: French (French Guiana)
                    - language: fr-PF
                      showLanguage: French (French polynesia)
                    - language: fr-GA
                      showLanguage: French (Gabon)
                    - language: fr-CG
                      showLanguage: French (Gongo)
                    - language: fr-GN
                      showLanguage: French (Guinea)
                    - language: fr-HT
                      showLanguage: French (Haiti)
                    - language: fr-LU
                      showLanguage: French (Luxembourg)
                    - language: fr-GP
                      showLanguage: French (MGuadeloupe)
                    - language: fr-MG
                      showLanguage: French (Madagascar)
                    - language: fr-ML
                      showLanguage: French (Mali)
                    - language: fr-MQ
                      showLanguage: French (Martinique)
                    - language: fr-YT
                      showLanguage: French (Mayotte)
                    - language: fr-MC
                      showLanguage: French (Monaco)
                    - language: fr-MA
                      showLanguage: French (Morocco)
                    - language: fr-NC
                      showLanguage: French (New caledonia)
                    - language: fr-NE
                      showLanguage: French (Niger)
                    - language: fr-RE
                      showLanguage: French (Reunion)
                    - language: fr-SN
                      showLanguage: French (Senegal)
                    - language: fr-SC
                      showLanguage: French (Seychells)
                    - language: fr-CH
                      showLanguage: French (Switzerland)
                    - language: fr-TG
                      showLanguage: French (Togo)
                    - language: fr-WF
                      showLanguage: French (Wallis and futuna island)
                    - language: fur-IT
                      showLanguage: Friulian (Italy)
                    - language: ff-CM
                      showLanguage: Fulah (Cameroon)
                    - language: gl-ES
                      showLanguage: Galician (Spain)
                    - language: ka-GE
                      showLanguage: Georgian (Georgia)
                    - language: de-AT
                      showLanguage: German (Austria)
                    - language: de-BE
                      showLanguage: German (Belgium)
                    - language: de-DE
                      showLanguage: German (Germany)
                    - language: de-LI
                      showLanguage: German (Liechtenstein)
                    - language: de-LU
                      showLanguage: German (Luxembourg)
                    - language: de-CH
                      showLanguage: German (Switzerland)
                    - language: el-CY
                      showLanguage: Greek (Cyprus)
                    - language: el-GR
                      showLanguage: Greek (Greece)
                    - language: gu-IN
                      showLanguage: Gujarati (India)
                    - language: ha-GH
                      showLanguage: Hausa (Ghana)
                    - language: ha-NG
                      showLanguage: Hausa (Nigeria)
                    - language: haw-US
                      showLanguage: Hawaiian (United States)
                    - language: iw-IL
                      showLanguage: Hebrew (Israel)
                    - language: hi-IN
                      showLanguage: Hindi language (India)
                    - language: hu-HU
                      showLanguage: Hungarian (Hungary)
                    - language: is-IS
                      showLanguage: Icelandic (Iceland)
                    - language: ig-NG
                      showLanguage: Igbo (Nigeria)
                    - language: smn-FI
                      showLanguage: Inari Sami (Finland)
                    - language: in-ID
                      showLanguage: Indonesian (Indonesia)
                    - language: ga-IE
                      showLanguage: Irish (Ireland)
                    - language: it-IT
                      showLanguage: Italian (Italy)
                    - language: it-SM
                      showLanguage: Italian (San marion)
                    - language: it-CH
                      showLanguage: Italian (Switzerland)
                    - language: it-VA
                      showLanguage: Italian (Vatican)
                    - language: ja-JP
                      showLanguage: Japanese (Japan)
                    - language: kea-CV
                      showLanguage: Kabuverdianu (Cape Verde)
                    - language: kab-DZ
                      showLanguage: Kabyle (Algeria)
                    - language: kkj-CM
                      showLanguage: Kako (Cameroon)
                    - language: kn-IN
                      showLanguage: Kannada (India)
                    - language: kk-KZ
                      showLanguage: Kazakh (Kazakhstan)
                    - language: km-KH
                      showLanguage: Khmer (Cambodia)
                    - language: rw-RW
                      showLanguage: Kinyarwanda (Rwanda)
                    - language: kok-IN
                      showLanguage: Konkani (India)
                    - language: ko-KP
                      showLanguage: Korean (Korean)
                    - language: ko-KR
                      showLanguage: Korean (South Korea)
                    - language: lkt-US
                      showLanguage: Lakota (United States)
                    - language: lo-LA
                      showLanguage: Lao (Lao)
                    - language: lv-LV
                      showLanguage: Latvian (Latvia)
                    - language: ln-AO
                      showLanguage: Lingala (Angola)
                    - language: ln-CF
                      showLanguage: Lingala (Central African Republic)
                    - language: lt-LT
                      showLanguage: Lithuanian (Lithuania)
                    - language: dsb-DE
                      showLanguage: Lower Sorbian (Germany)
                    - language: lb-LU
                      showLanguage: Luxembourg (Luxembourg)
                    - language: mk-MK
                      showLanguage: Macedonian (Macedonia)
                    - language: ms-BN
                      showLanguage: Malay (Brunei)
                    - language: ms-MY
                      showLanguage: Malay (Malaysia)
                    - language: ms-SG
                      showLanguage: Malay (Singapore)
                    - language: ml-IN
                      showLanguage: Malayalam (India)
                    - language: mt-MT
                      showLanguage: Maltese (Malta)
                    - language: mr-IN
                      showLanguage: Marathi (India)
                    - language: mgo-CM
                      showLanguage: Meta (Cameroon)
                    - language: mn-MN
                      showLanguage: Mongolian (Mongolia)
                    - language: ne-IN
                      showLanguage: Nepali (India)
                    - language: ne-NP
                      showLanguage: Nepali (Nepal)
                    - language: nnh-CM
                      showLanguage: Ngiemboon (Cameroon)
                    - language: jgo-CM
                      showLanguage: Ngomba (Cameroon)
                    - language: se-FI
                      showLanguage: Northern Sami (Finland)
                    - language: se-NO
                      showLanguage: Northern Sami (Norway)
                    - language: se-SE
                      showLanguage: Northern Sami (Sweden)
                    - language: no-NO
                      showLanguage: Norwegian (Norway)
                    - language: nb-NO
                      showLanguage: Norwegian Bokmål (Norway)
                    - language: nn-NO
                      showLanguage: Norwegian Nynorsk (Norway)
                    - language: or-IN
                      showLanguage: Oriya (India)
                    - language: os-GE
                      showLanguage: Ossetic (Georgia)
                    - language: os-RU
                      showLanguage: Ossetic (Russia)
                    - language: ps-AF
                      showLanguage: Pashto (Afghanistan)
                    - language: fa-AF
                      showLanguage: Persian (Afghanistan)
                    - language: fa-IR
                      showLanguage: Persian (Iran)
                    - language: pl-PL
                      showLanguage: Polish (Poland)
                    - language: pt-AO
                      showLanguage: Portuguese (Angola)
                    - language: pt-BR
                      showLanguage: Portuguese (Brazil)
                    - language: pt-CV
                      showLanguage: Portuguese (Cape verde)
                    - language: pt-TL
                      showLanguage: Portuguese (East timor)
                    - language: pt-GW
                      showLanguage: Portuguese (Guine-bissau)
                    - language: pt-MZ
                      showLanguage: Portuguese (Mozambique)
                    - language: pt-PT
                      showLanguage: Portuguese (Portugal)
                    - language: pt-ST
                      showLanguage: Portuguese (Sao tomeand principe)
                    - language: pa-Guru-IN
                      showLanguage: Punjabi (Gurmukhi,India)
                    - language: qu-BO
                      showLanguage: Quechua (Bolivia)
                    - language: qu-EC
                      showLanguage: Quechua (Ecuador)
                    - language: qu-PE
                      showLanguage: Quechua (Peru)
                    - language: ro-RO
                      showLanguage: Romanian (Romania)
                    - language: rm-CH
                      showLanguage: Romansh (Switzerland)
                    - language: rn-BI
                      showLanguage: Rundi (Burundi)
                    - language: ru-BY
                      showLanguage: Russian (Belarus)
                    - language: ru-KZ
                      showLanguage: Russian (Kazakhstan)
                    - language: ru-KG
                      showLanguage: Russian (Kyrgyzstan)
                    - language: ru-RU
                      showLanguage: Russian (Russia)
                    - language: ru-TJ
                      showLanguage: Russian (Tajikistan)
                    - language: sah-RU
                      showLanguage: Sakha (Russia)
                    - language: sg-CF
                      showLanguage: Sango (Central African Republic)
                    - language: gd-GB
                      showLanguage: Scottish Gaelic (United Kingdom)
                    - language: sr-RS
                      showLanguage: Serbian(Serbia)
                    - language: si-LK
                      showLanguage: Sinhalese (Sri Lanka)
                    - language: sk-SK
                      showLanguage: Slovak (Slovakia)
                    - language: sl-SI
                      showLanguage: Slovenian (Slovenia)
                    - language: so-DJ
                      showLanguage: Somali (Djibouti)
                    - language: es-AR
                      showLanguage: Spanish (Argentina)
                    - language: es-BO
                      showLanguage: Spanish (Bolivia)
                    - language: es-CL
                      showLanguage: Spanish (Chile)
                    - language: es-CO
                      showLanguage: Spanish (Colombia)
                    - language: es-CR
                      showLanguage: Spanish (Costa Rica)
                    - language: es-CU
                      showLanguage: Spanish (Cuba)
                    - language: es-DO
                      showLanguage: Spanish (Dominican republic)
                    - language: es-EC
                      showLanguage: Spanish (Ecuador)
                    - language: es-SV
                      showLanguage: Spanish (El_Salvador)
                    - language: es-GQ
                      showLanguage: Spanish (Equatorial guinea)
                    - language: es-GT
                      showLanguage: Spanish (Guatemala)
                    - language: es-HN
                      showLanguage: Spanish (Honduras)
                    - language: es-MX
                      showLanguage: Spanish (Mexico)
                    - language: es-NI
                      showLanguage: Spanish (Nicaragua)
                    - language: es-PA
                      showLanguage: Spanish (Panama)
                    - language: es-PY
                      showLanguage: Spanish (Paraguay)
                    - language: es-PE
                      showLanguage: Spanish (Peru)
                    - language: es-PH
                      showLanguage: Spanish (Philippines)
                    - language: es-ES
                      showLanguage: Spanish (Spain)
                    - language: es-US
                      showLanguage: Spanish (United States)
                    - language: es-UY
                      showLanguage: Spanish (Uruguay)
                    - language: es-VE
                      showLanguage: Spanish (Venezuela)
                    - language: zgh-MA
                      showLanguage: Standard Moroccan Tamazight (Morocco)
                    - language: sw-KE
                      showLanguage: Swahili (Kenya)
                    - language: sv-FI
                      showLanguage: Swedish (Finland)
                    - language: sv-SE
                      showLanguage: Swedish (Sweden)
                    - language: sv-AX
                      showLanguage: Swedish (Åland Islands)
                    - language: gsw-FR
                      showLanguage: Swiss German (France)
                    - language: gsw-CH
                      showLanguage: Swiss German (Switzerland)
                    - language: ta-IN
                      showLanguage: Tamil (India)
                    - language: ta-MY
                      showLanguage: Tamil (Malaysia)
                    - language: ta-SG
                      showLanguage: Tamil (Singapore)
                    - language: ta-LK
                      showLanguage: Tamil (Sri Lanka)
                    - language: te-IN
                      showLanguage: Telugu (India)
                    - language: th-TH
                      showLanguage: Thai (Thailand)
                    - language: bo-IN
                      showLanguage: Tibetan (India)
                    - language: to-TO
                      showLanguage: Tongan (Tonga)
                    - language: tr-CY
                      showLanguage: Turkish (Cyprus)
                    - language: tr-TR
                      showLanguage: Turkish (Turkey)
                    - language: tk-TM
                      showLanguage: Turkmen (Turkmenistan)
                    - language: uk-UA
                      showLanguage: Ukrainian (Ukraine)
                    - language: hsb-DE
                      showLanguage: Upper Sorbian (Germany)
                    - language: ur-IN
                      showLanguage: Urdu (India)
                    - language: ur-PK
                      showLanguage: Urdu (Pakistan)
                    - language: ug-CN
                      showLanguage: Uyghur (China)
                    - language: uz-UZ
                      showLanguage: Uzbek (Uzbekistan)
                    - language: vi-VN
                      showLanguage: Vietnamese (Vietnam)
                    - language: wae-CH
                      showLanguage: Walser (Switzerland)
                    - language: cy-GB
                      showLanguage: Welsh (United Kingdom)
                    - language: fy-NL
                      showLanguage: Western Frisian (Netherlands)
                    - language: yav-CM
                      showLanguage: Yangben (Cameroon)
                    - language: yo-BJ
                      showLanguage: Yoruba (Benin)
                    - language: yo-NG
                      showLanguage: Yoruba (Nigeria)
                    - language: zu-ZA
                      showLanguage: Zulu (South Africa)
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052341-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 批量开启云手机 `POST /api/v1/cloud-mobile/power-on-mobile`

批量开启云手机

来源：https://api-docs.hubstudio.cn/380052342e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/power-on-mobile:
    post:
      summary: 批量开启云手机
      deprecated: false
      description: |-
        批量开启云手机
        ```text
        headless字段  客户端3.50.0及以上版本支持
        ```
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                mobileIds:
                  type: array
                  items:
                    type: string
                  description: 限制20个ID
                  title: 云手机ID列表
                headless:
                  type: boolean
                  description: 默认值为true，传false显示云手机界面，true 则不显示
                  title: 是否显示云手机界面
              required:
                - mobileIds
              x-apifox-orders:
                - mobileIds
                - headless
            example:
              mobileIds:
                - '91'
                - '79'
                - '2'
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: string
                  data:
                    type: object
                    properties:
                      failResult:
                        type: string
                        description: 失败结果显示。不为空时展示
                        nullable: true
                      successCount:
                        type: integer
                        description: 成功开机数
                        nullable: true
                    x-apifox-orders:
                      - failResult
                      - successCount
                    title: 业务数据
                  msg:
                    type: string
                    title: 响应消息
                    nullable: true
                x-apifox-orders:
                  - code
                  - data
                  - msg
              example:
                code: ''
                data:
                  failResult: ''
                  successCount: 0
                msg: ''
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052342-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 批量关闭云手机 `POST /api/v1/cloud-mobile/shutdown-mobile`

批量关闭云手机

来源：https://api-docs.hubstudio.cn/380052343e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/shutdown-mobile:
    post:
      summary: 批量关闭云手机
      deprecated: false
      description: 批量关闭云手机
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                check:
                  type: boolean
                  description: rue-开启使用验证，false-不验证，默认值true
                  title: 强制关闭正在使用中的云手机
                mobileIds:
                  type: array
                  items:
                    type: string
                  description: 限制20个ID
                  title: 云手机ID列表
              required:
                - mobileIds
              x-apifox-orders:
                - check
                - mobileIds
            example:
              check: true
              mobileIds:
                - '121515'
                - '454515'
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    description: 0表示成功，其余为失败
                    type: string
                    title: 业务状态码
                  data:
                    type: array
                    items:
                      type: object
                      properties:
                        errorMsg:
                          type: string
                          title: 失败信息
                        mobileId:
                          type: integer
                          title: 云手机ID
                        mobileName:
                          type: string
                          title: 云手机名称
                        status:
                          description: 0-失败，1-成功
                          type: integer
                          title: 操作状态
                      x-apifox-orders:
                        - errorMsg
                        - mobileId
                        - mobileName
                        - status
                    title: 业务数据
                  msg:
                    type: string
                    title: 响应消息
                x-apifox-orders:
                  - code
                  - data
                  - msg
              example:
                code: ''
                data:
                  - errorMsg: ''
                    mobileId: 0
                    mobileName: ''
                    status: 0
                msg: ''
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052343-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 更新代理 `POST /api/v1/cloud-mobile/update-proxy`

更新云手机代理

来源：https://api-docs.hubstudio.cn/380052344e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/update-proxy:
    post:
      summary: ' 更新代理'
      deprecated: false
      description: 更新云手机代理
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                asDynamicType:
                  description: 1-静态，2-动态，默认1
                  type: integer
                  title: 是否动态（网络设置）
                automaticPositioning:
                  description: 默认true
                  type: boolean
                  title: 是否自动定位（定位设置）
                country:
                  type: string
                  title: 国家code（时区/语言设置）
                dnsStrategy:
                  description: 0-跟随IP，1-DNS保护。默认0
                  type: integer
                  title: DNS策略（网络设置）
                followIp:
                  description: 默认true
                  type: boolean
                  title: 跟随ip（时区/语言设置）
                lat:
                  type: integer
                  title: 维度（定位设置）
                lng:
                  type: integer
                  title: 经度（定位设置）
                mobileId:
                  type: integer
                  title: 云手机ID
                proxyAccount:
                  type: string
                  title: 代理账号（网络设置）
                proxyHost:
                  type: string
                  title: 代理主机地址（网络设置）
                proxyPassword:
                  type: string
                  title: 代理密码（网络设置）
                proxyPort:
                  type: integer
                  title: 代理端口（网络设置）
                proxyTypeId:
                  description: >-
                    1-HTTP，2-HTTPS，4-Socks5，5-Oxylabsauto，6-Lumauto，7-Luminati，11-smartproxy
                  type: integer
                  title: 代理类型（网络设置）
                proxyTypeId2:
                  description: proxyTypeId = 7时可设置 1-HTTP，2-HTTPS，4-Socks5。默认1
                  type: integer
                  title: 代理类型2（网络设置）
                referenceCity:
                  type: string
                  title: 参考城市（网络设置）
                referenceCountryCode:
                  type: string
                  title: 参考国家code（网络设置）
                referenceRegionCode:
                  type: string
                  title: 参考州code（网络设置）
                timeZone:
                  type: string
                  title: 时区（时区/语言设置）
                ysjLanguage:
                  type: string
                  title: 语言（时区/语言设置）
                ipDatabaseChannel:
                  type: integer
                  title: 代理检测渠道
                  description: 1-IP2Location 2-DB-IP 3-MaxMind
              required:
                - proxyPort
                - proxyTypeId
                - mobileId
                - proxyHost
              x-apifox-orders:
                - asDynamicType
                - automaticPositioning
                - country
                - dnsStrategy
                - followIp
                - lat
                - lng
                - mobileId
                - proxyAccount
                - proxyHost
                - proxyPassword
                - proxyPort
                - proxyTypeId
                - proxyTypeId2
                - referenceCity
                - referenceCountryCode
                - referenceRegionCode
                - timeZone
                - ysjLanguage
                - ipDatabaseChannel
            example:
              asDynamicType: 0
              automaticPositioning: true
              country: ''
              dnsStrategy: 0
              followIp: true
              lat: 0
              lng: 0
              mobileId: 0
              proxyAccount: ''
              proxyHost: ''
              proxyPassword: ''
              proxyPort: 0
              proxyTypeId: 0
              proxyTypeId2: 0
              referenceCity: ''
              referenceCountryCode: ''
              referenceRegionCode: ''
              timeZone: ''
              ysjLanguage: ''
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    description: 0表示成功，其余为失败
                    title: 业务状态码
                  data:
                    type: boolean
                    title: 业务数据载体
                  msg:
                    type: string
                    title: 响应消息
                x-apifox-orders:
                  - code
                  - data
                  - msg
              example:
                code: ''
                data: true
                msg: ''
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052344-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 批量获取云手机ADB状态 `POST /api/v1/cloud-mobile/list-adb`

批量获取云手机的ADB状态

来源：https://api-docs.hubstudio.cn/380052345e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/list-adb:
    post:
      summary: 批量获取云手机ADB状态
      deprecated: false
      description: 批量获取云手机的ADB状态
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                mobileIds:
                  type: array
                  items:
                    type: string
                  description: 限制20个ID
                  title: 云手机ID列表
              required:
                - mobileIds
              x-apifox-orders:
                - mobileIds
            examples: {}
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: string
                  data:
                    type: array
                    items:
                      type: object
                      properties:
                        adbIp:
                          type: string
                          title: adb的ip
                          nullable: true
                        adbPassword:
                          type: string
                          title: adb连接密码
                          nullable: true
                        adbPort:
                          type: string
                          title: adb连接端口
                          nullable: true
                        mobileId:
                          type: integer
                          title: 云手机id
                          nullable: true
                        remark:
                          type: string
                          title: 获取失败备注信息
                          nullable: true
                        success:
                          type: integer
                          description: 1=是  0=否
                          title: 是否成功获取
                          nullable: true
                      x-apifox-orders:
                        - adbIp
                        - adbPassword
                        - adbPort
                        - mobileId
                        - remark
                        - success
                      nullable: true
                    title: 业务数据载体
                    nullable: true
                  msg:
                    type: string
                    title: 响应消息
                    nullable: true
                x-apifox-orders:
                  - code
                  - data
                  - msg
                nullable: true
              example:
                code: ''
                data:
                  - adbIp: ''
                    adbPassword: ''
                    adbPort: ''
                    mobileId: 0
                    remark: ''
                    success: 0
                msg: ''
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052345-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 批量更新云手机ADB状态 `POST /api/v1/cloud-mobile/batch-update-adb`

批量更新云手机的ADB状态

来源：https://api-docs.hubstudio.cn/380052346e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/batch-update-adb:
    post:
      summary: 批量更新云手机ADB状态
      deprecated: false
      description: 批量更新云手机的ADB状态
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                enableAdb:
                  type: boolean
                  description: true-开启，false-关闭
                  title: 是否开启adb
                mobileIds:
                  type: array
                  items:
                    type: string
                  description: 限制20个ID
                  title: 云手机ID列表
              required:
                - enableAdb
                - mobileIds
              x-apifox-orders:
                - enableAdb
                - mobileIds
            example:
              enableAdb: false
              mobileIds:
                - '19'
                - '21'
                - '35'
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    description: 0表示成功，其余为失败
                    title: 业务状态码
                    nullable: true
                  data:
                    type: array
                    items:
                      type: object
                      properties:
                        mobileId:
                          type: integer
                          title: 云手机id
                          nullable: true
                        remark:
                          type: string
                          title: 获取失败备注信息
                          nullable: true
                        success:
                          type: integer
                          description: '  1=是  0=否'
                          title: 是否成功获取
                          nullable: true
                      x-apifox-orders:
                        - mobileId
                        - remark
                        - success
                    title: 业务数据载体
                    nullable: true
                  msg:
                    type: string
                    title: 响应消息
                    nullable: true
                x-apifox-orders:
                  - code
                  - data
                  - msg
              example:
                code: ''
                data:
                  - mobileId: 0
                    remark: ''
                    success: 0
                msg: ''
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052346-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 一键新机 `POST /api/v1/cloud-mobile/new-machine`

执行云手机一键新机操作

来源：https://api-docs.hubstudio.cn/380052347e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/new-machine:
    post:
      summary: 一键新机
      deprecated: false
      description: 执行云手机一键新机操作
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                mobileId:
                  type: integer
                  title: 云手机ID
                brand:
                  type: string
                  title: 品牌
                  description: 通过/api/v1/cloud-mobile/brand/models获得
                model:
                  type: string
                  title: 机型
                  description: 通过/api/v1/cloud-mobile/brand/models获得
              x-apifox-orders:
                - mobileId
                - brand
                - model
              required:
                - mobileId
            example:
              mobileId: 53
              brand: deserunt
              model: aute culpa et
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    description: 0表示成功，其余为失败
                    type: string
                    title: 业务状态码
                  data:
                    type: boolean
                    title: 业务数据载体
                  msg:
                    type: string
                    title: 响应消息
                x-apifox-orders:
                  - code
                  - data
                  - msg
              example:
                code: ''
                data: true
                msg: ''
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052347-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 获取一键新机状态及可用数量 `POST /api/v1/cloud-mobile/new-machine-status`

获取云手机一键新机的状态及可用数量

来源：https://api-docs.hubstudio.cn/380052348e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/new-machine-status:
    post:
      summary: 获取一键新机状态及可用数量
      deprecated: false
      description: 获取云手机一键新机的状态及可用数量
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                mobileId:
                  type: integer
                  title: 云手机ID
              required:
                - mobileId
              x-apifox-orders:
                - mobileId
            examples: {}
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: string
                    description: 0表示成功，其余为失败
                    title: 业务状态码
                  data:
                    type: object
                    properties:
                      availableQuantity:
                        type: integer
                        title: 当前周期一键新机可用次数
                        nullable: true
                      mobileId:
                        type: integer
                        title: 云手机id
                        nullable: true
                      status:
                        type: boolean
                        description: ' false-关闭 true-开启'
                        title: 一键新机状态
                        nullable: true
                      totalQuantity:
                        type: integer
                        title: 当前周期一键新机总次数
                        nullable: true
                    x-apifox-orders:
                      - availableQuantity
                      - mobileId
                      - status
                      - totalQuantity
                    title: 业务数据载体
                  msg:
                    type: string
                    title: 响应消息
                x-apifox-orders:
                  - code
                  - data
                  - msg
              example:
                code: ''
                data:
                  availableQuantity: 0
                  mobileId: 0
                  status: true
                  totalQuantity: 0
                msg: ''
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052348-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 查询品牌机型 `POST /api/v1/cloud-mobile/brand/models`

查询对应安卓版本的可用品牌机型

来源：https://api-docs.hubstudio.cn/380052351e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/brand/models:
    post:
      summary: 查询品牌机型
      deprecated: false
      description: 查询对应安卓版本的可用品牌机型
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                productId:
                  type: integer
                  title: 云手机商品ID
              required:
                - productId
              x-apifox-orders:
                - productId
            example: "{\r\n  \"productId\": 0 // 云手机商品ID\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  msg:
                    type: string
                    title: 响应消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  data:
                    type: array
                    items:
                      type: object
                      properties:
                        brand:
                          type: string
                          title: 品牌
                        model:
                          type: string
                          title: 机型
                      x-apifox-orders:
                        - brand
                        - model
                    title: 业务数据载体
                x-apifox-orders:
                  - code
                  - msg
                  - data
                  - requestId
                  - timestamp
              example:
                code: 0
                msg: Success
                requestId: 07ece6731a5e4450b9dee2fdb09cb6ee
                timestamp: 1742289013309
                data:
                  - brand: brandName
                    model: modelName
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052351-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 执行shell命令 `POST /api/v1/cloud-mobile/exe-command`

云手机执行shell命令

来源：https://api-docs.hubstudio.cn/380052359e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/exe-command:
    post:
      summary: 执行shell命令
      deprecated: false
      description: 云手机执行shell命令
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                mobileId:
                  type: integer
                  title: 云手机ID
                command:
                  description: 如果是需要执行多行,使用;号隔开即可
                  type: string
                  title: 命令
                01KAJYBK6R36JW8F2K4M6F3MGX:
                  type: string
              required:
                - mobileId
                - command
                - 01KAJYBK6R36JW8F2K4M6F3MGX
              x-apifox-orders:
                - 01KAJYBK6R36JW8F2K4M6F3MGX
                - mobileId
                - command
            example: "{\r\n  \"mobileId\": 0, // 云手机ID\r\n  \"command\": \"\" // 命令，如果是需要执行多行,使用;号隔开即可\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                    description: |
                      0表示成功，其余为失败
                  msg:
                    type: string
                    title: 响应消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  data:
                    type: boolean
                    title: 业务数据载体
                x-apifox-orders:
                  - code
                  - msg
                  - requestId
                  - timestamp
                  - data
              example:
                code: 0
                msg: Success
                requestId: '0'
                timestamp: 1742289013309
                data: true
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052359-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 修改云手机信息 `POST /api/v1/cloud-mobile/update`

修改云手机名称、备注、序号

来源：https://api-docs.hubstudio.cn/380052360e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/update:
    post:
      summary: 修改云手机信息
      deprecated: false
      description: 修改云手机名称、备注、序号
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                mobileId:
                  type: integer
                  title: 云手机ID
                name:
                  type: string
                  description: 不超过60个字符
                  title: 云手机名称
                ignore:
                  type: boolean
                  description: 默认：false
                  title: 是否忽略云手机名称重复
                remark:
                  type: string
                  description: 不超过500个字符
                  title: 备注
                number:
                  type: integer
                  description: 最大值不超过 999999
                  title: 序号
              required:
                - mobileId
              x-apifox-orders:
                - mobileId
                - name
                - ignore
                - remark
                - number
            example:
              mobileId: 0
              name: 云手机名称
              remark: 云手机备注
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                    description: 0表示成功，其余为失败
                  msg:
                    type: string
                    title: 响应消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  data:
                    type: boolean
                    title: 业务数据
                x-apifox-orders:
                  - code
                  - msg
                  - requestId
                  - timestamp
                  - data
              example:
                code: 0
                msg: Success
                requestId: '0'
                timestamp: 1742289013309
                data: true
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052360-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 发送短信到云手机 `POST /api/v1/cloud-mobile/simulateSendSms`

发送短信到云手机

来源：https://api-docs.hubstudio.cn/395416906e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/simulateSendSms:
    post:
      summary: 发送短信到云手机
      deprecated: false
      description: 发送短信到云手机
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                mobileId:
                  description: 支持机型：Android13、支持Android14、支持Android15A
                  type: integer
                  title: 云手机ID
                senderNumber:
                  description: （最长21位，允许数字+空格）
                  type: string
                  title: 发送方号码
                smsContent:
                  description: （长度限制127位）
                  type: string
                  title: 短信内容
              required:
                - mobileId
                - senderNumber
                - smsContent
              x-apifox-orders:
                - mobileId
                - senderNumber
                - smsContent
            example: |-
              {
                "mobileId": 0,  // 云手机ID,支持机型：Android13、支持Android14、支持Android15A
                "senderNumber": "", // 发送方号码（最长21位，允许数字+空格）
                "smsContent": "" // 短信内容（长度限制127位）
              }
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                    description: 0表示成功，其余为失败
                  msg:
                    type: string
                    title: 响应消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  data:
                    type: boolean
                    title: 业务数据载体
                x-apifox-orders:
                  - code
                  - msg
                  - requestId
                  - timestamp
                  - data
              example:
                code: 0
                msg: Success
                requestId: '0'
                timestamp: 1742289013309
                data: true
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-395416906-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 批量删除云手机 `POST /api/v1/cloud-mobile/del-mobile-batch`

批量删除云手机

来源：https://api-docs.hubstudio.cn/387838493e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/del-mobile-batch:
    post:
      summary: 批量删除云手机
      deprecated: false
      description: 批量删除云手机
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                mobileIds:
                  type: array
                  items:
                    type: string
                  title: 云手机列表
              required:
                - mobileIds
              x-apifox-orders:
                - mobileIds
            example: "{\r\n  \"mobileIds\": [1400789] // 云手机id\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                    description: 0表示成功，其余为失败
                  msg:
                    type: string
                    title: 响应消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  data:
                    type: boolean
                    title: 业务数据载体
                x-apifox-orders:
                  - code
                  - msg
                  - requestId
                  - timestamp
                  - data
              example:
                code: 0
                msg: Success
                requestId: 82f4e879b27831abd84ccf29ebc55789
                timestamp: 1765181498244
                data: true
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-387838493-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 批量修改云手机分组 `POST /api/v1/cloud-mobile/set-tag`

批量设置云手机的分组名

来源：https://api-docs.hubstudio.cn/387998970e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/cloud-mobile/set-tag:
    post:
      summary: 批量修改云手机分组
      deprecated: false
      description: 批量设置云手机的分组名
      tags:
        - 云手机
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                mobileIds:
                  type: array
                  items:
                    type: string
                  title: 云手机列表
                tagName:
                  type: string
                  title: 分组名称
              required:
                - mobileIds
                - tagName
              x-apifox-orders:
                - mobileIds
                - tagName
            example: "{\r\n  \"mobileIds\": [1400802],  // 云手机列表\r\n  \"tagName\": \"分组名称\"   // 分组名称\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                    description: 0表示成功，其余为失败
                  msg:
                    type: string
                    title: 响应消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  data:
                    type: boolean
                    title: 业务数据载体
                x-apifox-orders:
                  - code
                  - msg
                  - requestId
                  - timestamp
                  - data
              example:
                code: 0
                msg: Success
                requestId: ddabf46578af95727847f6b36f74f33a
                timestamp: 1765185363168
                data: true
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 云手机
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-387998970-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```


### 平台账号管理

#### 账号分页列表 `POST /api/v1/account/list`

- 查询平台账号的信息。用户仅能查询自己有权限的平台账号信息

来源：https://api-docs.hubstudio.cn/380052371e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/account/list:
    post:
      summary: 账号分页列表
      deprecated: false
      description: |-
        - 查询平台账号的信息。用户仅能查询自己有权限的平台账号信息
        - 请求体可以所有字段都为空，但是必须传 {}，不能是空请求体
        - 如果传空字符串，该字段不会进行过滤查询
        - 需要具备"团队设置-编辑-环境-我的账号-密码查看"权限才能返回账号密码信息，没有此权限则返回null
        需要具备"团队设置-编辑-环境-我的账号-密码查看"权限才能返回账号密码信息，没有此权限则返回null。
      tags:
        - 平台账号管理
        - 平台账号管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                accountName:
                  type: string
                  title: 账号
                current:
                  type: integer
                  title: 当前页
                name:
                  type: string
                  title: 账号名称
                  description: 对该账号的描述，传null和空字段等于没有过滤该字段
                size:
                  type: integer
                  title: 每页数据
              x-apifox-orders:
                - accountName
                - current
                - name
                - size
            example:
              accountName: username
              current: 1
              name: 自定义名称
              size: 15
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  msg:
                    type: string
                    title: 响应消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  data:
                    type: object
                    title: 业务数据载体
                    properties:
                      list:
                        type: array
                        items:
                          type: object
                          properties:
                            accountId:
                              type: integer
                              title: 账号ID
                            name:
                              type: string
                              title: 账号名称
                              description: 对该账号的描述
                            accountName:
                              type: string
                              title: 账号
                              description: 网站登录的账号
                            accountPassword:
                              type: string
                              description: 网站登录的密码
                              title: 账号密码
                            otpSecret:
                              type: string
                              title: 2FA密钥
                            siteName:
                              type: string
                              title: 平台名称
                              description: 内置的平台名称
                            siteAlias:
                              type: 'null'
                              title: 平台别名
                              description: 对该账号的描述
                            domainName:
                              type: 'null'
                              title: 域名
                              description: 显示自定义平台对应的域名
                          x-apifox-orders:
                            - accountId
                            - name
                            - accountName
                            - accountPassword
                            - otpSecret
                            - siteName
                            - siteAlias
                            - domainName
                    x-apifox-orders:
                      - list
                x-apifox-orders:
                  - code
                  - msg
                  - requestId
                  - timestamp
                  - data
              example:
                code: 0
                msg: Success
                requestId: 1c68583789ab472a8fab783e5a5485f1
                timestamp: 1756094388606
                data:
                  list:
                    - accountId: 1
                      name: name
                      accountName: accountName
                      accountPassword: accountPassword
                      otpSecret: ''
                      siteName: siteName
                      siteAlias: null
                      domainName: null
                  total: 1
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 平台账号管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052371-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 账号更新 `POST /api/v1/account/update`

修改账号信息

来源：https://api-docs.hubstudio.cn/380052372e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/account/update:
    post:
      summary: 账号更新
      deprecated: false
      description: |-
        修改账号信息
        ```text
        注意：
        - accountId 为必传参数。
        - accountPassword, name, otpSecret 三个参数中必须至少有一个，可以多个。
        - 如果给某个可选参数传递空字符串 (如 "name": "")，则会清空数据库中该字段对应的数据。
        - 如果不传递某个可选参数 (例如不传 name 字段)，则不对该字段进行任何修改。
        ```
      tags:
        - 平台账号管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                accountId:
                  type: integer
                  title: 账号id，通过账号列表查询
                accountPassword:
                  type: string
                  title: 账号密码
                  description: accountPassword, name, otpSecret 三个参数中必须至少有一个，可以多个
                name:
                  type: string
                  title: 自定义的账号名称
                  description: accountPassword, name, otpSecret 三个参数中必须至少有一个，可以多个
                otpSecret:
                  type: string
                  title: 2FA密钥
                  description: accountPassword, name, otpSecret 三个参数中必须至少有一个，可以多个
              x-apifox-orders:
                - accountId
                - accountPassword
                - name
                - otpSecret
            example:
              accountId: 1
              accountPassword: password
              name: 空影力华
              otpSecret: ea irure esse Excepteur
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  msg:
                    type: string
                    title: 响应消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  data:
                    type: boolean
                    title: 业务数据载体
                x-apifox-orders:
                  - code
                  - msg
                  - requestId
                  - timestamp
                  - data
              example:
                code: 0
                msg: Success
                requestId: 10f1569c234847cc82ed91c421a406b5
                timestamp: 1756094713083
                data: true
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 平台账号管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052372-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 添加环境账号 `POST /api/v1/container/add-account`

为环境添加账号信息

来源：https://api-docs.hubstudio.cn/380052370e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/container/add-account:
    post:
      summary: 添加环境账号
      deprecated: false
      description: >-
        为环境添加账号信息

        ```text

        注意：

        - 当 siteName 为 "自定义平台" 时，系统会判断此为自定义平台。如果 domainName 相同，则不会新增账号记录。

        - 当 siteName 为官方内置平台名称（如“加拿大亚马逊”）时，系统会判断此为内置平台。需要平台名称 (siteName) 和平台账号
        (accountName) 都相同，才不会被添加；如果其中任何一个不同，都会添加一条新的账号记录。

        ```
      tags:
        - 平台账号管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                accountName:
                  type: string
                  title: 平台账号
                accountPassword:
                  type: string
                  title: 平台密码
                containerCode:
                  type: integer
                  title: 环境编号
                domainName:
                  type: string
                  title: 自定义平台的域名
                name:
                  type: string
                  title: 自定义账号名称
                otpSecret:
                  type: string
                  title: 2FA密钥
                siteAlias:
                  type: string
                  title: 自定义平台的别名
                siteName:
                  type: string
                  title: 平台名称
              required:
                - accountName
                - siteName
                - accountPassword
                - containerCode
              x-apifox-orders:
                - accountName
                - accountPassword
                - containerCode
                - domainName
                - name
                - otpSecret
                - siteAlias
                - siteName
            example: "{\r\n    \"accountName\": \"zhanghao\",  // 平台账号\r\n\r\n    \"accountPassword\": \"password\",  // 平台密码\r\n\r\n    \"containerCode\": 854921712,  // 环境编号\r\n\r\n    \"domainName\": \"https://www.baidu.com\",  // 自定义平台的域名\r\n\r\n    \"name\": \"账号名称\",  // 自定义账号名称\r\n\r\n    \"otpSecret\": \"otpSecret\",  // 2FA密钥\r\n\r\n    \"siteAlias\": \"自定义平台\",  // 自定义平台的别名\r\n\r\n    \"siteName\": \"自定义平台\"  // 平台名称\r\n\r\n}"
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  msg:
                    type: string
                    title: 响应消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  data:
                    type: boolean
                    title: 业务数据载体
                x-apifox-orders:
                  - code
                  - msg
                  - requestId
                  - timestamp
                  - data
              example:
                code: 0
                msg: Success
                requestId: 5e4694a6369b46b09c1288c5efd8b905
                timestamp: 1756097123655
                data: true
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 平台账号管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052370-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 账号删除 `POST /api/v1/account/del`

删除账号信息

来源：https://api-docs.hubstudio.cn/380052369e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/account/del:
    post:
      summary: 账号删除
      deprecated: false
      description: |-
        删除账号信息
        ```text
        - 请求体不能为空，必须包含 accountIds 字段。可以传空数组 {}，但不能完全不传请求体。
        - accountIds 数组不能为空，必须包含至少一个账号ID。
        - 可以同时传入多个账号ID进行批量删除。
        - 该操作具有原子性，如果提供的账号ID列表中有一个不存在，则整个删除操作都会失败，不会删除任何账号。
        ```
      tags:
        - 平台账号管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                accountIds:
                  type: array
                  items:
                    type: integer
                  description: 例：[1111]，可以通过/api/v1/account/list获得
                  title: 账号ID数组
              required:
                - accountIds
              x-apifox-orders:
                - accountIds
            example:
              accountIds:
                - 28842
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  code:
                    type: integer
                    title: 业务状态码
                  msg:
                    type: string
                    title: 响应消息
                  requestId:
                    type: string
                    title: 请求ID
                  timestamp:
                    type: integer
                    title: 时间戳
                  data:
                    type: boolean
                    title: 业务数据载体
                x-apifox-orders:
                  - code
                  - msg
                  - requestId
                  - timestamp
                  - data
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 平台账号管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052369-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```


### 分组管理

#### 获取环境分组列表 `POST /api/v1/group/list`

查询当前团队内的浏览器环境分组名称。查询成功返回分组名称和分组ID

来源：https://api-docs.hubstudio.cn/380052375e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/group/list:
    post:
      summary: 获取环境分组列表
      deprecated: false
      description: 查询当前团队内的浏览器环境分组名称。查询成功返回分组名称和分组ID
      tags:
        - 分组管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties: {}
              x-apifox-orders: []
            examples: {}
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  msg:
                    type: string
                    title: 响应消息
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: array
                    items:
                      type: object
                      properties:
                        tagName:
                          type: string
                          title: 分组名称
                        tagCode:
                          type: integer
                          title: 分组ID
                          nullable: true
                      required:
                        - tagName
                        - tagCode
                      x-apifox-orders:
                        - tagName
                        - tagCode
                    title: 业务数据载体
                x-apifox-orders:
                  - msg
                  - code
                  - data
              example: "{\r\n    \"msg\": \"Success\",\r\n    \"code\": 0,\r\n    \"data\": [\r\n        {\r\n            \"tagName\": \"Facebook分组\"\r\n            \"tagCode\": 123456\r\n        },\r\n        {\r\n            \"tagName\": \"广告号专组\"\r\n            \"tagCode\": 123456\r\n        },\r\n        {\r\n            \"tagName\": \"阿基的分组\"\r\n            \"tagCode\": null\r\n        }\r\n    ]\r\n}"
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 分组管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052375-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 新建环境分组 `POST /api/v1/group/create`

添加环境的分组，名称不能重复

来源：https://api-docs.hubstudio.cn/380052374e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/group/create:
    post:
      summary: 新建环境分组
      deprecated: false
      description: 添加环境的分组，名称不能重复
      tags:
        - 分组管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                tagName:
                  type: string
                  title: 环境分组名
              required:
                - tagName
              x-apifox-orders:
                - tagName
            example:
              tagName: 分组名
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  msg:
                    type: string
                    title: 响应消息
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: boolean
                    title: 业务数据载体
                x-apifox-orders:
                  - msg
                  - code
                  - data
              example:
                msg: Success
                code: 0
                data: true
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 分组管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052374-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```

#### 删除环境分组 `POST /api/v1/group/del`

删除指定名称的环境分组。删除成功后返回true

来源：https://api-docs.hubstudio.cn/380052373e0.md

## OpenAPI Specification

```yaml
openapi: 3.0.1
info:
  title: ''
  description: ''
  version: 1.0.0
paths:
  /api/v1/group/del:
    post:
      summary: 删除环境分组
      deprecated: false
      description: 删除指定名称的环境分组。删除成功后返回true
      tags:
        - 分组管理
      parameters:
        - name: Accept-Language
          in: header
          description: 默认请求语言
          example: zh-CN
          schema:
            type: string
            default: zh-CN
        - name: Authorization
          in: header
          description: Local API 开启安全校验必填，API Key来自客户端
          example: 'NULL'
          schema:
            type: string
            default: 'NULL'
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                tagCode:
                  description: 删除指定名称的分组
                  type: string
              required:
                - tagCode
              x-apifox-orders:
                - tagCode
            example:
              tagCode: '8008329'
      responses:
        '200':
          description: ''
          content:
            application/json:
              schema:
                type: object
                properties:
                  msg:
                    type: string
                    title: 响应消息
                  code:
                    type: integer
                    title: 业务状态码
                  data:
                    type: boolean
                    title: 业务数据载体
                x-apifox-orders:
                  - msg
                  - code
                  - data
          headers: {}
          x-apifox-name: 成功
      security: []
      x-apifox-folder: 分组管理
      x-apifox-status: released
      x-run-in-apifox: https://app.apifox.com/web/project/7443932/apis/api-380052373-run
components:
  schemas: {}
  securitySchemes: {}
servers:
  - url: http://127.0.0.1:6873
    description: 测试环境
security: []

```


## 附录：通用说明与部署文档


### API 使用说明文档

来源：https://api-docs.hubstudio.cn/8331450m0.md

### 1.简介

1.1. 基础API接口，支持Local API的功能，通过程序化的方式，读写环境配置信息，启动和关闭浏览器等基础功能，具体详见：[**【API接口文档】**](https://support.hubstudio.cn/0379/7beb/fbb0)  
[**【API在线调试接口文档】**](https://api-docs.hubstudio.cn/) 

1.2.可以配合Selenium、Puppeteer和playwright等自动化框架来实现浏览器操作的自动化。

1.3.接口有请求频率限制，每个接口每分钟最多请求100次


**使用前请根据以下指引完成操作并获取信息，然后参照[**【http模式】**](https://support.hubstudio.cn/0379/7beb/935c/3d48) 启动客户端并开始使用API**


### **2.获取用户凭证**
2.1.打开Hubstudio客户端，点击API-用户凭证获取 **`app_id`** 和 **`app_secret`**


![image.png](https://api.apifox.com/api/v1/projects/7443932/resources/631954/image-preview)


2.2.如需API与客户端同时运行使用，只需要通过接口直接调用API即可（仅支持Hub V3.35.0及以上客户端版本）

![image.png](https://api.apifox.com/api/v1/projects/7443932/resources/631958/image-preview)

### **3.团队code和环境ID**
3.1.Hubstudio需要 **`groupCode`** 指定操作的团队对象，groupCode可以访问**用户中心-团队信息**查看。如下图：

![image.png](https://api.apifox.com/api/v1/projects/7443932/resources/631960/image-preview)

3.2.API通过 **`containerCode`** （环境ID）打开环境，环境ID如图，也可以通过“获取环境列表接口”（`Path：/api/v1/env/list`）获取：

![image.png](https://api.apifox.com/api/v1/projects/7443932/resources/631961/image-preview)


### Linux Server 部署与自动化指南

来源：https://api-docs.hubstudio.cn/8945881m0.md

在 Ubuntu 无头服务器上部署 HubStudio 指纹浏览器，通过 Local API 实现浏览器环境的自动化管理 —— 无需桌面环境。

---

## 完成本指南后你将获得

1. 一个在 Ubuntu 24.04 Server 上运行的 HubStudio 无头实例
2. 通过 socat 配置网络转发，使外部机器可以通过 CDP（Chrome DevTools Protocol）连接浏览器
3. 一个可用的 Python 压力测试脚本，能够批量创建、启动、控制和清理浏览器环境

---

## 架构总览

```
┌──────────────────────────────────────────────────────────┐
│                    Ubuntu 24.04 Server                    │
│                                                          │
│  ┌──────────────┐    ┌───────────────────────────────┐   │
│  │   xvfb       │───▶│  HubStudio AppImage           │   │
│  │ (虚拟显示)    │    │  Local API :6873              │   │
│  │              │    │  CDP debug :<动态端口>          │   │
│  │              │    │    (127.0.0.1, 每个环境不同)    │   │
│  └──────────────┘    └───────────────────────────────┘   │
│                              │                           │
│                        socat 端口转发                     │
│                              │                           │
│                     0.0.0.0:6874  → 127.0.0.1:6873       │
│                     0.0.0.0:<N+1> → 127.0.0.1:<N>        │
└──────────────────────────────────────────────────────────┘
                               │
                          外部机器
                   (Playwright / Puppeteer / Selenium)
```

> **📝 说明：** CDP 调试端口是**动态分配**的 —— 每个浏览器环境启动后由 `/api/v1/browser/start` 接口返回各自的端口号。上图中 `<N>` 为占位符。

---

## 前置条件

| 条件 | 说明 |
|------|------|
| **操作系统** | Ubuntu 24.04 Server (x86_64) |
| **推荐配置** | 8 vCPU、8 GB RAM（支持约 5 个并发环境） |
| **网络** | 需要出站互联网访问；入站开放 `6874` 端口及所选择的 CDP 转发端口，或使用 SSH 隧道 |
| **HubStudio** | 已下载客户端（[下载地址](https://www.hubstudio.cn/download/)） |
| **Python**（可选） | Python 3.8+ 及 `pip`，用于运行自动化脚本 |

---

## Step 1 — 安装系统依赖

通过 SSH 连接到服务器，安装 HubStudio 运行所需的软件包：

```bash
# FUSE 支持（AppImage 运行必需）
sudo apt install -y libfuse2t64

# GTK / 无障碍 / 显示库
sudo apt install -y libatk1.0-0 libatk-bridge2.0-0 libatspi2.0-0
sudo apt install -y libcups2
sudo apt install -y libgtk-3-0 libgdk-pixbuf2.0-0
sudo apt install -y libgbm1 libxkbcommon0 libasound2t64

# 虚拟帧缓冲（无头显示）
sudo apt install -y xvfb

# TCP 转发工具
sudo apt install -y socat
```

### 安装字体（可选）

如果浏览器页面出现文字缺失或乱码，安装对应的字体包：

```bash
# 中日韩字体
sudo apt install -y fonts-noto-cjk fonts-noto-cjk-extra

# Emoji 及扩展字体
sudo apt install -y fonts-noto-color-emoji fonts-noto-extra
```

> 其他语言可安装对应的 [Noto 字体家族](https://fonts.google.com/noto)。

---

## Step 2 — 启动 HubStudio

### 2.1 赋予可执行权限

```bash
chmod +x ./Hubstudio
```

### 2.2 后台启动

使用 `xvfb-run` 提供虚拟显示，后台运行 HubStudio：

```bash
nohup xvfb-run -a -s "-screen 0 1920x1080x24" \
  ./Hubstudio \
  --no-sandbox \
  --headless=true \
  --disable-gpu \
  --ozone-platform=x11 \
  --app-id="${APP_ID}" \
  --app-secret="${APP_SECRET}" \
  --login-group-code="${团队ID}" \
  > hubstudio.log 2>&1 &
```

**启动参数说明：**

| 参数 | 说明 |
|------|------|
| `--no-sandbox` | 禁用沙盒模式（root 用户或容器环境下必需） |
| `--headless=true` | 无头模式运行，不显示 GUI |
| `--disable-gpu` | 禁用 GPU 加速 |
| `--ozone-platform=x11` | 指定使用 X11 显示平台 |
| `--app-id` | HubStudio 开发者中心的 APP ID |
| `--app-secret` | HubStudio 开发者中心的 APP Secret |
| `--login-group-code` | 团队 ID（从用户中心获取） |

> ⚠️ **注意：** 请将 `${APP_ID}`、`${APP_SECRET}`、`${团队ID}` 替换为实际值。获取方式见 [Step 3](#step-3--获取-hubstudio-api-凭据)。

### 2.3 验证启动

```bash
# 检查进程是否运行
ps aux | grep Hubstudio

# 查看日志
tail -f hubstudio.log
```

> 进程完全初始化可能需要 5–10 秒。

---

## Step 3 — 获取 HubStudio API 凭据

### 3.1 获取 APP ID 和 APP Secret

1. 登录 HubStudio 客户端
2. 进入顶部导航栏 **「开发者中心」**
3. 在左侧菜单选择 **「浏览器&云手机API」**
4. 在 **Local API** 区域中即可看到：
   - **接口地址**：默认为 `http://127.0.0.1:6873`
   - **APP ID**：应用标识
   - **APP Secret**：应用密钥（点击复制图标获取）

![HubStudio 开发者中心 — 从 Local API 区域复制 APP ID 和 APP Secret](images/hubstudio_app_id.png)

### 3.2 获取团队 ID

1. 点击 HubStudio 右上角头像
2. 进入 **「用户中心」**
3. 在 **「团队信息」** 区域即可看到 **团队ID**

![HubStudio 用户中心 — 在团队信息区域找到团队ID](images/hubstudio_team_id.png)

### 3.3 验证 API 可用

```bash
# 在服务器本地测试
curl -s http://127.0.0.1:6873
```

> `code` 为 `0` 的响应表示 API 已就绪。

> **✅ 检查点：** HubStudio 服务已完全启动。继续 [Step 4](#step-4--配置网络转发socat) 设置远程访问，或者如果脚本运行在同一台服务器上，可直接跳转到 [Step 5](#step-5--运行自动化脚本)。

---

## Step 4 — 配置网络转发（socat）

默认情况下，Local API（`:6873`）和 CDP 调试端口都绑定在 `127.0.0.1`。如果需要从外部机器访问（例如你的开发电脑），需要使用 `socat` 转发流量。

> ⚠️ **安全风险 — 不要将这些端口暴露到公网。**
>
> - Local API 的大部分端点**没有内置鉴权**
> - CDP 调试端口拥有浏览器的**完全远程控制权**（读取 Cookie、注入脚本、截屏）
>
> **建议：**
> - 优先使用 **SSH 隧道** 代替 socat（参见 [4.4 节](#44-推荐使用-ssh-隧道)）
> - 如果必须使用 socat，通过防火墙规则**限制到指定 IP**
> - 使用 **VPN** 或云服务商安全组限制入站流量
> - **绝不要**在公网服务器上将端口开放到 `0.0.0.0` 而不做 IP 限制

### 4.1 转发 Local API 端口

```bash
nohup socat TCP-LISTEN:6874,fork,reuseaddr,bind=0.0.0.0 TCP:127.0.0.1:6873 &
```

> 外部机器现在可以通过 `http://<服务器IP>:6874` 访问 API。

### 4.2 转发 CDP 调试端口

通过 API 启动浏览器环境后，响应中会包含一个**动态** `debuggingPort`（如 `36363`）。每个环境的端口可能不同。转发它以便外部自动化工具（Playwright、Puppeteer、Selenium）连接：

```bash
# 示例：如果 debuggingPort=36363，转发到外部端口 36364
# 请根据 /api/v1/browser/start 返回的实际端口调整
nohup socat TCP-LISTEN:36364,fork,reuseaddr,bind=0.0.0.0 TCP:127.0.0.1:36363 &
```

> ⚠️ **注意：** 同时运行多个环境时，确保转发端口不会与其他环境的调试端口冲突。建议使用较大偏移量或专用端口范围。

> 💡 **提示：** 在生产环境中，建议在每次 `/api/v1/browser/start` 调用后**动态创建** socat 转发，使用返回的 `debuggingPort`。本项目的 [Python 脚本](https://github.com/hubstudio-Max/linux-demo)中已包含该实现。

> 如果自动化脚本运行在**同一台服务器**上，可以跳过 socat，直接连接 `127.0.0.1`。

### 4.3 开放防火墙端口

如果使用 socat，建议限制到受信任的 IP：

```bash
# 仅允许指定 IP（推荐）
sudo ufw allow from <YOUR_IP> to any port 6874 proto tcp
sudo ufw allow from <YOUR_IP> to any port 36364 proto tcp

# 或允许任意 IP（生产环境不推荐）
# sudo ufw allow 6874/tcp
# sudo ufw allow 36364/tcp
```

### 4.4 推荐：使用 SSH 隧道

最安全的远程访问方式是使用 SSH 隧道 —— 无需修改防火墙或配置 socat。

由于 CDP 端口是**动态分配**的（启动环境时才确定），工作流程为：

1. **先建立 API 端口隧道：**

   ```bash
   # 在本地机器上执行
   ssh -L 6873:127.0.0.1:6873 user@<服务器IP>
   ```

2. **通过隧道启动环境**（`http://127.0.0.1:6873/api/v1/browser/start`），获取返回的 `debuggingPort`。

3. **为 CDP 端口建立第二个隧道：**

   ```bash
   # 将 <debuggingPort> 替换为 API 返回的实际端口
   ssh -L <debuggingPort>:127.0.0.1:<debuggingPort> user@<服务器IP>
   ```

4. **连接** `http://127.0.0.1:<debuggingPort>` —— SSH 让远程端口看起来像本地端口。

> 💡 **提示：** 如果预先知道端口范围，可以在一条命令中合并多个隧道：
>
> ```bash
> ssh -L 6873:127.0.0.1:6873 -L 36363:127.0.0.1:36363 -L 36364:127.0.0.1:36364 user@<服务器IP>
> ```

---

## Step 5 — 运行自动化脚本

### 5.1 安装 Python 依赖

```bash
pip install requests playwright paramiko
```

> Playwright 仅用于其 CDP 客户端（`connect_over_cdp`）。**不需要**运行 `playwright install` —— HubStudio 自带浏览器。如果你在脚本中使用了 Playwright 启动独立浏览器，则需要安装：`playwright install chromium`。

### 5.2 修改配置参数

编辑 `stress_test_cdp_hubstudio.py` 文件顶部的配置项：

```python
# ==================== Configuration ====================
APPID = '你的APP_ID'              # HubStudio APP ID
SECRETKEY = '你的APP_SECRET'       # HubStudio APP Secret
BASEURL = 'http://服务器IP:6874'   # HubStudio API 地址（socat 转发后的端口）

CDP_HOST = '服务器IP'              # CDP 连接地址

# SSH 配置（用于动态 socat 端口转发）
SSH_USER = 'your_user'             # SSH 登录用户名
SSH_HOST = CDP_HOST                # SSH 服务器地址
SSH_PORT = 22                      # SSH 端口
SSH_PASSWORD = 'your_password'     # SSH 密码
SSH_KEY_PATH = ''                  # SSH 私钥路径（优先使用，为空则使用密码）

# 压力测试参数
CONCURRENCY = 4                    # 并发数
TOTAL_RUNS = 100                   # 总任务数
```

### 5.3 自动化流程说明

每个测试任务执行以下完整生命周期：

```
① 创建环境 → ② 启动浏览器 → ③ 建立 socat 转发 → ④ CDP 连接
     → ⑤ 页面操作 & 截图 → ⑥ 关闭浏览器 → ⑦ 清理缓存 → ⑧ 删除环境
```

**详细步骤：**

| 步骤 | API 端点 | 说明 |
|------|---------|------|
| ① 创建环境 | `POST /api/v1/env/create` | 创建指纹浏览器环境 |
| ② 启动浏览器 | `POST /api/v1/browser/start` | 启动浏览器，获取 CDP 调试端口 |
| ③ socat 转发 | SSH + socat | 在远程服务器上动态创建端口转发 |
| ④ CDP 连接 | Playwright CDP | 通过 CDP 协议连接浏览器（最多 5 次重试） |
| ⑤ 页面操作 | — | 打开 Google 页面并截图 |
| ⑥ 关闭浏览器 | `POST /api/v1/browser/stop` | 停止浏览器实例 |
| ⑦ 清理缓存 | `POST /api/v1/cache/clear` | 清理本地缓存数据 |
| ⑧ 删除环境 | `POST /api/v1/env/del` | 删除浏览器环境 |

> 📝 **说明：** 并发由 `asyncio.Semaphore` 控制，所有任务异步执行但并发数不超过 `CONCURRENCY` 设定值。

### 5.4 运行测试

```bash
python stress_test_cdp_hubstudio.py
```

---

## 性能基准

以下结果在 Ubuntu 24.04 Server（8 vCPU、8 GB RAM）上测得：

| 指标 | 数值 |
|------|------|
| **总任务数** | 100 |
| **并发数** | 4（同时运行） |
| **成功率** | 98.0% |
| **总耗时** | 325.40 s |
| **平均每任务耗时** | 3.25 s |
| **吞吐量** | 0.31 tasks/s |

> 以上数据仅供参考。实际性能取决于服务器配置、网络状况和页面复杂度。

如有失败任务，测试结束时会附带错误详情。截图保存在 `screenshots/` 目录下（成功任务的截图会自动清理）。

---

## 生产环境部署（systemd）

在生产服务器上，建议将 HubStudio 配置为 **systemd 服务**，实现开机自启、崩溃重启和集中日志管理。

### 创建服务文件

```bash
sudo tee /etc/systemd/system/hubstudio.service > /dev/null <<'EOF'
[Unit]
Description=HubStudio Browser (headless)
After=network-online.target
Wants=network-online.target

[Service]
Type=simple
User=root
WorkingDirectory=/opt/hubstudio
ExecStart=/usr/bin/xvfb-run -a -s "-screen 0 1920x1080x24" /opt/hubstudio/Hubstudio --no-sandbox --headless=true --disable-gpu --ozone-platform=x11 --app-id=YOUR_APP_ID --app-secret=YOUR_APP_SECRET --login-group-code=YOUR_GROUP_CODE
Restart=on-failure
RestartSec=10
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
EOF
```

> 请调整 `WorkingDirectory`、`ExecStart` 路径及启动参数为实际值。

> 💡 **提示：** 生产环境建议创建专用用户（如 `hubstudio`）而非以 `root` 运行，并相应调整文件权限。

### 启用并启动

```bash
sudo systemctl daemon-reload
sudo systemctl enable hubstudio    # 开机自启
sudo systemctl start hubstudio     # 立即启动
```

### 管理服务

```bash
sudo systemctl status hubstudio    # 查看状态
sudo journalctl -u hubstudio -f    # 实时日志
sudo systemctl restart hubstudio   # 重启
sudo systemctl stop hubstudio      # 停止
```

---

## 项目结构

```
hubstudio-demo/
├── stress_test_cdp_hubstudio.py   # 压力测试主脚本
├── screenshots/                    # 截图输出目录（自动创建）
└── README.md                       # 项目说明文档
```

---

## 故障排查

### HubStudio 启动失败

```
AppImages require FUSE to run.
```

**解决方案：** 安装 FUSE 支持：

```bash
sudo apt install -y libfuse2t64
```

---

### 浏览器页面文字缺失 / 乱码

**解决方案：** 安装目标语言的字体包（参见 [Step 1 — 安装字体](#安装字体可选)）。

---

### 外部机器 CDP 连接被拒绝

**原因：** CDP 调试端口默认绑定 `127.0.0.1`。

**解决方案：** 配置 socat 转发（参见 [Step 4](#step-4--配置网络转发socat)），并确保防火墙放行转发端口。

---

### API 返回"连接被拒绝"

**原因：** HubStudio 尚未完成启动，或进程已崩溃。

**解决方案：**

1. 启动后等待 5–10 秒
2. 检查日志：`tail -f hubstudio.log`
3. 确认进程运行：`ps aux | grep Hubstudio`

---

### socat 端口转发失败

**原因：** socat 未安装，或端口已被占用。

**解决方案：**

```bash
# 检查 socat 是否安装
which socat

# 安装 socat
sudo apt install -y socat

# 检查端口是否被占用
ss -tlnp | grep 6874
```

---

### Playwright CDP 连接超时

**解决方案：**

1. 确认 socat 转发正常运行
2. 检查 SSH 配置：`ssh user@host`
3. 确认防火墙放行了 CDP 转发端口
4. 确保已安装 Playwright：

   ```bash
   pip install playwright
   ```

---

## 相关资源

| 目标 | 链接 |
|------|------|
| HubStudio API 接口文档 | [API Reference](https://api-docs.hubstudio.cn/8331450m0) |
| HubStudio 客户端下载 | [下载页面](https://www.hubstudio.cn/download/) |


### Hubstudio CLI 命令行操作指南

来源：https://api-docs.hubstudio.cn/8982261m0.md

`hubstudio-cli` 是随 Hubstudio 客户端分发的独立命令行工具，用于通过命令行调用 Hubstudio 客户端的 Local API。

它不会启动 Hubstudio 客户端，也不依赖 Node.js。使用前需要先启动 Hubstudio 客户端、完成登录，并在客户端中开启本地 API。

[官网 Local API 文档](https://api-docs.hubstudio.cn/)

## 适用对象

本文档适合需要通过命令行完成以下操作的用户：

- 打开、关闭或查询浏览器环境。
- 查询或管理环境、账号、分组。
- 调用云手机、RPA 等 Local API 能力。
- 在 Linux 服务器或自动化环境中配合无头 Hubstudio 客户端使用 CLI。

## 下载与安装

### 1. 下载地址

请根据您的操作系统和处理器架构下载对应的 `hubstudio-cli` 命令行工具：

| 操作系统 / 架构 | 下载链接 | 默认二进制文件名 |
| :--- | :--- | :--- |
| **macOS (Intel / x64)** | [点击下载](https://www.hubstudio.cn/client/prod/mac/x64/0.1.0/1780466510/hubstudio-cli) | `hubstudio-cli` |
| **macOS (Apple Silicon / arm64)** | [点击下载](https://www.hubstudio.cn/client/prod/mac/arm64/0.1.0/1780466361/hubstudio-cli) | `hubstudio-cli` |
| **Linux (x64)** | [点击下载](https://www.hubstudio.cn/client/prod/linux/x64/0.1.0/1780466390/hubstudio-cli) | `hubstudio-cli` |
| **Windows (x64)** | [点击下载](https://www.hubstudio.cn/client/prod/win/x64/0.1.0/1780466420/hubstudio-cli.exe) | `hubstudio-cli.exe` |

### 2. 安装与权限配置

#### macOS / Linux 系统
1. **赋予可执行权限**：下载后的二进制文件默认可能没有执行权限，请在终端中执行以下命令赋予权限：
   ```bash
   chmod +x hubstudio-cli
   ```
2. **（可选）配置全局环境变量**：若希望在任意目录下直接调用，可将二进制文件移动到系统的 `PATH` 路径下（例如 `/usr/local/bin`）：
   ```bash
   sudo mv hubstudio-cli /usr/local/bin/
   ```

#### Windows 系统
1. 下载后得到 `hubstudio-cli.exe`。
2. **（可选）配置全局环境变量**：将 `hubstudio-cli.exe` 所在的文件夹路径添加到系统的“环境变量 -> Path”中，即可在任意目录下通过命令行直接运行。

---

## 快速开始

确认 Hubstudio 客户端已经启动并开启 Local API 后，可以直接在命令行中执行。

> [!TIP]
> - 在 macOS / Linux 系统中，如果未将工具加入全局环境变量，执行时需带上当前路径前缀（如 `./hubstudio-cli`）。
> - 在 Windows 系统中，可通过命令提示符（CMD）或 PowerShell 运行 `hubstudio-cli.exe`。

```bash
# 以配置了全局环境变量为例，可以直接执行：
hubstudio-cli client-version
hubstudio-cli start-browser containerCode=11895682
hubstudio-cli stop-browser containerCode=11895682
hubstudio-cli browser-status
hubstudio-cli list-displays
```

如果客户端开启了「本地 API 安全认证」，需要传入客户端里配置的 API Key：

```
hubstudio-cli --local-api-key "你的API Key" start-browser containerCode=11895682
```

查看帮助：

```
hubstudio-cli --help
hubstudio-cli help start-browser
hubstudio-cli start-browser --help
```

如果命令连接失败，请优先查看本文末尾的“常见问题”。

## 常用命令速查

| 场景 | 命令示例 |
| --- | --- |
| 查询客户端版本 | `hubstudio-cli client-version` |
| 打开环境 | `hubstudio-cli start-browser containerCode=11895682` |
| 关闭环境 | `hubstudio-cli stop-browser containerCode=11895682` |
| 查询运行状态 | `hubstudio-cli browser-status` |
| 查询指定环境 | `hubstudio-cli browser-status --json '{"containerCodes":["11895682"]}'` |
| 列出屏幕 | `hubstudio-cli list-displays` |
| 查询环境列表 | `hubstudio-cli env-list --json '{"page":1,"pageSize":20}'` |
| 查看帮助 | `hubstudio-cli --help` |

## 使用前提

- Hubstudio 图形客户端必须已经启动。
- 当前账号必须已登录。
- 客户端里的 Local API 必须已开启。
- 如果开启了本地 API 安全认证，使用 CLI 时必须传入 API Key 参数（使用 `--local-api-key` 选项或设置 `HUBSTUDIO_LOCAL_API_KEY` 环境变量）。
- Local API 默认地址是 `http://127.0.0.1:6873`。如果客户端使用了其它端口，CLI 会优先通过客户端 IPC 查询当前端口，也可以用 `-p` 手动指定。
- Linux AppImage 可以用无头方式启动客户端，启动后再使用 `hubstudio-cli` 调用 Local API。无头模式必须提供 App Key 登录参数：

```
./Hubstudio.AppImage \
  --no-sandbox \
  --headless=true \
  --app-id="你的 app_id" \
  --app-secret="你的 app_secret" \
  --login-group-code=你的团队code
```

无头模式不会展示登录页和主界面，但仍会启动本地服务、完成 App Key 登录，并提供 Local API 给 `hubstudio-cli` 调用。

## 全局选项

全局选项需要放在子命令前面，例如：

```
hubstudio-cli --local-api-key "你的API Key" start-browser containerCode=11895682
```

```
-p, --port <n>             Local API 端口
-H, --host <h>             主机，默认 127.0.0.1
    --local-api-key <key>  Local API 安全认证密钥
    --timeout <seconds>   HTTP 请求超时秒数，默认 30 秒，0 表示不限制
-j, --json <json>          整段 JSON 请求体，覆盖 key=value
    --json-stdin           从标准输入读取 JSON 请求体
-h, --help                 显示帮助
-V, --version              显示版本
```

等价环境变量：

| 环境变量 | 说明 |
| --- | --- |
| `HUBSTUDIO_LOCAL_API_PORT` | IPC 查询失败且未传 `-p` 时使用 |
| `HUBSTUDIO_LOCAL_API_KEY` | 等价 `--local-api-key` |
| `HUBSTUDIO_CLI_TIMEOUT` | 等价 `--timeout` |

### 超时控制

默认情况下，CLI 会通过客户端 IPC 查询一次端口；端口发现不受 `--timeout` 影响。HTTP 请求阶段默认最多等待 30 秒，如果希望调整 HTTP 请求等待时间，可以使用 `--timeout`：

```
hubstudio-cli --timeout 30 browser-status
HUBSTUDIO_CLI_TIMEOUT=30 hubstudio-cli browser-status
```

`--timeout` 的单位是秒。未传时默认 `30` 秒；传入 `0` 表示不限制 HTTP 请求等待时间。

如果 HTTP 请求阶段超过指定时间仍未收到 Local API 响应，CLI 会先退出，并输出类似下面的超时响应：

```
{
  "code": 408,
  "msg": "CLI request timeout after 30s. The Local API operation may still be running in Hubstudio client."
}
```

请注意：超时只表示 CLI 停止等待 HTTP 响应，不代表 Hubstudio 客户端内部操作已取消。对于打开环境、创建环境、云手机开机、RPA 任务等可能继续在客户端中执行的操作，建议随后通过状态查询接口确认最终结果。

## 端口发现

通常不需要手动指定端口。Hubstudio 客户端启动 Local API 后，会同时启动一个本机 IPC 服务，CLI 会向该服务查询当前 Local API 端口。

IPC 服务地址：

| 平台 | IPC 地址 |
| --- | --- |
| Windows | `\\.\pipe\Hubstudio-cli` |
| macOS/Linux | `/tmp/Hubstudio-cli` |

如果 IPC 查询失败，CLI 会依次使用 `HUBSTUDIO_LOCAL_API_PORT` 和默认端口 `6873`。

手动指定端口示例：

```
hubstudio-cli -p 6873 browser-status
HUBSTUDIO_LOCAL_API_PORT=6873 hubstudio-cli browser-status
```

## 请求体写法

CLI 支持三种请求体写法：简单参数推荐使用 `key=value`，复杂参数推荐使用 `--json` 或 `--json-stdin`。

### key=value

CLI 会把子命令后的 `key=value` 转成 JSON 请求体：

```
hubstudio-cli client-version
hubstudio-cli start-browser containerCode=11895682 isHeadless=true pageZoom=100
```

转换规则：

| 输入 | JSON 类型 |
| --- | --- |
| `true` / `false` | boolean |
| `null` | null |
| `100` / `1.5` | number |
| `{...}` / `[...]` | object / array |
| 其它 | string |

### --json

复杂请求体建议直接使用 `--json`：

```
hubstudio-cli arrange-browsers --json '{"x":10,"y":10,"width":600,"height":500,"gapX":20,"gapY":20,"colNum":3}'
```

### --json-stdin

也可以从标准输入读取 JSON：

```
echo '{"containerCode":"11895682"}' | hubstudio-cli start-browser --json-stdin
```

## 客户端信息命令

### `client-version`

通过 Hubstudio 客户端启动后的 `Hubstudio-cli` IPC Server 获取当前客户端版本号。该命令不调用 Local API，也不需要请求体参数。

```
hubstudio-cli client-version
```

成功时返回：

```
{
  "code": 0,
  "msg": "Success",
  "data": {
    "version": "3.52.0",
    "pid": 12345
  }
}
```

如果客户端尚未启动，或 IPC Server 不可连接，会返回错误信息。

## 子命令

`hubstudio-cli` 的每个子命令都对应一个 Local API `POST` 接口。命令名由 API 路径去掉 `/api/v1/` 后将 `/` 替换为 `-` 得到，例如 `/api/v1/env/list` 对应 `env-list`。

下面按官网 [API 使用说明文档](https://api-docs.hubstudio.cn/) 的分组列出当前 CLI 支持的子命令。每个子命令都包含命令名称、Local API、官网文档和请求体参数清单。

说明：

- 表格中的“参数清单”主要列请求体 JSON 字段；全局选项和认证方式见上文。
- 所有接口均通过 `POST` 调用本地 Local API。
- 如果请求体字段较多，建议直接使用 `--json`，避免 shell 对特殊字符的转义问题。
- 字段是否必填以官网 OpenAPI `required` 标记为准；部分业务必填字段可能由接口逻辑校验，调用前请结合官网文档确认。

### 1. 环境管理

#### `env-list`

Local API：`POST /api/v1/env/list`

官网文档：[获取环境列表](https://api-docs.hubstudio.cn/380052376e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `containerCodes` | `array<string>` | 否 | 指定环境ID查询环境 |
| `containerName` | `string` | 否 | 指定环境名称查询环境 |
| `createEndTime` | `string` | 否 | 创建时间-截止时间 example: yyyy-MM-dd HH:mm:ss |
| `createStartTime` | `string` | 否 | 创建时间-起始时间 example: yyyy-MM-dd HH:mm:ss |
| `ipAddress` | `string` | 否 | IP地址查询 |
| `proxyTypeNames` | `array<string>` | 否 | 代理类型 HTTP、HTTPS、SSH、Socks5、Oxylabsauto、Lumauto 、Luminati、 smartproxy、IPIDEA、Iphtml、不使用代理 |
| `remark` | `string` | 否 | 指定环境备注信息查询环境 |
| `tagCode` | `integer` | 否 | 分组编号 默认不传参，若需要查询“未分组”的环境，传任意 |
| `tagNames` | `array<string>` | 否 | 环境分组名称数组 查询指定分组的环境 |
| `current` | `integer` | 否 | 当前页面 |
| `size` | `integer` | 否 | 分页条数 最多200条。 |
| `serviceProvider` | `string` | 否 | 环境内代理所属服务商 `ROLA_IP`、`922S5`、`通用api` |

##### 调用示例

**方式 A：简单参数过滤（key=value 格式）**
```bash
# 查询第 1 页，每页 20 条，备注为 "测试" 的环境
hubstudio-cli env-list current=1 size=20 remark="测试"
```

**方式 B：指定环境 ID 列表（--json 格式，包含数组）**
```bash
hubstudio-cli env-list --json '{"containerCodes": ["11895682", "11895683"]}'
```


#### `env-create`

Local API：`POST /api/v1/env/create`

官网文档：[创建环境](https://api-docs.hubstudio.cn/380052377e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `containerName` | `string` | 是 | 环境名 限制60字以内 |
| `remark` | `string` | 否 | 环境备注信息 |
| `tagName` | `string` | 否 | 指定环境所属分组的名称 若分组名称不存在，将默认环境未分组。 |
| `cookie` | `string` | 否 | 设置cookie 支持JSON格式的cookie |
| `asDynamicType` | `integer` | 是 | IP变更提醒 0-关闭提醒(默认)1-开启提醒 |
| `proxyTypeName` | `string` | 是 | 1. 自定义代理类型：`HTTP`、`HTTPS`、`SSH`、`Socks5`、`Oxylabsauto`、`Lumauto_HTTP`、`Lumauto_HTTPS`、`Luminati_HTTP`、`Luminati_HTTPS`、`smartproxy`、`Iphtml_HTTP`、`Iphtml_Socks5`、`IPIDEA`、`不使用代理`<br>2. API提取代理类型：`Socks5_ROLA_IP`、`HTTPS_ROLA_IP`、`Socks5_922S5`、`HTTP_922S5`、`HTTPS_922S5`、`Socks5_通用api`、`HTTP_通用api`、`HTTPS_通用api`、`Socks5_IPIDEA-API`、`HTTP_IPIDEA-API`、`HTTPS_IPIDEA-API` |
| `ipGetRuleType` | `integer` | 否 | IP提取方式 1-IP失效时提取新IP ，2-，每次打开环境时提取新IP。API提取代理时必填 |
| `linkCode` | `string` | 否 | 提取链接 API提取代理时必填 |
| `proxyServer` | `string` | 否 | 代理主机 自定义代理时必填 |
| `proxyPort` | `integer` | 否 | 代理端口 自定义代理时必填 |
| `proxyAccount` | `string` | 否 | 代理帐号 |
| `proxyPassword` | `string` | 否 | 代理密码 |
| `referenceCountryCode` | `string` | 否 | 环境内帐号需要登录的指定的国家 Oxylabsauto、Lumauto、Smartproxy必须填写国家或者IP |
| `referenceIp` | `string` | 否 | 根据IP自动填充环境内帐号需要登录的指定的国家 Oxylabsauto、Lumauto、Smartproxy必须填写国家或者IP |
| `referenceCity` | `string` | 否 | 参考城市 |
| `referenceRegionCode` | `string` | 否 | 参考州 |
| `ipDatabaseChannel` | `integer` | 否 | 代理查询渠道 用户未指定时使用全局默认值。支持设置查询渠道选项，1-IP2Location 2-DB-IP 3-MaxMind |
| `ipProtocolType` | `integer` | 否 | IP协议选项 支持设置IP协议，新环境默认使用速度优先 1.速度优先 2.IPv4 3.IPv6 |
| `type` | `string` | 否 | 操作系统。可选值：`windows`、`android`、`ios`、`macos`（不传默认 `windows`） |
| `phoneModel` | `string` | 否 | 手机机型。当 `type` 选择 `android` 或 `ios` 时为必填项。可选参数值：`google Pixel 4`、`红米8`、`红米7`、`google Pixel 5a`、`三星Galaxy Note8`、`小米10`、`三星Galaxy S9+`、`小米9`、`iPhone 6 Plus`、`iPhone 8 Plus`、`iPhone SE 2`、`iPhone 7 Plus`、`iPhone X`、`iPhone13 Pro`、`iPhone XS`、`iPhone 13 Pro Max`、`iPhone 12 mini`、`iPhone 8`、`iPhone 13 mini`、`iPhone 6`、`iPhone 12 Pro Max`、`iPhone 7`、`iPhone 12` 、`iPhone 12 Pro`、`iPhone 11 Pro`、`iPhone 13`、`iPhone 14`、`iPhone 14 Pro`、`iPhone 14 Pro Max`、`iPhone 15`、`iPhone 15 Pro`、`iPhone 15 Pro Max`、`google Pixel 6`、`google Pixel 6a`、`google Pixel 6 Pro`、`google Pixel 7`、`google Pixel 7 Pro`、`google Pixel 7a`、`google Pixel 8`、`google Pixel 8 Pro`、`google Pixel 8a`、`Samsung Galaxy S20`、`Samsung Galaxy S20 +`、`Samsung Galaxy S21`、`Samsung Galaxy S21 +`、`Samsung Galaxy S21 Ultra`、`Samsung Galaxy S22`、`Samsung Galaxy S22 +`、`Samsung Galaxy S22 Ultra` |
| `browser` | `string` | 否 | 浏览器类型 firefox/chrome，不填默认创建谷歌环境， |
| `coreVersion` | `integer` | 否 | 内核版本号 支持100~126。用selenium时，可以根据这个字段来判断驱动chromedriver的版本号。 |
| `videoThrottle` | `integer` | 否 | 视频限流  0关闭 1开启 2跟随团队。不传参默认跟随团队。 |
| `imgThrottle` | `integer` | 否 | 图片限流  0关闭 1自定义 2跟随团队。不传参默认跟随团队。 |
| `imgThrottleSize` | `integer` | 否 | 图片尺寸大小 |
| `advancedBo` | `object` | 否 | Hubstudio浏览器高级指纹参数配置 |
| `advancedBo.uaVersion` | `string` | 否 | ua版本 |
| `advancedBo.ua` | `string` | 否 | 自定义UA 要求传参格式符合标准。举例：Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36 |
| `advancedBo.languageType` | `integer` | 否 | 语言 0-跟随IP，1-自定义，2-跟随电脑 |
| `advancedBo.languages` | `array<string>` | 否 | 语言列表 默认使用第一个传入的语言作为渲染语言 |
| `advancedBo.gmt` | `string` | 否 | 时区 timezone时区，不传参默认使用系统默认。自定义时格式举例：GMT-12:00 |
| `advancedBo.geography` | `string` | 否 | 地理位置 timezone地理，不传参默认使用系统默认。自定义时格式举例： Etc/GMT + 12 |
| `advancedBo.geoTips` | `integer` | 否 | 网站请求获取您当前地理位置时的选择 0-ask（询问）、2-block（禁止） |
| `advancedBo.geoRule` | `integer` | 否 | 地理位置规则 不传参默认使用系统默认。0-基于IP生成对应位置，1-使用自定义设置的位置 |
| `advancedBo.longitude` | `string` | 否 | 地理位置 自定义时必填，格式如“-40.123”（范围-180到180） |
| `advancedBo.latitude` | `string` | 否 | 纬度 地理位置自定义时必填，格式如“30.123”（范围-90到90） |
| `advancedBo.radius` | `string` | 否 | 经度 地理位置自定义时必填，，格式如“10“（范围10-5000） |
| `advancedBo.height` | `string` | 否 | 分辨率-高 type为Android或IOS时，不支持设置分辨率。分辨率高、宽都传-1时，分辨率随机 |
| `advancedBo.width` | `string` | 否 | 分辨率-宽 type为Android或IOS时，不支持设置分辨。分辨率高、宽都传-1时，分辨率随机 |
| `advancedBo.fontsType` | `integer` | 否 | 字体设置规则 0-隐私，1-真实 |
| `advancedBo.fonts` | `array<string>` | 否 | 字体 按照字体的英文传入（编辑环境时，请将所有的字体传入。若传入的字体过少，可能会导致网页数据显示不全） |
| `advancedBo.fontFingerprint` | `integer` | 否 | 字体指纹 0-开启ClientRects隐私保护，1-使用电脑默认的ClientRects |
| `advancedBo.webRtc` | `integer` | 否 | webrtc设置规则 0-开启WebRTC，但禁止获取IP，1-开启WebRTC，将公网IP替换为代理IP，2-开启WebRTC，跟随电脑真实IP，3-禁用WebRTC，网站会检测到您关闭了WebRTC，4-转发WebRTC，将公网IP替换为代理IP |
| `advancedBo.webRtcLocalIp` | `string` | 否 | 内网IP。10.0.0.0/8；10.0.0.0 - 10.255.255.255；172.16.0.0/12；172.16.0.0 - 172.31.255.255；192.168.0.0/16；192.168.0.0 - 192.168.255.255 |
| `advancedBo.canvas` | `integer` | 否 | canvas设置规则 0-开启Canvas隐私保护，1-跟随电脑的Canvas |
| `advancedBo.webgl` | `integer` | 否 | webgl设置规则 0-开启WebGL隐私保护，1-跟随电脑的WebGL |
| `advancedBo.hardwareAcceleration` | `integer` | 否 | webgl参数 0-关闭硬件加速，1-开启硬件加速 |
| `advancedBo.webglInfo` | `integer` | 否 | 开启硬件加速时可传参，不传参默认使用系统默认。0-webglvendor和webglRenderer信息将根据ua进行匹配，1-跟随电脑的WebGL Info |
| `advancedBo.audioContext` | `integer` | 否 | 音频设置规则 0-开启AudioContext隐私保护，1-跟随电脑的AudioContext |
| `advancedBo.speechVoices` | `integer` | 否 | 讲述人设置规则 0-开启SpeechVoices，1-关闭SpeechVoicess |
| `advancedBo.media` | `integer` | 否 | 媒体设置规则 0-开启媒体设备隐私保护，1-使用Chrome原生隐私保护（不授权则不会暴露真实媒体设备数量） |
| `advancedBo.cpu` | `integer` | 否 | cpu设置规则 2,4,6,8，10,12,16,0（0代表真实） |
| `advancedBo.memory` | `integer` | 否 | 内存设置规则 2,4,6,8,0（0代表真实） |
| `advancedBo.doNotTrack` | `integer` | 否 | donottrack设置规则 0-默认不设置，1-默认不允许追踪，2-默认允许追踪 |
| `advancedBo.battery` | `integer` | 否 | 电池设置规则 0-开启电池隐私保护，1-使用电脑真实的电池信息,2-禁止访问电池信息 |
| `advancedBo.portScan` | `integer` | 否 | 端口扫描保护设置规则 0-不允许网站检测您使用的本地网络端口，1-允许网站检测您使用的本地网络端口 |
| `advancedBo.whiteList` | `string` | 否 | 端口扫描保护白名单 端口扫描保护开启后，设置的本地端口可以访问，多个端口逗号隔开 |

##### 调用示例

**方式 A：创建简易 Windows 环境（key=value 格式）**
```bash
hubstudio-cli env-create containerName="测试环境" asDynamicType=0 proxyTypeName="不使用代理"
```

**方式 B：配置高级指纹和自定义代理（--json 格式）**
```bash
hubstudio-cli env-create --json '{
  "containerName": "店铺环境-01",
  "asDynamicType": 1,
  "proxyTypeName": "Socks5",
  "proxyServer": "127.0.0.1",
  "proxyPort": 1080,
  "advancedBo": {
    "ua": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
    "webRtc": 1,
    "canvas": 0
  }
}'
```


#### `env-update`

Local API：`POST /api/v1/env/update`

官网文档：[更新环境](https://api-docs.hubstudio.cn/380052378e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `containerCode` | `integer` | 是 | 环境ID |
| `containerName` | `string` | 是 | 环境名 若无需更改环境名称传入原有名称即可 |
| `remark` | `string` | 否 | 环境备注信息 不传视为留空，会覆盖原备注 |
| `type` | `string` | 是 | 操作系统类型 操作系统类型传值:windows/android/ios/macos 四个中的一个 |
| `tagName` | `string` | 是 | 环境所属分组信息 若分组名称不存在，将默认不修改环境分组 |
| `coreVersion` | `integer` | 是 | 内核版本号 支持100~126。用selenium时，可以根据这个字段来判断驱动chromedriver的版本号。 |
| `videoThrottle` | `integer` | 否 | 视频限流  0关闭 1开启 2跟随团队。不传参默认跟随团队。 |
| `imgThrottle` | `integer` | 否 | 图片限流  0关闭 1自定义 2跟随团队。不传参默认跟随团队。 |
| `imgThrottleSize` | `integer` | 否 | 图片尺寸大小 |
| `advancedBo` | `object` | 否 | Hubstudio浏览器高级指纹参数配置 |
| `advancedBo.uaVersion` | `string` | 否 | ua版本 |
| `advancedBo.ua` | `string` | 否 | 自定义UA要求传参格式符合标准 举例：Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36 |
| `advancedBo.languageType` | `integer` | 否 | 界面语言类型 0-跟随IP，1-自定义，2-跟随电脑 |
| `advancedBo.languages` | `array<string>` | 否 | 默认使用第一个传入的语言作为渲染语言 |
| `advancedBo.gmt` | `string` | 否 | timezone时区 不传参默认使用系统默认。自定义时格式举例：GMT-12:00 |
| `advancedBo.geography` | `string` | 否 | timezone地理 不传参默认使用系统默认。自定义时格式举例： Etc/GMT + 12 |
| `advancedBo.geoTips` | `integer` | 否 | 网站请求获取您当前地理位置 ，0-ask（询问）、2-block（禁止） |
| `advancedBo.geoRule` | `integer` | 否 | 地理位置规则 不传参默认使用系统默认。0-基于IP生成对应位置，1-使用自定义设置的位置 |
| `advancedBo.longitude` | `string` | 否 | 地理位置自定义时必填，格式如“-40.123”（范围-180到180） |
| `advancedBo.latitude` | `string` | 否 | 地理位置自定义时必填，格式如“30.123”（范围-90到90） |
| `advancedBo.radius` | `string` | 否 | 地理位置自定义时必填，，格式如“10“（范围10-5000） |
| `advancedBo.height` | `string` | 否 | 分辨率-高，type为Android或IOS时，不支持设置分辨率。分辨率高、宽都传-1时，分辨率随机 |
| `advancedBo.width` | `string` | 否 | 分辨率-宽，type为Android或IOS时，不支持设置分辨。分辨率高、宽都传-1时，分辨率随机 |
| `advancedBo.fontsType` | `integer` | 否 | 字体列表保护，0-隐私，1-真实 |
| `advancedBo.fonts` | `array<string>` | 否 | 按照字体的英文传入（编辑环境时，请将所有的字体传入。若传入的字体过少，可能会导致网页数据显示不全） |
| `advancedBo.fontFingerprint` | `integer` | 否 | 字体指纹，0-开启ClientRects隐私保护，1-使用电脑默认的ClientRects |
| `advancedBo.webRtc` | `integer` | 否 | 0-开启WebRTC，但禁止获取IP，1-开启WebRTC，将公网IP替换为代理IP，2-开启WebRTC，跟随电脑真实IP，3-禁用WebRTC，网站会检测到您关闭了WebRTC，4-转发WebRTC，将公网IP替换为代理IP |
| `advancedBo.webRtcLocalIp` | `string` | 否 | 内网IP。10.0.0.0/8；10.0.0.0 - 10.255.255.255；172.16.0.0/12；172.16.0.0 - 172.31.255.255；192.168.0.0/16；192.168.0.0 - 192.168.255.255 |
| `advancedBo.canvas` | `integer` | 否 | 0-开启Canvas隐私保护，1-跟随电脑的Canvas |
| `advancedBo.webgl` | `integer` | 否 | 0-开启WebGL隐私保护，1-跟随电脑的WebGL |
| `advancedBo.hardwareAcceleration` | `integer` | 否 | 0-关闭硬件加速，1-开启硬件加速 |
| `advancedBo.webglInfo` | `integer` | 否 | 开启硬件加速时可传参，不传参默认使用系统默认。0-webglvendor和webglRenderer信息将根据ua进行匹配，1-跟随电脑的WebGL Info |
| `advancedBo.audioContext` | `integer` | 否 | 0-开启AudioContext隐私保护，1-跟随电脑的AudioContext |
| `advancedBo.speechVoices` | `integer` | 否 | 0-开启SpeechVoices，1-关闭SpeechVoicess |
| `advancedBo.media` | `integer` | 否 | 0-开启媒体设备隐私保护，1-使用Chrome原生隐私保护（不授权则不会暴露真实媒体设备数量） |
| `advancedBo.cpu` | `integer` | 否 | 2,4,6,8，10,12,16,0（0代表真实） |
| `advancedBo.memory` | `integer` | 否 | 2,4,6,8,0（0代表真实） |
| `advancedBo.doNotTrack` | `integer` | 否 | 0-默认不设置，1-默认不允许追踪，2-默认允许追踪 |
| `advancedBo.battery` | `integer` | 否 | 0-开启电池隐私保护，1-使用电脑真实的电池信息,2-禁止访问电池信息 |
| `advancedBo.portScan` | `integer` | 否 | 0-不允许网站检测您使用的本地网络端口，1-允许网站检测您使用的本地网络端口 |
| `advancedBo.whiteList` | `string` | 否 | - |

##### 调用示例

**方式 A：更新环境名称和分组（--json 格式）**
```bash
hubstudio-cli env-update --json '{
  "containerCode": 11895682,
  "containerName": "修改后的环境名称",
  "type": "windows",
  "tagName": "新分组",
  "coreVersion": 120
}'
```


#### `env-proxy-update`

Local API：`POST /api/v1/env/proxy/update`

官网文档：[更新环境代理](https://api-docs.hubstudio.cn/380052379e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `containerCode` | `integer` | 是 | 环境ID |
| `asDynamicType` | `integer` | 是 | IP变更提醒 0-关闭提醒(默认)1-开启提醒 |
| `proxyTypeName` | `string` | 是 | 1. 自定义代理类型：`HTTP`、`HTTPS`、`SSH`、`Socks5`、`Oxylabsauto`、`Lumauto_HTTP`、`Lumauto_HTTPS`、`Luminati_HTTP`、`Luminati_HTTPS`、`smartproxy`、`Iphtml_HTTP`、`Iphtml_Socks5`、`IPIDEA`、`不使用代理`<br>2. API提取代理类型：`Socks5_ROLA_IP`、`HTTP_ROLA_IP`、`HTTPS_ROLA_IP`、`Socks5_922S5`、`HTTP_922S5`、`HTTPS_922S5`、`Socks5_通用api`、`HTTP_通用api`、`HTTPS_通用api`、`Socks5_IPIDEA-API`、`HTTP_IPIDEA-API`、`HTTPS_IPIDEA-API` |
| `ipGetRuleType` | `integer` | 否 | IP提取方式 1-IP失效时提取新IP ，2-，每次打开环境时提取新IP。API提取代理时必填 |
| `linkCode` | `string` | 否 | 提取链接 API提取代理时必填 |
| `proxyHost` | `string` | 否 | 代理主机 |
| `proxyPort` | `integer` | 否 | 代理端口 |
| `proxyAccount` | `string` | 否 | 代理帐号 |
| `proxyPassword` | `string` | 否 | 代理密码 |
| `referenceCountryCode` | `string` | 否 | 环境内帐号需要登录的指定的国家 Oxylabsauto、Lumauto、Smartproxy必须填写国家或者IP |
| `referenceCity` | `string` | 否 | 参考城市 |
| `referenceRegionCode` | `string` | 否 | 参考州 |
| `ipDatabaseChannel` | `integer` | 否 | 代理查询渠道 支持设置查询渠道选项，1-IP2Location 2-DB-IP 3-MaxMind |
| `ipProtocolType` | `integer` | 否 | IP协议选项 支持设置IP协议 1.速度优先 2.IPv4 3.IPv6 |

##### 调用示例

**方式 A：更新为不使用代理（key=value 格式）**
```bash
hubstudio-cli env-proxy-update containerCode=11895682 asDynamicType=0 proxyTypeName="不使用代理"
```

**方式 B：更新为自定义 Socks5 代理（key=value 格式）**
```bash
hubstudio-cli env-proxy-update containerCode=11895682 asDynamicType=1 proxyTypeName="Socks5" proxyHost="127.0.0.1" proxyPort=1080
```


#### `env-import-cookie`

Local API：`POST /api/v1/env/import-cookie`

官网文档：[导入 Cookie](https://api-docs.hubstudio.cn/380052380e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `containerCode` | `string` | 是 | 环境ID |
| `cookie` | `string` | 是 | 设置cookie 值为空字符串是清空cookie |

##### 调用示例

**方式 A：导入 Cookie（--json 格式）**
```bash
hubstudio-cli env-import-cookie --json '{
  "containerCode": "11895682",
  "cookie": "[{\"domain\": \".google.com\", \"name\": \"SID\", \"value\": \"example_val\"}]"
}'
```


#### `env-export-cookie`

Local API：`POST /api/v1/env/export-cookie`

官网文档：[导出 Cookie](https://api-docs.hubstudio.cn/380052381e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `containerCode` | `integer` | 是 | 环境ID |

##### 调用示例

**方式 A：导出环境 Cookie（key=value 格式）**
```bash
hubstudio-cli env-export-cookie containerCode=11895682
```


#### `env-del`

Local API：`POST /api/v1/env/del`

官网文档：[删除环境](https://api-docs.hubstudio.cn/380052382e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `containerCodes` | `array<integer>` | 是 | 环境ID列表 |

##### 调用示例

**方式 A：批量删除指定环境（--json 格式，包含数组）**
```bash
hubstudio-cli env-del --json '{"containerCodes": [11895682, 11895683]}'
```


#### `env-random-ua`

Local API：`POST /api/v1/env/random-ua`

官网文档：[获取随机 UA](https://api-docs.hubstudio.cn/380052383e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `type` | `string` | 否 | 操作系统参数 windows、android、ios（不传参数默认windows） |
| `phoneModel` | `string` | 否 | 手机机型 type选择android和ios时，机型必填。机型参数包括：“google Pixel 4、红米8、红米7、google Pixel 5a、三星Galaxy Note8、小米10、三星Galaxy S9+、小米9、iPhone 6 Plus、iPhone 8 Plus、iPhone SE 2、iPhone 7 Plus、iPhone X、iPhone 13 Pro、iPhone XS、iPhone 13 Pro Max、iPhone 12 mini、iPhone 8、iPhone 13 mini、iPhone 6、iPhone 12 Pro Max、iPhone 7、iPhone 12 、iPhone 12 Pro、iPhone 11 Pro、iPhone 13、iPhone 14、iPhone 14 Pro、iPhone 14 Pro Max、iPhone 15、iPhone 15 Pro、iPhone 15 Pro Max、google Pixel 6、google Pixel 6a、google Pixel 6 Pro、google Pixel 7、google Pixel 7 Pro、google Pixel 7a、google Pixel 8、google Pixel 8 Pro、google Pixel 8a、Samsung Galaxy S20、Samsung Galaxy S20 +、Samsung Galaxy S21、Samsung Galaxy S21 +、Samsung Galaxy S21 Ultra、Samsung Galaxy S22、Samsung Galaxy S22 +、Samsung Galaxy S22 Ultra ” |
| `version` | `array<integer>` | 否 | 内核版本 支持数组，不传参默认随机。 |

##### 调用示例

**方式 A：生成随机 Windows UA（key=value 格式）**
```bash
hubstudio-cli env-random-ua type="windows"
```

**方式 B：生成指定机型和内核范围的移动端 UA（--json 格式）**
```bash
hubstudio-cli env-random-ua --json '{"type": "android", "phoneModel": "小米10", "version": [117, 120]}'
```


#### `clear-cache`

Local API：`POST /api/v1/cache/clear`

官网文档：[清除环境本地缓存](https://api-docs.hubstudio.cn/380052384e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `browserOauths` | `array<string>` | 否 | 打开环境时返回的browserID 参数不传则删除所有环境的本地缓存 |
| `localStorage` | `boolean` | 否 | 是否清除 LocalStorage 默认为否 |
| `indexedDB` | `boolean` | 否 | 是否清除 IndexedDB 默认为否 |
| `cookie` | `boolean` | 否 | 是否清除 cookie 默认为否 |
| `extension` | `boolean` | 否 | 是否清除扩展数据 默认为否，不清除 |
| `extensionFile` | `boolean` | 否 | 是否清除扩展 默认为否，不清除 |

##### 调用示例

**方式 A：清除所有环境的本地缓存**
```bash
hubstudio-cli clear-cache
```

**方式 B：清除指定环境的 Cookie 和 LocalStorage（--json 格式）**
```bash
hubstudio-cli clear-cache --json '{
  "browserOauths": ["oauth_id_12345"],
  "localStorage": true,
  "cookie": true
}'
```


#### `reset-extension`

Local API：`POST /api/v1/browser/reset-extension`

官网文档：[清理环境内插件缓存](https://api-docs.hubstudio.cn/380052385e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `browserOauth` | `integer` | 是 | 打开环境时返回的browserID |
| `pluginIds` | `array<string>` | 是 | 指定要清除的插件的ID 可通过chrome://extensions/查看环境内所有插件的ID |

##### 调用示例

**方式 A：清除指定插件的缓存（--json 格式，包含数组）**
```bash
hubstudio-cli reset-extension --json '{
  "browserOauth": 12345,
  "pluginIds": ["ghbmnonnpghnedgpgelfhgghklgaoloi"]
}'
```


#### `env-refresh-fingerprint`

Local API：`POST /api/v1/env/refresh-fingerprint`

官网文档：[刷新指纹](https://api-docs.hubstudio.cn/380052386e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `containerCode` | `integer` | 是 | 环境ID |
| `uaVersion` | `integer` | 否 | UA版本 不传 uaVersion ，默认随机最新UA |
| `coreVersion` | `integer` | 否 | 客户端内核版本 不传，不会改变 |
| `type` | `string` | 否 | 操作系统类型 不传默认为windows |

##### 调用示例

**方式 A：刷新指定环境的指纹（key=value 格式）**
```bash
hubstudio-cli env-refresh-fingerprint containerCode=11895682 type="windows"
```


#### `container-webgl-renderer-list`

Local API：`POST /api/v1/container/webgl-renderer-list`

官网文档：[查询 webglVendor 和 webglRenderer](https://api-docs.hubstudio.cn/380052387e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `browser` | `string` | 否 | 浏览器 chrome、firefox。默认chrome |
| `hardwareAcceleration` | `integer` | 否 | 开启硬件加速 ，0-关闭硬件加速。默认:1 |
| `type` | `string` | 否 | 操作系统类型 windows，android，ios， macos。 默认windows |
| `uaVersion` | `string` | 是 | ua版本 例如：117 |

##### 调用示例

**方式 A：查询 WebGL 渲染器列表（key=value 格式）**
```bash
hubstudio-cli container-webgl-renderer-list browser="chrome" type="windows" uaVersion="117"
```


#### `container-batch-update-remark`

Local API：`POST /api/v1/container/batch-update-remark`

官网文档：[批量修改备注](https://api-docs.hubstudio.cn/380052388e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `containerCodes` | `array<string>` | 是 | 环境ID |
| `remark` | `string` | 否 | 备注 |
| `type` | `integer` | 是 | 修改类型  1覆盖 2追加 |

##### 调用示例

**方式 A：批量覆盖环境备注（--json 格式，包含数组）**
```bash
hubstudio-cli container-batch-update-remark --json '{
  "containerCodes": ["11895682", "11895683"],
  "remark": "批次A-测试",
  "type": 1
}'
```


#### `download-browser-core`

Local API：`POST /api/v1/browser/download-core`

官网文档：[下载内核](https://api-docs.hubstudio.cn/380052389e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Cores` | `array<object>` | 是 | 内核列表 |
| `Cores[].BrowserType` | `integer` | 是 | 浏览器内核类型 1-Chrome，2-Firefox |
| `Cores[].Version` | `string` | 是 | 内核版本 仅支持hub客户端支持的版本下载。 |

##### 调用示例

**方式 A：批量下载指定版本的内核（--json 格式，包含嵌套数组）**
```bash
hubstudio-cli download-browser-core --json '{
  "Cores": [
    {"BrowserType": 1, "Version": "120"}
  ]
}'
```


#### `container-update-container-base`

Local API：`POST /api/v1/container/update-container-base`

官网文档：[更新环境基础信息](https://api-docs.hubstudio.cn/439846791e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `containerCode` | `integer` | 是 | 环境ID |
| `containerName` | `string` | 否 | 环境名称 支持空字符串：不进行修改 |
| `remark` | `string` | 否 | 备注 支持空字符串：清空备注数据，不传则不进行修改 |
| `tagName` | `string` | 否 | 分组名称 限制中英文、数字、常用符号，不支持空格、空字符串，不修改可以不传 |

##### 调用示例

**方式 A：修改环境基本信息（key=value 格式）**
```bash
hubstudio-cli container-update-container-base containerCode=11895682 containerName="修改后的环境" remark="最新测试"
```


### 2. 浏览器环境

#### `start-browser`

Local API：`POST /api/v1/browser/start`

官网文档：[打开环境](https://api-docs.hubstudio.cn/380052361e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `containerCode` | `string` | 是 | 环境ID |
| `cdpHide` | `boolean` | 否 | 是否屏蔽cdp检测 默认false（true代表屏蔽）仅支持ChroBrowser133及以上内核版本 |
| `shouldCloseTabsOnOpen` | `boolean` | 否 | 是否打开历史标签页 需要在客户端 "偏好设置-个人设置-启动环境时" 选项中，选择 "打开上次"，此参数才会生效。当 shouldCloseTabsOnOpen 为 true 时，会同步服务端的标签页数据到客户端，即打开上次关闭时的标签页。当 shouldCloseTabsOnOpen 为 false 时，不会同步服务端的标签页数据，客户端的现有标签页数据不会被覆盖。 |
| `pageZoom` | `integer` | 否 | 缩放比例 只能传原生支持的比例，原生支持 50%，75%， 100%，125%，150%，175%，200%，其它参数不支持，150% 传 1.5。此参数仅支持3.46.0及以上版本 |
| `containerTabs` | `array<string>` | 否 | 启动url 举例： "containerTabs": ["https://www.hubstudio.cn/", "https://www.baidu.com/"] |
| `isHeadless` | `boolean` | 否 | 浏览器无头模式 默认false，设置无头后如无法连接，请使用用"args"参数进行设置: ["--headless=new"] |
| `isWebDriverReadOnlyMode` | `boolean` | 否 | 是否只读模式 默认false。（true代表只读模式，不会保存cookie等数据 |
| `skipSystemResourceCheck` | `boolean` | 否 | 跳过系统可用资源检测 默认false不跳过系统可用资源检测(仅支持v3.6.0及以上版本) |
| `args` | `array<string>` | 否 | 启动参数 举例 "args": [ "--kiosk", "--blink-settings=imagesEnabled=false" ] |
| `serialNumber` | `string` | 否 | 序号 containerCode和serialNumber都传，以containerCode为准，支持客户端版本3.55.0版本 |

##### 调用示例

**方式 A：打开指定环境（key=value 格式）**
```bash
hubstudio-cli start-browser containerCode=11895682
```

**方式 B：配置无头模式与特定启动参数（--json 格式）**
```bash
hubstudio-cli start-browser --json '{
  "containerCode": "11895682",
  "isHeadless": true,
  "args": ["--headless=new", "--disable-gpu"]
}'
```


#### `stop-browser`

Local API：`POST /api/v1/browser/stop`

官网文档：[关闭环境](https://api-docs.hubstudio.cn/380052362e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `containerCode` | `string` | 是 | 环境ID |
| `serialNumber` | `string` | 否 | 序号 containerCode和serialNumber都传，以containerCode为准，支持客户端版本3.55.0版本 |

##### 调用示例

**方式 A：关闭指定环境（key=value 格式）**
```bash
hubstudio-cli stop-browser containerCode=11895682
```


#### `stop-all-browsers`

Local API：`POST /api/v1/browser/stop-all`

官网文档：[关闭所有环境](https://api-docs.hubstudio.cn/380052366e0)

参数清单：无请求体参数。

##### 调用示例

**方式 A：关闭所有正在运行的浏览器环境**
```bash
hubstudio-cli stop-all-browsers
```


#### `browser-status`

Local API：`POST /api/v1/browser/all-browser-status`

官网文档：[获取浏览器状态](https://api-docs.hubstudio.cn/380052363e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `containerCodes` | `array<string>` | 是 | 环境id列表 |

##### 调用示例

**方式 A：批量查询指定环境的运行状态（--json 格式，包含数组）**
```bash
hubstudio-cli browser-status --json '{"containerCodes": ["11895682", "11895683"]}'
```


#### `foreground-browser`

Local API：`POST /api/v1/browser/foreground`

官网文档：[切换浏览器窗口](https://api-docs.hubstudio.cn/380052364e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `containerCode` | `string` | 是 | 环境ID |

##### 调用示例

**方式 A：切换指定浏览器窗口至前台（key=value 格式）**
```bash
hubstudio-cli foreground-browser containerCode=11895682
```


#### `list-displays`

Local API：`POST /api/v1/display/all`

官网文档：[获取全部屏幕（物理机的屏幕）](https://api-docs.hubstudio.cn/380052367e0)

参数清单：无请求体参数。

##### 调用示例

**方式 A：列出物理机的所有屏幕信息**
```bash
hubstudio-cli list-displays
```


#### `arrange-browsers`

Local API：`POST /api/v1/browser/arrange`

官网文档：[浏览器窗口自定义排列](https://api-docs.hubstudio.cn/380052368e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `x` | `integer` | 否 | 起始位置x坐标 默认为10，取值可为0~9999之间的整数 |
| `y` | `integer` | 否 | 起始位置y坐标 默认为10，取值可为0~9999之间的整数 |
| `width` | `integer` | 否 | 窗口宽度 默认为600，取值范围500~9999之间的整数 |
| `height` | `integer` | 否 | 窗口高度 默认为500，取值范围200~9999之间的整数 |
| `gapX` | `integer` | 否 | 窗口横向间距 默认为20，取值范围-9999~9999之间的整数 |
| `gapY` | `integer` | 否 | 窗口纵向间距 默认为20，取值范围-9999~9999之间的整数 |
| `colNum` | `integer` | 否 | 每行展示窗口数量 默认为3，取值范围1~99之间的整数 |
| `screenId` | `integer` | 否 | 屏幕ID |

##### 调用示例

**方式 A：自定义排列所有打开的窗口（key=value 格式）**
```bash
hubstudio-cli arrange-browsers x=10 y=10 width=600 height=500 gapX=20 gapY=20 colNum=3
```


### 3. 云手机

#### 3.1 应用管理

##### `cloud-mobile-app-page`

Local API：`POST /api/v1/cloud-mobile/app/page`

官网文档：[APP 列表(分页)\查询可安装应用列表](https://api-docs.hubstudio.cn/380052350e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `appName` | `string` | 否 | 应用名称 模糊查询 |
| `pageNum` | `integer` | 否 | 当前页数 从1开始 |
| `pageSize` | `integer` | 否 | 每页显示记录数 默认为10 |
| `productId` | `integer` | 是 | 云手机商品ID 通过 /api/v1/cloud-mobile/mobile-product-list接口获得 |

##### 调用示例

**方式 A：分页查询可安装应用（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-app-page appName="WeChat" pageNum=1 pageSize=10 productId=1001
```


##### `cloud-mobile-app-installedList`

Local API：`POST /api/v1/cloud-mobile/app/installedList`

官网文档：[已安装应用列表查询](https://api-docs.hubstudio.cn/380052353e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mobileId` | `integer` | 是 | 云手机ID |

##### 调用示例

**方式 A：获取已安装应用列表（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-app-installedList mobileId=10001
```


##### `cloud-mobile-group-app-create`

Local API：`POST /api/v1/cloud-mobile/group/app/create`

官网文档：[新增团队应用](https://api-docs.hubstudio.cn/395424141e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `packageName` | `string` | 是 | 应用包名 |
| `versionCode` | `string` | 是 | 应用版本号 |

##### 调用示例

**方式 A：新增团队应用（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-group-app-create packageName="com.tencent.mm" versionCode="8.0.40"
```


##### `cloud-mobile-app-install`

Local API：`POST /api/v1/cloud-mobile/app/install`

官网文档：[app 应用安装](https://api-docs.hubstudio.cn/380052354e0)

参数清单：无请求体参数。

##### 调用示例

**方式 A：安装应用**
```bash
hubstudio-cli cloud-mobile-app-install
```


##### `cloud-mobile-app-start`

Local API：`POST /api/v1/cloud-mobile/app/start`

官网文档：[APP 启动](https://api-docs.hubstudio.cn/380052356e0)

参数清单：无请求体参数。

##### 调用示例

**方式 A：启动应用**
```bash
hubstudio-cli cloud-mobile-app-start
```


##### `cloud-mobile-app-restart`

Local API：`POST /api/v1/cloud-mobile/app/restart`

官网文档：[APP 重启](https://api-docs.hubstudio.cn/380052355e0)

参数清单：无请求体参数。

##### 调用示例

**方式 A：重启应用**
```bash
hubstudio-cli cloud-mobile-app-restart
```


##### `cloud-mobile-app-stop`

Local API：`POST /api/v1/cloud-mobile/app/stop`

官网文档：[APP 停止](https://api-docs.hubstudio.cn/380052357e0)

参数清单：无请求体参数。

##### 调用示例

**方式 A：停止应用**
```bash
hubstudio-cli cloud-mobile-app-stop
```


##### `cloud-mobile-app-uninstall`

Local API：`POST /api/v1/cloud-mobile/app/uninstall`

官网文档：[APP 卸载](https://api-docs.hubstudio.cn/380052358e0)

参数清单：无请求体参数。

##### 调用示例

**方式 A：卸载应用**
```bash
hubstudio-cli cloud-mobile-app-uninstall
```


#### 3.2 文件管理

##### `cloud-mobile-upload-file`

Local API：`POST /api/v1/cloud-mobile/upload-file`

官网文档：[公网文件上传文件到云手机](https://api-docs.hubstudio.cn/395427282e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `downloadDest` | `string` | 否 | 上传至云手机中的目录 如果仅一层目录不存在，将自动创建；如果多层目录不存在，无法自动创建 |
| `fileUrl` | `string` | 否 | 文件地址 |
| `mobileId` | `integer` | 是 | 云手机ID |
| `fileName` | `string` | 否 | 文件名称 上传后文件叫什么名字 |

##### 调用示例

**方式 A：上传公网文件到云手机（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-upload-file downloadDest="/sdcard/Download/" fileUrl="http://example.com/app.apk" mobileId=10001 fileName="app.apk"
```


##### `cloud-mobile-setKeyBox`

Local API：`POST /api/v1/cloud-mobile/setKeyBox`

官网文档：[设置 keyBox](https://api-docs.hubstudio.cn/380407020e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mobileId` | `integer` | 是 | 云手机ID |
| `filePath` | `string` | 是 | 文件路径 例如：/sdcard/Download/xxx.xml |

##### 调用示例

**方式 A：设置 keyBox（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-setKeyBox mobileId=10001 filePath="/sdcard/Download/keybox.xml"
```


#### 3.3 RPA

##### `cloud-mobile-rpa-task-page`

Local API：`POST /api/v1/cloud-mobile/rpa/task/page`

官网文档：[RPA-计划分页查询](https://api-docs.hubstudio.cn/437761335e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `enabled` | `boolean` | 否 | 是否启用 true为启动，false为关闭 |
| `searchKey` | `string` | 否 | 搜索关键词 模糊搜索计划名称、计划描述、模板标题 |
| `taskState` | `integer` | 否 | 任务状态 0：等待执行；1：正在执行；2：执行完成；3：取消 |
| `current` | `string` | 否 | 当前页 默认值为第1页 |
| `size` | `string` | 否 | 每页条数 默认值为10条数据/页 |

##### 调用示例

**方式 A：分页查询 RPA 计划（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-rpa-task-page enabled=true searchKey="测试" current=1 size=10
```


##### `cloud-mobile-rpa-template-personal-page`

Local API：`POST /api/v1/cloud-mobile/rpa/template/personal/page`

官网文档：[RPA-个人模板分页查询](https://api-docs.hubstudio.cn/437761336e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `searchKey` | `string` | 否 | 搜索关键字 可模糊搜索标题和描述 |
| `current` | `string` | 否 | 当前页 默认值为第1页 |
| `size` | `string` | 否 | 每页条数 默认值为10条数据/页 |

##### 调用示例

**方式 A：分页查询个人 RPA 模板（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-rpa-template-personal-page searchKey="自动" current=1 size=10
```


##### `cloud-mobile-rpa-template-market-page`

Local API：`POST /api/v1/cloud-mobile/rpa/template/market/page`

官网文档：[RPA-市场模板分页查询](https://api-docs.hubstudio.cn/437761337e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `current` | `string` | 否 | 当前页 默认值为第1页 |
| `size` | `string` | 否 | 每页条数 默认值为10条数据/页 |
| `searchKey` | `string` | 否 | 搜索关键词 |

##### 调用示例

**方式 A：分页查询市场 RPA 模板（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-rpa-template-market-page current=1 size=10 searchKey="批量"
```


##### `cloud-mobile-rpa-task-save`

Local API：`POST /api/v1/cloud-mobile/rpa/task/save`

官网文档：[RPA-保存计划](https://api-docs.hubstudio.cn/437761338e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `cloudPhoneConfigs` | `array<object>` | 是 | 云手机配置集合 |
| `cloudPhoneConfigs[].cloudPhoneId` | `integer` | 否 | 云手机ID |
| `cloudPhoneConfigs[].templateParameter` | `string` | 否 | 模板参数 如果没有参数可不传 |
| `cloudPhoneConfigs[].triggerTime` | `string` | 否 | 触发时间 |
| `notes` | `string` | 否 | 计划描述 |
| `scheduleConfig` | `object` | 是 | 计划配置 |
| `scheduleConfig.endTime` | `string` | 否 | 结束时间 |
| `scheduleConfig.scheduleType` | `string` | 否 | 计划类型 ONCE-一次，DAILY-每天执行,可用值:ONCE,DAILY |
| `taskName` | `string` | 是 | 计划名称 |
| `templateId` | `integer` | 是 | 模板ID |
| `templateType` | `string` | 是 | 模板类型 PERSONAL：个人；MARKET：市场 |

##### 调用示例

**方式 A：保存个人 RPA 计划（--json 格式，包含嵌套对象与数组）**
```bash
hubstudio-cli cloud-mobile-rpa-task-save --json '{
  "taskName": "每日微信点赞",
  "templateId": 102,
  "templateType": "PERSONAL",
  "cloudPhoneConfigs": [
    {"cloudPhoneId": 10001}
  ],
  "scheduleConfig": {
    "scheduleType": "DAILY",
    "endTime": "2026-12-31 23:59:59"
  }
}'
```


##### `cloud-mobile-rpa-task-cancel`

Local API：`POST /api/v1/cloud-mobile/rpa/task/cancel`

官网文档：[RPA-取消计划](https://api-docs.hubstudio.cn/437761339e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 是 | 计划ID |

##### 调用示例

**方式 A：取消指定的 RPA 计划（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-rpa-task-cancel id=10001
```


##### `cloud-mobile-rpa-subTask-page`

Local API：`POST /api/v1/cloud-mobile/rpa/subTask/page`

官网文档：[RPA-执行记录分页查询](https://api-docs.hubstudio.cn/437761340e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `beginTime` | `string` | 否 | 开始时间 |
| `endTime` | `string` | 否 | 结束时间 |
| `mobileId` | `integer` | 否 | 云手机ID |
| `mobileName` | `string` | 否 | 云手机名称 |
| `searchKey` | `string` | 否 | 搜索关键词 模糊搜索计划名称、模板标题 |
| `taskId` | `integer` | 否 | 计划ID |
| `taskState` | `integer` | 否 | 任务状态 0：等待执行；1：正在执行；2：排队中；3：成功；4：失败；5：已取消 |
| `templateId` | `integer` | 否 | 模板ID |
| `current` | `string` | 否 | 当前页 默认值为第1页 |
| `size` | `string` | 否 | 每页条数 默认值为10条数据/页 |

##### 调用示例

**方式 A：分页查询 RPA 执行记录（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-rpa-subTask-page mobileId=10001 current=1 size=10
```


##### `cloud-mobile-rpa-subTask-cancel`

Local API：`POST /api/v1/cloud-mobile/rpa/subTask/cancel`

官网文档：[RPA-取消记录](https://api-docs.hubstudio.cn/437761341e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 是 | 任务ID |

##### 调用示例

**方式 A：取消指定的 RPA 执行记录（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-rpa-subTask-cancel id=12345
```


##### `cloud-mobile-rpa-subTask-detail`

Local API：`POST /api/v1/cloud-mobile/rpa/subTask/detail`

官网文档：[RPA-记录详情查询](https://api-docs.hubstudio.cn/437761342e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `string` | 是 | 记录编号 |

##### 调用示例

**方式 A：查询 RPA 执行记录详情（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-rpa-subTask-detail id="sub_task_uuid_12345"
```


##### `cloud-mobile-rpa-onceTask-save`

Local API：`POST /api/v1/cloud-mobile/rpa/onceTask/save`

官网文档：[RPA-快速保存一次性计划](https://api-docs.hubstudio.cn/437761343e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mobileId` | `integer` | 是 | 云手机ID |
| `description` | `string` | 否 | 计划描述 |
| `scheduleName` | `string` | 是 | 计划名称 |
| `templateId` | `integer` | 是 | 模板ID 个人模板或市场模板ID |
| `templateParameter` | `string` | 否 | 模板参数 JSON格式 |

##### 调用示例

**方式 A：快速保存一次性 RPA 计划（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-rpa-onceTask-save mobileId=10001 scheduleName="一次性任务" templateId=102
```


#### 3.4 其它云手机命令

##### `cloud-mobile-mobile-product-list`

Local API：`POST /api/v1/cloud-mobile/mobile-product-list`

官网文档：[云手机商品列表](https://api-docs.hubstudio.cn/380052338e0)

参数清单：无请求体参数。

##### 调用示例

**方式 A：获取云手机商品列表**
```bash
hubstudio-cli cloud-mobile-mobile-product-list
```


##### `cloud-mobile-mobile-page`

Local API：`POST /api/v1/cloud-mobile/mobile-page`

官网文档：[云手机分页列表](https://api-docs.hubstudio.cn/380052339e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `adbStatus` | `integer` | 否 | ADB状态 1-开启 0-关闭 |
| `current` | `integer` | 否 | 当前页数 默认为1 |
| `mobileIds` | `array<string>` | 否 | 手机IDS |
| `name` | `string` | 否 | 云手机名称 |
| `size` | `integer` | 否 | 每页显示记录数 默认10，最多200条 |
| `state` | `boolean` | 否 | 云手机状态 true-开机，false-关机 |

##### 调用示例

**方式 A：分页查询云手机列表（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-mobile-page state=true current=1 size=10
```

**方式 B：批量查询指定手机 ID 列表（--json 格式，包含数组）**
```bash
hubstudio-cli cloud-mobile-mobile-page --json '{"mobileIds": ["10001", "10002"]}'
```


##### `cloud-mobile-add-mobile`

Local API：`POST /api/v1/cloud-mobile/add-mobile`

官网文档：[添加云手机](https://api-docs.hubstudio.cn/380052340e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `count` | `integer` | 否 | 创建按需云手机的数量 默认为1 |
| `productId` | `integer` | 是 | 云手机商品ID 通过/api/v1/cloud-mobile/mobile-product-list获取 |

##### 调用示例

**方式 A：批量添加云手机（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-add-mobile count=2 productId=1001
```


##### `cloud-mobile-get-country-time-zone-language-list`

Local API：`POST /api/v1/cloud-mobile/get-country-time-zone-language-list`

官网文档：[国家时区语言列表](https://api-docs.hubstudio.cn/380052341e0)

参数清单：无请求体参数。

##### 调用示例

**方式 A：获取国家、时区、语言列表**
```bash
hubstudio-cli cloud-mobile-get-country-time-zone-language-list
```


##### `cloud-mobile-power-on-mobile`

Local API：`POST /api/v1/cloud-mobile/power-on-mobile`

官网文档：[批量开启云手机](https://api-docs.hubstudio.cn/380052342e0)

参数清单：无请求体参数。

##### 调用示例

**方式 A：批量开机所有关机的云手机**
```bash
hubstudio-cli cloud-mobile-power-on-mobile
```


##### `cloud-mobile-shutdown-mobile`

Local API：`POST /api/v1/cloud-mobile/shutdown-mobile`

官网文档：[批量关闭云手机](https://api-docs.hubstudio.cn/380052343e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `check` | `boolean` | 否 | 强制关闭正在使用中的云手机。`true`-开启使用验证，`false`-不验证，默认值 `true` |
| `mobileIds` | `array<string>` | 是 | 云手机ID列表 限制20个ID |

##### 调用示例

**方式 A：批量关机指定的云手机（--json 格式，包含数组）**
```bash
hubstudio-cli cloud-mobile-shutdown-mobile --json '{"check": true, "mobileIds": ["10001", "10002"]}'
```


##### `cloud-mobile-update-proxy`

Local API：`POST /api/v1/cloud-mobile/update-proxy`

官网文档：[更新代理](https://api-docs.hubstudio.cn/380052344e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `asDynamicType` | `integer` | 否 | 是否动态（网络设置） 1-静态，2-动态，默认1 |
| `automaticPositioning` | `boolean` | 否 | 是否自动定位（定位设置） 默认true |
| `country` | `string` | 否 | 国家code（时区/语言设置） |
| `dnsStrategy` | `integer` | 否 | DNS策略（网络设置） 0-跟随IP，1-DNS保护。默认0 |
| `followIp` | `boolean` | 否 | 跟随ip（时区/语言设置） 默认true |
| `lat` | `integer` | 否 | 维度（定位设置） |
| `lng` | `integer` | 否 | 经度（定位设置） |
| `mobileId` | `integer` | 是 | 云手机ID |
| `proxyAccount` | `string` | 否 | 代理账号（网络设置） |
| `proxyHost` | `string` | 是 | 代理主机地址（网络设置） |
| `proxyPassword` | `string` | 否 | 代理密码（网络设置） |
| `proxyPort` | `integer` | 是 | 代理端口（网络设置） |
| `proxyTypeId` | `integer` | 是 | 代理类型（网络设置）。`1`-`HTTP`，`2`-`HTTPS`，`4`-`Socks5`，`5`-`Oxylabsauto`，`6`-`Lumauto`，`7`-`Luminati`，`11`-`smartproxy` |
| `proxyTypeId2` | `integer` | 否 | 代理类型2（网络设置）。当 `proxyTypeId` = `7` 时可选配。可选值：`1`-`HTTP`，`2`-`HTTPS`，`4`-`Socks5`。默认 `1` |
| `referenceCity` | `string` | 否 | 参考城市（网络设置） |
| `referenceCountryCode` | `string` | 否 | 参考国家code（网络设置） |
| `referenceRegionCode` | `string` | 否 | 参考州code（网络设置） |
| `timeZone` | `string` | 否 | 时区（时区/语言设置） |
| `ysjLanguage` | `string` | 否 | 语言（时区/语言设置） |
| `ipDatabaseChannel` | `integer` | 否 | 代理检测渠道 1-IP2Location 2-DB-IP 3-MaxMind |

##### 调用示例

**方式 A：更新云手机代理配置（--json 格式）**
```bash
hubstudio-cli cloud-mobile-update-proxy --json '{
  "mobileId": 10001,
  "proxyTypeId": 4,
  "proxyHost": "127.0.0.1",
  "proxyPort": 1080
}'
```


##### `cloud-mobile-list-adb`

Local API：`POST /api/v1/cloud-mobile/list-adb`

官网文档：[批量获取云手机 ADB 状态](https://api-docs.hubstudio.cn/380052345e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mobileIds` | `array<string>` | 是 | 云手机ID列表 限制20个ID |

##### 调用示例

**方式 A：批量获取 ADB 状态（--json 格式，包含数组）**
```bash
hubstudio-cli cloud-mobile-list-adb --json '{"mobileIds": ["10001", "10002"]}'
```


##### `cloud-mobile-batch-update-adb`

Local API：`POST /api/v1/cloud-mobile/batch-update-adb`

官网文档：[批量更新云手机 ADB 状态](https://api-docs.hubstudio.cn/380052346e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `enableAdb` | `boolean` | 是 | 是否开启adb true-开启，false-关闭 |
| `mobileIds` | `array<string>` | 是 | 云手机ID列表 限制20个ID |

##### 调用示例

**方式 A：批量开启 ADB 状态（--json 格式，包含数组）**
```bash
hubstudio-cli cloud-mobile-batch-update-adb --json '{"enableAdb": true, "mobileIds": ["10001", "10002"]}'
```


##### `cloud-mobile-new-machine`

Local API：`POST /api/v1/cloud-mobile/new-machine`

官网文档：[一键新机](https://api-docs.hubstudio.cn/380052347e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mobileId` | `integer` | 是 | 云手机ID |
| `brand` | `string` | 否 | 品牌 通过/api/v1/cloud-mobile/brand/models获得 |
| `model` | `string` | 否 | 机型 通过/api/v1/cloud-mobile/brand/models获得 |

##### 调用示例

**方式 A：一键新机（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-new-machine mobileId=10001 brand="Xiaomi" model="小米10"
```


##### `cloud-mobile-new-machine-status`

Local API：`POST /api/v1/cloud-mobile/new-machine-status`

官网文档：[获取一键新机状态及可用数量](https://api-docs.hubstudio.cn/380052348e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mobileId` | `integer` | 是 | 云手机ID |

##### 调用示例

**方式 A：查询一键新机状态（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-new-machine-status mobileId=10001
```


##### `cloud-mobile-brand-models`

Local API：`POST /api/v1/cloud-mobile/brand/models`

官网文档：[查询品牌机型](https://api-docs.hubstudio.cn/380052351e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `productId` | `integer` | 是 | 云手机商品ID |

##### 调用示例

**方式 A：查询品牌机型列表（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-brand-models productId=1001
```


##### `cloud-mobile-exe-command`

Local API：`POST /api/v1/cloud-mobile/exe-command`

官网文档：[执行 shell 命令](https://api-docs.hubstudio.cn/380052359e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mobileId` | `integer` | 是 | 云手机ID |
| `command` | `string` | 是 | 命令 如果是需要执行多行,使用;号隔开即可 |

##### 调用示例

**方式 A：执行 shell 命令（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-exe-command mobileId=10001 command="pm list packages; getprop ro.product.model"
```


##### `cloud-mobile-update`

Local API：`POST /api/v1/cloud-mobile/update`

官网文档：[修改云手机信息](https://api-docs.hubstudio.cn/380052360e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mobileId` | `integer` | 是 | 云手机ID |
| `name` | `string` | 否 | 云手机名称 不超过60个字符 |
| `ignore` | `boolean` | 否 | 是否忽略云手机名称重复 默认：false |
| `remark` | `string` | 否 | 备注 不超过500个字符 |
| `number` | `integer` | 否 | 序号 最大值不超过 999999 |

##### 调用示例

**方式 A：修改云手机信息（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-update mobileId=10001 name="测试手机-01" remark="用于自动化脚本"
```


##### `cloud-mobile-simulateSendSms`

Local API：`POST /api/v1/cloud-mobile/simulateSendSms`

官网文档：[发送短信到云手机](https://api-docs.hubstudio.cn/395416906e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mobileId` | `integer` | 是 | 云手机ID 支持机型：Android13、支持Android14、支持Android15A |
| `senderNumber` | `string` | 是 | 发送方号码 （最长21位，允许数字+空格） |
| `smsContent` | `string` | 是 | 短信内容 （长度限制127位） |

##### 调用示例

**方式 A：向云手机模拟发送短信（key=value 格式）**
```bash
hubstudio-cli cloud-mobile-simulateSendSms mobileId=10001 senderNumber="13800138000" smsContent="您的验证码为 123456"
```


##### `cloud-mobile-del-mobile-batch`

Local API：`POST /api/v1/cloud-mobile/del-mobile-batch`

官网文档：[批量删除云手机](https://api-docs.hubstudio.cn/387838493e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mobileIds` | `array<string>` | 是 | 云手机列表 |

##### 调用示例

**方式 A：批量删除云手机（--json 格式，包含数组）**
```bash
hubstudio-cli cloud-mobile-del-mobile-batch --json '{"mobileIds": ["10001", "10002"]}'
```


##### `cloud-mobile-set-tag`

Local API：`POST /api/v1/cloud-mobile/set-tag`

官网文档：[批量修改云手机分组](https://api-docs.hubstudio.cn/387998970e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mobileIds` | `array<string>` | 是 | 云手机列表 |
| `tagName` | `string` | 是 | 分组名称 |

##### 调用示例

**方式 A：批量修改云手机分组（--json 格式，包含数组）**
```bash
hubstudio-cli cloud-mobile-set-tag --json '{"mobileIds": ["10001", "10002"], "tagName": "开发测试组"}'
```


### 4. 平台账号管理

#### `account-list`

Local API：`POST /api/v1/account/list`

官网文档：[账号分页列表](https://api-docs.hubstudio.cn/380052371e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `accountName` | `string` | 否 | 账号 |
| `current` | `integer` | 否 | 当前页 |
| `name` | `string` | 否 | 账号名称 对该账号的描述，传null和空字段等于没有过滤该字段 |
| `size` | `integer` | 否 | 每页数据 |

##### 调用示例

**方式 A：分页查询平台账号（key=value 格式）**
```bash
hubstudio-cli account-list accountName="test@example.com" current=1 size=10
```


#### `account-update`

Local API：`POST /api/v1/account/update`

官网文档：[账号更新](https://api-docs.hubstudio.cn/380052372e0)

参数清单：无请求体参数。

##### 调用示例

**方式 A：更新账号**
```bash
hubstudio-cli account-update
```


#### `container-add-account`

Local API：`POST /api/v1/container/add-account`

官网文档：[添加环境账号](https://api-docs.hubstudio.cn/380052370e0)

参数清单：无请求体参数。

##### 调用示例

**方式 A：添加环境账号**
```bash
hubstudio-cli container-add-account
```


#### `account-del`

Local API：`POST /api/v1/account/del`

官网文档：[账号删除](https://api-docs.hubstudio.cn/380052369e0)

参数清单：无请求体参数。

##### 调用示例

**方式 A：删除账号**
```bash
hubstudio-cli account-del
```


### 5. 分组管理

#### `group-list`

Local API：`POST /api/v1/group/list`

官网文档：[获取环境分组列表](https://api-docs.hubstudio.cn/380052375e0)

参数清单：无请求体参数。

##### 调用示例

**方式 A：获取环境分组列表**
```bash
hubstudio-cli group-list
```


#### `group-create`

Local API：`POST /api/v1/group/create`

官网文档：[新建环境分组](https://api-docs.hubstudio.cn/380052374e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `tagName` | `string` | 是 | 环境分组名 |

##### 调用示例

**方式 A：创建环境分组（key=value 格式）**
```bash
hubstudio-cli group-create tagName="独立测试分组"
```


#### `group-del`

Local API：`POST /api/v1/group/del`

官网文档：[删除环境分组](https://api-docs.hubstudio.cn/380052373e0)

参数清单：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `tagCode` | `string` | 是 | 删除指定名称的分组 |

##### 调用示例

**方式 A：删除环境分组（key=value 格式）**
```bash
hubstudio-cli group-del tagCode="tag_code_12345"
```


## 常见问题

| 问题 | 可能原因 | 处理方式 |
| --- | --- | --- |
| 连接失败 / `ECONNREFUSED` | 客户端未启动或 Local API 未开启 | 确认 Hubstudio 已启动、已登录、本地 API 已开启；必要时用 `-p` 指定端口。 |
| 返回未授权 | 开启了本地 API 安全认证 | 传入 `--local-api-key`，或设置环境变量 `HUBSTUDIO_LOCAL_API_KEY`。 |
| 读不到端口 | 客户端未运行、Local API 未就绪或 IPC 查询失败 | 确认客户端已启动并开启 Local API，或直接用 `-p` 指定端口。 |
| 找不到某个子命令 | 当前 CLI 版本暂未内置该命令 | 确认使用的是最新版本 `hubstudio-cli`；如仍缺失，请联系 Hubstudio 支持。 |


### 云手机 > RPA - RPA模板参数填写规范

来源：https://api-docs.hubstudio.cn/8441493m0.md

templateParameter 参数规范
 1. 参数来源
参数键（key）必须从以下接口返回的 parameter 字段中提取：
- 获取市场模板页面接口
- 获取个人模板页面接口
2. 数据结构
- 参数以 JSON 对象格式提交：{"key1": "value1", "key2": "value2"}
- 最终参数值需根据业务逻辑反序列化后使用
3. 常见类型参数
以下参数类型直接按对应格式传值：
- string（字符串）：{"name": "John"}
- decimal / number（数字）：{"amount": 100.50}
- boolean（布尔值）：{"enabled": true}
4. 媒体类型参数
对于图片（pic）、视频（video）等媒体参数，除基础信息外，必须包含固定键 __Extra__。示例格式如下：
```json
{
    "__Extra__": {
        "pic": {
            "name": "pic_name",
            "size": 204800
        },
        "video": {
            "name": "video_name",
            "size": 204800000
        }
    },
    "pic": "https://xxxxx.com/test.png",
    "video": "https://xxxxx.com/baby.mp4"
}
```
5. 参数配置示例
以下示例演示使用市场模板保存计划时，如何配置 templateParameter 参数：
- 步骤 1：获取模板参数
调用市场模板接口：
```
GET http://127.0.0.1:6873/api/v1/cloud-mobile/rpa/template/market/page
```
从响应中提取 parameter 字段，例如：
```json
{
    "code": 0,
    "msg": "Success",
    "requestId": "d8068c6d-e352-42bf-be4a-bcd1ec4fdf4c",
    "timestamp": 1775117889854,
    "data": {
        "list": [
            {
                "id": 7,
                "sort": 0,
                "title": "TikTok 智能养号",
                "type": 1,
                "description": "支持自定义编排流程",
                "parameter": "{\"点赞概率\":{\"extra\":{\"type\":\"integer\",\"required\":false,\"name\":\"点赞概率\",\"index\":3,\"numberRange\":{\"enabled\":true,\"min\":0,\"max\":95},\"defaultValue\":{\"enabled\":true,\"value\":25}},\"type\":\"integer\",\"default\":25},\"关注概率\":{\"extra\":{\"type\":\"integer\",\"required\":false,\"name\":\"关注概率\",\"index\":2,\"numberRange\":{\"enabled\":true,\"min\":0,\"max\":95},\"defaultValue\":{\"enabled\":true,\"value\":0}},\"type\":\"integer\",\"default\":0},\"查看评论概率\":{\"extra\":{\"type\":\"integer\",\"required\":false,\"name\":\"查看评论概率\",\"index\":4,\"numberRange\":{\"enabled\":true,\"min\":0,\"max\":95},\"defaultValue\":{\"enabled\":true,\"value\":5}},\"type\":\"integer\",\"default\":5},\"预估浏览视频数量\":{\"extra\":{\"type\":\"integer\",\"required\":true,\"name\":\"预估浏览视频数量\",\"index\":1,\"defaultValue\":{\"enabled\":true,\"value\":130},\"numberRange\":{\"enabled\":true,\"min\":1,\"max\":1000}},\"type\":\"integer\",\"default\":130}}",
                "updateDate": "2026-03-26T10:24:39",
                "appIconUrls": [
               "http://xxxxx.com/3f86a248e7726c8b05369322584ee087.png"
                ],
                "author": "赵"
            },
            {
                "id": 6,
                "sort": 0,
                "title": "TikTok 发布视频",
                "type": 1,
                "description": "支持自定义编排流程",
                "parameter": "{\"选择视频\":{\"extra\":{\"type\":\"video\",\"required\":true,\"sizeLimit\":{\"value\":300,\"unit\":\"MB\"},\"formatType\":[\"mp4\",\"mov\"],\"name\":\"选择视频\",\"index\":1},\"type\":\"video\"},\"视频文案\":{\"extra\":{\"type\":\"string\",\"required\":false,\"multiline\":{\"enabled\":true,\"value\":3},\"name\":\"视频文案\",\"charValid\":{\"enabled\":true,\"value\":1000},\"index\":2,\"placeholder\":\"为视频添加描述性的文案。\"},\"type\":\"string\"},\"封面图片\":{\"extra\":{\"type\":\"image\",\"required\":false,\"sizeLimit\":{\"value\":5,\"unit\":\"MB\"},\"formatType\":[\"jpg\",\"png\",\"jpeg\"],\"name\":\"封面图片\",\"index\":3},\"type\":\"image\"},\"AI标签\":{\"extra\":{\"type\":\"boolean\",\"required\":false,\"name\":\"AI标签\",\"index\":4,\"placeholder\":\"添加后，您的内容将显示一条标签，表明它是 AI 生成或 AI 编辑的内容。\"},\"type\":\"boolean\"},\"获取线索\":{\"extra\":{\"type\":\"boolean\",\"required\":false,\"name\":\"获取线索\",\"index\":5,\"placeholder\":\"在视频中添加“获取线索”组件？需要已启用“获取线索”功能的商业账号。\"},\"type\":\"boolean\"},\"商品ID\":{\"extra\":{\"type\":\"string\",\"required\":false,\"multiline\":{\"enabled\":true,\"value\":2},\"name\":\"商品ID\",\"charValid\":{\"enabled\":true,\"value\":50},\"index\":6,\"placeholder\":\"选填。输入商品 ID 以在视频中挂载商品。如果账号不支持此功能，发布将会失败。\"},\"type\":\"string\"},\"商品标题\":{\"extra\":{\"type\":\"string\",\"required\":false,\"multiline\":{\"enabled\":true,\"value\":2},\"name\":\"商品标题\",\"charValid\":{\"enabled\":true,\"value\":30},\"index\":7,\"placeholder\":\"选填。输入小黄车对应的商品标题或描述，最多 30 个字符。\"},\"type\":\"string\"},\"评论\":{\"extra\":{\"type\":\"string\",\"required\":false,\"multiline\":{\"enabled\":true,\"value\":3},\"name\":\"评论\",\"charValid\":{\"enabled\":true,\"value\":200},\"index\":8,\"placeholder\":\"视频发布成功后自动发布一条评论，最多 200 个字符。\"},\"type\":\"string\"}}",
                "updateDate": "2026-03-25T20:24:13",
                "appIconUrls": [
              "http://xxxxx.com/3f86a248e7726c8b05369322584ee087.png"
                ],
                "author": "赵"
            },
            {
                "id": 4,
                "sort": 0,
                "title": "Instagram 智能养号",
                "type": 1,
                "description": "支持自定义编排流程",
                "parameter": "{\"Probability of liking\":{\"extra\":{\"type\":\"integer\",\"required\":false,\"name\":\"Probability of liking\",\"index\":3,\"numberRange\":{\"enabled\":true,\"min\":0,\"max\":90},\"defaultValue\":{\"enabled\":true,\"value\":5}},\"type\":\"integer\",\"default\":5},\"Probability of following\":{\"extra\":{\"type\":\"integer\",\"required\":false,\"name\":\"Probability of following\",\"index\":2,\"numberRange\":{\"enabled\":true,\"min\":0,\"max\":90},\"defaultValue\":{\"enabled\":true,\"value\":5}},\"type\":\"integer\",\"default\":5},\"Probability of viewing comments\":{\"extra\":{\"type\":\"integer\",\"required\":false,\"name\":\"Probability of viewing comments\",\"index\":4,\"numberRange\":{\"enabled\":true,\"min\":0,\"max\":90},\"defaultValue\":{\"enabled\":true,\"value\":5}},\"type\":\"integer\",\"default\":5},\"Estimated number of videos browsed\":{\"extra\":{\"type\":\"integer\",\"required\":true,\"name\":\"Estimated number of videos browsed\",\"index\":1,\"defaultValue\":{\"enabled\":true,\"value\":15},\"numberRange\":{\"enabled\":true,\"min\":1,\"max\":1000}},\"type\":\"integer\",\"default\":15}}",
                "updateDate": "2026-03-25T10:56:10",
                "appIconUrls": [
              "https://xxxxx.com/2e477dd8f50a42d3b116083247e7806d.png"
                ],
                "author": "Owen"
            },
            {
                "id": 5,
                "sort": 1,
                "title": "Instagram发布视频-Plus",
                "type": 1,
                "description": "支持自定义编排流程",
                "parameter": "{\"videoDownloadUrl\":{\"type\":\"video\",\"extra\":{\"name\":\"Select Video\",\"type\":\"video\",\"required\":true,\"defaultValue\":{\"enabled\":false},\"index\":1,\"sizeLimit\":{\"value\":200,\"unit\":\"MB\"},\"formatType\":[\"mp4\",\"mov\"]}},\"videoDescription\":{\"type\":\"string\",\"extra\":{\"name\":\"Video Caption\",\"type\":\"string\",\"required\":false,\"charValid\":{\"enabled\":true,\"value\":1000},\"multiline\":{\"enabled\":true,\"value\":3},\"defaultValue\":{\"enabled\":false},\"index\":2}},\"enableAddAILabel\":{\"type\":\"boolean\",\"extra\":{\"name\":\"AI Label\",\"type\":\"boolean\",\"required\":false,\"defaultValue\":{\"enabled\":false},\"index\":4}}}",
                "updateDate": "2026-04-01T17:07:44",
                "appIconUrls": [
              "https://xxxxx.com/2e477dd8f50a42d3b116083247e7806d.png"
                ],
                "author": "Owen"
            }
        ],
        "total": 4
    }
}
```


- 步骤 2：构建参数 JSON
根据参数要求构造 JSON 对象：

```JSON
{
  "__Extra__": {
    "videoDownloadUrl": {
      "name": "video_name.mp4",
      "size": 204800000
    }
  },
  "videoDownloadUrl": "https://xxxxx.com/baby.mp4"
}
```

- 转义 JSON 字符串
将 JSON 对象转换为转义后的字符串格式：

```
"{\"__Extra__\": {\"videoDownloadUrl\": {\"name\": \"video_name.mp4\",\"size\": 204800000}},\"videoDownloadUrl\": \"https://xxxxx.com/baby.mp4\"}"
```

- 步骤 4：调用接口
将转义后的字符串作为 templateParameter 参数的值，传入目标接口

接口信息
接口地址：POST /api/v1/cloud-mobile/rpa/task/save
