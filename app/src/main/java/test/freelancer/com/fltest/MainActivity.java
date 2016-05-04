package test.freelancer.com.fltest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import test.freelancer.com.fltest.objects.TVProgram;
import test.freelancer.com.fltest.utils.Connection;
import test.freelancer.com.fltest.utils.PrefsManager;
import test.freelancer.com.fltest.utils.WebRequestUtil;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MainActivity extends Activity {

    private TextView mOutput;
    private PullToRefreshListView mListView;
    private Connection mConnection = null;
    private static final int OFFSET = 0;
    private List<TVProgram> mArrayList = null;
    private PrefsManager mPrefs;
    private TVProgram mTvProgram;
    private WebRequestUtil mRequestUtil;
    private static final String TAG_NAME = "name";
    private static final String TAG_CHANNEL = "channel";
    private static final String TAG_ENDTIME = "end_time";
    private static final String TAG_STARTTIME = "start_time";
    private static final String TAG_RATING = "rating";
    private AsyncTaskParseJson loader = null;
    private ArrayAdapter<TVProgram> mAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        GetTVProgramsList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializeControls();
        // Instantiate Connection class
        mConnection = new Connection(getApplicationContext());
        // Get connection state to know if application is connected to internet

        // Reflect TV Program list to List View
        mPrefs = new PrefsManager(MainActivity.this);
        PrefsManager.init(MainActivity.this);
        GetTVProgramsList();


//        if (mConnection.isConnectingToInternet()) {
//            Toast.makeText(MainActivity.this, "You are connected to the internet...", Toast.LENGTH_LONG).show();
//            //PrefsManager.init(getApplicationContext());
//
//            // Execute AsyncTask
//
//        } else {
//            Toast.makeText(MainActivity.this, "Please connect to internet first.", Toast.LENGTH_LONG).show();
//        }
    }

    private void InitializeControls() {
        mListView = (PullToRefreshListView) findViewById(R.id.listOutput);
        mListView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loader = new AsyncTaskParseJson();
                loader.execute();
                return true;
            }
        });
     //   mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
     //       @Override
      //      public void onRefresh(PullToRefreshBase<ListView> refreshView) {

       //     }
      //  });
        mRequestUtil = new WebRequestUtil();
    }

    private void GetTVProgramsList() {
        mArrayList = mPrefs.getCachedProgrammeResponse();

        if(mArrayList == null) {
            mArrayList = new ArrayList<TVProgram>();
            loader = new AsyncTaskParseJson();
            loader.execute();
        }

        mAdapter = new ProgrammeAdapter(this, android.R.layout.simple_list_item_1, mArrayList);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public class AsyncTaskParseJson extends AsyncTask<Void, String, JSONObject> {

        final String TAG = "AsyncTaskParseJson.java";

        // set your json string url here
        String jsonUrlString = Constants.API;
        // contacts JSONArray
        JSONArray dataJsonArr = null;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected JSONObject doInBackground(Void... arg0) {

            JSONObject json = null;
            // instantiate our json parser
            JsonParser jParser = new JsonParser();

            // get json string from url
            json = mRequestUtil.getJSONFromUrl(jsonUrlString+mPrefs.getLastItemCounter());
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject post) {
            try {
                JSONArray response = post.getJSONArray("results");
                int counter = post.getInt("count");


//                String answer = post.getString();
//                Gson gson = new Gson();
//                TVProgram tvProgram = gson.fromJson(answer, TVProgram.class);
//                if (tvProgram.equals(null)) {
//                    Toast.makeText(MainActivity.this, "Error in getting Json", Toast.LENGTH_LONG).show();
//                }
                if(counter <= mPrefs.getLastItemCounter()) return;

                for (int i = 0;i < response.length();i++) {
                    mTvProgram = new TVProgram();
                    JSONObject mObject = response.getJSONObject(i);
                    // Get the values from Json Object and set to the class
                    mTvProgram.setName(mObject.get(TAG_NAME).toString().replace("\"", ""));
                    mTvProgram.setChannel(mObject.get(TAG_CHANNEL).toString().replace("\"", ""));
                    mTvProgram.setStartTime(mObject.get(TAG_STARTTIME).toString().replace("\"", ""));
                    mTvProgram.setEndTime(mObject.get(TAG_ENDTIME).toString().replace("\"", ""));
                    mTvProgram.setRating(mObject.get(TAG_RATING).toString().replace("\"", ""));
                    mArrayList.add(mTvProgram);
                }
                mPrefs.setMaxItem(counter);
                mPrefs.setLastItemCounter(mPrefs.getLastItemCounter()+response.length());
                mPrefs.saveProgrammeResponse(mArrayList);
                mAdapter.notifyDataSetChanged();




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
