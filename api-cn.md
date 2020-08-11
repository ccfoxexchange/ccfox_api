
# API简介
# 合约交易接入说明
  ## 合约交易接口列表

  | 权限类型 | 接口数据类型   | 请求方法                                 | 类型   | 描述                   | 需要验签 |
| -------- | -------------- | ---------------------------------------- | ------ | ---------------------- | -------- |
| 读取     | 基础信息类接口 | /api/v1/future/queryContract             | GET    | 获取所有合约信息       | 否       |
| 读取     | 基础信息类接口 | /api/v1/common/queryCurrency             | GET    | 获取币种信息           | 否       |
| 读取     | 基础信息类接口 | /api/v1/common/exchange/list             | GET    | 获取所有法币           | 否       |
| 读取     | 基础信息类接口 | /api/v1/common/exchange/coins            | GET    | 获取计价货币的价格     | 否       |
| 读取     | 基础信息类接口 | /api/v1/future/queryContractDeliveryList | GET    | 获取期货交割历史       | 否       |
| 读取     | 行情类接口     | /api/v1/futureQuot/queryMarketStat       | GET    | 获取24小时统计         | 否       |
| 读取     | 行情类接口     | /api/v1/futureQuot/queryCandlestick      | GET    | 获取K线数据            | 否       |
| 读取     | 行情类接口     | /api/v1/futureQuot/querySnapshot         | GET    | 获取单个合约行情快照   | 否       |
| 读取     | 行情类接口     | /api/v1/futureQuot/queryIndicatorList    | GET    | 获取所有行情快照       | 否       |
| 读取     | 行情类接口     | /api/v1/futureQuot/queryTickTrade        | GET    | 获取逐笔成交           | 否       |
| 读取     | 用户类接口     | /api/v1/future/queryVarietyMargin        | GET    | 获取用户合约保证金梯度 | 否       |
| 读取     | 用户类接口     | /api/v1/future/margin                    | GET    | 获取用户资产信息       | 是       |
| 读取     | 用户类接口     | /api/v1/future/position                  | GET    | 获取用户持仓信息       | 是       |
| 读取     | 用户类接口     | /api/v1/future/user                      | GET    | 获取用户基本信息       | 是       |
| 交易     | 交易类接口     | /api/v1/future/order                     | GET    | 合约下单               | 是       |
| 交易     | 交易类接口     | /api/v1/future/orders                    | GET    | 合约批量下单           | 是       |
| 交易     | 交易类接口     | /api/v1/future/order                     | DELETE | 合约撤单               | 是       |
| 交易     | 交易类接口     | /api/v1/future/orders                    | DELETE | 合约批量撤单           | 是       |
| 交易     | 交易类接口     | /api/v1/future/order/all                 | DELETE | 合约撤消所有订单       | 是       |
| 交易     | 交易类接口     | /api/v1/future/order                     | GET    | 获取订单信息           | 是       |
| 交易     | 交易类接口     | /api/v1/future/queryActiveOrder          | GET    | 获取当前订单列表       | 是       |
| 交易     | 交易类接口     | /api/v1/future/queryLastestHistoryOrders | GET    | 获取历史委托           | 是       |
| 交易     | 交易类接口     | /api/v1/future/position/isolate          | POST   | 切换仓位模式           | 是       |
| 交易     | 交易类接口     | /api/v1/future/position/transferMargin   | POST   | 调整保证金率           | 是       |
| 交易     | 交易类接口     | /api/v1/future/queryForceLower           | GET    | 获取强减队列           | 是       |
| 交易     | 交易类接口     | /api/v1/future/queryMatch                | GET    | 获取当前成交强减队列   | 是       |
| 交易     | 交易类接口     | /api/v1/future/position/adjustModel      | POST   | 调整持仓模式           | 是       |

  ## 访问地址

  - 测试环境地址 https://apitest.ccfox.com/
  - 生产环境地址 https://api.ccfox.com/

  ## 签名认证

  ### 申请创建 API Key

  通过web端页面 创建API Key
  API Key 包括以下两部分
  - Access Key API 访问密钥
  - Secret Key 签名认证加密所使用的密钥

  ### 签名说明
API 请求在通过 internet 传输的过程中极有可能被篡改，为了确保请求未被更改，除公共接口（基础信息，行情数据）外的私有接口均必须使用您的 API Key 做签名认证，以校验参数或参数值在传输途中是否发生了更改。

一个合法的请求由以下几部分组成：

- 方法请求地址：即访问服务器地址 api.ccfox.com，比如 https://api.ccfox.com/api/v1/order。
- API访问密钥AccessKey：您申请的 API Key 中的 Access Key。
- 时间戳expires：您发出请求的时间戳。如：1548311559。在查询请求中包含此值有助于防止第三方截取您的请求。校验一分钟以内合法
- 签名signature：签名计算得出的值，用于确保签名有效和未被篡改。

> 注意：AccessKey，expires， signature三个参数都在请求头中，另外需要设置`'content-type' : 'application/json'`,参考以下实例(对应各自键值)

```js
// 请求头必须包含
var headers = {
  'content-type' : 'application/json',
  'apiExpires': expires, //UNIX时间戳以秒为单位校验一分钟以内合法
  'apiKey': AccessKey, // API 访问密钥（您获取的ApiKey中的AccessKey）
  'signature': signature // 签名
};
```

### 签名步骤

因为使用 HMAC SHA256进行签名计算时，使用不同内容计算得到的结果会完全不同。所以在进行签名计算前，请先对请求进行规范化处理。（GET和POST请求不同）

>  GET请求将请求参数json化，然后urlencode, 放在url参数后面(?filter=xxxxxxx), data为空字符串

> POST请求将请求参数json化成字符串(字符串不能有空格)，放在data参数的位置

例：查询某订单详情(GET)

```js
GET https://newcoin.io/api/v1/order?filter={"orderId":"11548326910655928"}
//accessKey： c4e516d4-8f42-4c48-bbbe-8d74ad62d45c
//secretKey： d4f74ddd-6875-48b9-827c-49473b80f24d

verb = 'GET'
//Note url-encoding on querystring - this is '/api/v1/instrument?filter={"orderId":"11548326910655928"}'
//Be sure to HMAC exactly what is sent on the wire
path = '/api/v1/order?filter=%7B%22orderId%22%3A%2211548326910655928%22%7D'
expires = 1518064237
data = ''
HEX(HMAC_SHA256(apiSecret, 'GET/api/v1/order?filter=%7B%22orderId%22%3A%2211548326910655928%22%7D1518064237'))
//Result is:'f8c9f640e1c9a068e27eac13e38fb900766d6953862d598bb483266dfa96017d'
signature = HEX(HMAC_SHA256(secretKey, verb + path + str(expires) + data))
```

### 签名失败

- 检查 APIKey 是否有效,是否复制正确,是否有绑定 IP 白名单
- 检查时间戳是否是 UTC 当前时间戳，校验一分钟以内合法
- 检查参数是否按(verb + path + expires + data)排序
- 检查编码utf-8

## 代码实例

- node.js

```javascript
var request = require("request");
var crypto = require("crypto");

var accessKey = "c4e516d4-8f42-4c48-bbbe-8d74ad62d45c";
var secretKey = "d4f74ddd-6875-48b9-827c-49473b80f24d";
var verb = "GET",
  path = "/api/v1/order?filter=%7B%22orderId%22%3A%2211548326910655928%22%7D",
  expires = 1548311559;
  data = "";

var postBody = JSON.stringify(data);

var signature = crypto
  .createHmac("sha256", secretKey)
  .update(verb + path + expires + data)
  .digest("hex");

var headers = {
  "content-type": "application/json",
  Accept: "application/json",
  apiExpires: expires,
  apikey: accessKey,
  signature: signature
};

const requestOptions = {
  headers: headers,
  url: "xxxx.io" + path,
  method: verb,
  body: postBody
};

request(requestOptions, function(error, response, body) {
  if (error) {
    console.log(error);
  }
  console.log(body);
});
```

python 实例

