package Agents.Behaviours;

import Agents.Messages.ExtinguishFireData;
import Agents.Messages.InitialData;
import Agents.Fireman;
import Logic.Fire;
import Util.Ocupation;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;

public class HandleFiremanMessages extends CyclicBehaviour {

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
                    break;
                case(ACLMessage.REQUEST):
                    if(content instanceof ExtinguishFireData){
                        handleExtinguishFireData(f, msg);
                    }
                    break;
                default:
                    System.out.println("Wrong message content.");
                    break;
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    private void handleExtinguishFireData(Fireman f, ACLMessage msg) {
        try {
            ExtinguishFireData cont = (ExtinguishFireData) msg.getContentObject();
            ACLMessage res = new ACLMessage();
            res.setContentObject(new ExtinguishFireData(cont.getFire()));
            res.addReceiver(msg.getSender());

            if(f.getOcupation() == Ocupation.RESTING || f.getOcupation() == Ocupation.RETURNING){
                res.setPerformative(ACLMessage.AGREE);
                this.myAgent.send(res);

                f.setOcupation(Ocupation.MOVING);
                Fire fire = ((ExtinguishFireData) msg.getContentObject()).getFire();
                this.myAgent.addBehaviour(new MovingFireman(this.myAgent, fire.getPositions().get(0)));

            }
            else{
                res.setPerformative(ACLMessage.REFUSE);
                this.myAgent.send(res);
            }
        } catch (UnreadableException | IOException e) {
            e.printStackTrace();
        }
    }

    private void handleInitialData(Fireman f, ACLMessage msg) {
        try {
            InitialData cont = (InitialData) msg.getContentObject();
            f.setActual_position(cont.getPos());
            f.setStd_position(cont.getPos());
            f.setStation(msg.getSender());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }


}
