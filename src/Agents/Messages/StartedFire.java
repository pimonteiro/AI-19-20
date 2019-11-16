package Agents.Messages;


import Util.Position;

import java.io.Serializable;

public class StartedFire implements Serializable {
    private Position p;

    public StartedFire(Position p) {
        this.p = p;
    }

    public Position getP() {
        return p;
    }

    public void setP(Position p) {
        this.p = p;
    }
}
