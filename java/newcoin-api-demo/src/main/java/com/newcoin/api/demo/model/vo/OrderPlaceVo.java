package com.newcoin.api.demo.model.vo;

public class OrderPlaceVo {
	
	//合约ID
	private Integer contractId;
	
	//合约名称，与contractId 传一个即可
	private String symbol;
	
	//客户端委托ID,如果不传使用uuid
	private String clientOrderId;
	
	//买1，卖-1
	private Integer side;
	
	//委托价格,order_type等于3（市价）时非必填
	private String  price;
	
	//委托数量
	private String  quantity;
	
	//委托类型，1（限价），3（市价）
	private Integer orderType;
	
	//开平标志，开仓1，平仓2
	private Integer positionEffect;
	
	//保证金类型，全仓1，逐仓2
	private Integer marginType;
	
	//保证金率，全仓时0，逐仓时>0
	private String  marginRate;
	
	//0（默认值），1（被动委托），2（最近价触发条件委托），3（指数触发条件委托），4（标记价触发条件委托）
	private Integer orderSubType;
	
	//止损价格，order_type等于3（市价）时非必填
	private String  stopPrice;
	
	public Integer getContractId() {
		return contractId;
	}
	public String getClientOrderId() {
		return clientOrderId;
	}
	public void setClientOrderId(String clientOrderId) {
		this.clientOrderId = clientOrderId;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	public Integer getSide() {
		return side;
	}
	public void setSide(Integer side) {
		this.side = side;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public String getStopPrice() {
		return stopPrice;
	}
	public void setStopPrice(String stopPrice) {
		this.stopPrice = stopPrice;
	}
	public Integer getPositionEffect() {
		return positionEffect;
	}
	public void setPositionEffect(Integer positionEffect) {
		this.positionEffect = positionEffect;
	}
	public Integer getMarginType() {
		return marginType;
	}
	public void setMarginType(Integer marginType) {
		this.marginType = marginType;
	}
	public String getMarginRate() {
		return marginRate;
	}
	public void setMarginRate(String marginRate) {
		this.marginRate = marginRate;
	}
	public Integer getOrderSubType() {
		return orderSubType;
	}
	public void setOrderSubType(Integer orderSubType) {
		this.orderSubType = orderSubType;
	}
}
