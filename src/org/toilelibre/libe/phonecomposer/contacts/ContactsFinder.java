package org.toilelibre.libe.phonecomposer.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsFinder {

    public static ContactData build(ContentResolver contentResolver, long id) {
        Cursor pCur = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[] { "" + id }, null);
        List<String> phoneNumbers = new ArrayList<>();
        List<String> phoneTypes = new ArrayList<>();
        String contactName = "";
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

            label = getPhoneLabel(type, label);
            contactName = pCur.getString(pCur
                    .getColumnIndex(ContactsContract.Data.DISPLAY_NAME));

            phoneNumbers.add(number);
            phoneTypes.add(label);
        }
        pCur.moveToFirst();
        String photo = pCur.getString(pCur
                .getColumnIndex(ContactsContract.Data.PHOTO_THUMBNAIL_URI));

        pCur.close();

        return new ContactData(phoneTypes, phoneNumbers, contactName, photo);
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
            case ContactsContract.CommonDataKinds.BaseTypes.TYPE_CUSTOM:
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

	public static class ContactData {

		private final List<String> phoneTypes;
		private final List<String> phoneNumbers;
		private final String contactName;
        private final String photo;

        public ContactData(List<String> phoneTypes, List<String> phoneNumbers, String contactName, String photo) {
            this.phoneTypes = phoneTypes;
            this.phoneNumbers = phoneNumbers;
            this.contactName = contactName;
            this.photo = photo;
        }


        public List<String> getPhoneTypes() {
            return phoneTypes;
        }

        public List<String> getPhoneNumbers() {
            return phoneNumbers;
        }

        public String getContactName() {
            return contactName;
        }

        public String getPhoto() {
            return photo;
        }
    }


	public static Map<Long, ContactData> find(ContentResolver contentResolver) {

		HashMap<Long, ContactData> contacts = new HashMap<>();
		String[] projection = new String[]{
				ContactsContract.Data.CONTACT_ID,
				ContactsContract.Data.DISPLAY_NAME,
		};
		Cursor phoneCursor = contentResolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				projection, null, null, null);
		phoneCursor.moveToFirst();
		while (phoneCursor.moveToNext()) {
            Long id = phoneCursor.getLong(phoneCursor
                            .getColumnIndex(ContactsContract.Data.CONTACT_ID));
			contacts.put(id, build(contentResolver, id));
		}
		phoneCursor.close();
        return contacts;
	}

}
