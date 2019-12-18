package com.newcoin.api.demo;

import com.newcoin.api.demo.enums.VerbEnum;
import com.newcoin.api.demo.model.vo.OrderPlaceVo;
import com.newcoin.api.demo.model.vo.OrderQueryVo;
import com.newcoin.api.demo.service.impl.SignatureServiceImpl;
import com.newcoin.api.demo.utils.HttpUtil;
import com.newcoin.api.demo.utils.JacksonUtil;
import com.newcoin.api.demo.utils.URLEncoderUtil;

public class ApiOrderDemoMain {
	
	private static SignatureServiceImpl signatureService = new SignatureServiceImpl();
	
	private static final String host      = "";	//host
	private static final String accApikey = ""; //apikey 公钥
	private static final String secApikey = ""; //apikey 私钥
	
	public static void main(String[] args) {
		//order 查詢
//		String orderInfo = OrderQueryDemo();
//		System.out.println(orderInfo);
		
		//order 下单
		String orderInfo = OrderPlaceDemo();
		System.out.println(orderInfo);
	}
	
	public static String OrderPlaceDemo() {
		OrderPlaceVo orderPlaceVo = new OrderPlaceVo();
		orderPlaceVo.setContractId(999999);
		orderPlaceVo.setSide(1);		   //合约委托方向（买1，卖-1）
		orderPlaceVo.setQuantity("1");	   //數量
		orderPlaceVo.setOrderType(3);	   //合约委托类型（1（限价），3（市价） ）
		orderPlaceVo.setPositionEffect(1); //开平标志（开仓1，平仓2）
		orderPlaceVo.setMarginType(1);     //仓位模式（全仓1，逐仓2）
		
		String requestBody = JacksonUtil.toJson(orderPlaceVo);
		
		Long apiExpires = 1670965643L;				   //需要填充
		
		String urlPath = "/api/v1/future/order";	   //订单查询path
		
		//签名操作
		String signature = signatureService.signature(secApikey, VerbEnum.POST.name(), urlPath, apiExpires, requestBody);
		
		return HttpUtil.sendPost(host+urlPath, requestBody, signature, accApikey, apiExpires);
	}
	
	
	/**
	 * 订单查询步骤
	 * @return
	 */
	public static String OrderQueryDemo() {
		OrderQueryVo orderQueryVo = new OrderQueryVo();
		orderQueryVo.setOrderId("11570874456209677");  //需要填充
		Long apiExpires = 1670965643L;				   //需要填充
		
		String urlPath = "/api/v1/future/order";	   //订单查询path
		urlPath = urlPath+"?filter="+URLEncoderUtil.urlDecoderEncode(JacksonUtil.toJson(orderQueryVo));	//参数需要urlDecoderEncode 编码
		
		//签名操作
		String signature = signatureService.signature(secApikey, VerbEnum.GET.name(), urlPath, apiExpires, "");

		//向api服务发送GET请求
		return HttpUtil.get(host+urlPath, signature, accApikey, apiExpires);
	}
}
