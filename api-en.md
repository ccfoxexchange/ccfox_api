
# API Instruction
# Contract Trading API instruction
  ## Contract Trading API list

| Authorisation | Data type   | Requst method                                 | Type   | description                   | Is Signature required |
| -------- | -------------- | ---------------------------------------- | ------ | ---------------------- | -------- |
| read     | Basic Info Interface | /api/v1/future/queryContract             | GET    | get all contracts info            | No       |
| read     | Basic Info Interface | /api/v1/common/queryCurrency             | GET    | get currency info                 | No       |
| read     | Basic Info Interface | /api/v1/common/exchange/list             | GET    | get all fiat                      | No       |
| read     | Basic Info Interface | /api/v1/common/exchange/coins            | GET    | get base currency's price         | No       |
| read     | Basic Info Interface | /api/v1/future/queryContractDeliveryList | GET    | get future deliver history        | No       |
| read     | Market Interface     | /api/v1/futureQuot/queryMarketStat       | GET    | get 24 hours volume               | No       |
| read     | Market Interface     | /api/v1/futureQuot/queryCandlestick      | GET    | get K-line data                   | No       |
| read     | Market Interface     | /api/v1/futureQuot/querySnapshot         | GET    | get specific contract snapshot    | No       |
| read     | Market Interface     | /api/v1/futureQuot/queryIndicatorList    | GET    | get all market quote snapshot     | No       |
| read     | Market Interface     | /api/v1/futureQuot/queryTickTrade        | GET    | get each tick trade               | No       |
| read     | User Interface     | /api/v1/future/queryVarietyMargin        | GET    | get variety of margin               | No       |
| read     | User Interface     | /api/v1/future/margin                    | GET    | get user margin info                | Yes       |
| read     | User Interface     | /api/v1/future/position                  | GET    | get user position info              | Yes       |
| read     | User Interface     | /api/v1/future/user                      | GET    | get user basic info                 | Yes       |
| trade     | Trading Interface     | /api/v1/future/order                     | GET    | make order                      | Yes       |
| trade     | Trading Interface     | /api/v1/future/orders                    | GET    | make amount of orders           | Yes       |
| trade     | Trading Interface     | /api/v1/future/order                     | DELETE | cancel order                    | Yes       |
| trade     | Trading Interface     | /api/v1/future/orders                    | DELETE | cancel amount of orders         | Yes       |
| trade     | Trading Interface     | /api/v1/future/order/all                 | DELETE | cancel all the orders           | Yes       |
| trade     | Trading Interface     | /api/v1/future/order                     | GET    | get order info                  | Yes       |
| trade     | Trading Interface     | /api/v1/future/queryActiveOrder          | GET    | get latest order list           | Yes       |
| trade     | Trading Interface     | /api/v1/future/queryLastestHistoryOrders | GET    | get historical order            | Yes       |
| trade     | Trading Interface     | /api/v1/future/position/isolate          | POST   | switch position mode            | Yes       |
| trade     | Trading Interface     | /api/v1/future/position/transferMargin   | POST   | adjust margin                   | Yes       |
| trade     | Trading Interface     | /api/v1/future/queryForceLower           | GET    | get forced-deleverage list      | Yes       |
| trade     | Trading Interface     | /api/v1/future/queryMatch                | GET    | get matched deleverage list     | Yes       |
| trade     | Trading Interface     | /api/v1/future/position/adjustModel      | POST   | adjust position model           | Yes       |

  ## Access URL

  - Test https://apitest.ccfox.com/
  - Production  https://api.ccfox.com/

  ## Signature Verification

  ### Apply to create API Key

  Create the API Key by web page
  API Key includes following two parts
  - Access Key 
  - Secret Key 

  ### Signature Instruction
API request would be compromised by internet transaction, therefore, for ensuring the request is not edited, except public API( Basic info, market quote), all the API must be verified by your API key as signature, for the purpose of verifying whether the attributes and parameters have been changed or not.

A legal request must include following parts:

- Method URL：server access api.ccfox.com，such as https://api.ccfox.com/api/v1/order
- API Access Key：Access Key of the applied API Key  
- timestamp expires：the timestamp when your request is sent. e.g.1548311559. Put the timestamp would be helpful in verification those requests from thrid-party. Legal within one minute
- signature：The value after Signature calculation, for validate the signature has not been changed.

> Attention：AccessKey，expires， signature all these three parameters should in the request header，in addtion, `'content-type' : 'application/json'` should be settled, the examples are given below(refer to each key).

```js
// Request header must include:
var headers = {
  'content-type' : 'application/json',
  'apiExpires': expires, //UNIX timestamp second,  legal within one minute for validation
  'apiKey': AccessKey, // API access in applied API Key
  'signature': signature // Signature
};
```

### Signature steps

Due to the difference of content, the computing outcome would be diffrent when using the HMAC SHA256 signature computing. Therefore please do standardization to the request before signature computing(notice that there will have some difference between GET and POST)

> GET request would convert the request parameters to json, then do the urlencode and put the outcome after the url parameter (?filter=xxxxxxx), 'data' is empty string.

> POST request would convert request json to string (empty space is not allowed), which should be in the 'data'

For example：enquiring the order status(GET)

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

### Signature Failure

- Please check whether the APIKey is valid, correctly copied,the IP in the whitelist.
- Please check the timestamp is current UTC timestamp, valid within one minute.
- Please check the parameters are ordered like (verb + path + expires + data).
- Please check utf-8 coding.

## Code sample

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

python sample

```python
import time
import hashlib
import hmac
from urllib.parse import urlparse
# Signature HMAC_SHA256(secret, verb + path + expires + data), hexadecimal.
# verb must be uppercase，url is relative，expires must be unix timestamp in second format
# besides, data must be json format and empty space should not exist between each key value if the data value is exist.
def generate_signature(secret, verb, url, expires, data):
    """Generate a request signature compatible with cloud."""
    # parse the url to get the path
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
# or could be generated in following way:
# expires = int(round(time.time()) + 5)
#GET request would convert the request parameters to json, then do the urlencode and put the outcome after the url parameter (?filter=xxxxxxx), 'data' is empty string.
#POST request would convert request json to string (empty space is not allowed), which should be in the 'data'

print(generate_signature('chNOOS4KvNXR_Xq4k4c9qsfoKWvnDecLATCRlcBwyKDYnWgO', 'GET', '/api/v1/instrument?filter=%7B%22orderId%22%3A%2211548326910655928%22%7D1518064237', expires, ''))
print(generate_signature('chNOOS4KvNXR_Xq4k4c9qsfoKWvnDecLATCRlcBwyKDYnWgO', 'POST', '/api/v1/instrument', expires, '{"symbol":"XBTM15","price":219.0,"clOrdID":"mm_bitmex_1a/oemUeQ4CAJZgP3fjHsA","orderQty":98}'))
```

# Frequence Limitation

