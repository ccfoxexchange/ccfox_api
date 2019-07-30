# ws订阅 (仅支持Socket.io)
| 权限类型 | 接口数据类型 | topic                     | 类型 | 描述                      | 需要验签 |
| -------- | ------------ | ------------------------- | ---- | ------------------------- | -------- |
| 读取     | 推送类接口   | match                      | SUB  | 获取持仓-推送             | 是       |
| 读取     | 推送类接口   | future_kline                     | SUB  | 获取K线-推送              | 否       |
| 读取     | 推送类接口   | match                 | SUB  | 获取减仓队列-推送         | 是       |
| 读取     | 推送类接口   | match                  | SUB  | 获取历史委托-推送         | 是       |
| 读取     | 推送类接口   | exchange                | SUB  | 获取币种价格-推送         | 否       |
| 读取     | 推送类接口   | future_market_stat               | SUB  | 获取市场统计-推送         | 否       |
| 读取     | 推送类接口   | match               | SUB  | 获取当前委托-推送         | 是       |
| 读取     | 推送类接口   | match                     | SUB  | 获取成交-推送             | 是       |
| 读取     | 推送类接口   | future_all_indicator      | SUB  | 获取全量行情数据-推送     | 否       |
| 读取     | 推送类接口   | exchange                  | SUB  | 获取法币汇率-推送         | 否       |
| 读取     | 推送类接口   | realtime                  | SUB  | 获取系统时间-推送         | 否       |
| 读取     | 推送类接口   | future_snapshot_depth     | SUB  | 获取行情快照买卖档位-推送 | 否       |
| 读取     | 推送类接口   | future_snapshot_indicator | SUB  | 获取行情快照基础数据-推送 | 否       |
| 读取     | 推送类接口   | match                     | SUB  | 获取资产-推送             | 是       |
| 读取     | 推送类接口   | future_tick                      | SUB  | 获取逐笔成交-推送         | 是       |
| 读取     | 推送类接口   | notice                    | SUB  | 获取通知-推送             | 否       |


**其中 match返回包含多个messagetype**
- 3002, "资产消息",
- 3004,"当前委托",
- 3012,"持仓",
- 3006,"历史委托",
- 3010, "当前成交",
- 3014","强减对列",

# ws对接说明

**1.连接信息**

- 接口方式  Socket.io

- 地址  
  - 测试 wss://futurewstest.ccfox.com   
  - 生产 wss://futurews.ccfox.com

**2.鉴权**

> 需要使用socket.io对接，按照ws订阅表里面的是否需要验签来决定是否订阅下面鉴权，鉴权分为两种，第一种同ccfox官网方式，需要登录拿到token后进行订阅，第二种为纯api对接方式(一般建议使用第二种)

```js
// 子账号
[
    "auth",
    {
        "header": {
            "type": 1001
        },
        "body": {
            "token": "token" 
        }
    }
]
//token 同ccfox官网方式，需要登录拿到token


// api账号
[
    "auth",
    {
        "header": {
            "type": 1001
        },
        "body": {
            "apiKey": "AccessKey",
            "expires": "expires",
            "signature": "signature"
        }
    }
]

// AccessKey, // 获取方式同restful
// expires": expires, // 获取方式同restful
// signature // 签名方式同restful , 其中method="GET"，path="GET/realtime"

```

3.订阅

> 根据是否需要鉴权之后来进行业务top的订阅，下面式是订阅的标准格式，在相关ws接口说明里有详细的说明

```js
[
    "subscribe",
    {
        "header": {
            "type": 1003
        },
        "body": {
            "topics": [
                {
                    "topic": "future_snapshot_indicator",
                    "params": {
                        "symbols": [
                            {
                                "symbol": 0
                            }
                        ]
                    }
                },
                {
                    "topic": "future_snapshot_depth",
                    "params": {
                        "symbols": [
                            {
                                "symbol": 0
                            }
                        ]
                    }
                },
                {
                    "topic": "future_all_indicator"
                },
                {
                    "topic": "future_tick",
                    "params": {
                        "symbols": [
                            {
                                "symbol": 0
                            }
                        ]
                    }
                },
                {
                    "topic": "match"
                },
                {
                    "topic": "future_kline",
                    "params": {
                        "symbols": [
                            {
                                "symbol": 0,
                                "ranges": [
                                    "60000"
                                ]
                            }
                        ]
                    }
                },
                {
                    "topic": "exchange"
                },
                {
                    "topic": "coin_price"
                },
                {
                    "topic": "notice"
                }
            ]
        }
    }
]
```

