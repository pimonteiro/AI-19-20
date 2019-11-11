package Agents.Behaviours;

import Util.Position;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

public class SendFirePosition extends OneShotBehaviour {
    private Position firePos;

    public SendFirePosition(Position firePos) {
        this.firePos = firePos;
    }


    @Override
    public void action() {
        DFAgentDescription template1 = new DFAgentDescription();
        ServiceDescription sd1 = new ServiceDescription();
        sd1.setType("Station");
        template1.addServices(sd1);

        DFAgentDescription[] station;
        try{
            station = DFService.search(myAgent,template1);
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContentObject(new SendFirePosition(firePos));
            msg.addReceiver(station[0].getName());
            myAgent.send(msg);
        } catch (FIPAException | IOException e) {
            e.printStackTrace();
        }
    }
}
