package com.example.android.codeforces.Model;

public class Contest {

    private int contestId;
    private String contestName;
    private int rank;
    private int oldRating;
    private int change;
    private int newRating;

    public Contest(
            int contestId, String contestName, int rank, int oldRating, int change, int newRating) {
        this.contestId = contestId;
        this.contestName = contestName;
        this.rank = rank;
        this.oldRating = oldRating;
        this.change = change;
        this.newRating = newRating;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getOldRating() {
        return oldRating;
    }

    public void setOldRating(int oldRating) {
        this.oldRating = oldRating;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public int getNewRating() {
        return newRating;
    }

    public void setNewRating(int newRating) {
        this.newRating = newRating;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }
}
