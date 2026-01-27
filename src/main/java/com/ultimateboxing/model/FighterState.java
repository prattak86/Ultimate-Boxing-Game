package com.ultimateboxing.model;

/**
 * Represents the state of a fighter (player or enemy)
 */
public class FighterState {
    private String name;
    private double health;
    private double maxHealth;
    private String action; // "base", "blocking", "leftHook", "rightHook"
    private boolean wounded;

    public FighterState(String name, double health, double maxHealth) {
        this.name = name;
        this.health = health;
        this.maxHealth = maxHealth;
        this.action = "base";
        this.wounded = false;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
        if (this.health <= maxHealth / 2) {
            this.wounded = true;
        }
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isWounded() {
        return wounded;
    }

    public void setWounded(boolean wounded) {
        this.wounded = wounded;
    }

    public double getHealthPercentage() {
        return (health / maxHealth) * 100.0;
    }
}
