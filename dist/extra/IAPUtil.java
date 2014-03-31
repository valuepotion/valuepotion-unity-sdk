package com.valuepotion.sdk.extra;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;

import com.android.vending.billing.IInAppBillingService;

public class IAPUtil {
	public static class IAPItemDetail {
		String error;

		String title;
		String type;
		String description;
		String productId;
		String price;
		String priceCurrencyCode;

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getProductId() {
			return productId;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getPriceCurrencyCode() {
			return priceCurrencyCode;
		}

		public void setPriceCurrencyCode(String priceCurrencyCode) {
			this.priceCurrencyCode = priceCurrencyCode;
		}

		public long getPriceAmountMicros() {
			return priceAmountMicros;
		}

		public void setPriceAmountMicros(long priceAmountMicros) {
			this.priceAmountMicros = priceAmountMicros;
		}

		long priceAmountMicros;

		public double getPriceAmount() {
			return priceAmountMicros / 1000000.0;
		}

		public void fromJSON(String jsonstr) {
			JSONObject json;
			try {
				json = new JSONObject(jsonstr);
				title = json.getString("title");
				type = json.getString("type");
				description = json.getString("description");
				productId = json.getString("productId");
				price = json.getString("price");
				priceCurrencyCode = json.getString("price_currency_code");
				priceAmountMicros = json.getLong("price_amount_micros");
			} catch (JSONException e) {
				error = "JSON Parse Error";
			}
		}

		public void setError(int responseCode) {
			switch (responseCode) {
			case 0:
				error = null;
				break;
			case 1:
				error = "BILLING_RESPONSE_RESULT_USER_CANCELED";
				break;
			case 3:
				error = "BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE";
				break;
			case 4:
				error = "BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE";
				break;
			case 5:
				error = "BILLING_RESPONSE_RESULT_DEVELOPER_ERROR";
				break;
			case 6:
				error = "BILLING_RESPONSE_RESULT_ERROR";
				break;
			case 7:
				error = "BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED";
				break;
			case 8:
				error = "BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED";
				break;
			default:
				error = "UNKNOWN " + responseCode;
				break;
			}
		}
	}
	public static IAPItemDetail getSkuDetail(Context context, IInAppBillingService mService, String itemId) {
		ArrayList<String> skuList = new ArrayList<String>();
		skuList.add(itemId);
		Bundle querySkus = new Bundle();
		querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

		IAPItemDetail detail = new IAPItemDetail();
		try {
			Bundle skuDetails = mService.getSkuDetails(3, context.getPackageName(), "inapp", querySkus);
			int response = skuDetails.getInt("RESPONSE_CODE");
			if (response == 0) {
				ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
				String jsonstr = responseList.get(0);
				detail.fromJSON(jsonstr);
			} else {
				detail.setError(response);
			}
		} catch (RemoteException e) {
			detail.error = e.getMessage();
		}
		return detail;
	}
}
