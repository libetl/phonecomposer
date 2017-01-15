package org.toilelibre.libe.phonecomposer;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.BaseTypes;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class ComposeNow extends Activity {
	private List<String> phoneTypes;
	private List<String> phoneNumbers;
	private List<String> listViewValues;

	private int selectedIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.phoneTypes = new LinkedList<>();
		this.phoneNumbers = new LinkedList<>();
		this.listViewValues = new LinkedList<>();
		this.setContentView(R.layout.activity_compose_now);
		Log.i(ComposeNow.class.getName(),
				"Landed in ComposeNow Activity - PhoneComposer");
		Log.i(ComposeNow.class.getName(),
				"ComposeNow Activity - Intent to String : "
						+ this.getIntent().toString());
		Log.i(ComposeNow.class.getName(),
				"ComposeNow Activity - Extras found : "
						+ this.getIntent().getExtras());
		String phoneNumber = this.getIntent().getData().toString()
				.substring("tel:".length()).replace("%2B", "+");
		Log.i(ComposeNow.class.getName(),
				"ComposeNow Activity - This is the phone number : "
						+ phoneNumber);

		Cursor phoneCursor = this.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				null,
				ContactsContract.CommonDataKinds.Phone.NUMBER + "='"
						+ phoneNumber + "'", null, null);
		if (phoneCursor.getCount() > 0) {
			phoneCursor.moveToFirst();
			Log.i(ComposeNow.class.getName(),
					"ComposeNow Activity - Found a contact : "
							+ phoneCursor.toString());
			String contactName = phoneCursor.getString(phoneCursor
					.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));

			String thumbnailUri = phoneCursor.getString(phoneCursor
					.getColumnIndex(ContactsContract.Data.PHOTO_THUMBNAIL_URI));

			ImageView thumbnailIv = (ImageView) this
					.findViewById(R.id.quickContactBadge1);
			TextView nameTv = (TextView) this.findViewById(R.id.textView1);

			long id = phoneCursor.getLong(phoneCursor
					.getColumnIndex(ContactsContract.Data.CONTACT_ID));
			if (Integer
					.parseInt(phoneCursor.getString(phoneCursor
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
				Cursor pCur = this.getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = ?", new String[] { "" + id }, null);
				int i = 0;
				while (pCur.moveToNext()) {
					String label = pCur
							.getString(phoneCursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
					String number = pCur
							.getString(phoneCursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					int type = pCur
							.getInt(phoneCursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
					label = this.getPhoneLabel(type, label);
					if (number.equals(phoneNumber)) {
						this.selectedIndex = i;
					}
					this.phoneNumbers.add(number);
					this.phoneTypes.add(label);
					this.listViewValues.add(label + " : " + number);
					Log.i(ComposeNow.class.getName(),
							"ComposeNow Activity - Contact " + id
									+ " has this number : " + number + "("
									+ label + ")");
					i++;
				}
				pCur.close();
			}

			if (thumbnailUri != null) {
				thumbnailIv.setImageURI(Uri.parse(thumbnailUri));
			} else {
				thumbnailIv.setImageResource(R.drawable.ic_launcher);
			}
			if (contactName != null) {
				nameTv.setText(contactName);
			} else {
				nameTv.setText("?");
			}

			ListView lv = (ListView) this.findViewById(R.id.ListView1);
			lv.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, this.listViewValues));

			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Log.i(ComposeNow.class.getName(),
							"ComposeNow Activity - Selected another index : "
									+ arg2);
					ComposeNow.this.selectedIndex = arg2;
					ComposeNow.this.updateNumber(ComposeNow.this.selectedIndex);

				}

			});

			this.updateNumber(this.selectedIndex);

			Button b = (Button) this.findViewById(R.id.button1);
			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (ComposeNow.this.selectedIndex >= 0) {
						String phoneNumber = ComposeNow.this.phoneNumbers
								.get(ComposeNow.this.selectedIndex);
						Log.i(ComposeNow.class.getName(),
								"ComposeNow Activity - Let's call : "
										+ phoneNumber);
					}

				}

			});

		} else {
			Log.i(ComposeNow.class.getName(),
					"ComposeNow Activity - No contact found.");

		}
	}

	private void updateNumber(int selectedIndex2) {
		TextView numberTv = (TextView) this.findViewById(R.id.textView3);
		if (selectedIndex2 >= 0) {
			numberTv.setText(this.listViewValues.get(selectedIndex2));
		} else {
			numberTv.setText("Problem");
		}

	}

	private String getPhoneLabel(int type, String label) {
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_now, menu);
		return true;
	}

}
