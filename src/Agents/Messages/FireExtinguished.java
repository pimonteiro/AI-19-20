package Agents.Messages;

import Util.Position;

import java.io.Serializable;

public class FireExtinguished implements Serializable {
    private Position firePos;

    public FireExtinguished(Position firePos) {
        this.firePos = firePos;
    }

    public Position getPos() {
        return firePos;
    }

    public void setPos(Position firePos) {
        this.firePos = firePos;
    }
}
