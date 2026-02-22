# 情侣互动小程序 API 接口文档

## 1. 文档概览

本文档描述了情侣互动小程序的后端API接口，包含用户认证、情侣关系、消息、动态、爱情币等多个模块的接口定义。

### 1.1 接口基础信息

- **API根路径**：`/api`
- **请求方法**：GET、POST、PUT、DELETE
- **响应格式**：JSON
- **认证方式**：Token认证（通过请求头Authorization传递）

### 1.2 响应数据结构

```json
{
  "code": "0000", // 错误码，0000表示成功
  "data": {}, // 响应数据
  "message": "" // 错误信息，成功时为空
}
```

## 2. API模块列表

| 模块名称  | 模块路径        | 功能描述               |
| ----- | ----------- | ------------------ |
| 认证模块  | auth        | 用户登录、注册、获取用户信息等    |
| 情侣模块  | couple      | 情侣关系管理、纪念日管理、记忆管理等 |
| 消息模块  | message     | 聊天消息、秘密消息、定时消息管理等  |
| 动态模块  | dynamic     | 动态发布、点赞、评论等        |
| 爱情币模块 | coins       | 爱情币余额、任务管理、奖励兑换等   |
| 用户模块  | user        | 用户信息管理、设置管理、收藏管理等  |
| 纪念日模块 | anniversary | 纪念日管理              |
| 壁纸模块  | wallpaper   | 壁纸获取、下载、分类等        |

## 3. 认证模块（user）

/api/user

### 3.1 登录

- **接口路径**：`/login`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述  |
  | -------- | ------ | --- | --- |
  | email | string | 是   | 邮箱 |
  | password | string | 是   | 密码  |

- **响应数据**：
  
  ```json
  {
    "code": "0000",
    "data": {
      "token": "",
      "userInfo": {}
    },
    "message": ""
  }
  ```

### 3.2 注册

- **接口路径**：`/register`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名             | 类型     | 必填  | 描述   |
  | --------------- | ------ | --- | ---- |
  | email | string | 是   | 邮箱  |
  | password        | string | 是   | 密码   |
  | confirmPassword | string | 是   | 确认密码 |

- **响应数据**：
  
  ```json
  {
    "code": "0000",
    "data": {
      "token": "",
      "userInfo": {}
    },
    "message": ""
  }
  ```

### 3.3 验证码登录

- **接口路径**：`/login/captcha`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述  |
  | -------- | ------ | --- | --- |
  | email | string | 是   | 邮箱 |
  | captcha | string | 是   | 验证码  |

- **响应数据**：
  
  ```json
  {
    "code": "0000",
    "data": {
      "token": "",
      "userInfo": {}
    },
    "message": ""
  }
  ```

### 3.4 获取用户信息

- **接口路径**：`/info/{id}`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述  |
  | -------- | ------ | --- | --- |
  | id | long | 是   | 用户ID |

- **响应数据**：
  
  ```json
  {
    "code": "0000",
    "data": {
      "id": 1,
      "email": "user@example.com",
      "nickname": "用户昵称",
      "gender": 1,
      "hasCouple": 0
    },
    "message": ""
  }
  ```

### 3.5 获取验证码

- **接口路径**：`/captcha`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述  |
  | -------- | ------ | --- | --- |
  | email | string | 是   | 邮箱 |
  | type | string | 是   | 验证码类型：register（注册）、forget（忘记密码）、login（登录） |

- **响应数据**：
  
  ```json
  {
    "code": "0000",
    "data": "captchaId",
    "message": ""
  }
  ```

### 3.6 忘记密码

- **接口路径**：`/forget`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名             | 类型     | 必填  | 描述    |
  | --------------- | ------ | --- | ----- |
  | email | string | 是   | 邮箱 |
  | captcha | string | 是   | 验证码 |
  | password | string | 是   | 新密码 |

- **响应数据**：
  
  ```json
  {
    "code": "0000",
    "data": null,
    "message": ""
  }
  ```

### 3.7 修改密码

