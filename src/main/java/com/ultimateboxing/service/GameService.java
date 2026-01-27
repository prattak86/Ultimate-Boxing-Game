package com.ultimateboxing.service;

import com.ultimateboxing.model.GameState;
import com.ultimateboxing.model.FighterState;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Game service managing the boxing game logic
 */
@Service
public class GameService {
    private GameState gameState;
    private Random random = new Random();
    private static final double ENEMY_DAMAGE_RESIST = 1.0;
    private static final double PLAYER_DAMAGE_RESIST = 2.0;

    public GameService() {
        this.gameState = new GameState();
    }

    public GameState getGameState() {
        return gameState;
    }

    public GameState resetGame() {
        this.gameState = new GameState();
        return gameState;
    }

    public GameState processPlayerAction(String action) {
        if (gameState.isGameOver()) {
            return gameState;
        }

        FighterState player = gameState.getPlayer();
        FighterState enemy = gameState.getEnemy();

        // Reset actions
        player.setAction("base");
        enemy.setAction("base");

        // Process player action
        switch (action) {
            case "leftPunch":
                player.setAction("leftHook");
                handlePlayerPunch();
                break;
            case "rightPunch":
                player.setAction("rightHook");
                handlePlayerPunch();
                break;
            case "block":
                player.setAction("blocking");
                gameState.setMessage("Blocking!");
                break;
            default:
                break;
        }

        // Enemy AI action (random behavior)
        enemyAI();

        // Check for game over
        checkGameOver();

        // Reset actions after a brief delay (will be handled by frontend)
        return gameState;
    }

    private void handlePlayerPunch() {
        FighterState enemy = gameState.getEnemy();
        
        if (!enemy.getAction().equals("blocking")) {
            // Hit lands
            double damage = 10.0 * ENEMY_DAMAGE_RESIST;
            enemy.setHealth(enemy.getHealth() - damage);
            
            // Update score with combo multiplier
            int consecutiveHits = gameState.getConsecutiveHits();
            int points = 5 * (consecutiveHits + 1);
            gameState.setPlayerScore(gameState.getPlayerScore() + points);
            gameState.setConsecutiveHits(consecutiveHits + 1);
            
            gameState.setMessage("Hit! +" + points + " points");
        } else {
            // Blocked
            gameState.setConsecutiveHits(0);
            gameState.setMessage("Blocked!");
        }
    }

    private void enemyAI() {
        FighterState player = gameState.getPlayer();
        FighterState enemy = gameState.getEnemy();

        // Simple AI: random action
        double actionChoice = random.nextDouble();
        
        if (actionChoice < 0.3) {
            // Enemy punches (30% chance each left/right)
            if (random.nextBoolean()) {
                enemy.setAction("leftHook");
            } else {
                enemy.setAction("rightHook");
            }
            
            // Check if punch lands
            if (!player.getAction().equals("blocking")) {
                double damage = 10.0 * PLAYER_DAMAGE_RESIST;
                player.setHealth(player.getHealth() - damage);
                gameState.setConsecutiveHits(0); // Reset combo on being hit
                gameState.setMessage(gameState.getMessage() + " Enemy hit you!");
            }
        } else if (actionChoice < 0.5) {
            // Enemy blocks (20% chance)
            enemy.setAction("blocking");
        }
        // 50% chance enemy does nothing
    }

    private void checkGameOver() {
        FighterState player = gameState.getPlayer();
        FighterState enemy = gameState.getEnemy();

        if (enemy.getHealth() <= 0) {
            gameState.setGameOver(true);
            gameState.setPlayerWon(true);
            gameState.setMessage("You Win! Final Score: " + gameState.getPlayerScore());
        } else if (player.getHealth() <= 0) {
            gameState.setGameOver(true);
            gameState.setPlayerWon(false);
            gameState.setMessage("You Lost! Final Score: " + gameState.getPlayerScore());
        }
    }
}
