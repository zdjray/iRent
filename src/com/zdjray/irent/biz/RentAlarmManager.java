package com.zdjray.irent.biz;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RentAlarmManager {
	
	public void startClock() {
		Log.d(TAG, "start clock...");
		// ���AlarmManagerʵ��
		final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		// ʵ����Intent
		Intent intent = new Intent();
		// ����Intent action����
		intent.setAction(ACTION_CLOCK);
		final PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		// ���ϵͳʱ��
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.HOUR_OF_DAY) >= 9) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		final long triggerTime = calendar.getTimeInMillis();
		am.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, oneDay, pi);
	}
	
	public static RentAlarmManager getInstance(Context context) {
		if (instance == null) {
			instance = new RentAlarmManager(context);
		}
		
		return instance;
	}
	
	private RentAlarmManager(Context context) {
		this.context = context;
	}
	
	public final static String ACTION_CLOCK = "com.zdjray.irent.CLOCK";
	
	private static RentAlarmManager instance;
	private static long oneDay = 24 * 3600 * 1000;
	private Context context;
	private String TAG = this.getClass().getName();
}
