package com.floyd.handler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.floyd.event.EventContext;
import com.floyd.event.EventHandler;
import com.floyd.request.HttpCallback;
import com.floyd.request.HttpErrorInfo;
import com.floyd.request.Request;
import com.floyd.request.RequestFactory;
import com.floyd.request.RequestMethod;
import com.floyd.request.RequestParam;

public class HttpPostRequestEventHandler implements
		EventHandler<RequestParam> {

	private Context context;

	public HttpPostRequestEventHandler(Context context) {
		this.context = context;
	}

	public NetworkInfo getNetworkInfo(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
		return networkInfo;
	}

	@Override
	public void handler(final EventContext<RequestParam> eventContext, RequestParam args) {
		boolean valid = false;
		NetworkInfo networkInfo = getNetworkInfo(context);
		if (networkInfo != null) {
			valid = networkInfo.isConnectedOrConnecting();
		}

		if (!valid) {
			eventContext.invokeError(HttpErrorInfo.NETWORK_INVALIDABLE_ERROR.code, HttpErrorInfo.NETWORK_INVALIDABLE_ERROR.message);
			return;
		}

		Request request = RequestFactory.getRequest(RequestMethod.POST,
				args.url, args.params, new HttpCallback() {

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
