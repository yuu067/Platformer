package utils;

import Entities.Crabby;
import Main.Game;
import Objects.Cannon;
import Objects.GameContainer;
import Objects.Potion;
import Objects.Spike;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.CRABBY;
import static utils.Constants.ObjectConstants.*;

public class HelpMethods {
    public static boolean CanMoveHere(float x,float y,float width,float height,int[][]lvlData){
        if (!isSolid(x,y,lvlData)){
            if (!isSolid(x+width,y+height,lvlData)){
                if (!isSolid(x+width,y,lvlData)){
                    if (!isSolid(x,y+height,lvlData)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile=(int)(hitbox.y/Game.TILES_SIZE);
        if (airSpeed>0){
            int tileYPos=currentTile*Game.TILES_SIZE;
            int yOffsett=(int)(Game.TILES_SIZE-hitbox.height);
//            if (Game.SCALE==1||Game.SCALE==2)
//                return tileYPos+yOffsett-1;
//            else
                return tileYPos+yOffsett-1;
        }else {
            return currentTile*Game.TILES_SIZE;
        }
    }

    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed>0){
            return isSolid(hitbox.x+ hitbox.width+xSpeed, hitbox.y+ hitbox.height+1,lvlData);
        }else {
            return isSolid(hitbox.x+xSpeed, hitbox.y+ hitbox.height+1,lvlData);

        }
    }

    public static boolean IsEntityInFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if(!isSolid(hitbox.x,hitbox.y+ hitbox.height+1,lvlData)){
            if(!isSolid(hitbox.x+ hitbox.width,hitbox.y+ hitbox.height+1,lvlData)){
                return false;
            }
        }
        return true;
    }
    public static int[][] GetLevelData(BufferedImage img){
        int[][] levelData=new int[img.getHeight()][img.getWidth()];

        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color=new Color(img.getRGB(i,j));
                int value= color.getRed();
                if (value>=48){
                    value=0;
                }
                levelData[j][i]=value;
            }
        }

        return levelData;
    }
    public static ArrayList<Crabby> GetCrabs(BufferedImage img){
        ArrayList<Crabby>crabbies=new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color=new Color(img.getRGB(i,j));
                int value= color.getGreen();
                if (value==CRABBY){
                    crabbies.add(new Crabby(i*Game.TILES_SIZE,j*Game.TILES_SIZE));
                }
            }
        }

        return crabbies;
    }
    public static Point getPlayerSpawn(BufferedImage img){
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color=new Color(img.getRGB(i,j));
                int value= color.getGreen();
                if (value==100){
                    return new Point(i*Game.TILES_SIZE,j*Game.TILES_SIZE);
                }
            }
        }
        return new Point(1*Game.TILES_SIZE,1*Game.TILES_SIZE);
    }

    public static boolean isAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData){
        for (int i = 0; i < xEnd-xStart; i++) {
            if (isTileSolid(xStart+i,y,lvlData)){
                return false;
            }
            if (!isTileSolid(xStart+i,y+1,lvlData)){
                return false;
            }
        }

        return true;
    }
    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float hitbox, Rectangle2D.Float hitbox1, int tileY) {

        int firstXTile=(int)(hitbox.x/Game.TILES_SIZE);
        int secondXTile=(int)(hitbox1.x/Game.TILES_SIZE);

        if (firstXTile>secondXTile){
            return isAllTilesWalkable(secondXTile,firstXTile,tileY,lvlData);
        }else {
            return isAllTilesWalkable(firstXTile,secondXTile,tileY,lvlData);
        }
    }
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile=(int)(hitbox.x/Game.TILES_SIZE);
        if (xSpeed>0){
            int tileXPos=currentTile*Game.TILES_SIZE;
            int xOffsett=(int)(Game.TILES_SIZE-hitbox.width);
            return tileXPos+xOffsett-1;
        }else {
            return currentTile*Game.TILES_SIZE;
        }
    }
    private static boolean isSolid(float x,float y, int[][]lvlData){
        int maxWidth= lvlData[0].length*Game.TILES_SIZE;
        if (x<0||x>= maxWidth){
            return true;
        }if (y<0||y>= Game.GAME_HEIGHT){
            return true;
        }
        float xIndex=x/ Game.TILES_SIZE;
        float yIndex=y/ Game.TILES_SIZE;
        return isTileSolid((int)xIndex,(int)yIndex,lvlData);
    }
    public static boolean isTileSolid(int xTile,int yTile,int[][] lvlData){

        int value=lvlData[yTile][xTile];
        if (value>=48||value<0||value!=11){
            return true;
        }else
            return false;
    }

    public static ArrayList<Potion> GetPotions(BufferedImage img){
        ArrayList<Potion> potions =new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color=new Color(img.getRGB(i,j));
                int value= color.getBlue();
                if (value==RED_POTION||value==BLUE_POTION){
                    potions.add(new Potion(i*Game.TILES_SIZE,j*Game.TILES_SIZE,value));
                }
            }
        }

        return potions;
    }

    public static ArrayList<GameContainer> GetGameContainers(BufferedImage img){
        ArrayList<GameContainer> gameContainers =new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color=new Color(img.getRGB(i,j));
                int value= color.getBlue();
                if (value==BOX||value==BARREL){
                    gameContainers.add(new GameContainer(i*Game.TILES_SIZE,j*Game.TILES_SIZE,value));
                }
            }
        }

        return gameContainers;
    }

    public static ArrayList<Spike> GetSpikes(BufferedImage img) {
        ArrayList<Spike> spikes =new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color=new Color(img.getRGB(i,j));
                int value= color.getBlue();
                if (value==SPIKE){
                    spikes.add(new Spike(i*Game.TILES_SIZE,j*Game.TILES_SIZE,SPIKE));
                }
            }
        }

        return spikes;
    }
    public static ArrayList<Cannon> GetCannons(BufferedImage img) {
        ArrayList<Cannon> cannons =new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color=new Color(img.getRGB(i,j));
                int value= color.getBlue();
                if (value==CANNON_LEFT||value==CANNON_RIGHT){
                    cannons.add(new Cannon(i*Game.TILES_SIZE,j*Game.TILES_SIZE,value));
                }
            }
        }

        return cannons;
    }
}
