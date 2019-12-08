package Agents.Behaviours.Handlers;

import Agents.Messages.FireOnTheWay;
import Agents.Station;
import Util.Ocupation;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class HandleFireOnTheWay extends OneShotBehaviour {
    private Station s;
    private ACLMessage msg;

    public HandleFireOnTheWay(Station s, ACLMessage msg) {
        super(s);
        this.s = s;
        this.msg = msg;
    }

    @Override
    public void action() {
        try {
            FireOnTheWay fire = (FireOnTheWay) msg.getContentObject();
            s.getTreatment_fire().put(msg.getSender(), fire.getFire());
            s.getWorld().getFireman().get(msg.getSender()).setOcupation(Ocupation.IN_ACTION);
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }
}
