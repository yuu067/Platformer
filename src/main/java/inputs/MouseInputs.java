package inputs;

import GameStates.GameState;
import Main.GamePannel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInputs implements MouseListener, MouseMotionListener {
    private GamePannel gamePannel;
    public MouseInputs(GamePannel gamePannel){
        this.gamePannel=gamePannel;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if (GameState.state == GameState.PLAYING) {
            gamePannel.getGame().getPlaying().mouseClicked(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (GameState.state){
            case PLAYING -> {
                gamePannel.getGame().getPlaying().mouseMoved(e);
            }
            case MENU -> {
                gamePannel.getGame().getMenu().mouseMoved(e);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (GameState.state){
            case PLAYING -> {
                gamePannel.getGame().getPlaying().mousePressed(e);
            }
            case MENU -> {
                gamePannel.getGame().getMenu().mousePressed(e);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (GameState.state){
            case PLAYING -> {
                gamePannel.getGame().getPlaying().mouseReleased(e);
            }
            case MENU -> {
                gamePannel.getGame().getMenu().mouseReleased(e);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (GameState.state == GameState.PLAYING) {
            gamePannel.getGame().getPlaying().mouseDragged(e);
        }
    }
}
