package GameStates;

import Entities.EnemyManager;
import Entities.Player;
import Levels.LevelManager;
import Main.Game;
import Objects.ObjectManager;
import UI.LevelCompletedOverlay;
import UI.GameOverOverlay;
import UI.PauseOverlay;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utils.Constants.Enviroment.*;
import static Main.Game.SCALE;

public class Playing extends State implements StateMethods{
    public boolean gameOver;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private Player player;
    private boolean isPaused;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private int xLevelOffset;
    private int leftBorder=(int)(0.2*Game.GAME_WIDTH);
    private int rightBorder=(int)(0.8*Game.GAME_WIDTH);
    private int maxLvlOffset;
    private BufferedImage backgroundImg;
    private BufferedImage bigCloud,smallCloud;
    private boolean lvlCompleted=false;

    private int[] smallCloudPos;
    private Random rnd=new Random();

    public Playing(Game game) {
        super(game);
        initClasses();
        backgroundImg= LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMG);
        bigCloud=LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud=LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudPos=new int[8];
        for (int i = 0; i <smallCloudPos.length; i++) {
            smallCloudPos[i]=(int)(92* SCALE)+ rnd.nextInt((int) (100* SCALE));
        }
        calculateLvlOffset();
        loadStartLevel();
    }

    public void loadNextLevel(){
        gameResetAll();
        lvlCompleted=false;
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }
    public void setMaxLvlOffset(int levelOffset){
        this.maxLvlOffset=levelOffset;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calculateLvlOffset() {
        maxLvlOffset=levelManager.getCurrentLevel().getMaxLvlOffset();
    }

    private void initClasses() {
        levelManager=new LevelManager(game);
        enemyManager=new EnemyManager(this);
        objectManager=new ObjectManager(this);
        player=new Player(50*SCALE,200*SCALE,(int) (64 * SCALE), (int) (40 * SCALE),this);
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        pauseOverlay=new PauseOverlay(this);
        gameOverOverlay=new GameOverOverlay(this);
        levelCompletedOverlay =new LevelCompletedOverlay(this);
    }


    @Override
    public void update() {
        if (isPaused){
            pauseOverlay.update();
        } else if (lvlCompleted) {
            levelCompletedOverlay.update();
        }else if(!gameOver){
            levelManager.update();
            player.update();
            objectManager.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(),player);
            checkCloseToBorder();
        }

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg,0,0,Game.GAME_WIDTH,Game.GAME_HEIGHT,null );
        drawClouds(g);
        levelManager.draw(g,xLevelOffset);
        player.render(g,xLevelOffset);
        enemyManager.draw(g,xLevelOffset);
        objectManager.draw(g,xLevelOffset);
        if (isPaused){
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0,0,Game.GAME_WIDTH,Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        }else if (gameOver){
            gameOverOverlay.draw(g);
        } else if (lvlCompleted) {
            levelCompletedOverlay.draw(g);
        }
    }

    private void checkCloseToBorder() {
        int playerX=(int)player.getHitbox().x;
        int diff=playerX-xLevelOffset;

        if (diff>rightBorder){
            xLevelOffset+=diff-rightBorder;
        } else if (diff<leftBorder) {
            xLevelOffset+=diff-leftBorder;
        }

        if (xLevelOffset>maxLvlOffset)
            xLevelOffset=maxLvlOffset;

        else if (xLevelOffset<0)
            xLevelOffset=0;

    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < 3; i++) {
            g.drawImage(bigCloud, (i * BIG_CLOUD_WIDTH)- (int)(xLevelOffset*0.3),(int)(203 * Game.SCALE),BIG_CLOUD_WIDTH,BIG_CLOUD_HEIGHT,null); //en el tuto era 204
        }
        for (int i = 0; i < smallCloudPos.length; i++) {
            g.drawImage(smallCloud,(SMALL_CLOUD_WIDTH*4*i) - (int)(xLevelOffset*0.7),smallCloudPos[i],SMALL_CLOUD_WIDTH,SMALL_CLOUD_HEIGHT,null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver){
            if (e.getButton()==MouseEvent.BUTTON1){
                player.setAttacking(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver){
            if (isPaused) {
                pauseOverlay.mousePressed(e);
            } else if (lvlCompleted) {
                levelCompletedOverlay.mousePressed(e);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver){
            if (isPaused) {
                pauseOverlay.mouseReleased(e);
            } else if (lvlCompleted) {
                levelCompletedOverlay.mouseReleased(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver){
            if (isPaused) {
                pauseOverlay.mouseMoved(e);
            } else if (lvlCompleted) {
                levelCompletedOverlay.mouseMoved(e);
            }
    }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            gameOverOverlay.keyPressed(e);
        }else if (!lvlCompleted){
            switch (e.getKeyCode()){
                case KeyEvent.VK_W:
                    //nada por ahora
                    break;
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_S:
                    //nada por ahora
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    GameState.state=GameState.MENU;
                    break;
                case KeyEvent.VK_ESCAPE:
                    isPaused=!isPaused;
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver){
            switch (e.getKeyCode()){
                case KeyEvent.VK_W:
                    //nada por ahora
                    break;
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_S:
                    //nada por ahora
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }
        }
    }
    public void unpauseGame(){
        isPaused=false;
    }
    public Player getPlayer() {
        return player;
    }

    public void windowFocusLost() {
        player.resetBooleans();
    }

    public void mouseDragged(MouseEvent e) {
        if (!gameOver)
            if (isPaused)
                    pauseOverlay.mouseDragged(e);
    }

    public void gameResetAll() {
        //TODO: RESET ALL
        gameOver=false;
        unpauseGame();
        player.resetAll();
        objectManager.resetAll();
        enemyManager.resetAllEnemies();
    }


    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void setGameOver(boolean b) {
        this.gameOver=b;
    }

    public void setLevelCompleted(boolean b) {
        this.lvlCompleted=b;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox) {
        objectManager.checkObjecTouched(hitbox);
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
         objectManager.checkObjectHit(attackBox);
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public void checkSpikesTouched(Player player) {
        objectManager.checkSpikesTouched(player );
    }
}
