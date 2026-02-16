// Game state
let gameState = null;
let isProcessingAction = false;
let enemyTurnTimer = null;
let idlePulseTimer = null;
let playerPositionX = -140;
let enemyPositionX = 140;

// Audio elements
const crowdCheer = document.getElementById('crowd-cheer');
const crowdSad = document.getElementById('crowd-sad');
const punchSound = document.getElementById('punch-sound');
const missSound = document.getElementById('miss-sound');
const themeMusic = document.getElementById('theme-music');

// Set audio volumes
crowdCheer.volume = 0.3;
crowdSad.volume = 0.3;
punchSound.volume = 0.4;
missSound.volume = 0.4;
themeMusic.volume = 0.3;

// Screen elements
const startScreen = document.getElementById('start-screen');
const gameScreen = document.getElementById('game-screen');
const gameoverScreen = document.getElementById('gameover-screen');

// Game elements
const playerHealthBar = document.getElementById('player-health-bar');
const enemyHealthBar = document.getElementById('enemy-health-bar');
const scoreText = document.getElementById('score-text');
const messageDisplay = document.getElementById('message-display');
const playerImg = document.getElementById('player-img');
const enemyImg = document.getElementById('enemy-img');
const playerFighter = document.getElementById('player-sprite');
const enemyFighter = document.getElementById('enemy-sprite');

// Buttons
const startButton = document.getElementById('start-button');
const leftPunchBtn = document.getElementById('left-punch-btn');
const blockBtn = document.getElementById('block-btn');
const rightPunchBtn = document.getElementById('right-punch-btn');
const resetButton = document.getElementById('reset-button');

// Result elements
const resultImage = document.getElementById('result-image');
const resultText = document.getElementById('result-text');
const finalScore = document.getElementById('final-score');

// Event Listeners
startButton.addEventListener('click', startGame);
leftPunchBtn.addEventListener('click', () => performAction('leftPunch'));
blockBtn.addEventListener('click', () => performAction('block'));
rightPunchBtn.addEventListener('click', () => performAction('rightPunch'));
resetButton.addEventListener('click', resetGame);

// Keyboard controls
document.addEventListener('keydown', (e) => {
    if (gameScreen.style.display === 'none') return;
    if (isProcessingAction) return;

    const key = e.key.toLowerCase();
    if (key === 'a') {
        performAction('leftPunch');
    } else if (key === 's') {
        performAction('block');
    } else if (key === 'd') {
        performAction('rightPunch');
    }
});

// Start the game
async function startGame() {
    startScreen.style.display = 'none';
    gameScreen.style.display = 'block';

    crowdCheer.play().catch(e => console.log('Audio play prevented:', e));

    setTimeout(() => {
        themeMusic.play().catch(e => console.log('Audio play prevented:', e));
    }, 1000);

    await updateGameState();
    startEnemyTurnLoop();
    startIdleMovement();
    applyFighterPositions();
}

// Perform player action
async function performAction(action) {
    if (isProcessingAction || gameState?.gameOver) return;

    isProcessingAction = true;
    disableControls();

    try {
        const response = await fetch('/api/game/action', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ action: action })
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        gameState = await response.json();
        updateUI();
        animateExchange(action, gameState.enemy.action);

        if (gameState.message.includes('Hit')) {
            punchSound.play().catch(e => console.log('Audio play prevented:', e));
        } else if (gameState.message.includes('Blocked')) {
            missSound.play().catch(e => console.log('Audio play prevented:', e));
        }

        setTimeout(() => {
            if (!gameState.gameOver) {
                resetSprites();
                settlePositions();
            }
        }, 500);

        if (gameState.gameOver) {
            stopGameLoops();
            setTimeout(() => {
                showGameOver();
            }, 1000);
        }

    } catch (error) {
        console.error('Error performing action:', error);
        messageDisplay.textContent = 'Error connecting to server';
    } finally {
        setTimeout(() => {
            isProcessingAction = false;
            if (!gameState?.gameOver) {
                enableControls();
            }
        }, 300);
    }
}

