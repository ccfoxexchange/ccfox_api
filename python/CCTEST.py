from ccfox import ccfoxClient

ccfox=ccfoxClient('API','secret')


#1.1获取所有合约信息
#print(ccfox.list_futureQueryContract())
    
#1.2获取所有币种信息
#print(ccfox.list_commonQueryCurrency())

#1.3获取所有法币
#print(ccfox.list_exchange())

#1.4获取计价货币的价格  当前USD价格
#print(ccfox.list_exchangecoins())

#1.5查询期货交割历史
#print(ccfox.get_queryContractDeliveryList('2'))


#2.1获取24小时统计  24H,30D,7D成交额
#print(ccfox.get_queryMarketStat())

#2.2获取K线数据
#print(ccfox.get_queryCandlestick('1000008','60000'))

#2.3获取单个合约行情快照 bids与asks结构
#print(ccfox.list_querySnapshot('1000008'))

#2.4 获取所有行情快照
#print(ccfox.list_queryIndicatorList())

#2.5 获取逐笔成交
#print(ccfox.list_queryTickTrade('1000008'))


#3.1获取用户基本信息
#print(ccfox.get_userInfo())
    
#3.2获取用户资产信息
#print(ccfox.get_usermargin())
    
#3.3获取用户持仓信息
#print(ccfox.get_position())

#3.4 获取用户合约保证金梯度
#print(ccfox.get_queryVarietyMargin(6,1000003))


#4.1 合约下单
#print(ccfox.future_order(contractId='1000003',side='-1',price=202,quantity=1,orderType='1',positionEffect='1',marginType='1',marginRate='0'))

#4.2 合约批量下单
#ccfox.future_orders

#4.3 合约撤单
#print(ccfox.delete_order('{"contractId":1000003,"originalOrderId":"11570874455904760"}'))

#4.4 合约批量撤单
#ccfox.delete_orders

#4.5 合约撤消所有订单
#print(ccfox.delete_allorder())

#4.6 获取订单信息
#print(ccfox.get_order('{"orderId":"11570874455895583"}'))

#4.7 获取当前订单列表 
#print(ccfox.get_queryActiveOrder())
    
#4.8 获取历史委托 
#print(ccfox.get_queryLastestHistoryOrders())
    
#4.9 切换仓位模式
#print(ccfox.post_positionisolate('1000003','1','0')) #全仓

#4.10 调整保证金率
#print(ccfox.post_transferMargin('1000003','?'))

#4.11 获取强减队列
#print(ccfox.get_queryForceLower())

#4.12 获取当前成交
#print(ccfox.get_queryMatch())
