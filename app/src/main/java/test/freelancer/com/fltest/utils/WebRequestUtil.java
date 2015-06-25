package test.freelancer.com.fltest.utils;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import test.freelancer.com.fltest.Constants;
import test.freelancer.com.fltest.objects.TVProgramResponse;

/**
 * Created by Android 18 on 6/22/2015.
 */
public class WebRequestUtil {

    private static InputStream inputStream;
    private static String response;
    private static String result;
    private static JSONObject jObject = null;
    private static JSONArray dataJsonArr = null;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    final String TAG = "JsonParser.java";

    public static TVProgramResponse requestProgramsList(int offset) throws IOException {

        String response = connect(Constants.API);

        Gson gson = new Gson();
        // Debug log
        Log.d("response", "response (offset: " + offset + "): " + response);
        return gson.fromJson(response, TVProgramResponse.class);
    }

    public static String connect(String url) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response;
        response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
//            String result = convertStreamToString(instream);

            instream.close();
            return result;
        }
        return null;
    }

    public JSONObject getJSONFromUrl(String url) {

        // make HTTP request
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
            is.close();
            json = sb.toString();

        } catch (Exception e) {
            Log.e(TAG, "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }
    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            json = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Get Json as Json Object
                jObject = new JSONObject(json);
                // Next step: Parse the Json
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Return json
        return json;
    }

//    private static class getTvProgramsList extends AsyncTask<String, Integer, Void> {
//
//        String yourJsonStringUrl = response;
//
//        JSONArray dataJsonArr = null;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(String... args) {
//            try {
//                // instantiate our json parser
//                JsonParser jParser = new JsonParser();
//                JSONObject mSon = (JSONObject) json;
//                // get the array of users
//                dataJsonArr = mSon.getJSONArray("Users");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(JsonObject toResponse) {
//        }
//    }
}
