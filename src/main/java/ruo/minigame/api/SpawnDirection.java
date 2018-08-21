package ruo.minigame.api;

public enum SpawnDirection {
    FORWARD, BACK, RIGHT, LEFT, FORWARD_RIGHT, FORWARD_LEFT, BACK_RIGHT, BACK_LEFT;


    public SpawnDirection reverse() {
        if (this == FORWARD)
            return BACK;
        if (this == BACK)
            return FORWARD;
        if (this == RIGHT)
            return LEFT;
        if (this == LEFT)
            return RIGHT;
        return this;
    }

    public SpawnDirection simple(){
        if(this == FORWARD_LEFT || this == FORWARD_RIGHT)
            return FORWARD;
        if(this == BACK_LEFT || this == BACK_RIGHT)
            return BACK;
        return this;
    }
}