- **接口路径**：`/update`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名             | 类型     | 必填  | 描述    |
  | --------------- | ------ | --- | ----- |
  | oldPassword     | string | 是   | 旧密码   |
  | newPassword     | string | 是   | 新密码   |
  | confirmNewPassword | string | 是   | 确认新密码 |

- **响应数据**：
  
  ```json
  {
    "code": "0000",
    "data": null,
    "message": ""
  }
  ```

## 4. 情侣模块（couple）

/api/couple

### 4.1 绑定情侣关系

- **接口路径**：`/bind`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名           | 类型     | 必填  | 描述     |
  | ------------- | ------ | --- | ------ |
  | coupleCode    | string | 是   | 情侣码    |
  | loveStartDate | string | 是   | 恋爱开始日期 |

- **响应数据**：
  
  ```json
  {
    "coupleInfo": {}
  }
  ```

### 4.2 获取情侣信息

- **接口路径**：`/info`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "coupleInfo": {}
  }
  ```

### 4.3 更新情侣信息

- **接口路径**：/update`

- **请求方法**：PUT

- **请求参数**：
  
  | 参数名           | 类型     | 必填  | 描述     |
  | ------------- | ------ | --- | ------ |
  | loveStartDate | string | 否   | 恋爱开始日期 |
  | coupleName    | string | 否   | 情侣名称   |

- **响应数据**：
  
  ```json
  {
    "coupleInfo": {}
  }
  ```

### 4.4 解除情侣关系

- **接口路径**：`/unbind`

- **请求方法**：POST

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 4.5 获取情侣纪念日

- **接口路径**：`/anniversaries`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "anniversaries": []
  }
  ```

### 4.6 创建情侣纪念日

- **接口路径**：`/api/couple/anniversary/create`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名         | 类型     | 必填  | 描述    |
  | ----------- | ------ | --- | ----- |
  | title       | string | 是   | 纪念日标题 |
  | date        | string | 是   | 纪念日日期 |
  | description | string | 否   | 纪念日描述 |

- **响应数据**：
  
  ```json
  {
    "anniversary": {}
  }
  ```

### 4.7 更新情侣纪念日

- **接口路径**：`/api/couple/anniversary/{id}`

- **请求方法**：PUT

- **请求参数**：
  
  | 参数名         | 类型     | 必填  | 描述    |
  | ----------- | ------ | --- | ----- |
  | title       | string | 否   | 纪念日标题 |
  | date        | string | 否   | 纪念日日期 |
  | description | string | 否   | 纪念日描述 |

- **响应数据**：
  
  ```json
  {
    "anniversary": {}
  }
  ```

### 4.8 删除情侣纪念日

- **接口路径**：`/api/couple/anniversary/{id}`

- **请求方法**：DELETE

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 4.9 获取情侣记忆

- **接口路径**：`/api/couple/memories`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述   |
  | -------- | ------ | --- | ---- |
  | page     | number | 否   | 页码   |
  | pageSize | number | 否   | 每页数量 |

- **响应数据**：
  
  ```json
  {
    "memories": [],
    "total": 0
  }
  ```

### 4.10 创建情侣记忆

- **接口路径**：`/api/couple/memory/create`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名     | 类型     | 必填  | 描述   |
  | ------- | ------ | --- | ---- |
  | title   | string | 是   | 记忆标题 |
  | date    | string | 是   | 记忆日期 |
  | content | string | 是   | 记忆内容 |
  | images  | array  | 否   | 图片列表 |

- **响应数据**：
  
  ```json
  {
    "memory": {}
  }
  ```

### 4.11 更新情侣记忆

- **接口路径**：`/api/couple/memory/{id}`

- **请求方法**：PUT

- **请求参数**：
  
  | 参数名     | 类型     | 必填  | 描述   |
  | ------- | ------ | --- | ---- |
  | title   | string | 否   | 记忆标题 |
  | date    | string | 否   | 记忆日期 |
  | content | string | 否   | 记忆内容 |
  | images  | array  | 否   | 图片列表 |

- **响应数据**：
  
  ```json
  {
    "memory": {}
  }
  ```

### 4.12 删除情侣记忆

- **接口路径**：`/api/couple/memory/{id}`

- **请求方法**：DELETE

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

## 5. 消息模块（message）

### 5.1 获取聊天消息列表

- **接口路径**：`/api/message/list`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述   |
  | -------- | ------ | --- | ---- |
  | page     | number | 否   | 页码   |
  | pageSize | number | 否   | 每页数量 |

- **响应数据**：
  
  ```json
  {
    "messages": [],
    "total": 0
  }
  ```

### 5.2 发送消息

- **接口路径**：`/api/message/send`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名     | 类型     | 必填  | 描述   |
  | ------- | ------ | --- | ---- |
  | content | string | 是   | 消息内容 |
  | type    | string | 否   | 消息类型 |

- **响应数据**：
  
  ```json
  {
    "message": {}
  }
  ```

### 5.3 标记消息已读

- **接口路径**：`/api/message/read/{messageId}`

- **请求方法**：POST

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 5.4 删除消息

- **接口路径**：`/api/message/{messageId}`

- **请求方法**：DELETE

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 5.5 获取消息未读数

- **接口路径**：`/api/message/unread`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "unreadCount": 0
  }
  ```

