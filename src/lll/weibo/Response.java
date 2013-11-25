package lll.weibo;

import org.json.JSONException;
import org.json.JSONObject;

public class Response extends JSONObject {

	final static int STATUS_CODE_UNKNOWN = -1;
	final static int STATUS_CODE_SUCCESS = 0;
	final static int STATUS_CODE_ERROR = 1;

	final static String SESSION_UNKNOWN = "";
	
	final static String DETAIL_UNKNOWN = "";
	
	static Response Success() {
		return new Response(STATUS_CODE_SUCCESS);
	}
	
	static Response Error() {
		return new Response(STATUS_CODE_ERROR);
	}
	
	Response(int statusCode) {
		SetStatusCode(statusCode);
	}
	
	Response SetStatusCode(int statusCode) {
		try {
			put("statusCode", statusCode);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	int GetStatusCode() {
		try {
			return getInt("statusCode");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return STATUS_CODE_UNKNOWN;
	}
	
	Response SetSession(String session) {
		try {
			put("session", session);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	String GetSession() {
		try {
			return getString("session");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return SESSION_UNKNOWN;
	}
	
	Response SetExtra(String extra) {
		try {
			put("extra", extra);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	String GetExtra() {
		try {
			return getString("extra");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return DETAIL_UNKNOWN;
	}
	
}