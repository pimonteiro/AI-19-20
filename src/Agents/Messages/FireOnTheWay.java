package Agents.Messages;

import Logic.Fire;

import java.io.Serializable;

public class FireOnTheWay implements Serializable {
    Fire fire;

    public FireOnTheWay(Fire f){
        this.fire = f;
    }

    public Fire getFire() {
        return fire;
    }
}
