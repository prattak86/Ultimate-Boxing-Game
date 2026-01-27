import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The fighter class hold common private data for the player and enemy classes, as well as
 * stub methods for common methods that will be overwritten.  Provides getters and setters
 * for variables that will be acessed or changed outside of the fighter class.
 * 
 * @author Adam Pratt and Ben Slater
 * @version v1.1 Release 5/8/2018
 */
public class Fighter extends Actor
{
    /** Private data fields */
    private double maxHealth;
    private double currentHealth;
    private boolean isBlocking;
    private boolean isPunching;
    private GreenfootImage base;
    private GreenfootImage rightHook;
    private GreenfootImage leftHook;
    private GreenfootImage blocking;
    
    
    /** Constructors */
    public Fighter() {
    }
    
    public Fighter(int maxHealth, String base, String rightHook, String leftHook, String blocking) {
        this.maxHealth = maxHealth;
        currentHealth = maxHealth;
        this.base = new GreenfootImage(base);
        this.rightHook = new GreenfootImage(rightHook);
        this.leftHook = new GreenfootImage(leftHook);
        this.blocking = new GreenfootImage(blocking);
        isBlocking = false;
        isPunching = false;
    }
    
    /** Getters */
    public double getCurrentHealth() {
        return currentHealth;
    }
    
    public double getMaxHealth() {
        return maxHealth;
    }
    
    public boolean getIsBlocking() {
        return isBlocking;
    }
    
    public boolean getIsPunching() {
        return isPunching;
    }
    
    public GreenfootImage getBase() {
        return base;
    }
    
    public GreenfootImage getRightHook() {
        return rightHook;
    }
    
    public GreenfootImage getLeftHook() {
        return leftHook;
    }
    
    public GreenfootImage getBlocking() {
        return blocking;
    }
    
    
    /** Setters */
    public void setCurrentHealth(double health) {
        currentHealth = health;
    }
    
    public void setIsBlocking(boolean isBlocking) {
        this.isBlocking = isBlocking;
    }
    
    public void setIsPunching(boolean isPunching) {
        this.isPunching = isPunching;
    }
    
    /** Effectors */
    /**
     * Act - do whatever the Fighter wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        // Add your action code here.
    }
    
    /**
     * Checks health of fighter.
     */
    public void checkHealth() {
    }
    
    /**
     * Checks if fighters punch does damage.
     */
    public void punch(Fighter target) {
    }
    
    /**
     * Changes the image set of the fighter.
     */
    public void changeImageSet(String newBase, String newRightHook, String newLeftHook, String newBlocking) {
        base = new GreenfootImage(newBase);
        rightHook = new GreenfootImage(newRightHook);
        leftHook = new GreenfootImage(newLeftHook);
        blocking = new GreenfootImage(newBlocking);
    }
}
