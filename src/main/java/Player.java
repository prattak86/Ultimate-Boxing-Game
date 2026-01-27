import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The player class extends the Fighter class, adding data fields and methods for a fighter that
 * will be controlled by user input.  Includes getters and setters for any data needed outside of 
 * the player class, and overwrites the stub methods from the fighter class.  Also reads input 
 * from the keyboard to determine the player objects actions.
 * 
 * @author Adam Pratt and Ben Slater
 * @version v1.1 Release 5/8/2018
 */
public class Player extends Fighter
{
    /** Private data fields */
    private int consecutiveHitCounter;
    private static final int ACTION_BUFFER = 30;
    private int actionTimer;
    
    /** Constructors */
    public Player() {
        super();
    }
    
    public Player(int maxHealth, String base, String rightHook, String leftHook, String blocking) {
        super(maxHealth, base, rightHook, leftHook, blocking);
        consecutiveHitCounter = 0;
        actionTimer = 0;
        getBase().scale(140, 385);
        getBlocking().scale(140, 385);
        getLeftHook().scale(140, 385);
        getRightHook().scale(140, 385);
        setImage(getBase());
    }
    
    /** Getters */
    public int getConsecutiveHitCounter() {
        return consecutiveHitCounter;
    }
    
    /** Setters */
    public void setConsecutiveHitCounter(int consecutiveHitCounter) {
        this.consecutiveHitCounter = consecutiveHitCounter;
    }
    
    /** Effectors */
    /**
     * Act - Start by checking health, then determine if it is time to act yet.  If so, 
     * call checkInput to act according to keyboard input.
     */
    public void act() 
    {
        // Add your action code here.
        checkHealth();
        if (actionTimer >= ACTION_BUFFER) {
            checkInput();
        }
        else {
           actionTimer++;
           if (actionTimer >= ACTION_BUFFER - 5) {
               setImage(getBase());
               setIsPunching(false);
            }
        }
    }
    
    /**
     * Checks health of player and changes images if damaged and checks for death.
     * If wounded, change image set to wounded sprites.
     * If dead, call the gameOver method.
     */
    public void checkHealth() {
        if (getCurrentHealth() <= getMaxHealth() / 2) {
            changeImageSet("playerBaseWounded.png", "playerRightHookWounded.png",
                "playerLeftHookWounded.png", "playerBlockingWounded.png");
            getBase().scale(140, 385);
            getBlocking().scale(140, 385);
            getLeftHook().scale(140, 385);
            getRightHook().scale(140, 385);
        }
        if (getCurrentHealth() <= 0) {
            Ring ring = (Ring) getWorld();
            ring.gameOver();
        }
    }
    
    /**
     * Checks or input and controls player accordingly, and changes image 
     * to match action.  Also updates the appropriate status booleans.
     */
    public void checkInput() {
        Ring ring = (Ring) getWorld();
        Enemy enemy = ring.getEnemy();
        if (Greenfoot.isKeyDown("s")  ) {
            setImage(getBlocking());
            setIsBlocking(true);
        }
        else if (Greenfoot.isKeyDown("a") && !getIsBlocking()) {
            setImage(getLeftHook());
            setIsPunching(true);
            actionTimer = 0;
            punch(enemy);
        }
        else if (Greenfoot.isKeyDown("d") && !getIsBlocking()) {
            setImage(getRightHook());
            setIsPunching(true);
            actionTimer = 0;
            punch(enemy);
        }
        else if (Greenfoot.getKey()!=null ) {
            setImage(getBase());
            setIsBlocking(false);
            setIsPunching(false);
        }
    }
    
    /**
     * Checks if player's punch connects, and plays the appropriate sound effect.
     * Also deals damage, calls the redrawing of the healthbars, updates the consecutive 
     * hit counter, and adds points to the player's score.
     */
    public void punch(Enemy enemy) {
        if (!enemy.getIsBlocking()) {
            enemy.setCurrentHealth(enemy.getCurrentHealth() - 
                10.0 * enemy.getDamageResist());
            Ring ring = (Ring) getWorld();
            Counter score = ring.getScoreCountPlayer();
            ring.drawHealthBars(false);
            score.add(5*(consecutiveHitCounter+1));
            consecutiveHitCounter++;
            Greenfoot.playSound("punch.mp3");
        }
        else {
            Greenfoot.playSound("miss.mp3");
        }
    }
}
