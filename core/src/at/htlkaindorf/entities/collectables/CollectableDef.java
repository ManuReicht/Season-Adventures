package at.htlkaindorf.entities.collectables;

import com.badlogic.gdx.math.Vector2;

public class CollectableDef {
    public Vector2 position;
    public Class<?> type;

    public CollectableDef(Vector2 position, Class<?> type){
        this.position = position;
        this.type = type;
    }
}
