package com.ultimateboxing.service;

import com.ultimateboxing.model.FighterState;
import com.ultimateboxing.model.GameState;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Game service managing the boxing game logic
 */
@Service
public class GameService {
    private static final double PLAYER_PUNCH_DAMAGE = 10.0;
    private static final double ENEMY_PUNCH_DAMAGE = 20.0;

    private GameState gameState;
    private final Random random = new Random();

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

    public synchronized GameState processPlayerAction(String action) {
        if (gameState.isGameOver()) {
            return gameState;
        }

        FighterState player = gameState.getPlayer();
        FighterState enemy = gameState.getEnemy();

        resetRoundActions(player, enemy);

        // Set player's intent first so an immediate enemy punch can be blocked.
        switch (action) {
            case "leftPunch":
                player.setAction("leftHook");
                break;
            case "rightPunch":
                player.setAction("rightHook");
                break;
            case "block":
                player.setAction("blocking");
                gameState.setMessage("Blocking!");
                break;
            default:
                gameState.setMessage("Stay alert...");
                break;
        }

        // Enemy decides this same exchange.
        enemyAI();

        // Resolve player punch after enemy chooses action, so enemy blocks can matter.
        if ("leftHook".equals(player.getAction()) || "rightHook".equals(player.getAction())) {
            handlePlayerPunch();
        }

        checkGameOver();
        return gameState;
    }

    /**
     * Processes an enemy-only turn so the game keeps moving even if the player idles.
     */
    public synchronized GameState processEnemyTurn() {
        if (gameState.isGameOver()) {
            return gameState;
        }

        FighterState player = gameState.getPlayer();
        FighterState enemy = gameState.getEnemy();

        resetRoundActions(player, enemy);
        gameState.setMessage("Stay alert...");

        enemyAI();
        checkGameOver();

        return gameState;
    }

    private void resetRoundActions(FighterState player, FighterState enemy) {
        player.setAction("base");
        enemy.setAction("base");
    }

    private void handlePlayerPunch() {
        FighterState enemy = gameState.getEnemy();

        if (!"blocking".equals(enemy.getAction())) {
            enemy.setHealth(enemy.getHealth() - PLAYER_PUNCH_DAMAGE);

            int consecutiveHits = gameState.getConsecutiveHits();
            int points = 5 * (consecutiveHits + 1);
            gameState.setPlayerScore(gameState.getPlayerScore() + points);
            gameState.setConsecutiveHits(consecutiveHits + 1);

            gameState.setMessage("Hit! +" + points + " points");
        } else {
            gameState.setConsecutiveHits(0);
            gameState.setMessage("Blocked!");
        }
    }

    private void enemyAI() {
        FighterState player = gameState.getPlayer();
        FighterState enemy = gameState.getEnemy();

        double actionChoice = random.nextDouble();

        if (actionChoice < 0.4) {
            enemy.setAction(random.nextBoolean() ? "leftHook" : "rightHook");

            if (!"blocking".equals(player.getAction())) {
                player.setHealth(player.getHealth() - ENEMY_PUNCH_DAMAGE);
                gameState.setConsecutiveHits(0);
                appendMessage("Enemy hit you!");
            } else {
                appendMessage("Enemy punch blocked!");
            }
        } else if (actionChoice < 0.7) {
            enemy.setAction("blocking");
            appendMessage("Enemy is blocking.");
        }
    }

    private void appendMessage(String text) {
        String currentMessage = gameState.getMessage();

        if (currentMessage == null || currentMessage.isBlank()) {
            gameState.setMessage(text);
            return;
        }

        gameState.setMessage(currentMessage + " " + text);
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
