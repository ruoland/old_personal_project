package olib.api;

import java.util.Random;

public enum Direction {
    FORWARD, BACK, RIGHT, LEFT, FORWARD_RIGHT, FORWARD_LEFT, BACK_RIGHT, BACK_LEFT;


    public boolean isBack(){
        return this == BACK || this == BACK_LEFT || this == BACK_RIGHT;
    }
    public boolean isForward(){
        return this == FORWARD || this == FORWARD_LEFT || this == FORWARD_RIGHT;
    }
    public Direction reverse() {
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

    public Direction simple(){
        if(this == FORWARD_LEFT || this == FORWARD_RIGHT)
            return FORWARD;
        if(this == BACK_LEFT || this == BACK_RIGHT)
            return BACK;
        return this;
    }

    public static Direction random(){
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
