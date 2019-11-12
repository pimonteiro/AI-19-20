package Agents.Behaviours;

import Agents.Messages.FireExtinguished;
import Agents.Messages.InitialData;
import Agents.Station;
import Util.Position;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class HandleStationMessages extends CyclicBehaviour {
    @Override
    public void action() {
        Station s = (Station) myAgent;
        ACLMessage msg = myAgent.receive();
        if(msg == null){
            block();
            return;
        }

        try {
            Object content = msg.getContentObject();
            switch (msg.getPerformative()){
                case(ACLMessage.INFORM):
                    if(content instanceof FireExtinguished){
                        handleFireExtinguished(s, msg);
                    } else {
                        System.out.println("Wrong message content.");
                    }
                    break;
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    private void handleFireExtinguished(Station s, ACLMessage msg) throws UnreadableException {
        FireExtinguished cont = (FireExtinguished) msg.getContentObject();
        Position p = cont.getPos();
        //ir ao station buscar o fire ao hashmap
        //tenho que ver se o fogo apagou completamente ou

        //buscar o fireman
        //TODO msg.getSender() //dá-me um AID

        //eliminar do world o fire
        //s.getWorld().getFire().remove(...); //eliminar o fire da station!

        //eliminar do Station o fire do treatment_fire
        //alterar o estado do fireman para a regressar
    }
}
