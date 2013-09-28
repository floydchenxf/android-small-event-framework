package com.trade.goods.login.handler;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.floyd.event.EventContext;
import com.floyd.handler.AbstractConvertObjEventHandler;

public class LoginVOConvertEventHandler extends
		AbstractConvertObjEventHandler<LoginVO, byte[]> {

	@Override
	public LoginVO convertObj(final EventContext<byte[]> eventContext, byte[] args) {
		String result = new String(args);
		JSONTokener jsonTokener = new JSONTokener(result);
		JSONObject json = null;
		try {
			json = new JSONObject(jsonTokener);
		} catch (JSONException e) {
			eventContext.invokeError(333, "数据出问题了");
			return null;
		}
		
		boolean success = json.optBoolean("success");
		
		if (!success) {
			String message = json.optString("message");
			eventContext.invokeError(222, message);
			return null;
		}
		
		LoginVO loginVO = new LoginVO();
		return loginVO;
	}

}
