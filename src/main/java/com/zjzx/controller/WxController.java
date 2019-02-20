package com.zjzx.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSONObject;
import com.zjzx.service.JsApiTicketService;
import com.zjzx.service.WxService;

@Controller
@RequestMapping("wx")
public class WxController {
//	@RequestMapping(value = "login",method=RequestMethod.GET)
//	public void login(HttpServletRequest request,HttpServletResponse response){
//		System.out.println("success");
//		String signature = request.getParameter("signature");
//		String timestamp = request.getParameter("timestamp");
//		String nonce = request.getParameter("nonce");
//		String echostr = request.getParameter("echostr");
//		PrintWriter out = null;
//		try {
//			  out = response.getWriter();
//			if(CheckUtil.checkSignature(signature, timestamp, nonce)){
//				out.write(echostr);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			out.close();
//		}
//		
//	}
	@Autowired
	WxService wxService;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	JsApiTicketService jsApiTicketService;
	
	@RequestMapping(value = "login",method=RequestMethod.GET)
	public void login(String code,String state,HttpServletResponse response) throws IOException{
		JSONObject resMap = wxService.getAccessTokenOpenid(code);
		//{"access_token":"18_Ec6ReBaUA9OxU-SSczhnY5EgyWSNUPwbZn9aNYx0bK1wRzdcOiVuyM1-k5wVMbDiSBOKwvUFEfHBtD4K5wPYU1OYs9uDNQL-f4-vbdge0mk","refresh_token":"18_LeQeNnUgcvdpBGFg7w6YCuLAUDVm_dto0BU0DVXJI3zqgBOvqtL20KzN_hWYEs-5MUbdA0ea4TOCIEY7LHbs05k0iQibFgwiszNRppuKSQA","openid":"oGdVi1rLHHs7syGjGLgzWjiHok-0","scope":"snsapi_base","expires_in":7200}
		String openid = resMap.getString("openid");
		String access_token = resMap.getString("access_token");
		String redirec = state+"?openid="+openid+"&access_token="+access_token;
		response.sendRedirect(redirec);
		
	}
	@RequestMapping("getWxUser")
	@ResponseBody
	public JSONObject getWxUser(String openid,String access_token) {
		JSONObject wx_user = wxService.getWxUser(openid,access_token);
		System.out.println("openid="+openid);
		System.out.println("access_token="+access_token);
		System.out.println("wx_user="+wx_user);
//		String unionid = wx_user.getString("unionid");
//		System.out.println(unionid);
//		JSONObject userMap = restTemplate.getForObject(ZjzxServerConfig.URL+"/user/getUserByWxUid?uid="+unionid, JSONObject.class);
//		JSONObject app_user = userMap.getJSONObject("user");
//		JSONObject resMap = new JSONObject();
//		resMap.put("status", "success");
//		resMap.put("wx_user", wx_user);
//		resMap.put("app_user", app_user);
		return wx_user;
	}
	@RequestMapping("getJsSDKConfig")
	@ResponseBody
	public JSONObject getJsSDKConfig(String url) {
		JSONObject resMap = jsApiTicketService.getJsSDKConfig(url);
		return resMap;
	}
}
