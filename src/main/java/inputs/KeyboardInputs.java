package inputs;

import GameStates.GameState;
import Main.GamePannel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static utils.Constants.DirectionConstants.*;
public class KeyboardInputs implements KeyListener {
    private GamePannel gamePannel;
    public KeyboardInputs(GamePannel gamePannel) {
        this.gamePannel=gamePannel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
//        System.out.println("funciona");
        switch (GameState.state){
            case PLAYING -> {
                gamePannel.getGame().getPlaying().keyPressed(e);
            }
            case MENU -> {
                gamePannel.getGame().getMenu().keyPressed(e);
            }
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (GameState.state){
            case PLAYING -> {
                gamePannel.getGame().getPlaying().keyReleased(e);
            }
            case MENU -> {
                gamePannel.getGame().getMenu().keyReleased(e);
            }
        }
    }
}
