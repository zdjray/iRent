package com.zdjray.irent.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;

public class RentUtil {

	public static String generateUniqueKey() {
		return generateMD5(System.currentTimeMillis() + "");
	}

	public static String generateMD5(String key) {
		byte[] hash;

		try {
			hash = MessageDigest.getInstance("MD5").digest(key.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);

		for (byte b : hash) {
			int i = (b & 0xFF);
			if (i < 0x10)
				hex.append('0');
			hex.append(Integer.toHexString(i));
		}

		return hex.toString();
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static int tryParseInt(String str, int defaults) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return defaults;
		}
	}

	@SuppressLint("SimpleDateFormat")
	public static String formatDate(Calendar calendar) {
		calendar = (calendar == null) ? Calendar.getInstance() : calendar;
		// 设置日期输出的格式
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(calendar.getTime());
	}
	
	@SuppressLint("SimpleDateFormat")
	public static Calendar parseDate(String dateStr) {
		Calendar calendar = Calendar.getInstance();
		try {
			// 设置日期输出的格式
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			calendar.setTimeInMillis(df.parse(dateStr).getTime());
		} catch (Exception e) {
		}
		return calendar;
	}
	
	public static boolean isValidPhoneNo(String phoneNo) {
		return Pattern.compile("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)")
			.matcher(phoneNo).matches();
		
	}
}
