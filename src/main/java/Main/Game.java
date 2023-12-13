package Main;

import Entities.Player;
import GameStates.GameState;
import GameStates.Playing;
import GameStates.Menu;
import Levels.LevelManager;

import java.awt.*;

public class Game implements Runnable{
    private GameWindow gameWindow;
    private GamePannel gamePannel;
    private Thread gameThread;
    private final int FPS_SET=120;
    private final int UPS_SET=200;
    public final static int TILES_DEFAULT_SIZE=32;
    public final static float SCALE=1.6F;
    public static int TILES_IN_WIDTH=26;
    public static int TILES_IN_HEIGHT=14;
    public final static int TILES_SIZE= (int) (TILES_DEFAULT_SIZE*SCALE);
    public final static int GAME_WIDTH=(int)(TILES_SIZE*TILES_IN_WIDTH);
    public final static int GAME_HEIGHT=(int)(TILES_SIZE*TILES_IN_HEIGHT);
    private Playing playing;
    private Menu menu;

    public Game(){
        initClasses();
        gamePannel = new GamePannel(this);
        gameWindow = new GameWindow(gamePannel);
        gamePannel.setFocusable(true);
        gamePannel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {
        menu=new Menu(this);
        playing=new Playing(this);
    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (GameState.state){
            case PLAYING -> {
            playing.update();
            }
            case MENU -> {
                menu.update();
            }
            case QUIT,OPTIONS -> {
                System.exit(0);
            }
        }
    }
    public void render(Graphics g){
        switch (GameState.state){
            case PLAYING -> {
                playing.draw(g);
            }
            case MENU -> {
                menu.draw(g);
            }
        }
    }

    @Override
    public void run() {
        gamePannel.requestFocus();
        double timePerFrame=1000000000.0/FPS_SET;
        double timpePerUpdate=1000000000.0/UPS_SET;
        long previousTime=System.nanoTime();

        int frames=0;
        int updates=0;
        long lastCheck=System.currentTimeMillis();

        double deltaU=0;
        double deltaF=0;

        while (true){
            long currentTime=System.nanoTime();

            deltaU+=(currentTime-previousTime)/timpePerUpdate;
            deltaF+=(currentTime-previousTime)/timePerFrame;
            previousTime=currentTime;

            if (deltaU>=1){
                update();
                updates++;
                deltaU--;
            }
            if (deltaF>=1){
                gamePannel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis()-lastCheck >=1000){
                lastCheck=System.currentTimeMillis();
                System.out.println("FPS: "+frames+" UPS: "+updates);
                frames=0;
                updates=0;
            }
        }
    }
    public void windowFocusLost() {
        if (GameState.state==GameState.PLAYING){
            playing.getPlayer().resetBooleans();
        }
    }

    public Menu getMenu() {
        return menu;
    }
    public Playing getPlaying(){
        return playing;
    }
}
