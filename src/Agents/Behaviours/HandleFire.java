package Agents.Behaviours;

import Agents.Fireman;
import Agents.Messages.FireExtinguished;
import Logic.Fire;
import Util.Ocupation;
import Util.Position;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.List;

public class HandleFire extends TickerBehaviour {

    public HandleFire(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        Fireman f = (Fireman) myAgent;
        Fire fire =  f.getTreating_fire();

        if (fire != null) {
            List<Position> pos = fire.getPositions();
            f.setCap_water(f.getCap_water() - 1);

            if (f.getTreating_fire().getPositions().size() > 1) {
                System.out.println("[FIREMAN " + f.getName() + "] Cleaning " + pos.get(pos.size() - 1).toString());
                f.getTreating_fire().getPositions().remove(pos.size() - 1);
            } else {
                if (f.getTreating_fire().getPositions().size() == 1) {
                    System.out.println("[FIREMAN " + f.getName() + "] Finishing Cleaning " + pos.get(pos.size() - 1).toString());
                    f.getTreating_fire().getPositions().remove(pos.size() - 1);
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd1 = new ServiceDescription();
                    sd1.setType("Station");
                    template.addServices(sd1);

                    DFAgentDescription[] station;

                    try {
                        station = DFService.search(myAgent, template);
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.setContentObject(new FireExtinguished());
                        msg.addReceiver(station[0].getName());
                        myAgent.send(msg);

                    } catch (FIPAException | IOException e) {
                        e.printStackTrace();
                    }

                    f.setOcupation(Ocupation.RETURNING);
                    f.setTreating_fire(null);
                    f.setDestiny(f.getStd_position());
                    //TODO this.myAgent.addBehaviour(new MovingFireman(this.myAgent, f.getStd_position()));
                    this.myAgent.removeBehaviour(this);
                }
            }
        }
    }
}
