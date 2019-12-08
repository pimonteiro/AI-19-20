package Agents.Messages;

import Util.Position;

import java.io.Serializable;

public class FireExtinguished implements Serializable {
    Position position;

    public FireExtinguished(Position p) {
        this.position = p;
    }

    public Position getPosition() {
        return position;
    }
}
