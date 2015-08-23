package com.zdjray.irent.biz;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import android.util.Log;

import com.zdjray.irent.R;
import com.zdjray.irent.biz.beans.RoomInfo;
import com.zdjray.irent.config.ConfigManager;

public class CacheManager implements ConfigManager {

	private CacheManager(Context context) {
		this.context = context;
		loadConfig();
	}
	
	private void loadConfig() {
		Log.d(TAG, "begin loading config...");
		this.roomInfos = new HashMap<String, RoomInfo>();
		this.roomInfoKeyList = new HashSet<String>();
		
		try {
			this.roomInfoKeyList = loadRoomInfoKeyList();
			Log.d(TAG, "loaded keys: " + roomInfoKeyList);
		} catch (Exception e) {
			// do nothing
		}
		
		for (Iterator<String> iterator = roomInfoKeyList.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			RoomInfo roomInfo = null;
			try {
				roomInfo = loadRoomInfo(key);
				Log.d(TAG, "loaded room info: " + roomInfo.getContact().getRoomNo());
			} catch(NullPointerException e) {
				this.roomInfoKeyList.remove(key);
				this.roomInfos.remove(key);
				try {
					saveRoomInfoKeyList(roomInfoKeyList);
					deleteRoomInfo(key);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Log.d(TAG, "load room info error:  " + "can't find room no" + key);
			}
			catch (Exception e) {
				Log.d(TAG, "load room info error:  " + e.getCause());
			}
			
			if (roomInfo != null) {
				this.roomInfos.put(key, roomInfo);
			}
		}
	}

	public static CacheManager getInstance(Context context) {
		if (instance == null) {
			instance = new CacheManager(context);
		}
		
		return instance;
	}

	public Map<String, RoomInfo> getRoomInfos() {
		return this.roomInfos;
	}
	
	public RoomInfo getRoomInfo(String roomNo) {
		RoomInfo roomInfo = roomInfos.get(roomNo);
		if (roomInfo == null) {
			return new RoomInfo();
		}
		return roomInfo;
	}
	
	public void setRoomInfo(String key, RoomInfo roomInfo) throws IOException {
		this.roomInfos.put(key, roomInfo);
		saveRoomInfo(key, roomInfo);
	}

	public Set<String> getRoomInfoKeyList() {
		return roomInfoKeyList;
	}
	
	public void setRoomInfoKeyList(Set<String> roomInfoKeyList) throws IOException {
		this.roomInfoKeyList = roomInfoKeyList;
		saveRoomInfoKeyList(roomInfoKeyList);
	}
	
	public void removeRoomInfo(String roomno) throws IOException {
		roomInfoKeyList.remove(roomno);
		saveRoomInfoKeyList(roomInfoKeyList);
		roomInfos.remove(roomno);
		deleteRoomInfo(roomno);
		Log.d(TAG, "roomno " + roomno + " deleted");
	}
	
	@Override
	public RoomInfo loadRoomInfo(String key) throws OptionalDataException, ClassNotFoundException, IOException {
		byte[] roomInfoBytes = Base64.decode(
				getSharedPreferences(getString(R.string.roominfo_file_name), Context.MODE_PRIVATE).getString(key, ""),
				Base64.DEFAULT);
		ByteArrayInputStream bais = new ByteArrayInputStream(roomInfoBytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (RoomInfo) ois.readObject();
	}

	@Override
	public void saveRoomInfo(String key, RoomInfo roomInfo) throws IOException {
		ByteArrayOutputStream roomInfoBytes = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(roomInfoBytes);
		oos.writeObject(roomInfo);
		String roomInfoBase64 = Base64.encodeToString(roomInfoBytes.toByteArray(), Base64.DEFAULT);

		Editor editor = getSharedPreferences(getString(R.string.roominfo_file_name), Context.MODE_PRIVATE).edit();// 获取编辑器
		editor.putString(key, roomInfoBase64);
		editor.commit();
	}
	
	@Override
	public void deleteRoomInfo(String key) throws IOException {
		Editor editor = getSharedPreferences(getString(R.string.roominfo_file_name), Context.MODE_PRIVATE).edit();// 获取编辑器
		editor.remove(key);
		editor.commit();
	}

	@Override
	public Set<String> loadRoomInfoKeyList() throws OptionalDataException, ClassNotFoundException, IOException {
		return getSharedPreferences(getString(R.string.roominfo_file_name), Context.MODE_PRIVATE).getStringSet(
				getString(R.string.roominfo_key), new HashSet<String>());
	}

	@Override
	public void saveRoomInfoKeyList(Set<String> roomInfoList) throws IOException {
		Editor editor = getSharedPreferences(getString(R.string.roominfo_file_name), Context.MODE_PRIVATE).edit();// 获取编辑器
		editor.putStringSet(getString(R.string.roominfo_key), roomInfoList);
		editor.commit();
	}

	private String getString(int resId) {
		return context.getString(resId);
	}

	private SharedPreferences getSharedPreferences(String name, int mode) {
		return context.getSharedPreferences(name, mode);
	}

	private static CacheManager instance;
	private Map<String, RoomInfo> roomInfos;
	private Set<String> roomInfoKeyList;
	private Context context;
	private String TAG = this.getClass().getName();
}
