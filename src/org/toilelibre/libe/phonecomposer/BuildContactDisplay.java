package org.toilelibre.libe.phonecomposer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.BaseTypes;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BuildContactDisplay {

	public static ContactData build(final Activity a) {
		a.setContentView(R.layout.activity_compose_now);
		return BuildContactDisplay.build(a, a);
	}
	
	public static ContactData build(final Activity a, Object v) {
		Log.i(ComposeNowActivity.class.getName(),
				"Landed in ComposeNow Activity - PhoneComposer");
		Log.i(ComposeNowActivity.class.getName(),
				"ComposeNow Activity - Intent to String : "
						+ a.getIntent().toString());
		Log.i(ComposeNowActivity.class.getName(),
				"ComposeNow Activity - Extras found : "
						+ a.getIntent().getExtras());
		String phoneNumber = a.getIntent().getData().toString()
				.substring("tel:".length()).replaceAll("%2B", "+")
				.replaceAll("%20", " ");
		Log.i(ComposeNowActivity.class.getName(),
				"ComposeNow Activity - This is the phone number : "
						+ phoneNumber);

		Cursor phoneCursor = a.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				null,
				ContactsContract.CommonDataKinds.Phone.NUMBER + "='"
						+ phoneNumber + "'", null, null);

		if (phoneCursor.getCount() == 0) {

			phoneNumber = phoneNumber.replaceAll(" ", "");
			Log.i(ComposeNowActivity.class.getName(),
					"ComposeNow Activity - Didn't find. Let's attempt to remove the space chars : "
							+ phoneNumber);

			phoneCursor = a.getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Phone.NUMBER + "='"
							+ phoneNumber + "'", null, null);
		}

		if (phoneCursor.getCount() > 0) {
			phoneCursor.moveToFirst();
			Log.i(ComposeNowActivity.class.getName(),
					"ComposeNow Activity - Found a contact : "
							+ phoneCursor.toString());

			long id = phoneCursor.getLong(phoneCursor
					.getColumnIndex(ContactsContract.Data.CONTACT_ID));
			if (Integer
					.parseInt(phoneCursor.getString(phoneCursor
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
				return BuildContactDisplay.build(a, v, id, phoneNumber);

			}
		}
		ContactData cd = new ContactData();
		Log.i(ComposeNowActivity.class.getName(),
				"ComposeNow Activity - No contact found. Never mind !");

		cd.getPhoneNumbers().add(phoneNumber);
		cd.getPhoneTypes().add("no_type");
		cd.setContactName("Unknown");
		cd.getListViewValues().add("no_type : " + phoneNumber);
		TextView nameTv = (TextView) BuildContactDisplay.findViewById(v, R.id.textView1);
		nameTv.setText("Unknown");
		ImageView thumbnailIv = (ImageView) a
				.findViewById(R.id.quickContactBadge1);
		thumbnailIv.setImageResource(android.R.drawable.ic_menu_call);
		ListView lv = (ListView) BuildContactDisplay.findViewById(v, R.id.ListView1);
		lv.setAdapter(new ArrayAdapter<String>(a,
				android.R.layout.simple_list_item_1, cd.getListViewValues()));

		BuildContactDisplay.buildEvents (a, v, cd);
		return cd;

	}
	
	private static Object findViewById(Object v, int id) {
		if (v instanceof Activity){
			return ((Activity) v).findViewById(id);
		}else if (v instanceof View){
			return ((View) v).findViewById(id);			
		}
		return null;
	}

	public static ContactData build(final Activity a, long id) {
		return BuildContactDisplay.build(a, a, id);
	}

	public static ContactData build(final Activity a, Object v, long id) {
		return BuildContactDisplay.build(a, v, id, "");
	}

	public static ContactData build(final Activity a, long id,
			String phoneNumber) {
		return BuildContactDisplay.build(a, a, id, phoneNumber);
	}
	
	public static ContactData build(final Activity a, Object v, long id,
			String phoneNumber) {
		final ContactData cd = new ContactData();
		Cursor pCur = a.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
				new String[] { "" + id }, null);

		int i = 0;
		while (pCur.moveToNext()) {
			String label = pCur
					.getString(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
			String number = pCur
					.getString(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			int type = pCur
					.getInt(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
			label = BuildContactDisplay.getPhoneLabel(type, label);
			if (number.equals(phoneNumber)) {
				cd.setSelectedIndex(i);
			}
			cd.setContactName(pCur.getString(pCur
					.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)));

			cd.getPhoneNumbers().add(number);
			cd.getPhoneTypes().add(label);
			cd.getListViewValues().add(label + " : " + number);
			Log.i(ComposeNowActivity.class.getName(),
					"ComposeNow Activity - Contact " + id
							+ " has this number : " + number + "(" + label
							+ ")");
			i++;
		}
		pCur.moveToFirst();
		String thumbnailUri = pCur.getString(pCur
				.getColumnIndex(ContactsContract.Data.PHOTO_THUMBNAIL_URI));

		pCur.close();
		ImageView thumbnailIv = (ImageView) BuildContactDisplay.
				findViewById(v, R.id.quickContactBadge1);
		TextView nameTv = (TextView) BuildContactDisplay.findViewById(v, R.id.textView1);

		if (thumbnailUri != null) {
			thumbnailIv.setImageURI(Uri.parse(thumbnailUri));
		} else {
			thumbnailIv.setImageResource(android.R.drawable.ic_menu_call);
		}
		if (cd.getContactName() != null) {
			nameTv.setText(cd.getContactName());
		} else {
			nameTv.setText("?");
		}

		ListView lv = (ListView) BuildContactDisplay.findViewById(v, R.id.ListView1);
		lv.setAdapter(new ArrayAdapter<String>(a,
				android.R.layout.simple_list_item_1, cd.getListViewValues()));

		BuildContactDisplay.buildEvents (a, v, cd);
		
		return cd;
	}

	private static void buildEvents(final Activity a, final Object v, final ContactData cd) {
		ListView lv = (ListView) BuildContactDisplay.findViewById(v, R.id.ListView1);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.i(ComposeNowActivity.class.getName(),
						"ComposeNow Activity - Selected another index : "
								+ arg2);
				cd.setSelectedIndex(arg2);
				BuildContactDisplay.updateNumber(v, cd, cd.getSelectedIndex());

			}

		});

		BuildContactDisplay.updateNumber(v, cd, cd.getSelectedIndex());

		Button b = (Button) BuildContactDisplay.findViewById(v, R.id.button1);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cd.getSelectedIndex() >= 0) {
					String phoneNumber = cd.getPhoneNumbers().get(
							cd.getSelectedIndex());
					String displayName = cd.getListViewValues().get(
							cd.getSelectedIndex());

					Log.i(ComposeNowActivity.class.getName(),
							"ComposeNow Activity - Let's dial : " + phoneNumber);
					Intent intentDial = new Intent(a, DialActivity.class);
					intentDial.putExtra("phoneNumber", phoneNumber);
					intentDial.putExtra("contactName", cd.getContactName());
					intentDial.putExtra("displayName", displayName);
					a.startActivity(intentDial);
				}

			}

		});
		
	}

	private static void updateNumber(Object v, ContactData cd,
			int selectedIndex2) {
		TextView numberTv = (TextView) BuildContactDisplay.findViewById(v, R.id.textView3);
		if (selectedIndex2 >= 0) {
			numberTv.setText(cd.getListViewValues().get(selectedIndex2));
		} else {
			numberTv.setText("Problem");
		}

	}

	private static String getPhoneLabel(int type, String label) {
		String s;
		switch (type) {
		case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
			s = "home_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
			s = "mobile_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
			s = "work_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
			s = "fax_work_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
			s = "fax_home_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
			s = "pager_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
			s = "other_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK:
			s = "callback_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_CAR:
			s = "car_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN:
			s = "company_main_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_ISDN:
			s = "isdn_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
			s = "main_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX:
			s = "other_fax_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_RADIO:
			s = "radio_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_TELEX:
			s = "telex_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD:
			s = "tty_tdd_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
			s = "work_mobile_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER:
			s = "work_pager_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT:
			s = "assistant_phone";
			break;
		case ContactsContract.CommonDataKinds.Phone.TYPE_MMS:
			s = "mms_phone";
			break;
		case BaseTypes.TYPE_CUSTOM:
			if (label == null)
				s = "phone";
			else
				s = label;
			break;
		default:
			s = "phone";
		}
		return s;
	}
}