```python
import time
import hashlib
import hmac
from urllib.parse import urlparse
# 签名是 HMAC_SHA256(secret, verb + path + expires + data)，十六进制编码。
# verb 必须是大写的，url 是相对的，expires 必须是 unix 时间戳（以秒为单位）
# 并且数据（如果存在的话）必须是 JSON 格式，并且键值之间没有空格。
def generate_signature(secret, verb, url, expires, data):
    """Generate a request signature compatible with cloud."""
    # 解析该 url 来移除基础地址而得到 path
    parsedURL = urlparse(url)
    path = parsedURL.path
    if parsedURL.query:
        path = path + '?' + parsedURL.query

    if isinstance(data, (bytes, bytearray)):
        data = data.decode('utf8')

    print("Computing HMAC: %s" % verb + path + str(expires) + data)
    message = verb + path + str(expires) + data

    signature = hmac.new(bytes(secret, 'utf8'), bytes(message, 'utf8'), digestmod=hashlib.sha256).hexdigest()
    return signature

expires = 1518064236
# 或者你可以像以下这样生成:
# expires = int(round(time.time()) + 5)
# GET请求将参数json化，然后urlencode, 放在url参数后面(?filter=xxxxxxx), data为空字符串
# POST请求将参数json化成字符串(字符串不能有空格)，放在data参数的位置
print(generate_signature('chNOOS4KvNXR_Xq4k4c9qsfoKWvnDecLATCRlcBwyKDYnWgO', 'GET', '/api/v1/instrument?filter=%7B%22orderId%22%3A%2211548326910655928%22%7D1518064237', expires, ''))
print(generate_signature('chNOOS4KvNXR_Xq4k4c9qsfoKWvnDecLATCRlcBwyKDYnWgO', 'POST', '/api/v1/instrument', expires, '{"symbol":"XBTM15","price":219.0,"clOrdID":"mm_bitmex_1a/oemUeQ4CAJZgP3fjHsA","orderQty":98}'))
```

# 频率限制

