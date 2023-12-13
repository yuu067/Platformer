package Objects;

import Entities.Player;
import GameStates.Playing;
import Levels.Level;
import Main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.ObjectConstants.*;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImgs;
    private BufferedImage[][] containerImgs;
    private BufferedImage spikeImg;
    private ArrayList<Potion>potions;
    private ArrayList<GameContainer>containers;
    private ArrayList<Spike>spikes;

    public ObjectManager(Playing playing) {
        this.playing=playing;
        loadImgs();
    }

    public void loadObjects(Level newLevel) {
        potions=new ArrayList<>(newLevel.getPotions());
        containers=new ArrayList<>(newLevel.getGameContainers());
        spikes=newLevel.getSpikes();
    }
    private void loadImgs() {
        BufferedImage potionSprite= LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs=new BufferedImage[2][7];
        for (int j = 0; j < potionImgs.length; j++) {
            for (int i = 0; i < potionImgs[j].length; i++) {
                potionImgs[j][i]=potionSprite.getSubimage(12*i,16*j,12,16);
            }
        }

        BufferedImage ContainerSprite= LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs=new BufferedImage[2][8];
        for (int j = 0; j < containerImgs.length; j++) {
            for (int i = 0; i < containerImgs[j].length; i++) {
                containerImgs[j][i]=ContainerSprite.getSubimage(40*i,30*j,40,30);
            }
        }

        spikeImg=LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);
    }
    public void update(){
        for (Potion p :
                potions) {
            if (p.isActive()){
                p.update();
            }
        }
        for (GameContainer gc:
        containers){
            if (gc.isActive()){
                gc.update();
            }
        }
    }

    public void draw(Graphics g,int xLvlOffset){
        drawPotions(g,xLvlOffset);
        drawContainers(g,xLvlOffset);
        drawTraps(g,xLvlOffset);
    }
    public void checkSpikesTouched(Player player){
        for (Spike s:spikes){
            if (s.getHitbox().intersects(player.getHitbox())){
                player.kill();
                return;
            }
        }
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike s :
                spikes) {
            g.drawImage(spikeImg,(int)(s.getHitbox().x-xLvlOffset),(int) (s.getHitbox().y-s.getyDrawOffset()),SPIKE_WIDTH,SPIKE_HEIGHT ,null);
        }
    }

    public void checkObjecTouched(Rectangle2D.Float hitbox){
        for (Potion p :
                potions) {
            if (p.isActive()){
                if (hitbox.intersects(p.getHitbox())){
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
        }
    }
    public void applyEffectToPlayer(Potion p){
        if (p.getObjectType()==RED_POTION){
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        }else {
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
        }
    }
    public void checkObjectHit(Rectangle2D.Float attackbox){
        for (GameContainer gc :
                containers) {
            if (gc.isActive() && !gc.doAnimation) {
                if (gc.getHitbox().intersects(attackbox)){
                    gc.setAnimation(true);
                    int type=0;
                    if (gc.getObjectType()==BARREL){
                        type=1;
                    }
                    potions.add(new Potion((int)(gc.getHitbox().x+gc.getHitbox().width/2),(int)(gc.getHitbox().y-5* Game.SCALE),type));
                    return;
                }
            }
        }
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc:
                containers){
            if (gc.isActive()){
                int type=0;
                if (gc.getObjectType()==BARREL)
                    type=1;

                g.drawImage(containerImgs[type][gc.getAniIndex()],
                        (int)(gc.getHitbox().x-gc.xDrawOffset-xLvlOffset),
                        (int)(gc.getHitbox().y-gc.getyDrawOffset()),
                        CONTAINER_WIDTH,CONTAINER_HEIGHT,null);

            }
        }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion p :
                potions) {
            if (p.isActive()){
                int type=0;
                if (p.getObjectType()==RED_POTION)
                    type=1;
                g.drawImage(potionImgs[type][p.getAniIndex()],
                        (int)(p.getHitbox().x-p.xDrawOffset-xLvlOffset),
                        (int)(p.getHitbox().y-p.getyDrawOffset()),
                        POTION_WIDTH,POTION_HEIGHT,null);
            }
        }
    }

    public void resetAll() {
        loadObjects(playing.getLevelManager().getCurrentLevel());
        for (Potion p :
                potions) {
            p.reset();
        }
        for (GameContainer gc:
                containers){
            gc.reset();
        }
    }
}
