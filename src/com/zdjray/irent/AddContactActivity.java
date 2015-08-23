package com.zdjray.irent;

import java.util.Calendar;
import java.util.Set;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.zdjray.irent.biz.CacheManager;
import com.zdjray.irent.biz.beans.Contact;
import com.zdjray.irent.biz.beans.RentInfo;
import com.zdjray.irent.biz.beans.RoomInfo;
import com.zdjray.irent.util.RentUtil;

public class AddContactActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_contact, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_add_contact, container, false);
			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			
			String roomNo = getActivity().getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);
			if (roomNo != null && !roomNo.isEmpty()) {
				getActivity().setTitle(R.string.title_activity_edit_contact);
				((AddContactActivity)getActivity()).isEdit = true;
				
				// uneditable
				((EditText)getActivity().findViewById(R.id.addcontact_roomno)).setCursorVisible(false);
				((EditText)getActivity().findViewById(R.id.addcontact_roomno)).setFocusable(false);
				((EditText)getActivity().findViewById(R.id.addcontact_roomno)).setFocusableInTouchMode(false);
				
				
				RoomInfo roomInfo = CacheManager.getInstance(getActivity()).getRoomInfo(roomNo);
				
				// contact info
				((EditText)getActivity().findViewById(R.id.addcontact_roomno)).setText(roomInfo.getContact().getRoomNo() + "");
				((EditText)getActivity().findViewById(R.id.addcontact_renter_name)).setText(roomInfo.getContact().getName());
				((EditText)getActivity().findViewById(R.id.addcontact_renter_phoneno)).setText(roomInfo.getContact().getPhoneNo());
				((EditText)getActivity().findViewById(R.id.addcontact_renter_remark)).setText(roomInfo.getContact().getRemark());
				// rent info
				((EditText)getActivity().findViewById(R.id.addcontact_rent_start_date)).setText(RentUtil.formatDate(roomInfo.getRentInfo().getRentStartDate()));
				((EditText)getActivity().findViewById(R.id.addcontact_rent_end_date)).setText(RentUtil.formatDate(roomInfo.getRentInfo().getRentEndDate()));
				((EditText)getActivity().findViewById(R.id.addcontact_rent_price)).setText(roomInfo.getRentInfo().getRentPrice() + "");
				((EditText)getActivity().findViewById(R.id.addcontact_rent_deposit)).setText(roomInfo.getRentInfo().getRentDeposit() + "");
				
			} else {
				((EditText)getActivity().findViewById(R.id.addcontact_rent_start_date)).setText(RentUtil.formatDate(Calendar.getInstance()));
				((EditText)getActivity().findViewById(R.id.addcontact_rent_end_date)).setText(RentUtil.formatDate(Calendar.getInstance()));
				((EditText)getActivity().findViewById(R.id.addcontact_rent_price)).setText("6000");
				((EditText)getActivity().findViewById(R.id.addcontact_rent_deposit)).setText("0");
			}
			
			((EditText)getActivity().findViewById(R.id.addcontact_rent_start_date)).setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						Calendar calendar = RentUtil.parseDate(((EditText)getActivity().findViewById(R.id.addcontact_rent_start_date)).getText().toString());
						
						new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
							
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								// 获取一个日历对象，并初始化为当前选中的时间
								Calendar calendar = Calendar.getInstance();
								calendar.set(year, monthOfYear, dayOfMonth);
								((EditText)getActivity().findViewById(R.id.addcontact_rent_start_date)).setText(RentUtil.formatDate(calendar));
							}
						}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
						return true;
					}
					return false;
				}
			});
			
			((EditText)getActivity().findViewById(R.id.addcontact_rent_end_date)).setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						Calendar calendar = RentUtil.parseDate(((EditText)getActivity().findViewById(R.id.addcontact_rent_end_date)).getText().toString());
						
						new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
							
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								// 获取一个日历对象，并初始化为当前选中的时间
								Calendar calendar = Calendar.getInstance();
								calendar.set(year, monthOfYear, dayOfMonth);
								((EditText)getActivity().findViewById(R.id.addcontact_rent_end_date)).setText(RentUtil.formatDate(calendar));
							}
						}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
						return true;
					}
					return false;
				}
			});
		}
	}
	
	// for the call button
	public void callPhone(View view) {
		String num = ((EditText) findViewById(R.id.addcontact_renter_phoneno)).getText().toString().trim();
		// 正则验证输入的是否正确
		if (RentUtil.isValidPhoneNo(num)) {
			callConfirm(num);
		} else {
			Toast.makeText(this, getString(R.string.tips_renterphoneno_invalid), Toast.LENGTH_SHORT).show();
		}
	}

	private void callConfirm(String num) {
		final String phoneNo = num;

		Dialog alertDialog = new AlertDialog.Builder(this).setTitle(getString(R.string.title_addcontact_call)).setMessage(phoneNo)
				.setIcon(R.drawable.ic_action_call).setPositiveButton(getString(R.string.confirm_addcontact_calldialog_positive), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNo));
						startActivity(intent);
					}
				}).setNegativeButton(getString(R.string.confirm_addcontact_calldialog_negtive), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Do nothing
					}
				}).create();
		alertDialog.show();
	}

	// for the save button
	public void saveContact(View view) {
		Log.d(TAG, "save contact");

		String roomNoStr = ((EditText) findViewById(R.id.addcontact_roomno)).getText().toString().trim();
		String renterName = ((EditText) findViewById(R.id.addcontact_renter_name)).getText().toString().trim();
		String renterPhoneNo = ((EditText) findViewById(R.id.addcontact_renter_phoneno)).getText().toString().trim();
		String renterRemark = ((EditText) findViewById(R.id.addcontact_renter_remark)).getText().toString().trim();
		String rentStartDate = ((EditText) findViewById(R.id.addcontact_rent_start_date)).getText().toString().trim();
		String rentEndDate = ((EditText) findViewById(R.id.addcontact_rent_end_date)).getText().toString().trim();
		String rentPriceStr = ((EditText) findViewById(R.id.addcontact_rent_price)).getText().toString().trim();
		String rentDepositStr = ((EditText) findViewById(R.id.addcontact_rent_deposit)).getText().toString().trim();
		
		int roomNo = RentUtil.tryParseInt(roomNoStr, 0);
		int rentPrice = RentUtil.tryParseInt(rentPriceStr, -1);
		int rentDeposit = RentUtil.tryParseInt(rentDepositStr, -1);

		// implies not zero
		if (roomNoStr.isEmpty()) {
			Toast.makeText(this, getString(R.string.tips_roomno_empty), Toast.LENGTH_SHORT).show();
			return;
		}
		if (roomNo < 100 || roomNo > 1100) {
			Log.d(TAG, "invalid roomno: " + roomNo);
			Toast.makeText(this, getString(R.string.tips_roomno_invalid), Toast.LENGTH_SHORT).show();
			return;
		}

		if (renterName.isEmpty()) {
			Toast.makeText(this, getString(R.string.tips_rentername_empty), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (renterPhoneNo.isEmpty()) {
			Toast.makeText(this, getString(R.string.tips_renterphoneno_empty), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (!RentUtil.isValidPhoneNo(renterPhoneNo.trim())) {
			Toast.makeText(this, getString(R.string.tips_renterphoneno_invalid), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (rentStartDate.isEmpty()) {
			Toast.makeText(this, getString(R.string.tips_rentstartdate_empty), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (rentEndDate.isEmpty()) {
			Toast.makeText(this, getString(R.string.tips_rentenddate_empty), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (rentPriceStr.isEmpty()) {
			Toast.makeText(this, getString(R.string.tips_rentprice_empty), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (rentPrice < 0) {
			Toast.makeText(this, getString(R.string.tips_rentprice_invalid), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (rentDepositStr.isEmpty()) {
			Toast.makeText(this, getString(R.string.tips_rentdeposit_empty), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (rentDeposit < 0) {
			Toast.makeText(this, getString(R.string.tips_rentdeposit_invalid), Toast.LENGTH_SHORT).show();
			return;
		}
		
		try {
			roomInfoKey = roomNoStr;

			// TODO 容错，一定不能把数据丢了
			Set<String> roomInfoKeyList = CacheManager.getInstance(this).getRoomInfoKeyList();
			// duplicate room
			if (!isEdit && roomInfoKeyList.contains(roomInfoKey)) {
				Toast.makeText(this, getString(R.string.tips_room_rent), Toast.LENGTH_SHORT).show();
				return;
			}
			
			roomInfoKeyList.add(roomInfoKey);
			CacheManager.getInstance(this).setRoomInfoKeyList(roomInfoKeyList);

			RoomInfo roomInfo = new RoomInfo();

			Contact contact = new Contact();
			contact.setRoomNo(roomNo);
			contact.setName(renterName);
			contact.setPhoneNo(renterPhoneNo);
			contact.setRemark(renterRemark);
			roomInfo.setContact(contact);

			RentInfo rentInfo = new RentInfo();
			rentInfo.setRentStartDate(RentUtil.parseDate(rentStartDate));
			rentInfo.setRentEndDate(RentUtil.parseDate(rentEndDate));
			rentInfo.setRentPrice(rentPrice);
			rentInfo.setRentDeposit(rentDeposit);
			roomInfo.setRentInfo(rentInfo);

			CacheManager.getInstance(this).setRoomInfo(roomInfoKey, roomInfo);

		} catch (Exception e) {
			Toast.makeText(this, getString(R.string.tips_store_exception), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			// return and tip user to commit again
			return;
		}

		sendBroadcast2Refresh();
		Toast.makeText(this, getString(R.string.tips_saved), Toast.LENGTH_LONG).show();
		// back to parent activity
		finish();
	}

	private void sendBroadcast2Refresh() {
		// 广播通知
		Intent intent = new Intent();
		intent.setAction(MainActivity.ACTION_REFRESH_LIST);
		sendBroadcast(intent);
		Log.d(TAG, "send message to refresh list");
	}

	private String roomInfoKey;
	private boolean isEdit = false;
	private String TAG = this.getClass().getName();
}
