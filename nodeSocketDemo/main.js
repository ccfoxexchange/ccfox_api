var socket = require('socket.io-client')('https://futurews.ccfox.com/')
var crypto = require('crypto')

/**
 * @name 数据准备
 */
var verb = 'GET'
var path = 'GET/realtime'
var accessKey = 'xxxxxxx'
var secretKey = 'xxxxxx'
var expires = 1583228136
var contractId = 1000000 //合约ID
var ranges = '900000' //k线分辨率

var signature = crypto
  .createHmac('sha256', secretKey)
  .update(verb + path + expires)
  .digest('hex')

socket.on('connect', function() {
  console.log('socket connect!')
  socket.emit('auth', {
    header: {
      type: 1001
    },
    body: {
      apiKey: accessKey,
      expires: expires,
      signature: signature
    }
  })

  socket.emit('subscribe', {
    header: {
      type: 1003
    },
    body: {
      topics: [
        {
          topic: 'realtime'
        },
        {
          topic: 'future_all_indicator'
        },
        {
          topic: 'exchange'
        },
        {
          topic: 'coin_price'
        },
        {
          topic: 'future_snapshot_indicator',
          params: {
            symbols: [
              {
                symbol: contractId
              }
            ]
          }
        },
        {
          topic: 'future_snapshot_depth',
          params: {
            symbols: [
              {
                symbol: contractId
              }
            ]
          }
        },
        {
          topic: 'future_tick',
          params: {
            symbols: [
              {
                symbol: contractId
              }
            ]
          }
        },
        {
          topic: 'notice'
        },
        {
          topic: 'match'
        },
        {
          topic: 'future_kline',
          params: {
            symbols: [
              {
                symbol: contractId,
                ranges: [ranges]
              }
            ]
          }
        }
      ]
    }
  })
})
socket.on('auth', function(e) {
  console.log(e, 'auth')
})
// socket.on('realtime', function(e) {
//   console.log(e, 'realtime')
// })
// socket.on('future_snapshot_indicator', function(e) {
//   console.log(e, 'future_snapshot_indicator')
// })
socket.on('future_kline', function(e) {
  console.log(e, 'future_kline')
})
