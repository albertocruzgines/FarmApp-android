package farmapp.android;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import farmapp.android.api.farmappApi;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class UserActivity extends Activity {
	private final static String TAG = "UserActivity";
	private final static int ID_DIALOG_FETCHING = 0;

	private String[] values;
	private JSONArray array;

	private class FetchGamesList extends AsyncTask<String, Void, JSONObject> {
		@Override
		protected void onPostExecute(JSONObject jsonobject) {
			Log.d(TAG, "Llego aqui");
			try {
				String status = (String) jsonobject.get("status");
				Log.d(TAG, "status: " + status);
				
				/*String result = (String) jsonobject.get("result");
				Log.d(TAG, "result: " + result);*/
				
				if (status.equals("OK")) {
					/*JSONObject pilot = json.getJSONObject("pilot");
			        String firstName = pilot.getString("firstName");
			        String lastName = pilot.getString("lastName");*/
			        
					JSONObject result = jsonobject.getJSONObject("result");
			        String direccion = result.getString("direccion");
			        String name = result.getString("name");
			        String email = result.getString("email");
			       
			        Log.d(TAG, "direccion"+direccion);
			        Log.d(TAG, "name: "+name);
			        Log.d(TAG, "email: "+email);
			       
			        
			        String[] values = new String[4];
			        values[0] = "Datos del usuario";
			        values[1] = email;
			        values[2] = name;
			        values[3] = direccion;
			        
			        
					/*array = jsonobject.getJSONArray("result");
					String[] values = new String[array.length()];
					for (int i = 0; i < array.length(); i++) {
						JSONObject game = array.getJSONObject(i);
						values[i] = game.getString("username") + " "
								+ game.getString("name") + " "
								+ game.getString("email");
						
						Log.d(TAG, game.getString("name"));
						Log.d(TAG, game.getString("email"));
					
					}*/
					setValues(values);
					dismissDialog(ID_DIALOG_FETCHING);
					showList();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private final static String TAG = "FetchGamesList";

		FetchGamesList() {
			super();

		}

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject jsonobject = null;
			try {
				Log.d(TAG, "FetchGamesList doInBackground, email: "+params[0]);
				String content[] = farmappApi.getInstance(
						getApplicationContext()).info(params[0]);
				for (int i = 0; i < content.length; i++)
					Log.d(FetchGamesList.TAG, content[i]);

				String json = content[content.length - 1];
				jsonobject = new JSONObject(json);
				Log.d(TAG, "doInBackground json: "+ jsonobject);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonobject;
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_layout);
		Bundle bundle = this.getIntent().getExtras();
		Log.d(TAG, bundle.get("id").toString());

		showDialog(ID_DIALOG_FETCHING);
		(new FetchGamesList()).execute(bundle.getString("email"));

	}
	
	public void Vpedidos(View v) {
		Bundle bundle=this.getIntent().getExtras();
		
        String iduser=bundle.get("id").toString();
        System.out.println("El id que me pasa es"+iduser);
        Intent i = new Intent(this, PedidosActivity.class );
        i.putExtra("idusuario",iduser );
        
        startActivity(i);
		
}

	private void showList() {
		//ListView listView = (ListView) findViewById(R.id.gameslist);

		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				//android.R.layout.simple_list_item_1, android.R.id.text1, values);

		// Assign adapter to ListView
		//listView.setAdapter(adapter);
		
		setContentView(R.layout.user_layout);
		((TextView) findViewById(R.id.tv4)).setText((values[1]));
		((TextView) findViewById(R.id.tv5)).setText((values[2]));
		((TextView) findViewById(R.id.tv6)).setText((values[3]));
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case ID_DIALOG_FETCHING:
			ProgressDialog loadingDialog = new ProgressDialog(this);
			loadingDialog.setMessage("Fetching games...");
			loadingDialog.setIndeterminate(true);
			loadingDialog.setCancelable(false);
			return loadingDialog;

		}
		return super.onCreateDialog(id);
	}

	private void setValues(String[] values) {
		this.values = values;
	}
}
