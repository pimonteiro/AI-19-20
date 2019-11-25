package Agents.Messages;

import Logic.Fire;

public class FireAlreadyHandled {
    Fire fire;

    public FireAlreadyHandled(Fire f){
        this.fire = f;
    }

    public Fire getFire() {
        return fire;
    }
}
