package org.toilelibre.libe.phonecomposer.contacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.toilelibre.libe.phonecomposer.R;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;

/**
 * An activity representing a list of ContactList. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ContactDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ContactListFragment} and the item details (if present) is a
 * {@link ContactDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ContactListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class ContactListActivity extends FragmentActivity implements
		ContactListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	
	private Map<String, String> contactDN = new HashMap<String, String> ();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_contact_list);
		String[] projection = new String[] {
				ContactsContract.Data.CONTACT_ID,
				ContactsContract.Data.DISPLAY_NAME
		};
		Cursor phoneCursor = this.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				projection,null, null, null);
		phoneCursor.moveToFirst();
		while (phoneCursor.moveToNext()){
			this.contactDN.put("" + phoneCursor.getString(phoneCursor
					.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
					, phoneCursor.getString(phoneCursor
					.getColumnIndex(ContactsContract.Data.CONTACT_ID)));
		}
		phoneCursor.close();
			

		if (findViewById(R.id.contact_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ContactListFragment) this.getSupportFragmentManager()
					.findFragmentById(R.id.contact_list))
					.setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	    List<String> contacts = new ArrayList<String> ();
	    contacts.addAll(this.contactDN.keySet());
	    Collections.sort(contacts);

	    ((ContactListFragment)this.getSupportFragmentManager().findFragmentById(R.id.contact_list)).setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1,	contacts
				));
	    ((ContactListFragment)this.getSupportFragmentManager().findFragmentById(R.id.contact_list)).setContactList (this.contactDN);
	}

	/**
	 * Callback method from {@link ContactListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		long longId = 0;
		try {
			longId = Long.parseLong (id);
		}catch (NumberFormatException nfe){
			
		}
		if (this.mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putLong (ContactDetailFragment.ARG_ITEM_ID, longId);
			ContactDetailFragment fragment = new ContactDetailFragment();
			fragment.setArguments(arguments);
			this.getSupportFragmentManager().beginTransaction()
					.replace(R.id.contact_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ContactDetailActivity.class);
			detailIntent.putExtra(ContactDetailFragment.ARG_ITEM_ID, longId);
			this.startActivity(detailIntent);
		}
	}

	public Map<String, String> getContactDN() {
		return contactDN;
	}
	
}