> Interface data type instrcution is in the [Contract Trading API list](https://github.com/ccfoxexchange/ccfox_api/blob/master/api-en.md#Contract Trading API list)
 - trading interface   60/s  
 - others              120/min


# Error Code

| Code | Description                                  |
| ---- | -------------------------------------------- |
| 0    | Succeed                                      |
| 1001 | Account not existed                          |
| 1002 | Contract not existed                         |
| 1003 | Application Signal Error                     |
| 1004 | Illegal Order Price                          |
| 1005 | Illegal Order Quantity                       |
| 1006 | Over Limit Order Quantity                    |
| 1007 | Insufficient Margin                          |
| 1008 | Insufficient Quantity                        |
| 1009 | Account is taken over, trade banned          |
| 1010 | Account is taken over, transfer out banned   |
| 1011 | Trade Banned in this contract                |
| 1012 | Illegal Order Side                           |
| 1013 | Illegal Order Type                           |
| 1016 | Minimum Order quantity is not reached        |
| 1018 | Illegal Order Value                          |
| 1019 | Order is not existed                         |
| 1020 | There is no order from the opponent          |
| 1022 | Invalid closable quantity in current position|
| 1023 | Account is taken over, close banned          |
| 1027 | Contract Already Existed                     |
| 1028 | Order amount is over the limit               |
| 1029 | Position amount is over the limit            |
| 1031 | Conflict happens about margin type between existed position and the placing order in current contract |
| 1032 | Initiate Margin Error                        |
| 1035 | Margin Transfer in/out Banned                |
| 1036 | Order price is over the limit                |
| 1037 | Order value is over the limit                |
| 1041 | System is not ready, index is not sent or the previous data is not stored in SQL   |
| 1042 | Forbidden to open isolated order             |
| 1044 | Market price order costs too much spread     |
| 1046 | the passive order cannot be placed in current price, the order will be canceled  |
| 1050 | Trigger type is not settled                  |
| 1051 | Invalid trigger price                        |
| 1053 | conditional order amount is over limit       |
| 1056 | Order or position is already placed under this currency, switch position mode is forbidden  |



# Abbriviation List

> For the Purpose of minify the json file, the compressed key is applied, the relationship is as followed

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

# API

# 1.Basic Info



## 1.1 Get all contracts info



### Basic Info

**Path：** /api/v1/future/queryContract

**Method：** GET

**API Description：**

Attention：Signature does not required

### Return Data

| Name                       | Type        | Is required | Default Value | Note                                                     | Other Info          |
| -------------------------- | ----------- | -------- | ------ | ------------------------------------ | ----------------- |
| result                     | object []   | No      |        |                                          | item type: object |
| ├─ varietyId               | number      | Yes     |        | Variety ID                            |                   |
| ├─ applId                  | number      | Yes     |        | App Id                                |                   |
| ├─ underlyingId            | string      | Yes     |        | underlying ID                         |                   |
| ├─ productType             | number      | Yes     |        | 1 for mock future                     |                   |
| ├─ symbol                  | string      | Yes     |        | symbol                                |                   |
| ├─ chainextCid             | number      | Yes     |        | index id 'cid' in chainext            |                   |
| ├─ priceTick               | number      | Yes     |        | price tick                            |                   |
| ├─ lotSize                 | number      | Yes     |        | lowest size                           |                   |
| ├─ takerFeeRatio           | number      | Yes     |        | Taker fee rate                        |                   |
| ├─ makerFeeRatio           | number      | Yes     |        | Maker fee rate                        |                   |
| ├─ limitMaxLevel           | number      | Yes     |        | Max level in limit price order        |                   |
| ├─ marketMaxLevel          | number      | Yes     |        | Max level in market price order       |                   |
| ├─ maxNumOrders            | number      | Yes     |        | Maximum order number                  |                   |
| ├─ priceLimitRate          | number      | Yes     |        | price limit rate                      |                   |
| ├─ commodityId             | number      | Yes     |        | commodity ID                          |                   |
| ├─ currencyId              | number      | Yes     |        | currency ID                           |                   |
| ├─ contractType            | number      | Yes     |        | contract type,1 term, 2 perpetual     |                   |
| ├─ deliveryType            | number      | Yes     |        | delivery type, 1 cash，2 spot         |                   |
| ├─ deliveryPeriod          | number      | Yes     |        | delivery cycle, 0 perpetual 1 daily 2 weekly 3 monthly 4 seasonal             |                   |
| ├─ deliveryFeeRatio        | number      | Yes     |        | delivery fee ratio                                               |                   |
| ├─ contractSide            | number      | Yes     |        | contract side, 1 forward, 2 inverse                                   |                   |
| ├─ contractUnit            | number      | Yes     |        | Contract unit                              |                   |
| ├─ posiLimit               | number      | Yes     |        | position limit                                          |                   |
| ├─ orderLimit              | number      | Yes     |        | order limit                               |                   |
| ├─impactMarginNotional     | number      | Yes     |        | impact margin notional                    |                   |
| ├─ minMaintainRate         | number      | Yes     |        | Minimum maintanence fee rate             |                   |
| ├─ fairBasisInterval       | number      | Yes     |        | fair basis invertal(second)'                              |                   |
| ├─ clearPriceInterval      | number      | Yes     |        | Clear Price Interval(second)                                   |                   |
| ├─deliveryPriceInterval    | number      | Yes     |        | Delivery price interval(second), time backward                       |                   |
| ├─perpetualPremiumLimit    | number      | Yes     |        | Perpetual Contact Premium Limit                    |                   |
| ├─perpetualFundingfeeLimit | number      | Yes     |        | Perpetual Contract Funding Fee Limit          |                   |
| ├─perpetualSettleFrequency | number      | Yes     |        | Settle Frequency(number per day)              |                   |
| ├─risklessrateGoods        | number      | Yes     |        | Goods riskless rate                                           |                   |
| ├─risklessrateMoney        | number      | Yes     |        | Money riskless rate                                           |                   |
| ├─ isAutoList              | number      | Yes     |        | Auto-listed ：0 yes 1 No                                    |                   |
| ├─ onceListTime            | number      | Yes     |        | Once-list Time（will be listed only once）                           |                   |
| ├─regularSettlementTime    | number      | Yes     |        | regular settle time （The relative time of stock index contract settlement, set when entering the sql       |                   |
| ├─ createTime              | number      | Yes     |        | create time                                                 |                   |
| ├─ enabled                 | number      | Yes     |        | Enabled，0：yes，1：no                 |                   |
| ├─futureContractList       | object []   | Yes     |        | Contract List                         | item type: object |
| ├─ contractId              | number      | Yes     |        | contract ID                           |                   |
| ├─ applId                  | number      | Yes     |        | app ID, 1：spot，2：future                                 |                   |
| ├─ underlyingId            | string      | Yes     |        | underlying ID                                                   |                   |
| ├─ productType             | number      | Yes     |        | 1 for mock future                          |                   |
| ├─ symbol                  | string      | Yes     |        | symbol                                                 |                   |
| ├─ priceTick               | number      | Yes     |        | price tick                                            |                   |
| ├─ lotSize                 | number      | Yes     |        | minimum trading size                                           |                   |
| ├─ takerFeeRatio           | number      | Yes     |        | Taker fee rate                                            |                   |
| ├─makerFeeRatio            | number      | Yes     |        | Maker fee rate                                            |                   |
| ├─ limitMaxLevel           | number      | Yes     |        | limit price order max level                                     |                   |
| ├─marketMaxLevel           | number      | Yes     |        | market price order max level                                     |                   |
| ├─maxNumOrders             | number      | Yes     |        | Max ordering number                         |                   |
| ├─ priceLimitRate          | number      | Yes     |        | price limit rate                                                 |                   |
| ├─ commodityId             | number      | Yes     |        | commodity ID                                               |                   |
| ├─ currencyId              | number      | Yes     |        | currency ID                                               |                   |
| ├─ contractType            | number      | Yes     |        | contract type，1 term，2 perprtual                                  |                   |
| ├─ deliveryType            | number      | Yes     |        | delivery type，1 money，2 goods                           |                   |
| ├─ deliveryPeriod          | number      | Yes     |        | delivery cycle，0 perpetual 1 day 2 week 3 month 4 season度                            |                   |
| ├─deliveryFeeRatio         | number      | Yes     |        | delivery fee rate                                              |                   |
| ├─ contractSide            | number      | Yes     |        | contract side，1 forward，2 inverse                                   |                   |
| ├─ contractUnit            | number      | Yes     |        | contract unit                                                 |                   |
| ├─ posiLimit               | number      | Yes     |        | position limit                                                |                   |
| ├─ orderLimit              | number      | Yes     |        | order limit                                                 |                   |
| ├─impactMarginNotional     | number      | Yes     |        | impact margin notional                                            |                   |
| ├─minMaintainRate          | number      | Yes     |        | Minimum maintanence margin rate                                         |                   |
| ├─fairBasisInterval        | number      | Yes     |        | Fair basis interval                                        |                   |
| ├─clearPriceInterval       | number      | Yes     |        | clear price interval                                           |                   |
| ├─deliveryPriceInterval    | number      | Yes     |        | delivery price interval                                           |                   |
| ├─ createTime              | number      | Yes     |        | create time                                                 |                   |
| ├─ varietyId               | integer     | Yes     |        | variety ID，underlying ID                                           |                   |
| ├─ lastTradeTime           | number      | Yes     |        | last trade time                                             |                   |
| ├─ deliveryTime            | number      | Yes     |        | last delivery time                                             |                   |
| ├─ listPrice               | number      | Yes     |        | list price                |                   |
| ├─ listTime                | number      | Yes     |        | list time                           |                   |
| ├─contractStatus           | number      | Yes     |        | contract status: 1 Call Auction,2 continuous trading,3 trading paused ,4 delisted, 5 
Unlisted |                   |
| ├─perpetualPremiumLimit    | number,null | Yes     |        | range of perpetual Premium limit                                 |                   |
| ├─perpetualFundingfeeLimit | number,null | Yes     |        | range ratio of perpetual Funding fee Limit                             |                   |
| ├─perpetualSettleFrequency | number,null | Yes     |        | Settle Frequency(numbers per day)                                       |                   |
| ├─risklessrateGoods        | number,null | Yes     |        | Goods riskless rate                                           |                   |
| ├─risklessrateMoney        | number,null | Yes     |        | money riskless rate                                           |                   |
| ├─ isAutoList              | number      | Yes     |        | autolisted：0 Yes, 1 No                                   |                   |

## 1.2 enquire all currenies info



### Basic information

**Path：** /api/v1/common/queryCurrency

**Method：** GET

**API Description：**

### Request Parameter

### Return Data

| Name                     | type      | Is required | Default Value | Note                           | Other Info          |
| ------------------------ | --------- | -------- | ------ | ------------------------------ | ----------------- |
| result                   | object [] | No   |        |                                | item type: object |
| ├─ currencyId            | number    | No   |        | Currency ID                         |                   |
| ├─ symbol                | string    | No   |        | Currency name                       |                   |
| ├─ nameCn                | string    | No   |        | Currency Chinese name                     |                   |
| ├─ symbolDesc            | string    | No   |        | Currency description                       |                   |
| ├─ displayPrecision      | number    | No   |        | display Precision                   |                   |
| ├─minWithdrawQuantity    | number    | No   |        | Minimum withdraw quantity                     |                   |
| ├─ withdrawFlatFee       | number    | No   |        | withdraw fee                     |                   |
| ├─ auditThreshold        | number    | No   |        | audit threshold                   |                   |
| ├─exeutiveAuditThreshold | number    | No   |        | executive audit threshold                   |                   |
| ├─ forbidWithdraw        | number    | No   |        | whether withdraw is allowed：0 allowed 1 forbidden   |                   |
| ├─forbidCreateAddress    | number    | No   |        | whether address creation is allowed：0 allowed，1 forbidden |                   |
| ├─ forbidRecharge        | number    | No   |        | whether recharge is allowed：0 allowed, 1 forbidden     |                   |
| ├─ prefixUrl             | string    | No   |        | blockchain browser url                 |                   |
| ├─ enabled               | number    | No   |        | whether the currency is enabled：0 enable，1 disable, 2 deleted  |                   |

## 1.3 enquire all fiat info



### Basic information

**Path：** /api/v1/common/exchange/list

**Method：** GET

**API Description：**

### Request Parameter

### Return Data

| Name          | type      | Is required | Default Value | Note                | Other Info          |
| ------------- | --------- | -------- | ------ | ------------------- | ----------------- |
| result        | object [] | No   |        |                     | item type: object |
| ├─ id         | number    | No   |        | Fiat ID              |                   |
| ├─ name       | string    | No   |        | Fiat Name            |                   |
| ├─ rate       | number    | No   |        | Exchange rate to USD |                   |
| ├─ updateTime | string    | No   |        | update time          |                   |

## 1.4 get all money of account



### Basic information

**Path：** /api/v1/common/exchange/coins

**Method：** GET

**API Description：**

currencyId: currency ID
latest: latest USD price

### Request Parameter

### Return Data

| Name          | type      | Is required | Default Value | Note        | Other Info          |
| ------------- | --------- | -------- | ------ | ----------- | ----------------- |
| result        | object [] | No   |        |             | item type: object |
| ├─ currencyId | number    | Yes     |        | currency ID      |                   |
| ├─ latest     | number    | Yes     |        | latest USD price |                   |

## 1.5 enquire contract delivery history



### Basic information

**Path：** /api/v1/future/queryContractDeliveryList

**Method：** GET

**API Description：**

### Request Parameter

**Query**

| Parameter Name   | Is required | Sample | Note                        |
| ---------- | -------- | ---- | --------------------------- |
| type       | Yes       |      | 1 term 2 perpetual                 |
| pageNum    | No       |      | current page number，default：1             |
| pageSize   | No       |      | page size ，default：10          |
| contractId | No       |      | contract ID                      |
| sort       | No       |      | sorting type，only support：DESC，ASC |

### Return Data

| Name                       | type      | Is required | Default Value | Note                                 | Other Info          |
| -------------------------- | --------- | -------- | ------ | ------------------------------------ | ----------------- |
| code                       | number    | No   |        | 0：succeed，not 0：failed                   |                   |
| msg                        | string    | No   |        | request reply msg                          |                   |
| data                       | object    | No   |        |                                      |                   |
| ├─ pageNum                 | number    | No   |        | currency page number                               |                   |
| ├─ pageSize                | number    | No   |        | page size                           |                   |
| ├─ size                    | number    | No   |        | current size                           |                   |
| ├─ startRow                | number    | No   |        | the row number in sql of the fisrt element in currenct page   |                   |
| ├─ endRow                  | number    | No   |        | the row number in sql of the last element in currenct page |                   |
| ├─ total                   | number    | No   |        | total record number                             |                   |
| ├─ pages                   | number    | No   |        | total page number                               |                   |
| ├─ list                    | object [] | No   |        | result collection                               | item type: object |
| ├─ applId                  | number    | Yes     |        | 2 future                               |                   |
| ├─ contractType            | number    | Yes     |        | contract type                             |                   |
| ├─ contractId              | number    | Yes     |        | 合约ID                               |                   |
| ├─perpetualSettleFrequency | number    | Yes     |        | settle frequency(numebers per day)                   |                   |
| ├─ symbol                  | string    | Yes     |        | contract name                             |                   |
| ├─ deliveryTime            | number    | Yes     |        | delivery time                             |                   |
| ├─ deliveryQty             | number    | Yes     |        | delivery quantity                             |                   |
| ├─ deliveryAmt             | number    | Yes     |        | delivery amount                             |                   |
| ├─ deliveryPrice           | number    | Yes     |        | delivery price                             |                   |
| ├─ fundingRate             | number    | Yes     |        | perpetual contract fee rate                     |                   |
| ├─ prePage                 | number    | No   |        | previous page                               |                   |
| ├─ nextPage                | number    | No   |        | next page                               |                   |
| ├─ isFirstPage             | boolean   | No   |        | whether this is the first page         |                   |
| ├─ isLastPage              | boolean   | No   |        | whether this is the last page               |                   |
| ├─ hasPreviousPage         | boolean   | No   |        | whether there is a previous page         |                   |
| ├─ hasNextPage             | boolean   | No   |        | whether there is a previous page         |                   |
| ├─ navigatePages           | number    | No   |        | navigate pages                            |                   |
| ├─ navigatepageNums        | number [] | No   |        | all navigate page numbers          | item type: number |
| ├─ navigateFirstPage       | number    | No   |        | first page on navibar                     |                   |
| ├─ navigateLastPage        | number    | No   |        | last page on navibar                   |                   |
| ├─ firstPage               | number    | No   |        | first page                              |                   |
| ├─ lastPage                | number    | No   |        | last page                     |                   |

# 2.Quotes 



## 2.1 Enquire last 24 hours statistics



### Basic information

**Path：** /api/v1/futureQuot/queryMarketStat

**Method：** GET

**API Description：**

### Request Parameter

**Query**

| Parameter Name   | Is required | Sample | Note   |
| ---------- | -------- | ---- | ------ |
| currencyId | Yes       |      | currency ID |

### Return Data

| Name                | type   | Is required | Default Value | Note         | Other Info |
| ------------------- | ------ | -------- | ------ | ------------ | -------- |
| code                | number | No   |        |              |          |
| msg                 | string | No   |        |              |          |
| data                | object | No   |        |              |          |
| ├─ messageType      | string | No   |        |              |          |
| ├─ currencyId       | number | No   |        | currency ID       |          |
| ├─ total24hTurnover | string | No   |        | 24hour volume |          |
| ├─ total7dTurnover  | string | No   |        | 7days volume    |          |
| ├─ total30dTurnover | string | No   |        | 30days volume   |          |

## 2.2 enquire K-line data



### Basic information

**Path：** /api/v1/futureQuot/queryCandlestick

**Method：** GET

**API Description：**

note：lines data structure：List<List>
e.g. ：
    [
        [${time}, ${open price}, ${highest price}, ${lowest price}, ${close price}, ${ match volume}]
    ]

range value：
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





### Request Parameter

**Query**

| Parameter Name | Is required | Sample | Note     |
| -------- | -------- | ---- | -------- |
| symbol   | Yes       |      | contract ID   |
| range    | Yes       |      | linear value |

### Return Data

| Name     | type     | Is required | Default Value | Note | Other Info         |
| -------- | -------- | -------- | ------ | ---- | ---------------- |
| success  | boolean  | No   |        |      |                  |
| data     | object   | No   |        |      |                  |
| ├─ lines | array [] | No   |        |      | item type: array |

## 2.3 enquire a specific contract info 



### Basic information

**Path：** /api/v1/futureQuot/querySnapshot

**Method：** GET

**API Description：**

note：
    bids&asks structure：List<List>
     如
        [
            [${price}, ${quantity}]
        ]

### Request Parameter

**Query**

| Parameter Name   | Is required | Sample | Note   |
| ---------- | -------- | ---- | ------ |
| contractId | Yes       |      | contract ID |

### Return Data

| Name       | type      | Is required | Default Value | Note                                | Other Info          |
| ---------- | --------- | -------- | ------ | ----------------------------------- | ----------------- |
| contractId | number    | No   |        | contract ID                              |                   |
| result     | object    | No   |        |                                     |                   |
| ├─ mt      | number    | No   |        | message type：messageType               |                   |
| ├─ ai      | number    | No   |        | application ID：applId                      |                   |
| ├─ ci      | number    | No   |        | contract ID：contractId                  |                   |
| ├─ sb      | string    | No   |        | contract symbol：symbol                    |                   |
| ├─ td      | number    | No   |        | trade date：tradeDate                 |                   |
| ├─ te      | number    | No   |        | trade time：time                      |                   |
| ├─ lp      | string    | No   |        | last price：lastPrice                 |                   |
| ├─ mq      | string    | No   |        | match quantity：matchQty                    |                   |
| ├─ nt      | number    | No   |        | numbers of trade：numTrades                 |                   |
| ├─ op      | string    | No   |        | open price：openPrice                   |                   |
| ├─ ph      | string    | No   |        | highest price：priceHigh                   |                   |
| ├─ pl      | string    | No   |        | lowest price：priceLow                    |                   |
| ├─ hph     | string    | No   |        | historical highest price ：historyPriceHigh        |                   |
| ├─ hpl     | string    | No   |        | historical lowest price：historyPriceLow         |                   |
| ├─ tt      | string    | No   |        | total turn over：totalTurnover           |                   |
| ├─ tv      | string    | No   |        | total volume：totalVolume             |                   |
| ├─ tbv     | string    | No   |        | total bid volume：totalBidVol             |                   |
| ├─ tav     | string    | No   |        | total ask volume：totalAskVol             |                   |
| ├─ pp      | string    | No   |        | previous close price：prevPrice         |                   |
| ├─ cp      | string    | No   |        | index price，clear price：clearPrice        |                   |
| ├─ pv      | string    | No   |        | total position volume：posiVol                   |                   |
| ├─ pcr     | string    | No   |        | price change radio of day：priceChangeRadio      |                   |
| ├─ pc      | string    | No   |        | price change of day：priceChange               |                   |
| ├─ lui     | number    | No   |        | quote update id：lastUpdateId              |                   |
| ├─ cs      | number    | No   |        | contract status ：contractStatus          |                   |
| ├─ dp      | string    | No   |        | delivery price：deliveryPrice             |                   |
| ├─ fr      | string    | No   |        | funding rate：fundingRate               |                   |
| ├─ pfr     | string    | No   |        | prediction funding rate：predictionFundingRate |                   |
| ├─ pi      | string    | No   |        | premium index：premiumIndex              |                   |
| ├─ ppi     | string    | No   |        | predicted premium index：predictionPremiumIndex  |                   |
| ├─ fb      | string    | No   |        | fair basis：fairBasis                 |                   |
| ├─ ts      | number    | No   |        | trading signal，1 for bid ，2 for ask：tradingsignal |                   |
| ├─ sl      | number    | No   |        | 1~100, trading signal level ：signalLevel    |                   |
| ├─ ip      | string    | No   |        | indexprice of underlying：indexPrice            |                   |
| ├─ bids    | string [] | No   |        | bid spread                            | item type: string |
| ├─ asks    | string [] | No   |        | ask spread                            | item type: string |

## 2.4 enquire all quote snapshot



### Basic information

**Path：** /api/v1/futureQuot/queryIndicatorList

**Method：** GET

**API Description：**

For the meaning of each key, please refer to  2.3 - /futureQuot/querySnapshot 

### Request Parameter

### Return Data

| Name   | type      | Is required | Default Value | Note | Other Info          |
| ------ | --------- | -------- | ------ | ---- | ----------------- |
|        | object [] | No   |        |      | item type: object |
| ├─ mt  | number    | Yes     |        |      |                   |
| ├─ ai  | number    | Yes     |        |      |                   |
| ├─ ci  | number    | Yes     |        |      |                   |
| ├─ sb  | string    | Yes     |        |      |                   |
| ├─ td  | number    | Yes     |        |      |                   |
| ├─ te  | number    | Yes     |        |      |                   |
| ├─ lp  | string    | Yes     |        |      |                   |
| ├─ mq  | string    | Yes     |        |      |                   |
| ├─ nt  | number    | Yes     |        |      |                   |
| ├─ op  | string    | Yes     |        |      |                   |
| ├─ ph  | string    | Yes     |        |      |                   |
| ├─ pl  | string    | Yes     |        |      |                   |
| ├─ hph | string    | Yes     |        |      |                   |
| ├─ hpl | string    | Yes     |        |      |                   |
| ├─ tt  | string    | Yes     |        |      |                   |
| ├─ tv  | string    | Yes     |        |      |                   |
| ├─ tbv | string    | Yes     |        |      |                   |
| ├─ tav | string    | Yes     |        |      |                   |
| ├─ pp  | string    | Yes     |        |      |                   |
| ├─ cp  | string    | Yes     |        |      |                   |
| ├─ pv  | string    | Yes     |        |      |                   |
| ├─ pcr | string    | Yes     |        |      |                   |
| ├─ pc  | string    | Yes     |        |      |                   |
| ├─ lui | number    | Yes     |        |      |                   |
| ├─ cs  | number    | Yes     |        |      |                   |
| ├─ dp  | string    | Yes     |        |      |                   |
| ├─ fr  | string    | Yes     |        |      |                   |
| ├─ pfr | string    | Yes     |        |      |                   |
| ├─ pi  | string    | Yes     |        |      |                   |
| ├─ ppi | string    | Yes     |        |      |                   |
| ├─ fb  | string    | Yes     |        |      |                   |
| ├─ ts  | number    | Yes     |        |      |                   |
| ├─ sl  | number    | Yes     |        |      |                   |
| ├─ ip  | string    | Yes     |        |      |                   |

## 2.5 enquire every single matches



### Basic information

**Path：** /api/v1/futureQuot/queryTickTrade

**Method：** GET

**API Description：**

note：
    trades data structure ：List<List>
   E.g.：
    [
        [${timestamp}, ${matchprice}, ${matchquantity}, ${side}]
    ]

### Request Parameter

**Query**

| Parameter Name   | Is required | Sample | Note   |
| ---------- | -------- | ---- | ------ |
| contractId | Yes       |      | contractID |

### Return Data

| Name      | type     | Is required | Default Value | Note         | Other Info         |
| --------- | -------- | -------- | ------ | ------------ | ---------------- |
| success   | boolean  | No   |        |              |                  |
| data      | object   | No   |        | data collection     |                  |
| ├─ trades | array [] | No   |        | last 50 matches | item type: array |

# 3.user class



## 3.1 enquire user Basic information



### Basic information

**Path：** /api/v1/future/user

**Method：** GET

**API Description：**

Purpose ：get registered user-related info

### Request Parameter

**Headers**

| Parameter Name     | Parameter Value           | Is required | Sample | Note   |
| ------------ | ---------------- | -------- | ---- | ------ |
| Content-Type | application/json | Yes       |      |        |
| signature    |                  | Yes       |      | signature   |
| apiKey       |                  | Yes       |      | api public key   |
| apiExpires   |                  | Yes       |      | timestamp |

### Return Data

| Name                 | type   | Is required | Default Value | Note                                       | Other Info |
| -------------------- | ------ | -------- | ------ | ------------------------------------------ | -------- |
| message_type         | number | Yes     |        |                                            |          |
| data                 | object | Yes     |        |                                            |          |
| ├─ id                | number | Yes     |        | user id                                     |          |
| ├─ username          | string | Yes     |        | username                                     |          |
| ├─ email             | string | Yes     |        | email                                   |          |
| ├─ enabled           | number | Yes     |        | user status（0：normal，1: locked，2: unregistered）        |          |
| ├─ createTime        | string | Yes     |        | user account create time              |          |
| ├─ lastLogin         | null   | Yes     |        |   last login time                       |          |
| ├─ phone             | string | Yes     |        | phone number                                     |          |
| ├─ googleCode        | string | Yes     |        | google verification code                                 |          |
| ├─ tradePassword     | null   | Yes     |        | password for trade                                   |          |
| ├─ loginPassGrade    | number | Yes     |        | grade of login password                               |          |
| ├─ googleValidate    | null   | Yes     |        | whether open google validation or not：1， open 2 close           |          |
| ├─avoidFishingCode   | null   | Yes     |        | Anti-phishing attack code                                 |          |
| ├─ tradeSafe         | null   | Yes     |        | 1 input password everytime，2 input every two hours 3，no input required |          |
| ├─ lastTradeTime     | null   | Yes     |        | last time of input trading password                      |          |
| ├─ createTimeStart   | null   | Yes     |        |                                            |          |
| ├─ createTimeEnd     | null   | Yes     |        |                                            |          |
| ├─certificationGrade | null   | Yes     |        | certification level                             |          |

## 3.2 enquire user assets info


### Basic information

**Path：** /api/v1/future/margin

**Method：** GET

**API Description：**

Purpose：enquire user assets info（including available fund、frozen fund、margin, etc of account in each currency）

### Return Data

| Name                | type      | Is required | Default Value | Note               | Other Info          |
| ------------------- | --------- | -------- | ------ | ------------------ | ----------------- |
| code                | number    | No   |        | 0：succeed，not 0：failed |                   |
| msg                 | string    | No   |        | message:msg            |                   |
| data                | object [] | No   |        | data collection           | item type: object |
| ├─ currencyId       | string    | No   |        | currency ID             |                   |
| ├─ totalBalance     | string    | No   |        | total assets             |                   |
| ├─ available        | string    | No   |        | available assets           |                   |
| ├─ frozenForTrade   | string    | No   |        | frozen assets in order account       |                   |
| ├─ initMargin       | string    | No   |        | initial margin       |                   |
| ├─ frozenInitMargin | string    | No   |        | frozen initial margin   |                   |
| ├─ closeProfitLoss  | string    | No   |        | closed p&l         |                   |

## 3.3 Enquire user postion info



### Basic information

**Path：** /api/v1/future/position

**Method：** GET

**API Description：**

Purpose ：enquire current position data of user

### Return Data

| Name                  | type      | Is required | Default Value | Note               | Other Info          |
| --------------------- | --------- | -------- | ------ | ------------------ | ----------------- |
| code                  | number    | No   |        | 0：succeed ，not 0：failed |                   |
| msg                   | number    | No   |        | message:msg            |                   |
| data                  | object [] | No   |        | data collection          | item type: object |
| ├─ contractId         | number    | No   |        | contract ID           |                   |
| ├─ posiQty            | string    | No   |        | position quantity           |                   |
| ├─ openAmt            | string    | No   |        | position amount           |                   |
| ├─ initMargin         | string    | No   |        | initial margin         |                   |
| ├─ posiStatus         | number    | No   |        | position status           |                   |
| ├─ marginType         | number    | No   |        | margin type        |                   |
| ├─ closeProfitLoss    | string    | No   |        | closed profit & loss         |                   |
| ├─ initMargiRate      | string    | No   |        | initial margin rate       |                   |
| ├─ maintainMarginRate | string    | No   |        | maintanence margin rate       |                   |
| ├─ frozenCloseQty     | string    | No   |        | frozen close position quantity      |                   |
| ├─ frozenOpenQty      | string    | No   |        | frozen open position quantity       |                   |
| ├─ posiSide           | number    | No   |        | position side 0 hedge 1 long -1 short      |                   |


## 3.4 enquire user's contract margin levels



### Basic information

**Path：** /api/v1/future/queryVarietyMargin

**Method：** GET

**API Description：**

### Request Parameter

**Query**

| Parameter Name   | Is required | Sample    | Note   |
| ---------- | -------- | ------- | ------ |
| varietyId  | Yes       | Integer | variety id |
| contractId | Yes       | Integer | contract id |

### Return Data

| Name            | type      | Is required | Default Value | Note           | Other Info          |
| --------------- | --------- | -------- | ------ | ------------------      | ----------------- |
| code            | integer   | Yes     |        | 0：succeed，not 0：failed |                   |
| msg             | string    | Yes     |        | message:msg              |                   |
| data            | object [] | Yes     |        | data collection          | item type: object |
| ├─ applId       | integer   | Yes     |        | application id           |                   |
| ├─ varietyId    | integer   | Yes     |        | variety ID               |                   |
| ├─ contractId   | integer   | No      |        | contract ID              |                   |
| ├─ posiQty      | string    | Yes     |        | position quantity        |                   |
| ├─ initRate     | string    | Yes     |        | initial margin rate      |                   |
| ├─ maintainRate | string    | Yes     |        | maintanence margin rate  |                   |

# 4.trade class

## 4.1 contract ordering 

### Basic information

**Path：** /api/v1/future/order

**Method：** POST

**API Description：**

purpose：future contract  - ordering（including long, short, close order）
note：
  contract ID：contractId，contract name：symbol when ordering，at least one need to be transfered，when both transfered，using contractId
  client side ID：clientOrderId, will be auto-generated when not transfered

### Request Parameter

**Headers**

| Parameter Name     | Parameter Value           | Is required | Sample | Note |
| ------------ | ---------------- | -------- | ---- | ---- |
| Content-Type | application/json | Yes       |      |      |

**Body**

| Name           | type    | Is required | Default Value | Note                                             | Other Info   |
| -------------- | ------- | -------- | ------ | ------------------------------------------------ | ---------- |
| contractId     | integer | No   |        | contract ID                                         |            |
| symbol         | string  | No   |        | contract Name                                       |            |
| clientOrderId  | string  | No   |        | client side order ID                                     |            |
| side           | integer | Yes     |        | contract order site （ bid 1，ask -1）                        | enumerate: -1,1 |
| price          | string  | No   |        | contract order（not required when order_type equals tp 3（market price） |            |
| quantity       | string  | Yes     |        | contract order quantity                                      |            |
| orderType      | integer | Yes     |        | contract order type（1（limit price），3（market price） ）            |            |
| positionEffect | number  | Yes     |        | position effect（1 open, 2 close）                         |            |
| marginType     | number  | Yes     |        | margin type（1 for cross, 2 for isolated ）                         |            |
| marginRate     | string  | Yes     |        | margin rate（cross: >=0，0：take the default setting of contract，isolated: >0    |            |

### Return Data

| Name | type   | Is required | Default Value | Note               | Other Info |
| ---- | ------ | -------- | ------ | ------------------ | -------- |
| code | number | No   |        | 0：succeed ，not 0：failed |          |
| msg  | string | No   |        | message msg            |          |
| data | object | No   |        | message collection          |          |

## 4.2 placeing amount of order

### Basic information

**Path：** /api/v1/future/orders

**Method：** POST

**API Description：**

purpose ：future contract - placing amount of order （multiple order）
**attention**：
  contract ID：contractId，contract name：symbol when ordering，at least one need to be transfered，when both transfered，using contractId
  client side ID：clientOrderId, will be auto-generated when not transfered
**respone data - the data format is as follow**：
    "data":{"succ":[[${clientOrderId]],"reject":[[${clientOrderId},${failure code}]]}

### Request Parameter

**Headers**

| Parameter Name     | Parameter Value           | Is required | Sample | Note |
| ------------ | ---------------- | -------- | ---- | ---- |
| Content-Type | application/json | Yes       |      |      |

**Body**

| Name             | type      | Is required | Default Value | Note                           | Other Info          |
| ---------------- | --------- | -------- | ------ | ------------------------------ | ----------------- |
| orders           | object [] | No   |        |                                | item type: object |
| ├─ side          | number    | Yes     |        | bid and ask side，1 bid -1 ask              |                   |
| ├─ clientOrderId | string    | No   |        | client side order ID                   |                   |
| ├─ orderPrice    | string    | No   |        | order price                       |                   |
| ├─ orderQty      | string    | Yes     |        | order amount                       |                   |
| ├─ orderType     | number    | Yes     |        | order type：1（limit price），3（ market price） |                   |
| ├─ contractId    | number    | Yes     |        | contract ID                         |                   |
| ├─ symbol        | string    | No   |        | contract Name                       |                   |
| ├─positionEffect | number    | Yes     |        | position effect，1 open ，2 close         |                   |
| ├─ marginType    | number    | Yes     |        | margin type，1 cross ，2 isolate      |                   |
| ├─ initRate      | string    | No   |        | margin rate，cross ：>=0，0：according to the contract default，isolate：>0    |                   |

### Return Data

| Name      | type     | Is required | Default Value | Note         | Other Info         |
| --------- | -------- | -------- | ------ | ------------ | ---------------- |
| code      | number   | No   |        |              |                  |
| msg       | string   | No   |        |              |                  |
| data      | object   | No   |        |              |                  |
| ├─ succ   | array [] | No   |        | order succeed list  | item type: array |
| ├─ reject | array [] | No   |        | order failed list | item type: array |

## 4.3 Cancelling contract



### Basic information

**Path：** /api/v1/future/order

**Method：** DELETE

**API Description：**

Purpose ：future contract - cancel a specific order which is not matched

### Request Parameter

**Query**

| Parameter Name | Is required | Sample                                          | Note                                                         |
| -------- | -------- | --------------------------------------------- | ------------------------------------------------------------ |
| filter   | Yes       | {"contractId":1,"originalOrderId":"12345677"} | UrlEncode decrypted as ：{"contractId":1,"originalOrderId":"12345677"} contractId：contract ID originalOrderId:original order id |

**Body**

| Name | type | Is required | Default Value | Note | Other Info |
| ---- | ---- | -------- | ------ | ---- | -------- |
|      |      |          |        |      |          |

### Return Data

| Name | type   | Is required | Default Value | Note               | Other Info |
| ---- | ------ | -------- | ------ | ------------------ | -------- |
| code | number | No   |        | 0：successd，not 0：failed |          |
| msg  | string | No   |        | message:msg           |          |

## 4.4 cancelling amount of contract



### Basic information

**Path：** /api/v1/future/orders

**Method：** DELETE

**API Description：**

purpose ：future contract - Cancelling amount of contract that not matched

### Request Parameter

**Body**

| Name               | type      | Is required | Default Value | Note       | Other Info          |
| ------------------ | --------- | -------- | ------ | ---------- | ----------------- |
| cancels            | object [] | Yes   |        |            | item type: object |
| ├─ contractId      | number    | Yes   |        | contract ID     |                   |
| ├─ originalOrderId | string    | Yes   |        | original order Id |                   |
| ├─ clientOrderId   | string    | No   |        | client side Order Id, will be auto-generated when not transfered |                   |

### Return Data

| Name | type    | Is required | Default Value | Note              | Other Info |
| ---- | ------- | -------- | ------ | ----------------- | -------- |
| code | integer | No   |        | 0:Succeed，Not 0：Failed |          |

## 4.5 Cancelling all the order in contract



### Basic information

**Path：** /api/v1/future/order/all

**Method：** DELETE

**API Description：**

purpose ：future contract - Cancelling all the unmatched contract

### Return Data

| Name | type   | Is required | Default Value | Note               | Other Info |
| ---- | ------ | -------- | ------ | ------------------ | -------- |
| code | number | No   |        | 0:Succeed，Not 0：Failed |          |
| msg  | string | No   |        | message:msg            |          |
| data | string | No   |        | data collection          |          |

## 4.6 enquire order info


### Basic information

**Path：** /api/v1/future/order

**Method：** GET

**API Description：**

purpose ：contract - enquire related info of contract

**Query**

| Parameter Name | Is required | Sample                            | Note                                                         |
| -------- | -------- | ------------------------------- | ------------------------------------------------------------ |
| filter   | Yes       | {"orderId":"11548740843018185"} | {"orderId":"11548740843018185"} urlencode  decrypt to{"orderId":"11548740843018185"} |

### Return Data

| Name             | type   | Is required | Default Value | Note                                                         | Other Info |
| ---------------- | ------ | -------- | ------ | ------------------------------------------------------------ | -------- |
| code             | number | No   |        | 0：succeed，not 0：failed                                           |          |
| msg              | string | No   |        | message: msg                                                      |          |
| data             | object | No   |        | data collection                                                     |          |
| ├─applId         | number | No   |        | application id                                                     |          |
| ├─contractId     | number | No   |        | contract ID                                                     |          |
| ├─accountId      | number | No   |        | account ID                                                       |          |
| ├─clOrderId      | string | No   |        | client order id                                                 |          |
| ├─ side          | number | No   |        | order side（1：bid，-1：ask）                                    |          |
| ├─orderPrice     | string | No   |        | order price                                                     |          |
| ├─orderQty       | string | No   |        | order quantity                                                     |          |
| ├─orderId        | string | No   |        | order id                                                 |          |
| ├─orderTime      | number | No   |        | order placed time                                                    |          |
| ├─orderStatus    | number | No   |        | order status（0 not submit，1 submitting，2 not matched，3 partly matched，4 all matched ，5partly matched, partly canceled,6 all cancelled，7 cancelling，8 invalid） |          |
| ├─matchQty       | string | No   |        | match quantity                                                     |          |
| ├─matchAmt       | string | No   |        | match amount                                                     |          |
| ├─cancelQty      | string | No   |        | cancel quantity                                                     |          |
| ├─matchTime      | number | No   |        | time when order matched                                        |          |
| ├─orderType      | number | No   |        | order type（1：limit，3：market）                                 |          |
| ├─timeInForce    | number | No   |        | time in force type：available in current day、GTC、IOC，default 1， 1, "Match all when price triggered"；2, "Match then cancel rest orders" |          |
| ├─positionEffect | number | No   |        | position effect（1 open 2 close）                                      |          |
| ├─marginType     | number | No   |        | margin type（1、cross，2：isolated）                                 |          |
| ├─fcOrderId      | string | No   |        | liquidation order id，it refers to a liquidation order when not null                                |          |

## 4.7 enquire current order book



### Basic information

**Path：** /api/v1/future/queryActiveOrder

**Method：** GET

**API Description：**

purpose ：future contract - enquire current order book list
### Request Parameter

**Query**

| Parameter Name   | Is required | Sample | Note              |
| ---------- | -------- | ---- | ----------------- |
| contractId | Yes       |      | contract ID            |

### Return Data

| Name             | type      | Is required | Default Value | Note                                                         | Other Info          |
| ---------------- | --------- | -------- | ------ | ------------------------------------------------------------ | ----------------- |
| code             | number    | No   |        | 0: succeed , not 0 failed                                   |                   |
| msg              | string    | No   |        | message msg                                             |                   |
| data             | object [] | No   |        | data collection                                           | item type: object |
| ├─applId         | number    | No   |        | application id                                    |                   |
| ├─contractId     | number    | No   |        | contract ID                                       |                   |
| ├─accountId      | number    | No   |        | account ID                                            |                   |
| ├─clOrderId      | string    | No   |        | client side order id                                   |                   |
| ├─ side          | number    | No   |        | order side（1：bid，-1：ask）                        |                   |
| ├─orderPrice     | string    | No   |        | order price                               |                   |
| ├─orderQty       | string    | No   |        | order quantity                                         |                   |
| ├─orderId        | string    | No   |        | order id                                                  |                   |
| ├─orderTime      | number    | No   |        | order time                                         |                   |
| ├─orderStatus    | number    | No   |        | order status（0 not submit，1 submitting，2 not matched，3 partly matched，4 all matched ，5partly matched, partly canceled,6 all cancelled，7 cancelling，8 invalid） |                   |
| ├─matchQty       | string    | No   |        | match quantity                                                    |                   |
| ├─matchAmt       | string    | No   |        | match amount                                                     |                   |
| ├─cancelQty      | string    | No   |        | canceled quantity                                          |                   |
| ├─matchTime      | number    | No   |        | Match time                                                    |                   |
| ├─orderType      | number    | No   |        | Order type（1：Limit price，3： Market price）                                 |                   |
| ├─timeInForce    | number    | No   |        | Order time in force ：available on same day、GTC、IOC，default1， 1, "Match all when price triggered"；2, "Match then cancel rest orders"|                   |
| ├─positionEffect | number    | No   |        | Position Effect（1 open 2 close）                                      |                   |
| ├─marginType     | number    | No   |        | margin type（1、cross，2： isolated）                                 |                   |
| ├─fcOrderId      | string    | No   |        | liquidation order id which refers to this is a liquidation order when not null                               |                   |
| ├─deltaPrice     | string    | No   |        | difference between order price and mark price                     |                   |
| ├─frozenPrice    | string    | No   |        | frozen price                                               |                   |


## 4.8 enquire historical order

### Basic information

**Path：** /api/v1/future/queryHisOrder

**Method：** GET

**API Description：**

### Request Parameter

**Query**

| Parameter Name   | Is required | Sample | Note              |
| ---------- | -------- | ---- | ----------------- |
| contractId | No       |      | Contract ID            |
| pageSize   | No       |      | default：100，<=1000 |

### Return Data

| Name                 | type      | Is required | Default Value | Note                                                         | Other Info          |
| -------------------- | --------- | -------- | ------ | ------------------------------------------------------------ | ----------------- |
| code                 | number    | No   |        |                                                              |                   |
| msg                  | string    | No   |        |                                                              |                   |
| data                 | object [] | No   |        |                                                              | item type: object |
| ├─ appl_id           | integer   | No   |        | contract ID, 2 for futures                                    |                   |
| ├─ timestamp         | number    | No   |        | timestamp when order placed                                  |                   |
| ├─ user_id           | integer   | No   |        | user ID                                                       |                   |
| ├─ contract_id       | integer   | No   |        | contract ID                                                       |                   |
| ├─ uuid              | string    | No   |        | order id                                                     |                   |
| ├─ side              | integer   | No   |        | bid and ask side                                        |                   |
| ├─ price             | string    | No   |        | order price                                                       |                   |
| ├─ quantity          | string    | No   |        | order quantity                                          |                   |
| ├─ order_type        | null      | No   |        | order type，1-limit price；3-market price                     |                   |
| ├─ order_sub_type    | null      | No   |        | order subtype 1-passive；2-last price trggiered condition ；3-index triggered |                   |
| ├─ time_in_force     | null      | No   |        | time in force，0-Default Value； 1-available before cancel   |                   |
| ├─ minimal_quantity  | null      | No   |        | minimal trading quantity                               |                   |
| ├─ stop_price        | null      | No   |        | take profit & stop loss price                                   |                   |
| ├─ stop_condition    | null      | No   |        | symbol to indicate stop loss or take profit ，0-default；1-take profit；2-stop loss     |                   |
| ├─ order_status      | null      | No   |        | order status 0-unsubmitted,1-submitting,2-submitted,not matched,3-partly matched,4-all matched,5-partly canceled,6-all canceled,7-canceling,8-invalid,11- cached order(higher than condition),12-cached order(lower than condition)|                   |
| ├─ maker_fee_ratio   | null      | No   |        | Maker fee rate                                                |                   |
| ├─ taker_fee_ratio   | null      | No   |        | Taker fee rate                                                |                   |
| ├─ cl_order_id       | null      | No   |        | client order id                                                  |                   |
| ├─ filled_currency   | null      | No   |        | filled amount                                |                   |
| ├─ filled_quantity   | null      | No   |        | filled quantity                                  |                   |
| ├─ canceled_quantity | null      | No   |        | canceled quantity                               |                   |
| ├─ match_time        | null      | No   |        | match time                                                     |                   |
| ├─ position_effect   | null      | No   |        | position effect，1 open，2 close                             |                   |
| ├─ margin_type       | null      | No   |        | margin type，1 cross，2 isolated               |                   |
| ├─ margin_rate       | null      | No   |        | margin rate                                                     |                   |
| ├─ fc_order_id       | null      | No   |        | liquidation order id                                    |                   |
| ├─ delta_price       | null      | No   |        | difference between order price and mark price                    |                   |
| ├─ frozen_price      | null      | No   |        | frozen price   

## 4.9 switch position mode



### Basic information

**Path：** /api/v1/future/position/isolate

**Method：** POST

**API Description：**

purpose ：future - switch placed order postion mode（isolated to cross、cross to isolated）

**Body**

| Name           | Type   | Is required | Default Value | Note                     | Other Info |
| -------------- | ------ | -------- | ------ | ------------------------ | -------- |
| contractId     | string | Yes     |        | contract ID                 |          |
| initMarginRate | string | No   |        | initial margin rate0<${}<=1     |          |
| marginType     | string | Yes     |        | margin type，1 for cross，2 for isolated |          |
| posiSide       | integer | No  |        | 0：hedge，1：long，-1：short. default：0 |          |

### Return Data

| Name | Type   | Is required | Default Value | Note               | Other Info |
| ---- | ------ | -------- | ------ | ------------------ | -------- |
| code | number | No   |        | 0: succeed , not 0 failed |          |
| msg  | string | No   |        | Message msg            |          |

## 4.10 adjust margin 



### Basic information

**Path：** /api/v1/future/position/transferMargin

**Method：** POST

**API Description：**

Purpost：adjust the margin rate, also means to adjust leverage

**Body**

| Name       | type    | Is required | Default Value | Note                                       | Other Info |
| ---------- | ------- | -------- | ------ | ------------------------------------------ | -------- |
| contractId | integer | No   |        | Contract ID                                     |          |
| margin     | string  | No   |        | Margin（>0 : increase margin，<0 ： decrease margin） |          |
| posiSide   | integer  | No   |        | 0：hedge，1：long ，-1：short default：0 |          |

### Return Data

| Name | type   | Is required | Default Value | Note              | Other Info |
| ---- | ------ | -------- | ------ | ----------------- | -------- |
| code | number | No   |        | 0: succeed，not 0：failed |          |
| msg  | string | No   |        | message: msg           |          |
| data | object | No   |        |                   |          |

## 4.11 enquire forced-deleverage list



### Basic information

**Path：** /api/v1/future/queryForceLower

**Method：** GET

### Return Data

| Name          | type      | Is required | Default Value | Note                           | Other Info          |
| ------------- | --------- | -------- | ------ | ------------------------------ | ----------------- |
| code          | number    | No   |        | 0：succeed， not 0：failed             |                   |
| msg           | string    | No   |        | message msg                        |                   |
| data          | object [] | No   |        | data collection                       | item type: object |
| ├─ contractId | number    | No   |        | contract ID                         |                   |
| ├─ rank       | number    | No   |        | deleverage ranking，small value means high potential|                   |

## 4.12 get current matches



### Basic information

**Path：** /api/v1/future/queryHisMatch

**Method：** GET

**API Description：**

### Request Parameter

**Query**

| Parameter Name   | Is required | Sample | Note                        |
| ---------- | -------- | ---- | --------------------------- |
| contractId | No       |      | contract ID                      |
| pageSize   | No       |      | page size，default：100，<=1000 |

### Return Data

| Name                   | type      | Is required | Default Value | Note                                                     | Other Info          |
| ---------------------- | --------- | -------- | ------ | -------------------------------------------------------- | ----------------- |
| code                   | number    | No   |        |                                                  |                   |
| msg                    | string    | No   |        |                                       |                   |
| data                   | object [] | No   |        |                                        | item type: object |
| ├─ appl_id             | null      | No   |        | future ID,2                          |                   |
| ├─ match_time          | null      | No   |        | match time                     |                   |
| ├─ contract_id         | null      | No   |        | contract ID                                         |                   |
| ├─ exec_id             | null      | No   |        | executive id                                         |                   |
| ├─ bid_user_id         | null      | No   |        | bid userID                             |                   |
| ├─ ask_user_id         | null      | No   |        | ask userID                                      |                   |
| ├─ bid_order_id        | null      | No   |        | bid order id                                       |                   |
| ├─ ask_order_id        | null      | No   |        | ask user id                                      |                   |
| ├─ match_price         | null      | No   |        | match  price                                  |                   |
| ├─ match_qty           | null      | No   |        | match quantity                                    |                   |
| ├─ match_amt           | null      | No   |        | match amount                           |                   |
| ├─ bid_fee             | null      | No   |        | bid fee                                               |                   |
| ├─ ask_fee             | null      | No   |        | ask fee                                               |                   |
| ├─ taker_side          | null      | No   |        | Taker side                        |                   |
| ├─ side                | null      | No   |        | order side                                      |                   |
| ├─ update_time         | null      | No   |        | last update time                                     |                   |
| ├─ bid_position_effect | null      | No   |        | bid position effect                                     |                   |
| ├─ ask_position_effect | null      | No   |        | ask position effect                                |                   |
| ├─ bid_margin_type     | null      | No   |        | bid margin type                              |                   |
| ├─ ask_margin_type     | null      | No   |        | ask margin type                                |                   |
| ├─ bid_init_rate       | null      | No   |        | bid initiate rate                         |                   |
| ├─ ask_init_rate       | null      | No   |        | ask initiate rate                                       |                   |
| ├─ bid_match_type      | null      | No   |        | bid match type：0 normal 1 liquidated 2 deleverage（bankrupt）3 deleverage |                   |
| ├─ ask_match_type      | null      | No   |        | ask match type：0 normal 1 liquidated 2 deleverage（bankrupt）3 deleverage|                   |
| ├─ bid_pnl_type        | null      | No   |        | bid P&L type：0 normal match 1 normal close 2 liquidated 3 deleverage              |                   |
| ├─ bid_pnl             | null      | No   |        | bid profit & loss                        |                   |
| ├─ ask_pnl_type        | null      | No   |        | ask P&L type：0 normal match 1 normal close 2 liquidated 3 deleverage               |                   |
| ├─ ask_pnl             | null      | No   |        | ask profit & loss                              |                 |

## 4.13 adjust position mode

### Basic information

**Path：** /api/v1/future/position/adjustModel

**Method：** POST

**API Description：**

### Request Parameter

**Headers**

| Parameter Name     | Parameter Value           | Is required | Sample | Note |
| ------------ | ---------------- | -------- | ---- | ---- |
| Content-Type | application/json | Yes       |      |      |

**Body**

| Name           | type    | Is required | Default Value | Note                                             | Other Info   |
| -------------- | ------- | -------- | ------ | ------------------------------------------------ | ---------- |
| currencyId     | integer | No   |        | currency ID                                         |            |
| type           | integer | No   |        | position mode，0：hedge，1：                       |            |

### Return Data

| Name | type   | Is required | Default Value | Note               | Other Info |
| ---- | ------ | -------- | ------ | ------------------ | -------- |
| code | number | No   |        | 0: succeed , not 0 failed |          |
| msg  | string | No   |        | message msg            |          |
| data | object | No   |        | message collection           |          |

