package com.baiyuhui.huangyan.util;


import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

import com.loopj.android.http.AsyncHttpClient;

public class FinalAsyncHttpClient {

	AsyncHttpClient client;
	
	public FinalAsyncHttpClient() {
		client = new AsyncHttpClient();
		client.setConnectTimeout(10*1000);//5s超时
		if (Utils.getCookies() != null) {//每次请求都要带上cookie
			BasicCookieStore bcs = new BasicCookieStore();
			bcs.addCookies(Utils.getCookies().toArray(
					new Cookie[Utils.getCookies().size()]));
			client.setCookieStore(bcs);
		}
	}
	
	public AsyncHttpClient getAsyncHttpClient(){
		return this.client;
	}
	
	
}