# socket推送

> ws返回数据在restful接口都有相应映射，可以从restful返回数据查看字段说明

## 5.1 获取持仓-推送

### 基本信息

**接口描述：**

订阅topic: match
消息订阅格式如下所示：

```{
"header": {
"type": 1003
},
"body": {
    "topics": [{
        "topic": "match"
    }]
}
}
```

### 返回数据

| 名称                 | 类型      | 是否必须 | 默认值 | 备注                         | 其他信息          |
| -------------------- | --------- | -------- | ------ | ---------------------------- | ----------------- |
| messageTpe           | number    | 非必须   |        | 3012                         |                   |
| lastId               | number    | 非必须   |        | 消息ID                       |                   |
| applId               | number    | 非必须   |        | 2                            |                   |
| accountId            | number    | 非必须   |        | 账号ID                       |                   |
| posis                | object [] | 非必须   |        | 持仓列表                     | item 类型: object |
| ├─ contractId        | number    | 非必须   |        | 合约ID                       |                   |
| ├─ marginType        | number    | 非必须   |        | 保证金类型，1全仓2逐仓       |                   |
| ├─ initMarginRate    | string    | 非必须   |        | 初始保证率                   |                   |
| ├─maintainMarginRate | string    | 非必须   |        | 维持保证金                   |                   |
| ├─ initMargin        | string    | 非必须   |        | 初始保证金                   |                   |
| ├─ extraMargin       | string    | 非必须   |        | 额外保证金                   |                   |
| ├─ openAmt           | string    | 非必须   |        | 开仓金额                     |                   |
| ├─ posiQty           | string    | 非必须   |        | 持仓量，大于0多头，小于0空头 |                   |
| ├─ frozenOpenQty     | string    | 非必须   |        | 开仓委托冻结量               |                   |
| ├─ frozenCloseQty    | string    | 非必须   |        | 平仓委托冻结量               |                   |
| ├─ posiStatus        | number    | 非必须   |        | 1正常，2等待强平             |                   |
| ├─ closeProfitLoss   | string    | 非必须   |        | 已实现盈亏                   |                   |

## 5.2 获取K线-推送



### 基本信息

**接口描述：**

订阅topic: future_kline
消息订阅格式如下所示：

```{
"header": {
"type": 1003
},
"body": {
    "topics": [{
        "topic": "future_kline",
        "params": {
            "symbols": [{
            "symbol": 100,
            "ranges": ["60000"]
        }]
    }
    }]
}
}
```

注意项：
1、支持按照1分钟5分钟这种单独订阅，需要订阅所有，就传数组，一定要传
2、lines 的数据结构为：List<List>
如：
[
    [${时间戳}, ${开市价格}, ${最高价格}, ${最低价格}, ${闭市价格}, ${成交量}]
]
3、range取值：
1Min = 60000;
3Min = 180000;
5Min = 300000;
15Min = 900000;
30Min = 1800000;
1Hour = 3600000;
2Hour = 7200000;
4Hour = 14400000;
6Hour = 21600000;
12Hour = 43200000;
1Day = 86400000;
1Week = 604800000;

### 返回数据

| 名称       | 类型      | 是否必须 | 默认值 | 备注   | 其他信息          |
| ---------- | --------- | -------- | ------ | ------ | ----------------- |
| contractId | integer   | 非必须   |        | 合约ID |                   |
| range      | string    | 非必须   |        | 类型   |                   |
| lines      | object [] | 非必须   |        |        | item 类型: object |

## 5.3 获取减仓队列-推送

### 基本信息

**Path：** /push/forelower

**Method：** GET

**接口描述：**

消息topic: match
消息订阅格式如下所示：
```{
"header": {
"type": 1003
},
"body": {
    "topics": [{
        "topic": "match"
    }]
}
}
```

### 返回数据

| 名称          | 类型      | 是否必须 | 默认值 | 备注                           | 其他信息          |
| ------------- | --------- | -------- | ------ | ------------------------------ | ----------------- |
| messageType   | number    | 非必须   |        | 3014                           |                   |
| applId        | number    | 非必须   |        | 2                              |                   |
| accountId     | number    | 非必须   |        | 用户ID                         |                   |
| ranks         | object [] | 非必须   |        | 减仓队列                       | item 类型: object |
| ├─ contractId | number    | 非必须   |        | 合约ID                         |                   |
| ├─ rank       | string    | 非必须   |        | 强减信号，值越小强减可能性越大 |                   |

