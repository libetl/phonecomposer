package org.toilelibre.libe.phonecomposer;

import java.util.LinkedList;
import java.util.List;

public class ContactData {

	private List<String> phoneTypes;
	private List<String> phoneNumbers;
	private List<String> listViewValues;

	private String contactName;

	private int selectedIndex;
	
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	public ContactData() {
		super();
		this.phoneTypes = new LinkedList<String> ();
		this.phoneNumbers = new LinkedList<String> ();
		this.listViewValues = new LinkedList<String> ();
		this.selectedIndex = 0;
		this.contactName = "Unknown";
	}
	
	public List<String> getPhoneTypes() {
		return phoneTypes;
	}
	public void setPhoneTypes(List<String> phoneTypes) {
		this.phoneTypes = phoneTypes;
	}
	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
	public List<String> getListViewValues() {
		return listViewValues;
	}
	public void setListViewValues(List<String> listViewValues) {
		this.listViewValues = listViewValues;
	}
}
