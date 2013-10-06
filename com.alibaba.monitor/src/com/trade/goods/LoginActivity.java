package com.trade.goods;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.floyd.event.Event;
import com.floyd.event.EventCallback;
import com.floyd.event.EventEmitter;
import com.floyd.event.EventEmitterGenerator;
import com.floyd.event.FiredEvent;
import com.floyd.handler.HttpPostRequestEventHandler;
import com.floyd.request.RequestParam;
import com.trade.goods.login.handler.LoginVO;
import com.trade.goods.login.handler.LoginVOConvertEventHandler;

public class LoginActivity extends Activity {

	TextView loginButton;

	Dialog dataLoadDialog;

	EditText unameView;

	EditText passwdView;

	EventEmitter eventEmitter = EventEmitterGenerator.getInstance()
			.generateEmitter();
	
	FiredEvent firedEvent;
	Event loginEvent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		dataLoadDialog = new Dialog(this, R.style.data_load_dialog);
		ProgressBar bar = new ProgressBar(this);
		dataLoadDialog.setContentView(bar);
		dataLoadDialog.setCanceledOnTouchOutside(false);
		
		dataLoadDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface arg0) {
				if (firedEvent != null) {
					firedEvent.cancel();
				}
				
			}
		});

		unameView = (EditText) findViewById(R.id.uname);
		passwdView = (EditText) findViewById(R.id.password);

		loginButton = (TextView) findViewById(R.id.login_button);

		EventCallback<LoginVO> loginCallback = new EventCallback<LoginVO>() {

			@Override
			public void onError(int code, String message) {
				dataLoadDialog.dismiss();
				Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(LoginVO t) {
				dataLoadDialog.dismiss();
				Intent it = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(it);
				LoginActivity.this.finish();
			}

			@Override
			public void onProgress(int i) {
				// do nothing;
			}

		};

		loginEvent = Event.createInstance();
		loginEvent.setNextHandler(new HttpPostRequestEventHandler(this))
				.setNextHandler(new LoginVOConvertEventHandler())
				.setEventCallback(loginCallback);
		final String url = this.getResources().getString(R.string.login_url);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String uname = unameView.getText().toString();
				String password = passwdView.getText().toString();

				if (uname.trim().equals("") || password.trim().equals("")) {
					Toast.makeText(LoginActivity.this, R.string.input_login_info, Toast.LENGTH_SHORT).show();
					return;
				}

				Map<String, String> params = new HashMap<String, String>();
				params.put("uname", uname);
				params.put("upwd", password);
				RequestParam requestParam = new RequestParam();
				requestParam.url = url;
				requestParam.params = params;

				dataLoadDialog.show();
				firedEvent = eventEmitter.fireEvent(loginEvent, requestParam);
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

}
