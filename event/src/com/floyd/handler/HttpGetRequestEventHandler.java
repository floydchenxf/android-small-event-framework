package com.floyd.handler;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.floyd.event.EventContext;
import com.floyd.event.EventHandler;
import com.floyd.request.HttpCallback;
import com.floyd.request.Request;
import com.floyd.request.RequestFactory;
import com.floyd.request.RequestMethod;

public class HttpGetRequestEventHandler implements EventHandler<String>{
	
	private Context context;
	
	public HttpGetRequestEventHandler(Context context) {
		this.context = context;
	}
	
	public NetworkInfo getNetworkInfo(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
		return networkInfo;
	}


	@Override
	public void handler(final EventContext<String> eventContext, String url) {
		
		boolean valid = false;
		NetworkInfo networkInfo = getNetworkInfo(context);
		if (networkInfo != null) {
			valid = networkInfo.isConnectedOrConnecting();
		}
		
		if (!valid) {
			eventContext.invokeError(999, "网络不可用");
			return;
		}
		
		Request request = RequestFactory.getRequest(RequestMethod.GET, url, null, new HttpCallback() {
			
			@Override
			public void onSuccess(Object obj) {
				eventContext.invokeNext(obj);
			}
			
			@Override
			public void onProgress(int i) {
				eventContext.invokeProgress(i);
			}
			
			@Override
			public void onError(int code, String message) {
				eventContext.invokeError(code, message);
			}
		});
		request.request();
	}

}
