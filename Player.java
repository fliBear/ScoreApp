package com.example.scoring;

import java.util.ArrayList;

public class Player {
    private String name;
    private int score;
    private boolean editing;
    private boolean finishedEditing;
    private boolean inputtingNextRound;
    private ArrayList<Integer> roundScores;

    public Player(String name, int score) {
        roundScores = new ArrayList<>();
        this.name = name;
        this.score = score;
        editing= false;
        finishedEditing = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int roundScore) {
        roundScores.add(roundScore);
        score += roundScores.get(roundScores.size() - 1);
    }

    /*
    subtract last round score from score
    remove last round score from list of round scores
    add new round score and add it to score
     */
    public void editLastRoundScore(int editedRoundScore) {
        if(hasRoundData()) {
            int lastElementIndex = roundScores.size() - 1;
            score -= roundScores.get(lastElementIndex);
            roundScores.remove(lastElementIndex);
            roundScores.add(editedRoundScore);
            score += roundScores.get(lastElementIndex);
        }
    }

    public boolean hasRoundData() {
        return !roundScores.isEmpty();
    }

    public void clearRoundScores() {
        roundScores.clear();
    }

    public boolean isEditing() {
        return editing;
    }

    public void startEditing() {
        editing = true;
    }

    public void stopEditing() {
        editing = false;
    }

    public void finishEditing() {
        finishedEditing = true;
    }

    public void resetPlayerEditing() {
        finishedEditing = false;
    }

    public boolean hasFinishedEditing() {
        return finishedEditing;
    }

    public boolean isInputtingNextRound() {
        return inputtingNextRound;
    }

    public void startInputtingNextRound() {
        inputtingNextRound = true;
    }

    public void stopInputtingNextRound() {
        inputtingNextRound = false;
    }
}
