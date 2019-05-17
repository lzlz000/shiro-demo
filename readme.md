# Shirodemo

## springboot结合shiro的小例子

- 用户角色权限的配置
```ShiroConfig``` 配置了角色和url的关系
数据库表user,role,user_role维护用户和角色、权限的关系

- kaptcha 验证码登录

- 通过shiro实现了一个单用户登录的效果，即一处登录另一处踢下线
## 轮询即时通讯demo
 ```/static/IM.html```

```javascript
// 订阅频道 channel1
IM.subscribe('channel1')

// 此时订阅了该频道的用户会收到消息
IM.send('channel1','哈哈')

// 取消订阅
IM.unsubscribe('channel1')
```

