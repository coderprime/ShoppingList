package ray.cyberpup.com.shoppinglistchallenge;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created on 5/8/15
 *
 * @author Raymond Tong
 */
public class ShopListSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String LOG_TAG = ShopListSyncAdapter.class.getSimpleName();

    private final String SERVER_URL = "http://czshopper.herokuapp.com/items.json";
    private final String AUTH_TOKEN = "5Zq5ccxFreiWsENGyRUA";


    private Context mContext; // used for toast
    private ContentResolver mContentResolver;

    public ShopListSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    // Maintain Android 3.0 compatibility
    @TargetApi(11)
    public ShopListSyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        mContext = context;
        mContentResolver = context.getContentResolver();

    }

    private void saveDataToDb(String jsonString){
        final String JSON_CATEGORY = "category";
        final String JSON_CREATED_AT = "created_at";
        final String JSON_ITEM = "name";
        final String JSON_ID = "id";
        final String JSON_UPDATED_AT = "updated_at";
        final String JSON_USER_ID = "user_id";

        try{

            JSONObject shoplistJson = new JSONObject(jsonString);
            Log.d(LOG_TAG,shoplistJson.keys().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    // Data Transfer Code
    // Runs in a background thread
    // TODO: For now only read from server
    // TODO: Handle data conflicts, outofdate data
    // TODO: Transfer data w/o draining battery
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {


        // Read input
        InputStream inputStream = null;
        HttpURLConnection urlConn = null;
        BufferedReader bufferedReader = null;


        try {
            //  Connect to Server
            URL url = new URL(SERVER_URL);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoOutput(true);
            urlConn.setRequestMethod("GET");
            urlConn.setConnectTimeout(5000); // 5 seconds timeout
            //urlConn.setUseCaches(false); // Just in case server is set up to enable caching
            urlConn.setRequestProperty("Accept", "application/json");
            //urlConn.setRequestProperty("Content-type", "application/json");
            urlConn.setRequestProperty("X-CZ-Authorization", AUTH_TOKEN);

            urlConn.connect();
            Log.d(LOG_TAG, ""+urlConn.getResponseCode());

            if(urlConn.getResponseCode() == 200) {

                // Connection successful
                inputStream = urlConn.getInputStream();

                if (inputStream == null) {
                    Toast.makeText(mContext , "No Data Available", Toast.LENGTH_SHORT);
                    return;
                }

                // Synchronized mutable sequence of characters
                StringBuffer stringBuffer = new StringBuffer();

                // byte to character bridge
                // read from resulting character-input stream
                // using 8192 characters buffer size
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                // Download Data
                // Read JSON into a single string
                String line;
                while ((line = bufferedReader.readLine()) != null) {

                    stringBuffer.append(line + "\n"); // newline added for debugging only

                }

                if (stringBuffer.length() == 0) {
                    Toast.makeText(mContext, "No Items Available", Toast.LENGTH_SHORT);
                    return;
                }

                Log.d(LOG_TAG, "saving...");
                // Store Data into Content Provider
                saveDataToDb(stringBuffer.toString());

                // DEBUG
                Log.d(LOG_TAG, stringBuffer.toString());


            } else{

                Toast.makeText(mContext , "Can not connect to server", Toast.LENGTH_SHORT);
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            if (urlConn != null){

                urlConn.disconnect();
            }
            if (bufferedReader != null){

                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }



    }
}
