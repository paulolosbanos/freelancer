package test.freelancer.com.fltest;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import test.freelancer.com.fltest.objects.TVProgram;
import test.freelancer.com.fltest.objects.TVProgramResponse;
import test.freelancer.com.fltest.utils.PrefsManager;
import test.freelancer.com.fltest.utils.WebRequestUtil;

/**
 * List that displays the TV Programmes
 */
public class ListFragment extends Fragment{

    private PullToRefreshListView mListView;
    private ListAdapter mAdapter;
    private int preLast;
    private int maxResult = -1;
    private int index;
    private int top;
    private int lastPositionListView;
    private List<TVProgram> mListPrograms;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mListView = (PullToRefreshListView) inflater.inflate(R.layout.fragment_list, container, false);

        mListPrograms = PrefsManager.getsInstance().getCachedProgrammeResponse(); //Try to get from prefs

        mAdapter = new ListAdapter(getActivity(), R.layout.listview_item_tv_program, mListPrograms);
        mListView.setAdapter(mAdapter);
        mListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                bottomReached();
            }
        });
        // download the program guide
        new GetProgramsListTask(0).execute();

        return mListView;
    }


    private void bottomReached() {
        int offset = mAdapter.getCount();

        saveCurrentListViewPosition();
        if(offset != maxResult && maxResult != -1) {
            new GetProgramsListTask(offset).execute();
        }

        if(offset >= maxResult - 1) {
            mListView.setOnLastItemVisibleListener(null);
        }
    }

    private void saveCurrentListViewPosition() {
        lastPositionListView = mListView.getRefreshableView().getFirstVisiblePosition();

    }

    private void goToLastSavedListViewPosition() {
        mListView.post(new Runnable() {
            @Override
            public void run() {
                 if(lastPositionListView > (mAdapter.getCount() - 1)) {
                     lastPositionListView = 0;
                 }
                 mListView.getRefreshableView().setSelection(lastPositionListView);
            }
        });
    }



    private class GetProgramsListTask extends AsyncTask<Void, Void, TVProgramResponse> {

        private int offset;

        public GetProgramsListTask(int offset) {
            this.offset = offset;
        }

        @Override
        protected TVProgramResponse doInBackground(Void... voids) {
            try {
                return WebRequestUtil.requestProgramsList(offset);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(TVProgramResponse tvProgramResponse) {
            super.onPostExecute(tvProgramResponse);

            if(tvProgramResponse == null) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                return;
            }

            List<TVProgram> newList = tvProgramResponse.getResults();

            maxResult = tvProgramResponse.getCount();
//            mAdapter = new ListAdapter(getActivity(), R.layout.listview_item_tv_program, newList);
            mAdapter.updateExistingList(newList);

            mAdapter.notifyDataSetChanged();
            mListView.setAdapter(mAdapter);
            goToLastSavedListViewPosition();


        }
    }

    public class ListAdapter extends ArrayAdapter<TVProgram> {

        private final LayoutInflater inflater;
        private List<TVProgram> programsList = new ArrayList<>();

        public ListAdapter(Context context, int resId, List<TVProgram> programsList) {
            super(context, resId, programsList);
            if(programsList != null) {
                this.programsList = programsList;
            }

            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return this.programsList.isEmpty() ? 0 : this.programsList.size();
        }

        @Override
        public TVProgram getItem(int position) {
            return this.programsList.get(position);
        }

        public void updateExistingList(List<TVProgram> list) {
           for(TVProgram tvProgram : list) {
               if(!programsList.contains(tvProgram)) {
                   programsList.add(tvProgram);
               }
           }

           Log.d("TOTAL", "total program list count: " + programsList.size());

           PrefsManager.getsInstance().saveProgrammeResponse(programsList);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rootView = convertView;

            ViewHolder viewHolder;

            if(convertView == null) {

                rootView = inflater.inflate(R.layout.listview_item_tv_program, null, false);
                viewHolder = new ViewHolder();

                viewHolder.position = (TextView) rootView.findViewById(R.id.list_item_position);
                viewHolder.name = (TextView) rootView.findViewById(R.id.list_item_name);
                viewHolder.channel = (TextView) rootView.findViewById(R.id.list_item_channel_value);
                viewHolder.rating = (TextView) rootView.findViewById(R.id.list_item_rating);
                viewHolder.time = (TextView) rootView.findViewById(R.id.list_item_time_value);

                rootView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rootView.getTag();
            }

            TVProgram tvProgram = programsList.get(position);
            viewHolder.position.setText(String.valueOf(position + 1) + ".");
            viewHolder.name.setText(tvProgram.getName());
            viewHolder.channel.setText(tvProgram.getChannel());
            viewHolder.rating.setText("(Rated " + tvProgram.getRating() + ")");
            viewHolder.time.setText(tvProgram.getStartTime() + " - " + tvProgram.getEndTime());


            return rootView;
        }


        class ViewHolder {
            private TextView position;
            private TextView name;
            private TextView rating;
            private TextView channel;
            private TextView time;
        }
    }
}