async function performEnemyTurn() {
    if (isProcessingAction || gameState?.gameOver) return;

    try {
        const response = await fetch('/api/game/enemy-turn', {
            method: 'POST'
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        gameState = await response.json();
        updateUI();
        animateExchange('none', gameState.enemy.action);

        if (gameState.message.includes('Enemy hit you')) {
            punchSound.play().catch(e => console.log('Audio play prevented:', e));
        }

        setTimeout(() => {
            if (!gameState.gameOver) {
                resetSprites();
                settlePositions();
            }
        }, 500);

        if (gameState.gameOver) {
            stopGameLoops();
            setTimeout(() => showGameOver(), 1000);
        }
    } catch (error) {
        console.error('Error during enemy turn:', error);
    }
}

function startEnemyTurnLoop() {
    if (enemyTurnTimer) {
        clearInterval(enemyTurnTimer);
    }

    enemyTurnTimer = setInterval(() => {
        performEnemyTurn();
    }, 2200);
}

function startIdleMovement() {
    if (idlePulseTimer) {
        clearInterval(idlePulseTimer);
    }

    idlePulseTimer = setInterval(() => {
        if (isProcessingAction || gameState?.gameOver) {
            return;
        }

        const drift = Math.random() > 0.5 ? 8 : -8;
        playerPositionX += drift;
        enemyPositionX -= drift;

        playerPositionX = clamp(playerPositionX, -180, -80);
        enemyPositionX = clamp(enemyPositionX, 80, 180);

        applyFighterPositions();
    }, 600);
}

function stopGameLoops() {
    if (enemyTurnTimer) {
        clearInterval(enemyTurnTimer);
        enemyTurnTimer = null;
    }

    if (idlePulseTimer) {
        clearInterval(idlePulseTimer);
        idlePulseTimer = null;
    }
}

function animateExchange(playerAction, enemyAction) {
    if (playerAction === 'leftPunch' || playerAction === 'rightPunch') {
        playerPositionX = -90;
    } else if (playerAction === 'block') {
        playerPositionX = -125;
    }

    if (enemyAction === 'leftHook' || enemyAction === 'rightHook') {
        enemyPositionX = 95;
    } else if (enemyAction === 'blocking') {
        enemyPositionX = 120;
    }

    applyFighterPositions();
}

function settlePositions() {
    playerPositionX = -140;
    enemyPositionX = 140;
    applyFighterPositions();
}

function applyFighterPositions() {
    if (playerFighter) {
        playerFighter.style.transform = `translateX(${playerPositionX}px)`;
    }

    if (enemyFighter) {
        enemyFighter.style.transform = `translateX(${enemyPositionX}px)`;
    }
}

function clamp(value, min, max) {
    return Math.max(min, Math.min(max, value));
}

// Update game state from server
async function updateGameState() {
    try {
        const response = await fetch('/api/game/state');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        gameState = await response.json();
        updateUI();
    } catch (error) {
        console.error('Error fetching game state:', error);
    }
}

// Update UI based on game state
function updateUI() {
    if (!gameState) return;

    const playerHealthPercent = (gameState.player.health / gameState.player.maxHealth) * 100;
    const enemyHealthPercent = (gameState.enemy.health / gameState.enemy.maxHealth) * 100;

    playerHealthBar.style.width = playerHealthPercent + '%';
    enemyHealthBar.style.width = enemyHealthPercent + '%';

    if (playerHealthPercent > 50) {
        playerHealthBar.style.background = '#00ff00';
    } else if (playerHealthPercent > 25) {
        playerHealthBar.style.background = '#ffff00';
    } else {
        playerHealthBar.style.background = '#ff0000';
    }

    if (enemyHealthPercent > 50) {
        enemyHealthBar.style.background = '#00ff00';
    } else if (enemyHealthPercent > 25) {
        enemyHealthBar.style.background = '#ffff00';
    } else {
        enemyHealthBar.style.background = '#ff0000';
    }

    scoreText.textContent = 'Player Score: ' + gameState.playerScore;
    messageDisplay.textContent = gameState.message || '';
    updateSprites();
}

function updateSprites() {
    if (!gameState) return;

    const playerAction = gameState.player.action;
    const enemyAction = gameState.enemy.action;
    const playerWounded = gameState.player.wounded;
    const enemyWounded = gameState.enemy.wounded;

    const playerPrefix = playerWounded ? 'playerBaseWounded' : 'playerBase';
    switch (playerAction) {
        case 'leftHook':
            playerImg.src = `/images/${playerWounded ? 'playerLeftHookWounded' : 'playerLeftHook'}.png`;
            break;
        case 'rightHook':
            playerImg.src = `/images/${playerWounded ? 'playerRightHookWounded' : 'playerRightHook'}.png`;
            break;
        case 'blocking':
            playerImg.src = `/images/${playerWounded ? 'playerBlockingWounded' : 'playerBlocking'}.png`;
            break;
        default:
            playerImg.src = `/images/${playerPrefix}.png`;
    }

    const enemyPrefix = enemyWounded ? 'enemyBaseWounded' : 'enemyBase';
    switch (enemyAction) {
        case 'leftHook':
            enemyImg.src = `/images/${enemyWounded ? 'enemyLeftHookWounded' : 'enemyLeftHook'}.png`;
            break;
        case 'rightHook':
            enemyImg.src = `/images/${enemyWounded ? 'enemyRightHookWounded' : 'enemyRightHook'}.png`;
            break;
        case 'blocking':
            enemyImg.src = `/images/${enemyWounded ? 'enemyBlockingWounded' : 'enemyBlocking'}.png`;
            break;
        default:
            enemyImg.src = `/images/${enemyPrefix}.png`;
    }
}

function resetSprites() {
    if (!gameState) return;

    const playerWounded = gameState.player.wounded;
    const enemyWounded = gameState.enemy.wounded;

    playerImg.src = `/images/${playerWounded ? 'playerBaseWounded' : 'playerBase'}.png`;
    enemyImg.src = `/images/${enemyWounded ? 'enemyBaseWounded' : 'enemyBase'}.png`;
}

function showGameOver() {
    gameScreen.style.display = 'none';
    gameoverScreen.style.display = 'block';

    themeMusic.pause();
    themeMusic.currentTime = 0;

    if (gameState.playerWon) {
        resultImage.src = '/images/YouWin.jpg';
        resultText.textContent = 'YOU WIN!';
        resultText.style.color = '#00ff00';
        crowdCheer.play().catch(e => console.log('Audio play prevented:', e));
    } else {
        resultImage.src = '/images/YouLost.jpg';
        resultText.textContent = 'YOU LOST!';
        resultText.style.color = '#ff0000';
        crowdSad.play().catch(e => console.log('Audio play prevented:', e));
    }

    finalScore.textContent = 'Final Score: ' + gameState.playerScore;
}

async function resetGame() {
    try {
        const response = await fetch('/api/game/reset', {
            method: 'POST'
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        gameState = await response.json();

        gameoverScreen.style.display = 'none';
        gameScreen.style.display = 'block';

        settlePositions();
        updateUI();
        enableControls();

        startEnemyTurnLoop();
        startIdleMovement();
        themeMusic.play().catch(e => console.log('Audio play prevented:', e));

    } catch (error) {
        console.error('Error resetting game:', error);
    }
}

function disableControls() {
    leftPunchBtn.disabled = true;
    blockBtn.disabled = true;
    rightPunchBtn.disabled = true;
}

function enableControls() {
    leftPunchBtn.disabled = false;
    blockBtn.disabled = false;
    rightPunchBtn.disabled = false;
}

console.log('Ultimate Boxing Game loaded!');
