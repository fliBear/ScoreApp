package com.example.scoring;

import java.util.ArrayList;

public class Game {

    private static  Game instance;
    private ArrayList<Player> players;
    private int numOfPlayers;
    private int defaultValue;
    private int round;
    private SortType sortType;
    private Theme theme;
    private boolean hasDefaultValue;
    private boolean editing;
    private boolean inputtingNextRound;

    private Game() {
        players = new ArrayList<>();
        round = 0;
        hasDefaultValue = false;
        editing = false;
        inputtingNextRound = false;
    }

    public static Game getInstance() {
        if(instance == null) {
            instance = new Game();
            return new Game();
        } else {
            return instance;
        }
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public void removePlayer() {
        numOfPlayers--;
    }

    public void addPlayer() {
        numOfPlayers++;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
        hasDefaultValue = true;
    }

    public int getRound() {
        return round;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
        sortPlayers();
    }

    public void sortPlayers() {
        switch (sortType) {
            case LOWEST_FIRST:
                players.sort((p1, p2) -> p1.getScore() - p2.getScore());
                break;
            case HIGHEST_FIRST:
                players.sort((p1, p2) -> p2.getScore() - p1.getScore());
                break;
            default:
                break;
        }
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public boolean hasDefaultValue() {
        return hasDefaultValue;
    }

    public boolean isSetupFinished() {
        return players.size() == numOfPlayers;
    }

    public boolean createPlayer(String name) {
        return players.add(new Player(name, defaultValue));
    }

    public boolean createPlayer(String name, int score) {
        return players.add(new Player(name, score));
    }

    public void clearPlayers() {
        players.clear();
    }

    public ArrayList<Player> getPlayers() {
        return players;
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

    public boolean isInputtingNextRound() {
        return inputtingNextRound;
    }

    public void startNextRound() {
        players.get(0).startInputtingNextRound();
        round++;
        inputtingNextRound = true;
    }

    public void stopInputtingNextRound() {
        inputtingNextRound = false;
    }
}
