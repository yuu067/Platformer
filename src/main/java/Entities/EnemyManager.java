package Entities;

import GameStates.Playing;
import Levels.Level;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.*;


public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] crabbyArray;
    private ArrayList<Crabby> crabbies=new ArrayList<>();
    private boolean isAnyActive=true;

    public EnemyManager(Playing playing){
        this.playing=playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        crabbies=level.getCrabbies();
        System.out.println(crabbies.size());
    }

    private void loadEnemyImgs() {
        crabbyArray=new BufferedImage[5][9];
        BufferedImage temp= LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
        for (int j = 0; j < crabbyArray.length; j++) {
            for (int i = 0; i < crabbyArray[j].length; i++) {
                crabbyArray[j][i]=temp.getSubimage(i*CRABBY_WIDTH_DEFAULT,j*CRABBY_HEIGHT_DEFAULT,CRABBY_WIDTH_DEFAULT,CRABBY_HEIGHT_DEFAULT);
            }
        }
    }
    public void update(int[][] lvlData,Player player){
        isAnyActive=false;
        for (Crabby c:
             crabbies) {
            if (c.isActive()){
                c.update(lvlData,player);
                isAnyActive=true;
            }
        }
        if (!isAnyActive){
            playing.setLevelCompleted(true);
        }
    }
    public void checkEnemyHit(Rectangle2D.Float attackbox){
        for (Crabby c:crabbies){
            if(c.isActive()){
                if (attackbox.intersects(c.getHitbox())) {
                    c.hurt(10);
                    return;
                }
            }
        }
    }
    public void draw(Graphics g, int xLevelOffset){
        drawCrabs(g,xLevelOffset);
    }

    private void drawCrabs(Graphics g, int xLevelOffset) {
        for (Crabby c:
                crabbies) {
            if (c.isActive()){
                g.drawImage(crabbyArray[c.getState()][c.getAniIndex()],(int)c.getHitbox().x-xLevelOffset-CRABBY_DRAW_OFFSET_X+c.flipX(),(int)c.getHitbox().y- CRABBY_DRAW_OFFSET_Y,CRABBY_WIDTH*c.flipW(),CRABBY_HEIGHT,null);
//            c.drawHitbox(g,xLevelOffset); //for debbuging purposes
//                c.drawAttackBox(g,xLevelOffset);
            }
        }
    }

    public void resetAllEnemies() {
        for (Crabby c :
                crabbies) {
            c.resetEnemy();
        }
    }
}
