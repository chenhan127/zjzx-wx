package com.zjzx.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.zjzx.constant.WxConfig;

@Service
public class WxService {
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	TokenService tokenService;
	@Autowired
	WxConfig wxConfig;
	/**
	 * 根据code 获取 token和openide
	 * @param code
	 * @return
	 */
	public JSONObject getAccessTokenOpenid(String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type={grant_type}";
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("appid", wxConfig.getWx_appid());
		param.put("secret", wxConfig.getWx_appsecret());
		param.put("code", code);
		param.put("grant_type", "authorization_code");
		String result = restTemplate.getForObject(url, String.class, param);
		JSONObject resMap = JSONObject.parseObject(result);
		return resMap;
	}
	
	/**
	 * 根据openid 获取微信用户
	 * @param openid
	 * @return
	 */
	public JSONObject getWxUser(String openid,String access_token) {
		String url ="https://api.weixin.qq.com/sns/userinfo?access_token={access_token}&openid={openid}&lang=zh_CN";
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("access_token", access_token);
		param.put("openid", openid);
		String result = restTemplate.getForObject(url, String.class, param);
		try {
			result = new String(result.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject wxUser = JSONObject.parseObject(result);
		//{"country":"中国","province":"安徽","city":"池州","openid":"oGdVi1rLHHs7syGjGLgzWjiHok-0","sex":1,"nickname":"小,寒,💥","headimgurl":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKYViajevjoOuQcp8YRJCQqFAx2Q78Kaj0IffwP0ibVmbMUlTHCc5OkfXmM3sLWibWc8iazvCjE2EHFrA/132","language":"zh_CN","privilege":[]}
		return wxUser;
	}
	
	

}
