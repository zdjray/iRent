package com.zdjray.irent.biz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;

import com.zdjray.irent.MainActivity;
import com.zdjray.irent.R;
import com.zdjray.irent.biz.beans.RoomInfo;

public class RentAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "received alarm");
		
		ArrayList<String> nameList = getNames(context);
		String msg = nameList.toString();
		
		if (nameList.size() > 0) {
			Intent intentGo = new Intent(context, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentGo, 0);
			
			Notification noti = new Notification.Builder(context)
			.setContentTitle(context.getString(R.string.notify_title))
			.setContentText(context.getString(R.string.notify_content_template).replace("{names}", msg))
			.setSmallIcon(R.drawable.ic_home)
			.setTicker(context.getString(R.string.notify_ticker))
			.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
			.setContentIntent(pendingIntent)
			.build();
			
			NotificationManager notiManage = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			notiManage.notify(0x1986, noti);
		}
	}
	
	private ArrayList<String> getNames(Context context) {
		ArrayList<String> nameList = new ArrayList<String>();
		Collection<RoomInfo> roomInfos = CacheManager.getInstance(context).getRoomInfos().values();
		
		for (Iterator<RoomInfo> iterator = roomInfos.iterator(); iterator.hasNext();) {
			RoomInfo roomInfo = (RoomInfo) iterator.next();
			if (roomInfo.getRentInfo().getRentEndDate().getTimeInMillis() - notifyPreDay < Calendar.getInstance().getTimeInMillis()) {
				nameList.add(roomInfo.getContact().getName() + "(" + roomInfo.getContact().getRoomNo() + ")");
			}
		}
		return nameList;
	}

	private long notifyPreDay = 7 * 24 * 3600 * 1000;
	private String TAG = this.getClass().getName();

}
