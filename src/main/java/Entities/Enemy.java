package Entities;

import Main.Game;

import java.awt.geom.Rectangle2D;

import static utils.Constants.ANI_SPEED;
import static utils.Constants.DirectionConstants.LEFT;
import static utils.Constants.DirectionConstants.RIGHT;
import static utils.Constants.EnemyConstants.GetSpriteAmount;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.GRAVITY;
import static utils.HelpMethods.*;
public abstract class Enemy extends Entity{
    protected int enemyType;
    protected boolean firstUpdate=true;
    protected int walkDir=LEFT;
    protected int tileY;
    protected float attackDist =Game.TILES_SIZE;
    protected boolean active=true;
    protected boolean attackChecked;

    public Enemy(float x, float y, int width, int height,int enemyType) {
        super(x, y, width, height);
        this.enemyType=enemyType;
        maxHealth=GetMaxHealth(enemyType);
        this.walkSpeed=Game.SCALE*0.35f;
        currentHealth=maxHealth;
    }
    protected void firstUpdateCheck(int [][] lvlData){
            if (!IsEntityInFloor(hitbox,lvlData)){
                inAir=true;
                firstUpdate=false;
            }
    }

    protected void inAirCheck(int [][] lvlData){
        if (CanMoveHere(hitbox.x, hitbox.y+airSpeed, hitbox.width, hitbox.height, lvlData)){
            hitbox.y+=airSpeed;
            airSpeed+= GRAVITY;
        }else {
            inAir=false;
            hitbox.y=GetEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed);
            tileY=(int)hitbox.y/Game.TILES_SIZE;
        }
    }

    protected void newState(int enemyState){
        this.state=enemyState;
        aniIndex=0;
        aniTick=0;
    }

    protected boolean canSeePlayer(int[][] lvlData,Player player){
        int playerTileY=(int)(player.getHitbox().y/Game.TILES_SIZE);
        if (playerTileY==tileY){
            if (isPlayerInRange(player)){
                if (IsSightClear(lvlData,hitbox,player.hitbox,tileY)){
                    return true;
                }
            }
        }
        return false;
    }

    public void hurt(int i) {
        currentHealth-=i;
        if(currentHealth<=0){
            newState(DEAD);
        }else {
            newState(HIT);
        }
    }
    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitbox)){
            player.changeHealth(GetEnemyDamage(enemyType));
        }
        attackChecked=true;
    }

    protected void turnTowardsPlayer(Player player){
        if (player.hitbox.x> hitbox.x){
            walkDir=RIGHT;
        }else {
            walkDir=LEFT;
        }
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue=(int)Math.abs(player.hitbox.x- hitbox.x);
        return absValue<= attackDist*5;
    }

    protected boolean isPlayerCloseForAttack(Player player){
        int absValue=(int)Math.abs(player.hitbox.x- hitbox.x);
        return absValue<= attackDist;
    }

    protected void move(int[][] lvlData){
        float xSpeed=0;
        if (walkDir==LEFT){
            xSpeed =- walkSpeed;
        }else {
            xSpeed = walkSpeed;
        }
        if (CanMoveHere(hitbox.x+xSpeed,hitbox.y, hitbox.width, hitbox.height, lvlData)){
            if (isFloor(hitbox,xSpeed,lvlData)){
                hitbox.x+=xSpeed;
                return;
            }
        }
        changeWalkDir();
    }

    protected void updateAniTick(){
        aniTick++;
        if (aniTick>=ANI_SPEED){
            aniTick=0;
            aniIndex++;
            if (aniIndex>= GetSpriteAmount(enemyType,state)){
                aniIndex=0;

                switch (state){
                    case ATTACK,HIT -> newState(IDLE);
                    case DEAD -> active=false;
                }
            }
        }
    }

    public void update(int[][] lvlData){
        updateMove(lvlData);
        updateAniTick();
    }

    private void updateMove(int[][] lvlData) {
        if (firstUpdate){
            if (!IsEntityInFloor(hitbox,lvlData)){
                inAir=true;
                firstUpdate=false;
            }
        }
        if (inAir){
            if (CanMoveHere(hitbox.x, hitbox.y+airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y+=airSpeed;
                airSpeed+= GRAVITY;
            }else {
                inAir=false;
                hitbox.y=GetEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed);
            }
        }else{
            switch (state){
                case IDLE:
                    state=RUNNING;
                    break;
                case RUNNING:
                    float xSpeed=0;
                    if (walkDir==LEFT){
                        xSpeed =- walkSpeed;
                    }else {
                        xSpeed = walkSpeed;
                    }
                    if (CanMoveHere(hitbox.x+xSpeed,hitbox.y, hitbox.width, hitbox.height, lvlData)){
                        if (isFloor(hitbox,xSpeed,lvlData)){
                            hitbox.x+=xSpeed;
                            return;
                        }
                    }
                    changeWalkDir();
                    break;
            }
        }

    }

    protected void changeWalkDir() {
        if (walkDir==LEFT){
            walkDir=RIGHT;
        }else {
            walkDir=LEFT;
        }
    }

    public boolean isActive() {
        return active;
    }

    public void resetEnemy() {
        hitbox.x=x;
        hitbox.y=y;
        firstUpdate=true;
        currentHealth=maxHealth;
        newState(IDLE);
        active=true;
        airSpeed=0;
    }
}
