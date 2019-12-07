package Agents.Behaviours.Handlers;

import Agents.Fireman;
import Agents.Messages.UpdateFire;
import Logic.Fire;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;

public class HandleUpdateFire extends OneShotBehaviour {
    private Fireman f;
    private ACLMessage msg;

    public HandleUpdateFire(Fireman f, ACLMessage msg) {
        super(f);
        this.f = f;
        this.msg = msg;
    }

    @Override
    public void action() {
        try {
            ArrayList<Fire> pos = f.getFires();
            UpdateFire cont = (UpdateFire) msg.getContentObject();
            if(cont.isType()){
                pos.add(cont.getFire());
            }
            else{
                pos.remove(cont.getFire());
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }
}
