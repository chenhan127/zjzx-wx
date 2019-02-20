package com.zjzx.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.zjzx.constant.WxConfig;

@Service
public class JsApiTicketService {
	private String jsapi_ticket;

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	TokenService tokenService;
	@Autowired
	WxConfig wxConfig;

	@Scheduled(fixedDelay = 1000 * 7200)
	public void RefreshJsApiTicket() {
		String jsapiUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={access_token}&type=jsapi";
		JSONObject param = new JSONObject();
		param.put("access_token", tokenService.getAccess_token());
		String result = restTemplate.getForObject(jsapiUrl, String.class, param);
		JSONObject resMap = JSONObject.parseObject(result);
		System.out.println(resMap);
		String jsapi_ticket = resMap.getString("ticket");
		this.jsapi_ticket = jsapi_ticket;
		System.out.println("jsapi_ticket刷新：" + jsapi_ticket);
	}

	public String getJsapi_ticket() {
		if (jsapi_ticket == null) {
			RefreshJsApiTicket();
		}
		return this.jsapi_ticket;
	}

	public JSONObject getJsSDKConfig(String url) {
		// TODO Auto-generated method stub
		String rand = "chenhan"; // 随机字符串
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);

		String string1 = "";
		String signature = "";
		// System.out.println("SHA1签名 :"+str);
		string1 = "jsapi_ticket=" + this.jsapi_ticket + "&noncestr=" + rand + "&timestamp=" + timestamp + "&url=" + url;
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("签名1:" + signature);
		// System.out.println("签名2:"+ SHA1Util.encode(string1) );
		JSONObject obj = new JSONObject();
		obj.put("timestamp", timestamp);
		obj.put("nonceStr", rand);
		obj.put("signature", signature);
		obj.put("debug", wxConfig.getJSsdk_debug());
		obj.put("appId", wxConfig.getWx_appid());
		return obj;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