### 5.6 创建秘密消息

- **接口路径**：`/api/message/special/create`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述   |
  | -------- | ------ | --- | ---- |
  | title    | string | 是   | 消息标题 |
  | content  | string | 是   | 消息内容 |
  | password | string | 是   | 解锁密码 |

- **响应数据**：
  
  ```json
  {
    "message": {}
  }
  ```

### 5.7 解锁秘密消息

- **接口路径**：`/api/message/special/unlock`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名       | 类型     | 必填  | 描述   |
  | --------- | ------ | --- | ---- |
  | messageId | string | 是   | 消息ID |
  | password  | string | 是   | 解锁密码 |

- **响应数据**：
  
  ```json
  {
    "message": {}
  }
  ```

### 5.8 获取秘密消息列表

- **接口路径**：`/api/message/special/list`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "messages": []
  }
  ```

### 5.9 删除秘密消息

- **接口路径**：`/api/message/special/{messageId}`

- **请求方法**：DELETE

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 5.10 创建定时消息

- **接口路径**：`/api/message/timed/create`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名        | 类型     | 必填  | 描述   |
  | ---------- | ------ | --- | ---- |
  | title      | string | 是   | 消息标题 |
  | content    | string | 是   | 消息内容 |
  | unlockDate | string | 是   | 解锁日期 |

- **响应数据**：
  
  ```json
  {
    "message": {}
  }
  ```

### 5.11 获取定时消息列表

- **接口路径**：`/api/message/timed/list`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "messages": []
  }
  ```

### 5.12 删除定时消息

- **接口路径**：`/api/message/timed/{messageId}`

- **请求方法**：DELETE

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

## 6. 动态模块（dynamic）

### 6.1 获取动态列表

- **接口路径**：`/api/dynamic/list`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述   |
  | -------- | ------ | --- | ---- |
  | page     | number | 否   | 页码   |
  | pageSize | number | 否   | 每页数量 |

- **响应数据**：
  
  ```json
  {
    "dynamics": [],
    "total": 0
  }
  ```

### 6.2 发布动态

- **接口路径**：`/api/dynamic/create`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述   |
  | -------- | ------ | --- | ---- |
  | content  | string | 是   | 动态内容 |
  | images   | array  | 否   | 图片列表 |
  | location | string | 否   | 位置信息 |

- **响应数据**：
  
  ```json
  {
    "dynamic": {}
  }
  ```

### 6.3 获取动态详情

