package UI;

import utils.LoadSave;
import static utils.Constants.UI.PauseButtons.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SoundButton extends PauseButton{
    private BufferedImage[][] soundImages;
    private Boolean mouseOver,mousePressed;
    private Boolean muted;
    private int rowIndex,colIndex;

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        mouseOver=false;
        mousePressed=false;
        muted=false;
        loadSoundImgs();
    }

    private void loadSoundImgs() {
        BufferedImage temp= LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
        soundImages=new BufferedImage[2][3];
        for (int j = 0; j < soundImages.length ; j++) {
            for (int i = 0; i < soundImages[j].length; i++) {
                    soundImages[j][i]=temp.getSubimage(i*SOUND_SIZE_DEFAULT,j*SOUND_SIZE_DEFAULT,SOUND_SIZE_DEFAULT,SOUND_SIZE_DEFAULT);
            }
        }
    }
    public void update(){
        if (muted){
            rowIndex=1;
        }else{
            rowIndex=0;
        }
        colIndex=0;
        if (mouseOver){
            colIndex=1;
        }
        if (mousePressed){
            colIndex=2;
        }
    }

    public void resetBools(){
        mouseOver=false;
        mousePressed=false;
    }
    public void draw(Graphics g){
        g.drawImage(soundImages[rowIndex][colIndex],x,y,width,height,null);
    }

    public Boolean getMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(Boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public Boolean getMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(Boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public Boolean getMuted() {
        return muted;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
    }
}
