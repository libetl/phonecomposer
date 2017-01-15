package org.toilelibre.libe.phonecomposer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.toilelibre.libe.phonecomposer.code.ExitCode;
import org.toilelibre.libe.phonecomposer.fragment.DialDialogFragment;
import org.toilelibre.libe.phonecomposer.thread.DialThread;
import org.toilelibre.libe.phonecomposer.thread.OnThreadCompleteListener;

import java.util.Locale;

public class DialActivity extends Activity {

	@SuppressLint("DefaultLocale")
	private String treatPhoneNumberPlus (String phoneNumber){
	if (phoneNumber.indexOf('+') == -1){
		  return phoneNumber;
		}
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String isoCode = tm.getNetworkCountryIso().toUpperCase(Locale.getDefault());
		String operatorName = tm.getNetworkOperatorName().replaceAll("[^a-zA-Z]|\\.", "").toLowerCase(
				Locale.getDefault());
		ExitCode ec = null;
		try {
		ec = ExitCode.valueOf(isoCode);
		}catch (IllegalArgumentException iae){
		}
		if (ec == null){
			try {
			ec = ExitCode.valueOf(isoCode + operatorName);
			}catch (IllegalArgumentException iae){
			}
			
		}
		if (ec == null){
		  return phoneNumber;
		}
		String phoneNumber1 = phoneNumber.replace("+", ec.getExitcode());
		String phoneNumber2 = phoneNumber1;
		
		if (phoneNumber.substring(1).startsWith(ec.getIntcode())){
			phoneNumber2 = phoneNumber.replace("+" + ec.getIntcode(), ec.getTrunk().replace("-", ""));
		}
		return phoneNumber2;
	}
	
	private String treatPhoneNumberSpecialChars(String phoneNumber) {
		return phoneNumber.replaceAll("[^0-9]", "");
	}	

	
	private String treatPhoneNumber (String phoneNumber){
		String res = this.treatPhoneNumberPlus(phoneNumber);
		res = this.treatPhoneNumberSpecialChars(res);
		return res;
	}
	
	
	  @SuppressLint("CommitTransaction")
	@Override
	/** Called when the activity is first created. */
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    // DialogFragment.show() will take care of adding the fragment
	    // in a transaction.  We also want to remove any currently showing
	    // dialog, so make our own transaction and take care of that here.
	    FragmentTransaction ft = this.getFragmentManager().beginTransaction();
	    Fragment prev = this.getFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);

		String phoneNumber = this.getIntent().getStringExtra("phoneNumber");
		String contactName = this.getIntent().getStringExtra("contactName");
		String displayName = this.getIntent().getStringExtra("displayName");
	    // Create and show the dialog.
	    DialogFragment newFragment = DialDialogFragment.newInstance(phoneNumber, contactName, displayName);
	    newFragment.show(ft, "dialog");
	    
	    String phoneNumber1 = this.treatPhoneNumber(phoneNumber);
	    
		Log.i(ComposeNowActivity.class.getName(),
				"DialActivity Activity - Let's dial : "
						+ phoneNumber1);

	    
	    DialThread dt = new DialThread (phoneNumber1, new OnThreadCompleteListener (){
			@Override
			public void onThreadComplete() {
				DialActivity.this.close();				
			}});
	    dt.start();
	  }

	public void close() {
		this.finish();
		
	}
}
