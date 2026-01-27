// Game state
let gameState = null;
let isProcessingAction = false;

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
    
    // Play crowd cheer
    crowdCheer.play();
    
    // Wait a bit then start music
    setTimeout(() => {
        themeMusic.play();
    }, 1000);
    
    // Get initial game state
    await updateGameState();
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
        
        gameState = await response.json();
        updateUI();
        
        // Play sounds based on message
        if (gameState.message.includes('Hit')) {
            punchSound.play();
        } else if (gameState.message.includes('Blocked')) {
            missSound.play();
        }
        
        // Reset sprites after animation
        setTimeout(() => {
            if (!gameState.gameOver) {
                resetSprites();
            }
        }, 500);
        
        // Check for game over
        if (gameState.gameOver) {
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

// Update game state from server
async function updateGameState() {
    try {
        const response = await fetch('/api/game/state');
        gameState = await response.json();
        updateUI();
    } catch (error) {
        console.error('Error fetching game state:', error);
    }
}

// Update UI based on game state
function updateUI() {
    if (!gameState) return;
    
    // Update health bars
    const playerHealthPercent = (gameState.player.health / gameState.player.maxHealth) * 100;
    const enemyHealthPercent = (gameState.enemy.health / gameState.enemy.maxHealth) * 100;
    
    playerHealthBar.style.width = playerHealthPercent + '%';
    enemyHealthBar.style.width = enemyHealthPercent + '%';
    
    // Update health bar colors
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
    
    // Update score
    scoreText.textContent = 'Player Score: ' + gameState.playerScore;
    
    // Update message
    messageDisplay.textContent = gameState.message || '';
    
    // Update fighter sprites
    updateSprites();
}

// Update fighter sprites based on action
function updateSprites() {
    if (!gameState) return;
    
    const playerAction = gameState.player.action;
    const enemyAction = gameState.enemy.action;
    const playerWounded = gameState.player.wounded;
    const enemyWounded = gameState.enemy.wounded;
    
    // Update player sprite
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
    
    // Update enemy sprite
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

// Reset sprites to base
function resetSprites() {
    if (!gameState) return;
    
    const playerWounded = gameState.player.wounded;
    const enemyWounded = gameState.enemy.wounded;
    
    playerImg.src = `/images/${playerWounded ? 'playerBaseWounded' : 'playerBase'}.png`;
    enemyImg.src = `/images/${enemyWounded ? 'enemyBaseWounded' : 'enemyBase'}.png`;
}

// Show game over screen
function showGameOver() {
    gameScreen.style.display = 'none';
    gameoverScreen.style.display = 'block';
    
    themeMusic.pause();
    themeMusic.currentTime = 0;
    
    if (gameState.playerWon) {
        resultImage.src = '/images/YouWin.jpg';
        resultText.textContent = 'YOU WIN!';
        resultText.style.color = '#00ff00';
        crowdCheer.play();
    } else {
        resultImage.src = '/images/YouLost.jpg';
        resultText.textContent = 'YOU LOST!';
        resultText.style.color = '#ff0000';
        crowdSad.play();
    }
    
    finalScore.textContent = 'Final Score: ' + gameState.playerScore;
}

// Reset game
async function resetGame() {
    try {
        const response = await fetch('/api/game/reset', {
            method: 'POST'
        });
        gameState = await response.json();
        
        gameoverScreen.style.display = 'none';
        gameScreen.style.display = 'block';
        
        updateUI();
        enableControls();
        
        themeMusic.play();
        
    } catch (error) {
        console.error('Error resetting game:', error);
    }
}

// Disable control buttons
function disableControls() {
    leftPunchBtn.disabled = true;
    blockBtn.disabled = true;
    rightPunchBtn.disabled = true;
}

// Enable control buttons
function enableControls() {
    leftPunchBtn.disabled = false;
    blockBtn.disabled = false;
    rightPunchBtn.disabled = false;
}

// Initialize
console.log('Ultimate Boxing Game loaded!');
