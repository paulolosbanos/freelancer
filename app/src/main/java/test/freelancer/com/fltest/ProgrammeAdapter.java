package test.freelancer.com.fltest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import test.freelancer.com.fltest.objects.TVProgram;

/**
 * Created by Android 18 on 5/4/2016.
 */
public class ProgrammeAdapter extends ArrayAdapter<TVProgram> {

    private List<TVProgram> programsList = new ArrayList<>();
    private LayoutInflater inflater = null;

    public ProgrammeAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ProgrammeAdapter(Context context, int resource, List<TVProgram> items) {
        super(context, resource, items);
        programsList = items;
        inflater = LayoutInflater.from(context);
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
