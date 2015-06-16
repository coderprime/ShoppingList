package ray.cyberpup.com.shoppinglistchallenge;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * Shopping List app challenge
 * <p/>
 * Design Requirements:
 * <p/>
 * 1. Access and maintaining shopping list data on server (JSON format)
 * 2. Support offline read access to list using local storage
 * 3. Support at least Android 2.3
 * 4. Gracefully handle errors
 * 5. Consistently format code to ensure readability
 * <p/>
 * Design Specifications:
 * 1. Main screen show current shopping list in a table view grouped by categories
 * 2. Add button presents a form where name and category for an item can be entered
 * 3. Save or cancel to dismiss form view
 * 4. Implement pull to refresh
 * 5. Tap item from main list to edit name and category properties
 * 6. Edit screen should include delete to remove item from list
 * 7. Editing is cancellable
 * <p/>
 * Created on 5/5/15
 * <p/>
 * Methodology:
 * Originally I have decided to use Volley (HTTP library) to perform network data transmission. Since
 * data streaming and downloading is small, I don't need to worry about Volley causing
 * memory problems as Volley holds all responses in memory during parsing.  However, I couldn't get
 * Volley to work on Gingerbread. So I opted to use a regular
 * instead
 * Note: use DownloadManager for larger operations
 * <p/>
 * <p/>
 * // TODO: implement progress bar
 *
 * @author Raymond Tong
 */
public class ShoppingListActivity extends Activity {

    private TextView mTextView = null;
    private EditText mCategory = null;
    private EditText mItem = null;

