# BitBrowser API 本地整理文档

- 来源：https://doc2.bitbrowser.cn/jiekou.html
- 文档页数：12
- API 接口数：56

## 文档目录

- [⿻ API接口文档](#⿻-api接口文档)
- [本地服务指南（demo下载）](#本地服务指南（demo下载）)
- [API 常见问题（先看）](#api-常见问题（先看）)
- [API 请求参数示例](#api-请求参数示例)
- [浏览器窗口接口](#浏览器窗口接口)
- [浏览器分组接口](#浏览器分组接口)
- [浏览器窗口标签](#浏览器窗口标签)
- [本地库数据接口](#本地库数据接口)
- [无头模式指南](#无头模式指南)
- [附录](#附录)
- [📱 云手机环境接口](#📱-云手机环境接口)
- [云手机环境标签](#云手机环境标签)

## 接口速查

| 模块 | 接口 | 方法 | 路径 |
| --- | --- | --- | --- |
| 浏览器窗口接口 | 健康检查接口，无参数，可以用来测试 Local Server 是否连接成功 | POST | `/health` |
| 浏览器窗口接口 | 创建浏览器窗口，browserFingerPrint 指纹对象必传 | POST | `/browser/update` |
| 浏览器窗口接口 | 修改窗口与指纹指定字段值，支持批量修改 | POST | `/browser/update/partial` |
| 浏览器窗口接口 | 打开浏览器窗口 | POST | `/browser/open` |
| 浏览器窗口接口 | 关闭浏览器窗口 | POST | `/browser/close` |
| 浏览器窗口接口 | 重置浏览器关闭状态 | POST | `/browser/closing/reset` |
| 浏览器窗口接口 | 删除浏览器窗口 | POST | `/browser/delete` |
| 浏览器窗口接口 | 获取浏览器窗口详情 | POST | `/browser/detail` |
| 浏览器窗口接口 | 分页获取浏览器窗口列表，page 参数从 0 开始，0 是第一页的数据 | POST | `/browser/list` |
| 浏览器窗口接口 | 排列窗口以及调整窗口尺寸 | POST | `/windowbounds` |
| 浏览器窗口接口 | 一键自适应排列窗口 | POST | `/windowbounds/flexable` |
| 浏览器窗口接口 | 批量修改浏览器窗口分组 | POST | `/browser/group/update` |
| 浏览器窗口接口 | 批量修改窗口代理信息 | POST | `/browser/proxy/update` |
| 浏览器窗口接口 | 批量修改窗口备注 | POST | `/browser/remark/update` |
| 浏览器窗口接口 | 通过序号批量关闭窗口 | POST | `/browser/close/byseqs` |
| 浏览器窗口接口 | 关闭所有窗口，无参数 | POST | `/browser/close/all` |
| 浏览器窗口接口 | 获取已打开窗口的进程 pid 集合，也可以用来判断窗口是否已打开，支持批量查询 | POST | `/browser/pids` |
| 浏览器窗口接口 | 获取所有活着的已打开的窗口的进程 ID，会自动过滤掉已死掉的进程，无参数 | POST | `/browser/pids/all` |
| 浏览器窗口接口 | 获取活着的给定窗口的 pids，会检查进程，减少进程退出，但是窗口状态没关闭的问题 | POST | `/browser/pids/alive` |
| 浏览器窗口接口 | 批量删除窗口，一次最多 100 个 | POST | `/browser/delete/ids` |
| 浏览器窗口接口 | 清理窗口缓存，注意，会清理掉所有的本地缓存文件，和服务端缓存文件 | POST | `/cache/clear` |
| 浏览器窗口接口 | 保留扩展数据，删除窗口缓存 | POST | `/cache/clear/exceptExtensions` |
| 浏览器窗口接口 | 获取所有已打开窗口的调试端口 remote-debugging-port | POST | `/browser/ports` |
| 浏览器窗口接口 | 代理检测接口，可以用来查询代理信息，以及检测代理是否可用，注意如果 IP 需要在全局代理下使用，则要开全局 | POST | `/checkagent` |
| 浏览器窗口接口 | 随机指纹值，传入窗口 ID，随机一次指纹，返回指纹对象，注意，不是返回窗口对象，只返回指纹对象 | POST | `/browser/fingerprint/random` |
| 浏览器窗口接口 | 对已打开窗口设置实时 cookie | POST | `/browser/cookies/set` |
| 浏览器窗口接口 | 清空 cookie，7.0.2 及以上版本客户端支持 | POST | `/browser/cookies/clear` |
| 浏览器窗口接口 | 获取已打开窗口的实时 cookies，注意实时 cookie 可能一直在变，两次获取到的可能不一致 | POST | `/browser/cookies/get` |
| 浏览器窗口接口 | 格式化给定 cookie，方便用户使用 | POST | `/browser/cookies/format` |
| 浏览器窗口接口 | 获取所有显示器列表，无参数 | POST | `/alldisplays` |
| 浏览器窗口接口 | 执行 RPA 任务 | POST | `/rpa/run` |
| 浏览器窗口接口 | 停止 RPA 任务 | POST | `/rpa/stop` |
| 浏览器窗口接口 | 仿真输入，将会自动将剪贴板中的文本，延迟输入到页面的聚焦输入框中，注意：页面中必须有聚焦的输入框，否则无法输入 | POST | `/autopaste` |
| 浏览器窗口接口 | 读取本地 excel 文件内容，建议配合 RPA 使用 | POST | `/utils/readexcel` |
| 浏览器窗口接口 | 读取文本类文件内容，如 json,txt 等文本文件，建议配合 RPA 使用 | POST | `/undefined` |
| 浏览器分组接口 | 查询分组列表接口,page 从 0 开始 | POST | `/group/list` |
| 浏览器分组接口 | 添加分组 | POST | `/group/add` |
| 浏览器分组接口 | 修改分组 | POST | `/group/edit` |
| 浏览器分组接口 | 删除分组 | POST | `/group/delete` |
| 浏览器分组接口 | 获取分组详情 | POST | `/group/detail` |
| 浏览器窗口标签 | 获取浏览器窗口标签列表，无参数 | POST | `/browserTag/list` |
| 浏览器窗口标签 | 创建窗口标签 | POST | `/browserTag/create` |
| 浏览器窗口标签 | 修改窗口标签 | POST | `/browserTag/update` |
| 浏览器窗口标签 | 删除窗口标签，支持批量 | POST | `/browserTag/delete` |
| 浏览器窗口标签 | 更新窗口标签绑定关系 | POST | `/browserTag/updateRelation` |
| 本地库数据接口 | 分页查询日志列表 | POST | `/extralog/list` |
| 本地库数据接口 | 添加日志 | POST | `/extralog/add` |
| 本地库数据接口 | 删除日志 | POST | `/extralog/delete` |
| 本地库数据接口 | 获取日志详情 | POST | `/extralog/detail` |
| 本地库数据接口 | 清空日志记录，无参数，请小心操作，此接口将清空所有本地日志数据，无法恢复 | POST | `/extralog/clear` |
| 📱 云手机环境接口 | 获取云手机开机状态 | POST | `/phone/running` |
| 云手机环境标签 | 获取云手机标签列表，无参数 | POST | `/cloudPhoneTag/list` |
| 云手机环境标签 | 创建云手机标签 | POST | `/cloudPhoneTag/create` |
| 云手机环境标签 | 修改云手机标签 | POST | `/cloudPhoneTag/update` |
| 云手机环境标签 | 删除云手机标签，支持批量 | POST | `/cloudPhoneTag/delete` |
| 云手机环境标签 | 更新云手机标签绑定关系 | POST | `/cloudPhoneTag/updateRelation` |

## 详细文档


### ⿻ API接口文档

来源：https://doc2.bitbrowser.cn/jiekou.html

更新时间：2023-03-07T17:12:06+08:00

::catalog


### 本地服务指南（demo下载）

来源：https://doc2.bitbrowser.cn/jiekou/ben-di-fu-wu-zhi-nan.html

更新时间：2025-12-11T18:50:49+08:00

### **想加入交流群？**
如果您想加入【**API开发者**】交流群（仅限开发者，非开发者勿加，否则将被移出），或【**脚本开发定制**】交流群，**[请点击这里联系客服](<https://www.bitbrowser.cn/contact/>)**，我们将为您安排入群！
**温馨提示**：请根据自身需求选择要加入的群组，以确保交流内容的相关性和质量！

**注意：重要数据，比如钱包助记词等，务必要备份保存，防止不必要的数据损失**。

## **JavaScript 示例代码**

注意：demo里的API参数仅作为演示使用，详细参数，请以API文档为准。demo里包含puppeteer示例代码。

[nodejs-demo 下载](https://update.bitbrowser.cn/demos/nodejs-demo.zip)

## **Python示例代码**

注意：demo里的API参数仅作为演示使用，详细参数，请以API文档为准。demo里包含selenium与playwright的示例代码。

[python-demo 下载](https://update.bitbrowser.cn/demos/python-demo.zip)

## **E语言示例代码**

注意：demo里的API参数仅作为演示使用，详细参数，请以API文档为准。

[E语言demo 下载](https://update.bitbrowser.cn/demos/E%E8%AF%AD%E8%A8%80%E7%A4%BA%E4%BE%8B%E4%BB%A3%E7%A0%81.rar)

## **Postman调试demo，直接导入即可**

[postman调试文件下载](https://update.bitbrowser.cn/demos/Bitnet%20Export.postman_collection.json)

## **服务简介**

比特浏览器支持通过接口调用方式使用浏览器功能，帮助用户运行自有的自动化脚本。

## **使用方式**

1. 安装并登录比特浏览器
2. 打开系统设置，获取到本地接口url
3. 获取示例代码，运行脚本

## **接口使用概述**

* 接口请求方式均为post，传参方式均为body传参，传递json格式数据，不是form-data，也不是url传参数，强烈建议下载上方的postman 调试demo示例
* 接口返回json对象，success为true作成功，如有返回数据，附加在data对象中
* 接口返回success为false时，表示业务失败，可能为程序原因，或参数校验等原因，失败信息会附加到msg中

```
// success example
{
  success: true,
  data: {
    id: '2c9c29a28sdd33dds8026f78se380142',
    groupName: '新建分组测试'
  }
}

// failed example
{ success: false, msg: '分组id必传' }
```

## **API调用线路图参考**

![](../.topwrite/assets/image_1678181225158.png)


### API 常见问题（先看）

来源：https://doc2.bitbrowser.cn/jiekou/api-chang-jian-wen-ti.html

更新时间：2023-03-07T19:24:33+08:00

## **接口参数使用不规范而导致程序出错甚至闪退**

请注意，使用Local Api创建或修改窗口，或是其他操作时，接口参数一定要符合规范，不要随意删改参数，不要传递未知意义的值或类型。

## **使用按键精灵等工具调试接口时，提示传参格式不对**

Local Api的所有接口的参数传递方式为body传参，json格式，不接受request url、formdata、string等类型传参方式。建议使用postman调试接口，调通后，再进行脚本开发调用。postman调试示例如下：

![](<../.gitbook/assets/image (8) (1).png>)


### API 请求参数示例

来源：https://doc2.bitbrowser.cn/jiekou/request.html

更新时间：2024-12-25T11:58:08+08:00

> 以下接口参数，均为示例参数，仅展示了部分字段，详细字段，参考具体对应的接口文档。

## 创建 Windows 窗口示例参数

```json
// 接口：/browser/update
{
  "name": "windows browser",
  "browserFingerPrint": {
    "coreVersion": "118",
    "ostype": "PC",
    "os": "Win32",
    "osVersion": "11,10"
  },
  "proxyMethod": 2,
  "proxyType": "noproxy"
}
```

## 创建 Mac 窗口示例参数

```json
// 接口：/browser/update
{
  "name": "mac browser",
  "browserFingerPrint": {
    "coreVersion": "118",
    "ostype": "PC",
    "os": "MacIntel",
    "hardwareConcurrency": "10"
  },
  "proxyMethod": 2,
  "proxyType": "noproxy"
}
```

## 创建 Linux 窗口示例参数

```json
// 接口：/browser/update
{
  "name": "Linux browser",
  "browserFingerPrint": {
    "coreVersion": "124",
    "ostype": "PC",
    "os": "Linux x86_64"
  },
  "proxyMethod": 2,
  "proxyType": "noproxy"
}
```

## 创建 iPhone 窗口示例参数

```json
// 接口：/browser/update
{
  "name": "iphone browser",
  "browserFingerPrint": {
    "coreVersion": "118",
    "ostype": "IOS",
    "os": "iPhone",
    "openWidth": 500,
    "openHeight": 900,
    "resolutionType": "1",
    "resolution": "360x780",
    "devicePixelRatio": 3
  },
  "proxyMethod": 2,
  "proxyType": "noproxy"
}
```

## 创建 Android 窗口示例参数

```json
// 接口：/browser/update
{
  "name": "android browser",
  "browserFingerPrint": {
    "coreVersion": "118",
    "ostype": "Android",
    "os": "Linux armv81",
    "openWidth": 500,
    "openHeight": 900,
    "resolutionType": "1",
    "resolution": "360x780",
    "devicePixelRatio": 2
  },
  "proxyMethod": 2,
  "proxyType": "noproxy"
}
```

## 修改窗口信息，不改指纹

```json
// 接口：/browser/update/partial
// 按需要修改窗口信息，需要改什么信息，则传入什么字段，比如需要修改窗口名称，就传name字段，需要改备注，就传remark字段，不改的不需要传，具体字段与/browser/update接口的字段相同
{
  "ids": ["d2fc7636d01b4943a3c3bd2187ca2272"],
  "name": "我是修改的",
  "browserFingerPrint": {} // 不修改指纹，则无需传入指纹配置
}
```

## 修改窗口指纹信息

```json
// 接口：/browser/update/partial
// 按需修改窗口指纹信息，注意，修改设备类型的时候，比如PC修改到android，一定要把相关的字段都改到，参考以下
{
  "ids": ["d1982fc6322545d4a372275238b65c2f"],
  "browserFingerPrint": {
    "coreVersion": "118",
    "ostype": "Android",
    "os": "Linux armv81",
    "version": "",
    "osVersion": "",
    "platformVersion": "9.0.0",
    "batchRandom": true,
    "batchUpdateFingerPrint": true
  }
}
```

## 创建窗口，并设置 socks5 代理信息

```json
{
  "name": "windows browser",
  "browserFingerPrint": {
    "coreVersion": "118",
    "ostype": "PC",
    "os": "Win32",
    "osVersion": "11,10"
  },
  "proxyMethod": 2,
  "proxyType": "socks5",
  "host": "1.2.3.4",
  "port": 1020,
  "proxyUserName": "abc",
  "proxyPassword": "def"
}
```

## 创建窗口，并设置代理为 api 提取链接

```json
{
  "name": "windows browser",
  "browserFingerPrint": {
    "coreVersion": "118",
    "ostype": "PC",
    "os": "Win32",
    "osVersion": "11,10"
  },
  "ipCheckService": "ip123in",
  "proxyMethod": 3,
  "proxyType": "socks5",
  "dynamicIpUrl": "http://example.tiquip.com/tiqu",
  "dynamicIpChannel": "common",
  "isDynamicIpChangeIp": true
}
```


### 浏览器窗口接口

来源：https://doc2.bitbrowser.cn/jiekou/liu-lan-qi-jie-kou.html

更新时间：2026-06-22T12:16:45+08:00

## 接口说明

* 接口请求 Method 均为 POST，传参方式均为 body 传参，传递 json 格式数据，不是 form-data，也不是 url 传参数
* 接口返回 json 对象，返回对象中 success 为 true 表示成功，如有返回数据，附加在 data 对象中
* 接口返回 success 为 false 时，表示失败，可能为程序原因，或参数校验等原因，失败信息会附加到 msg 中

```json
// 请求成功返回示例
{
  "success": true,
  "data": {
    "id": "2c9c29a28sdd33dds8026f78se380142",
    "groupName": "新建分组测试"
  }
}

// 请求失败返回示例
{
  "success": false,
  "msg": "分组id必传"
}
```

## 健康检查接口，无参数，可以用来测试 Local Server 是否连接成功

**POST**: `/health`

```json
// 只会返回这一种数据
{
  "success": true
}
```

## 创建浏览器窗口，browserFingerPrint 指纹对象必传

**POST**: `/browser/update`

* 创建窗口需要随机指纹对象时，只传空对象 `{}` 即可，指纹值里，留空会随机；
* win7/win8/win server 2012 已经不再支持 109 及以上内核，所以以上系统，请指定 coreVersion 为 104 内核版本，防止指定 112 及以上版本内核时，出现打不开窗口的情况
* win8 以下不支持 firefox 火狐内核
* 创建窗口时，参数只需要提交必要参数，如代理信息，分组信息，指纹信息如无必要，不建议提交与修改

```json
// 创建Windows窗口，并设置socks5代理示例
{
  "name": "windows browser",
  "proxyMethod": 2,
  "proxyType": "socks5",
  "host": "1.2.3.4",
  "port": 1020,
  "proxyUserName": "abc",
  "proxyPassword": "def",
  "browserFingerPrint": {
    "coreVersion": "130",
    "ostype": "PC",
    "os": "Win32",
    "osVersion": "11,10"
  }
}
```

* 参数详情

| 名称                          | 类型      | 必选 | 说明                                                                                                                                              |
| --------------------------- | ------- | -- | ----------------------------------------------------------------------------------------------------------------------------------------------- |
| groupId                     | string  | 是  | 如果没有指定分组 ID，则系统会默认创建一个 API 分组，并把窗口分配到 API 分组里                                                                                                   |
| platform                    | string  | 否  | 账号平台 URL，如：<https://www.facebook.com>                                                                                                           |
| url                         | string  | 否  | 额外打开的 url，多个用逗号`,`连接                                                                                                                            |
| remark                      | string  | 否  | 浏览器窗口备注信息                                                                                                                                       |
| userName                    | string  | 否  | 平台账号用户名，用于自动填充                                                                                                                                  |
| password                    | string  | 否  | 平台账号密码，用户自动填充                                                                                                                                   |
| isSynOpen                   | number  | 否  | 多开设置，是否允许多个账号同时打开同一个浏览器窗口，值为1（允许），0（禁止）                                                                                                         |
| faSecretKey                 | string  | 否  | 2FA 密钥的 SecretKey                                                                                                                               |
| cookie                      | string  | 否  | 平台账号 cookie，json 格式的 cookie 字符串，必须符合标准，参考[示例](https://doc2.bitbrowser.cn/jiekou/fu-lu.html#cookie%E6%A0%BC%E5%BC%8F%E5%8C%96%E7%A4%BA%E4%BE%8B) |
| proxyMethod                 | number  | 是  | 代理方式，2 自定义，3 提取 IP，默认 2。注意：设置提取 IP 时，需要同时设置下方 dynamicIpUrl 等几个字段值                                                                               |
| proxyType                   | string  | 否  | 代理类型 \['noproxy', 'http', 'https', 'socks5', 'ssh']中一个，默认 noproxy，直连模式                                                                          |
| host                        | string  | 否  | 代理主机                                                                                                                                            |
| port                        | number  | 否  | 代理端口                                                                                                                                            |
| ipCheckService              | string  | 否  | IP 信息查询库，默认 ip123in，选项 ip-api、ip123in、luminati，luminati 为 Luminati 代理专用                                                                         |
| isIpv6                      | string  | 否  | IP 协议，是否是 IPv6，默认 false                                                                                                                         |
| proxyUserName               | string  | 否  | 代理账号                                                                                                                                            |
| proxyPassword               | string  | 否  | 代理账号密码                                                                                                                                          |
| refreshProxyUrl             | string  | 否  | 代理刷新 URL，这个是代理平台提供的                                                                                                                             |
| enableSocks5Udp             | boolean | 否  | 是否开启UDP协议，确保您的代理是socks5并且支持UDP                                                                                                                  |
| country                     | string  | 否  | 国家地区 code，使用动态代理可能用到                                                                                                                            |
| province                    | string  | 否  | 州/省 code，使用动态代理可能用到                                                                                                                             |
| city                        | string  | 否  | 城市 code，使用动态代理可能用到                                                                                                                              |
| workbench                   | string  | 否  | 浏览器窗口工作台页面，localserver 或 disable，默认 localserver，不需要显示工作台时，设置 disable                                                                            |
| abortImage                  | boolean | 否  | 禁止加载图片，默认 false                                                                                                                                 |
| abortImageMaxSize           | number  | 否  | 禁止加载固定大小以上的图片，如 10KB，必须 abortImage 为 true 时生效，默认 0，禁止加载所有图片                                                                                     |
| abortMedia                  | boolean | 否  | 禁止视频自动播放，默认 false                                                                                                                               |
| muteAudio                   | boolean | 否  | 浏览器静音，默认 false                                                                                                                                  |
| stopWhileNetError           | boolean | 否  | 网络不通停止打开，默认 false                                                                                                                               |
| stopWhileIpChange           | boolean | 否  | IP 发生变化停止打开，默认 false                                                                                                                            |
| stopWhileCountryChange      | boolean | 否  | IP 对应国家发生变化，停止打开，默认 false                                                                                                                       |
| dynamicIpUrl                | string  | 否  | proxyMethod = 3 时，提取 IP 链接                                                                                                                      |
| dynamicIpChannel            | string  | 否  | 提取链接服务商，rola、doveip、cloudam、common，默认 common 即可                                                                                                 |
| isDynamicIpChangeIp         | boolean | 否  | 提取 IP，每次打开都提取新 IP，默认 false                                                                                                                      |
| duplicateCheck              | number  | 否  | 提取 IP 校验重复，1 校验，0 不校验。打开窗口时，将检测提取 IP 是否重复，重复则重新提取，最多重新提取 5 次                                                                                    |
| isGlobalProxyInfo           | boolean | 否  | 是否使用全局的动态代理信息，针对 iphtml，oxylabs，lumauto，ipidea 动态代理                                                                                             |
| syncTabs                    | boolean | 否  | 是否同步浏览器 tabs ，默认 true                                                                                                                           |
| syncCookies                 | boolean | 否  | 同步 Cookie，默认 true                                                                                                                               |
| syncIndexedDb               | boolean | 否  | 同步 IndexedDB，默认 false，极少的情况下才需要同步                                                                                                               |
| syncLocalStorage            | boolean | 否  | 同步 Local Storage 数据，默认 false                                                                                                                    |
| syncBookmarks               | boolean | 否  | 同步书签，默认 false                                                                                                                                   |
| syncAuthorization           | boolean | 否  | 同步已保存的密码，默认 false                                                                                                                               |
| credentialsEnableService    | boolean | 否  | 禁止保存密码弹窗，默认 false                                                                                                                               |
| syncHistory                 | boolean | 否  | 同步历史记录，默认 false                                                                                                                                 |
| syncExtensions              | boolean | 否  | 同步扩展应用数据，默认 false                                                                                                                               |
| isValidUsername             | boolean | 否  | 根据平台，用户名，密码，校验重复， false，创建时有效                                                                                                                   |
| allowedSignin               | boolean | 否  | 允许 google 账号登录浏览器，默认 false，使用 Google 账号登录到浏览器右上角后，可能会导致使用 Gmail 等谷歌服务时，跨设备不同步，不建议开启                                                             |
| clearCacheFilesBeforeLaunch | boolean | 否  | 启动前清理缓存文件                                                                                                                                       |
| clearCacheWithoutExtensions | boolean | 否  | 启动前清理缓存文件(保留扩展数据)                                                                                                                               |
| clearCookiesBeforeLaunch    | boolean | 否  | 启动前清理 cookie                                                                                                                                    |
| clearHistoriesBeforeLaunch  | boolean | 否  | 启动前清理历史记录                                                                                                                                       |
| randomFingerprint           | boolean | 否  | 每次启动均随机指纹                                                                                                                                       |
| disableGpu                  | boolean | 否  | 是否关闭 GPU 硬件加速，默认 false                                                                                                                          |
| disableTranslatePopup       | boolean | 否  | 禁止浏览器弹出谷歌翻译，默认 false                                                                                                                            |
| disableNotifications        | boolean | 否  | 禁止弹出消息通知弹窗，默认 false                                                                                                                             |
| disableClipboard            | boolean | 否  | 禁止网站读取剪贴板内容，默认 false                                                                                                                            |
| memorySaver                 | boolean | 否  | 省内存模式，开启后有可能会导致部分异常，不建议开启，默认 false                                                                                                              |
| browserFingerPrint          | object  | 是  | 指纹对象，参考下方指纹对象                                                                                                                                   |

```json
// browserFingerPrint 对象
{
  "coreProduct": "chrome", // 内核，chrome | firefox，默认chrome，需要火狐内核时，填firefox
  "coreVersion": "130", // chrome 内核默认 130，firefox内核默认 128，所有内核版本，参考客户端界面内可选值
  "ostype": "PC", // 操作系统平台 PC | Android | IOS
  "os": "Win32", // navigator.platform值，严格与操作系统一一对应, Windows => Win32, macOS => MacIntel, Linux => Linux x86_64, iOS => iPhone, Android => Linux armv81
  "osVersion": "", // 操作系统版本，不填时，按照os随机，填了以后，按照所填的值范围内随机，windows候选项 11,10，Android候选项14,13,12,11,10,9，iOS候选 17.0,16.6,16.5,16.4,16.3,16.2,16.1,16.0,15.7,15.6,15.5,15.4,15.3,15.2,15.1,15.0，可填多个值，逗号分隔，比如windows: '11,10'
  "version": "", //浏览器版本，不填则随机，建议与coreVersion版本保持一致
  //   以下指纹如无特殊需求，不建议修改，只传入上方几个指纹字段即可
  "userAgent": "", // ua，不填则自动生成
  "isIpCreateTimeZone": true, // 基于IP生成对应的时区
  "timeZone": "", // 时区，isIpCreateTimeZone 为false时，参考附录中的时区列表
  "timeZoneOffset": 0, // isIpCreateTimeZone 为false时设置，时区偏移量
  "webRTC": "3", //webrtc 0 => 替换, 1 => 允许, 2 => 禁止, 3 => 隐私
  "ignoreHttpsErrors": false, // 忽略https证书错误，true, false
  "position": "1", //地理位置 0 => 询问, 1 => 允许, 2 => 禁止
  "isIpCreatePosition": true, // 是否基于IP生成对应的地理位置
  "lat": "", // 纬度 isIpCreatePosition 为false时设置
  "lng": "", // 经度 isIpCreatePosition 为false时设置
  "precisionData": "", //精度米 isIpCreatePosition 为false时设置
  "isIpCreateLanguage": true, // 是否基于IP生成对应国家的浏览器语言
  "languages": "", // isIpCreateLanguage 为false时设置，值参考附录
  "isIpCreateDisplayLanguage": false, // 是否基于IP生成对应国家的浏览器界面语言
  "displayLanguages": "", // isIpCreateDisplayLanguage 为false时设置，默认为空，即跟随系统，值参考附录
  "openWidth": 1280, // 窗口宽度，只是设置窗口打开时的尺寸，与指纹无关
  "openHeight": 720, // 窗口高度，只是设置窗口打开时的尺寸，与指纹无关
  "resolutionType": "0", // 分辨率类型 0 => 跟随电脑, 1 => 自定义，默认建议跟随电脑
  "resolution": "1920 x 1080", // 自定义分辨率时，具体值
  "windowSizeLimit": true, // 分辨率类型为自定义，且ostype为PC时，此项有效，约束窗口最大尺寸不超过分辨率
  "devicePixelRatio": 1, // 显示缩放比例，默认1，填写时，建议 1, 1.5, 2, 2.5, 3
  "fontType": "2", // 字体生成类型 0 => 系统默认 | 2 => 随机
  "canvas": "0", //canvas 0随机｜1关闭
  "webGL": "0", //webGL图像，0随机｜1关闭
  "webGLMeta": "0", //webgl元数据 0自定义｜1关闭
  "webGLManufacturer": "", // webGLMeta 自定义时，webGL厂商值，建议留空会自动生成
  "webGLRender": "", // webGLMeta自定义时，webGL渲染值，建议留空自动生成
  "audioContext": "0", // audioContext值，0随机｜1关闭
  "mediaDevice": "0",  // 媒体设备，0 随机 | 1 关闭
  "speechVoices": "0", // Speech Voices，0随机｜1关闭
  "hardwareConcurrency": "4", // 硬件并发数
  "deviceMemory": "8", // 设备内存，4，8，不要传入大于8的值
  "doNotTrack": "1", // doNotTrack 1开启｜0关闭
  "clientRectNoiseEnabled": true, // ClientRects true使用相匹配的值代替您真实的ClientRects | false每个浏览器使用当前电脑默认的ClientRects
  "portScanProtect": "0", // 端口扫描保护 0开启｜1关闭，注意默认开启保护，组织所有本地127的ws链接，比如某些打印机之类的，如有连接本地服务需求，建议关闭，或者在 portWhiteList 中，填写对应端口，加入白名单
  "portWhiteList": "", // 端口扫描保护开启时的白名单，逗号分隔
  "deviceInfoEnabled": true, // 自定义设备信息，默认开启
  "computerName": "", // deviceInfoEnabled 为true时设置，建议留空系统自动生成即可
  "macAddr": "", // deviceInfoEnabled 为true时设置，建议留空系统自动生成即可
  "hostIP": "", // deviceInfoEnabled 为true时设置，建议留空系统自动生成即可
  "disableSslCipherSuitesFlag": false, // ssl是否禁用特性，默认不禁用，注意开启后自定义设置时，有可能会导致某些网站无法访问
  "disableSslCipherSuites": null, // ssl 禁用特性，序列化的ssl特性值，参考附录
  "enablePlugins": false, // 是否启用插件指纹
  "plugins": "", // enablePlugins为true时，序列化的插件值，插件指纹值参考附录
  "launchArgs": "" // 启动参数，如无痕模式打开，那么设置启动参数为 "--incognito", 多个启动参数用逗号分隔，如 "--incognito,--no-sandbox"
}
```

## 修改窗口与指纹指定字段值，支持批量修改

只传需要更新的字段即可，如需要更新 name，则只传 name，具体所有可修改参数，均与 `/browser/update` 接口一致，修改代理使用代理专用接口 `/browser/proxy/update` 修改

**POST**: `/browser/update/partial`

```json
// 请求参数示例，比如批量修改两个窗口的name与groupId
{
  "ids": ["3baa6e990fee4e839c72722c8dc18019", "3baa6e990fee4e839c72722c8dc18011"],
  "name": "修改的name",
  "groupId": "41notc1202sr8gu5o6emb9ihaqbzbkic"
}
```

## 打开浏览器窗口

返回 ws 和 http 连接地址，以及 coreVersion 内核版本和 driver，chromedriver path

**POST**: `/browser/open`

```json
// Body 请求参数示例
{
  "id": "3baa6e990fee4e839c72722c8dc18019",
  "args": [],
  "queue": true
}
```

* 参数详情

| 名称                | 类型      | 必选 | 说明                                                                 |
| ----------------- | ------- | -- | ------------------------------------------------------------------ |
| id                | string  | 是  | 浏览器窗口 id，创建完会返回 ID，或者通过 list 接口查询，或者界面上配置里点击【复制 ID】按钮复制，注意，ID 不是序号 |
| args              | array   | 否  | 浏览器启动参数，合法的 chromium 启动参数均支持，数组类型                                  |
| queue             | boolean | 否  | 是否以队列方式打开，设置为 true 后，可有效防止多线程同时启动时导致的并发报错                          |
| ignoreDefaultUrls | boolean | 否  | 打开窗口时，忽略已同步的url，只打开空白页面或者工作台页面                                     |
| newPageUrl        | string  | 否  | 指定open时打开的url，ignoreDefaultUrls为true时可配置，不能单独使用                    |

* args 有用参数介绍
  1. \--remote-debugging-address=0.0.0.0，通放局域网端口，默认打开的窗口，ws 连接地址均为 127，使用此参数后，可以用局域网或者公网 IP 连接
  2. \--headless 无头模式，注意使用无头模式时，需要清空已同步或者设置的url，因为无头模式只支持打开时最多设置一个浏览器窗口页面
  3. \--incognito 隐私模式，无痕模式打开浏览器窗口
  4. \--load-extension=xxx/extension/path1,xxx/extension/path2 加载非扩展中心中的扩展，多个扩展使用逗号分隔
  5. 比如使用上述所有三个参数的话，args 参数为 `["--remote-debugging-address=0.0.0.0", "--incognito", "--load-extension=xxx/extension/path1,xxx/extension/path2"]`
  6. chromium 命令行参数参考: <https://peter.sh/experiments/chromium-command-line-switches/>

```json
// 打开窗口后，返回数据示例
{
  "success": true,
  "data": {
    "ws": "ws://127.0.0.1:53325/devtools/browser/857b2d0d-aae6-4852-ab3c-0784f0b2c1fb",
    "http": "127.0.0.1:53325",
    "coreVersion": "112",
    "driver": "/Users/ddd/Library/Application Support/Electron/chromedriver/112/chromedriver",
    "seq": 3474,
    "name": "",
    "remark": "",
    "groupId": "2c9c29a28161edd0018161f3790d0002",
    "pid": 31295
  }
}
```

## 关闭浏览器窗口

调用完不要立即删除窗口或者重新打开，等待 5 秒后进程彻底退出再操作

**POST**: `/browser/close`

```json
// Body 请求参数示例
{
  "id": "3baa6e990fee4e839c72722c8dc18019"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明                                                                 |
| -- | ------ | -- | ------------------------------------------------------------------ |
| id | string | 是  | 浏览器窗口 id，创建完会返回 ID，或者通过 list 接口查询，或者界面上配置里点击【复制 ID】按钮复制，注意，ID 不是序号 |

## 重置浏览器关闭状态

**注意：此接口仅用于窗口异常关闭后，再次打开时，提示“窗口正在打开中/关闭中”导致的 api 无法继续打开窗口时，可使用此接口重置窗口状态为未打开，使用此接口时，要确定窗口已经实际关闭**

**POST**: `/browser/closing/reset`

```json
// Body 请求参数示例
{
  "id": "3baa6e990fee4e839c72722c8dc18019"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明       |
| -- | ------ | -- | -------- |
| id | string | 是  | 浏览器窗口 id |

## 删除浏览器窗口

此删除为彻底删除接口，删除后无法从回收站找回

**POST**: `/browser/delete`

```json
// Body 请求参数示例
{
  "id": "3baa6e990fee4e839c72722c8dc18019"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明       |
| -- | ------ | -- | -------- |
| id | string | 是  | 浏览器窗口 id |

## 获取浏览器窗口详情

**POST**: `/browser/detail`

```json
// Body 请求参数示例
{
  "id": "3baa6e990fee4e839c72722c8dc18019"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明       |
| -- | ------ | -- | -------- |
| id | string | 是  | 浏览器窗口 id |

```json
// 返回窗口数据详情
{
  "success": true,
  "data": {
    "id": "af25e626167f4870b8f257e697bb4f05",
    "seq": 4447,
    "platform": "",
    "platformIcon": "",
    "url": "",
    "name": "windows browser",
    "userName": "",
    "password": "",
    "cookie": "",
    "otherCookie": "",
    "isGlobalProxyInfo": false,
    "isIpv6": false,
    "proxyMethod": 2,
    "proxyType": "socks5",
    "agentId": "",
    "ipCheckService": "ip123in",
    "host": "1.2.3.4",
    "port": 1020,
    "proxyUserName": "abc",
    "proxyPassword": "def",
    "lastIp": "",
    "lastCountry": "",
    "isIpNoChange": false,
    "ip": "",
    "country": "",
    "province": "",
    "city": "",
    "dynamicIpUrl": "",
    "isDynamicIpChangeIp": false,
    "remark": "",
    "status": 0,
    "operUserName": "",
    "isDelete": 0,
    "delReason": "",
    "isMostCommon": 0,
    "isRemove": 0,
    "tempStr": null,
    "createdBy": "2c9c29a27e230d14017e23c151ce0036",
    "userId": "2c9c29a27e230d14017e23c151ce0036",
    "createdTime": "2024-12-25 10:29:06",
    "recycleBinRemark": "",
    "mainUserId": "2c9c29a27e230d14017e23c151ce0036",
    "abortImage": false,
    "abortMedia": false,
    "stopWhileNetError": false,
    "stopWhileCountryChange": false,
    "syncTabs": true,
    "syncCookies": true,
    "syncIndexedDb": false,
    "syncBookmarks": false,
    "syncAuthorization": false,
    "syncHistory": false,
    "allowedSignin": false,
    "syncSessions": true,
    "workbench": "localserver",
    "clearCacheFilesBeforeLaunch": false,
    "clearCookiesBeforeLaunch": false,
    "clearHistoriesBeforeLaunch": false,
    "randomFingerprint": false,
    "muteAudio": false,
    "disableGpu": false,
    "abortImageMaxSize": null,
    "syncExtensions": false,
    "syncUserExtensions": false,
    "syncLocalStorage": false,
    "credentialsEnableService": false,
    "disableTranslatePopup": false,
    "stopWhileIpChange": false,
    "disableClipboard": false,
    "disableNotifications": false,
    "memorySaver": false,
    "browserFingerPrint": {
      "id": "b5396bfe57e14a5aa7c469b6b749420c",
      "seq": 4447,
      "coreVersion": "130",
      "browserId": "af25e626167f4870b8f257e697bb4f05",
      "ostype": "PC",
      "os": "Win32",
      "architecture": "x86",
      "osVersion": "10",
      "platformVersion": "10.0.0",
      "version": "129",
      "userAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.6668.100 Safari/537.36",
      "isIpCreateTimeZone": true,
      "timeZone": "",
      "timeZoneOffset": 0,
      "ignoreHttpsErrors": false,
      "webRTC": "3",
      "position": "1",
      "isIpCreatePosition": true,
      "isIpCreateDisplayLanguage": false,
      "displayLanguages": "",
      "isIpCreateLanguage": true,
      "resolutionType": "0",
      "resolution": "1920 x 1080",
      "openWidth": 1280,
      "openHeight": 720,
      "fontType": "2",
      "font": "Courier,System,Avenir Next,Avenir Next Condensed,Diwan Kufi,Chalkboard SE,Apple Chancery,Baghdad,STIXSizeOneSym,Phosphate",
      "canvas": "0",
      "canvasValue": "687534",
      "webGL": "0",
      "webGLValue": "76433",
      "webGLMeta": "0",
      "webGLManufacturer": "Google Inc. (Intel)",
      "webGLRender": "ANGLE (Intel, Intel(R) HD Graphics 3000 Direct3D11 vs_5_0 ps_5_0, D3D11)",
      "audioContext": "0",
      "audioContextValue": "52",
      "mediaDevice": "1",
      "speechVoices": "0",
      "speechVoicesValue": "[{\"name\":\"Microsoft Gadis - Indonesian (Indonesia)\",\"default\":false,\"lang\":\"id-ID\",\"is_remote\":false,\"voice_uri\":\"Microsoft Gadis - Indonesian (Indonesia)\"},{\"name\":\"Microsoft Grace - Maltese (Malta)\",\"default\":false,\"lang\":\"mt-MT\",\"is_remote\":false,\"voice_uri\":\"Microsoft Grace - Maltese (Malta)\"},{\"name\":\"Microsoft Gudrun - Icelandic (Iceland)\",\"default\":false,\"lang\":\"is-IS\",\"is_remote\":false,\"voice_uri\":\"Microsoft Gudrun - Icelandic (Iceland)\"},{\"name\":\"Google Bahasa Indonesia\",\"default\":false,\"lang\":\"id-ID\",\"is_remote\":true,\"voice_uri\":\"Google Bahasa Indonesia\"},{\"name\":\"Google Deutsch\",\"default\":false,\"lang\":\"de-DE\",\"is_remote\":true,\"voice_uri\":\"Google Deutsch\"},{\"name\":\"Google español\",\"default\":false,\"lang\":\"es-ES\",\"is_remote\":true,\"voice_uri\":\"Google español\"},{\"name\":\"Google español de Estados Unidos\",\"default\":false,\"lang\":\"es-US\",\"is_remote\":true,\"voice_uri\":\"Google español de Estados Unidos\"},{\"name\":\"Google français\",\"default\":false,\"lang\":\"fr-FR\",\"is_remote\":true,\"voice_uri\":\"Google français\"},{\"name\":\"Google italiano\",\"default\":false,\"lang\":\"it-IT\",\"is_remote\":true,\"voice_uri\":\"Google italiano\"},{\"name\":\"Google Nederlands\",\"default\":false,\"lang\":\"nl-NL\",\"is_remote\":true,\"voice_uri\":\"Google Nederlands\"},{\"name\":\"Google polski\",\"default\":false,\"lang\":\"pl-PL\",\"is_remote\":true,\"voice_uri\":\"Google polski\"},{\"name\":\"Google português do Brasil\",\"default\":false,\"lang\":\"pt-BR\",\"is_remote\":true,\"voice_uri\":\"Google português do Brasil\"},{\"name\":\"Google UK English Female\",\"default\":false,\"lang\":\"en-GB\",\"is_remote\":true,\"voice_uri\":\"Google UK English Female\"},{\"name\":\"Google UK English Male\",\"default\":false,\"lang\":\"en-GB\",\"is_remote\":true,\"voice_uri\":\"Google UK English Male\"},{\"name\":\"Google US English\",\"default\":false,\"lang\":\"en-US\",\"is_remote\":true,\"voice_uri\":\"Google US English\"},{\"name\":\"Google русский\",\"default\":false,\"lang\":\"ru-RU\",\"is_remote\":true,\"voice_uri\":\"Google русский\"},{\"name\":\"Google हिन्दी\",\"default\":false,\"lang\":\"hi-IN\",\"is_remote\":true,\"voice_uri\":\"Google हिन्दी\"},{\"name\":\"Google 國語（臺灣）\",\"default\":false,\"lang\":\"zh-TW\",\"is_remote\":true,\"voice_uri\":\"Google 國語（臺灣）\"},{\"name\":\"Google 日本語\",\"default\":false,\"lang\":\"ja-JP\",\"is_remote\":true,\"voice_uri\":\"Google 日本語\"},{\"name\":\"Google 한국의\",\"default\":false,\"lang\":\"ko-KR\",\"is_remote\":true,\"voice_uri\":\"Google 한국의\"},{\"name\":\"Google 普通话（中国大陆）\",\"default\":false,\"lang\":\"zh-CN\",\"is_remote\":true,\"voice_uri\":\"Google 普通话（中国大陆）\"},{\"name\":\"Google 粤語（香港）\",\"default\":false,\"lang\":\"zh-HK\",\"is_remote\":true,\"voice_uri\":\"Google 粤語（香港）\"}]",
      "hardwareConcurrency": "4",
      "deviceMemory": "8",
      "deviceInfoEnabled": true,
      "computerName": "DESKTOP-E9N2HJQP",
      "macAddr": "74-29-AF-3A-E9-63",
      "clientRectNoiseEnabled": true,
      "clientRectNoiseValue": 34307,
      "doNotTrack": "1",
      "portScanProtect": "0",
      "portWhiteList": "",
      "isDelete": 0,
      "colorDepth": 24,
      "totalDiskSpace": "4082286592",
      "devicePixelRatio": 1,
      "disableSslCipherSuitesFlag": false,
      "disableSslCipherSuites": null,
      "plugins": "",
      "enablePlugins": false,
      "windowSizeLimit": true,
      "createdBy": "2c9c29a27e230d14017e23c151ce0036",
      "createdTime": "2024-12-25 10:29:06",
      "isValidUsername": true,
      "abortImage": false,
      "abortImageMaxSize": null,
      "abortMedia": false,
      "stopWhileNetError": false,
      "stopWhileCountryChange": false,
      "syncTabs": true,
      "syncCookies": true,
      "syncIndexedDb": false,
      "syncBookmarks": false,
      "syncAuthorization": false,
      "syncHistory": false,
      "allowedSignin": false,
      "syncSessions": false,
      "workbench": "localserver",
      "clearCacheFilesBeforeLaunch": false,
      "clearCookiesBeforeLaunch": false,
      "clearHistoriesBeforeLaunch": false,
      "randomFingerprint": false,
      "muteAudio": false,
      "disableGpu": false,
      "syncExtensions": false,
      "syncUserExtensions": false,
      "syncLocalStorage": false,
      "credentialsEnableService": false,
      "disableTranslatePopup": false,
      "stopWhileIpChange": false,
      "disableClipboard": false,
      "disableNotifications": false,
      "memorySaver": false,
      "coreProduct": "chrome",
      "webgpu": {
        "driver": null,
        "vendor": "intel",
        "description": null,
        "device": null,
        "architecture": "gen-6"
      },
      "batchRandom": false,
      "batchUpdateFingerPrint": false,
      "firefoxVersionMap": {
        "120": "127,126,125,124,123,122,121,120,119,118,117",
        "128": "128,127,126,125"
      },
      "launchArgs": null,
      "uamodel": "",
      "extendOptions": null,
      "randomPlatformVersion": 0,
      "defaultAccuracy": null
    },
    "createdName": null,
    "belongUserName": null,
    "updateName": null,
    "agentIpCount": null,
    "belongToMe": false,
    "seqExport": null,
    "groupIDs": null,
    "browserShareID": null,
    "share": null,
    "shareUserName": null,
    "isShare": 0,
    "isValidUsername": false,
    "createNum": 0,
    "isRandomFinger": true,
    "remarkType": 1,
    "refreshProxyUrl": null,
    "duplicateCheck": 0,
    "ossExtend": null,
    "randomKey": null,
    "randomKeyUser": null,
    "syncBrowserAccount": null,
    "cookieBak": "",
    "passwordBak": null,
    "manual": 0,
    "proxyPasswordBak": null,
    "proxyAgreementType": null,
    "clearCacheWithoutExtensions": false,
    "syncPaymentsAndAddress": false,
    "extendIds": [],
    "isSynOpen": 1,
    "faSecretKey": null,
    "coreProduct": null,
    "ostype": null,
    "os": null,
    "sort": 0,
    "checkPassword": null
  }
}
```

## 分页获取浏览器窗口列表，page 参数从 0 开始，0 是第一页的数据

**注意：一次最多获取 100 条，超出最大限制仍然返回 100 条**

**POST**: `/browser/list`

```json
// Body 请求参数示例
{
  "page": 0,
  "pageSize": 10
}
```

* 参数详情

| 名称        | 类型      | 必选 | 说明                                  |
| --------- | ------- | -- | ----------------------------------- |
| page      | number  | 是  | 分页，从 0 开始                           |
| pageSize  | number  | 是  | 分页数量，最大 100，超出 100 仍然返回 100 条，默认 10 |
| groupId   | string  | 否  | 分组 ID，传入时查询此分组下的窗口列表                |
| name      | string  | 否  | 窗口名称，模糊匹配                           |
| remark    | string  | 否  | 序号，精确查询                             |
| seq       | number  | 否  | 窗口序号，查询指定序号的窗口，精确查询                 |
| minSeq    | number  | 否  | 最小序号，范围查询，不可与 seq 同时使用              |
| maxSeq    | number  | 否  | 最大序号，范围查询，不可与 seq 同时使用              |
| sort      | string  | 否  | 排序参数，只允许两个值，desc 倒序，asc 正序          |
| ownedByMe | boolean | 否  | 获取【自建】窗口，仅返回由当前登录账号创建的窗口            |
| opened    | boolean | 否  | 获取已打开的窗口，由于网络原因，此状态并不完全准确           |

## 排列窗口以及调整窗口尺寸

**POST**: `/windowbounds`

```json
// Body 请求参数示例
{
  "type": "box",
  "startX": 0,
  "startY": 0,
  "width": 500,
  "height": 400,
  "col": 3,
  "spaceX": 50,
  "spaceY": 50,
  "offsetX": 50,
  "offsetY": 50,
  "orderBy": "asc",
  "ids": ["6bb433a5833245039c13c822402ab30f", "799f01cbd1dd4e28b5ac6edcc88a00b5"], // 传入ids时会自动忽略 seqlist
  "seqlist": [4348]
}
```

* 参数详情

| 名称       | 类型     | 必选 | 说明                                                               |
| -------- | ------ | -- | ---------------------------------------------------------------- |
| type     | string | 是  | 排列方式，宫格 box ， 对角线 diagonal                                       |
| startX   | number | 是  | 起始 X 位置，默认 0                                                     |
| startY   | number | 是  | 起始 Y 位置，默认 0                                                     |
| width    | number | 是  | 宽度，最小 500                                                        |
| height   | number | 是  | 高度，最小 200                                                        |
| col      | number | 是  | 宫格排列时，每行列数                                                       |
| spaceX   | number | 是  | 宫格横向间距，默认 0                                                      |
| spaceY   | number | 是  | 宫格纵向间距，默认 0                                                      |
| offsetX  | number | 是  | 对角线横向偏移量                                                         |
| offsetY  | number | 是  | 对角线纵向偏移量                                                         |
| orderBy  | number | 是  | 按序号排列，asc 正序，desc 倒叙                                             |
| ids      | array  | 否  | 要排列的窗口 ID 数组，不传则排列全部，传入时忽略 seqlist 的值                            |
| seqlist  | array  | 否  | 要排列的窗口序号数组，不传则排列全部                                               |
| screenId | number | 否  | 显示器屏幕 ID，需要排列在哪个显示器上，就传入显示器 ID，具体显示器 ID，可以通过 `/alldisplays` 接口获取 |

## 一键自适应排列窗口

**POST**: `/windowbounds/flexable`

```json
// Body 请求参数示例
{
  "seqlist": []
}
```

* 参数详情

| 名称      | 类型    | 必选 | 说明                                   |
| ------- | ----- | -- | ------------------------------------ |
| seqlist | array | 否  | 窗口序号列表，如 `[12, 14, 1889]`， 不传则排列全部窗口 |

## 批量修改浏览器窗口分组

批量指定窗口到同一个分组下

**POST**: `/browser/group/update`

```json
// Body 请求参数示例
{
  "groupId": "41notc1202sr8gu5o6emb9ihaqbzbkic",
  "browserIds": ["af25e626167f4870b8f257e697bb4f05", "3baa6e990fee4e839c72722c8dc18019"]
}
```

## 批量修改窗口代理信息

批量修改窗口的代理信息为同一个代理信息，如果只需要修改一个窗口的代理，那么传入一个窗口 ID 即可

**POST**: `/browser/proxy/update`

```json
// Body 请求参数示例
{
  "ids": ["26d23866312e4f6e8262a31b7fbe0ce1"],
  "ipCheckService": "ip123in",
  "proxyMethod": 2,
  "proxyType": "socks5",
  "host": "hzyd.donghui.tech",
  "port": 36303,
  "proxyUserName": "SK-N618-US-1001-1200-1050",
  "proxyPassword": "ip-109.121.47.56"
}
```

* 参数详情

| 名称                  | 类型      | 必选 | 说明                                                             |
| ------------------- | ------- | -- | -------------------------------------------------------------- |
| ids                 | array   | 是  | 浏览器窗口 ID 数组                                                    |
| ipCheckService      | string  | 否  | IP 查询渠道，默认 ip123in，选项 ip-api, luminati，luminati 为 Luminati 专用  |
| proxyMethod         | number  | 是  | 代理方式，2 自定义代理，3 提取 IP，默认 2                                      |
| proxyType           | string  | 是  | 代理类型，可选`http, https, socks5, ssh` 默认 noproxy                   |
| host                | string  | 是  | 代理主机                                                           |
| port                | number  | 是  | 代理端口                                                           |
| proxyUserName       | string  | 是  | 代理用户名                                                          |
| proxyPassword       | string  | 是  | 代理密码填                                                          |
| refreshProxyUrl     | string  | 否  | 代理刷新 URL 填                                                     |
| dynamicIpUrl        | string  | 否  | 提取 IP url                                                      |
| dynamicIpChannel    | string  | 否  | 提取 IP 服务商 rola, ipidea, deoveip, cloudam, common 默认填 common 即可 |
| isDynamicIpChangeIp | boolean | 否  | true，每次打开窗口都提取新 IP， false，上次的 IP 失效时才提取新 IP                    |
| isIpv6              | boolean | 否  | 是否是 IPv6，默认 false                                              |

## 批量修改窗口备注

**POST**: `/browser/remark/update`

```json
// Body 请求参数示例
{
  "remark": "备注信息",
  "browserIds": ["af25e626167f4870b8f257e697bb4f05", "3baa6e990fee4e839c72722c8dc18019"]
}
```

* 参数详情

| 名称         | 类型     | 必选 | 说明                            |
| ---------- | ------ | -- | ----------------------------- |
| browserIds | array  | 是  | 窗口 ID 数组，只改一个窗口备注时，传入一个 ID 即可 |
| remark     | string | 是  | 备注信息                          |

## 通过序号批量关闭窗口

**POST**: `/browser/close/byseqs`

```json
// Body 请求参数示例
{
  "seqs": [12, 13]
}
```

* 参数详情

| 名称   | 类型    | 必选 | 说明     |
| ---- | ----- | -- | ------ |
| seqs | array | 是  | 窗口序号列表 |

## 关闭所有窗口，无参数

**POST**: `/browser/close/all`

## 获取已打开窗口的进程 pid 集合，也可以用来判断窗口是否已打开，支持批量查询

**POST**: `/browser/pids`

```json
// Body 请求参数示例
{
  "ids": ["af25e626167f4870b8f257e697bb4f05", "3baa6e990fee4e839c72722c8dc18019"]
}
```

* 参数详情

| 名称  | 类型    | 必选 | 说明            |
| --- | ----- | -- | ------------- |
| ids | array | 是  | 窗口 id 集合，数组类型 |

```json
// 返回数据示例，一个窗口ID对应一个pid
{
  "success": true,
  "data": {
    "02d39dd4f9c54e40bc1ef51929d27235": 69902,
    "39dd4f4e40bc1ef51929d27232sdf3ds": 84773
  }
}
```

## 获取所有活着的已打开的窗口的进程 ID，会自动过滤掉已死掉的进程，无参数

**POST**: `/browser/pids/all`

```json
// 返回示例
{
  "success": true,
  "data": {
    "02d39dd4f9c54e40bc1ef51929d27235": 69902,
    "39dd4f4e40bc1ef51929d27232sdf3ds": 84773
  }
}
```

## 获取活着的给定窗口的 pids，会检查进程，减少进程退出，但是窗口状态没关闭的问题

**POST**: `/browser/pids/alive`

```json
// Body 请求参数示例
{
  "ids": ["af25e626167f4870b8f257e697bb4f05", "3baa6e990fee4e839c72722c8dc18019"]
}
```

* 参数详情

| 名称  | 类型    | 必选 | 说明            |
| --- | ----- | -- | ------------- |
| ids | array | 是  | 窗口 id 集合，数组类型 |

```json
// 返回示例
{
  "success": true,
  "data": {
    "02d39dd4f9c54e40bc1ef51929d27235": 69902,
    "39dd4f4e40bc1ef51929d27232sdf3ds": 84773
  }
}
```

## 批量删除窗口，一次最多 100 个

彻底删除记录，包括本地缓存与云端缓存，删除后窗口无法恢复

**POST**: `/browser/delete/ids`

```json
// Body 请求参数示例
{
  "ids": ["af25e626167f4870b8f257e697bb4f05", "3baa6e990fee4e839c72722c8dc18019"]
}
```

* 参数详情

| 名称  | 类型    | 必选 | 说明            |
| --- | ----- | -- | ------------- |
| ids | array | 是  | 窗口 id 集合，数组类型 |

## 清理窗口缓存，注意，会清理掉所有的本地缓存文件，和服务端缓存文件

**POST**: `/cache/clear`

```json
// Body 请求参数示例
{
  "ids": ["af25e626167f4870b8f257e697bb4f05", "3baa6e990fee4e839c72722c8dc18019"]
}
```

* 参数详情

| 名称  | 类型    | 必选 | 说明            |
| --- | ----- | -- | ------------- |
| ids | array | 是  | 窗口 id 集合，数组类型 |

## 保留扩展数据，删除窗口缓存

**POST**: `/cache/clear/exceptExtensions`

```json
// Body 请求参数示例
{
  "ids": ["af25e626167f4870b8f257e697bb4f05"]
}
```

* 参数详情

| 名称  | 类型    | 必选 | 说明          |
| --- | ----- | -- | ----------- |
| ids | array | 是  | 窗口 ID 集合，数组 |

## 获取所有已打开窗口的调试端口 remote-debugging-port

**POST**: `/browser/ports`

```json
// 返回示例，id对应端口
{
  "success": true,
  "data": {
    "8caf925feebb4d2fb0bfd79ed9591e11": "64170",
    "c6925679d4e848a59e7ec49e44184013": "64217"
  }
}
```

## 代理检测接口，可以用来查询代理信息，以及检测代理是否可用，注意如果 IP 需要在全局代理下使用，则要开全局

**POST**: `/checkagent`

```json
// Body 请求参数示例
{
  "host": "1.2.23",
  "port": 1234,
  "proxyType": "socks5",
  "proxyUserName": "username",
  "proxyPassword": "password",
  "ipCheckService": "ip123in"
}
```

* 参数详情

| 名称             | 类型     | 必选 | 说明                           |
| -------------- | ------ | -- | ---------------------------- |
| host           | string | 是  | 代理主机                         |
| port           | number | 是  | 代理端口                         |
| proxyType      | string | 是  | 代理类型 http, socks5, ssh 选一    |
| proxyUserName  | string | 是  | 代理用户名                        |
| proxyPassword  | string | 是  | 代理密码                         |
| ipCheckService | string | 是  | IP 检测渠道，默认 ip123in，可选 ip-api |
| checkExists    | number | 是  | 检测 IP 是否已使用，值为 1 或 0         |

```json
// 返回示例
{
  "success": true,
  "data": {
    "success": true,
    "data": {
      "ip": "94.154.157.98",
      "countryName": "英国(GB)",
      "stateProv": "England(ENG)",
      "countryCode": "GB",
      "region": "ENG",
      "city": "London",
      "languages": "en-GB",
      "timeZone": "Europe/London",
      "offset": "1",
      "longitude": "-0.0991",
      "latitude": "51.5269",
      "zip": "EC1V",
      "status": 1,
      "used": false,
      "usedTime": null
    }
  }
}
```

## 随机指纹值，传入窗口 ID，随机一次指纹，返回指纹对象，注意，不是返回窗口对象，只返回指纹对象

**POST**: `/browser/fingerprint/random`

```json
// Body 请求参数示例
{
  "browserId": "af25e626167f4870b8f257e697bb4f05"
}
```

* 参数详情

| 名称        | 类型     | 必选 | 说明    |
| --------- | ------ | -- | ----- |
| browserId | string | 是  | 窗口 ID |

## 对已打开窗口设置实时 cookie

**POST**: `/browser/cookies/set`

```json
// Body 请求参数示例
{
  "browserId": "af25e626167f4870b8f257e697bb4f05",
  "cookies": [{ "name": "ck_name", "value": "ck_value", "domain": "example.com", ... }, ...]
}
```

* 参数详情

| 名称        | 类型     | 必选 | 说明                                       |
| --------- | ------ | -- | ---------------------------------------- |
| browserId | string | 是  | 窗口 ID                                    |
| cookies   | array  | 是  | 要设置的 cookies 数组，注意必须是标准 cookies 格式，可参考附录 |

## 清空 cookie，7.0.2 及以上版本客户端支持

注意：无论窗口是否打开，此接口都能清理本地缓存中的 cookie，已同步到服务端的 cookie，用 saveSynced 字段控制，默认本地与云端一起清理掉

**POST**: `/browser/cookies/clear`

```json
// Body 请求参数示例
{
  "browserId": "af25e626167f4870b8f257e697bb4f05",
  "saveSynced": true
}
```

* 参数详情

| 名称         | 类型      | 必选 | 说明                             |
| ---------- | ------- | -- | ------------------------------ |
| browserId  | string  | 是  | 窗口 ID                          |
| saveSynced | boolean | 是  | 是否清空窗口已同步到服务端的 cookie，默认为 true |

## 获取已打开窗口的实时 cookies，注意实时 cookie 可能一直在变，两次获取到的可能不一致

**POST**: `/browser/cookies/get`

```json
// Body 请求参数示例
{
  "browserId": "af25e626167f4870b8f257e697bb4f05"
}
```

* 参数详情

| 名称        | 类型     | 必选 | 说明    |
| --------- | ------ | -- | ----- |
| browserId | string | 是  | 窗口 ID |

```json
// 返回数据示例
{
  "success": true,
  "data": [
    {
      "name": "i18n_redirected",
      "value": "zh",
      "domain": "www.browserscan.net",
      "path": "/",
      "expires": 1766633932,
      "httpOnly": false,
      "secure": true,
      "session": false,
      "sameParty": false
    },
    {
      "name": "_ga",
      "value": "GA1.1.821789507.1734754466",
      "domain": ".browserscan.net",
      "path": "/",
      "expires": 1766633932,
      "httpOnly": false,
      "secure": false,
      "session": false,
      "sameParty": false
    },
    {
      "name": "MR",
      "value": "0",
      "domain": ".c.bing.com",
      "path": "/",
      "expires": 1766633932,
      "httpOnly": false,
      "secure": true,
      "session": false,
      "sameSite": "None",
      "sameParty": false
    },
    ...
  ]
}
```

## 格式化给定 cookie，方便用户使用

**POST**: `/browser/cookies/format`

```json
// Body 请求参数示例
{
  "cookie": "sbd=dec",
  "hostname": "abc.com"
}
```

* 参数详情

| 名称       | 类型     | 必选 | 说明                                                                                                          |
| -------- | ------ | -- | ----------------------------------------------------------------------------------------------------------- |
| cookie   | any    | 是  | 给定 cookie 数据，可能是数组，字符串等                                                                                     |
| hostname | string | 是  | cookie 的 domain 值，对于一些 cookie 中没有携带 domain 值的 cookie 数据，需要手动指定下 hostname 值，比如 .abc.com ，绝大部分 cookie 不需要专门指定 |

## 获取所有显示器列表，无参数

**POST**: `/alldisplays`

```json
// 返回数据示例
{
  "success": true,
  "data": [
    {
      "id": 1,
      "label": "内建视网膜显示器",
      "bounds": {
        "x": 0,
        "y": 0,
        "width": 1728,
        "height": 1117
      },
      "workArea": {
        "x": 0,
        "y": 38,
        "width": 1728,
        "height": 1004
      },
      "accelerometerSupport": "unknown",
      "monochrome": false,
      "colorDepth": 30,
      "colorSpace": "{primaries:BT709, transfer:SRGB_HDR, matrix:RGB, range:FULL}",
      "depthPerComponent": 10,
      "size": {
        "width": 1728,
        "height": 1117
      },
      "displayFrequency": 120,
      "workAreaSize": {
        "width": 1728,
        "height": 1004
      },
      "scaleFactor": 2,
      "rotation": 0,
      "internal": true,
      "touchSupport": "unknown"
    },
    {
      "id": 2,
      "label": "HandaCai",
      "bounds": {
        "x": -1920,
        "y": 37,
        "width": 1920,
        "height": 1080
      },
      "workArea": {
        "x": -1920,
        "y": 62,
        "width": 1920,
        "height": 1055
      },
      "accelerometerSupport": "unknown",
      "monochrome": false,
      "colorDepth": 24,
      "colorSpace": "{primaries:BT709, transfer:SRGB, matrix:RGB, range:FULL}",
      "depthPerComponent": 8,
      "size": {
        "width": 1920,
        "height": 1080
      },
      "displayFrequency": 60,
      "workAreaSize": {
        "width": 1920,
        "height": 1055
      },
      "scaleFactor": 1,
      "rotation": 0,
      "internal": false,
      "touchSupport": "unknown"
    }
  ]
}
```

## 执行 RPA 任务

**POST**: `/rpa/run`

```json
// Body 请求参数示例
{
  "id": "2c9cce4492132cd8019213385aec0018"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明                                    |
| -- | ------ | -- | ------------------------------------- |
| id | string | 是  | RPA 任务 ID，从 rpa 管理界面，编辑 rpa 任务处，复制 ID |

## 停止 RPA 任务

**POST**: `/rpa/stop`

```json
// Body 请求参数示例
{
  "id": "2c9cce4492132cd8019213385aec0018"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明                                    |
| -- | ------ | -- | ------------------------------------- |
| id | string | 是  | RPA 任务 ID，从 rpa 管理界面，编辑 rpa 任务处，复制 ID |

## 仿真输入，将会自动将剪贴板中的文本，延迟输入到页面的聚焦输入框中，注意：页面中必须有聚焦的输入框，否则无法输入

**POST**: `/autopaste`

```json
// Body 请求参数示例
{
  "browserId": "2c9cce4492132cd8019213385aec0018",
  "url": "https://www.baidu.com"
}
```

* 参数详情

| 名称        | 类型     | 必选 | 说明                    |
| --------- | ------ | -- | --------------------- |
| browserId | string | 是  | 窗口 ID                 |
| url       | string | 是  | 调用仿真输入的页面的 url，必须严格相等 |

## 读取本地 excel 文件内容，建议配合 RPA 使用

**POST**: `/utils/readexcel`

```json
// Body 请求参数示例
{
  "filepath": "C:\\Users\\User\\Downloads\\abc.xlsx"
}
```

* 参数详情

| 名称       | 类型     | 必选 | 说明                  |
| -------- | ------ | -- | ------------------- |
| filepath | string | 是  | 必填，本地 excel 文件的绝对路径 |

## 读取文本类文件内容，如 json,txt 等文本文件，建议配合 RPA 使用

***注意：接口即将弃用，请使用其他方式读取信息***

**POST**: `/undefined`

```json
// Body 请求参数示例
{
  "filepath": "C:\\Users\\User\\Downloads\\abc.xlsx"
}
```

* 参数详情

| 名称       | 类型     | 必选 | 说明                                                    |
| -------- | ------ | -- | ----------------------------------------------------- |
| filepath | string | 是  | 必填，本地文件的绝对路径，读取后均为 stringfy 结果，如 json 格式文件，在使用时，自行格式化 |


### 浏览器分组接口

来源：https://doc2.bitbrowser.cn/jiekou/fen-zu-jie-kou.html

更新时间：2025-08-25T15:28:11+08:00

> 注意：分组查询接口用来查询分组，查询浏览器窗口，请查看“浏览器窗口接口”
>
> 比如要查询某个分组下的窗口列表，请使用 browser/list 接口

## 查询分组列表接口,page 从 0 开始

**POST**: `/group/list`

```json
// Body 请求参数示例
{
  "page": 0,
  "pageSize": 10,
  "all": true
}
```

* 参数详情

| 名称       | 类型      | 必选 | 说明                                       |
| -------- | ------- | -- | ---------------------------------------- |
| page     | number  | 是  | 分页， 从 0 开始                               |
| pageSize | number  | 是  | 每页条数，最大 100 条，传入大于 100 的数字，也最多返回 100 条数据 |
| all      | boolean | 否  | 是否获取权限范围内的所有分组                           |

## 添加分组

**POST**: `/group/add`

```json
// Body 请求参数示例
{
  "groupName": "Group Name",
  "sortNum": 0
}
```

* 参数详情

| 名称        | 类型     | 必选 | 说明   |
| --------- | ------ | -- | ---- |
| groupName | string | 是  | 分组名称 |
| sortNum   | number | 是  | 排序数字 |

## 修改分组

**POST**: `/group/edit`

```json
// Body 请求参数示例
{
  "id": "41notc1202sr8gu5o6emb9ihaqbzbkic",
  "groupName": "Update Group Name",
  "sortNum": 0
}
```

* 参数详情

| 名称        | 类型     | 必选 | 说明    |
| --------- | ------ | -- | ----- |
| id        | string | 是  | 分组 ID |
| groupName | string | 是  | 分组名称  |
| sortNum   | number | 是  | 排序数字  |

## 删除分组

**POST**: `/group/delete`

```json
// Body 请求参数示例
{
  "id": "41notc1202sr8gu5o6emb9ihaqbzbkic"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明    |
| -- | ------ | -- | ----- |
| id | string | 是  | 分组 ID |

## 获取分组详情

**POST**: `/group/detail`

```json
// Body 请求参数示例
{
  "id": "41notc1202sr8gu5o6emb9ihaqbzbkic"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明    |
| -- | ------ | -- | ----- |
| id | string | 是  | 分组 ID |


### 浏览器窗口标签

来源：https://doc2.bitbrowser.cn/browserTags.html

更新时间：2026-04-09T11:00:32+08:00

> 注意：当前标签总量与界面中一致，最多可创建20个标签
> 单个窗口最多允许绑定3个标签

## 获取浏览器窗口标签列表，无参数

**POST**: `/browserTag/list`

```json
// 返回数据示例
{
    "success": true,
    "data": [
        {
            "id": "402880669d09d5aa019d09e3060e001c",
            "tagName": "API Tag name1",
            "tagColor": "#ECC2F3"
        },
        {
            "id": "402880669d09d5aa019d09de365a001b",
            "tagName": "API 创建的",
            "tagColor": "#ECC2F3"
        },
        {
            "id": "402880669d09d5aa019d09dd90a6001a",
            "tagName": "545453",
            "tagColor": "#FFF18C"
        }
    ]
}
```
## 创建窗口标签
**POST**: `/browserTag/create`
```json
// Body 请求参数示例
{
    "tagName": "标签名",
    "tagColor": "#ECC2F3"
}
```

* 参数详情

| 名称       | 类型      | 必选 | 说明                                       |
| -------- | ------- | -- | ---------------------------------------- |
| tagName     | string  | 是  | 标签名，最长不超过50个字符    |
| tagColor | string  | 是  | 只接受 #aabbcc 格式的颜色值 |

## 修改窗口标签
**POST**: `/browserTag/update`
```json
// Body 请求参数示例
{
    "id": "402880669cb319ac019cb710cd9b01ec",
    "tagName": "标签名",
    "tagColor": "#ECC2F3"
}
```

* 参数详情

| 名称       | 类型      | 必选 | 说明                                       |
| -------- | ------- | -- | ---------------------------------------- |
| id     | string  | 是  | 标签id，32位  |
| tagName     | string  | 是  | 标签名，最长不超过50个字符    |
| tagColor | string  | 是  | 只接受 #aabbcc 格式的颜色值 |

## 删除窗口标签，支持批量
**POST**: `/browserTag/delete`
```json
// Body 请求参数示例
{
    "ids": ["402880669cb319ac019cb710cd9b01ec"]
}
```

* 参数详情

| 名称       | 类型      | 必选 | 说明                                       |
| -------- | ------- | -- | ---------------------------------------- |
| ids     | array  | 是  | 标签id集合，数组格式  |

## 更新窗口标签绑定关系
**POST**: `/browserTag/updateRelation`
```json
// Body 请求参数示例
{
    "browserId": "043239f6d2844f8d8407ac8a1972daec",
    "addTagIds": ["402880669d09d5aa019d09dd90a6001a"],
    "removeTagIds": ["402880669d09d5aa019d09de365a001b"]
}
```

* 参数详情

| 名称       | 类型      | 必选 | 说明                                       |
| -------- | ------- | -- | ---------------------------------------- |
| browserId     | string  | 是  | 窗口ID，32位  |
| addTagIds     | array  | 是  | 数组格式，要绑定的标签ID集合  |
| removeTagIds     | array  | 是  | 数组格式，要解除绑定关系的标签ID集合  |


### 本地库数据接口

来源：https://doc2.bitbrowser.cn/jiekou/local-db-api.html

更新时间：2024-12-18T15:51:28+08:00

> 注意：本地库数据接口，不与客户端产生任何关联，完全独立的日志记录系统，可在 “操作日志>本地库数据”页面查看已记录的数据，也可用 api 查询数据，用来辅助脚本作者与用户记录自定义日志，或者作为简易本地数据库使用。

## 分页查询日志列表

**POST**: `/extralog/list`

```json
// Body 请求参数示例
{
  "page": 0,
  "page_size": 10,
  "search_key": "",
  "search_value": "",
  "order_by": "desc"
}
```

* 参数详情

| 参数            | 类型     | 是否必填 | 说明                         |
| ------------- | ------ | ---- | -------------------------- |
| page          | int    | 是    | 页码，从 0 开始                  |
| page\_size    | int    | 是    | 分页条数，默认 10                 |
| search\_key   | string | 是    | 查询字段，具体字段参考 add 接口         |
| search\_value | string | 是    | 查询字段值，根据字段值模糊查询记录          |
| order\_by     | string | 否    | 排序参数，默认根据 id 排序，desc ｜ asc |

## 添加日志

**POST**: `/extralog/add`

```json
// Body 请求参数示例
{
  "log_key": "666",
  "log_name": "i am name",
  "log_value": "de333f",
  "log_type": "Browser",
  "log_desc": "i am a desc",
  "log_remark": "remark",
  "log_extra_info": "自定义info内容，可存储序列化的结构型数据，比如stringfy的json数据"
}
```

* 参数详情

| 参数               | 类型     | 是否必填 | 说明                                             |
| ---------------- | ------ | ---- | ---------------------------------------------- |
| log\_name        | string | 是    | 自定义 name                                       |
| log\_key         | string | 是    | 自定义 key，用户可为每个记录生成唯一 key，记录到此字段，方便使用           |
| log\_type        | string | 是    | 自定义 type，可分类型记录日志，方便查找                         |
| log\_value       | string | 是    | 自定义 value，可作值记录，或者其他数据                         |
| log\_desc        | string | 否    | 自定义 desc                                       |
| log\_remark      | string | 否    | 自定义 remark                                     |
| log\_extra\_info | string | 否    | 自定义 info 内容，可存储序列化的结构型数据，比如 stringfy 的 json 数据 |

## 更新日志

更新接口字段与添加接口相同，以及必需传入 id，其他字段则需要改哪个字段，就传哪个字段，无须全部传入

```json
// Body 请求参数示例
{
  "id": 122,
  "log_name": "I'm log name"
}
```

* 参数详情

| 参数  | 类型     | 是否必填 | 说明                       |
| --- | ------ | ---- | ------------------------ |
| id  | int    | 是    | 记录 ID                    |
| ... | string | 否    | 其余字段与上方[添加日志](#添加日志)字段相同 |

## 删除日志

**POST**: `/extralog/delete`

```json
// Body 请求参数示例
{
  "id": 12
}
```

## 获取日志详情

**POST**: `/extralog/detail`

````json
// Body 请求参数示例
{
  "id": 12,
  "name": "dsds"
}

```
// 返回详情数据示例
{
  "success": true,
  "data": {
    "id": 8,
    "log_username": "username",
    "log_userid": "3dsd3dsdd3dsdsdssds3dsssddsdsd",
    "created_time": "2024-07-03 10:11:32",
    "log_name": "i am name",
    "log_key": "33",
    "log_type": "Browser",
    "log_value": "我是修改的",
    "log_desc": "i am a desc",
    "log_remark": "remark",
    "log_extra_info": "0.0"
}
}
````

## 清空日志记录，无参数，请小心操作，此接口将清空所有本地日志数据，无法恢复

**POST**: `/extralog/clear`


### 无头模式指南

来源：https://doc2.bitbrowser.cn/headless.html

更新时间：2025-08-25T15:28:11+08:00

## 使用方式
1. 通过browser/open接口的args参数，传入 `--headless` 参数使用，配合 `ignoreDefaultUrls` 参数，防止出错，如下，请求参数如下：
```json
// 接口：/browser/open
{
    "id": "3baa6e990fee4e839c72722c8dc18019",
    "args": ["--headless"], 
    "queue": true,
    "ignoreDefaultUrls": true // 一定要加这个参数，以及不要配置newPageUrl，窗口打开后自己通过脚本打开page页面即可
}
```
2. 创建或者修改窗口时，设置窗口对象中 `url` 为空，指纹对象中 launchArgs为 `--headless`，然后直接调用open接口打开
```json
// 创建或者修改窗口接口
{
    url: "", // 一定要设置url为空
    browserFingerPrint: {
        launchArgs: "--headless" // 启动参数，可以逗号分隔传入多个
    }
}
```
## 常见问题
当出现如下报错时：
```json
{
    "success": false,
    "msg": "打开窗口失败，请检查指纹配置是否正确，或切换内核重试：Failed to launch the browser process!\n[0825/152447.375793:ERROR:chrome/app/chrome_main.cc:202] Multiple targets are not supported in headless mode.\n\n\nTROUBLESHOOTING: https://pptr.dev/troubleshooting\n"
}
```
即报错信息中包含：
```
Multiple targets are not supported in headless mode
```
表示传入了多个url，而无头模式并不支持启动时打开多个url，参考上述使用方式修复问题！


### 附录

来源：https://doc2.bitbrowser.cn/jiekou/fu-lu.html

更新时间：2023-11-16T18:14:24+08:00

## 国家code

```
[
  {
    cid: 'AD',
    cname: '安道尔共和国',
    areaCode: '376'
  },
  {
    cid: 'AE',
    cname: '阿拉伯联合酋长国',
    areaCode: '971'
  },
  {
    cid: 'AF',
    cname: '阿富汗',
    areaCode: '93'
  },
  {
    cid: 'AG',
    cname: '安提瓜和巴布达',
    areaCode: '1268'
  },
  {
    cid: 'AI',
    cname: '安圭拉岛',
    areaCode: '1264'
  },
  {
    cid: 'AL',
    cname: '阿尔巴尼亚',
    areaCode: '355'
  },
  {
    cid: 'AM',
    cname: '亚美尼亚',
    areaCode: '374'
  },
  {
    cid: 'AO',
    cname: '安哥拉',
    areaCode: '244'
  },
  {
    cid: 'AR',
    cname: '阿根廷',
    areaCode: '54'
  },
  {
    cid: 'AT',
    cname: '奥地利',
    areaCode: '43'
  },
  {
    cid: 'AU',
    cname: '澳大利亚',
    areaCode: '61'
  },
  {
    cid: 'AZ',
    cname: '阿塞拜疆',
    areaCode: '994'
  },
  {
    cid: 'BB',
    cname: '巴巴多斯',
    areaCode: '1246'
  },
  {
    cid: 'BD',
    cname: '孟加拉国',
    areaCode: '880'
  },
  {
    cid: 'BE',
    cname: '比利时',
    areaCode: '32'
  },
  {
    cid: 'BF',
    cname: '布基纳法索',
    areaCode: '226'
  },
  {
    cid: 'BG',
    cname: '保加利亚',
    areaCode: '359'
  },
  {
    cid: 'BH',
    cname: '巴林',
    areaCode: '973'
  },
  {
    cid: 'BI',
    cname: '布隆迪',
    areaCode: '257'
  },
  {
    cid: 'BJ',
    cname: '贝宁',
    areaCode: '229'
  },
  {
    cid: 'BL',
    cname: '巴勒斯坦',
    areaCode: '970'
  },
  {
    cid: 'BM',
    cname: '百慕大群岛',
    areaCode: '1441'
  },
  {
    cid: 'BN',
    cname: '文莱',
    areaCode: '673'
  },
  {
    cid: 'BO',
    cname: '玻利维亚',
    areaCode: '591'
  },
  {
    cid: 'BR',
    cname: '巴西',
    areaCode: '55'
  },
  {
    cid: 'BS',
    cname: '巴哈马',
    areaCode: '1242'
  },
  {
    cid: 'BW',
    cname: '博茨瓦纳',
    areaCode: '267'
  },
  {
    cid: 'BY',
    cname: '白俄罗斯',
    areaCode: '375'
  },
  {
    cid: 'BZ',
    cname: '伯利兹',
    areaCode: '501'
  },
  {
    cid: 'CA',
    cname: '加拿大',
    areaCode: '1'
  },
  {
    cid: 'CF',
    cname: '中非共和国',
    areaCode: '236'
  },
  {
    cid: 'CG',
    cname: '刚果',
    areaCode: '242'
  },
  {
    cid: 'CH',
    cname: '瑞士',
    areaCode: '41'
  },
  {
    cid: 'CK',
    cname: '库克群岛',
    areaCode: '682'
  },
  {
    cid: 'CL',
    cname: '智利',
    areaCode: '56'
  },
  {
    cid: 'CM',
    cname: '喀麦隆',
    areaCode: '237'
  },
  {
    cid: 'CN',
    cname: '中国',
    areaCode: '86'
  },
  {
    cid: 'CO',
    cname: '哥伦比亚',
    areaCode: '57'
  },
  {
    cid: 'CR',
    cname: '哥斯达黎加',
    areaCode: '506'
  },
  {
    cid: 'CS',
    cname: '捷克',
    areaCode: '420'
  },
  {
    cid: 'CU',
    cname: '古巴',
    areaCode: '53'
  },
  {
    cid: 'CY',
    cname: '塞浦路斯',
    areaCode: '357'
  },
  {
    cid: 'CZ',
    cname: '捷克',
    areaCode: '420'
  },
  {
    cid: 'DE',
    cname: '德国',
    areaCode: '49'
  },
  {
    cid: 'DJ',
    cname: '吉布提',
    areaCode: '253'
  },
  {
    cid: 'DK',
    cname: '丹麦',
    areaCode: '45'
  },
  {
    cid: 'DO',
    cname: '多米尼加共和国',
    areaCode: '1890'
  },
  {
    cid: 'DZ',
    cname: '阿尔及利亚',
    areaCode: '213'
  },
  {
    cid: 'EC',
    cname: '厄瓜多尔',
    areaCode: '593'
  },
  {
    cid: 'EE',
    cname: '爱沙尼亚',
    areaCode: '372'
  },
  {
    cid: 'EG',
    cname: '埃及',
    areaCode: '20'
  },
  {
    cid: 'ES',
    cname: '西班牙',
    areaCode: '34'
  },
  {
    cid: 'ET',
    cname: '埃塞俄比亚',
    areaCode: '251'
  },
  {
    cid: 'FI',
    cname: '芬兰',
    areaCode: '358'
  },
  {
    cid: 'FJ',
    cname: '斐济',
    areaCode: '679'
  },
  {
    cid: 'FR',
    cname: '法国',
    areaCode: '33'
  },
  {
    cid: 'GA',
    cname: '加蓬',
    areaCode: '241'
  },
  {
    cid: 'GB',
    cname: '英国',
    areaCode: '44'
  },
  {
    cid: 'GD',
    cname: '格林纳达',
    areaCode: '1809'
  },
  {
    cid: 'GE',
    cname: '格鲁吉亚',
    areaCode: '995'
  },
  {
    cid: 'GF',
    cname: '法属圭亚那',
    areaCode: '594'
  },
  {
    cid: 'GH',
    cname: '加纳',
    areaCode: '233'
  },
  {
    cid: 'GI',
    cname: '直布罗陀',
    areaCode: '350'
  },
  {
    cid: 'GM',
    cname: '冈比亚',
    areaCode: '220'
  },
  {
    cid: 'GN',
    cname: '几内亚',
    areaCode: '224'
  },
  {
    cid: 'GR',
    cname: '希腊',
    areaCode: '30'
  },
  {
    cid: 'GT',
    cname: '危地马拉',
    areaCode: '502'
  },
  {
    cid: 'GU',
    cname: '关岛',
    areaCode: '1671'
  },
  {
    cid: 'GY',
    cname: '圭亚那',
    areaCode: '592'
  },
  {
    cid: 'HK',
    cname: '中国香港特别行政区',
    areaCode: '852'
  },
  {
    cid: 'HN',
    cname: '洪都拉斯',
    areaCode: '504'
  },
  {
    cid: 'HT',
    cname: '海地',
    areaCode: '509'
  },
  {
    cid: 'HU',
    cname: '匈牙利',
    areaCode: '36'
  },
  {
    cid: 'ID',
    cname: '印度尼西亚',
    areaCode: '62'
  },
  {
    cid: 'IE',
    cname: '爱尔兰',
    areaCode: '353'
  },
  {
    cid: 'IL',
    cname: '以色列',
    areaCode: '972'
  },
  {
    cid: 'IN',
    cname: '印度',
    areaCode: '91'
  },
  {
    cid: 'IQ',
    cname: '伊拉克',
    areaCode: '964'
  },
  {
    cid: 'IR',
    cname: '伊朗',
    areaCode: '98'
  },
  {
    cid: 'IS',
    cname: '冰岛',
    areaCode: '354'
  },
  {
    cid: 'IT',
    cname: '意大利',
    areaCode: '39'
  },
  {
    cid: 'JM',
    cname: '牙买加',
    areaCode: '1876'
  },
  {
    cid: 'JO',
    cname: '约旦',
    areaCode: '962'
  },
  {
    cid: 'JP',
    cname: '日本',
    areaCode: '81'
  },
  {
    cid: 'KE',
    cname: '肯尼亚',
    areaCode: '254'
  },
  {
    cid: 'KG',
    cname: '吉尔吉斯坦',
    areaCode: '331'
  },
  {
    cid: 'KH',
    cname: '柬埔寨',
    areaCode: '855'
  },
  {
    cid: 'KP',
    cname: '朝鲜',
    areaCode: '850'
  },
  {
    cid: 'KR',
    cname: '韩国',
    areaCode: '82'
  },
  {
    cid: 'KT',
    cname: '科特迪瓦共和国',
    areaCode: '225'
  },
  {
    cid: 'KW',
    cname: '科威特',
    areaCode: '965'
  },
  {
    cid: 'LA',
    cname: '老挝',
    areaCode: '856'
  },
  {
    cid: 'LB',
    cname: '黎巴嫩',
    areaCode: '961'
  },
  {
    cid: 'LC',
    cname: '圣卢西亚',
    areaCode: '1758'
  },
  {
    cid: 'LI',
    cname: '列支敦士登',
    areaCode: '423'
  },
  {
    cid: 'LK',
    cname: '斯里兰卡',
    areaCode: '94'
  },
  {
    cid: 'LR',
    cname: '利比里亚',
    areaCode: '231'
  },
  {
    cid: 'LS',
    cname: '莱索托',
    areaCode: '266'
  },
  {
    cid: 'LT',
    cname: '立陶宛',
    areaCode: '370'
  },
  {
    cid: 'LU',
    cname: '卢森堡',
    areaCode: '352'
  },
  {
    cid: 'LV',
    cname: '拉脱维亚',
    areaCode: '371'
  },
  {
    cid: 'LY',
    cname: '利比亚',
    areaCode: '218'
  },
  {
    cid: 'MA',
    cname: '摩洛哥',
    areaCode: '212'
  },
  {
    cid: 'MC',
    cname: '摩纳哥',
    areaCode: '377'
  },
  {
    cid: 'MD',
    cname: '摩尔多瓦',
    areaCode: '373'
  },
  {
    cid: 'MG',
    cname: '马达加斯加',
    areaCode: '261'
  },
  {
    cid: 'ML',
    cname: '马里',
    areaCode: '223'
  },
  {
    cid: 'MM',
    cname: '缅甸',
    areaCode: '95'
  },
  {
    cid: 'MN',
    cname: '蒙古',
    areaCode: '976'
  },
  {
    cid: 'MO',
    cname: '中国澳门特别行政区',
    areaCode: '853'
  },
  {
    cid: 'MS',
    cname: '蒙特塞拉特岛',
    areaCode: '1664'
  },
  {
    cid: 'MT',
    cname: '马耳他',
    areaCode: '356'
  },
  {
    cid: 'MU',
    cname: '毛里求斯',
    areaCode: '230'
  },
  {
    cid: 'MV',
    cname: '马尔代夫',
    areaCode: '960'
  },
  {
    cid: 'MW',
    cname: '马拉维',
    areaCode: '265'
  },
  {
    cid: 'MX',
    cname: '墨西哥',
    areaCode: '52'
  },
  {
    cid: 'MY',
    cname: '马来西亚',
    areaCode: '60'
  },
  {
    cid: 'MZ',
    cname: '莫桑比克',
    areaCode: '258'
  },
  {
    cid: 'NA',
    cname: '纳米比亚',
    areaCode: '264'
  },
  {
    cid: 'NE',
    cname: '尼日尔',
    areaCode: '977'
  },
  {
    cid: 'NG',
    cname: '尼日利亚',
    areaCode: '234'
  },
  {
    cid: 'NI',
    cname: '尼加拉瓜',
    areaCode: '505'
  },
  {
    cid: 'NL',
    cname: '荷兰',
    areaCode: '31'
  },
  {
    cid: 'NO',
    cname: '挪威',
    areaCode: '47'
  },
  {
    cid: 'NP',
    cname: '尼泊尔',
    areaCode: '977'
  },
  {
    cid: 'NR',
    cname: '瑙鲁',
    areaCode: '674'
  },
  {
    cid: 'NZ',
    cname: '新西兰',
    areaCode: '64'
  },
  {
    cid: 'OM',
    cname: '阿曼',
    areaCode: '968'
  },
  {
    cid: 'PA',
    cname: '巴拿马',
    areaCode: '507'
  },
  {
    cid: 'PE',
    cname: '秘鲁',
    areaCode: '51'
  },
  {
    cid: 'PF',
    cname: '法属玻利尼西亚',
    areaCode: '689'
  },
  {
    cid: 'PG',
    cname: '巴布亚新几内亚',
    areaCode: '675'
  },
  {
    cid: 'PH',
    cname: '菲律宾',
    areaCode: '63'
  },
  {
    cid: 'PK',
    cname: '巴基斯坦',
    areaCode: '92'
  },
  {
    cid: 'PL',
    cname: '波兰',
    areaCode: '48'
  },
  {
    cid: 'PR',
    cname: '波多黎各',
    areaCode: '1787'
  },
  {
    cid: 'PT',
    cname: '葡萄牙',
    areaCode: '351'
  },
  {
    cid: 'PY',
    cname: '巴拉圭',
    areaCode: '595'
  },
  {
    cid: 'QA',
    cname: '卡塔尔',
    areaCode: '974'
  },
  {
    cid: 'RO',
    cname: '罗马尼亚',
    areaCode: '40'
  },
  {
    cid: 'RU',
    cname: '俄罗斯',
    areaCode: '7'
  },
  {
    cid: 'SA',
    cname: '沙特阿拉伯',
    areaCode: '966'
  },
  {
    cid: 'SB',
    cname: '所罗门群岛',
    areaCode: '677'
  },
  {
    cid: 'SC',
    cname: '塞舌尔',
    areaCode: '248'
  },
  {
    cid: 'SD',
    cname: '苏丹',
    areaCode: '249'
  },
  {
    cid: 'SE',
    cname: '瑞典',
    areaCode: '46'
  },
  {
    cid: 'SG',
    cname: '新加坡',
    areaCode: '65'
  },
  {
    cid: 'SI',
    cname: '斯洛文尼亚',
    areaCode: '386'
  },
  {
    cid: 'SK',
    cname: '斯洛伐克',
    areaCode: '421'
  },
  {
    cid: 'SL',
    cname: '塞拉利昂',
    areaCode: '232'
  },
  {
    cid: 'SM',
    cname: '圣马力诺',
    areaCode: '378'
  },
  {
    cid: 'SN',
    cname: '塞内加尔',
    areaCode: '221'
  },
  {
    cid: 'SO',
    cname: '索马里',
    areaCode: '252'
  },
  {
    cid: 'SR',
    cname: '苏里南',
    areaCode: '597'
  },
  {
    cid: 'ST',
    cname: '圣多美和普林西比',
    areaCode: '239'
  },
  {
    cid: 'SV',
    cname: '萨尔瓦多',
    areaCode: '503'
  },
  {
    cid: 'SY',
    cname: '叙利亚',
    areaCode: '963'
  },
  {
    cid: 'SZ',
    cname: '斯威士兰',
    areaCode: '268'
  },
  {
    cid: 'TD',
    cname: '乍得',
    areaCode: '235'
  },
  {
    cid: 'TG',
    cname: '多哥',
    areaCode: '228'
  },
  {
    cid: 'TH',
    cname: '泰国',
    areaCode: '66'
  },
  {
    cid: 'TJ',
    cname: '塔吉克斯坦',
    areaCode: '992'
  },
  {
    cid: 'TM',
    cname: '土库曼斯坦',
    areaCode: '993'
  },
  {
    cid: 'TN',
    cname: '突尼斯',
    areaCode: '216'
  },
  {
    cid: 'TO',
    cname: '汤加',
    areaCode: '676'
  },
  {
    cid: 'TR',
    cname: '土耳其',
    areaCode: '90'
  },
  {
    cid: 'TT',
    cname: '特立尼达和多巴哥',
    areaCode: '1809'
  },
  {
    cid: 'TW',
    cname: '中国台湾省',
    areaCode: '886'
  },
  {
    cid: 'TZ',
    cname: '坦桑尼亚',
    areaCode: '255'
  },
  {
    cid: 'UA',
    cname: '乌克兰',
    areaCode: '380'
  },
  {
    cid: 'UG',
    cname: '乌干达',
    areaCode: '256'
  },
  {
    cid: 'US',
    cname: '美国',
    areaCode: '1'
  },
  {
    cid: 'UY',
    cname: '乌拉圭',
    areaCode: '598'
  },
  {
    cid: 'UZ',
    cname: '乌兹别克斯坦',
    areaCode: '233'
  },
  {
    cid: 'VC',
    cname: '圣文森特岛',
    areaCode: '1784'
  },
  {
    cid: 'VE',
    cname: '委内瑞拉',
    areaCode: '58'
  },
  {
    cid: 'VN',
    cname: '越南',
    areaCode: '84'
  },
  {
    cid: 'YE',
    cname: '也门',
    areaCode: '967'
  },
  {
    cid: 'YU',
    cname: '南斯拉夫',
    areaCode: '381'
  },
  {
    cid: 'ZA',
    cname: '南非',
    areaCode: '27'
  },
  {
    cid: 'ZM',
    cname: '赞比亚',
    areaCode: '260'
  },
  {
    cid: 'ZR',
    cname: '扎伊尔',
    areaCode: '243'
  }
]
```

## 时区

```
[
  { tz: 'GMT-09:00', gmt: 'America/Metlakatla' },
  { tz: 'GMT-12:00', gmt: 'Etc/GMT+12' },
  { tz: 'GMT-11:00', gmt: 'Etc/GMT+11' },
  { tz: 'GMT-11:00', gmt: 'Pacific/Midway' },
  { tz: 'GMT-11:00', gmt: 'Pacific/Niue' },
  { tz: 'GMT-11:00', gmt: 'Pacific/Pago Pago' },
  { tz: 'GMT-10:00', gmt: 'America/Adak' },
  { tz: 'GMT-10:00', gmt: 'Etc/GMT+10' },
  { tz: 'GMT-10:00', gmt: 'HST' },
  { tz: 'GMT-10:00', gmt: 'Pacific/Honolulu' },
  { tz: 'GMT-10:00', gmt: 'Pacific/Rarotonga' },
  { tz: 'GMT-10:00', gmt: 'Pacific/Tahiti' },
  { tz: 'GMT-09:30', gmt: 'Pacific/Marquesas' },
  { tz: 'GMT-09:00', gmt: 'America/Anchorage' },
  { tz: 'GMT-09:00', gmt: 'America/Juneau' },
  { tz: 'GMT-09:00', gmt: 'America/Nome' },
  { tz: 'GMT-09:00', gmt: 'America/Sitka' },
  { tz: 'GMT-09:00', gmt: 'America/Yakutat' },
  { tz: 'GMT-09:00', gmt: 'Etc/GMT+9' },
  { tz: 'GMT-09:00', gmt: 'Pacific/Gambier' },
  { tz: 'GMT-08:00', gmt: 'America/Los Angeles' },
  { tz: 'GMT-08:00', gmt: 'America/Tijuana' },
  { tz: 'GMT-08:00', gmt: 'America/Vancouver' },
  { tz: 'GMT-08:00', gmt: 'Etc/GMT+8' },
  { tz: 'GMT-08:00', gmt: 'PST8PDT' },
  { tz: 'GMT-08:00', gmt: 'Pacific/Pitcairn' },
  { tz: 'GMT-07:00', gmt: 'America/Boise' },
  { tz: 'GMT-07:00', gmt: 'America/Cambridge Bay' },
  { tz: 'GMT-07:00', gmt: 'America/Chihuahua' },
  { tz: 'GMT-07:00', gmt: 'America/Creston' },
  { tz: 'GMT-07:00', gmt: 'America/Dawson' },
  { tz: 'GMT-07:00', gmt: 'America/Dawson Creek' },
  { tz: 'GMT-07:00', gmt: 'America/Denver' },
  { tz: 'GMT-07:00', gmt: 'America/Edmonton' },
  { tz: 'GMT-07:00', gmt: 'America/Fort Nelson' },
  { tz: 'GMT-07:00', gmt: 'America/Hermosillo' },
  { tz: 'GMT-07:00', gmt: 'America/Inuvik' },
  { tz: 'GMT-07:00', gmt: 'America/Mazatlan' },
  { tz: 'GMT-07:00', gmt: 'America/Ojinaga' },
  { tz: 'GMT-07:00', gmt: 'America/Phoenix' },
  { tz: 'GMT-07:00', gmt: 'America/Whitehorse' },
  { tz: 'GMT-07:00', gmt: 'America/Yellowknife' },
  { tz: 'GMT-07:00', gmt: 'Etc/GMT+7' },
  { tz: 'GMT-07:00', gmt: 'MST' },
  { tz: 'GMT-07:00', gmt: 'MST7MDT' },
  { tz: 'GMT-06:00', gmt: 'America/Bahia Banderas' },
  { tz: 'GMT-06:00', gmt: 'America/Belize' },
  { tz: 'GMT-06:00', gmt: 'America/Chicago' },
  { tz: 'GMT-06:00', gmt: 'America/Costa Rica' },
  { tz: 'GMT-06:00', gmt: 'America/El Salvador' },
  { tz: 'GMT-06:00', gmt: 'America/Guatemala' },
  { tz: 'GMT-06:00', gmt: 'America/Indiana/Knox' },
  { tz: 'GMT-06:00', gmt: 'America/Indiana/Tell City' },
  { tz: 'GMT-06:00', gmt: 'America/Managua' },
  { tz: 'GMT-06:00', gmt: 'America/Matamoros' },
  { tz: 'GMT-06:00', gmt: 'America/Menominee' },
  { tz: 'GMT-06:00', gmt: 'America/Merida' },
  { tz: 'GMT-06:00', gmt: 'America/Mexico City' },
  { tz: 'GMT-06:00', gmt: 'America/Monterrey' },
  { tz: 'GMT-06:00', gmt: 'America/North Dakota/Beulah' },
  { tz: 'GMT-06:00', gmt: 'America/North Dakota/Center' },
  { tz: 'GMT-06:00', gmt: 'America/North Dakota/New_Salem' },
  { tz: 'GMT-06:00', gmt: 'America/Rainy River' },
  { tz: 'GMT-06:00', gmt: 'America/Rankin Inlet' },
  { tz: 'GMT-06:00', gmt: 'America/Regina' },
  { tz: 'GMT-06:00', gmt: 'America/Resolute' },
  { tz: 'GMT-06:00', gmt: 'America/Swift Current' },
  { tz: 'GMT-06:00', gmt: 'America/Tegucigalpa' },
  { tz: 'GMT-06:00', gmt: 'America/Winnipeg' },
  { tz: 'GMT-06:00', gmt: 'CST6CDT' },
  { tz: 'GMT-06:00', gmt: 'Etc/GMT+6' },
  { tz: 'GMT-06:00', gmt: 'Pacific/Galapagos' },
  { tz: 'GMT-05:00', gmt: 'America/Atikokan' },
  { tz: 'GMT-05:00', gmt: 'America/Bogota' },
  { tz: 'GMT-05:00', gmt: 'America/Cancun' },
  { tz: 'GMT-05:00', gmt: 'America/Cayman' },
  { tz: 'GMT-05:00', gmt: 'America/Detroit' },
  { tz: 'GMT-05:00', gmt: 'America/Eirunepe' },
  { tz: 'GMT-05:00', gmt: 'America/Grand Turk' },
  { tz: 'GMT-05:00', gmt: 'America/Guayaquil' },
  { tz: 'GMT-05:00', gmt: 'America/Havana' },
  { tz: 'GMT-05:00', gmt: 'America/Indiana/Indianapolis' },
  { tz: 'GMT-05:00', gmt: 'America/Indiana/Marengo' },
  { tz: 'GMT-05:00', gmt: 'America/Indiana/Petersburg' },
  { tz: 'GMT-05:00', gmt: 'America/Indiana/Vevay' },
  { tz: 'GMT-05:00', gmt: 'America/Indiana/Vincennes' },
  { tz: 'GMT-05:00', gmt: 'America/Indiana/Winamac' },
  { tz: 'GMT-05:00', gmt: 'America/Indianapolis' },
  { tz: 'GMT-05:00', gmt: 'America/Iqaluit' },
  { tz: 'GMT-05:00', gmt: 'America/Jamaica' },
  { tz: 'GMT-05:00', gmt: 'America/Kentucky/Louisville' },
  { tz: 'GMT-05:00', gmt: 'America/Kentucky/Monticello' },
  { tz: 'GMT-05:00', gmt: 'America/Lima' },
  { tz: 'GMT-05:00', gmt: 'America/Montreal' },
  { tz: 'GMT-05:00', gmt: 'America/Nassau' },
  { tz: 'GMT-05:00', gmt: 'America/New York' },
  { tz: 'GMT-05:00', gmt: 'America/Nipigon' },
  { tz: 'GMT-05:00', gmt: 'America/Panama' },
  { tz: 'GMT-05:00', gmt: 'America/Pangnirtung' },
  { tz: 'GMT-05:00', gmt: 'America/Port-au-Prince' },
  { tz: 'GMT-05:00', gmt: 'America/Rio Branco' },
  { tz: 'GMT-05:00', gmt: 'America/Thunder Bay' },
  { tz: 'GMT-05:00', gmt: 'America/Toronto' },
  { tz: 'GMT-05:00', gmt: 'EST' },
  { tz: 'GMT-05:00', gmt: 'EST5EDT' },
  { tz: 'GMT-05:00', gmt: 'Etc/GMT+5' },
  { tz: 'GMT-05:00', gmt: 'Pacific/Easter' },
  { tz: 'GMT-04:00', gmt: 'America/Anguilla' },
  { tz: 'GMT-04:00', gmt: 'America/Antigua' },
  { tz: 'GMT-04:00', gmt: 'America/Aruba' },
  { tz: 'GMT-04:00', gmt: 'America/Barbados' },
  { tz: 'GMT-04:00', gmt: 'America/Blanc-Sablon' },
  { tz: 'GMT-04:00', gmt: 'America/Boa Vista' },
  { tz: 'GMT-04:00', gmt: 'America/Campo Grande' },
  { tz: 'GMT-04:00', gmt: 'America/Caracas' },
  { tz: 'GMT-04:00', gmt: 'America/Cuiaba' },
  { tz: 'GMT-04:00', gmt: 'America/Curacao' },
  { tz: 'GMT-04:00', gmt: 'America/Dominica' },
  { tz: 'GMT-04:00', gmt: 'America/Glace Bay' },
  { tz: 'GMT-04:00', gmt: 'America/Goose Bay' },
  { tz: 'GMT-04:00', gmt: 'America/Grenada' },
  { tz: 'GMT-04:00', gmt: 'America/Guadeloupe' },
  { tz: 'GMT-04:00', gmt: 'America/Guyana' },
  { tz: 'GMT-04:00', gmt: 'America/Halifax' },
  { tz: 'GMT-04:00', gmt: 'America/Kralendijk' },
  { tz: 'GMT-04:00', gmt: 'America/La Paz' },
  { tz: 'GMT-04:00', gmt: 'America/Lower Princes' },
  { tz: 'GMT-04:00', gmt: 'America/Manaus' },
  { tz: 'GMT-04:00', gmt: 'America/Marigot' },
  { tz: 'GMT-04:00', gmt: 'America/Martinique' },
  { tz: 'GMT-04:00', gmt: 'America/Moncton' },
  { tz: 'GMT-04:00', gmt: 'America/Montserrat' },
  { tz: 'GMT-04:00', gmt: 'America/Port of_Spain' },
  { tz: 'GMT-04:00', gmt: 'America/Porto Velho' },
  { tz: 'GMT-04:00', gmt: 'America/Puerto Rico' },
  { tz: 'GMT-04:00', gmt: 'America/Santo Domingo' },
  { tz: 'GMT-04:00', gmt: 'America/St Barthelemy' },
  { tz: 'GMT-04:00', gmt: 'America/St Kitts' },
  { tz: 'GMT-04:00', gmt: 'America/St Lucia' },
  { tz: 'GMT-04:00', gmt: 'America/St Thomas' },
  { tz: 'GMT-04:00', gmt: 'America/St Vincent' },
  { tz: 'GMT-04:00', gmt: 'America/Thule' },
  { tz: 'GMT-04:00', gmt: 'America/Tortola' },
  { tz: 'GMT-04:00', gmt: 'Atlantic/Bermuda' },
  { tz: 'GMT-04:00', gmt: 'Etc/GMT+4' },
  { tz: 'GMT-03:30', gmt: 'America/St Johns' },
  { tz: 'GMT-03:00', gmt: 'America/Araguaina' },
  { tz: 'GMT-03:00', gmt: 'America/Argentina/Buenos Aires' },
  { tz: 'GMT-03:00', gmt: 'America/Argentina/Catamarca' },
  { tz: 'GMT-03:00', gmt: 'America/Argentina/Cordoba' },
  { tz: 'GMT-03:00', gmt: 'America/Argentina/Jujuy' },
  { tz: 'GMT-03:00', gmt: 'America/Argentina/La Rioja' },
  { tz: 'GMT-03:00', gmt: 'America/Argentina/Mendoza' },
  { tz: 'GMT-03:00', gmt: 'America/Argentina/Rio Gallegos' },
  { tz: 'GMT-03:00', gmt: 'America/Argentina/Salta' },
  { tz: 'GMT-03:00', gmt: 'America/Argentina/San Juan' },
  { tz: 'GMT-03:00', gmt: 'America/Argentina/San Luis' },
  { tz: 'GMT-03:00', gmt: 'America/Argentina/Tucuman' },
  { tz: 'GMT-03:00', gmt: 'America/Argentina/Ushuaia' },
  { tz: 'GMT-03:00', gmt: 'America/Asuncion' },
  { tz: 'GMT-03:00', gmt: 'America/Bahia' },
  { tz: 'GMT-03:00', gmt: 'America/Belem' },
  { tz: 'GMT-03:00', gmt: 'America/Cayenne' },
  { tz: 'GMT-03:00', gmt: 'America/Fortaleza' },
  { tz: 'GMT-03:00', gmt: 'America/Godthab' },
  { tz: 'GMT-03:00', gmt: 'America/Maceio' },
  { tz: 'GMT-03:00', gmt: 'America/Miquelon' },
  { tz: 'GMT-03:00', gmt: 'America/Montevideo' },
  { tz: 'GMT-03:00', gmt: 'America/Nuuk' },
  { tz: 'GMT-03:00', gmt: 'America/Paramaribo' },
  { tz: 'GMT-03:00', gmt: 'America/Punta Arenas' },
  { tz: 'GMT-03:00', gmt: 'America/Recife' },
  { tz: 'GMT-03:00', gmt: 'America/Santarem' },
  { tz: 'GMT-03:00', gmt: 'America/Santiago' },
  { tz: 'GMT-03:00', gmt: 'America/Sao Paulo' },
  { tz: 'GMT-03:00', gmt: 'Antarctica/Palmer' },
  { tz: 'GMT-03:00', gmt: 'Antarctica/Rothera' },
  { tz: 'GMT-03:00', gmt: 'Atlantic/Stanley' },
  { tz: 'GMT-03:00', gmt: 'Etc/GMT+3' },
  { tz: 'GMT-02:00', gmt: 'America/Noronha' },
  { tz: 'GMT-02:00', gmt: 'Atlantic/South Georgia' },
  { tz: 'GMT-02:00', gmt: 'Etc/GMT+2' },
  { tz: 'GMT-01:00', gmt: 'America/Scoresbysund' },
  { tz: 'GMT-01:00', gmt: 'Atlantic/Azores' },
  { tz: 'GMT-01:00', gmt: 'Atlantic/Cape Verde' },
  { tz: 'GMT-01:00', gmt: 'Etc/GMT+1' },
  { tz: 'GMT+00:00', gmt: 'Africa/Abidjan' },
  { tz: 'GMT+00:00', gmt: 'Africa/Accra' },
  { tz: 'GMT+00:00', gmt: 'Africa/Bamako' },
  { tz: 'GMT+00:00', gmt: 'Africa/Banjul' },
  { tz: 'GMT+00:00', gmt: 'Africa/Bissau' },
  { tz: 'GMT+00:00', gmt: 'Africa/Conakry' },
  { tz: 'GMT+00:00', gmt: 'Africa/Dakar' },
  { tz: 'GMT+00:00', gmt: 'Africa/Freetown' },
  { tz: 'GMT+00:00', gmt: 'Africa/Lome' },
  { tz: 'GMT+00:00', gmt: 'Africa/Monrovia' },
  { tz: 'GMT+00:00', gmt: 'Africa/Nouakchott' },
  { tz: 'GMT+00:00', gmt: 'Africa/Ouagadougou' },
  { tz: 'GMT+00:00', gmt: 'Africa/Sao Tome' },
  { tz: 'GMT+00:00', gmt: 'America/Danmarkshavn' },
  { tz: 'GMT+00:00', gmt: 'Antarctica/Troll' },
  { tz: 'GMT+00:00', gmt: 'Atlantic/Canary' },
  { tz: 'GMT+00:00', gmt: 'Atlantic/Faroe' },
  { tz: 'GMT+00:00', gmt: 'Atlantic/Madeira' },
  { tz: 'GMT+00:00', gmt: 'Atlantic/Reykjavik' },
  { tz: 'GMT+00:00', gmt: 'Atlantic/St Helena' },
  { tz: 'GMT+00:00', gmt: 'Etc/GMT' },
  { tz: 'GMT+00:00', gmt: 'Etc/GMT+0' },
  { tz: 'GMT+00:00', gmt: 'Etc/GMT-0' },
  { tz: 'GMT+00:00', gmt: 'Etc/GMT0' },
  { tz: 'GMT+00:00', gmt: 'Etc/Greenwich' },
  { tz: 'GMT+00:00', gmt: 'Etc/Universal' },
  { tz: 'GMT+00:00', gmt: 'Etc/Zulu' },
  { tz: 'GMT+00:00', gmt: 'Europe/Dublin' },
  { tz: 'GMT+00:00', gmt: 'Europe/Guernsey' },
  { tz: 'GMT+00:00', gmt: 'Europe/Isle of_Man' },
  { tz: 'GMT+00:00', gmt: 'Europe/Jersey' },
  { tz: 'GMT+00:00', gmt: 'Europe/Lisbon' },
  { tz: 'GMT+00:00', gmt: 'Europe/London' },
  { tz: 'GMT+00:00', gmt: 'GMT' },
  { tz: 'GMT+00:00', gmt: 'UTC' },
  { tz: 'GMT+00:00', gmt: 'WET' },
  { tz: 'GMT+01:00', gmt: 'Africa/Algiers' },
  { tz: 'GMT+01:00', gmt: 'Africa/Bangui' },
  { tz: 'GMT+01:00', gmt: 'Africa/Brazzaville' },
  { tz: 'GMT+01:00', gmt: 'Africa/Casablanca' },
  { tz: 'GMT+01:00', gmt: 'Africa/Ceuta' },
  { tz: 'GMT+01:00', gmt: 'Africa/Douala' },
  { tz: 'GMT+01:00', gmt: 'Africa/El Aaiun' },
  { tz: 'GMT+01:00', gmt: 'Africa/Kinshasa' },
  { tz: 'GMT+01:00', gmt: 'Africa/Lagos' },
  { tz: 'GMT+01:00', gmt: 'Africa/Libreville' },
  { tz: 'GMT+01:00', gmt: 'Africa/Luanda' },
  { tz: 'GMT+01:00', gmt: 'Africa/Malabo' },
  { tz: 'GMT+01:00', gmt: 'Africa/Ndjamena' },
  { tz: 'GMT+01:00', gmt: 'Africa/Niamey' },
  { tz: 'GMT+01:00', gmt: 'Africa/Porto-Novo' },
  { tz: 'GMT+01:00', gmt: 'Africa/Tunis' },
  { tz: 'GMT+01:00', gmt: 'Arctic/Longyearbyen' },
  { tz: 'GMT+01:00', gmt: 'CET' },
  { tz: 'GMT+01:00', gmt: 'Etc/GMT-1' },
  { tz: 'GMT+01:00', gmt: 'Europe/Amsterdam' },
  { tz: 'GMT+01:00', gmt: 'Europe/Andorra' },
  { tz: 'GMT+01:00', gmt: 'Europe/Belgrade' },
  { tz: 'GMT+01:00', gmt: 'Europe/Berlin' },
  { tz: 'GMT+01:00', gmt: 'Europe/Bratislava' },
  { tz: 'GMT+01:00', gmt: 'Europe/Brussels' },
  { tz: 'GMT+01:00', gmt: 'Europe/Budapest' },
  { tz: 'GMT+01:00', gmt: 'Europe/Busingen' },
  { tz: 'GMT+01:00', gmt: 'Europe/Copenhagen' },
  { tz: 'GMT+01:00', gmt: 'Europe/Gibraltar' },
  { tz: 'GMT+01:00', gmt: 'Europe/Ljubljana' },
  { tz: 'GMT+01:00', gmt: 'Europe/Luxembourg' },
  { tz: 'GMT+01:00', gmt: 'Europe/Madrid' },
  { tz: 'GMT+01:00', gmt: 'Europe/Malta' },
  { tz: 'GMT+01:00', gmt: 'Europe/Monaco' },
  { tz: 'GMT+01:00', gmt: 'Europe/Oslo' },
  { tz: 'GMT+01:00', gmt: 'Europe/Paris' },
  { tz: 'GMT+01:00', gmt: 'Europe/Podgorica' },
  { tz: 'GMT+01:00', gmt: 'Europe/Prague' },
  { tz: 'GMT+01:00', gmt: 'Europe/Rome' },
  { tz: 'GMT+01:00', gmt: 'Europe/San Marino' },
  { tz: 'GMT+01:00', gmt: 'Europe/Sarajevo' },
  { tz: 'GMT+01:00', gmt: 'Europe/Skopje' },
  { tz: 'GMT+01:00', gmt: 'Europe/Stockholm' },
  { tz: 'GMT+01:00', gmt: 'Europe/Tirane' },
  { tz: 'GMT+01:00', gmt: 'Europe/Vaduz' },
  { tz: 'GMT+01:00', gmt: 'Europe/Vatican' },
  { tz: 'GMT+01:00', gmt: 'Europe/Vienna' },
  { tz: 'GMT+01:00', gmt: 'Europe/Warsaw' },
  { tz: 'GMT+01:00', gmt: 'Europe/Zagreb' },
  { tz: 'GMT+01:00', gmt: 'Europe/Zurich' },
  { tz: 'GMT+01:00', gmt: 'MET' },
  { tz: 'GMT+02:00', gmt: 'Africa/Blantyre' },
  { tz: 'GMT+02:00', gmt: 'Africa/Bujumbura' },
  { tz: 'GMT+02:00', gmt: 'Africa/Cairo' },
  { tz: 'GMT+02:00', gmt: 'Africa/Gaborone' },
  { tz: 'GMT+02:00', gmt: 'Africa/Harare' },
  { tz: 'GMT+02:00', gmt: 'Africa/Johannesburg' },
  { tz: 'GMT+02:00', gmt: 'Africa/Khartoum' },
  { tz: 'GMT+02:00', gmt: 'Africa/Kigali' },
  { tz: 'GMT+02:00', gmt: 'Africa/Lubumbashi' },
  { tz: 'GMT+02:00', gmt: 'Africa/Lusaka' },
  { tz: 'GMT+02:00', gmt: 'Africa/Maputo' },
  { tz: 'GMT+02:00', gmt: 'Africa/Maseru' },
  { tz: 'GMT+02:00', gmt: 'Africa/Mbabane' },
  { tz: 'GMT+02:00', gmt: 'Africa/Tripoli' },
  { tz: 'GMT+02:00', gmt: 'Africa/Windhoek' },
  { tz: 'GMT+02:00', gmt: 'Asia/Amman' },
  { tz: 'GMT+02:00', gmt: 'Asia/Beirut' },
  { tz: 'GMT+02:00', gmt: 'Asia/Damascus' },
  { tz: 'GMT+02:00', gmt: 'Asia/Famagusta' },
  { tz: 'GMT+02:00', gmt: 'Asia/Gaza' },
  { tz: 'GMT+02:00', gmt: 'Asia/Hebron' },
  { tz: 'GMT+02:00', gmt: 'Asia/Jerusalem' },
  { tz: 'GMT+02:00', gmt: 'Asia/Nicosia' },
  { tz: 'GMT+02:00', gmt: 'EET' },
  { tz: 'GMT+02:00', gmt: 'Etc/GMT-2' },
  { tz: 'GMT+02:00', gmt: 'Europe/Athens' },
  { tz: 'GMT+02:00', gmt: 'Europe/Bucharest' },
  { tz: 'GMT+02:00', gmt: 'Europe/Chisinau' },
  { tz: 'GMT+02:00', gmt: 'Europe/Helsinki' },
  { tz: 'GMT+02:00', gmt: 'Europe/Kaliningrad' },
  { tz: 'GMT+02:00', gmt: 'Europe/Kiev' },
  { tz: 'GMT+02:00', gmt: 'Europe/Mariehamn' },
  { tz: 'GMT+02:00', gmt: 'Europe/Nicosia' },
  { tz: 'GMT+02:00', gmt: 'Europe/Riga' },
  { tz: 'GMT+02:00', gmt: 'Europe/Sofia' },
  { tz: 'GMT+02:00', gmt: 'Europe/Tallinn' },
  { tz: 'GMT+02:00', gmt: 'Europe/Uzhgorod' },
  { tz: 'GMT+02:00', gmt: 'Europe/Vilnius' },
  { tz: 'GMT+02:00', gmt: 'Europe/Zaporozhye' },
  { tz: 'GMT+03:00', gmt: 'Africa/Addis Ababa' },
  { tz: 'GMT+03:00', gmt: 'Africa/Asmara' },
  { tz: 'GMT+03:00', gmt: 'Africa/Dar es_Salaam' },
  { tz: 'GMT+03:00', gmt: 'Africa/Djibouti' },
  { tz: 'GMT+03:00', gmt: 'Africa/Juba' },
  { tz: 'GMT+03:00', gmt: 'Africa/Kampala' },
  { tz: 'GMT+03:00', gmt: 'Africa/Mogadishu' },
  { tz: 'GMT+03:00', gmt: 'Africa/Nairobi' },
  { tz: 'GMT+03:00', gmt: 'Antarctica/Syowa' },
  { tz: 'GMT+03:00', gmt: 'Asia/Aden' },
  { tz: 'GMT+03:00', gmt: 'Asia/Baghdad' },
  { tz: 'GMT+03:00', gmt: 'Asia/Bahrain' },
  { tz: 'GMT+03:00', gmt: 'Asia/Istanbul' },
  { tz: 'GMT+03:00', gmt: 'Asia/Kuwait' },
  { tz: 'GMT+03:00', gmt: 'Asia/Qatar' },
  { tz: 'GMT+03:00', gmt: 'Asia/Riyadh' },
  { tz: 'GMT+03:00', gmt: 'Etc/GMT-3' },
  { tz: 'GMT+03:00', gmt: 'Europe/Istanbul' },
  { tz: 'GMT+03:00', gmt: 'Europe/Kirov' },
  { tz: 'GMT+03:00', gmt: 'Europe/Minsk' },
  { tz: 'GMT+03:00', gmt: 'Europe/Moscow' },
  { tz: 'GMT+03:00', gmt: 'Europe/Simferopol' },
  { tz: 'GMT+03:00', gmt: 'Indian/Antananarivo' },
  { tz: 'GMT+03:00', gmt: 'Indian/Comoro' },
  { tz: 'GMT+03:00', gmt: 'Indian/Mayotte' },
  { tz: 'GMT+03:30', gmt: 'Asia/Tehran' },
  { tz: 'GMT+04:00', gmt: 'Asia/Baku' },
  { tz: 'GMT+04:00', gmt: 'Asia/Dubai' },
  { tz: 'GMT+04:00', gmt: 'Asia/Muscat' },
  { tz: 'GMT+04:00', gmt: 'Asia/Tbilisi' },
  { tz: 'GMT+04:00', gmt: 'Asia/Yerevan' },
  { tz: 'GMT+04:00', gmt: 'Etc/GMT-4' },
  { tz: 'GMT+04:00', gmt: 'Europe/Astrakhan' },
  { tz: 'GMT+04:00', gmt: 'Europe/Samara' },
  { tz: 'GMT+04:00', gmt: 'Europe/Saratov' },
  { tz: 'GMT+04:00', gmt: 'Europe/Ulyanovsk' },
  { tz: 'GMT+04:00', gmt: 'Europe/Volgograd' },
  { tz: 'GMT+04:00', gmt: 'Indian/Mahe' },
  { tz: 'GMT+04:00', gmt: 'Indian/Mauritius' },
  { tz: 'GMT+04:00', gmt: 'Indian/Reunion' },
  { tz: 'GMT+04:30', gmt: 'Asia/Kabul' },
  { tz: 'GMT+05:00', gmt: 'Antarctica/Mawson' },
  { tz: 'GMT+05:00', gmt: 'Asia/Aqtau' },
  { tz: 'GMT+05:00', gmt: 'Asia/Aqtobe' },
  { tz: 'GMT+05:00', gmt: 'Asia/Ashgabat' },
  { tz: 'GMT+05:00', gmt: 'Asia/Atyrau' },
  { tz: 'GMT+05:00', gmt: 'Asia/Dushanbe' },
  { tz: 'GMT+05:00', gmt: 'Asia/Karachi' },
  { tz: 'GMT+05:00', gmt: 'Asia/Oral' },
  { tz: 'GMT+05:00', gmt: 'Asia/Qyzylorda' },
  { tz: 'GMT+05:00', gmt: 'Asia/Samarkand' },
  { tz: 'GMT+05:00', gmt: 'Asia/Tashkent' },
  { tz: 'GMT+05:00', gmt: 'Asia/Yekaterinburg' },
  { tz: 'GMT+05:00', gmt: 'Etc/GMT-5' },
  { tz: 'GMT+05:00', gmt: 'Indian/Kerguelen' },
  { tz: 'GMT+05:00', gmt: 'Indian/Maldives' },
  { tz: 'GMT+05:30', gmt: 'Asia/Calcutta' },
  { tz: 'GMT+05:30', gmt: 'Asia/Colombo' },
  { tz: 'GMT+05:30', gmt: 'Asia/Kolkata' },
  { tz: 'GMT+05:45', gmt: 'Asia/Kathmandu' },
  { tz: 'GMT+05:45', gmt: 'Asia/Katmandu' },
  { tz: 'GMT+06:00', gmt: 'Antarctica/Vostok' },
  { tz: 'GMT+06:00', gmt: 'Asia/Almaty' },
  { tz: 'GMT+06:00', gmt: 'Asia/Bishkek' },
  { tz: 'GMT+06:00', gmt: 'Asia/Dhaka' },
  { tz: 'GMT+06:00', gmt: 'Asia/Omsk' },
  { tz: 'GMT+06:00', gmt: 'Asia/Qostanay' },
  { tz: 'GMT+06:00', gmt: 'Asia/Thimphu' },
  { tz: 'GMT+06:00', gmt: 'Asia/Urumqi' },
  { tz: 'GMT+06:00', gmt: 'Etc/GMT-6' },
  { tz: 'GMT+06:00', gmt: 'Indian/Chagos' },
  { tz: 'GMT+06:30', gmt: 'Asia/Yangon' },
  { tz: 'GMT+06:30', gmt: 'Indian/Cocos' },
  { tz: 'GMT+07:00', gmt: 'Antarctica/Davis' },
  { tz: 'GMT+07:00', gmt: 'Asia/Bangkok' },
  { tz: 'GMT+07:00', gmt: 'Asia/Barnaul' },
  { tz: 'GMT+07:00', gmt: 'Asia/Ho Chi_Minh' },
  { tz: 'GMT+07:00', gmt: 'Asia/Hovd' },
  { tz: 'GMT+07:00', gmt: 'Asia/Jakarta' },
  { tz: 'GMT+07:00', gmt: 'Asia/Krasnoyarsk' },
  { tz: 'GMT+07:00', gmt: 'Asia/Novokuznetsk' },
  { tz: 'GMT+07:00', gmt: 'Asia/Novosibirsk' },
  { tz: 'GMT+07:00', gmt: 'Asia/Phnom Penh' },
  { tz: 'GMT+07:00', gmt: 'Asia/Pontianak' },
  { tz: 'GMT+07:00', gmt: 'Asia/Tomsk' },
  { tz: 'GMT+07:00', gmt: 'Asia/Vientiane' },
  { tz: 'GMT+07:00', gmt: 'Etc/GMT-7' },
  { tz: 'GMT+07:00', gmt: 'Indian/Christmas' },
  { tz: 'GMT+08:00', gmt: 'Asia/Brunei' },
  { tz: 'GMT+08:00', gmt: 'Asia/Choibalsan' },
  { tz: 'GMT+08:00', gmt: 'Asia/Hong Kong' },
  { tz: 'GMT+08:00', gmt: 'Asia/Irkutsk' },
  { tz: 'GMT+08:00', gmt: 'Asia/Kuala Lumpur' },
  { tz: 'GMT+08:00', gmt: 'Asia/Kuching' },
  { tz: 'GMT+08:00', gmt: 'Asia/Macau' },
  { tz: 'GMT+08:00', gmt: 'Asia/Makassar' },
  { tz: 'GMT+08:00', gmt: 'Asia/Manila' },
  { tz: 'GMT+08:00', gmt: 'Asia/Shanghai' },
  { tz: 'GMT+08:00', gmt: 'Asia/Singapore' },
  { tz: 'GMT+08:00', gmt: 'Asia/Taipei' },
  { tz: 'GMT+08:00', gmt: 'Asia/Ulaanbaatar' },
  { tz: 'GMT+08:00', gmt: 'Australia/Perth' },
  { tz: 'GMT+08:00', gmt: 'Etc/GMT-8' },
  { tz: 'GMT+08:45', gmt: 'Australia/Eucla' },
  { tz: 'GMT+09:00', gmt: 'Asia/Chita' },
  { tz: 'GMT+09:00', gmt: 'Asia/Dili' },
  { tz: 'GMT+09:00', gmt: 'Asia/Jayapura' },
  { tz: 'GMT+09:00', gmt: 'Asia/Khandyga' },
  { tz: 'GMT+09:00', gmt: 'Asia/Pyongyang' },
  { tz: 'GMT+09:00', gmt: 'Asia/Seoul' },
  { tz: 'GMT+09:00', gmt: 'Asia/Tokyo' },
  { tz: 'GMT+09:00', gmt: 'Asia/Yakutsk' },
  { tz: 'GMT+09:00', gmt: 'Etc/GMT-9' },
  { tz: 'GMT+09:00', gmt: 'Pacific/Palau' },
  { tz: 'GMT+09:30', gmt: 'Australia/Darwin' },
  { tz: 'GMT+10:00', gmt: 'Antarctica/DumontDUrville' },
  { tz: 'GMT+10:00', gmt: 'Asia/Ust-Nera' },
  { tz: 'GMT+10:00', gmt: 'Asia/Vladivostok' },
  { tz: 'GMT+10:00', gmt: 'Australia/Brisbane' },
  { tz: 'GMT+10:00', gmt: 'Australia/Lindeman' },
  { tz: 'GMT+10:00', gmt: 'Etc/GMT-10' },
  { tz: 'GMT+10:00', gmt: 'Pacific/Chuuk' },
  { tz: 'GMT+10:00', gmt: 'Pacific/Guam' },
  { tz: 'GMT+10:00', gmt: 'Pacific/Port Moresby' },
  { tz: 'GMT+10:00', gmt: 'Pacific/Saipan' },
  { tz: 'GMT+10:30', gmt: 'Australia/Adelaide' },
  { tz: 'GMT+10:30', gmt: 'Australia/Broken Hill' },
  { tz: 'GMT+11:00', gmt: 'Antarctica/Casey' },
  { tz: 'GMT+11:00', gmt: 'Antarctica/Macquarie' },
  { tz: 'GMT+11:00', gmt: 'Asia/Magadan' },
  { tz: 'GMT+11:00', gmt: 'Asia/Sakhalin' },
  { tz: 'GMT+11:00', gmt: 'Asia/Srednekolymsk' },
  { tz: 'GMT+11:00', gmt: 'Australia/Currie' },
  { tz: 'GMT+11:00', gmt: 'Australia/Hobart' },
  { tz: 'GMT+11:00', gmt: 'Australia/Lord Howe' },
  { tz: 'GMT+11:00', gmt: 'Australia/Melbourne' },
  { tz: 'GMT+11:00', gmt: 'Australia/Sydney' },
  { tz: 'GMT+11:00', gmt: 'Etc/GMT-11' },
  { tz: 'GMT+11:00', gmt: 'Pacific/Bougainville' },
  { tz: 'GMT+11:00', gmt: 'Pacific/Efate' },
  { tz: 'GMT+11:00', gmt: 'Pacific/Guadalcanal' },
  { tz: 'GMT+11:00', gmt: 'Pacific/Kosrae' },
  { tz: 'GMT+11:00', gmt: 'Pacific/Noumea' },
  { tz: 'GMT+11:00', gmt: 'Pacific/Pohnpei' },
  { tz: 'GMT+12:00', gmt: 'Asia/Anadyr' },
  { tz: 'GMT+12:00', gmt: 'Asia/Kamchatka' },
  { tz: 'GMT+12:00', gmt: 'Etc/GMT-12' },
  { tz: 'GMT+12:00', gmt: 'Pacific/Fiji' },
  { tz: 'GMT+12:00', gmt: 'Pacific/Funafuti' },
  { tz: 'GMT+12:00', gmt: 'Pacific/Kwajalein' },
  { tz: 'GMT+12:00', gmt: 'Pacific/Majuro' },
  { tz: 'GMT+12:00', gmt: 'Pacific/Nauru' },
  { tz: 'GMT+12:00', gmt: 'Pacific/Norfolk' },
  { tz: 'GMT+12:00', gmt: 'Pacific/Tarawa' },
  { tz: 'GMT+12:00', gmt: 'Pacific/Wake' },
  { tz: 'GMT+12:00', gmt: 'Pacific/Wallis' },
  { tz: 'GMT+13:00', gmt: 'Antarctica/McMurdo' },
  { tz: 'GMT+13:00', gmt: 'Etc/GMT-13' },
  { tz: 'GMT+13:00', gmt: 'Pacific/Auckland' },
  { tz: 'GMT+13:00', gmt: 'Pacific/Enderbury' },
  { tz: 'GMT+13:00', gmt: 'Pacific/Fakaofo' },
  { tz: 'GMT+13:00', gmt: 'Pacific/Tongatapu' },
  { tz: 'GMT+13:45', gmt: 'Pacific/Chatham' },
  { tz: 'GMT+14:00', gmt: 'Etc/GMT-14' },
  { tz: 'GMT+14:00', gmt: 'Pacific/Apia' },
  { tz: 'GMT+14:00', gmt: 'Pacific/Kiritimati' }
]
```

## 语言

```
[
  { code: 'am-ET', nation: '埃塞俄比亚', lang: '阿姆哈拉语(埃塞俄比亚)' },
  { code: 'ar-AE', nation: '阿联酋', lang: '阿拉伯语(阿联酋)' },
  { code: 'ar-BH', nation: '巴林', lang: '阿拉伯语(巴林)' },
  { code: 'ar-DZ', nation: '阿尔及利亚', lang: '阿拉伯语(阿尔及利亚)' },
  { code: 'ar-EG', nation: '埃及', lang: '阿拉伯语(埃及)' },
  { code: 'ar-IQ', nation: '伊拉克', lang: '阿拉伯语(伊拉克)' },
  { code: 'ar-JO', nation: '约旦', lang: '阿拉伯语(约旦)' },
  { code: 'ar-KW', nation: '科威特', lang: '阿拉伯语(科威特)' },
  { code: 'ar-LB', nation: '黎巴嫩', lang: '阿拉伯语(黎巴嫩)' },
  { code: 'ar-LY', nation: '利比亚', lang: '阿拉伯语(利比亚)' },
  { code: 'ar-MA', nation: '摩洛哥', lang: '阿拉伯语(摩洛哥)' },
  { code: 'ar-OM', nation: '阿曼', lang: '阿拉伯语(阿曼)' },
  { code: 'ar-QA', nation: '卡塔尔', lang: '阿拉伯语(卡塔尔)' },
  { code: 'ar-SA', nation: '沙特阿拉伯', lang: '阿拉伯语(沙特阿拉伯)' },
  { code: 'ar-SY', nation: '叙利亚', lang: '阿拉伯语(叙利亚)' },
  { code: 'ar-TN', nation: '突尼斯', lang: '阿拉伯语(突尼斯)' },
  { code: 'ar-YE', nation: '也门', lang: '阿拉伯语(也门)' },
  { code: 'arn-CL', nation: '智利', lang: '马普丹冈语(智利)' },
  { code: 'az-Cyrl-AZ', nation: '阿塞拜疆', lang: '阿塞拜疆语(阿塞拜疆)' },
  { code: 'az-Latn-AZ', nation: '阿塞拜疆', lang: '阿塞拜疆语(阿塞拜疆)' },
  { code: 'be-BY', nation: '白俄罗斯', lang: '白俄罗斯语(白俄罗斯)' },
  { code: 'bg-BG', nation: '保加利亚', lang: '保加利亚语(保加利亚)' },
  { code: 'bn-BD', nation: '孟加拉', lang: '孟加拉语(孟加拉)' },
  { code: 'bs-BA', nation: '波黑', lang: '波斯尼亚语(波黑)' },
  { code: 'ca-ES', nation: '西班牙', lang: '加泰罗尼亚语(西班牙)' },
  { code: 'cs-CZ', nation: '捷克', lang: '捷克语(捷克)' },
  { code: 'da-DK', nation: '丹麦', lang: '丹麦语(丹麦)' },
  { code: 'de-AT', nation: '奥地利', lang: '德语(奥地利)' },
  { code: 'de-CH', nation: '瑞士', lang: '德语(瑞士)' },
  { code: 'de-DE', nation: '德国', lang: '德语(德国)' },
  { code: 'de-LI', nation: '列支敦士登', lang: '德语(列支敦士登)' },
  { code: 'de-LU', nation: '卢森堡', lang: '德语(卢森堡)' },
  { code: 'dv-MV', nation: '马尔代夫', lang: '迪维希语(马尔代夫)' },
  { code: 'el-GR', nation: '希腊', lang: '希腊语(希腊)' },
  { code: 'en-AU', nation: '澳大利亚', lang: '英语(澳大利亚)' },
  { code: 'en-BZ', nation: '伯利兹', lang: '英语(伯利兹)' },
  { code: 'en-US', nation: '美国', lang: '英语(美国)' },
  { code: 'en-GB', nation: '英国', lang: '英语(英国)' },
  { code: 'en-JM', nation: '牙买加', lang: '英语(牙买加)' },
  { code: 'en-NZ', nation: '新西兰', lang: '英语(新西兰)' },
  { code: 'en-SG', nation: '新加坡', lang: '英语(新加坡)' },
  { code: 'en-TT', nation: '特立尼达和多巴哥', lang: '英语(特立尼达和多巴哥)' },
  { code: 'en-ZW', nation: '津巴布韦', lang: '英语(津巴布韦)' },
  { code: 'es-AR', nation: '阿根廷', lang: '西班牙语(阿根廷)' },
  { code: 'es-CO', nation: '哥伦比亚', lang: '西班牙语(哥伦比亚)' },
  { code: 'es-CR', nation: '哥斯达黎加', lang: '西班牙语(哥斯达黎加)' },
  { code: 'es-DO', nation: '多米尼加', lang: '西班牙语(多米尼加)' },
  { code: 'es-ES', nation: '西班牙', lang: '西班牙语(西班牙)' },
  { code: 'es-HN', nation: '洪都拉斯', lang: '西班牙语(洪都拉斯)' },
  { code: 'es-MX', nation: '墨西哥', lang: '西班牙语(墨西哥)' },
  { code: 'es-NI', nation: '尼加拉瓜', lang: '西班牙语(尼加拉瓜)' },
  { code: 'es-PA', nation: '巴拿马', lang: '西班牙语(巴拿马)' },
  { code: 'es-PR', nation: '波多黎各', lang: '西班牙语(波多黎各)' },
  { code: 'es-PY', nation: '巴拉圭', lang: '西班牙语(巴拉圭)' },
  { code: 'es-SV', nation: '萨尔瓦多', lang: '西班牙语(萨尔瓦多)' },
  { code: 'es-UY', nation: '乌拉圭', lang: '西班牙语(乌拉圭)' },
  { code: 'es-VE', nation: '委内瑞拉', lang: '西班牙语(委内瑞拉)' },
  { code: 'et-EE', nation: '爱沙尼亚', lang: '爱沙尼亚语(爱沙尼亚)' },
  { code: 'fa-IR', nation: '伊朗', lang: '波斯语(伊朗)' },
  { code: 'fi-FI', nation: '芬兰', lang: '芬兰语(芬兰)' },
  { code: 'fil-PH', nation: '菲律宾', lang: '菲律宾语(菲律宾)' },
  { code: 'fo-FO', nation: '法罗群岛', lang: '法罗语(法罗群岛)' },
  { code: 'fr-BE', nation: '比利时', lang: '法语(比利时)' },
  { code: 'fr-CA', nation: '加拿大', lang: '法语(加拿大)' },
  { code: 'fr-FR', nation: '法国', lang: '法语(法国)' },
  { code: 'fr-MC', nation: '摩纳哥', lang: '法语(摩纳哥)' },
  { code: 'fy-NL', nation: '荷兰', lang: '弗里西亚语(荷兰)' },
  { code: 'ga-IE', nation: '爱尔兰', lang: '爱尔兰语(爱尔兰)' },
  { code: 'ha-Latn-NG', nation: '尼日利亚', lang: '豪撒语(尼日利亚)' },
  { code: 'he-IL', nation: '以色列', lang: '希伯来语(以色列)' },
  { code: 'hi-IN', nation: '印度', lang: '印地语(印度)' },
  { code: 'hr-BA', nation: '波黑', lang: '克罗地亚语(波黑)' },
  { code: 'hr-HR', nation: '克罗地亚', lang: '克罗地亚语(克罗地亚)' },
  { code: 'hu-HU', nation: '匈牙利', lang: '匈牙利语(匈牙利)' },
  { code: 'hy-AM', nation: '亚美尼亚', lang: '亚美尼亚语(亚美尼亚)' },
  { code: 'id-ID', nation: '印度尼西亚', lang: '印度尼西亚语(印尼)' },
  { code: 'is-IS', nation: '冰岛', lang: '冰岛语(冰岛)' },
  { code: 'it-IT', nation: '意大利', lang: '意大利语(意大利)' },
  { code: 'ja-JP', nation: '日本', lang: '日语(日本)' },
  { code: 'ka-GE', nation: '格鲁吉亚', lang: '格鲁吉亚语(格鲁吉亚)' },
  { code: 'kk-KZ', nation: '哈萨克斯坦', lang: '哈萨克语(哈萨克斯坦)' },
  { code: 'kl-GL', nation: '格陵兰', lang: '格陵兰语(格陵兰)' },
  { code: 'km-KH', nation: '柬埔寨', lang: '高棉语(柬埔寨)' },
  { code: 'ko-KR', nation: '韩国', lang: '朝鲜语(韩国)' },
  { code: 'ky-KG', nation: '吉尔吉斯斯坦', lang: '吉尔吉斯语(吉尔吉斯斯坦)' },
  { code: 'lo-LA', nation: '老挝', lang: '老挝语(老挝)' },
  { code: 'lt-LT', nation: '立陶宛', lang: '立陶宛语(立陶宛)' },
  { code: 'lv-LV', nation: '拉脱维亚', lang: '拉脱维亚语(拉脱维亚)' },
  { code: 'mi-NZ', nation: '新西兰', lang: '毛利语(新西兰)' },
  { code: 'mk-MK', nation: '马其顿', lang: '马其顿语(马其顿)' },
  { code: 'mn-Mong', nation: '蒙古国', lang: '蒙古语(Mong)' },
  { code: 'ms-MY', nation: '马来西亚', lang: '马来语(马来西亚)' },
  { code: 'ms-BN', nation: '马来西亚', lang: '马来语(文莱)' },
  { code: 'mt-MT', nation: '马耳他', lang: '马耳他语(马耳他)' },
  { code: 'ne-NP', nation: '尼泊尔', lang: '尼泊尔语(尼泊尔)' },
  { code: 'nl-NL', nation: '荷兰', lang: '荷兰语(荷兰)' },
  { code: 'nn-no', nation: '挪威', lang: '挪威语(挪威)' },
  { code: 'nso-ZA', nation: '南非', lang: '巴索托语(南非)' },
  { code: 'pl-PL', nation: '波兰', lang: '波兰语(波兰)' },
  { code: 'prs-AF', nation: '阿富汗', lang: '达里语(阿富汗)' },
  { code: 'ps-AF', nation: '阿富汗', lang: '普什图语(阿富汗)' },
  { code: 'pt-BR', nation: '巴西', lang: '葡萄牙语(巴西)' },
  { code: 'pt-PT', nation: '葡萄牙', lang: '葡萄牙语(葡萄牙)' },
  { code: 'qut-GT', nation: '危地马拉', lang: '基切语(危地马拉)' },
  { code: 'quz-BO', nation: '玻利维亚', lang: '克丘亚语(玻利维亚)' },
  { code: 'quz-EC', nation: '厄瓜多尔', lang: '克丘亚语(厄瓜多尔)' },
  { code: 'quz-PE', nation: '秘鲁', lang: '克丘亚语(秘鲁)' },
  { code: 'ro-RO', nation: '罗马尼亚', lang: '罗马尼亚语(罗马尼亚)' },
  { code: 'ru-RU', nation: '俄罗斯', lang: '俄语(俄罗斯)' },
  { code: 'rw-RW', nation: '卢旺达', lang: '卢旺达语(卢旺达)' },
  { code: 'se-FI', nation: '芬兰', lang: '北萨米语(芬兰)' },
  { code: 'se-NO', nation: '挪威', lang: '北萨米语(挪威)' },
  { code: 'se-SE', nation: '瑞典', lang: '北萨米语(瑞典)' },
  { code: 'si-LK', nation: '斯里兰卡', lang: '僧伽罗语(斯里兰卡)' },
  { code: 'sk-SK', nation: '斯洛伐克', lang: '斯洛伐克语(斯洛伐克)' },
  { code: 'sl-SI', nation: '斯洛文尼亚', lang: '斯洛文尼亚语(斯洛文尼亚)' },
  { code: 'sq-AL', nation: '阿尔巴尼亚', lang: '阿尔巴尼亚语(阿尔巴尼亚)' },
  { code: 'sr-BA', nation: '波黑', lang: '塞尔维亚语(波黑)' },
  { code: 'sr-Latn-ME', nation: '黑山', lang: '塞尔维亚语(黑山)' },
  { code: 'sr-Latn-RS', nation: '塞尔维亚', lang: '塞尔维亚语(塞尔维亚)' },
  { code: 'sv-SE', nation: '瑞典', lang: '瑞典语(瑞典)' },
  { code: 'sw-KE', nation: '肯尼亚', lang: '斯瓦希里语(肯尼亚)' },
  { code: 'tg-Cyrl-TJ', nation: '塔吉克斯坦', lang: '塔吉克语(塔吉克斯坦)' },
  { code: 'th-TH', nation: '泰国', lang: '泰语(泰国)' },
  { code: 'tk-TM', nation: '土库曼斯坦', lang: '土库曼语(土库曼斯坦)' },
  { code: 'tn-ZA', nation: '南非', lang: '茨瓦纳语(南非)' },
  { code: 'tr-TR', nation: '土耳其', lang: '土耳其语(土耳其)' },
  { code: 'uk-UA', nation: '乌克兰', lang: '乌克兰语(乌克兰)' },
  { code: 'ur-PK', nation: '巴基斯坦', lang: '乌尔都语(巴基斯坦)' },
  { code: 'uz-Latn-UZ', nation: '乌兹别克斯坦', lang: '乌兹别克语(乌兹别克斯坦)' },
  { code: 'vi-VN', nation: '越南', lang: '越南语(越南)' },
  { code: 'wo-SN', nation: '塞内加尔', lang: '沃洛夫语(塞内加尔)' },
  { code: 'zh-CN', nation: '中国', lang: '中文(简体)' },
  { code: 'zh-HK', nation: '中国香港', lang: '中文(中国香港)' },
  { code: 'zh-MO', nation: '中国澳门', lang: '中文(中国澳门)' },
  { code: 'zh-TW', nation: '中国台湾', lang: '中文(繁体)(中国台湾)' }
]
```

## 字体

```
[
  'Comic Sans MS Bold',
  'Comic Sans',
  'Caurier Regular',
  'Fixedsys Regular',
  'Gabriola Regular',
  'HoloLens MDL2 Assets Regular',
  'Impact Regular',
  'Javanese Text Regular',
  'Leelawadee UI',
  'Lucida Console Regular',
  'Lucida Sans Unicode Regular',
  'Microsoft Himalaya Regular',
  'Microsoft PhangsPa',
  'Microsoft Sans Serif Regular',
  'Microsoft YaHei UI',
  'Microsoft Yi Baiti Regular',
  'MingLiu_HKSCS-ExtB Regular',
  'MingLiu-ExtB Regular',
  'Modern Regular',
  'Mongolia Baiti Regular',
  'MS Gothic Regular',
  'MS PGothic Regular',
  'MS Sans Serif Regular',
  'MS Serif Regular',
  'MS UI Gothic Regular',
  'MV Boli Regular',
  'Nimarla UI',
  'Myanmar Tet',
  'Nirmala UI',
  'NSimSun Regular',
  'PMingLiU-ExtB Regular',
  'Roman Regular',
  'Script Regular',
  'SimSun Regular',
  'SimSun-ExtB Regular',
  'Sitka Banner',
  'Sitka Display',
  'Sitka Heading',
  'Sitka Subheading',
  'Sitka Text',
  'Small Fonts Regular',
  'Sylfaen Regular',
  'Symbol Regular',
  'System Bold',
  'Tahoma',
  'Terminal',
  'Webdings Regular',
  'Wingdings Regular',
  'Yu Gothic UI',
  'Calibri Light',
  'Courier',
  'Fixedsys',
  'Franklin Gothic Medium',
  'HoloLens MDL2 Assets',
  'Leelawadee UI Semilight',
  'MS Sans Serif',
  'MS Serif',
  'Malgun Gothic Semilight',
  'Microsoft JhengHei Light',
  'Microsoft JhengHei UI Light',
  'Microsoft YaHei Light',
  'Microsoft YaHei UI Light',
  'Modern',
  'NSimsun',
  'Nirmala UI Semilight',
  'Roman',
  'Script',
  'Small Fonts',
  'System',
  'Wingdings',
  'Yu Gothic Light',
  'Yu Gothic Medium',
  'Yu Gothic UI Light',
  'Yu Gothic UI Semibold',
  'Yu Gothic UI Semilight',
  'Al Bayan',
  'Al Nile',
  'Al Tarikh',
  'American Typewriter',
  'Andale Mono',
  'Apple Braille',
  'Apple Chancery',
  'Apple Color Emoji',
  'Apple SD Gothic Neo',
  'Apple Symbols',
  'AppleGothic',
  'AppleMyungjo',
  'Avenir Next Condensed',
  'Avenir Next',
  'Avenir',
  'Ayuthaya',
  'Baghdad',
  'Bangla MN',
  'Bangla Sangam MN',
  'Baskerville',
  'Beirut',
  'Big Caslon',
  'Bodoni Ornaments',
  'Bradley Hand',
  'Brush Script MT',
  'Chalkboard SE',
  'Chalkboard',
  'Chalkduster',
  'Cochin',
  'Copperplate',
  'Corsiva Hebrew',
  'Damascus',
  'DecoType Naskh',
  'Devanagari MT',
  'Devanagari Sangam MN',
  'Didot',
  'Diwan Kufi',
  'Diwan Thuluth',
  'Euphemia UCAS',
  'Farah',
  'Farisi',
  'Futura',
  'GB18030 Bitmap',
  'Geeza Pro',
  'Geneva',
  'Gill Sans',
  'Gujarati MT',
  'Gujarati Sangam MN',
  'Gurmukhi MN',
  'Gurmukhi MT',
  'Gurmukhi Sangam MN',
  'Heiti Sc',
  'Helvetica Neue',
  'Helvetica',
  'Herculanum',
  'Hiragino Sans GB',
  'Hiragino Sans',
  'Hoefler Text',
  'ITF Devanagari',
  'InaiMathi',
  'Kannada MN',
  'Kefa',
  'Khmer MN',
  'Khmer Sangam MN',
  'Kohinoor Bangla',
  'Kohinoor Telugu',
  'Kokonor',
  'Krungthep',
  'KufiStandardGK',
  'Lao MN',
  'Lao Sangam MN',
  'Lucida Grande',
  'Luminari',
  'Marker Felt',
  'Menlo',
  'Mishafi Gold',
  'Monaco',
  'Mshtakan',
  'Muna',
  'Nadeem',
  'New Peninim MT',
  'Noteworthy',
  'Optima',
  'Oriya Sangam MN',
  'PT Mono',
  'PT Sans Caption',
  'PT Sans Narrow',
  'PT Sans',
  'PT Serif Caption',
  'PT Serif',
  'Palatino',
  'Papyrus',
  'Phosphate',
  'PingFang HK',
  'Plantagenet Cherokee',
  'Raanana',
  'STIXGeneral',
  'STIXIntegralsD',
  'STIXIntegralsSm',
  'STIXIntegralsUp',
  'STIXIntegralsUpD',
  'STIXIntegralsUpSm',
  'STIXSizeFiveSym',
  'STIXSizeFourSym',
  'STIXSizeOneSym',
  'STIXSizeThreeSym',
  'STIXSizeTwoSym',
  'STIXVariants',
  'STSong',
  'Sana',
  'Sathu',
  'Savoye LET',
  'SignPainter',
  'Silom',
  'sinhala Sangam MN',
  'Skia',
  'Snell Roundhand',
  'Songti SC',
  'Sukhumvit Set',
  'Tamil Sangam MN',
  'Telugu Sangam MN',
  'Thonburi',
  'Trattatello',
  'Waseem',
  'Zapfino',
  'Charter',
  'DIN Alternate',
  'DIN Condensed',
  'Noto Nastaliq Urdu',
  'Rockwell',
  'Zapf Dingbats',
  'BlinkMacSystemFont',
  'Mishafi',
  'Myanmar MN',
  'Myanmar Sangam MN',
  'Oriya MN',
  'Songti TC',
  'Tamil MN',
  'Telugu MN'
]
```

## OS系统平台

```
['Win32', 'Linux i686', 'Linux armv7l', 'MacIntel']
```

## Cookie格式化示例

```
// cookie字符串为JSON.stringfy后的数组对象，每一项为一个cookie对象
// cookie对象的key必须是驼峰命名的英文字母，重点关注
// expires与expirationDate字段都可表示过期时间，重点关注
[{
    "domain": ".instagram.com", // string
    "expirationDate": 1680060024.48996, // 必须为number，不可为null或者undefined
    "httpOnly": false,
    "name": "ig_nrcb",
    "path": "/",
    "secure": true,
    "session": false,
    "storeId": null, 
    "value": "1", // string
    "sameSite": "Strict" // 为标准的sameSite值或不填此字段，不可为0，null，false等假值
}, {...}]
```

## 插件Plugins

```
// 随机从下方列表中选择几项设置，示例如下：
{
  ...,
  enablePlugins: false, // 是否启用插件指纹
  plugins: '[{"name":"PDF Viewer","description":"Portable Document Format","filename":"internal-pdf-viewer","mimeType":"application/pdf|text/pdf","mimeDescription":"Portable Document Format|Portable Document Format","suffixes":"pdf|pdf"},{"name":"Chrome PDF Viewer","description":"Portable Document Format","filename":"internal-pdf-viewer","mimeType":"application/pdf|text/pdf","mimeDescription":"Portable Document Format|Portable Document Format","suffixes":"pdf|pdf"}]'
}

// 参考列表
[
  {
    name: 'PDF Viewer',
    description: 'Portable Document Format',
    filename: 'internal-pdf-viewer',
    mimeType: 'application/pdf|text/pdf',
    mimeDescription: 'Portable Document Format|Portable Document Format',
    suffixes: 'pdf|pdf'
  },
  {
    name: 'Chrome PDF Viewer',
    description: 'Portable Document Format',
    filename: 'internal-pdf-viewer',
    mimeType: 'application/pdf|text/pdf',
    mimeDescription: 'Portable Document Format|Portable Document Format',
    suffixes: 'pdf|pdf'
  },
  {
    name: 'Chromium PDF Viewer',
    description: 'Portable Document Format',
    filename: 'internal-pdf-viewer',
    mimeType: 'application/pdf|text/pdf',
    mimeDescription: 'Portable Document Format|Portable Document Format',
    suffixes: 'pdf|pdf'
  },
  {
    name: 'Microsoft Edge PDF Viewer',
    description: 'Portable Document Format',
    filename: 'internal-pdf-viewer',
    mimeType: 'application/pdf|text/pdf',
    mimeDescription: 'Portable Document Format|Portable Document Format',
    suffixes: 'pdf|pdf'
  },
  {
    name: 'WebKit built-in PDF',
    description: 'Portable Document Format',
    filename: 'internal-pdf-viewer',
    mimeType: 'application/pdf|text/pdf',
    mimeDescription: 'Portable Document Format|Portable Document Format',
    suffixes: 'pdf|pdf'
  }
]
```

## SSL特性参考

```
// 随机从下方列表中选择几项设置，设置value值逗号分隔，示例如下：
{
  ...,
  disableSslCipherSuitesFlag: false, // ssl是否禁用特性，默认不禁用，注意开启后自定义设置时，有可能会导致某些网站无法访问
  disableSslCipherSuites: '0xC02C,0xC030', // ssl 禁用特性，value值按照逗号分隔
}

// 参考列表
[
  {
    label: 'TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384',
    value: '0xC02C'
  },
  {
    label: 'TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384',
    value: '0xC030'
  },
  {
    label: 'TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256',
    value: '0xC02B'
  },
  {
    label: 'TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256',
    value: '0xC02F'
  },
  {
    label: 'TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256',
    value: '0xCCA9'
  },
  {
    label: 'TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256',
    value: '0xCCA8'
  },
  {
    label: 'TLS_DHE_RSA_WITH_AES_256_GCM_SHA384',
    value: '0x009F'
  },
  {
    label: 'TLS_DHE_RSA_WITH_AES_128_GCM_SHA256',
    value: '0x009E'
  },
  {
    label: 'TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384',
    value: '0xC024'
  },
  {
    label: 'TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384',
    value: '0xC028'
  },
  {
    label: 'TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA',
    value: '0xC00A'
  },
  {
    label: 'TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA',
    value: '0xC014'
  },
  {
    label: 'TLS_DHE_RSA_WITH_AES_256_CBC_SHA256',
    value: '0x006B'
  },
  {
    label: 'TLS_DHE_RSA_WITH_AES_256_CBC_SHA',
    value: '0x0039'
  },
  {
    label: 'TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256',
    value: '0xC023'
  },
  {
    label: 'TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256',
    value: '0xC027'
  },
  {
    label: 'TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA',
    value: '0xC009'
  },
  {
    label: 'TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA',
    value: '0xC013'
  },
  {
    label: 'TLS_DHE_RSA_WITH_AES_128_CBC_SHA256',
    value: '0x0067'
  },
  {
    label: 'TLS_DHE_RSA_WITH_AES_128_CBC_SHA',
    value: '0x0033'
  },
  {
    label: 'TLS_RSA_WITH_AES_256_GCM_SHA384',
    value: '0x009D'
  },
  {
    label: 'TLS_RSA_WITH_AES_128_GCM_SHA256',
    value: '0x009C'
  },
  {
    label: 'TLS_RSA_WITH_AES_256_CBC_SHA256',
    value: '0x003D'
  },
  {
    label: 'TLS_RSA_WITH_AES_128_CBC_SHA256',
    value: '0x003C'
  },
  {
    label: 'TLS_RSA_WITH_AES_256_CBC_SHA',
    value: '0x0035'
  },
  {
    label: 'TLS_RSA_WITH_AES_128_CBC_SHA',
    value: '0x002F'
  },
  {
    label: 'TLS_AES_128_GCM_SHA256',
    value: '0x1301'
  },
  {
    label: 'TLS_AES_256_GCM_SHA384',
    value: '0x1302'
  },
  {
    label: 'TLS_AES_128_CCM_8_SHA256',
    value: '0x1305'
  },
  {
    label: 'TLS_AES_128_CCM_SHA256',
    value: '0x1304'
  }
]
```


### 📱 云手机环境接口

来源：https://doc2.bitbrowser.cn/jiekou/cloud-phone.html

更新时间：2026-04-13T12:14:07+08:00

## 接口说明

1. 云手机接口并发限制不同于浏览器窗口，云手机接口并发限制为：**每秒 1 次请求**
2. 云手机 API 涉及到计费的接口较多，特别是开机、挂起、关机等，请一定严格了解清楚后再进行操作
3. 操作云手机本身的相关接口，如开机、关机、一键新机、安装、卸载 App 等，接口为异步接口，无法立即返回云手机状态，调用完 API 后，需要调用检查开机状态接口，判断云手机是否开机
4. 环境使用临时算力时，开机后立即开始计费，关机才会停止计费，请务必了解

## 创建云手机环境

**POST** `/phone/profile/add`

```json
// Body 请求参数示例
{
  "envType": 1,
  "computeType": 0,
  "name": "",
  "remark": "",
  "port": 45001,
  "proxyUserName": "xge66fa14336ca14",
  "proxyPassword": "dMpfTa0tbr512cmi4P",
  "host": "1.2.3.4",
  "regionId": "d69bcf13db844d1cb81e779f64161223",
  "cloudPhonesFingerPrint": {},
  "ipCheckService": "DB-IP",
  "udp": 1
}
```

* 参数详情

| 名称                     | 类型     | 必须 | 默认值     | 描述                                                     |
| ---------------------- | ------ | -- | ------- | ------------------------------------------------------ |
| regionId               | string | 是  | -       | 算力类型 ID，到客户端界面中点击"创建环境"按钮，顶部算力类型，选中类型后，点击右边的复制按钮，复制 ID |
| envType                | int    | 是  | 1       | 0为套餐模式，1为临时计费模式，勿填错                                    |
| computeType            | int    | 是  | 0       | 算力类型，可选值: 0, 1, 2; (0: 智能分配算力, 1: 临时算力, 2: 包月算力)       |
| name                   | string | 否  | -       | 环境名称，最多 30 个字符                                         |
| groupId                | string | 否  | -       | 分组 ID，创建环境时，指定分组                                       |
| remark                 | string | 否  | -       | 环境备注，最多 500 个字符                                        |
| host                   | string | 是  | -       | 代理主机，如: 1.2.3.4                                        |
| port                   | int    | 是  | -       | 代理端口，如: 123                                            |
| proxyUserName          | string | 是  | -       | 代理账号，最多 200 个字符                                        |
| proxyPassword          | string | 是  | -       | 代理密码，最多 200 个字符                                        |
| ipCheckService         | string | 是  | DB-IP | 代理 IP 查询渠道 ，可选值：DB-IP, ip-api                        |
| udp                    | int    | 是  | 1       | 代理 UDP 协议，可选值: 1, 0; 无法连接网络时，可以尝试关闭 UDP，不建议关闭          |
| cloudPhonesFingerPrint | object | 是  | `{}`    | 指纹配置对象，默认不用配置，除非有特殊需求，如果需要控制，参考下方 json 对象              |

* cloudPhonesFingerPrint 指纹对象

```json
// Body 请求参数示例
{
  "isIpCreatePosition": true, // 基于IP生成对应的地理位置，不开启可自定义
  "isIpCreateLanguage": true, // 基于IP生成对应国家的手机系统语言
  "isIpCreateTimeZone": true, // 基于IP生成对应的时区
  "timeZone": "", // 当 isIpCreateTimeZone 为false时，自定义设置时区
  "timeZoneOffset": 0, // 当 isIpCreateTimeZone 为false 时，自定义设置时区偏移量
  "localeLanguage": "", // 当 isIpCreateLanguage 为false时自定义设置语言
  "longitude": "", // 当 isIpCreatePosition 为false时自定义设置经度
  "latitude": "" // 当 isIpCreatePosition 为false时自定义设置纬度
}
```

## 修改云手机环境，算力类型 regionId 不可修改

**POST** `/phone/profile/update`

```json
// Body 请求参数示例
{
  "id": "2c9cce4493a98fa90193aa7d71e70043",
  "name": "我修改了"
}
```

* 参数详情

除了 ID 必须传外，只需传入需要修改的字段即可，比如需要修改 name，那么接口只需要传入 ID 与 name 字段即可，完整请求参数与上方 `/phone/profile/add` 接口一致

## 获取云手机环境列表

POST `/phone/list`

```json
// Body 请求参数示例
{
     "page": 0,
     "pageSize": 10
}

// 返回的列表中，如果环境开启了adb，则列表中会返回adb连接信息，如下：
// "{\"code\":\"SAjp7zSIJfHGAZi9Ot\",\"port\":21136,\"ip\":\"124.236.71.153\"}"
// 请自行格式化使用，code是adb连接密码
```

* 参数详情

| 名称       | 类型      | 必选 | 说明                                       |
| -------- | ------- | -- | ---------------------------------------- |
| page     | number  | 是  | 分页， 从 0 开始                               |
| pageSize | number  | 是  | 每页条数，最大 100 条，传入大于 100 的数字，也最多返回 100 条数据 |
| groupId  | string  | 否  | 分组 ID                                    |
| name     | string  | 否  | 环境名称，模糊匹配                                |
| seq      | number  | 否  | 序号，精确匹配                                  |
| minSeq   | number  | 否  | 最小序号，查询大于此序号的数据                          |
| maxSeq   | integer | 否  | 最大序号，查询小于此序号的数据                          |
| host     | string  | 否  | 代理 Host ，精确匹配                            |
|envType|integer|否|计费类型，计费类型，默认1，按时计费模式的环境，0则查询套餐模式的环境|

## 获取云手机配置详情

**POST** `/phone/detail`

```json
// Body 请求参数示例
{
  "id": "2c9cce44930a5b0e01930a7c95b0000a"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明       |
| -- | ------ | -- | -------- |
| id | string | 是  | 云手机环境 ID |

## 删除云手机环境，支持批量，未关闭，打开中，关闭中均不可删除

**POST** `/phone/delete`

```json
// Body 请求参数示例
{
  "ids": ["2c9cce44930a5b0e01930a7c95b0000a"]
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明       |
| -- | ------ | -- | -------- |
| ids | array | 是  | 云手机环境 ID集合 |

## 打开云手机

打开云手机为异步接口，如果之前云手机是关机状态，那么大概需要 1 分钟左右开机，如果是打开挂起的云手机，那么最快 5 秒钟即可打开；在此期间可以调用 `/phone/running` 判断是否已开机

**POST** `/phone/openphone`

```json
// Body 请求参数示例
{
  "id": "2c9cce44930a5b0e01930a7c95b0000a"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明       |
| -- | ------ | -- | -------- |
| id | string | 是  | 云手机环境 ID |

## 挂起云手机

**POST** `/phone/suspend`

关闭手机窗口，并挂起手机，手机仍然在云端持续运行，仍然持续计费

```json
// Body 请求参数示例
{
  "id": "2c9cce4493a98fa90193aa7d71e70043"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明       |
| -- | ------ | -- | -------- |
| id | string | 是  | 云手机环境 ID |

## 关机云手机

**POST** `/phone/shutdown`

关闭云手机窗口，并关机云手机，手机不再云端继续运行，停止计费

```json
// Body 请求参数示例
{
  "id": "2c9cce4493a98fa90193aa7d71e70043"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明       |
| -- | ------ | -- | -------- |
| id | string | 是  | 云手机环境 ID |

## 获取云手机开机状态

**POST**: `/phone/running`

判断云手机是否已开机

```json
// Body 请求参数示例
{
  "id": "2c9cce4493a98fa90193aa7d71e70043"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明       |
| -- | ------ | -- | -------- |
| id | string | 是  | 云手机环境 ID |

```json
// 请求返回示例
{
  "success": true,
  "data": true // data 返回 true 表示已开机，返回 false 表示未开机
}
```

## 开启/关闭云手机 Root 权限

**POST** `/phone/root/switch`

切换 Root 权限，开启或者关闭 Root 权限是异步接口，且无法获取 Root 是否已开启/关闭成功，用户需要自己通过第三方工具监听；开启/关闭 Root 权限只能在云手机开机状态下操作

```json
// Body 请求参数示例
{
  "id": "2c9cce4493a98fa90193aa7d71e70043",
  "enable": true
}
```

* 参数详情

| 名称     | 类型      | 必选 | 说明                                  |
| ------ | ------- | -- | ----------------------------------- |
| id     | string  | 是  | 云手机环境 ID                            |
| enable | boolean | 是  | true: 开启 Root 权限, false: 关闭 Root 权限 |

## 上传文件

**POST** `/phone/uploadFile`

上传文件时，如果多个环境上传同一个文件，那么最好一次性传入需要上传文件的环境 id 集合，服务端将仅上传一次本地文件，然后下发到指定环境中

上传文件必须等待上一次上传完成，才能继续调用上传接口

```json
// Body 请求参数示例
{
  "path": "C:\\Users\\admin\\AppData\\Roaming\\logs\\aaa.log",
  "ids": ["2c9cce4493a98fa90193aa7d71e70043"]
}
```

* 参数详情

| 名称   | 类型     | 必选 | 说明                                           |
| ---- | ------ | -- | -------------------------------------------- |
| path | string | 是  | 文件绝对路径，如： "C:\Users\admin\xxx\aa.txt" 类似这种路径 |
| ids  | array  | 是  | 云手机环境 ID 集合                                  |

## 删除云手机正在上传的文件

云手机上传文件时，如果遇到上传异常失败，导致后续无法上传的情况下，可以调用下这个接口，清理掉正在上传的文件及缓存，可以重新上传

**POST** `/phone/clearUploadCache`
```json
// Body 请求参数示例
{
  "path": "C:\\Users\\admin\\AppData\\Roaming\\logs\\aaa.log"
}
```

* 参数详情

| 名称   | 类型     | 必选 | 说明                                           |
| ---- | ------ | -- | -------------------------------------------- |
| path | string | 是  | 文件绝对路径，如： "C:\Users\admin\xxx\aa.txt" 类似这种路径 |

## 清空 APP 数据

**POST** `/phone/clearAppData`

清空 app 的缓存数据，用户登录状态将被清理，请谨慎操作。

```json
// Body 请求参数示例
{
  "id": "2c9cce4493a98fa90193aa7d71e70043",
  "packageName": "com.google.android.googlequicksearchbox"
}
```

* 参数详情

| 名称          | 类型     | 必选 | 说明                 |
| ----------- | ------ | -- | ------------------ |
| id          | string | 是  | 环境 ID              |
| packageName | string | 是  | app 的 package name |

## 一键新机

**POST** `/phone/profile/recover`

重置云手机环境参数，每个环境的最大新机次数为：每个月最多 60 次，用完后下个月重置，请谨慎操作。

一次新机大概需要5分钟，新机没完成时，再次调用新机接口，会报错。

```json
// Body 请求参数示例
{
  "id": "2c9cce44930a5b0e01930a7c95b0000a"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明       |
| -- | ------ | -- | -------- |
| id | string | 是  | 云手机环境 ID |

## 安装应用

**POST** `/phone/installApp`

安装应用市场中的 app，到应用市场中复制对应版本的应用 ID，调用此接口即可自动安装对应 app

```json
// Body 请求参数示例
{
  "id": "2c9cce44930a5b0e01930a7c95b0000a",
  "appId": "1855178384240701441"
}
```

* 参数详情

| 名称    | 类型     | 必选 | 说明                         |
| ----- | ------ | -- | -------------------------- |
| id    | string | 是  | 云手机环境 ID                   |
| appId | string | 是  | 应用 ID，到应用市场中，指定 app 的版本后复制 |

## 卸载应用

**POST** `/phone/unInstallApp`

根据 app 的 packageName 卸载 app，卸载 app 时将一同清理 app 的缓存数据

```json
// Body 请求参数示例
{
  "id": "2c9cce44930a5b0e01930a7c95b0000a",
  "packageName": "com.xxx.xxx"
}
```

* 参数详情

| 名称          | 类型     | 必选 | 说明                |
| ----------- | ------ | -- | ----------------- |
| id          | string | 是  | 云手机环境 ID          |
| packageName | string | 是  | app 的 packageName |

## 打开应用

**POST** `/phone/launchApp`

根据 app 的 packageName 打开 app

```json
// Body 请求参数示例
{
  "id": "2c9cce44930a5b0e01930a7c95b0000a",
  "packageName": "com.xxx.xxx"
}
```

* 参数详情

| 名称          | 类型     | 必选 | 说明                 |
| ----------- | ------ | -- | ------------------ |
| id          | string | 是  | 云手机环境 ID           |
| packageName | string | 是  | app 的 package name |

## 关闭应用

**POST** `/phone/closeApp`

关闭 app，app 将在后台继续运行

```json
// Body 请求参数示例
{
  "packageName": "com.xxx.xxx",
  "id": "2c9cce44930a5b0e01930a7c95b0000a"
}
```

* 参数详情

| 名称          | 类型     | 必选 | 说明                |
| ----------- | ------ | -- | ----------------- |
| packageName | string | 是  | app 的 packageName |
| id          | string | 是  | 云手机环境 ID          |

## 重启云手机

**POST** `/phone/restart`

重启云手机，重启过程中不可使用任何云手机功能

```json
// Body 请求参数示例
{
  "id": "2c9cce44930a5b0e01930a7c95b0000a"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明       |
| -- | ------ | -- | -------- |
| id | string | 是  | 云手机环境 ID |

## 获取云手机上已经安装的 APP 列表

**POST** `/phone/getInstalledAppList`

获取手机上已经安装的 app 列表，只能获取用户安装的 app 列表，而无法获取系统自带的 app

```json
// Body 请求参数示例
{
  "id": "string"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明       |
| -- | ------ | -- | -------- |
| id | string | 是  | 云手机环境 ID |

## 获取云手机分组列表

**POST** `/phone/group/list`

```json
// Body 请求参数示例
{
  "page": 0,
  "pageSize": 4,
  "all": true
}
```

* 参数详情

| 名称       | 类型      | 必选 | 说明                                           |
| -------- | ------- | -- | -------------------------------------------- |
| page     | number  | 是  | 分页，从 0 开始                                    |
| pageSize | number  | 是  | 分页条数，最多 100 条，默认 10                          |
| all      | boolean | 否  | 默认只能获取当前用户自身的分组，如果需要获取权限范围内所有用户的分组，则需要传 true |

## 添加分组

**POST** `/phone/group/add`

```json
// Body 请求参数示例
{
  "groupName": "111",
  "sortNum": 3
}
```

* 参数详情

| 名称        | 类型     | 必选 | 说明   |
| --------- | ------ | -- | ---- |
| groupName | string | 是  | 分组名称 |
| sortNum   | number | 是  | 排序   |

## 修改分组

**POST** `/phone/group/edit`

```json
// Body 请求参数示例
{
  "id": "2c9cce4492b877120192b87811650009",
  "groupName": "11",
  "sortNum": 3
}
```

* 参数详情

| 名称        | 类型     | 必选 | 说明    |
| --------- | ------ | -- | ----- |
| id        | string | 是  | 分组 ID |
| groupName | string | 是  | 分组名称  |
| sortNum   | number | 是  | 分组排序  |

## 删除分组

**POST** `/phone/group/delete`

```json
// Body 请求参数示例
{
  "id": "2c9cce44932f0e8401932f388bdd0015"
}
```

* 参数详情

| 名称 | 类型     | 必选 | 说明    |
| -- | ------ | -- | ----- |
| id | string | 是  | 分组 ID |

## 批量修改备注

**POST** `/phone/batchUpdate/remark`

批量修改备注，支持替换或追加模式修改备注，默认替换模式，选择追加模式时，传入的数据，将被追加到环境原本的备注后面

```json
// Body 请求参数示例
{
  "phoneIds": ["2c9cce44931abdda01931e5354ba0168", "2c9cce44930a5b0e01930a7c95b0000a"],
  "remark": "12"
}
```

* 参数详情

| 名称         | 类型      | 必选 | 说明                             |
| ---------- | ------- | -- | ------------------------------ |
| phoneIds   | array   | 是  | 云手机环境 ID 列表                    |
| remark     | string  | 是  | 备注                             |
| remarkType | integer | 是  | 可选值：1, 2; 1: 替换模式，2: 追加模式，默认 1 |

## 批量修改分组

**POST** `/phone/batchUpdate/group`

批量指定环境分组为同一个分组

```json
// Body 请求参数示例
{
  "groupId": "2c9cce4492b877120192b87811650009",
  "phoneIds": ["2c9cce4492be13790192be16f965000a", "2c9cce4493007978019300809a9d0007", "2c9cce44931abdda01931e5354ba0168"]
}
```

* 参数详情

| 名称       | 类型     | 必选 | 说明          |
| -------- | ------ | -- | ----------- |
| groupId  | string | 是  | 分组 id       |
| phoneIds | array  | 是  | 云手机环境 ID 集合 |

## 批量修改代理

**POST** `/phone/batchUpdate/proxy`

批量修改环境的代理信息为相同指定代理信息

```json
// Body 请求参数示例
{
  "phoneIds": ["2c9cce4492be13790192be16f965000a", "2c9cce4493007978019300809a9d0007", "2c9cce44931abdda01931e5354ba0168"],
  "host": "1.2.3.4",
  "port": "59059",
  "proxyUserName": "ImL5bo00",
  "proxyPassword": "JYZG7Eqj"
}
```

* 参数详情

| 名称            | 类型     | 必选 | 说明       |
| ------------- | ------ | -- | -------- |
| phoneIds      | array  | 是  | 云手机环境 ID 数组 |
| host          | string | 是  | 代理主机     |
| port          | int    | 是  | 代理端口     |
| proxyUserName | string | 是  | 代理账号     |
| proxyPassword | string | 是  | 代理密码     |

## 获取adb信息，云手机必须是开机状态才有adb信息

**POST** `/phone/adb/get`

获取adb连接信息

```json
// Body 请求参数示例
{
  "id": "2c9cce4492be13790192be16f965000a"
}
```

* 参数详情

| 名称            | 类型     | 必选 | 说明       |
| ------------- | ------ | -- | -------- |
| id      | string  | 是  | 云手机环境 ID |

## 开启云手机adb，云手机必须是开机状态才能设置adb

**POST** `/phone/adb/enable`

开启云手机adb

```json
// Body 请求参数示例
{
  "ids": ["2c9cce4492be13790192be16f965000a"]
}
```

* 参数详情

| 名称            | 类型     | 必选 | 说明       |
| ------------- | ------ | -- | -------- |
| ids      | arrary  | 是  | 云手机环境 ID 数组 |

## 关闭云手机adb，云手机必须是开机状态才能设置adb

**POST** `/phone/adb/disable`

关闭云手机adb

```json
// Body 请求参数示例
{
  "ids": ["2c9cce4492be13790192be16f965000a"]
}
```

* 参数详情

| 名称            | 类型     | 必选 | 说明       |
| ------------- | ------ | -- | -------- |
| ids      | arrary  | 是  | 云手机环境 ID 数组 |


### 云手机环境标签

来源：https://doc2.bitbrowser.cn/cloudPhoneTags.html

更新时间：2026-04-09T11:00:32+08:00

> 注意：当前标签总量与界面中一致，最多可创建20个标签
> 单个云手机环境最多允许绑定3个标签

## 获取云手机标签列表，无参数

**POST**: `/cloudPhoneTag/list`

```json
// 返回数据示例
{
    "success": true,
    "data": [
        {
            "id": "402880669d09d5aa019d09e3060e001c",
            "tagName": "API Tag name1",
            "tagColor": "#ECC2F3"
        },
        {
            "id": "402880669d09d5aa019d09de365a001b",
            "tagName": "API 创建的",
            "tagColor": "#ECC2F3"
        },
        {
            "id": "402880669d09d5aa019d09dd90a6001a",
            "tagName": "545453",
            "tagColor": "#FFF18C"
        }
    ]
}
```
## 创建云手机标签
**POST**: `/cloudPhoneTag/create`
```json
// Body 请求参数示例
{
    "tagName": "标签名",
    "tagColor": "#ECC2F3"
}
```

* 参数详情

| 名称       | 类型      | 必选 | 说明                                       |
| -------- | ------- | -- | ---------------------------------------- |
| tagName     | string  | 是  | 标签名，最长不超过50个字符    |
| tagColor | string  | 是  | 只接受 #aabbcc 格式的颜色值 |

## 修改云手机标签
**POST**: `/cloudPhoneTag/update`
```json
// Body 请求参数示例
{
    "id": "402880669cb319ac019cb710cd9b01ec",
    "tagName": "标签名",
    "tagColor": "#ECC2F3"
}
```

* 参数详情

| 名称       | 类型      | 必选 | 说明                                       |
| -------- | ------- | -- | ---------------------------------------- |
| id     | string  | 是  | 标签id，32位  |
| tagName     | string  | 是  | 标签名，最长不超过50个字符    |
| tagColor | string  | 是  | 只接受 #aabbcc 格式的颜色值 |

## 删除云手机标签，支持批量
**POST**: `/cloudPhoneTag/delete`
```json
// Body 请求参数示例
{
    "ids": ["402880669cb319ac019cb710cd9b01ec"]
}
```

* 参数详情

| 名称       | 类型      | 必选 | 说明                                       |
| -------- | ------- | -- | ---------------------------------------- |
| ids     | array  | 是  | 标签id集合，数组格式  |

## 更新云手机标签绑定关系
**POST**: `/cloudPhoneTag/updateRelation`
```json
// Body 请求参数示例
{
    "cloudPhoneId": "043239f6d2844f8d8407ac8a1972daec",
    "addTagIds": ["402880669d09d5aa019d09dd90a6001a"],
    "removeTagIds": ["402880669d09d5aa019d09de365a001b"]
}
```

* 参数详情

| 名称       | 类型      | 必选 | 说明                                       |
| -------- | ------- | -- | ---------------------------------------- |
| cloudPhoneId     | string  | 是  | 云手机环境 ID，32位  |
| addTagIds     | array  | 是  | 数组格式，要绑定的标签ID集合  |
| removeTagIds     | array  | 是  | 数组格式，要解除绑定关系的标签ID集合  |
