package Levels;

import GameStates.GameState;
import Main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Main.Game.TILES_SIZE;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level>levels;
    private int lvlIndex=0;
    public LevelManager(Game game){
        this.game=game;
//        levelSprite= LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        importOutsideSprites();
        levels=new ArrayList<>();
        buildAllLevels();
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels=LoadSave.getAllLevels();
        for (BufferedImage img:allLevels){
            levels.add(new Level(img));
        }
    }

    private void importOutsideSprites() {
        BufferedImage image=LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite=new BufferedImage[48];
        for (int j=0;j<4;j++){
            for (int i = 0; i < 12; i++) {
                int index=j*12+i;
                levelSprite[index]=image.getSubimage(32*i,j*32,32,32);
            }
        }
    }

    public void draw(Graphics g, int xLevelOffset){

        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
                int index=levels.get(lvlIndex).getSpriteIndex(i,j);
                g.drawImage(levelSprite[index],TILES_SIZE*i-xLevelOffset,TILES_SIZE*j,TILES_SIZE,TILES_SIZE,null);

            }
        }
//        32*2,32*2,
    }
    public void update(){

    }

    public int getAmounOfLevels(){
        return levels.size();
    }

    public Level getCurrentLevel() {
        return levels.get(lvlIndex);
    }

    public void loadNextLevel() {
        lvlIndex++;
        if (lvlIndex>=getAmounOfLevels()){
            lvlIndex=0;
            System.out.println("No more levels! GameCompleted");
            GameState.state=GameState.MENU;
        }
        Level newLevel=levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLevelData(newLevel.getLevelData());
        game.getPlaying().setMaxLvlOffset(newLevel.getMaxLvlOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }
}
