package ruo.minigame.api;

public enum SpawnDirection {
    FORWARD, BACK, RIGHT, LEFT, FORWARD_RIGHT, FORWARD_LEFT, BACK_RIGHT, BACK_LEFT;


    public SpawnDirection reverse()
    {
        if(this == FORWARD)
            return BACK;
        if(this == BACK)
            return FORWARD;
        if(this == RIGHT)
            return LEFT;
        if(this == LEFT)
            return RIGHT;
        return this;
    }
}
