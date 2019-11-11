package Agents.Behaviours;

import Agents.Communication.Message.InitialData;
import Agents.Fireman;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class HandleMessages extends CyclicBehaviour {

    @Override
    public void action() {
        Fireman f = (Fireman) myAgent;
        ACLMessage msg = myAgent.receive();
        if(msg == null){
            block();
            return;
        }
        try{
            Object content = msg.getContentObject();
            switch (msg.getPerformative()){
                case(ACLMessage.INFORM):
                    if(content instanceof InitialData) {
                        handleInitialData(f, msg);
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

    private void handleInitialData(Fireman f, ACLMessage msg) throws UnreadableException {
        InitialData cont = (InitialData) msg.getContentObject();
        f.setActual_position(cont.getPos());
        f.setStd_position(cont.getPos());
    }
}