## 5.4 获取历史委托-推送

### 基本信息

**接口描述：**

消息topic: match
注意：数据集合列表中，最多50笔最近委托数据记录
消息订阅格式如下所示：
```{
"header": {
"type": 1003
},
"body": {
    "topics": [{
        "topic": "match"
    }]
}
}
```

### 请求参数

### 返回数据

| 名称               | 类型      | 是否必须 | 默认值 | 备注                         | 其他信息          |
| ------------------ | --------- | -------- | ------ | ---------------------------- | ----------------- |
| messageType        | number    | 非必须   |        | 3006                         |                   |
| lastId             | number    | 非必须   |        | 消息ID                       |                   |
| accountId          | number    | 非必须   |        | 账号ID                       |                   |
| orders             | object [] | 非必须   |        | 委托列表                     | item 类型: object |
| ├─ orderId         | string    | 非必须   |        | 委托号                       |                   |
| ├─ side            | number    | 非必须   |        | 买卖方向（1：买，-1：卖）    |                   |
| ├─ price           | string    | 非必须   |        | 委托价格                     |                   |
| ├─ quantity        | string    | 非必须   |        | 委托数量                     |                   |
| ├─ orderType       | number    | 非必须   |        | 委托类型                     |                   |
| ├─ timestamp       | number    | 非必须   |        | 委托时间                     |                   |
| ├─ contractId      | number    | 非必须   |        | 合约ID                       |                   |
| ├─ filledCurrency  | string    | 非必须   |        | 成交金额                     |                   |
| ├─ filledQuantity  | string    | 非必须   |        | 成交数量                     |                   |
| ├─canceledQuantity | string    | 非必须   |        | 撤单数量                     |                   |
| ├─ positionEffect  | number    | 非必须   |        | 开平标志，1开仓2平仓         |                   |
| ├─ fcOrderId       | string    | 非必须   |        | 强平委托号，非空时为强平委托 |                   |

## 5.5 获取币种价格-推送

### 基本信息

**接口描述：**

订阅topic:  coin_price
消息订阅格式，如下所示：

```{
"header": {
"type": 1003
},
"body": {
    "topics": [{
        "topic": "exchange",
    }]
}
}
```

### 返回数据

| 名称          | 类型      | 是否必须 | 默认值 | 备注     | 其他信息          |
| ------------- | --------- | -------- | ------ | -------- | ----------------- |
|               | object [] | 非必须   |        |          | item 类型: object |
| ├─ currencyId | number    | 非必须   |        | 货币ID   |                   |
| ├─ latest     | string    | 非必须   |        | 最新价格 |                   |

## 5.6 获取市场统计-推送



### 基本信息

**接口描述：**

订阅topic：future_market_stat
消息订阅格式，如下所示：

```{
"header": {
"type": 1003
},
"body": {
    "topics": [{
        "topic": "future_market_stat",
    }]
}
}
```

### 返回数据

| 名称             | 类型    | 是否必须 | 默认值 | 备注         | 其他信息 |
| ---------------- | ------- | -------- | ------ | ------------ | -------- |
| messageType      | string  | 非必须   |        | 消息类型：13 |          |
| currencyId       | integer | 非必须   |        | 币种ID       |          |
| total24hTurnover | string  | 非必须   |        | 24小时成交额 |          |
| total7dTurnover  | string  | 非必须   |        | 7日成交额    |          |
| total30dTurnover | string  | 非必须   |        | 30日成交额   |          |

## 5.7 获取当前委托-推送

### 基本信息

**接口描述：**

消息topic: match
消息订阅格式，如下所示：

```{
"header": {
"type": 1003
},
"body": {
    "topics": [{
        "topic": "match"
    }]
}
}
```

### 请求参数

### 返回数据

