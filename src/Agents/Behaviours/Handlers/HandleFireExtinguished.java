package Agents.Behaviours.Handlers;

import Agents.AgentData;
import Agents.Messages.UpdateFire;
import Agents.Station;
import Logic.Fire;
import Logic.Metric;
import Util.Ocupation;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

public class HandleFireExtinguished extends OneShotBehaviour {
    private Station s;
    private ACLMessage msg;

    public HandleFireExtinguished(Station s, ACLMessage msg) {
        super(s);
        this.s = s;
        this.msg = msg;
    }

    @Override
    public void action() {
        AID aid = msg.getSender();
        Fire f = s.getTreatment_fire().get(aid);
        if(f != null){
            try {
                Metric c = s.getMetrics();
                c.addNewFireResolved(f);
                AgentData agentData = s.getWorld().getFireman().get(aid);

                UpdateFire co = new UpdateFire(f,false);
                ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                message.setContentObject(co);
                for(AID ag : s.getWorld().getFireman().keySet()){
                    message.addReceiver(ag);
                }
                this.myAgent.send(message);

                //eliminar o fire do World
                s.getWorld().getFire().remove(f);
                //eliminar o par agente&fire do treatment_fire
                s.getTreatment_fire().remove(aid);
                //alterar o estado do fireman para "a regressar"
                agentData.setOcupation(Ocupation.RETURNING);
                //eliminar treatment fire do fireman
                agentData.setTreating_fire(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
