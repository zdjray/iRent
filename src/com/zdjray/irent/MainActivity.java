package com.zdjray.irent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zdjray.irent.biz.CacheManager;
import com.zdjray.irent.biz.RentAlarmManager;
import com.zdjray.irent.biz.beans.RoomInfo;
import com.zdjray.irent.util.RentUtil;

public class MainActivity extends ActionBarActivity {

	public final static String EXTRA_MESSAGE = "com.zdjray.irent.MESSAGE";
	public final static String ACTION_REFRESH_LIST = "com.zdjray.irent.REFRESHMAINLIST";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		RentAlarmManager.getInstance(this).startClock();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_settings:
			Log.d(TAG, "Menu settings fired");
			openSettings();
			return true;
		case R.id.action_add_contact:
			Log.d(TAG, "Menu add contact fired");
			openAddContact();
			return true;
		case R.id.action_about:
			Log.d(TAG, "Menu about fired");
			openAbout();
			return true;
		case R.id.action_search:
			Log.d(TAG, "Menu search fired");
			openSearch();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends ListFragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			return rootView;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(ACTION_REFRESH_LIST);
			getActivity().registerReceiver(refreshBroadcastReceiver, intentFilter);
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			
			updateList();
			registerForContextMenu(this.getListView());
		}
		
		@Override
		public void onDestroy() {
			super.onDestroy();
			getActivity().unregisterReceiver(refreshBroadcastReceiver);
		}
		
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
			super.onCreateContextMenu(menu, v, menuInfo);
			MenuInflater inflater = this.getActivity().getMenuInflater();
		    inflater.inflate(R.menu.main_list_context, menu);
		}
		
		@Override
		public boolean onContextItemSelected(MenuItem item) {
			
			try {
				AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
				switch (item.getItemId()) {
				
				case R.id.action_main_delete:
					String roomno = getSortedRoomInfos(sortType).get(info.position).getKey();
					CacheManager.getInstance(getActivity()).removeRoomInfo(roomno);
					updateList();
					
					// tips
					Toast.makeText(getActivity(), getString(R.string.tips_list_delete_succeed), Toast.LENGTH_SHORT).show();
					return true;
					
				default:
					return super.onContextItemSelected(item);
				}
			} catch (Exception e) {
				Toast.makeText(getActivity(), getString(R.string.tips_list_delete_fail), Toast.LENGTH_SHORT).show();
				return super.onContextItemSelected(item);
			}
		}
		
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Log.d(TAG, "item clicked: position=" + position + ", id=" + id);
			
			Intent intent = new Intent(getActivity(), AddContactActivity.class);
			String roomNo = getSortedRoomInfos(sortType).get(position).getKey();
			intent.putExtra(EXTRA_MESSAGE, roomNo);
			startActivity(intent);
		};
		
		private ArrayList<Map.Entry<String, RoomInfo>> getSortedRoomInfos(int sortType) {
			Map<String, RoomInfo> roomInfos = CacheManager.getInstance(getActivity()).getRoomInfos();

			ArrayList<Map.Entry<String, RoomInfo>> infoList = new ArrayList<Map.Entry<String, RoomInfo>>(
					roomInfos.entrySet());
			Comparator<Map.Entry<String, RoomInfo>> comparator = null;
			switch (sortType) {
			case BY_ROOMNO:
				comparator = new Comparator<Map.Entry<String, RoomInfo>>() {
					@Override
					public int compare(Entry<String, RoomInfo> lhs, Entry<String, RoomInfo> rhs) {
						return lhs.getValue().getContact().getRoomNo() - rhs.getValue().getContact().getRoomNo();
					}
				};
				break;
			case BY_DATE:
			default:
				comparator = new Comparator<Map.Entry<String, RoomInfo>>() {
					@Override
					public int compare(Entry<String, RoomInfo> lhs, Entry<String, RoomInfo> rhs) {
						return lhs.getValue().getRentInfo().getRentEndDate()
								.compareTo(rhs.getValue().getRentInfo().getRentEndDate());
					}
				};
				break;
			}
			Collections.sort(infoList, comparator);
			return infoList;
		}

		private List<Map<String, Object>> getData() {

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

			for (int i = 0; i < roomInfoList.size(); i++) {
				RoomInfo roomInfo = roomInfoList.get(i).getValue();

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("img", R.drawable.ic_action_person);
				map.put("roomno", roomInfo.getContact().getRoomNo());
				map.put("name", roomInfo.getContact().getName());
				map.put("phoneno", roomInfo.getContact().getPhoneNo());
				map.put("duedate", RentUtil.formatDate(roomInfo.getRentInfo().getRentEndDate()));
				list.add(map);
			}
			return list;
		}

		// broadcast receiver
		private BroadcastReceiver refreshBroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				Log.d(TAG, "recevice broadcast action: " + action);
				if (action.equals(ACTION_REFRESH_LIST)) {
					updateList();
				}
			}
		};
		
		private void updateList() {
			Log.d(TAG, "list updated");
			updateData();
			// update list
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), getData(), R.layout.list_mainlist, from, to) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					View view = super.getView(position, convertView, parent);

					int week = 7 * 24 * 3600 * 1000;
					Calendar due = roomInfoList.get(position).getValue().getRentInfo().getRentEndDate();
					Calendar now = Calendar.getInstance();
					if (due.getTimeInMillis() - week < now.getTimeInMillis()) {
						Log.d(TAG, "update color: " + position);
						((TextView) view.findViewById(R.id.mainlist_duedate_pre)).setTextColor(getResources()
								.getColorStateList(R.color.red));
						((TextView) view.findViewById(R.id.mainlist_duedate)).setTextColor(getResources()
								.getColorStateList(R.color.red));
					}

					return view;
				}
			};
			setListAdapter(adapter);
			adapter.notifyDataSetChanged();
		}

		private void updateData() {
			roomInfoList = getSortedRoomInfos(sortType);
		}

		private static final int BY_DATE = 0;
		private static final int BY_ROOMNO = 1;
		private static String[] from = new String[] { "img", "roomno", "name", "phoneno", "duedate" };
		private static int[] to = new int[] { R.id.mainlist_img, R.id.mainlist_roomno, R.id.mainlist_name,
				R.id.mainlist_phoneno, R.id.mainlist_duedate };
		private int sortType = BY_DATE;
		ArrayList<Map.Entry<String, RoomInfo>> roomInfoList;
		private String TAG = this.getClass().getName();
	}

	private void openSettings() {
		Toast.makeText(this, getString(R.string.tips_underconstrutction), Toast.LENGTH_SHORT).show();
//		Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//		startActivity(intent);
		// overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}

	private void openAddContact() {
		Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
		startActivity(intent);
		// overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}
	
	private void openAbout() {
		Intent intent = new Intent(MainActivity.this, AboutActivity.class);
		startActivity(intent);
	}
	
	private void openSearch() {
		Toast.makeText(this, getString(R.string.tips_underconstrutction), Toast.LENGTH_SHORT).show();
	}
	
	private String TAG = this.getClass().getName();
}
