package com.ultimateboxing.model;

/**
 * Game state model representing the current state of a boxing match
 */
public class GameState {
    private FighterState player;
    private FighterState enemy;
    private int playerScore;
    private int consecutiveHits;
    private boolean gameOver;
    private boolean playerWon;
    private String message;

    public GameState() {
        this.player = new FighterState("Victor 'The People's Champ' Bilko", 200, 200);
        this.enemy = new FighterState("Danny 'Big Red' McDoogle", 200, 200);
        this.playerScore = 0;
        this.consecutiveHits = 0;
        this.gameOver = false;
        this.playerWon = false;
        this.message = "";
    }

    // Getters and Setters
    public FighterState getPlayer() {
        return player;
    }

    public void setPlayer(FighterState player) {
        this.player = player;
    }

    public FighterState getEnemy() {
        return enemy;
    }

    public void setEnemy(FighterState enemy) {
        this.enemy = enemy;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public int getConsecutiveHits() {
        return consecutiveHits;
    }

    public void setConsecutiveHits(int consecutiveHits) {
        this.consecutiveHits = consecutiveHits;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isPlayerWon() {
        return playerWon;
    }

    public void setPlayerWon(boolean playerWon) {
        this.playerWon = playerWon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
