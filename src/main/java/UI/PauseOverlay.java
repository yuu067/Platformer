package UI;

import GameStates.GameState;
import GameStates.Playing;
import Main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.PauseButtons.SOUND_SIZE;
import static utils.Constants.UI.Urm.*;
import static utils.Constants.UI.VolumeButton.*;
public class PauseOverlay {
    private  Playing playing;
    private BufferedImage backgroundImg;
    private int bgX,bgY,bgW,bgH;
    private SoundButton musicButton,sfxButton;
    private UrmButton menuB,replayB,unpauseB;
    private VolumeButton volumeButton;
    public PauseOverlay(Playing playing){
        this.playing=playing;
        loadBackground();
        createSoundButtons();
        createUrmButtons();
        createVolumeButton();
    }

    private void createVolumeButton() {
        int vX=(int) (309*Game.SCALE);
        int vY=(int)(278*Game.SCALE);
        volumeButton=new VolumeButton(vX,vY,SLIDER_WIDTH,VOLUME_HEIGHT);
    }

    private void createUrmButtons() {
        int menuX=(int)(313*Game.SCALE);
        int replayX=(int)(387*Game.SCALE);
        int unpauseX=(int)(462*Game.SCALE);
        int bY=(int)(325*Game.SCALE);
        menuB=new UrmButton(menuX,bY,URM_SIZE,URM_SIZE,2);
        replayB=new UrmButton(replayX,bY,URM_SIZE,URM_SIZE,1);
        unpauseB=new UrmButton(unpauseX,bY,URM_SIZE,URM_SIZE,0);
    }

    private void createSoundButtons() {
        int soundX=(int)(450*Game.SCALE);
        int musicY =(int) (140*Game.SCALE);
        int sfxY=(int)(186*Game.SCALE);
        musicButton=new SoundButton(soundX, musicY,SOUND_SIZE,SOUND_SIZE);
        sfxButton=new SoundButton(soundX, sfxY,SOUND_SIZE,SOUND_SIZE);

    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW=(int)(backgroundImg.getWidth()* Game.SCALE);
        bgH=(int)(backgroundImg.getHeight()*Game.SCALE);
        bgX=Game.GAME_WIDTH/2-bgW/2;
        bgY=(int)(25*Game.SCALE);

    }

    public void update(){
        musicButton.update();
        sfxButton.update();
        menuB.update();
        replayB.update();
        unpauseB.update();
        volumeButton.update();
    }

    public void draw(Graphics g){
        g.drawImage(backgroundImg,bgX,bgY,bgW,bgH,null);

        musicButton.draw(g);
        sfxButton.draw(g);
        //urm
        replayB.draw(g);
        menuB.draw(g);
        unpauseB.draw(g);
        //Slider
        volumeButton.draw(g);
    }
    public void mouseDragged(MouseEvent e){
        if (volumeButton.isMousePressed()){
            volumeButton.changeX(e.getX());
        }
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e,musicButton)){
            musicButton.setMousePressed(true);
        } else if (isIn(e,sfxButton)) {
            sfxButton.setMousePressed(true);
        } else if (isIn(e,menuB)) {
            menuB.setMousePressed(true);
        } else if (isIn(e,replayB)) {
            replayB.setMousePressed(true);
        } else if (isIn(e,unpauseB)) {
            unpauseB.setMousePressed(true);
        } else if (isIn(e,volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }

    private boolean isIn(MouseEvent e, PauseButton pb){
        return pb.getBounds().contains(e.getX(),e.getY());
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e,musicButton)){
            if (musicButton.getMousePressed()){
                musicButton.setMuted(!musicButton.getMuted());
            }
        } else if (isIn(e,sfxButton)) {
            if (sfxButton.getMousePressed()){
                sfxButton.setMuted(!sfxButton.getMuted());
            }
        }else if (isIn(e,menuB)) {
            if (menuB.isMousePressed()){
                GameState.state=GameState.MENU;
                playing.unpauseGame();
            }
        }else if (isIn(e,replayB)) {
            if (replayB.isMousePressed()){
                playing.gameResetAll();
            }
        }else if (isIn(e,unpauseB)) {
            if (unpauseB.isMousePressed()){
                playing.unpauseGame();
            }
        }
        musicButton.resetBools();
        sfxButton.resetBools();
        unpauseB.resetBools();
        replayB.resetBools();
        menuB.resetBools();
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        unpauseB.setMouseOver(false);
        replayB.setMouseOver(false);
        menuB.setMouseOver(false);
        volumeButton.setMouseOver(false);
        if (isIn(e,musicButton)){
            musicButton.setMouseOver(true);
        }else if(isIn(e,sfxButton)){
            sfxButton.setMouseOver(true);
        }else if(isIn(e,menuB)){
            menuB.setMouseOver(true);
        }else if(isIn(e,replayB)){
            replayB.setMouseOver(true);
        }else if(isIn(e,unpauseB)){
            unpauseB.setMouseOver(true);
        }else if(isIn(e,volumeButton)){
            volumeButton.setMouseOver(true);
        }
    }


    public void keyPressed(KeyEvent e) {

    }


    public void keyReleased(KeyEvent e) {

    }

}
