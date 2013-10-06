package com.trade.goods;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.floyd.event.Event;
import com.floyd.event.EventCallback;
import com.floyd.event.EventEmitter;
import com.floyd.event.EventEmitterGenerator;
import com.floyd.handler.HttpGetRequestEventHandler;
import com.floyd.request.RequestParam;
import com.trade.goods.main.handler.ProductTypeConvertEventHandler;
import com.trade.goods.main.handler.ProductTypeVO;

public class MainActivity extends Activity {

	EventEmitter emitter = EventEmitterGenerator.getInstance().generateEmitter();
	Event productType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		productType = Event.createInstance();
		EventCallback<List<ProductTypeVO>> key = new EventCallback<List<ProductTypeVO>>() {

			@Override
			public void onError(int code, String message) {
				Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(List<ProductTypeVO> productTypeVOs) {
				// TODO 创建adapte
			}

			@Override
			public void onProgress(int i) {
				// do nothing;
			}
		};
		productType.setNextHandler(new HttpGetRequestEventHandler(this))
				.setNextHandler(new ProductTypeConvertEventHandler())
				.setEventCallback(key);

		String urlFormat = this.getResources().getString(R.string.product_type_url);
		String url = String.format(urlFormat, 1, 1);

		RequestParam requestParam = new RequestParam();
		requestParam.url = url;

		emitter.fireEvent(productType, requestParam);
	}
}
