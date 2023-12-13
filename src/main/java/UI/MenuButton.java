package UI;

import GameStates.GameState;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.Buttons.*;

public class MenuButton {
    private int x,y,rowIndex,index,xOffSetCenter=B_WIDTH/2;
    private BufferedImage[] images;
    private GameState state;
    private boolean mouseOver;
    private boolean mouseIsPressed;
    private Rectangle bounds;

    public MenuButton(int x, int y, int rowIndex, GameState state){
        this.x=x;
        this.y=y;
        this.rowIndex=rowIndex;
        this.state=state;
        loadImages();
        initBounds();
    }

    private void initBounds() {
        bounds=new Rectangle(x-xOffSetCenter,y,B_WIDTH,B_HEIGHT);
    }

    private void loadImages() {
        images=new BufferedImage[3];
        BufferedImage temp= LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < images.length; i++) {
            images[i]=temp.getSubimage(i*B_WIDTH_DEFAULT,rowIndex*B_HEIGHT_DEFAULT,B_WIDTH_DEFAULT,B_HEIGHT_DEFAULT);
        }
    }
    public void draw(Graphics g){
        g.drawImage(images[index],x -xOffSetCenter,y,B_WIDTH,B_HEIGHT,null);
    }
    public void update(){
        index=0;
        if (mouseOver)
            index=1;
        if(mouseIsPressed)
            index=2;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMouseIsPressed() {
        return mouseIsPressed;
    }

    public void setMouseIsPressed(boolean mouseIsPressed) {
        this.mouseIsPressed = mouseIsPressed;
    }

    public Rectangle getBounds() {
        return bounds;
    }
    public void applyGameState(){
        GameState.state=state;
    }
    public void resetBools(){
        mouseOver=false;
        mouseIsPressed=false;
    }
}
