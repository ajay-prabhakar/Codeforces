package com.example.android.codeforces;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CodeforcesAdapter extends RecyclerView.Adapter<CodeforcesAdapter.CodeforcesViewHolder> {

    private ArrayList<Codeforces> CodeforcesList;
    private Context context;


    CodeforcesAdapter(Context context, ArrayList<Codeforces> CodeforcesList){

        this.CodeforcesList =CodeforcesList;
        this.context= context;

    }

    @Nullable
    @Override
    public CodeforcesAdapter.CodeforcesViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View v =LayoutInflater.from(context).inflate(R.layout.list_contest,parent,false);
        return new CodeforcesAdapter.CodeforcesViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull CodeforcesAdapter.CodeforcesViewHolder holder, int position) {
        final Codeforces codeforces= CodeforcesList.get(position);
        holder.contestName.setText(String.valueOf(codeforces.getContestName()));
        holder.rank.setText(String.valueOf(codeforces.getRank()));
        holder.change.setText(String.valueOf(codeforces.getChange()));
        holder.newRating.setText(String.valueOf(codeforces.getNewRating()));
    }

    public class CodeforcesViewHolder extends RecyclerView.ViewHolder {

        private final TextView contestName, rank, change, newRating;

        private CodeforcesViewHolder(View itemView) {
            super(itemView);
            contestName = itemView.findViewById(R.id.tvName);
            rank = itemView.findViewById(R.id.tvRank);

            change = itemView.findViewById(R.id.tvChange);
            newRating = itemView.findViewById(R.id.tvnewRating);
        }
    }

    @Override
    public int getItemCount() {
        return CodeforcesList.size();
    }


}
