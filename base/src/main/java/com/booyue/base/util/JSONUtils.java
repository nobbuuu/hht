package com.booyue.base.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public final class JSONUtils {
	private static final String TAG = "JSONUtils";

	private JSONUtils(){} 

	private static final Gson gson = new Gson();

	public static <T> T fromJson(String json, Class<T> classOfT ){

		if(isJSONValid(json)){
			try{
				
				T fromJson = new Gson().fromJson(json, classOfT);

				return fromJson;
				
			}catch(JsonSyntaxException e){
				e.printStackTrace();
				return null;
			}
		}else{
			
			return null;
		}

	}

	public static String toJson(Object bean){
		return new Gson().toJson(bean);
	}

	/**
	 * 是否有效
	 * @param jsonInString
	 * @return
     */
	public static boolean isJSONValid(String jsonInString) {

		try { 
			gson.fromJson(jsonInString, Object.class);
			return true; 
		} catch(com.google.gson.JsonSyntaxException ex) { 
			return false; 
		} 
	}


}
