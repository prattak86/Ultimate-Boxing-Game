import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The enemy class extends the Fighter class, adding data fields and methods for a fighter that
 * will be controlled by random number generation.  These data fileds can be manipulated to control 
 * the difficulty level of the enemy object.  The class includes getters and setters for any data 
 * needed outside of the enemy class, and overwrites the stub methods from the fighter class.  
 * Also generates a random number to determine the enemy objects actions.
 * 
 * @author Adam Pratt and Ben Slater
 * @version v1.1 Release 5/8/2018
 */
public class Enemy extends Fighter
{
    /** Public constant data fields */
    public static final int MAX_DELAY = 60;
    
    /** Private data fields */
    private int minDelay;
    private int actionTimer;
    private int randomBlockTimer;
    private int punchTimer;
    private int minBaseTimer;
    private double damageMod;
    private double damageResist;
    
    /** Constructors */
    public Enemy() {
        super();
    }
    
    public Enemy(int maxHealth, String base, String rightHook, String leftHook, String blocking,
        int minDelay, double damageMod, double damageResist) {
        super(maxHealth, base, rightHook, leftHook, blocking);
        this.minDelay = minDelay;
        this.damageMod = damageMod;
        this.damageResist = damageResist;
        actionTimer = 0;
        getBase().scale(156, 420);
        getBlocking().scale(156, 420);
        getLeftHook().scale(156, 420);
        getRightHook().scale(156, 420);
        setImage(getBase());
    }
    
    /** Getters */
    public double getDamageResist() {
        return damageResist;
    }
    
    /** Effectors */
    /**
     * Act - Start by checking health, then determine if it is time to act yet.  If so, 
     * call chooseAction to select a random action.
     */
    public void act() 
    {
        // Add your action code here.
        checkHealth();
        if (minBaseTimer > 0) {
            minBaseTimer--;
        }
        else if (punchTimer > 0) {
            punchTimer--;
        }
        else if (randomBlockTimer > 0) {
            randomBlockTimer--;
        }
        else {
            setIsBlocking(false);
            setIsPunching(false);
            setImage(getBase());
            if (actionTimer >= minDelay || actionTimer == MAX_DELAY) {
                chooseAction();
                actionTimer = 0;
            }
            else {
                actionTimer++;
            }
        }
    }
    
    /**
     * Randomly selects and action for the enemy to perform, and updates with the 
     * appropriate image.  Also updates the appropriate status booleans.
     */
    public void chooseAction() {
        Ring ring = (Ring) getWorld();
        Player player = ring.getPlayer();
        int action = Greenfoot.getRandomNumber(100);
        
        if (action < 20 && !getIsPunching()) {
            setImage(getBlocking());
            setIsBlocking(true);
            randomBlockTimer = Greenfoot.getRandomNumber(35) + 25;
        }
        if (action >= 20 && action < 40 && !getIsBlocking() && !getIsPunching()) {
            setImage(getLeftHook());
            setIsPunching(true);
            punchTimer = 15;
            punch(player);
        }
        if (action >= 40 && action < 60 && !getIsBlocking() && !getIsPunching()) {
            setImage(getRightHook());
            setIsPunching(true);
            punchTimer = 15;
            punch(player);
        }
        if (action >= 60 && (getIsBlocking() || getIsPunching())) {
            minBaseTimer = 10;
            setIsBlocking(false);
            setIsPunching(false);
        }
    }
    
    /**
     * Checks health of enemy and changes images if damaged and checks for death.
     * If wounded, change image set to wounded sprites.
     * If dead, call the gameOver method.
     */
    public void checkHealth() {
        if (getCurrentHealth() <= getMaxHealth() / 2) {
            changeImageSet("enemyBaseWounded.png", "enemyRightHookWounded.png",
                "enemyLeftHookWounded.png", "enemyBlockingWounded.png");
            getBase().scale(156, 420);
            getBlocking().scale(156, 420);
            getLeftHook().scale(156, 420);
            getRightHook().scale(156, 420);
        }
        if (getCurrentHealth() <= 0) {
            Ring ring = (Ring) getWorld();
            ring.gameOver();
        }
    }
    
    /**
     * Checks if enemy's punch connects, and plays the appropriate sound effect.
     * Also deals damage, calls the redrawing of the healthbars, and sets the consecutive 
     * hit counter to zero.
     */
    public void punch(Player player) {
        if (!player.getIsBlocking()) {
            player.setCurrentHealth(player.getCurrentHealth() - 10.0 * damageMod);
            Ring ring = (Ring) getWorld();
            ring.drawHealthBars(true);
            player.setConsecutiveHitCounter(0);
            //getPunch().play();
            Greenfoot.playSound("punch.mp3");
        }
        else {
            //getMiss().play();
            Greenfoot.playSound("miss.mp3");
        }
    }
}