> 接口数据类型在[合约交易接口列表](https://github.com/ccfoxexchange/ccfox_api/blob/master/api-cn.md#合约交易接口列表)有说明

 - 非交易接口 120/min
 - 交易接口   60/s

# 错误码

| 编码 | 注释                                         |
| ---- | -------------------------------------------- |
| 0    | 成功                                         |
| 1001 | 账户不存在                                   |
| 1002 | 合约不存在                                   |
| 1003 | 应用标识错误                                 |
| 1004 | 委托价格不合法                               |
| 1005 | 委托数量不合法                               |
| 1006 | 委托数量超过限制                             |
| 1007 | 保证金可用不足                               |
| 1008 | 可用数量不足                                 |
| 1009 | 账户被接管，禁止交易                         |
| 1010 | 账户被接管，禁止转出资金                     |
| 1011 | 合约禁止交易                                 |
| 1012 | 委托方向不合法                               |
| 1013 | 委托类型不合法                               |
| 1016 | 委托数量低于最小委托单位                     |
| 1018 | 委托金额不合法                               |
| 1019 | 委托不存在                                   |
| 1020 | 对手方没有订单                               |
| 1022 | 该笔持仓可平仓数量不足                       |
| 1023 | 账户被接管，禁止平仓                         |
| 1027 | 合约已经存在                                 |
| 1028 | 委托笔数超过限制                             |
| 1029 | 持仓数量超过限制                             |
| 1031 | 当前合约存在与本次委托的保证金类型不同的持仓 |
| 1032 | 初始保证金率错误                             |
| 1035 | 禁止转入转出保证金                           |
| 1036 | 委托价格超过限制                             |
| 1037 | 委托金额超过限制                             |
| 1041 | 系统未就绪，指数未发送或者之前有数据没有全部入库   |
| 1042 | 禁止逐仓开仓                                 |
| 1044 | 市价委托消耗了过多的流动性档位               |
| 1046 | 当前价格无法成为被动委托，订单将被撤销       |
| 1050 | 触发类型未就绪                             |
| 1051 | 触发价格不合理                             |
| 1053 | 条件单委托数量超出限制                      |
| 1056 | 该保证金币种下有持仓或者委托，禁止切换持仓模式                      |



# 字段缩写对照表

> 为防止数据包过大，返回数据键值进行了压缩，参考下面对应关系

```js
{
    contractId: v.ci,
    result: {
        messageType: v.mt,
        applId: v.ai,
        contractId: v.ci,
        symbol: v.sb,
        tradeDate: v.td,
        time: v.te,
        lastPrice: v.lp,
        matchQty: v.mq,
        numTrades: v.nt,
        openPrice: v.op,
        priceHigh: v.ph,
        priceLow: v.pl,
        historyPriceHigh: v.hph,
        historyPriceLow: v.hpl,
        totalTurnover: v.tt,
        totalVolume: v.tv,
        totalBidVol: v.tbv,
        totalAskVol: v.tav,
        prevPrice: v.pp,
        clearPrice: v.cp,
        posiVol: v.pv,
        priceChangeRadio: v.pcr,
        priceChange: v.pc,
        lastUpdateId: v.lui,
        contractStatus: v.cs,
        deliveryPrice: v.dp,
        fundingRate: v.fr,
        predictionFundingRate: v.pfr,
        premiumIndex: v.pi,
        predictionPremiumIndex: v.ppi,
        fairBasis: v.fb,
        tradingsignal: v.ts,
        indexPrice: v.ip,
        signalLevel: v.sl
    }
}
```

# api接口

# 1.基础信息类



## 1.1 获取所有合约信息



### 基本信息

**Path：** /api/v1/future/queryContract

**Method：** GET

**接口描述：**

注意：不需要签名

### 返回数据

| 名称                       | 类型        | 是否必须 | 默认值 | 备注                                                     | 其他信息          |
| -------------------------- | ----------- | -------- | ------ | -------------------------------------------------------- | ----------------- |
| result                     | object []   | 非必须   |        |                                                          | item 类型: object |
| ├─ varietyId               | number      | 必须     |        | 品种ID                                                   |                   |
| ├─ applId                  | number      | 必须     |        | 应用标识                                                 |                   |
| ├─ underlyingId            | string      | 必须     |        | 标的ID                                                   |                   |
| ├─ productType             | number      | 必须     |        | product_type=1时，为虚拟币期货                           |                   |
| ├─ symbol                  | string      | 必须     |        | 品种名称                                                 |                   |
| ├─ chainextCid             | number      | 必须     |        | chainext的指数的cid                                      |                   |
| ├─ priceTick               | number      | 必须     |        | 最小报价单位                                             |                   |
| ├─ lotSize                 | number      | 必须     |        | 最小交易量单位                                           |                   |
| ├─ takerFeeRatio           | number      | 必须     |        | Taker手续费率                                            |                   |
| ├─ makerFeeRatio           | number      | 必须     |        | Maker手续费率                                            |                   |
| ├─ limitMaxLevel           | number      | 必须     |        | 限价委托最大成交档位                                     |                   |
| ├─ marketMaxLevel          | number      | 必须     |        | 市价委托最大成交档位                                     |                   |
| ├─ maxNumOrders            | number      | 必须     |        | 用户最大挂单笔数                                         |                   |
| ├─ priceLimitRate          | number      | 必须     |        | 涨跌停率                                                 |                   |
| ├─ commodityId             | number      | 必须     |        | 商品币种ID                                               |                   |
| ├─ currencyId              | number      | 必须     |        | 货币币种ID                                               |                   |
| ├─ contractType            | number      | 必须     |        | 合约类型，1定期，2永续                                   |                   |
| ├─ deliveryType            | number      | 必须     |        | 交割类型，1现金交割，2实物交割                           |                   |
| ├─ deliveryPeriod          | number      | 必须     |        | 交割周期，0永续1日2周3月4季度                            |                   |
| ├─ deliveryFeeRatio        | number      | 必须     |        | 交割手续费                                               |                   |
| ├─ contractSide            | number      | 必须     |        | 合约方向，1正向，2反向                                   |                   |
| ├─ contractUnit            | number      | 必须     |        | 合约单位                                                 |                   |
| ├─ posiLimit               | number      | 必须     |        | 持仓限额                                                 |                   |
| ├─ orderLimit              | number      | 必须     |        | 委托限额                                                 |                   |
| ├─impactMarginNotional     | number      | 必须     |        | 保证金影响额                                             |                   |
| ├─ minMaintainRate         | number      | 必须     |        | 最小维持保证金率                                         |                   |
| ├─ fairBasisInterval       | number      | 必须     |        | 结算价基差计算间隔，单位秒'                              |                   |
| ├─ clearPriceInterval      | number      | 必须     |        | 结算价计算间隔，单位秒                                   |                   |
| ├─deliveryPriceInterval    | number      | 必须     |        | 交割价计算长度，倒推时间，按秒计                         |                   |
| ├─perpetualPremiumLimit    | number      | 必须     |        | 永续合约溢价指数限制范围                                 |                   |
| ├─perpetualFundingfeeLimit | number      | 必须     |        | 永续合约资金费率限制范围比例                             |                   |
| ├─perpetualSettleFrequency | number      | 必须     |        | 结算频率(一天几次)                                       |                   |
| ├─risklessrateGoods        | number      | 必须     |        | 商品货币日利率                                           |                   |
| ├─risklessrateMoney        | number      | 必须     |        | 计价货币日利率                                           |                   |
| ├─ isAutoList              | number      | 必须     |        | 是否自动加挂：0是 1否                                    |                   |
| ├─ onceListTime            | number      | 必须     |        | 一次性上市时间（即只上市一次）                           |                   |
| ├─regularSettlementTime    | number      | 必须     |        | 定期结算时间（股指合约结算的相对时间，入库时设置）       |                   |
| ├─ createTime              | number      | 必须     |        | 创建时间                                                 |                   |
| ├─ enabled                 | number      | 必须     |        | 是否可用，0：可用，1：不可用                             |                   |
| ├─futureContractList       | object []   | 必须     |        | 合约列表                                                 | item 类型: object |
| ├─ contractId              | number      | 必须     |        | 合约ID                                                   |                   |
| ├─ applId                  | number      | 必须     |        | 应用ID, 1：现货，2：期货                                 |                   |
| ├─ underlyingId            | string      | 必须     |        | 标的ID                                                   |                   |
| ├─ productType             | number      | 必须     |        | product_type=1时，为虚拟币期货                           |                   |
| ├─ symbol                  | string      | 必须     |        | 品种名称                                                 |                   |
| ├─ priceTick               | number      | 必须     |        | 最小报价单位                                             |                   |
| ├─ lotSize                 | number      | 必须     |        | 最小交易量单位                                           |                   |
| ├─ takerFeeRatio           | number      | 必须     |        | Taker手续费率                                            |                   |
| ├─makerFeeRatio            | number      | 必须     |        | Maker手续费率                                            |                   |
| ├─ limitMaxLevel           | number      | 必须     |        | 限价委托最大成交档位                                     |                   |
| ├─marketMaxLevel           | number      | 必须     |        | 市价委托最大成交档位                                     |                   |
| ├─maxNumOrders             | number      | 必须     |        | 用户最大挂单笔数                                         |                   |
| ├─ priceLimitRate          | number      | 必须     |        | 涨跌停率                                                 |                   |
| ├─ commodityId             | number      | 必须     |        | 商品币种ID                                               |                   |
| ├─ currencyId              | number      | 必须     |        | 货币币种ID                                               |                   |
| ├─ contractType            | number      | 必须     |        | 合约类型，1定期，2永续                                   |                   |
| ├─ deliveryType            | number      | 必须     |        | 交割类型，1现金交割，2实物交割                           |                   |
| ├─ deliveryPeriod          | number      | 必须     |        | 交割周期，0永续1日2周3月4季度                            |                   |
| ├─deliveryFeeRatio         | number      | 必须     |        | 交割手续费                                               |                   |
| ├─ contractSide            | number      | 必须     |        | 合约方向，1正向，2反向                                   |                   |
| ├─ contractUnit            | number      | 必须     |        | 合约单位                                                 |                   |
| ├─ posiLimit               | number      | 必须     |        | 持仓限额                                                 |                   |
| ├─ orderLimit              | number      | 必须     |        | 委托限额                                                 |                   |
| ├─impactMarginNotional     | number      | 必须     |        | 保证金影响额                                             |                   |
| ├─minMaintainRate          | number      | 必须     |        | 最小维持保证金率                                         |                   |
| ├─fairBasisInterval        | number      | 必须     |        | 结算价基差计算间隔                                       |                   |
| ├─clearPriceInterval       | number      | 必须     |        | 结算价计算间隔                                           |                   |
| ├─deliveryPriceInterval    | number      | 必须     |        | 交割价计算间隔                                           |                   |
| ├─ createTime              | number      | 必须     |        | 创建时间                                                 |                   |
| ├─ varietyId               | integer     | 必须     |        | 品种ID，标的ID                                           |                   |
| ├─ lastTradeTime           | number      | 必须     |        | 最后交易时间                                             |                   |
| ├─ deliveryTime            | number      | 必须     |        | 最后交割时间                                             |                   |
| ├─ listPrice               | number      | 必须     |        | 挂牌价格                                                 |                   |
| ├─ listTime                | number      | 必须     |        | 上市时间                                                 |                   |
| ├─contractStatus           | number      | 必须     |        | 交易对状态:1集合竞价,2连续交易,3交易暂停,4已摘牌,5未上市 |                   |
| ├─perpetualPremiumLimit    | number,null | 必须     |        | 永续合约溢价指数限制范围                                 |                   |
| ├─perpetualFundingfeeLimit | number,null | 必须     |        | 永续合约资金费率限制范围比例                             |                   |
| ├─perpetualSettleFrequency | number,null | 必须     |        | 结算频率(一天几次)                                       |                   |
| ├─risklessrateGoods        | number,null | 必须     |        | 商品货币日利率                                           |                   |
| ├─risklessrateMoney        | number,null | 必须     |        | 计价货币日利率                                           |                   |
| ├─ isAutoList              | number      | 必须     |        | 是否自动加挂：0是，1否                                   |                   |

## 1.2 查询所有货币信息



### 基本信息

**Path：** /api/v1/common/queryCurrency

**Method：** GET

**接口描述：**

### 请求参数

### 返回数据

| 名称                     | 类型      | 是否必须 | 默认值 | 备注                           | 其他信息          |
| ------------------------ | --------- | -------- | ------ | ------------------------------ | ----------------- |
| result                   | object [] | 非必须   |        |                                | item 类型: object |
| ├─ currencyId            | number    | 非必须   |        | 币种ID                         |                   |
| ├─ symbol                | string    | 非必须   |        | 币种名称                       |                   |
| ├─ nameCn                | string    | 非必须   |        | 币种中文名                     |                   |
| ├─ symbolDesc            | string    | 非必须   |        | 币种简介                       |                   |
| ├─ displayPrecision      | number    | 非必须   |        | 页面显示位数                   |                   |
| ├─minWithdrawQuantity    | number    | 非必须   |        | 最小提现数                     |                   |
| ├─ withdrawFlatFee       | number    | 非必须   |        | 提现手续费                     |                   |
| ├─ auditThreshold        | number    | 非必须   |        | 人工审核阈值                   |                   |
| ├─exeutiveAuditThreshold | number    | 非必须   |        | 高管审核阈值                   |                   |
| ├─ forbidWithdraw        | number    | 非必须   |        | 是否允许提币：0允许、1禁止     |                   |
| ├─forbidCreateAddress    | number    | 非必须   |        | 是否允许生成地址：0允许，1禁止 |                   |
| ├─ forbidRecharge        | number    | 非必须   |        | 是否允许充值：0允许，1禁止     |                   |
| ├─ prefixUrl             | string    | 非必须   |        | 币种浏览器地址                 |                   |
| ├─ enabled               | number    | 非必须   |        | 币状态：0启用，1不启用,2删除   |                   |

## 1.3 获取所有法币



### 基本信息

**Path：** /api/v1/common/exchange/list

**Method：** GET

**接口描述：**

### 请求参数

### 返回数据

| 名称          | 类型      | 是否必须 | 默认值 | 备注                | 其他信息          |
| ------------- | --------- | -------- | ------ | ------------------- | ----------------- |
| result        | object [] | 非必须   |        |                     | item 类型: object |
| ├─ id         | number    | 非必须   |        | 法币ID              |                   |
| ├─ name       | string    | 非必须   |        | 法币名称            |                   |
| ├─ rate       | number    | 非必须   |        | 当前相对于USD的汇率 |                   |
| ├─ updateTime | string    | 非必须   |        | 更新时间            |                   |

## 1.4 获取计价货币的价格



### 基本信息

**Path：** /api/v1/common/exchange/coins

**Method：** GET

**接口描述：**

currencyId: 货币ID
latest: 当前USD价格

### 请求参数

### 返回数据

| 名称          | 类型      | 是否必须 | 默认值 | 备注        | 其他信息          |
| ------------- | --------- | -------- | ------ | ----------- | ----------------- |
| result        | object [] | 非必须   |        |             | item 类型: object |
| ├─ currencyId | number    | 必须     |        | 货币ID      |                   |
| ├─ latest     | number    | 必须     |        | 当前USD价格 |                   |

## 1.5 查询合约交割历史



### 基本信息

**Path：** /api/v1/future/queryContractDeliveryList

**Method：** GET

**接口描述：**

### 请求参数

**Query**

| 参数名称   | 是否必须 | 示例 | 备注                        |
| ---------- | -------- | ---- | --------------------------- |
| type       | 是       |      | 1定期，2永续                |
| pageNum    | 否       |      | 当前页，默认：1             |
| pageSize   | 否       |      | 每页条数，默认：10          |
| contractId | 否       |      | 合约ID                      |
| sort       | 否       |      | 排序类型，只支持：DESC，ASC |

### 返回数据

| 名称                       | 类型      | 是否必须 | 默认值 | 备注                                 | 其他信息          |
| -------------------------- | --------- | -------- | ------ | ------------------------------------ | ----------------- |
| code                       | number    | 非必须   |        | 0：成功，非0：失败                   |                   |
| msg                        | string    | 非必须   |        | 请求回复msg                          |                   |
| data                       | object    | 非必须   |        |                                      |                   |
| ├─ pageNum                 | number    | 非必须   |        | 当前页                               |                   |
| ├─ pageSize                | number    | 非必须   |        | 每页的数量                           |                   |
| ├─ size                    | number    | 非必须   |        | 当前的数量                           |                   |
| ├─ startRow                | number    | 非必须   |        | 当前页面第一个元素在数据库中的行号   |                   |
| ├─ endRow                  | number    | 非必须   |        | 当前页面最后一个元素在数据库中的行号 |                   |
| ├─ total                   | number    | 非必须   |        | 总记录数                             |                   |
| ├─ pages                   | number    | 非必须   |        | 总页数                               |                   |
| ├─ list                    | object [] | 非必须   |        | 结果集                               | item 类型: object |
| ├─ applId                  | number    | 必须     |        | 2 期货                               |                   |
| ├─ contractType            | number    | 必须     |        | 合约类型                             |                   |
| ├─ contractId              | number    | 必须     |        | 合约ID                               |                   |
| ├─perpetualSettleFrequency | number    | 必须     |        | 结算频率(一天几次)                   |                   |
| ├─ symbol                  | string    | 必须     |        | 合约名称                             |                   |
| ├─ deliveryTime            | number    | 必须     |        | 交割时间                             |                   |
| ├─ deliveryQty             | number    | 必须     |        | 交割数量                             |                   |
| ├─ deliveryAmt             | number    | 必须     |        | 交割金额                             |                   |
| ├─ deliveryPrice           | number    | 必须     |        | 交割价格                             |                   |
| ├─ fundingRate             | number    | 必须     |        | 永续合约资金费率                     |                   |
| ├─ prePage                 | number    | 非必须   |        | 前一页                               |                   |
| ├─ nextPage                | number    | 非必须   |        | 下一页                               |                   |
| ├─ isFirstPage             | boolean   | 非必须   |        | 是否为第一页                         |                   |
| ├─ isLastPage              | boolean   | 非必须   |        | 是否为最后一页                       |                   |
| ├─ hasPreviousPage         | boolean   | 非必须   |        | 是否有前一页                         |                   |
| ├─ hasNextPage             | boolean   | 非必须   |        | 是否有下一页                         |                   |
| ├─ navigatePages           | number    | 非必须   |        | 导航页码数                           |                   |
| ├─ navigatepageNums        | number [] | 非必须   |        | 所有导航页号                         | item 类型: number |
| ├─ navigateFirstPage       | number    | 非必须   |        | 导航条上的第一页                     |                   |
| ├─ navigateLastPage        | number    | 非必须   |        | 导航条上的最后一页                   |                   |
| ├─ firstPage               | number    | 非必须   |        | 第一页                               |                   |
| ├─ lastPage                | number    | 非必须   |        | 最后一页                             |                   |

# 2.行情类



## 2.1 查询24小时统计



### 基本信息

**Path：** /api/v1/futureQuot/queryMarketStat

**Method：** GET

**接口描述：**

### 请求参数

**Query**

| 参数名称   | 是否必须 | 示例 | 备注   |
| ---------- | -------- | ---- | ------ |
| currencyId | 是       |      | 货币ID |

### 返回数据

| 名称                | 类型   | 是否必须 | 默认值 | 备注         | 其他信息 |
| ------------------- | ------ | -------- | ------ | ------------ | -------- |
| code                | number | 非必须   |        |              |          |
| msg                 | string | 非必须   |        |              |          |
| data                | object | 非必须   |        |              |          |
| ├─ messageType      | string | 非必须   |        |              |          |
| ├─ currencyId       | number | 非必须   |        | 货币ID       |          |
| ├─ total24hTurnover | string | 非必须   |        | 24小时成交额 |          |
| ├─ total7dTurnover  | string | 非必须   |        | 7天成交额    |          |
| ├─ total30dTurnover | string | 非必须   |        | 30天成交额   |          |

## 2.2 获取K线数据



### 基本信息

**Path：** /api/v1/futureQuot/queryCandlestick

**Method：** GET

**接口描述：**

注意：lines数据结构：List<List>
如：
    [
        [${时间}, ${开市价格}, ${最高价格}, ${最低价格}, ${闭市价格}, ${成交量}]
    ]

range取值：
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





### 请求参数

**Query**

| 参数名称 | 是否必须 | 示例 | 备注     |
| -------- | -------- | ---- | -------- |
| symbol   | 是       |      | 合约ID   |
| range    | 是       |      | 线类型值 |

### 返回数据

| 名称     | 类型     | 是否必须 | 默认值 | 备注 | 其他信息         |
| -------- | -------- | -------- | ------ | ---- | ---------------- |
| success  | boolean  | 非必须   |        |      |                  |
| data     | object   | 非必须   |        |      |                  |
| ├─ lines | array [] | 非必须   |        |      | item 类型: array |

## 2.3 获取单个合约行情快照



### 基本信息

**Path：** /api/v1/futureQuot/querySnapshot

**Method：** GET

**接口描述：**

注意：
    bids与asks结构：List<List>
     如
        [
            [${价格}, ${数量}]
        ]

### 请求参数

**Query**

| 参数名称   | 是否必须 | 示例 | 备注   |
| ---------- | -------- | ---- | ------ |
| contractId | 是       |      | 合约ID |

### 返回数据

| 名称       | 类型      | 是否必须 | 默认值 | 备注                                | 其他信息          |
| ---------- | --------- | -------- | ------ | ----------------------------------- | ----------------- |
| contractId | number    | 非必须   |        | 合约ID                              |                   |
| result     | object    | 非必须   |        |                                     |                   |
| ├─ mt      | number    | 非必须   |        | 消息类型：messageType               |                   |
| ├─ ai      | number    | 非必须   |        | 应用ID：applId                      |                   |
| ├─ ci      | number    | 非必须   |        | 合约ID：contractId                  |                   |
| ├─ sb      | string    | 非必须   |        | 合约名称：symbol                    |                   |
| ├─ td      | number    | 非必须   |        | 交易日期：tradeDate                 |                   |
| ├─ te      | number    | 非必须   |        | 交易时间：time                      |                   |
| ├─ lp      | string    | 非必须   |        | 最新价格：lastPrice                 |                   |
| ├─ mq      | string    | 非必须   |        | 成交量：matchQty                    |                   |
| ├─ nt      | number    | 非必须   |        | 成交比数：numTrades                 |                   |
| ├─ op      | string    | 非必须   |        | 开盘价：openPrice                   |                   |
| ├─ ph      | string    | 非必须   |        | 最高价：priceHigh                   |                   |
| ├─ pl      | string    | 非必须   |        | 最低价：priceLow                    |                   |
| ├─ hph     | string    | 非必须   |        | 历史最高价：historyPriceHigh        |                   |
| ├─ hpl     | string    | 非必须   |        | 历史最低价：historyPriceLow         |                   |
| ├─ tt      | string    | 非必须   |        | 当日成交额：totalTurnover           |                   |
| ├─ tv      | string    | 非必须   |        | 当日成交量：totalVolume             |                   |
| ├─ tbv     | string    | 非必须   |        | 买总委托数：totalBidVol             |                   |
| ├─ tav     | string    | 非必须   |        | 卖总委托数：totalAskVol             |                   |
| ├─ pp      | string    | 非必须   |        | 上一交易日收盘价：prevPrice         |                   |
| ├─ cp      | string    | 非必须   |        | 标记价格，结算价：clearPrice        |                   |
| ├─ pv      | string    | 非必须   |        | 总持仓量：posiVol                   |                   |
| ├─ pcr     | string    | 非必须   |        | 当日涨跌幅度：priceChangeRadio      |                   |
| ├─ pc      | string    | 非必须   |        | 当日涨跌：priceChange               |                   |
| ├─ lui     | number    | 非必须   |        | 行情序号：lastUpdateId              |                   |
| ├─ cs      | number    | 非必须   |        | 交易对状态：contractStatus          |                   |
| ├─ dp      | string    | 非必须   |        | 交割价格：deliveryPrice             |                   |
| ├─ fr      | string    | 非必须   |        | 资金费率：fundingRate               |                   |
| ├─ pfr     | string    | 非必须   |        | 预测资金费率：predictionFundingRate |                   |
| ├─ pi      | string    | 非必须   |        | 溢价指数：premiumIndex              |                   |
| ├─ ppi     | string    | 非必须   |        | 预溢价指数：predictionPremiumIndex  |                   |
| ├─ fb      | string    | 非必须   |        | 合理基差：fairBasis                 |                   |
| ├─ ts      | number    | 非必须   |        | 交易信号，1=买，2=卖：tradingsignal |                   |
| ├─ sl      | number    | 非必须   |        | 1~100,交易信号强度 ：signalLevel    |                   |
| ├─ ip      | string    | 非必须   |        | 标的指数价格：indexPrice            |                   |
| ├─ bids    | string [] | 非必须   |        | 买档位                              | item 类型: string |
| ├─ asks    | string [] | 非必须   |        | 卖档位                              | item 类型: string |

## 2.4 获取所有行情快照



### 基本信息

**Path：** /api/v1/futureQuot/queryIndicatorList

**Method：** GET

**接口描述：**

具体各字段含义，请参考 2.3 - /futureQuot/querySnapshot 接口

### 请求参数

### 返回数据

| 名称   | 类型      | 是否必须 | 默认值 | 备注 | 其他信息          |
| ------ | --------- | -------- | ------ | ---- | ----------------- |
|        | object [] | 非必须   |        |      | item 类型: object |
| ├─ mt  | number    | 必须     |        |      |                   |
| ├─ ai  | number    | 必须     |        |      |                   |
| ├─ ci  | number    | 必须     |        |      |                   |
| ├─ sb  | string    | 必须     |        |      |                   |
| ├─ td  | number    | 必须     |        |      |                   |
| ├─ te  | number    | 必须     |        |      |                   |
| ├─ lp  | string    | 必须     |        |      |                   |
| ├─ mq  | string    | 必须     |        |      |                   |
| ├─ nt  | number    | 必须     |        |      |                   |
| ├─ op  | string    | 必须     |        |      |                   |
| ├─ ph  | string    | 必须     |        |      |                   |
| ├─ pl  | string    | 必须     |        |      |                   |
| ├─ hph | string    | 必须     |        |      |                   |
| ├─ hpl | string    | 必须     |        |      |                   |
| ├─ tt  | string    | 必须     |        |      |                   |
| ├─ tv  | string    | 必须     |        |      |                   |
| ├─ tbv | string    | 必须     |        |      |                   |
| ├─ tav | string    | 必须     |        |      |                   |
| ├─ pp  | string    | 必须     |        |      |                   |
| ├─ cp  | string    | 必须     |        |      |                   |
| ├─ pv  | string    | 必须     |        |      |                   |
| ├─ pcr | string    | 必须     |        |      |                   |
| ├─ pc  | string    | 必须     |        |      |                   |
| ├─ lui | number    | 必须     |        |      |                   |
| ├─ cs  | number    | 必须     |        |      |                   |
| ├─ dp  | string    | 必须     |        |      |                   |
| ├─ fr  | string    | 必须     |        |      |                   |
| ├─ pfr | string    | 必须     |        |      |                   |
| ├─ pi  | string    | 必须     |        |      |                   |
| ├─ ppi | string    | 必须     |        |      |                   |
| ├─ fb  | string    | 必须     |        |      |                   |
| ├─ ts  | number    | 必须     |        |      |                   |
| ├─ sl  | number    | 必须     |        |      |                   |
| ├─ ip  | string    | 必须     |        |      |                   |

## 2.5 获取逐笔成交



### 基本信息

**Path：** /api/v1/futureQuot/queryTickTrade

**Method：** GET

**接口描述：**

注意：
    trades的结构为：List<List>
   如：
    [
        [${时间戳}, ${成交价格}, ${成交数量}, ${方向}]
    ]

### 请求参数

**Query**

| 参数名称   | 是否必须 | 示例 | 备注   |
| ---------- | -------- | ---- | ------ |
| contractId | 是       |      | 合约ID |

### 返回数据

| 名称      | 类型     | 是否必须 | 默认值 | 备注         | 其他信息         |
| --------- | -------- | -------- | ------ | ------------ | ---------------- |
| success   | boolean  | 非必须   |        |              |                  |
| data      | object   | 非必须   |        | 数据集合     |                  |
| ├─ trades | array [] | 非必须   |        | 最近50笔成交 | item 类型: array |

# 3.用户类



## 3.1 获取用户基本信息



### 基本信息

**Path：** /api/v1/future/user

**Method：** GET

**接口描述：**

功能 ：获取已注册用户的相关信息

### 请求参数

**Headers**

| 参数名称     | 参数值           | 是否必须 | 示例 | 备注   |
| ------------ | ---------------- | -------- | ---- | ------ |
| Content-Type | application/json | 是       |      |        |
| signature    |                  | 是       |      | 签名   |
| apiKey       |                  | 是       |      | 公钥   |
| apiExpires   |                  | 是       |      | 时间戳 |

### 返回数据

| 名称                 | 类型   | 是否必须 | 默认值 | 备注                                       | 其他信息 |
| -------------------- | ------ | -------- | ------ | ------------------------------------------ | -------- |
| message_type         | number | 必须     |        |                                            |          |
| data                 | object | 必须     |        |                                            |          |
| ├─ id                | number | 必须     |        | 用户id                                     |          |
| ├─ username          | string | 必须     |        | 用户名                                     |          |
| ├─ email             | string | 必须     |        | 用户邮件                                   |          |
| ├─ enabled           | number | 必须     |        | 用户状态（0：正常，1:锁定，2:注销）        |          |
| ├─ createTime        | string | 必须     |        | 用户注册时间                               |          |
| ├─ lastLogin         | null   | 必须     |        | 用户最近登录时间                           |          |
| ├─ phone             | string | 必须     |        | 手机号                                     |          |
| ├─ googleCode        | string | 必须     |        | 谷歌验证码                                 |          |
| ├─ tradePassword     | null   | 必须     |        | 交易密码                                   |          |
| ├─ loginPassGrade    | number | 必须     |        | 登录密码等级                               |          |
| ├─ googleValidate    | null   | 必须     |        | 是否开启谷歌验证码：1开启，2关闭           |          |
| ├─avoidFishingCode   | null   | 必须     |        | 防钓鱼码                                   |          |
| ├─ tradeSafe         | null   | 必须     |        | 1每次输入密码，2每两小时输入一次 3，不输入 |          |
| ├─ lastTradeTime     | null   | 必须     |        | 上次输入交易密码时间                       |          |
| ├─ createTimeStart   | null   | 必须     |        |                                            |          |
| ├─ createTimeEnd     | null   | 必须     |        |                                            |          |
| ├─certificationGrade | null   | 必须     |        | 认证等级                                   |          |

## 3.2 获取用户资产信息



### 基本信息

**Path：** /api/v1/future/margin

**Method：** GET

**接口描述：**

功能 ：查询用户的资产信息（包括 账户 各个币种的 可用余额、冻结金额、保证金等数据）

### 返回数据

| 名称                | 类型      | 是否必须 | 默认值 | 备注               | 其他信息          |
| ------------------- | --------- | -------- | ------ | ------------------ | ----------------- |
| code                | number    | 非必须   |        | 0：成功，非0：失败 |                   |
| msg                 | string    | 非必须   |        | 消息msg            |                   |
| data                | object [] | 非必须   |        | 数据集合           | item 类型: object |
| ├─ currencyId       | string    | 非必须   |        | 货币ID             |                   |
| ├─ totalBalance     | string    | 非必须   |        | 总资产             |                   |
| ├─ available        | string    | 非必须   |        | 可用资产           |                   |
| ├─ frozenForTrade   | string    | 非必须   |        | 委托冻结资产       |                   |
| ├─ initMargin       | string    | 非必须   |        | 已占用保证金       |                   |
| ├─ frozenInitMargin | string    | 非必须   |        | 委托冻结保证金     |                   |
| ├─ closeProfitLoss  | string    | 非必须   |        | 已实现盈亏         |                   |

## 3.3 获取用户持仓信息



### 基本信息

**Path：** /api/v1/future/position

**Method：** GET

**接口描述：**

功能 ：获取用户当前持仓的数据

### 返回数据

| 名称                  | 类型      | 是否必须 | 默认值 | 备注               | 其他信息          |
| --------------------- | --------- | -------- | ------ | ------------------ | ----------------- |
| code                  | number    | 非必须   |        | 0：成功，非0：失败 |                   |
| msg                   | number    | 非必须   |        | 消息msg            |                   |
| data                  | object [] | 非必须   |        | 数据集合           | item 类型: object |
| ├─ contractId         | number    | 非必须   |        | 合约对ID           |                   |
| ├─ posiQty            | string    | 非必须   |        | 开仓数量           |                   |
| ├─ openAmt            | string    | 非必须   |        | 开仓金额           |                   |
| ├─ initMargin         | string    | 非必须   |        | 初始保证金         |                   |
| ├─ posiStatus         | number    | 非必须   |        | 持仓状态           |                   |
| ├─ marginType         | number    | 非必须   |        | 保证金类型         |                   |
| ├─ closeProfitLoss    | string    | 非必须   |        | 已实现盈亏         |                   |
| ├─ initMargiRate      | string    | 非必须   |        | 初始保证金率       |                   |
| ├─ maintainMarginRate | string    | 非必须   |        | 维持保证金率       |                   |
| ├─ frozenCloseQty     | string    | 非必须   |        | 冻结平仓数量       |                   |
| ├─ frozenOpenQty      | string    | 非必须   |        | 冻结开仓数量       |                   |
| ├─ posiSide           | number    | 非必须   |        | 持仓方向0对冲1多头-1空头       |                   |


## 3.4 获取用户合约保证金梯度



### 基本信息

**Path：** /api/v1/future/queryVarietyMargin

**Method：** GET

**接口描述：**

### 请求参数

**Query**

| 参数名称   | 是否必须 | 示例    | 备注   |
| ---------- | -------- | ------- | ------ |
| varietyId  | 是       | Integer | 品种id |
| contractId | 是       | Integer | 交易对 |

### 返回数据

| 名称            | 类型      | 是否必须 | 默认值 | 备注               | 其他信息          |
| --------------- | --------- | -------- | ------ | ------------------ | ----------------- |
| code            | integer   | 必须     |        | 0：成功，非：0失败 |                   |
| msg             | string    | 必须     |        | 消息msg            |                   |
| data            | object [] | 必须     |        | 数据集合           | item 类型: object |
| ├─ applId       | integer   | 必须     |        | 应用标识           |                   |
| ├─ varietyId    | integer   | 必须     |        | 品种ID             |                   |
| ├─ contractId   | integer   | 非必须   |        | 合约ID             |                   |
| ├─ posiQty      | string    | 必须     |        | 持仓数量           |                   |
| ├─ initRate     | string    | 必须     |        | 初始保证金率       |                   |
| ├─ maintainRate | string    | 必须     |        | 维持保证金率       |                   |

# 4.交易类

## 4.1 合约下单

### 基本信息

**Path：** /api/v1/future/order

**Method：** POST

**接口描述：**

功能：期货合约 - 委托下单（包括空单、多单、平仓单）
注意：
  交易对ID：contractId，交易对名称：symbol 下单时，必须至少传递一个，当两个都传递时，用contractId
  客户端委托ID：clientOrderId，不传递时，内部会自动生成

### 请求参数

**Headers**

| 参数名称     | 参数值           | 是否必须 | 示例 | 备注 |
| ------------ | ---------------- | -------- | ---- | ---- |
| Content-Type | application/json | 是       |      |      |

**Body**

| 名称           | 类型    | 是否必须 | 默认值 | 备注                                             | 其他信息   |
| -------------- | ------- | -------- | ------ | ------------------------------------------------ | ---------- |
| contractId     | integer | 非必须   |        | 交易对ID                                         |            |
| symbol         | string  | 非必须   |        | 交易对名称                                       |            |
| clientOrderId  | string  | 非必须   |        | 客户端委托ID                                     |            |
| side           | integer | 必须     |        | 合约委托方向（买1，卖-1）                        | 枚举: -1,1 |
| price          | string  | 非必须   |        | 合约委托价格（order_type等于3（市价）时非必填 ） |            |
| quantity       | string  | 必须     |        | 合约委托数量                                     |            |
| orderType      | integer | 必须     |        | 合约委托类型（1（限价），3（市价） ）            |            |
| positionEffect | number  | 必须     |        | 开平标志（开仓1，平仓2）                         |            |
| marginType     | number  | 必须     |        | 仓位模式（全仓1，逐仓2）                         |            |
| marginRate     | string  | 必须     |        | 保证金率（全仓: >=0，0：取合约配置值，逐仓: >0    |            |

### 返回数据

| 名称 | 类型   | 是否必须 | 默认值 | 备注               | 其他信息 |
| ---- | ------ | -------- | ------ | ------------------ | -------- |
| code | number | 非必须   |        | 0：成功，非0：失败 |          |
| msg  | string | 非必须   |        | 消息msg            |          |
| data | object | 非必须   |        | 消息结合           |          |

## 4.2 合约批量下单

### 基本信息

**Path：** /api/v1/future/orders

**Method：** POST

**接口描述：**

功能：期货合约 - 批量委托下单 （下多笔委托订单）
**注意**：
  交易对ID：contractId，交易对名称：symbol 下单时，必须至少传递一个，当两个都传递时，用contractId
  客户端委托ID：clientOrderId，不传递时，内部会自动生成
**请求响应数据-data格式如下所示**：
    "data":{"succ":[[${客户端委托ID]],"reject":[[${客户端委托ID},${失败码}]]}

### 请求参数

**Headers**

| 参数名称     | 参数值           | 是否必须 | 示例 | 备注 |
| ------------ | ---------------- | -------- | ---- | ---- |
| Content-Type | application/json | 是       |      |      |

**Body**

| 名称             | 类型      | 是否必须 | 默认值 | 备注                           | 其他信息          |
| ---------------- | --------- | -------- | ------ | ------------------------------ | ----------------- |
| orders           | object [] | 非必须   |        |                                | item 类型: object |
| ├─ side          | number    | 必须     |        | 买卖方向，1买-1卖              |                   |
| ├─ clientOrderId | string    | 非必须   |        | 客户端委托ID                   |                   |
| ├─ orderPrice    | string    | 非必须   |        | 委托价格                       |                   |
| ├─ orderQty      | string    | 必须     |        | 委托数量                       |                   |
| ├─ orderType     | number    | 必须     |        | 委托类型：1（限价），3（市价） |                   |
| ├─ contractId    | number    | 必须     |        | 合约ID                         |                   |
| ├─ symbol        | string    | 非必须   |        | 合约名称                       |                   |
| ├─positionEffect | number    | 必须     |        | 开平标志，1开仓，2平仓         |                   |
| ├─ marginType    | number    | 必须     |        | 保证金类型，1全仓，2逐仓       |                   |
| ├─ initRate      | string    | 非必须   |        | 保证金率，全仓：>=0，0：取合约配置值，逐仓：>0    |                   |

### 返回数据

| 名称      | 类型     | 是否必须 | 默认值 | 备注         | 其他信息         |
| --------- | -------- | -------- | ------ | ------------ | ---------------- |
| code      | number   | 非必须   |        |              |                  |
| msg       | string   | 非必须   |        |              |                  |
| data      | object   | 非必须   |        |              |                  |
| ├─ succ   | array [] | 非必须   |        | 下单成功列表 | item 类型: array |
| ├─ reject | array [] | 非必须   |        | 下单失败列表 | item 类型: array |

## 4.3 合约撤单



### 基本信息

**Path：** /api/v1/future/order

**Method：** DELETE

**接口描述：**

功能 ：期货合约 - 单笔撤消 未成交的委托单

### 请求参数

**Query**

| 参数名称 | 是否必须 | 示例                                          | 备注                                                         |
| -------- | -------- | --------------------------------------------- | ------------------------------------------------------------ |
| filter   | 是       | {"contractId":1,"originalOrderId":"12345677"} | UrlEncode 解码后为：{"contractId":1,"originalOrderId":"12345677"} contractId：合约ID originalOrderId:原始委托号 |

**Body**

| 名称 | 类型 | 是否必须 | 默认值 | 备注 | 其他信息 |
| ---- | ---- | -------- | ------ | ---- | -------- |
|      |      |          |        |      |          |

### 返回数据

| 名称 | 类型   | 是否必须 | 默认值 | 备注               | 其他信息 |
| ---- | ------ | -------- | ------ | ------------------ | -------- |
| code | number | 非必须   |        | 0：成功，非0：失败 |          |
| msg  | string | 非必须   |        | 消息msg            |          |

## 4.4 合约批量撤单



### 基本信息

**Path：** /api/v1/future/orders

**Method：** DELETE

**接口描述：**

功能 ：期货合约 - 批量撤消 多笔未成交的委托单

### 请求参数

**Body**

| 名称               | 类型      | 是否必须 | 默认值 | 备注       | 其他信息          |
| ------------------ | --------- | -------- | ------ | ---------- | ----------------- |
| cancels            | object [] | 必须   |        |            | item 类型: object |
| ├─ contractId      | number    | 必须   |        | 合约ID     |                   |
| ├─ originalOrderId | string    | 必须   |        | 原始委托号 |                   |
| ├─ clientOrderId   | string    | 非必须   |        | 客户端委托号，不传服务端会生成 |                   |

### 返回数据

| 名称 | 类型    | 是否必须 | 默认值 | 备注              | 其他信息 |
| ---- | ------- | -------- | ------ | ----------------- | -------- |
| code | integer | 非必须   |        | 0:成功，非0：失败 |          |

## 4.5 合约撤消所有订单



### 基本信息

**Path：** /api/v1/future/order/all

**Method：** DELETE

**接口描述：**

功能 ：期货合约 - 撤消 所有未成交的委托单

### 返回数据

| 名称 | 类型   | 是否必须 | 默认值 | 备注               | 其他信息 |
| ---- | ------ | -------- | ------ | ------------------ | -------- |
| code | number | 非必须   |        | 0：成功，非0：失败 |          |
| msg  | string | 非必须   |        | 消息msg            |          |
| data | string | 非必须   |        | 数据集合           |          |

## 4.6 获取订单信息



### 基本信息

**Path：** /api/v1/future/order

**Method：** GET

**接口描述：**

功能 ：期货合约 - 查询订单的相关信息

**Query**

| 参数名称 | 是否必须 | 示例                            | 备注                                                         |
| -------- | -------- | ------------------------------- | ------------------------------------------------------------ |
| filter   | 是       | %7B%22orderId%22%3A%2211548740843018185%22%2C%22contractId%22%3A10000%7D | urlencode  后等于 {"orderId":"11548740843018185","contractId":10000} |

### 返回数据

| 名称             | 类型   | 是否必须 | 默认值 | 备注                                                         | 其他信息 |
| ---------------- | ------ | -------- | ------ | ------------------------------------------------------------ | -------- |
| code             | number | 非必须   |        | 0：成功，非0：失败                                           |          |
| msg              | string | 非必须   |        | 消息msg                                                      |          |
| data             | object | 非必须   |        | 数据集合                                                     |          |
| ├─applId         | number | 非必须   |        | 应用标识                                                     |          |
| ├─contractId     | number | 非必须   |        | 交易对ID                                                     |          |
| ├─accountId      | number | 非必须   |        | 账户ID                                                       |          |
| ├─clOrderId      | string | 非必须   |        | 客户订单编号                                                 |          |
| ├─ side          | number | 非必须   |        | 委托方向（1：买，-1：卖）                                    |          |
| ├─orderPrice     | string | 非必须   |        | 委托价格                                                     |          |
| ├─orderQty       | string | 非必须   |        | 委托数量                                                     |          |
| ├─orderId        | string | 非必须   |        | 委托订单编号                                                 |          |
| ├─orderTime      | number | 非必须   |        | 委托时间                                                     |          |
| ├─orderStatus    | number | 非必须   |        | 委托状态（0未申报，1正在申报，2已申报未成交，3部分成交，4全部成交，5部成部撤,6全部撤单，7撤单中，8失效） |          |
| ├─matchQty       | string | 非必须   |        | 成交数量                                                     |          |
| ├─matchAmt       | string | 非必须   |        | 成交金额                                                     |          |
| ├─cancelQty      | string | 非必须   |        | 撤单数量                                                     |          |
| ├─matchTime      | number | 非必须   |        | 成交时间                                                     |          |
| ├─orderType      | number | 非必须   |        | 委托类型（1：限价，3：市价）                                 |          |
| ├─timeInForce    | number | 非必须   |        | 订单有效时期类型：当日有效、GTC、IOC，默认1， 1, "可成交价格全部成交"；2, "立即成交剩余撤销" |          |
| ├─positionEffect | number | 非必须   |        | 开平标志（1开仓 2平仓）                                      |          |
| ├─marginType     | number | 非必须   |        | 仓位模式（1、全仓，2：逐仓）                                 |          |
| ├─fcOrderId      | string | 非必须   |        | 强平委托号，非空时为强平委托                                 |          |

## 4.7 获取当前订单列表



### 基本信息

**Path：** /api/v1/future/queryActiveOrder

**Method：** GET

**接口描述：**

功能 ：期货合约 - 查询 当前订单的列表数据
### 请求参数

**Query**

| 参数名称   | 是否必须 | 示例 | 备注              |
| ---------- | -------- | ---- | ----------------- |
| contractId | 是       |      | 合约ID            |

### 返回数据

| 名称             | 类型      | 是否必须 | 默认值 | 备注                                                         | 其他信息          |
| ---------------- | --------- | -------- | ------ | ------------------------------------------------------------ | ----------------- |
| code             | number    | 非必须   |        | 0：成功，非0：失败                                           |                   |
| msg              | string    | 非必须   |        | 消息msg                                                      |                   |
| data             | object [] | 非必须   |        | 数据集合                                                     | item 类型: object |
| ├─applId         | number    | 非必须   |        | 应用标识                                                     |                   |
| ├─contractId     | number    | 非必须   |        | 交易对ID                                                     |                   |
| ├─accountId      | number    | 非必须   |        | 账户ID                                                       |                   |
| ├─clOrderId      | string    | 非必须   |        | 客户订单编号                                                 |                   |
| ├─ side          | number    | 非必须   |        | 委托方向（1：买，-1：卖）                                    |                   |
| ├─orderPrice     | string    | 非必须   |        | 委托价格                                                     |                   |
| ├─orderQty       | string    | 非必须   |        | 委托数量                                                     |                   |
| ├─orderId        | string    | 非必须   |        | 委托订单编号                                                 |                   |
| ├─orderTime      | number    | 非必须   |        | 委托时间                                                     |                   |
| ├─orderStatus    | number    | 非必须   |        | 委托状态（0未申报，1正在申报，2已申报未成交，3部分成交，4全部成交，5部成部撤,6全部撤单，7撤单中，8失效） |                   |
| ├─matchQty       | string    | 非必须   |        | 成交数量                                                     |                   |
| ├─matchAmt       | string    | 非必须   |        | 成交金额                                                     |                   |
| ├─cancelQty      | string    | 非必须   |        | 撤单数量                                                     |                   |
| ├─matchTime      | number    | 非必须   |        | 成交时间                                                     |                   |
| ├─orderType      | number    | 非必须   |        | 委托类型（1：限价，3：市价）                                 |                   |
| ├─timeInForce    | number    | 非必须   |        | 订单有效时期类型：当日有效、GTC、IOC，默认1， 1, "可成交价格全部成交"；2, "立即成交剩余撤销" |                   |
| ├─positionEffect | number    | 非必须   |        | 开平标志（1开仓 2平仓）                                      |                   |
| ├─marginType     | number    | 非必须   |        | 仓位模式（1、全仓，2：逐仓）                                 |                   |
| ├─fcOrderId      | string    | 非必须   |        | 强平委托号，非空时为强平委托                                 |                   |
| ├─deltaPrice     | string    | 非必须   |        | 标记价格与委托价格价差                                      |                   |
| ├─frozenPrice    | string    | 非必须   |        | 资金计算价格                                               |                   |


## 4.8 获取历史委托

### 基本信息

**Path：** /api/v1/future/queryHisOrder

**Method：** GET

**接口描述：**

### 请求参数

**Query**

| 参数名称   | 是否必须 | 示例 | 备注              |
| ---------- | -------- | ---- | ----------------- |
| contractId | 否       |      | 合约ID            |
| pageSize   | 否       |      | 默认：100，<=1000 |

### 返回数据

| 名称                 | 类型      | 是否必须 | 默认值 | 备注                                                         | 其他信息          |
| -------------------- | --------- | -------- | ------ | ------------------------------------------------------------ | ----------------- |
| code                 | number    | 非必须   |        |                                                              |                   |
| msg                  | string    | 非必须   |        |                                                              |                   |
| data                 | object [] | 非必须   |        |                                                              | item 类型: object |
| ├─ appl_id           | integer   | 非必须   |        | 应用ID,期货：2                                               |                   |
| ├─ timestamp         | number    | 非必须   |        | 委托时间戳                                                   |                   |
| ├─ user_id           | integer   | 非必须   |        | 用户ID                                                       |                   |
| ├─ contract_id       | integer   | 非必须   |        | 合约ID                                                       |                   |
| ├─ uuid              | string    | 非必须   |        | 委托编号                                                     |                   |
| ├─ side              | integer   | 非必须   |        | 买卖方向                                                     |                   |
| ├─ price             | string    | 非必须   |        | 委托价                                                       |                   |
| ├─ quantity          | string    | 非必须   |        | 委托量                                                       |                   |
| ├─ order_type        | null      | 非必须   |        | 委托类型，1-限价；3-市价                                     |                   |
| ├─ order_sub_type    | null      | 非必须   |        | 委托子类型, 1-被动委托；2-最近价触发条件委托；3-指数触发     |                   |
| ├─ time_in_force     | null      | 非必须   |        | 订单有效时期类型，0-默认值； 1-取消前有效                    |                   |
| ├─ minimal_quantity  | null      | 非必须   |        | 最小成交量                                                   |                   |
| ├─ stop_price        | null      | 非必须   |        | 止损止盈价                                                   |                   |
| ├─ stop_condition    | null      | 非必须   |        | 止损止盈标志，0-默认；1-止盈；2-止损                         |                   |
| ├─ order_status      | null      | 非必须   |        | 委托状态 0-未申报,1-正在申报,2-已申报未成交,3-部分成交,4-全部成交,5-部分撤单,6-全部撤单,7-撤单中,8-失效,11-缓存高于条件的委托,12-缓存低于条件的委托 |                   |
| ├─ maker_fee_ratio   | null      | 非必须   |        | Maker手续费率                                                |                   |
| ├─ taker_fee_ratio   | null      | 非必须   |        | Taker手续费率                                                |                   |
| ├─ cl_order_id       | null      | 非必须   |        | 客户订单编号                                                 |                   |
| ├─ filled_currency   | null      | 非必须   |        | 成交金额                                                     |                   |
| ├─ filled_quantity   | null      | 非必须   |        | 成交量                                                       |                   |
| ├─ canceled_quantity | null      | 非必须   |        | 撤单数量                                                     |                   |
| ├─ match_time        | null      | 非必须   |        | 成交时间                                                     |                   |
| ├─ position_effect   | null      | 非必须   |        | 开平标志，1开仓，2平仓                                       |                   |
| ├─ margin_type       | null      | 非必须   |        | 保证金类型，1全仓，2逐仓                                     |                   |
| ├─ margin_rate       | null      | 非必须   |        | 保证金率                                                     |                   |
| ├─ fc_order_id       | null      | 非必须   |        | 强平委托号                                                   |                   |
| ├─ delta_price       | null      | 非必须   |        | 标记价与委托价之差                                           |                   |
| ├─ frozen_price      | null      | 非必须   |        | 资金计算价格       

## 4.9 切换仓位模式



### 基本信息

**Path：** /api/v1/future/position/isolate

**Method：** POST

**接口描述：**

功能 ：期货合约 - 已成交的订单切换仓位模式（逐仓 切换 全仓、全仓切换逐仓）

**Body**

| 名称           | 类型   | 是否必须 | 默认值 | 备注                     | 其他信息 |
| -------------- | ------ | -------- | ------ | ------------------------ | -------- |
| contractId     | string | 必须     |        | 交易对ID                 |          |
| initMarginRate | string | 非必须   |        | 初始保证金率0<${}<=1     |          |
| marginType     | string | 必须     |        | 保证金类型，全仓1，逐仓2 |          |
| posiSide       | integer | 非必须  |        | 0：对冲，1：多头，-1：空头。默认：0 |          |

### 返回数据

| 名称 | 类型   | 是否必须 | 默认值 | 备注               | 其他信息 |
| ---- | ------ | -------- | ------ | ------------------ | -------- |
| code | number | 非必须   |        | 0：成功，非0：失败 |          |
| msg  | string | 非必须   |        | 消息msg            |          |

## 4.10 调整保证金率



### 基本信息

**Path：** /api/v1/future/position/transferMargin

**Method：** POST

**接口描述：**

功能：调整保证金率，即调整杠杆倍数

**Body**

| 名称       | 类型    | 是否必须 | 默认值 | 备注                                       | 其他信息 |
| ---------- | ------- | -------- | ------ | ------------------------------------------ | -------- |
| contractId | integer | 非必须   |        | 合约ID                                     |          |
| margin     | string  | 非必须   |        | 保证金（>0 : 增加保证金，<0 ：减少保证金） |          |
| posiSide   | integer  | 非必须   |        | 0：对冲，1：多头，-1：空头。默认：0 |          |

### 返回数据

| 名称 | 类型   | 是否必须 | 默认值 | 备注              | 其他信息 |
| ---- | ------ | -------- | ------ | ----------------- | -------- |
| code | number | 非必须   |        | 0:成功，非0：失败 |          |
| msg  | string | 非必须   |        | 消息msg           |          |
| data | object | 非必须   |        |                   |          |

## 4.11 获取强减队列



### 基本信息

**Path：** /api/v1/future/queryForceLower

**Method：** GET

### 返回数据

| 名称          | 类型      | 是否必须 | 默认值 | 备注                           | 其他信息          |
| ------------- | --------- | -------- | ------ | ------------------------------ | ----------------- |
| code          | number    | 非必须   |        | 0：成功，非0：失败             |                   |
| msg           | string    | 非必须   |        | 消息msg                        |                   |
| data          | object [] | 非必须   |        | 数据集合                       | item 类型: object |
| ├─ contractId | number    | 非必须   |        | 合约ID                         |                   |
| ├─ rank       | number    | 非必须   |        | 强减排名，值越小强减可能性越大 |                   |

## 4.12 获取当前成交



### 基本信息

**Path：** /api/v1/future/queryHisMatch

**Method：** GET

**接口描述：**

### 请求参数

**Query**

| 参数名称   | 是否必须 | 示例 | 备注                        |
| ---------- | -------- | ---- | --------------------------- |
| contractId | 否       |      | 合约ID                      |
| pageSize   | 否       |      | 数据条数，默认：100，<=1000 |

### 返回数据

| 名称                   | 类型      | 是否必须 | 默认值 | 备注                                                     | 其他信息          |
| ---------------------- | --------- | -------- | ------ | -------------------------------------------------------- | ----------------- |
| code                   | number    | 非必须   |        |                                                          |                   |
| msg                    | string    | 非必须   |        |                                                          |                   |
| data                   | object [] | 非必须   |        |                                                          | item 类型: object |
| ├─ appl_id             | null      | 非必须   |        | 期货ID,2                                                 |                   |
| ├─ match_time          | null      | 非必须   |        | 成交时间                                                 |                   |
| ├─ contract_id         | null      | 非必须   |        | 交易对ID、合约号                                         |                   |
| ├─ exec_id             | null      | 非必须   |        | 成交编号                                                 |                   |
| ├─ bid_user_id         | null      | 非必须   |        | 买方账号ID                                               |                   |
| ├─ ask_user_id         | null      | 非必须   |        | 卖方账号ID                                               |                   |
| ├─ bid_order_id        | null      | 非必须   |        | 买方委托号                                               |                   |
| ├─ ask_order_id        | null      | 非必须   |        | 卖方委托号                                               |                   |
| ├─ match_price         | null      | 非必须   |        | 成交价                                                   |                   |
| ├─ match_qty           | null      | 非必须   |        | 成交数量                                                 |                   |
| ├─ match_amt           | null      | 非必须   |        | 成交金额                                                 |                   |
| ├─ bid_fee             | null      | 非必须   |        | 买方手续费                                               |                   |
| ├─ ask_fee             | null      | 非必须   |        | 卖方手续费                                               |                   |
| ├─ taker_side          | null      | 非必须   |        | Taker方向                                                |                   |
| ├─ side                | null      | 非必须   |        | 买卖方向                                                 |                   |
| ├─ update_time         | null      | 非必须   |        | 最近更新时间                                             |                   |
| ├─ bid_position_effect | null      | 非必须   |        | 买方开平标志                                             |                   |
| ├─ ask_position_effect | null      | 非必须   |        | 卖方开平标志                                             |                   |
| ├─ bid_margin_type     | null      | 非必须   |        | 买方保证金类型                                           |                   |
| ├─ ask_margin_type     | null      | 非必须   |        | 卖方保证金类型                                           |                   |
| ├─ bid_init_rate       | null      | 非必须   |        | 买方初始保证金率                                         |                   |
| ├─ ask_init_rate       | null      | 非必须   |        | 卖方初始保证金率                                         |                   |
| ├─ bid_match_type      | null      | 非必须   |        | 买方成交类型：0普通成交1强平成交2强减成交（破产方）3强减 |                   |
| ├─ ask_match_type      | null      | 非必须   |        | 卖方成交类型：0普通成交1强平成交2强减成交（破产方）3强减 |                   |
| ├─ bid_pnl_type        | null      | 非必须   |        | 买方盈亏类型：0正常成交1正常平仓2强平3强减               |                   |
| ├─ bid_pnl             | null      | 非必须   |        | 买方平仓盈亏                                             |                   |
| ├─ ask_pnl_type        | null      | 非必须   |        | 卖方盈亏类型：0正常成交1正常平仓2强平3强减               |                   |
| ├─ ask_pnl             | null      | 非必须   |        | 卖方平仓盈亏                                             |                 |

## 4.13 调整持仓模式

### 基本信息

**Path：** /api/v1/future/position/adjustModel

**Method：** POST

**接口描述：**

### 请求参数

**Headers**

| 参数名称     | 参数值           | 是否必须 | 示例 | 备注 |
| ------------ | ---------------- | -------- | ---- | ---- |
| Content-Type | application/json | 是       |      |      |

**Body**

| 名称           | 类型    | 是否必须 | 默认值 | 备注                                             | 其他信息   |
| -------------- | ------- | -------- | ------ | ------------------------------------------------ | ---------- |
| currencyId     | integer | 非必须   |        | 货币ID                                         |            |
| type           | integer | 非必须   |        | 持仓模式，0：对冲，1：双边                       |            |

### 返回数据

| 名称 | 类型   | 是否必须 | 默认值 | 备注               | 其他信息 |
| ---- | ------ | -------- | ------ | ------------------ | -------- |
| code | number | 非必须   |        | 0：成功，非0：失败 |          |
| msg  | string | 非必须   |        | 消息msg            |          |
| data | object | 非必须   |        | 消息结合           |          |
