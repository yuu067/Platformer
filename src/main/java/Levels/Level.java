package Levels;

import Entities.Crabby;
import Main.Game;
import Objects.Cannon;
import Objects.GameContainer;
import Objects.Potion;
import Objects.Spike;
import utils.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.HelpMethods.*;

public class Level {
    private BufferedImage image;
    private int[][] levelData;
    private ArrayList<Crabby>crabbies;
    private ArrayList<Potion>potions;
    private ArrayList<GameContainer>gameContainers;
    private ArrayList<Spike>spikes;
    private ArrayList<Cannon>cannons;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffset;
    private Point playerSpawn;

    public Level (BufferedImage img){
        this.image=img;
        createLvlData();
        calculatePlayerSpawn();
        createEnemies();
        createPotions();
        createContainers();
        createSpikes();
        createCannons();
        calculateLvlOffsets();
    }

    private void createCannons() {
        cannons=HelpMethods.GetCannons(image);
    }

    public ArrayList<Cannon> getCannons() {
        return cannons;
    }

    private void createSpikes() {
        spikes=HelpMethods.GetSpikes(image);
    }

    private void createContainers() {
        gameContainers=HelpMethods.GetGameContainers(image);
    }

    private void createPotions() {
        potions=HelpMethods.GetPotions(image);
    }

    private void calculatePlayerSpawn() {
        playerSpawn = HelpMethods.getPlayerSpawn(image);
    }

    private void calculateLvlOffsets() {
        lvlTilesWide=image.getWidth();
        maxTilesOffset=lvlTilesWide-Game.TILES_IN_WIDTH;
        maxLvlOffset=Game.TILES_SIZE*maxTilesOffset;
    }

    private void createEnemies() {
        crabbies=GetCrabs(image);
    }

    private void createLvlData() {
        levelData=GetLevelData(image);
    }

    public int getSpriteIndex(int x, int y){
        return levelData[y][x];
    }

    public int[][] getLevelData() {
        return levelData;
    }
    public int getMaxLvlOffset(){
        return maxLvlOffset;
    }

    public ArrayList<Crabby> getCrabbies() {
        return crabbies;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<GameContainer> getGameContainers() {
        return gameContainers;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }
}
