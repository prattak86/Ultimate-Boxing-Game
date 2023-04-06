import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Create a welcome screen in the world to provide user input controls. This also 
 * enables the user to click anywhere on the StartFight screen to start the fight 
 * and initiate the construction of the other objects in the world.
 * 
 * @author Adam Pratt and Ben Slater
 * @version v1.1 Release 5/8/2018
 */
public class StartFight extends Actor
{
    public static final float FONT_SIZE = 32.0f;
    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;
 
    /**
     * Create a display screen to provide user input controls. This also enables the
     * user to click anywhere on the StartFight screen to start the fight and initiate
     * the construction of the other objects in the world.
     */
    public StartFight()
    {
         makeImage("WELCOME TO ULTIMATE BOXING!", 
             "Controls: 'a' is left punch,", "'s' is block,", 
             "'d' is right punch.", "Click HERE to start the fight!");
    }
 
    /**
     * Make the start fight image, welcome title, and controls.
     */
    private void makeImage(String welcomeTitle, String controlString,
             String controlString2, String controlString3, String playString)
    {
         GreenfootImage image = new GreenfootImage(WIDTH, HEIGHT);
 
         image.setColor(new Color(155,155,255, 200));
         image.fillRect(0, 0, WIDTH, HEIGHT);
         image.setColor(new Color(0, 200, 50, 128));
         image.fillRect(10, 10, WIDTH-20, HEIGHT-20);
         Font font = image.getFont();
         font = font.deriveFont(FONT_SIZE);
         image.setFont(font);
         image.setColor(Color.WHITE);
         image.drawString(welcomeTitle, 90-55, 70);
         image.drawString(controlString, 40+60, 130+30);
         image.drawString(controlString2, 177+60, 170+30);
         image.drawString(controlString3, 177+60, 210+30);
         image.drawString(playString, 90, 330);
         setImage(image);
         act();
    }
    
    /**
     * Mouse click will act upon the object to initiate the createMatch() method inside
     * the Ring class to build the remaining objects in the World. Mouse click will also
     * cause the start fight object to remove itself.
     */
    public void act()
    {
        if(Greenfoot.mouseClicked(this)){
            Ring ring = (Ring) getWorld();
            ring.createMatch();
            ring.removeObject(this);
        }
    }
}
