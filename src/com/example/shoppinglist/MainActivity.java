package com.example.shoppinglist;

import java.io.IOException;
import java.util.Random;

import com.example.shoppinglist.deviceinfoendpoint.Deviceinfoendpoint;
import com.example.shoppinglist.deviceinfoendpoint.model.DeviceInfo;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener(){
		@Override
		public void onClick(View v) {
		    // TODO Auto-generated method stub
			Random rand = new Random();

		    // nextInt is normally exclusive of the top value,
		    // so add 1 to make it inclusive
		    int randomNum = rand.nextInt(1000);
				String Device_ID=Integer.toString(randomNum);
				String product=null;
		        final EditText products = (EditText) findViewById(R.id.editText1);
		        Bundle bundle = new Bundle();
		        product= products.getText().toString();
		        bundle.putString("products", products.getText().toString());
		        new CreateDeviceInfo().execute(Device_ID,product);
				Context context=getApplicationContext();
				Toast.makeText(context, "Shopping History Updated", Toast.LENGTH_SHORT).show();
			}
		});
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private class CreateDeviceInfo extends AsyncTask<String, Void, Void> {

        /**
         * Calls appropriate CloudEndpoint to indicate that user checked into a place.
         *
         * @param params the place where the user is checking in.
         */
		@Override
        protected Void doInBackground(String... params) {
          DeviceInfo deviceinfo = new com.example.shoppinglist.deviceinfoendpoint.model.DeviceInfo();

          // Set the ID of the store where the user is.
          deviceinfo.setDeviceRegistrationID(params[0]);
          deviceinfo.setDeviceInformation(params[1]);
          Deviceinfoendpoint.Builder builder = new Deviceinfoendpoint.Builder(
              AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

          builder = CloudEndpointUtils.updateBuilder(builder);

          Deviceinfoendpoint endpoint = builder.build();


          try {
            endpoint.insertDeviceInfo(deviceinfo).execute();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          return null;
        }
    }
}