- **接口路径**：`/api/dynamic/{dynamicId}`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "dynamic": {}
  }
  ```

### 6.4 删除动态

- **接口路径**：`/api/dynamic/{dynamicId}`

- **请求方法**：DELETE

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 6.5 点赞动态

- **接口路径**：`/api/dynamic/like`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名       | 类型     | 必填  | 描述   |
  | --------- | ------ | --- | ---- |
  | dynamicId | string | 是   | 动态ID |

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 6.6 取消点赞

- **接口路径**：`/api/dynamic/unlike`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名       | 类型     | 必填  | 描述   |
  | --------- | ------ | --- | ---- |
  | dynamicId | string | 是   | 动态ID |

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 6.7 获取点赞列表

- **接口路径**：`/api/dynamic/{dynamicId}/likes`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述   |
  | -------- | ------ | --- | ---- |
  | page     | number | 否   | 页码   |
  | pageSize | number | 否   | 每页数量 |

- **响应数据**：
  
  ```json
  {
    "likes": [],
    "total": 0
  }
  ```

### 6.8 评论动态

- **接口路径**：`/api/dynamic/comment`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名       | 类型     | 必填  | 描述   |
  | --------- | ------ | --- | ---- |
  | dynamicId | string | 是   | 动态ID |
  | content   | string | 是   | 评论内容 |

- **响应数据**：
  
  ```json
  {
    "comment": {}
  }
  ```

### 6.9 获取动态评论

- **接口路径**：`/api/dynamic/{dynamicId}/comments`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述   |
  | -------- | ------ | --- | ---- |
  | page     | number | 否   | 页码   |
  | pageSize | number | 否   | 每页数量 |

- **响应数据**：
  
  ```json
  {
    "comments": [],
    "total": 0
  }
  ```

### 6.10 删除评论

- **接口路径**：`/api/dynamic/comment/{commentId}`

- **请求方法**：DELETE

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 6.11 回复评论

- **接口路径**：`/api/dynamic/comment/reply`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名       | 类型     | 必填  | 描述   |
  | --------- | ------ | --- | ---- |
  | commentId | string | 是   | 评论ID |
  | content   | string | 是   | 回复内容 |

- **响应数据**：
  
  ```json
  {
    "comment": {}
  }
  ```

## 7. 爱情币模块（coins）

### 7.1 获取爱情币余额

- **接口路径**：`/api/coins/balance`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "balance": 0
  }
  ```

### 7.2 发布任务

- **接口路径**：`/api/coins/task/create`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名         | 类型     | 必填  | 描述   |
  | ----------- | ------ | --- | ---- |
  | title       | string | 是   | 任务标题 |
  | description | string | 是   | 任务描述 |
  | reward      | number | 是   | 奖励数量 |

- **响应数据**：
  
  ```json
  {
    "task": {}
  }
  ```

### 7.3 获取任务列表

- **接口路径**：`/api/coins/task/list`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述   |
  | -------- | ------ | --- | ---- |
  | page     | number | 否   | 页码   |
  | pageSize | number | 否   | 每页数量 |
  | status   | string | 否   | 任务状态 |

- **响应数据**：
  
  ```json
  {
    "tasks": [],
    "total": 0
  }
  ```

### 7.4 获取任务详情

- **接口路径**：`/api/coins/task/{taskId}`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "task": {}
  }
  ```

### 7.5 完成任务

- **接口路径**：`/api/coins/task/complete`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名    | 类型     | 必填  | 描述   |
  | ------ | ------ | --- | ---- |
  | taskId | string | 是   | 任务ID |

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 7.6 审核任务

- **接口路径**：`/api/coins/task/approve`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名      | 类型      | 必填  | 描述   |
  | -------- | ------- | --- | ---- |
  | taskId   | string  | 是   | 任务ID |
  | approved | boolean | 是   | 是否通过 |

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 7.7 取消任务

- **接口路径**：`/api/coins/task/cancel`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名    | 类型     | 必填  | 描述   |
  | ------ | ------ | --- | ---- |
  | taskId | string | 是   | 任务ID |

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 7.8 获取奖励列表

- **接口路径**：`/api/coins/reward/list`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述   |
  | -------- | ------ | --- | ---- |
  | page     | number | 否   | 页码   |
  | pageSize | number | 否   | 每页数量 |

- **响应数据**：
  
  ```json
  {
    "rewards": [],
    "total": 0
  }
  ```

### 7.9 获取奖励详情

- **接口路径**：`/api/coins/reward/{rewardId}`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "reward": {}
  }
  ```