| 名称           | 类型   | 是否必须 | 默认值 | 备注                             | 其他信息 |
| -------------- | ------ | -------- | ------ | -------------------------------- | -------- |
| messageType    | number | 非必须   |        | 消息类型：3004                   |          |
| accountId      | number | 非必须   |        | 账户ID                           |          |
| contractId     | number | 非必须   |        | 合约ID                           |          |
| orderId        | string | 非必须   |        | 委托号                           |          |
| clientOrderId  | string | 非必须   |        | 客户订单编号                     |          |
| price          | string | 非必须   |        | 委托价格                         |          |
| quantity       | string | 非必须   |        | 委托数量                         |          |
| leftQuantity   | string | 非必须   |        | 剩余数量                         |          |
| side           | number | 非必须   |        | 买卖方向（1：买，-1：卖）        |          |
| placeTimestamp | number | 非必须   |        | 委托时间                         |          |
| matchAmt       | string | 非必须   |        | 成交金额                         |          |
| orderType      | number | 非必须   |        | 合约委托类型（1：限价，3：市价） |          |
| positionEffect | number | 非必须   |        | 开平标志（1：开仓，2：平仓）     |          |
| marginType     | number | 非必须   |        | 保证金类型（1、全仓，2：逐仓）   |          |
| initMarginRate | string | 非必须   |        | 初始保证金率                     |          |
| fcOrderId      | string | 非必须   |        | 强平委托号，空时为强平委托       |          |
| markPrice      | string | 非必须   |        | 标记价格                         |          |
| feeRate        | string | 非必须   |        | 手续费率                         |          |
| contractUnit   | string | 非必须   |        | 合约单位                         |          |

## 5.8 获取成交-推送

### 基本信息

**接口描述：**

消息topic: match
注意：数据集合列表中，最多10笔最近成交数据记录
消息订阅格式，如下所示：
```{
"header": {
"type": 1003
},
"body": {
    "topics": [{
        "topic": "match"
    }]
}
}
```

### 返回数据

| 名称             | 类型      | 是否必须 | 默认值 | 备注                                                    | 其他信息          |
| ---------------- | --------- | -------- | ------ | ------------------------------------------------------- | ----------------- |
| messageType      | number    | 非必须   |        | 3010                                                    |                   |
| lastId           | number    | 非必须   |        | 消息Id                                                  |                   |
| applId           | number    | 非必须   |        | 2                                                       |                   |
| accountId        | number    | 非必须   |        | 账号ID                                                  |                   |
| matchs           | object [] | 非必须   |        | 成交列表                                                | item 类型: object |
| ├─ applId        | number    | 非必须   |        | 2                                                       |                   |
| ├─contractId     | number    | 非必须   |        | 合约ID                                                  |                   |
| ├─matchTime      | number    | 非必须   |        | 成交时间                                                |                   |
| ├─matchPrice     | null      | 非必须   |        | 成交价格                                                |                   |
| ├─matchQty       | null      | 非必须   |        | 成交数量                                                |                   |
| ├─matchAmt       | null      | 非必须   |        | 成交金额                                                |                   |
| ├─ execId        | null      | 非必须   |        | 成交号                                                  |                   |
| ├─ orderId       | null      | 非必须   |        | 委托ID                                                  |                   |
| ├─ fee           | null      | 非必须   |        | 手续费                                                  |                   |
| ├─positionEffect | number    | 非必须   |        | 开平标志                                                |                   |
| ├─ side          | number    | 非必须   |        | 买入方向                                                |                   |
| ├─matchType      | integer   | 非必须   |        | 成交类型：0-正常、1-强平、2- 破产强减、3-自动减仓盈利方 |                   |

## 5.9 获取全量行情数据-推送

### 基本信息

**接口描述：**

订阅topic:   future_all_indicator
消息订阅格式，如下所示：
```{
"header": {
"type": 1003
},
"body": {
    "topics": [{
        "topic": "future_all_indicator",
    }]
}
}
```

### 返回数据

