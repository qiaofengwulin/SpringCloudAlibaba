package com.qh.api.wxsdk;

import com.qh.utils.MD5Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class WXsignUtil {
	public static String getSign(Map<String, String> paraMap, String paternerKey) {
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, String> entry : paraMap.entrySet()) {
			if (entry.getValue() != "") {
				list.add(entry.getKey() + "=" + entry.getValue() + "&");
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result += "key=" + paternerKey;
		// Util.log("Sign Before MD5:" + result);
		System.out.println(result);
		result = MD5Util.MD5Encode(result).toUpperCase();
		// Util.log("Sign Result:" + result);
		return result;
	}
}