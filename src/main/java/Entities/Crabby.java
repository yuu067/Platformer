package Entities;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constants.DirectionConstants.RIGHT;
import static utils.Constants.EnemyConstants.*;

public class Crabby extends Enemy{
    private int attackBoxOffsetX;
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(22,19);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox=new Rectangle2D.Float(x,y,(int)(82*Game.SCALE),(int)(19*Game.SCALE));
        attackBoxOffsetX=(int) (Game.SCALE*30);
    }

    public void update(int[][] lvlData,Player player){
        updateBehaivior(lvlData,player);
        updateAniTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x= hitbox.x-attackBoxOffsetX;
        attackBox.y= hitbox.y;
    }



    private void updateBehaivior(int[][] lvlData, Player player) {
        if (firstUpdate)
           firstUpdateCheck(lvlData);

        if (inAir)
            inAirCheck(lvlData);
        else{
            switch (state){
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData,player)){
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player)){
                            newState(ATTACK);
                        }
                    }
                   move(lvlData);
                break;
                case ATTACK:
                    if (aniIndex==0){
                        attackChecked=false;
                    }
                    if (aniIndex==3 && !attackChecked){
                        checkPlayerHit(attackBox,player);
                    }
                    break;
                case HIT:
                    break;
            }
        }

    }



    public int flipX(){
        if (walkDir==RIGHT){
            return width;
        }else
            return 0;
    }
    public int flipW(){
        if (walkDir==RIGHT)
            return -1;
        else
            return 1;
    }



}
