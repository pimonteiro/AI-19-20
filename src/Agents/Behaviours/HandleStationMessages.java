package Agents.Behaviours;

import Agents.Messages.StartedFire;
import Agents.Station;
import Logic.Fire;
import Util.Position;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.List;

public class HandleStationMessages extends CyclicBehaviour {
    @Override
    public void action() {
        Station station = (Station) myAgent;
        ACLMessage msg = myAgent.receive();
        if(msg == null){
            block();
            return;
        }
        try{
            Object content = msg.getContentObject();
            switch (msg.getPerformative()){
                case(ACLMessage.INFORM):
                    if(content instanceof StartedFire) {
                        handleFireStarted(station, msg);
                    }
                    else{
                        System.out.println("Wrong message content.");
                    }
                    break;
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    private void handleFireStarted(Station s, ACLMessage msg){
        try {
            StartedFire cont = (StartedFire) msg.getContentObject();
            s.getWaiting_fire().add(cont.getFire());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }
}
