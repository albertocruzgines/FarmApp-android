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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ListfarmActivity extends Activity  {

    private final static String TAG = "ListActivity";
    private final static int ID_DIALOG_FETCHING = 0;
    private final static int ID_DIALOG_EXCEPTION = 1;

    private class ListTask extends AsyncTask<String, Void, JSONObject> {
        private final static String TAG = "ListTask";
        private Activity activity = null;
        private JSONArray array;

        ListTask(Activity activity) {
            super();
            this.activity = activity;
            Log.d(TAG, "ListTask Constructor");
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonobject = null;
            try {
                Log.d(TAG, "ListTask doInBackground");
                
                String content[] = farmappApi.getInstance(
                        getApplicationContext()).list(params[0]);
                        
                for (int i = 0; i < content.length; i++)
                    Log.d(ListTask.TAG, content[i]);

                String json = content[content.length - 1];
                jsonobject = new JSONObject(json);

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

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(JSONObject jsonobject) {
            Log.d(TAG, "entramos en post executeeeeeeeeeeeeeeeeeeeeeeeeee");
            dismissDialog(ID_DIALOG_FETCHING);
            try {
                String status = (String) jsonobject.get("status");
                
                
               // if (status.equals("OK")) {
                array = jsonobject.getJSONArray("result");
                String[] values = new String[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject farm = array.getJSONObject(i);
                        values[i] = farm.getString("ciudad");
                        Log.d(TAG, farm.getString("ciudad"));
                        //System.out.println("La ciudad es"+farm.getString("ciudad") );
                        
                    }
                    
                    
                    
                    
                    String prueba = ((EditText) findViewById(R.id.et1)).getText()
                            .toString();
                    System.out.println("La ciudad es"+prueba );
                    Intent intent = new Intent(activity,FarmActivity.class);
                    intent.putExtra("ciudad",((EditText) findViewById(R.id.et1)).getText()
                            .toString() );
                    
                    System.out.println("START ACTIVITY");
                    
                    startActivity(intent);
                

              /*  }else{
                	Log.d(TAG, (String) jsonobject.get("result"));
					AlertDialog.Builder builder = new AlertDialog.Builder(
							activity);
					builder.setMessage((String) jsonobject.get("result"))
							.setTitle("Error");
					builder.setPositiveButton("Aceptar",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();

								}
							});
					builder.create().show();
                }*/

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listfarm_layout);
    }

/*    public void showRegisterActivity(View v) {
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
    }*/

    public void Listar(View v) {
        String ciudad = ((EditText) findViewById(R.id.et1)).getText()
                .toString();
        //String password = ((EditText) findViewById(R.id.etPassword)).getText()
            //    .toString();
        Log.d(TAG, ciudad );
        showDialog(ID_DIALOG_FETCHING);
        (new ListTask(this)).execute(ciudad);

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
            loadingDialog.setMessage("Trying to list...");
            loadingDialog.setIndeterminate(true);
            loadingDialog.setCancelable(false);
            return loadingDialog;

        case ID_DIALOG_EXCEPTION:

            break;
        }
        return super.onCreateDialog(id);
    }


}