package com.example.shoppinglist;

import java.io.IOException;

import java.util.Random;

import com.example.shoppinglist.deviceinfoendpoint.Deviceinfoendpoint;
import com.example.shoppinglist.deviceinfoendpoint.model.DeviceInfo;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.provider.Settings.Secure;
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    Button button = (Button) findViewById(R.id.button1);
	    
	    button.setOnClickListener(new OnClickListener(){
			
		private String android_id = Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID); 
		
		@Override
		public void onClick(View v) {
			BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
		    String mydeviceaddress="";
		    if (bluetooth.isEnabled()) {
			    mydeviceaddress = bluetooth.getAddress();
			}
			else
			{
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, 1);
				mydeviceaddress = bluetooth.getAddress();
			}
				String Device_ID=mydeviceaddress;  //replace with Integer.toString(randomNum);
				String product=null;
		        final EditText products = (EditText) findViewById(R.id.editText1);
		        Bundle bundle = new Bundle();
		        product= products.getText().toString();
		        bundle.putString("products", products.getText().toString());
		        new CreateDeviceInfo().execute(Device_ID,product);
		        new UpdateDeviceInfo().execute(Device_ID,product);
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
	private class UpdateDeviceInfo extends AsyncTask<String, Void, Void> {
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
        	  endpoint.updateDeviceInfo(deviceinfo).execute();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          return null;
        }
			
    }
}
