package Main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;

import static Main.Game.GAME_WIDTH;
import static Main.Game.GAME_HEIGHT;

public class GamePannel extends JPanel {
    private MouseInputs mouseInputs;
    private KeyboardInputs keyboardInputs;
    private Game game;

    public GamePannel(Game game) {
        mouseInputs =new MouseInputs(this);
        keyboardInputs= new KeyboardInputs(this);
        this.game=game;
        setPannelSize();
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
        addKeyListener(keyboardInputs);
    }

    private void setPannelSize() {
        Dimension size=new Dimension(GAME_WIDTH,GAME_HEIGHT);
        setPreferredSize(size);
        System.out.println(GAME_WIDTH+":"+GAME_HEIGHT);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        game.render(g);
    }


    public Game getGame() {
        return game;
    }
}
