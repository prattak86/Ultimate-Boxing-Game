package com.ultimateboxing.controller;

import com.ultimateboxing.model.GameState;
import com.ultimateboxing.model.PlayerAction;
import com.ultimateboxing.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for the boxing game
 */
@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/state")
    public GameState getGameState() {
        return gameService.getGameState();
    }

    @PostMapping("/action")
    public GameState performAction(@RequestBody PlayerAction action) {
        return gameService.processPlayerAction(action.getAction());
    }

    @PostMapping("/reset")
    public GameState resetGame() {
        return gameService.resetGame();
    }
}
