import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * ScoreBoard class creates a score board at the end of the game and displays
 * different results depending on if you win or lose.
 * 
 * @author Adam Pratt and Ben Slater
 * @version v1.1 Release 5/8/2018
 */
public class ScoreBoard extends Actor
{
    public static final float FONT_SIZE = 48.0f;
    public static final int WIDTH = 600;
    public static final int HEIGHT = 300;
    private GreenfootSound cheerSound;
    private GreenfootSound ahhSound;
    /**
     * Create a score board with dummy result for testing.
     */
    public ScoreBoard()
    {
        this(100, true);
    }

    /**
     * Create a score board for the final result.
     */
    public ScoreBoard(int score, boolean isWinner)
    {
        if(isWinner){
            makeImage("     WINNER!", "Score: ", score);
            cheerSound = new GreenfootSound("crowdCheer.mp3");
            cheerSound.play();
        }
        else{
            makeImage("YOU LOSE :-(", "Score: ", score);
            ahhSound = new GreenfootSound("crowdSad.mp3");
            ahhSound.play();
        }
    }

    /**
     * Make the score board image.
     */
    private void makeImage(String title, String prefix, int score)
    {
        GreenfootImage image = new GreenfootImage(WIDTH, HEIGHT);

        image.setColor(new Color(255,255,255, 128));
        image.fillRect(0, 0, WIDTH, HEIGHT);
        image.setColor(new Color(0, 0, 0, 128));
        image.fillRect(5, 5, WIDTH-10, HEIGHT-10);
        Font font = image.getFont();
        font = font.deriveFont(FONT_SIZE);
        image.setFont(font);
        image.setColor(Color.WHITE);
        image.drawString(title, image.getWidth()/2-160, 100);
        image.drawString(prefix + score, image.getWidth()/2-105, 220);
        setImage(image);
    }
}
