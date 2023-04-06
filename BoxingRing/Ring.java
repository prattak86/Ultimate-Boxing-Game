import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * This is the Ring class for the World in the Ultimate Boxing game. This class sets
 * up the boxing ring environment and calls upon various Actor classes and subclasses.
 * 
 * @author Adam Pratt and Ben Slater 
 * @version v1.1 Release 5/8/2018
 */
public class Ring extends World
{
    private GreenfootImage ringBackground;
    private StartFight startFight;
    private GreenfootSound gamePlayMusic;
    private GreenfootSound crowdCheer;
    private Player player;
    private Enemy enemy;
    private Counter scoreCountPlayer;
    private ScoreBoard displayScoreBoard;
    private String playerName;
    private String opponentName;
    public static final float FONT_SIZE = 20.0f;
    /**
     * Constructor for objects of class Ring.
     * 
     */
    public Ring()
    {    
        // Create a new world with 800x600 cells with a cell size of 1x1 pixels.
        super(800, 600, 1);
        
        setUp();

    }
    /**
     * sets up the environment inside the Ring
     */
    public void setUp(){
        ringBackground = new GreenfootImage("BoxingRing.jpg");
        ringBackground.scale(getWidth(), getHeight());
        setBackground(ringBackground);
        // StartFight subclass of Actor displays the controls and allows user to initiate
        // the match by clicking start.
        startFight = new StartFight();
        addObject(startFight, getWidth()/2, getHeight()/2); 
    }
    /**
     * method which is called by the Actor subclass StartFight.
     */
    public void createMatch(){
        // crowd cheers when you start the game!
        crowdCheer = new GreenfootSound("crowdCheer.mp3");
        crowdCheer.play();
        
        // let's not make it too loud...
        crowdCheer.setVolume(30);
        
        // maybe wait a few cycles before the Rocky theme chimes in
        Greenfoot.delay(30);
        gamePlayMusic = new GreenfootSound("Rocky Theme Song.mp3");
        
        // keep the music to a dull roar to hear the sound effects a little better
        gamePlayMusic.setVolume(30);
        gamePlayMusic.play();
        
        // set up enemy object behind player object for the visual effect
        // initializes image sets inside each constructor
        enemy = new Enemy(200, "enemyBase.png", "enemyRightHook.png", "enemyLeftHook.png",
            "enemyBlocking.png", 25, 2.0, 1.0);
        player = new Player(200, "playerBase.png", "playerRightHook.png", "playerLeftHook.png",
            "playerBlocking.png");
        
        // add the enemy and player objects, respectively, and then size them for the environment
        addObject(enemy, getWidth()/2+20, 550-enemy.getImage().getHeight()/2);
        addObject(player, getWidth()/2-20, 600-player.getImage().getHeight()/2);
        
        // initialze the Counter object to display running total of player score
        scoreCountPlayer = new Counter("Player Score: ");
        
        // add the Counter object
        addObject(scoreCountPlayer, 400, 22);
        
        // call method to draw both health bars
        drawHealthBars(true); // player health
        drawHealthBars(false); // enemy health
    }
    
    /**
     * draw the health bars
     */
    public void drawHealthBars(boolean isPlayer)
    {
        ringBackground.setColor(Color.WHITE);
        
        // boolean isPlayer to determine if it is the player or enemy health bar
        // to construct
        if(isPlayer){
            ringBackground.fillRect(20, 15, 250, 15);
            if(player.getCurrentHealth() > player.getMaxHealth()/2){
                ringBackground.setColor(Color.GREEN);
            }
            if(player.getCurrentHealth() > player.getMaxHealth()*0.25
                && player.getCurrentHealth() <= player.getMaxHealth()/2){
                ringBackground.setColor(Color.YELLOW);
            }
            if(player.getCurrentHealth() <= player.getMaxHealth()*0.25){
                ringBackground.setColor(Color.RED);
            }
            ringBackground.fillRect(20, 15, 
                (int)((player.getCurrentHealth()/player.getMaxHealth())*250), 15);
        }
        else{
            ringBackground.fillRect(getWidth()-250-20, 15, 250, 15);
            if(enemy.getCurrentHealth() > enemy.getMaxHealth()/2){
                ringBackground.setColor(Color.GREEN);
            }
            if(enemy.getCurrentHealth() > enemy.getMaxHealth()*0.25
                && enemy.getCurrentHealth() <= enemy.getMaxHealth()/2){
                ringBackground.setColor(Color.YELLOW);
            }
            if(enemy.getCurrentHealth() <= enemy.getMaxHealth()*0.25){
                ringBackground.setColor(Color.RED);
            }
            ringBackground.fillRect((getWidth()-250-20)+
                250-(int) ((enemy.getCurrentHealth()/enemy.getMaxHealth())*250),
                15, (int)((enemy.getCurrentHealth()/enemy.getMaxHealth())*250), 15);
            
            // set the color to green for the player name string below health bar
            ringBackground.setColor(Color.GREEN);
            setPlayerName("Victor 'The People's Champ' Bilko");
            ringBackground.drawString(getPlayerName(), (getWidth()/2)-379, getHeight()-555);
            
            // set the color to red for the enemy name string below the other health bar
            ringBackground.setColor(Color.RED);
            setOpponentName("Danny 'Big Red' McDoogle");
            ringBackground.drawString(getOpponentName(), (getWidth()/2)+130, getHeight()-555);
        }
    }
    /**getters for private data**/
    public Player getPlayer(){
        return player;
    }
    public Enemy getEnemy(){
        return enemy;
    }
    public Counter getScoreCountPlayer(){
        return scoreCountPlayer;
    }
    public GreenfootImage getRingBackground(){
        return ringBackground;
    }
    public String getPlayerName(){
        return playerName;
    }
    public String getOpponentName(){
        return opponentName;
    }
    
    /**setters for private data**/
    public void setPlayer(Player player){
        this.player = player;
    }
    public void getEnemy(Enemy enemy){
        this.enemy = enemy;
    }
    public void getScoreCountPlayer(Counter scoreCountPlayer){
        this.scoreCountPlayer = scoreCountPlayer;
    }
    public void getRingBackground(GreenfootImage ringBackground){
        this.ringBackground = ringBackground;
    }
    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }
    public void setOpponentName(String opponentName){
        this.opponentName = opponentName;
    }
    
    
    /**
     * The game over method is called at the end, regardless if player wins or loses
     */
    public void gameOver(){
        // if enemy's health is less than or equal to zero, then display winner scoreboard
        if(enemy.getCurrentHealth() <= 0){
            addObject(new ScoreBoard(scoreCountPlayer.getValue(), true), getWidth()/2, getHeight()/2);
        }
        // otherwise, you lose... display loser scoreboard
        else{
            addObject(new ScoreBoard(scoreCountPlayer.getValue(), false), getWidth()/2, getHeight()/2);
        }
        // waiting a few executions to give a more realistic effect of when crowd would respond
        Greenfoot.delay(5);
        gamePlayMusic.stop();
        
        // This is in case the initial crowd cheer file is still playing if win/loss in a super
        // short amount of time, this will stop the current cheer sound if it is playing. Don't
        // want to overlap sound files.
        if(crowdCheer.isPlaying()){
            crowdCheer.stop();
        }
        removeObject(scoreCountPlayer);
        // stop the game execution
        Greenfoot.stop();
    }
}