| 名称   | 类型        | 是否必须 | 默认值 | 备注 | 其他信息          |
| ------ | ----------- | -------- | ------ | ---- | ----------------- |
|        | object []   | 非必须   |        |      | item 类型: object |
| ├─ mt  | number      | 必须     |        |      |                   |
| ├─ ai  | number      | 必须     |        |      |                   |
| ├─ ci  | number      | 必须     |        |      |                   |
| ├─ sb  | string      | 必须     |        |      |                   |
| ├─ td  | number      | 必须     |        |      |                   |
| ├─ te  | number      | 必须     |        |      |                   |
| ├─ lp  | string      | 必须     |        |      |                   |
| ├─ mq  | string      | 必须     |        |      |                   |
| ├─ nt  | number      | 必须     |        |      |                   |
| ├─ op  | string      | 必须     |        |      |                   |
| ├─ ph  | string      | 必须     |        |      |                   |
| ├─ pl  | string      | 必须     |        |      |                   |
| ├─ hph | string      | 必须     |        |      |                   |
| ├─ hpl | string      | 必须     |        |      |                   |
| ├─ tt  | string      | 必须     |        |      |                   |
| ├─ tv  | string      | 必须     |        |      |                   |
| ├─ tbv | string      | 必须     |        |      |                   |
| ├─ tav | string      | 必须     |        |      |                   |
| ├─ pp  | string      | 必须     |        |      |                   |
| ├─ cp  | string      | 必须     |        |      |                   |
| ├─ pv  | string      | 必须     |        |      |                   |
| ├─ pcr | string      | 必须     |        |      |                   |
| ├─ pc  | string      | 必须     |        |      |                   |
| ├─ lui | number      | 必须     |        |      |                   |
| ├─ cs  | number      | 必须     |        |      |                   |
| ├─ dp  | string      | 必须     |        |      |                   |
| ├─ fr  | null,string | 必须     |        |      |                   |
| ├─ pfr | null,string | 必须     |        |      |                   |
| ├─ pi  | null,string | 必须     |        |      |                   |
| ├─ ppi | null,string | 必须     |        |      |                   |
| ├─ fb  | string      | 必须     |        |      |                   |
| ├─ ts  | number      | 必须     |        |      |                   |
| ├─ sl  | number      | 必须     |        |      |                   |
| ├─ ip  | string      | 必须     |        |      |                   |

## 5.10 获取法币汇率-推送

### 基本信息

**接口描述：**

注意：订阅topic名称： exchange
数据订阅格式，如下所示：

```{
"header": {
"type": 1003
},
"body": {
    "topics": [{
        "topic": "exchange",
    }]
}
}
```

### 返回数据

| 名称          | 类型      | 是否必须 | 默认值 | 备注     | 其他信息          |
| ------------- | --------- | -------- | ------ | -------- | ----------------- |
|               | object [] | 非必须   |        |          | item 类型: object |
| ├─ id         | number    | 非必须   |        | 法币Id   |                   |
| ├─ name       | string    | 非必须   |        | 法币名称 |                   |
| ├─ rate       | string    | 非必须   |        | 汇率     |                   |
| ├─ updateTime | string    | 非必须   |        | 更新时间 |                   |

## 5.11 获取系统时间-推送

### 基本信息

**接口描述：**

数据topic：realtime
数据订阅格式，如下所示：

```{
"header": {
"type": 1003
},
"body": {
    "topics": [{
        "topic": "realtime"
    }
    }]
}
}
```

### 返回数据

| 名称      | 类型   | 是否必须 | 默认值 | 备注 | 其他信息 |
| --------- | ------ | -------- | ------ | ---- | -------- |
| timeSlice | string | 非必须   |        | 微秒 |          |

## 5.12 获取行情快照买卖档位-推送

### 基本信息

**接口描述：**

订阅topic: future_snapshot_depth
数据订阅格式，如下所示：

```{
"header": {
"type": 1003
},
"body": {
    "topics": [{
        "topic": "future_snapshot_depth",
        "params": {
            "symbols": [{
                "symbol": 100
        }]
    }]
}
}
```

注意：
数据格式中的symbol值为：合约ID
asks与bids 的数据格式：List<List>
 如：
    [
        [${价格}, ${数量}]
    ]

### 返回数据

| 名称       | 类型      | 是否必须 | 默认值 | 备注           | 其他信息          |
| ---------- | --------- | -------- | ------ | -------------- | ----------------- |
| contractId | integer   | 非必须   |        | 合约ID         |                   |
| bids       | object [] | 非必须   |        | 买档位数据列表 | item 类型: object |
| asks       | object [] | 非必须   |        | 卖档位数据列表 | item 类型: object |

## 5.13 获取行情快照基础数据-推送

### 基本信息

**接口描述：**

订阅topic: future_snapshot_indicator
数据订阅格式，如下所示：

```{
"header": {
"type": 1003
},
"body": {
    "topics": [{
        "topic": "future_snapshot_indicator",
        "params": {
            "symbols": [{
                "symbol": 100
        }]
    }]
}
}
```

注意：
数据格式中的symbol值为：合约ID

### 返回数据

