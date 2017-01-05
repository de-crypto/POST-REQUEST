package com.example.sabyasachib.postrequest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> contactList;
    private static String fv,success,message,result,apikey,apitoken,apkurl,download,attendence,compid,empcode,name,loginmess,mob,userid,status;
    TextView content, textoutput;
    String sapkVersion = "1.2.2", sLongitude = "0", sLattitude = "0", spassword = "Samsung@12", simei = "352311060220378", smodelname = "MotoG2", sBrowserName = "", sIPAddress = "";
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button saveme = (Button) findViewById(R.id.saveme);
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        // Get user defined values

        saveme.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                try {

                    // CALL GetText method to make post method call
                    new GetText().execute();
                } catch (Exception ex) {
                    content.setText(" url exeption! ");
                }
            }
        });
    }

    // Create GetText Metod
    protected class GetText extends AsyncTask<Void, Void, Void> {
        // Create data variable for sent values to server
        /*JSONObject jsonParam = new JSONObject();
        JSONObject mainjson = new JSONObject();
        jsonParam.put("apkVersion", sapkVersion);
        jsonParam.put("Longitude", sLongitude);
        jsonParam.put("Lattitude", sLattitude);
        jsonParam.put("password", spassword);
        jsonParam.put("imei", simei);
        jsonParam.put("ModelName", smodelname);
        jsonParam.put("BrowserName", sBrowserName);
        jsonParam.put("IPAddress", sIPAddress);

        mainjson.put("SIDTO", jsonParam);*/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Post Request is Under Process.....");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JSONObject jsonParam = new JSONObject();
            JSONObject mainjson = new JSONObject();
            try {
                jsonParam.put("apkVersion", sapkVersion);
                jsonParam.put("Longitude", sLongitude);
                jsonParam.put("Lattitude", sLattitude);
                jsonParam.put("password", spassword);
                jsonParam.put("imei", simei);
                jsonParam.put("ModelName", smodelname);
                jsonParam.put("BrowserName", sBrowserName);
                jsonParam.put("IPAddress", sIPAddress);
                mainjson.put("SIDTO", jsonParam);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://www.smartdost-siel.com/smartdost_ce_service/smartdost.svc/CommonLoginService";
            String str = mainjson.toString();
            String jsonStr = sh.excutePost(url,str);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {

                try {
                    //getting JSON Object "c"
                    JSONObject c = new JSONObject(jsonStr);

                    fv="null";
                    success="true";
                    message=c.getString("Message");
                    result="null";
                    JSONObject sres= c.getJSONObject("SingleResult");
                    apikey=sres.getString("APIKey");
                    apitoken=sres.getString("APIToken");
                    apkurl=sres.getString("APKURL");
                    attendence=sres.getString("AttendanceType");
                    compid=sres.getString("CompanyID");
                    download=sres.getString( "DownloadDataMasterCodesString");
                    empcode=sres.getString("EmplCode");
                    name=sres.getString("FirstName");
                    loginmess=sres.getString("Message");
                    mob=sres.getString("Mobile_Calling");
                    userid=sres.getString("UserID");

                    status=c.getString("StatusCode");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HashMap<String, String> contact = new HashMap<>();



                // adding each child node to HashMap key => value
                contact.put("Failed Validation", fv);
                contact.put("Is Success",success);
                contact.put("Message",message);
                contact.put("Result",result);
                contact.put("apikey",apikey);
                contact.put("apitoken",apitoken);
                contact.put("apkurl",apkurl);
                contact.put("Attendance",attendence);
                contact.put("Company id",compid);
                contact.put("DownloadDataMaster",download);
                contact.put("emplcode",empcode);
                contact.put("name", name);
                contact.put("Login Message",loginmess);
                contact.put("mobile", mob);
                contact.put("userid", userid);
                contact.put("StatusCode",status);

                // adding contact to contact list
                contactList.add(contact);
                //}
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contactList,
                    R.layout.list_item, new String[]{"Failed Validation", "Is Success",
                    "Message", "Result", "apikey", "apitoken", "apkurl", "Attendance", "Company id", "DownloadDataMaster", "emplcode", "name", "Login Message", "mobile", "userid", "StatusCode"}, new int[]{R.id.fv,
                    R.id.success, R.id.message, R.id.result, R.id.apikey, R.id.apitoken, R.id.apkurl, R.id.attend, R.id.companyid, R.id.download, R.id.emplcode, R.id.name, R.id.loginmessage, R.id.mobile, R.id.userid, R.id.status});

            lv.setAdapter(adapter);
        }
    }
}
