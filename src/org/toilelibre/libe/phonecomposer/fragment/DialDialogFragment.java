package org.toilelibre.libe.phonecomposer.fragment;

import org.toilelibre.libe.phonecomposer.DialActivity;
import org.toilelibre.libe.phonecomposer.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DialDialogFragment extends DialogFragment {
	int mNum;

	/**
	 * Create a new instance of MyDialogFragment, providing "num" as an
	 * argument.
	 */
	public static DialDialogFragment newInstance(String phoneNumber, String contactName, String displayName) {
		DialDialogFragment f = new DialDialogFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putString("phoneNumber", phoneNumber);
		args.putString("contactName", contactName);
		args.putString("displayName", displayName);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dial, container, false);
		this.getDialog().setTitle("Dialing...");

		((TextView) v.findViewById(R.id.textView1)).setText("Dialing " +
		this.getArguments().getString("contactName"));
		((TextView) v.findViewById(R.id.textView2)).setText(
		this.getArguments().getString("displayName"));
		Button button = (Button) v.findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// When button is clicked, call up to owning activity.
				DialDialogFragment.this.dismiss();
				((DialActivity)(DialDialogFragment.this.getActivity())).close ();
			}
		});

		return v;
	}
}
