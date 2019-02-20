package com.zjzx.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WxConfig {
	
	//  public static final String WX_APPID ="wxe0b93ff5c5898740";
	//	public static final String WX_APPSECRET ="785e918ce29bec337d7c95c2cb7a2f2c";
	
	@Value("${wx.app.appid}")
	private String WX_APPID;
	
	@Value("${wx.app.appsecert}")
	private String WX_APPSECRET;
	
	@Value("${wx.app.jssdk.debug}")
	private boolean  JSSDK_DEBUG;
	
	
	public  String getWx_appid() {
		return this.WX_APPID;
	}
	
	public String getWx_appsecret() {
		return this.WX_APPSECRET;
	}
	
	public boolean  getJSsdk_debug() {
		return this.JSSDK_DEBUG;
	}
}