### 7.10 兑换奖励

- **接口路径**：`/api/coins/reward/exchange`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述   |
  | -------- | ------ | --- | ---- |
  | rewardId | string | 是   | 奖励ID |

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 7.11 获取交易记录

- **接口路径**：`/api/coins/history`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述   |
  | -------- | ------ | --- | ---- |
  | page     | number | 否   | 页码   |
  | pageSize | number | 否   | 每页数量 |

- **响应数据**：
  
  ```json
  {
    "transactions": [],
    "total": 0
  }
  ```

### 7.12 获取任务统计

- **接口路径**：`/api/coins/task/stats`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "totalTasks": 0,
    "completedTasks": 0,
    "pendingTasks": 0
  }
  ```

### 7.13 获取奖励统计

- **接口路径**：`/api/coins/reward/stats`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "totalRewards": 0,
    "exchangedRewards": 0
  }
  ```

## 8. 用户模块（user）

### 8.1 获取个人信息

- **接口路径**：`/api/user/profile`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "userInfo": {}
  }
  ```

### 8.2 更新个人信息

- **接口路径**：`/api/user/profile`

- **请求方法**：PUT

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述  |
  | -------- | ------ | --- | --- |
  | nickname | string | 否   | 昵称  |
  | avatar   | string | 否   | 头像  |
  | gender   | number | 否   | 性别  |
  | birthday | string | 否   | 生日  |

- **响应数据**：
  
  ```json
  {
    "userInfo": {}
  }
  ```

### 8.3 上传头像

- **接口路径**：`/api/user/avatar`

- **请求方法**：POST（文件上传）

- **请求参数**：
  
  | 参数名    | 类型   | 必填  | 描述   |
  | ------ | ---- | --- | ---- |
  | avatar | file | 是   | 头像文件 |

- **响应数据**：
  
  ```json
  {
    "avatarUrl": ""
  }
  ```

### 8.4 获取用户设置

- **接口路径**：`/api/user/settings`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "settings": {}
  }
  ```

### 8.5 更新用户设置

- **接口路径**：`/api/user/settings`

- **请求方法**：PUT

- **请求参数**：
  
  | 参数名          | 类型      | 必填  | 描述   |
  | ------------ | ------- | --- | ---- |
  | notification | boolean | 否   | 通知开关 |
  | theme        | string  | 否   | 主题设置 |
  | language     | string  | 否   | 语言设置 |

- **响应数据**：
  
  ```json
  {
    "settings": {}
  }
  ```

### 8.6 获取用户收藏

- **接口路径**：`/api/user/favorites`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述   |
  | -------- | ------ | --- | ---- |
  | page     | number | 否   | 页码   |
  | pageSize | number | 否   | 每页数量 |

- **响应数据**：
  
  ```json
  {
    "favorites": [],
    "total": 0
  }
  ```

### 8.7 添加收藏

- **接口路径**：`/api/user/favorite/add`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述   |
  | -------- | ------ | --- | ---- |
  | type     | string | 是   | 收藏类型 |
  | targetId | string | 是   | 目标ID |

- **响应数据**：
  
  ```json
  {
    "favorite": {}
  }
  ```

### 8.8 取消收藏

- **接口路径**：`/api/user/favorite/{favoriteId}`

- **请求方法**：DELETE

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 8.9 获取用户历史记录

- **接口路径**：`/api/user/history`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述   |
  | -------- | ------ | --- | ---- |
  | page     | number | 否   | 页码   |
  | pageSize | number | 否   | 每页数量 |

- **响应数据**：
  
  ```json
  {
    "histories": [],
    "total": 0
  }
  ```

### 8.10 清空历史记录

- **接口路径**：`/api/user/history/clear`

- **请求方法**：POST

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 8.11 获取用户成就

- **接口路径**：`/api/user/achievements`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "achievements": []
  }
  ```

### 8.12 获取用户统计

- **接口路径**：`/api/user/stats`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "totalMessages": 0,
    "totalDynamics": 0,
    "totalCoins": 0
  }
  ```

