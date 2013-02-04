package farmapp.android.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.util.Log;

public class farmappApi {
	private final static String TAG = "farmappApi";
	private HttpClient httpclient = null;
	private String uri = null;
	private static farmappApi instance = null;

	private farmappApi(Context ctx) throws IOException {
		super();

		httpclient = new DefaultHttpClient();
		InputStream is = ctx.getResources().getAssets().open("api.properties");
		Properties p = new Properties();
		p.load(is);
		uri = p.getProperty("uri");
	}

	public static farmappApi getInstance(Context ctx) throws IOException {
		if (instance == null)
			instance = new farmappApi(ctx);
		return instance;
	}

	public String[] login(String username, String password)
			throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet();
		try {
			URI reqURI = new URI(uri + "action=login&email=" + username
					+ "&password=" + password);
			request.setURI(reqURI);
			System.out.println("URI :"+ reqURI);
			//System.out.println("usuario y password :"+ username + password);
			HttpResponse response = httpclient.execute(request);
			/*Log.d(TAG, response.getStatusLine().getReasonPhrase() + " - "
					+ response.getStatusLine().getStatusCode());*/

			InputStream is = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			Vector<String> lines = new Vector<String>();
			String line = null;
			while ((line = reader.readLine()) != null)
				lines.add(line);
			return lines.toArray(new String[lines.size()]);
		} catch (URISyntaxException e) {
			Log.e(TAG, "Please verify uri value in assets/api.properties");
			e.printStackTrace();
		}
		return null;
	}
	
	public String[] info(String username) throws ClientProtocolException, IOException{
		Log.d(TAG, "info");
		HttpGet request = new HttpGet();
		try {
			URI reqURI = new URI(uri + "action=info&email=" + username);
			request.setURI(reqURI);
			Log.d(TAG, "uri: "+reqURI);
			HttpResponse response = httpclient.execute(request);
			Log.d(TAG, response.getStatusLine().getReasonPhrase() + " - "
					+ response.getStatusLine().getStatusCode());

			InputStream is = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			Vector<String> lines = new Vector<String>();
			String line = null;
		
			while ((line = reader.readLine()) != null)
				lines.add(line);
			return lines.toArray(new String[lines.size()]);
		} catch (URISyntaxException e) {
			Log.e(TAG, "Please verify uri value in assets/api.properties");
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String[] list(String ciudad) throws ClientProtocolException, IOException{
		Log.d(TAG, "List");
		HttpGet request = new HttpGet();
		try {
			URI reqURI = new URI(uri + "action=listfarm&ciudad=" + ciudad);
			request.setURI(reqURI);
			Log.d(TAG, "uri: "+reqURI);
			HttpResponse response = httpclient.execute(request);
			Log.d(TAG, response.getStatusLine().getReasonPhrase() + " - "
					+ response.getStatusLine().getStatusCode());

			InputStream is = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			Vector<String> lines = new Vector<String>();
			String line = null;
		
			while ((line = reader.readLine()) != null)
				lines.add(line);
			return lines.toArray(new String[lines.size()]);
		} catch (URISyntaxException e) {
			Log.e(TAG, "Please verify uri value in assets/api.properties");
			e.printStackTrace();
		}
		return null;
	}
	
	public String[] listpedidos(String idusuario) throws ClientProtocolException, IOException{
		Log.d(TAG, "List");
		HttpGet request = new HttpGet();
		try {
			URI reqURI = new URI(uri + "action=listpedidosuser&idusuario=" + idusuario);
			request.setURI(reqURI);
			Log.d(TAG, "uri: "+reqURI);
			HttpResponse response = httpclient.execute(request);
			Log.d(TAG, response.getStatusLine().getReasonPhrase() + " - "
					+ response.getStatusLine().getStatusCode());

			InputStream is = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			Vector<String> lines = new Vector<String>();
			String line = null;
		
			while ((line = reader.readLine()) != null)
				lines.add(line);
			return lines.toArray(new String[lines.size()]);
		} catch (URISyntaxException e) {
			Log.e(TAG, "Please verify uri value in assets/api.properties");
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}