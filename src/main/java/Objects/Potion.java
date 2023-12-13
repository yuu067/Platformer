package Objects;

import Main.Game;

public class Potion extends GameObject{
    private float hoverOffset;
    private int maxHoverOffset,hoverDirection=1;
    public Potion(int x, int y, int objectType) {
        super(x, y, objectType);
        doAnimation=true;
        initHitbox(7,14);
        xDrawOffset=(int)(3* Game.SCALE);
        yDrawOffset=(int)(2*Game.SCALE);
        maxHoverOffset=(int)(10*Game.SCALE);
    }
    public void update(){
        updateAniTick();
        updateHover();
    }

    private void updateHover() {
        hoverOffset+=(0.075f*Game.SCALE*hoverDirection);
        if (hoverOffset>=maxHoverOffset||hoverOffset<=0){
            hoverDirection*=-1;
        }
        hitbox.y=y+hoverOffset;
    }
}
