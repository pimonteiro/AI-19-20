package Agents.Messages;

import Logic.Fire;

import java.io.Serializable;

public class FireAlreadyHandled implements Serializable {
    Fire fire;

    public FireAlreadyHandled(Fire f){
        this.fire = f;
    }

    public Fire getFire() {
        return fire;
    }
}
