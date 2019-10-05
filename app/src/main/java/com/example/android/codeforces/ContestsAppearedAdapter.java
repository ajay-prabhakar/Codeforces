package com.example.android.codeforces;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ContestsAppearedAdapter extends RecyclerView.Adapter<ContestsAppearedAdapter.ContestsViewHolder> {

    private ArrayList<Contest> contestList;
    private Context context;

    public ContestsAppearedAdapter(Context context, ArrayList<Contest> contestList){
        this.contestList = contestList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContestsAppearedAdapter.ContestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.individual_contest, parent, false);
        return new ContestsAppearedAdapter.ContestsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContestsAppearedAdapter.ContestsViewHolder holder, int position) {
        final Contest contest = contestList.get(position);
        holder.contestName.setText(String.valueOf(contest.getContestName()));
        holder.rank.setText(String.valueOf(contest.getRank()));
        holder.oldRating.setText(String.valueOf(contest.getOldRating()));
        holder.change.setText(String.valueOf(contest.getChange()));
        holder.newRating.setText(String.valueOf(contest.getNewRating()));
    }

    @Override
    public int getItemCount() {
        return contestList.size();
    }

    public class ContestsViewHolder extends RecyclerView.ViewHolder {

        private final TextView contestName, rank, oldRating, change, newRating;

        private ContestsViewHolder(View itemView) {
            super(itemView);
            contestName = itemView.findViewById(R.id.contestName);
            rank = itemView.findViewById(R.id.rank);
            oldRating = itemView.findViewById(R.id.oldRating);
            change = itemView.findViewById(R.id.change);
            newRating = itemView.findViewById(R.id.newRating);
        }
    }
}