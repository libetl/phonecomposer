package org.toilelibre.libe.phonecomposer;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ComposeNowActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setContentView(R.layout.activity_compose_now);
		super.onCreate(savedInstanceState);

		BuildContactDisplay.build (this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.compose_now, menu);
		return true;
	}

}
