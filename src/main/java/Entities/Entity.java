package Entities;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected float airSpeed;
    protected boolean inAir=false;
    protected int width;
    protected int aniTick,aniIndex;
    protected int state;
    protected int height;
    protected Rectangle2D.Float hitbox;
    protected int maxHealth;
    protected int currentHealth;
    protected Rectangle2D.Float attackBox;
    protected float walkSpeed;


    public Entity(float x, float y, int width, int height){
        this.x=x;
        this.y=y;
        this.height=height;
        this.width=width;
    }
    protected void drawHitbox(Graphics g, int xLvlOffset){
        //forDebugging
        g.setColor(Color.MAGENTA);
        g.drawRect((int) hitbox.x-xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }
    protected void drawAttackBox(Graphics g, int xLevelOffset) {
        g.setColor(Color.magenta);
        g.drawRect((int) attackBox.x-xLevelOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }
    protected void initHitbox(int width, int height) {
        hitbox= new Rectangle2D.Float(x,y,(int)(width*Game.SCALE),(int)(height*Game.SCALE));
    }
    public int getAniIndex(){
        return aniIndex;
    }
    public int getState(){
        return state;
    }
//    protected void updateHitbox(){
//        hitbox.x=(int)x;
//        hitbox.y=(int)y;
//    }
    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }
}
