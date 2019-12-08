package Agents.Behaviours.Handlers;

import Agents.Messages.StartedFire;
import Agents.Messages.UpdateFire;
import Agents.Station;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.io.IOException;

public class HandleFireStarted extends OneShotBehaviour {
    private Station s;
    private ACLMessage msg;

    public HandleFireStarted(Station s, ACLMessage msg) {
        super(s);
        this.s = s;
        this.msg = msg;
    }

    @Override
    public void action() {
        try {
            StartedFire cont = (StartedFire) msg.getContentObject();
            s.getWaiting_fire().add(cont.getFire());
            System.out.println("[STATION] Novo fogo: " + cont.getFire().toString());

            UpdateFire co = new UpdateFire(cont.getFire(),true);
            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            message.setContentObject(co);
            for(AID ag : s.getWorld().getFireman().keySet()){
                message.addReceiver(ag);
            }
            this.myAgent.send(message);
        } catch (UnreadableException | IOException e) {
            e.printStackTrace();
        }
    }
}
