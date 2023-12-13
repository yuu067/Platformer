package UI;

import GameStates.GameState;
import GameStates.Playing;
import Main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverOverlay {
    private Playing playing;
    public GameOverOverlay(Playing playing){
        this.playing=playing;
    }
    public void draw(Graphics g){
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0,0, Game.GAME_WIDTH,Game.GAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.drawString("Game Over",Game.GAME_WIDTH/2,(int)(50*Game.SCALE));
        g.drawString("Pres ESC to enter the main menu!!",Game.GAME_WIDTH/2,(int)(100*Game.SCALE));

    }
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
            playing.gameResetAll();
            GameState.state=GameState.MENU;
        }
    }
}