    private Button loadButton = null;
    private Button saveButton = null;
    private Button deleteButton = null;
    private Button updateButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglist);

        mCategory = (EditText) findViewById(R.id.category);
        mItem = (EditText) findViewById(R.id.item);

        mTextView = (TextView) findViewById(R.id.text);
        mTextView.setTextColor(Color.parseColor("#FFFFFF"));
        mTextView.setMovementMethod(new ScrollingMovementMethod());

        loadButton = (Button) findViewById(R.id.load);
        loadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FetchShoppingListTask task = new FetchShoppingListTask();
                task.execute("read");
            }
        });

        saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FetchShoppingListTask task = new FetchShoppingListTask();
                task.execute("save");
            }
        });

        deleteButton = (Button) findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FetchShoppingListTask task = new FetchShoppingListTask();
                task.execute("delete");
            }
        });

        updateButton = (Button)findViewById(R.id.update);
        updateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FetchShoppingListTask task = new FetchShoppingListTask();
                task.execute("update");
            }
        });


    }


    public class FetchShoppingListTask extends AsyncTask<String, Void, Integer> {

        private final String LOG_TAG = FetchShoppingListTask.class.getSimpleName();

        private final String BASE_URL = "http://czshopper.herokuapp.com";
        private final String CREATE = "/items.json";
        private final String READ = "/items.json";
        private final String UPDATE = "/items/";
        private final String DELETE = "/items/";
        private final String JSON = ".json";
        private final String AUTH_TOKEN = "5Zq5ccxFreiWsENGyRUA";

        private URL url = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        private String testJsonString = null;


        private Integer readJson() {

            // Read input
            InputStream inputStream = null;
            int responseCode = 0;
            try {
                // Initialize Connection Properties
                url = new URL(BASE_URL + READ);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Content-type", "application/json");
                urlConnection.setRequestProperty("X-CZ-Authorization", AUTH_TOKEN);

                urlConnection.connect();

                Log.d(LOG_TAG, "response read: " + urlConnection.getResponseMessage());
                Log.d(LOG_TAG, "response read: " + urlConnection.getResponseCode());

                responseCode = urlConnection.getResponseCode();

                if (responseCode == 200) {

                    inputStream = urlConnection.getInputStream();

                    // Synchronized mutable sequence of characters
                    StringBuffer buffer = new StringBuffer();

                    // byte to character bridge
                    // read from resulting character-input stream
                    // using 8192 characters buffer size
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {

                        buffer.append(line + "\n"); // newline added for debugging only

                    }

                    if (buffer.length() == 0) {
                        Log.d(LOG_TAG, "Buffer is empty...");
                    }

                    testJsonString = buffer.toString();


                }

                return responseCode;


            } catch (IOException e) {
                e.printStackTrace();

            } finally {

                if (urlConnection != null)
                    urlConnection.disconnect();

                try {
                    if (inputStream != null)
                        inputStream.close();
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return responseCode;
        }

        private Integer writeJson() {

            OutputStreamWriter outputStreamWriter= null;

            InputStream inputStream = null;
            int responseCode = 0;
            Writer out = null;

            try {

                // Initialize Connection Properties
                url = new URL(BASE_URL + CREATE);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Content-type", "application/json");

                urlConnection.setRequestProperty("X-CZ-Authorization", AUTH_TOKEN);

                urlConnection.connect();

                // JSON Object
                JSONObject saveItem = null;

                // Build jsonObject

                String category = mCategory.getText().toString();
                String item = mItem.getText().toString();

                if (category.length()!=0 && item.length()!=0){

                    saveItem = new JSONObject();
                    saveItem.put("category", category);
                    saveItem.put("name", item);

                    // Send POST output
                    outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());

                    // Java API docs suggests this for "top efficiency"
                    out = new BufferedWriter(outputStreamWriter);
                    out.write(saveItem.toString());
                    out.close();

                }

                Log.d(LOG_TAG, "response: "+urlConnection.getResponseMessage());
                Log.d(LOG_TAG, "response: "+urlConnection.getResponseCode());


                responseCode = urlConnection.getResponseCode();

                inputStream = urlConnection.getInputStream();

                // Synchronized mutable sequence of characters
                StringBuffer buffer = new StringBuffer();

                // byte to character bridge
                // read from resulting character-input stream
                // using 8192 characters buffer size
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n"); // newline added for debugging only

                }

                if (buffer.length() == 0) {
                    Log.d(LOG_TAG, "Buffer is empty...");
                }

                testJsonString = buffer.toString();



            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (urlConnection != null)
                    urlConnection.disconnect();


                if (outputStreamWriter != null)
                    try {
                        outputStreamWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                try {
                    if (inputStream != null)
                        inputStream.close();
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return responseCode;

        }

        private Integer deleteJson(String id) {

            int responseCode = 0;

            try {
                // Initialize Connection Properties
                url = new URL(BASE_URL + DELETE + id);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("X-CZ-Authorization", AUTH_TOKEN);
                urlConnection.connect();

                Log.d(LOG_TAG, urlConnection.getRequestMethod());
                Log.d(LOG_TAG, "response delete: " + urlConnection.getResponseMessage());
                Log.d(LOG_TAG, "response delete: " + urlConnection.getResponseCode());

                responseCode = urlConnection.getResponseCode();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseCode;
        }

        private Integer updateJson(String id) {

            InputStream inputStream = null;
            int responseCode = 0;
            Writer out = null;
            OutputStreamWriter outputStreamWriter=null;

            try {
                // Initialize Connection Properties
                url = new URL(BASE_URL + UPDATE + id);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Content-type", "application/json");
                urlConnection.setRequestProperty("X-CZ-Authorization", AUTH_TOKEN);
                urlConnection.connect();

                JSONObject updateItem = new JSONObject();

                String category = mCategory.getText().toString();
                String item = mItem.getText().toString();

                if (category.length()!=0 && item.length()!=0){

                    updateItem = new JSONObject();
                    updateItem.put("category", category);
                    updateItem.put("name", item);

                    // Send POST output
                    outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());

                    // Java API docs suggests this for "top efficiency"
                    out = new BufferedWriter(outputStreamWriter);
                    out.write(updateItem.toString());
                    out.close();

                }

                Log.d(LOG_TAG, urlConnection.getRequestMethod());
                Log.d(LOG_TAG, "response update: " + urlConnection.getResponseMessage());
                Log.d(LOG_TAG, "response update: " + urlConnection.getResponseCode());

                responseCode = urlConnection.getResponseCode();

                inputStream = urlConnection.getInputStream();

                // Synchronized mutable sequence of characters
                StringBuffer buffer = new StringBuffer();

                // byte to character bridge
                // read from resulting character-input stream
                // using 8192 characters buffer size
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n"); // newline added for debugging only

                }

                if (buffer.length() == 0) {
                    Log.d(LOG_TAG, "Buffer is empty...");
                }

                testJsonString = buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return responseCode;
        }



        @Override
        protected Integer doInBackground(String... params) {

            // TODO: prevent duplicate items

            String id = "1037.json"; // REMOVE

            int result=0;
            switch (params[0]) {

                case "read":
                    result = readJson();
                    break;
                case "save":
                    result = writeJson();
                    break;
                case "delete":
                    result = deleteJson(id);

                    break;
                case "update":
                    result = updateJson(id);

            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer responseCode) {

            String message="";
            mTextView.setText(testJsonString);

            switch(responseCode){
                case 200:
                    message = "OK";
                    break;
                case 201:
                    message = "Created";
                    break;
                case 422:
                    message = "Failed";
                    break;
                case 404:
                    message = "Not Found";
                    break;
                case 500:
                    message = "Server Error";
                    break;


            }
            Toast.makeText(ShoppingListActivity.this, message, Toast.LENGTH_SHORT).show();

        }
    }
}