## 9. 纪念日模块（anniversary）

### 9.1 获取纪念日列表

- **接口路径**：`/api/anniversary/list`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "anniversaries": []
  }
  ```

### 9.2 创建纪念日

- **接口路径**：`/api/anniversary/create`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名         | 类型     | 必填  | 描述    |
  | ----------- | ------ | --- | ----- |
  | title       | string | 是   | 纪念日标题 |
  | date        | string | 是   | 纪念日日期 |
  | description | string | 否   | 纪念日描述 |

- **响应数据**：
  
  ```json
  {
    "anniversary": {}
  }
  ```

### 9.3 获取纪念日详情

- **接口路径**：`/api/anniversary/{anniversaryId}`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "anniversary": {}
  }
  ```

### 9.4 更新纪念日

- **接口路径**：`/api/anniversary/{anniversaryId}`

- **请求方法**：PUT

- **请求参数**：
  
  | 参数名         | 类型     | 必填  | 描述    |
  | ----------- | ------ | --- | ----- |
  | title       | string | 否   | 纪念日标题 |
  | date        | string | 否   | 纪念日日期 |
  | description | string | 否   | 纪念日描述 |

- **响应数据**：
  
  ```json
  {
    "anniversary": {}
  }
  ```

### 9.5 删除纪念日

- **接口路径**：`/api/anniversary/{anniversaryId}`

- **请求方法**：DELETE

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 9.6 获取即将到来的纪念日

- **接口路径**：`/api/anniversary/upcoming`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名   | 类型     | 必填  | 描述   |
  | ----- | ------ | --- | ---- |
  | limit | number | 否   | 限制数量 |

- **响应数据**：
  
  ```json
  {
    "anniversaries": []
  }
  ```

### 9.7 获取纪念日统计

- **接口路径**：`/api/anniversary/stats`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "totalAnniversaries": 0,
    "upcomingAnniversaries": 0
  }
  ```

### 9.8 搜索纪念日

- **接口路径**：`/api/anniversary/search`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名     | 类型     | 必填  | 描述    |
  | ------- | ------ | --- | ----- |
  | keyword | string | 是   | 搜索关键词 |

- **响应数据**：
  
  ```json
  {
    "anniversaries": []
  }
  ```

## 10. 壁纸模块（wallpaper）

### 10.1 获取每日随机壁纸

- **接口路径**：`/randomWall`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "wallpaper": {}
  }
  ```

### 10.2 获取壁纸分类

