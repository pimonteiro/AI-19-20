package Agents.Behaviours;

import Agents.Messages.CancelFire;
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
                case(ACLMessage.CANCEL):
                    if(content instanceof CancelFire)
                        handleCancelFire(f, msg);
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
            res.setContentObject(cont);
            res.addReceiver(msg.getSender());

            if(f.getOcupation() == Ocupation.RESTING || f.getOcupation() == Ocupation.RETURNING){
                res.setPerformative(ACLMessage.AGREE);
                System.out.println("[FIREMAN " + f.getName() + "] Accepted Fire " + cont.getFire().toString());
                this.myAgent.send(res);

                f.setOcupation(Ocupation.MOVING);
                Fire fire = ((ExtinguishFireData) msg.getContentObject()).getFire();
                this.myAgent.addBehaviour(new MovingFireman(this.myAgent, fire.getPositions().get(0)));

            }
            else{
                res.setPerformative(ACLMessage.REFUSE);
                System.out.println("[FIREMAN " + f.getName() + "] Refused Fire " + cont.getFire().toString());
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
            System.out.println("[FIREMAN " + f.getName() + "] Posição: " + f.getActual_position().toString());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    private void handleCancelFire(Fireman f, ACLMessage msg){
        try {
            Fire fire = ((CancelFire) msg.getContentObject()).getFire();
            if(f.getTreating_fire().equals(fire)) {
                f.setTreating_fire(null);
                f.addBehaviour(new MovingFireman(myAgent, f.getStd_position()));
                f.setOcupation(Ocupation.RETURNING);
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

    }

}
