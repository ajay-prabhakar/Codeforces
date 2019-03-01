package com.example.android.codeforces;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CodeforcesAdapter extends ArrayAdapter<Codeforces> {

    public CodeforcesAdapter(Activity context, List<Codeforces> codeforces) {
        super(context, 0, codeforces);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_contest, parent, false);
        }


        Codeforces currentCodeforces = getItem(position);



        TextView nameView = (TextView)listItemView.findViewById(R.id.tvName);
        nameView.setText(currentCodeforces.getmName());



        TextView durationView = (TextView)listItemView.findViewById(R.id.tvDuration);
        durationView.setText(currentCodeforces.getmDuration());



        TextView startTime = (TextView)listItemView.findViewById(R.id.tvStart);
        startTime.setText(currentCodeforces.getmStartTime());


        return listItemView;

    }


}