- **接口路径**：`/classify`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "classifies": []
  }
  ```

### 10.3 获取分类壁纸列表

- **接口路径**：`/wallList`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名        | 类型     | 必填  | 描述   |
  | ---------- | ------ | --- | ---- |
  | classifyId | string | 是   | 分类ID |
  | page       | number | 否   | 页码   |
  | pageSize   | number | 否   | 每页数量 |

- **响应数据**：
  
  ```json
  {
    "wallpapers": [],
    "total": 0
  }
  ```

### 10.4 下载壁纸

- **接口路径**：`/downloadWall`

- **请求方法**：POST

- **请求参数**：
  
  | 参数名         | 类型     | 必填  | 描述   |
  | ----------- | ------ | --- | ---- |
  | wallpaperId | string | 是   | 壁纸ID |

- **响应数据**：
  
  ```json
  {
    "success": true
  }
  ```

### 10.5 获取壁纸详情

- **接口路径**：`/detailWall`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名         | 类型     | 必填  | 描述   |
  | ----------- | ------ | --- | ---- |
  | wallpaperId | string | 是   | 壁纸ID |

- **响应数据**：
  
  ```json
  {
    "wallpaper": {}
  }
  ```

### 10.6 搜索壁纸

- **接口路径**：`/searchWall`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述    |
  | -------- | ------ | --- | ----- |
  | keyword  | string | 是   | 搜索关键词 |
  | page     | number | 否   | 页码    |
  | pageSize | number | 否   | 每页数量  |

- **响应数据**：
  
  ```json
  {
    "wallpapers": [],
    "total": 0
  }
  ```

### 10.7 获取专题列表

- **接口路径**：`/subjectList`

- **请求方法**：GET

- **响应数据**：
  
  ```json
  {
    "subjects": []
  }
  ```

### 10.8 获取专题详情

- **接口路径**：`/subjectDetail`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名       | 类型     | 必填  | 描述   |
  | --------- | ------ | --- | ---- |
  | subjectId | string | 是   | 专题ID |

- **响应数据**：
  
  ```json
  {
    "subject": {}
  }
  ```

### 10.9 获取最新壁纸

- **接口路径**：`/uptodate`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述   |
  | -------- | ------ | --- | ---- |
  | page     | number | 否   | 页码   |
  | pageSize | number | 否   | 每页数量 |

- **响应数据**：
  
  ```json
  {
    "wallpapers": [],
    "total": 0
  }
  ```

### 10.10 获取壁纸排行榜

- **接口路径**：`/rank`

- **请求方法**：GET

- **请求参数**：
  
  | 参数名      | 类型     | 必填  | 描述   |
  | -------- | ------ | --- | ---- |
  | page     | number | 否   | 页码   |
  | pageSize | number | 否   | 每页数量 |

- **响应数据**：
  
  ```json
  {
    "wallpapers": [],
    "total": 0
  }
  ```

## 11. 错误码说明

| 错误码  | 错误信息    | 描述       |
| ---- | ------- | -------- |
| 0000 | 成功      | 接口调用成功   |
| 9999 | 系统错误    | 服务器内部错误  |
| 1001 | 参数错误    | 请求参数不正确  |
| 1002 | 未登录     | 用户未登录    |
| 1003 | 登录失败    | 邮箱或密码错误 |
| 1004 | 验证码错误   | 验证码不正确   |
| 1005 | 权限不足    | 用户没有操作权限 |
| 1006 | 资源不存在   | 请求的资源不存在 |
| 1007 | 网络错误    | 网络请求失败   |
| 1008 | 重复操作    | 操作重复     |
| 1009 | 情侣关系不存在 | 未绑定情侣关系  |

## 12. 接口调用示例

### 12.1 使用uni-app的API调用方式

```javascript
// 登录
uni.$api.auth.login({
  username: 'test',
  password: '123456'
}).then(res => {
  if (res.errCode === 0) {
    console.log('登录成功:', res.data);
  } else {
    console.error('登录失败:', res.errMsg);
  }
});

// 获取用户信息
uni.$api.auth.getUserInfo().then(res => {
  if (res.errCode === 0) {
    console.log('用户信息:', res.data);
  } else {
    console.error('获取用户信息失败:', res.errMsg);
  }
});
```

### 12.2 使用request工具调用方式

```javascript
import request from '../utils/request';

// 登录
request.post('/api/auth/login', {
  username: 'test',
  password: '123456'
}).then(res => {
  if (res.errCode === 0) {
    console.log('登录成功:', res.data);
  } else {
    console.error('登录失败:', res.errMsg);
  }
});

// 获取用户信息
request.get('/api/auth/user').then(res => {
  if (res.errCode === 0) {
    console.log('用户信息:', res.data);
  } else {
    console.error('获取用户信息失败:', res.errMsg);
  }
});
```

## 13. 注意事项

1. **认证**：所有需要认证的接口都需要在请求头中传递Authorization token。
2. **参数验证**：客户端应该在调用接口前对参数进行验证，确保参数的合法性。
3. **错误处理**：客户端应该正确处理接口返回的错误信息，给用户友好的提示。
4. **网络请求**：在网络不稳定的情况下，应该实现请求重试机制。
5. **数据安全**：敏感数据（如密码）应该进行加密处理。
6. **性能优化**：对于列表接口，应该使用分页加载，避免一次性加载过多数据。

## 14. 版本历史

| 版本号    | 发布日期       | 变更内容     |
| ------ | ---------- | -------- |
| v1.0.0 | 2026-02-05 | 初始化API文档 |