| 名称 | 类型   | 是否必须 | 默认值 | 备注 | 其他信息 |
| ---- | ------ | -------- | ------ | ---- | -------- |
| mt   | number | 非必须   |        |      |          |
| ai   | number | 非必须   |        |      |          |
| ci   | number | 非必须   |        |      |          |
| sb   | string | 非必须   |        |      |          |
| td   | number | 非必须   |        |      |          |
| te   | number | 非必须   |        |      |          |
| lp   | string | 非必须   |        |      |          |
| mq   | string | 非必须   |        |      |          |
| nt   | number | 非必须   |        |      |          |
| op   | string | 非必须   |        |      |          |
| ph   | string | 非必须   |        |      |          |
| pl   | string | 非必须   |        |      |          |
| hph  | string | 非必须   |        |      |          |
| hpl  | string | 非必须   |        |      |          |
| tt   | string | 非必须   |        |      |          |
| tv   | string | 非必须   |        |      |          |
| tbv  | string | 非必须   |        |      |          |
| tav  | string | 非必须   |        |      |          |
| pp   | string | 非必须   |        |      |          |
| cp   | string | 非必须   |        |      |          |
| pv   | string | 非必须   |        |      |          |
| pcr  | string | 非必须   |        |      |          |
| pc   | string | 非必须   |        |      |          |
| lui  | number | 非必须   |        |      |          |
| cs   | number | 非必须   |        |      |          |
| dp   | string | 非必须   |        |      |          |
| fr   | null   | 非必须   |        |      |          |
| pfr  | null   | 非必须   |        |      |          |
| pi   | null   | 非必须   |        |      |          |
| ppi  | null   | 非必须   |        |      |          |
| fb   | string | 非必须   |        |      |          |
| ts   | number | 非必须   |        |      |          |
| sl   | number | 非必须   |        |      |          |
| ip   | string | 非必须   |        |      |          |

## 5.14 获取资产-推送

### 基本信息

**接口描述：**

数据topic: match
数据订阅格式，如下所示：

```{
"header": {
"type": 1003
},
"body": {
    "topics": [{
        "topic": "match"
    }]
}
}
```

### 返回数据

| 名称             | 类型   | 是否必须 | 默认值 | 备注                       | 其他信息 |
| ---------------- | ------ | -------- | ------ | -------------------------- | -------- |
| messageType      | string | 非必须   |        | 3002                       |          |
| accountId        | string | 非必须   |        | 用户ID                     |          |
| lastId           | string | 非必须   |        | 消息ID                     |          |
| currencyId       | string | 非必须   |        | 币种I                      |          |
| totalBalance     | string | 非必须   |        | 总资产                     |          |
| available        | string | 非必须   |        | 可用资产                   |          |
| frozenForTrade   | string | 非必须   |        | 委托冻结保证金和手续费     |          |
| initMargin       | string | 非必须   |        | 仓位已占用保证金           |          |
| frozenInitMargin | string | 非必须   |        | 委托冻结保证金，不含手续费 |          |
| closeProfitLoss  | string | 非必须   |        | 累计已实现盈亏             |          |

## 5.15 获取逐笔成交-推送

**接口描述：**

数据topic: future_tick
数据订阅格式，如下所示：

```{
"header": {
"type": 1003
},
"body": {
    "topics": [{
        "topic": "future_tick",
        "params": {
            "symbols": [{
            "symbol": 100
        }]
    }
    }]
}
}
```

**注意**：

1. 订阅所有时，不传params
2. 数据格式中的symbol值为：合约ID
3. trades 数据结构 List ` [ [${时间戳}, ${成交价格}, ${成交数量}, ${方向}] ]`

### 返回数据

| 名称       | 类型      | 是否必须 | 默认值 | 备注     | 其他信息          |
| ---------- | --------- | -------- | ------ | -------- | ----------------- |
| contractId | integer   | 非必须   |        | 合约ID   |                   |
| trades     | object [] | 非必须   |        | 数据列表 | item 类型: object |

## 5.16 获取通知-推送

### 基本信息

**接口描述：**

`topic：notice`
订阅格式，如下所示：

```{
"header": {
"type": 1003
},
"body": {
    "topics": [{
        "topic": "notice"
    }]
}
}
```

### 返回数据

| 名称          | 类型      | 是否必须 | 默认值 | 备注                            | 其他信息          |
| ------------- | --------- | -------- | ------ | ------------------------------- | ----------------- |
|               | object [] | 非必须   |        |                                 | item 类型: object |
| ├─ title      | string    | 非必须   |        | 主题                            |                   |
| ├─ contractId | integer   | 非必须   |        | 合约ID                          |                   |
| ├─ type       | integer   | 非必须   |        | 类型(1, 告警、2, 强平、3, 强减) |                   |
| ├─ data       | string    | 非必须   |        | 通知数据                        |                   |
| ├─ time       | string    | 非必须   |        | 时间                            |                   |
