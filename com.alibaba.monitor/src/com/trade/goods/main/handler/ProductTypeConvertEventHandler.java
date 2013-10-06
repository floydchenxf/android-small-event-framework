package com.trade.goods.main.handler;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.floyd.event.EventContext;
import com.floyd.handler.AbstractConvertObjEventHandler;
import com.floyd.request.HttpErrorInfo;

public class ProductTypeConvertEventHandler extends
		AbstractConvertObjEventHandler<List<ProductTypeVO>, byte[]> {

	@Override
	public List<ProductTypeVO> convertObj(EventContext<byte[]> eventContext,
			byte[] args) {
		List<ProductTypeVO> resultList = new ArrayList<ProductTypeVO>();
		String result = new String(args);
		JSONTokener jsonTokener = new JSONTokener(result);
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(jsonTokener);

			if (jsonArray == null || jsonArray.length() <= 0) {
				eventContext.invokeError(
						HttpErrorInfo.PARSE_CONTENT_ERROR.code,
						HttpErrorInfo.PARSE_CONTENT_ERROR.message);
				return null;
			}

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				ProductTypeVO productTypeVO = convertVO(json);
				resultList.add(productTypeVO);
			}
		} catch (JSONException e) {
			eventContext.invokeError(HttpErrorInfo.PARSE_CONTENT_ERROR.code,
					HttpErrorInfo.PARSE_CONTENT_ERROR.message);
			return null;
		}

		if (resultList.isEmpty()) {
			return null;
		}

		return resultList;
	}

	private ProductTypeVO convertVO(JSONObject json) throws JSONException {
		ProductTypeVO productTypeVO = new ProductTypeVO();
		Long id = json.optLong("id");
		String name = json.optString("typeName");
		Long companyId = json.optLong("companyId");
		productTypeVO.setId(id);
		productTypeVO.setName(name);
		productTypeVO.setCompanyId(companyId);
		
		boolean topProductExists = json.has("topProductDOs");
		if (topProductExists) {
			JSONArray topProducts = json.getJSONArray("topProductDOs");
			for(int j =0; j < topProducts.length(); j++) {
				JSONObject productJson = topProducts.getJSONObject(j);
				Long pid = productJson.getLong("id");
				String pPic = productJson.getString("productPic");
				String pName = productJson.getString("productName");
				ProductVO productVO = new ProductVO();
				productVO.setId(pid);
				productVO.setProductName(pName);
				productVO.setProductPic(pPic);
				productTypeVO.addProduct(productVO);
			}
		}
		return productTypeVO;
	}

}
