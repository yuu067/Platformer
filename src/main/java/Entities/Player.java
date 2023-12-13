package Entities;

import GameStates.Playing;
import Main.Game;
import utils.Constants;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utils.Constants.ANI_SPEED;
import static utils.Constants.EnemyConstants.IDLE;
import static utils.Constants.PlayerConstants.*;
import static utils.Constants.GRAVITY;
import static utils.HelpMethods.*;

public class Player extends Entity{
    private BufferedImage[][] animations;
    private boolean left,right,jump;
    private boolean isMoving,isAttacking;

    //JUMPING/GRAVITY
    private float jumpSpeed=-2.25f*Game.SCALE;
    private float fallSpeedAfterCollision=0.5f*Game.SCALE;
    //hasta aqui
    private float xDrawOffSet=21*Game.SCALE;
    private float yDrawOffSet=4*Game.SCALE;

    private BufferedImage statusBarImg;
    private int statusBarWidth=(int) (192*Game.SCALE);
    private int statusBarHeight=(int) (58*Game.SCALE);
    private int statusBarX=(int) (20*Game.SCALE);
    private int statusBarY=(int) (10*Game.SCALE);

    private int healthBarWidth=(int) (150*Game.SCALE);
    private int healthBarHeight=(int) (4*Game.SCALE);
    private int healthBarXStart=(int) (34*Game.SCALE);
    private int healthBarYStart=(int) (14*Game.SCALE);
    private int healthWidth=healthBarWidth;
    private int flipX=0;
    private int flipW=1;

    private int[][] lvlData;

    //attackbox

    private boolean attackChecked= false;
    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y,width,height);
        this.playing=playing;
        this.state=IDLE;
        this.maxHealth=100;
        this.currentHealth=maxHealth;
        this.walkSpeed = Game.SCALE;
        loadAnimations();
        initHitbox(20,27);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox=new Rectangle2D.Float(x,y,(int)(20*Game.SCALE),(int)(20*Game.SCALE));
    }

    public void update(){
        updateHealthBar();
        if (currentHealth<=0){
            playing.setGameOver(true);
            return;
        }
        updateAttackBox();
        updatePosition();
        if (isMoving){
            checkPotionTouched();
            checkSpikesTouched();
        }
        if (isAttacking){
            checkAttack();
        }
        updateAnimationTick();
        setAnimation();
    }

    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        if (attackChecked || aniIndex!=1){
            return;
        }
        attackChecked=true;
        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
    }
    public void setSpawn(Point spawn){
        this.x=spawn.x;
        this.y=spawn.y;
        hitbox.x=x;
        hitbox.y=y;
    }

    private void updateAttackBox() {
        if (right){
            attackBox.x= hitbox.x+hitbox.width+(int)(Game.SCALE*5);
        } else if (left) {
            attackBox.x= hitbox.x-hitbox.width-(int)(Game.SCALE*5);
        }
        attackBox.y=hitbox.y+(Game.SCALE*10);
    }

    public void render(Graphics g, int xLevelOffset){
        g.drawImage(animations[state][aniIndex], (int) (hitbox.x-xDrawOffSet)-xLevelOffset+flipX, (int) (hitbox.y-yDrawOffSet),width*flipW,height,null);
//        drawHitbox(g,xLevelOffset);
        drawUI(g);
//        drawAttackBox(g,xLevelOffset);
    }




    private void updateHealthBar() {
        healthWidth=(int) ((currentHealth/(float)maxHealth)*healthBarWidth);
    }

    public void changeHealth(int value){
        currentHealth+=value;
        if (currentHealth<=0){
            currentHealth=0;
            //gameOver();
        } else if (currentHealth>=maxHealth) {
            currentHealth=maxHealth;
        }
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg,statusBarX,statusBarY,statusBarWidth,statusBarHeight,null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart+statusBarX,healthBarYStart+statusBarY,healthWidth,healthBarHeight);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >=ANI_SPEED){
            aniTick = 0;
            aniIndex++;
            if (aniIndex>=GetSpriteAmount(state)){
                aniIndex=0;
                isAttacking=false;
                attackChecked=false;
            }
        }
    }

    private void setAnimation() {
        int startAni=state;
        if (isMoving){
            state= RUNNING;
        }else {
            state= Constants.PlayerConstants.IDLE;
        }
        if (inAir){
            if (airSpeed<0){
                state= JUMPING;
            }else {
                state= FALLING;
            }
        }
        if (isAttacking){
            state= ATTACK;
            //creo que esta comprobación es innecesaria
            if (startAni!= ATTACK){
                aniIndex=1;
                aniTick=0;
                return;
            }
            //pero está en el tuto así que se la dejo
        }
        if (startAni!=state){
            resetAni();
        }
    }

    private void resetAni() {
        aniIndex=0;
        aniTick=0;
    }

    private void updatePosition() {
        isMoving=false;
        if (jump){
            jump();
        }

//        if (!left&&!right&&!inAir)
//            return;
        if (!inAir)
            if ((!left&&!right)||(left&&right))
                return;

        float xSpeed=0;
        if (left){
            xSpeed-= walkSpeed;
            flipX=width;
            flipW=-1;
        }
        if (right){
            flipX=0;
            flipW=1;
            xSpeed+= walkSpeed;
        }
        if (!inAir){
            if (!IsEntityInFloor(hitbox,lvlData)){
                inAir=true;
            }
        }
        if (inAir){
            if (CanMoveHere(hitbox.x,hitbox.y+airSpeed,hitbox.width,hitbox.height,lvlData)){
                hitbox.y+=airSpeed;
                airSpeed+= GRAVITY;
                updatexPos(xSpeed);
            }else {
                hitbox.y=GetEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed);
                if (airSpeed>0){
                    resetInAir();
                }else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updatexPos(xSpeed);
            }
        }else {
            updatexPos(xSpeed);
        }
        isMoving=true;
    }

    private void jump() {
        if (inAir)
            return;
        inAir=true;
        airSpeed=jumpSpeed;
    }

    private void resetInAir() {
        inAir=false;
        airSpeed=0;
    }

    private void updatexPos(float xSpeed) {
        if (CanMoveHere(hitbox.x+xSpeed,hitbox.y,hitbox.width,hitbox.height,lvlData)){
            hitbox.x+=xSpeed;
        }else{
            hitbox.x=GetEntityXPosNextToWall(hitbox,xSpeed);
        }
    }

    private void loadAnimations() {
        BufferedImage image= LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations=new BufferedImage[7][8];
        for (int j=0;j<animations.length;j++){
            for (int i=0;i<animations[j].length;i++){
                animations[j][i]=image.getSubimage(i*64,j*40,64,40);
            }
        }

        statusBarImg=LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void loadLevelData(int[][]lvlData){
        this.lvlData=lvlData;
        if(!IsEntityInFloor(hitbox,lvlData)){
            inAir=true;
        }
    }

    public void resetBooleans() {
        left=false;
        right=false;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }



    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }



    public void setJump(boolean b) {
        this.jump=b;
    }

    public void resetAll() {
        resetBooleans();
        inAir=false;
        isAttacking=false;
        isMoving=false;
        state= Constants.PlayerConstants.IDLE;
        currentHealth=maxHealth;
        hitbox.x=x;
        hitbox.y=y;
        if(!IsEntityInFloor(hitbox,lvlData)){
            inAir=true;
        }
    }

    public void changePower(int bluePotionValue) {
        System.out.println("AddedPower");
    }

    public void kill() {
        currentHealth=0;
    }
}
