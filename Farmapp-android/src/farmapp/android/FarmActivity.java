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
import android.widget.ListView;
import android.widget.Toast;

public class FarmActivity extends Activity {

    private final static String TAG = "FarmActivity";
    private final static int ID_DIALOG_FETCHING = 0;

    private String[] values;
    private JSONArray array;

    private class FetchfarmsList extends AsyncTask<String, Void, JSONObject> {
      @Override
        protected void onPostExecute(JSONObject jsonobject) {
            Log.d(TAG, "Llego aqui");
            try {
            	String status = (String) jsonobject.get("status");
                Log.d(TAG, "status: " + status);
            
                
                if (status.equals("OK")) {
                    
                    array = jsonobject.getJSONArray("result");
                    String[] values = new String[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject farm = array.getJSONObject(i);
                        values[i] = "Farmacia: " + farm.getString("nombre") + 
                               "\nDireccion: " + farm.getString("direccion");
                                //farm.getString("horario") ;
                                
                        Log.d(TAG, farm.getString("nombre"));
                        Log.d(TAG, farm.getString("direccion"));
                        Log.d(TAG, farm.getString("horario"));
                    
                    setValues(values);
                    dismissDialog(ID_DIALOG_FETCHING);
                    showList();
                    }
                    
                    setValues(values);
                    dismissDialog(ID_DIALOG_FETCHING);
                    showList();
                    
               }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    
        private final static String TAG = "FetchfarmsList";

        FetchfarmsList() {
            super();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonobject = null;
            try {
                Log.d(TAG, "FetchfarmsList doInBackground, ciudad: "+params[0]);
                String content[] = farmappApi.getInstance(
                        getApplicationContext()).list(params[0]);
                for (int i = 0; i < content.length; i++)
                    Log.d(FetchfarmsList.TAG, content[i]);

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
        setContentView(R.layout.farmacias_layout);
        Bundle bundle = this.getIntent().getExtras();
        Log.d(TAG, bundle.get("ciudad").toString());

        showDialog(ID_DIALOG_FETCHING);
        (new FetchfarmsList()).execute(bundle.getString("ciudad"));

    }

    private void showList() {
        ListView listView = (ListView) findViewById(R.id.gameslist);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

       listView.setAdapter(adapter);
   	listView.setOnItemClickListener(new OnItemClickListener() {

		public void onItemClick(AdapterView<?> view, View parent,
				int position, long id) {
			try {
				Log.d(TAG, array.getJSONObject(position).toString());
				JSONObject farm = array.getJSONObject(position);
				
				Toast toast = Toast.makeText(getApplicationContext(),"horario: "+ farm.getString("horario"),Toast.LENGTH_LONG);
				
				
				
				toast.show();
			
				// intent.putExtra("post", game.getString("post"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//startActivity(intent);
		}
	});
    
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
            loadingDialog.setMessage("Fetching farms...");
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