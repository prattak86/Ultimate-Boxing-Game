package com.ultimateboxing.model;

/**
 * Player action request
 */
public class PlayerAction {
    private String action; // "leftPunch", "rightPunch", "block", "reset"
    
    public PlayerAction() {
    }
    
    public PlayerAction(String action) {
        this.action = action;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
}
