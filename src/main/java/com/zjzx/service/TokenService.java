package com.zjzx.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.zjzx.constant.WxConfig;

@Service
public class TokenService {
	private String access_token;
	@Autowired
	WxConfig wxConfig;
	
	@Autowired
	RestTemplate restTemplate;
	@Scheduled(fixedDelay=1000*7200)
	public void RefreshToken() {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appid}&secret={secret}";
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("appid", wxConfig.getWx_appid());
		map.put("secret", wxConfig.getWx_appsecret());
		String result = restTemplate.getForObject(url, String.class, map);
		JSONObject resMap = JSONObject.parseObject(result);
		System.out.println(resMap);
		String access_token = resMap.getString("access_token");
		this.access_token = access_token;
	}
	
	public String getAccess_token() {
		if(access_token == null) {
			RefreshToken();
		}
		return this.access_token;
	}

}
