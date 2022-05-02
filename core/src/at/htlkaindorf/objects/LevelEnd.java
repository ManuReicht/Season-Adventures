package at.htlkaindorf.objects;

import at.htlkaindorf.Game;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.maps.MapObject;

public class LevelEnd extends InteractiveObject {
    public LevelEnd(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Game.getInstance().getLEVEL_END_BIT());
    }
}
