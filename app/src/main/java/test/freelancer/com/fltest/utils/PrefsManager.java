package test.freelancer.com.fltest.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import test.freelancer.com.fltest.objects.TVProgram;

/**
 * Created by Android 18 on 6/22/2015.
 */
public class PrefsManager {

    private static final String PREF_NAME = "tv_guide_prefs";
    private static final String KEY_TV_PROGRAM_LIST = "tv_list";
    private static PrefsManager sInstance;
    private static SharedPreferences mPref;
    private Context context;
    private final String TAG = "Freelancer";
    private static final int OFFSET = 0;

    public static void init(Context context) {
        // the default mode, where the created file can only be accessed by the calling application
        // (or all applications sharing the same user ID)
        // MODE_PRIVATE = 0 (int)
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if (sInstance == null) {
            sInstance = new PrefsManager(context);
        }
        try {
            // Request program list
            WebRequestUtil.requestProgramsList(OFFSET);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrefsManager(Context context) {

        this.context = context;
    }

    public static PrefsManager getsInstance() {
        return sInstance;
    }

    public void saveProgrammeResponse(List<TVProgram> tvProgramsList) {
        if (tvProgramsList != null && !tvProgramsList.isEmpty()) {
            Gson gson = new Gson();
            String result = gson.toJson(tvProgramsList);
            mPref.edit().putString(KEY_TV_PROGRAM_LIST, result);
            // Checker
            Log.d(TAG, "Result: " + TAG);
        }
    }

    public List<TVProgram> getCachedProgrammeResponse() {
        String result = mPref.getString(KEY_TV_PROGRAM_LIST, "");

        if (result.isEmpty()) {
            return null;
        }

        return new Gson().fromJson(result, new TypeToken<List<TVProgram>>() {
        }.getType());
    }
}
