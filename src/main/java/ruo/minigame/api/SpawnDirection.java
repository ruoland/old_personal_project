package ruo.minigame.api;

public enum SpawnDirection {
    FORWARD, BACK, RIGHT, LEFT, FORWARD_RIGHT, FORWARD_LEFT, BACK_RIGHT, BACK_LEFT;


    public SpawnDirection reverse() {
        if (this == FORWARD_LEFT)
            return BACK_LEFT;
        if (this == FORWARD_RIGHT)
            return BACK_LEFT;
        if (this == BACK_LEFT)
            return FORWARD_LEFT;
        if (this == BACK_RIGHT)
            return FORWARD_RIGHT;
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
