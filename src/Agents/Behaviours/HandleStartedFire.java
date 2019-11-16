package Agents.Behaviours;

import Agents.Message.StartedFire;
import Agents.Station;
import Logic.Fire;
import Util.Position;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

public class HandleStartedFire extends CyclicBehaviour {
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
                        StartedFire cont = (StartedFire) content;
                        List<Position> fireP = new ArrayList<>();
                        fireP.add(cont.getP());
                        Fire fire = new Fire(fireP, 0.2);
                        station.getWaiting_fire().add(fire);
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
}
