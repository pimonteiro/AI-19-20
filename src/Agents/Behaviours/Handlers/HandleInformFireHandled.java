package Agents.Behaviours.Handlers;

import Agents.Messages.FireAlreadyHandled;
import Agents.Station;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.util.Set;

public class HandleInformFireHandled extends OneShotBehaviour {
    private Station s;
    private ACLMessage msg;

    public HandleInformFireHandled(Station s, ACLMessage msg) {
        super(s);
        this.s = s;
        this.msg = msg;
    }

    @Override
    public void action() {
        try {
            FireAlreadyHandled fire = (FireAlreadyHandled) msg.getContentObject();
            Set<AID> aids = s.getTreatment_fire().keySet();
            int flag = 0;
            for (AID aid: aids) {
                if(s.getTreatment_fire().get(aid) == fire.getFire()){
                    s.getTreatment_fire().remove(aid);
                    ACLMessage message = new ACLMessage(ACLMessage.CANCEL);
                    message.addReceiver(aid);
                    this.myAgent.send(message);
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                AID aid = s.getQuestioning().get(fire.getFire()).get(s.getQuestioning().get(fire.getFire()).size()-1);
                ACLMessage message = new ACLMessage(ACLMessage.CANCEL);
                message.addReceiver(aid);
                this.myAgent.send(message);
                s.getQuestioning().remove(fire.getFire());
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }
}
