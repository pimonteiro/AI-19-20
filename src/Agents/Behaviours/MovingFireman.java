package Agents.Behaviours;

import Agents.Fireman;
import Agents.Messages.UpdateData;
import Util.Ocupation;
import Util.Position;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

public class MovingFireman extends TickerBehaviour {
    private Position destiny;

    public MovingFireman(Agent myagent, Position position) {
        super(myagent,1000);
        this.destiny = position;
    }

    @Override
    protected void onTick() {
        Fireman f = (Fireman) this.myAgent;
        if(f.getActual_position().equals(this.destiny)) {
            this.myAgent.removeBehaviour(this);
        }
        else{
            try {   //TODO verificar combustivel, fogos à beira e se precisa de abastecer
                Position next = this.destiny; //TODO decideNewPosition()
                f.setActual_position(next);
                f.setCap_fuel(f.getCap_fuel()-1);

                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                //TODO COmo serapar os diferentes estados CATARINA
                Ocupation ocu = Ocupation.MOVING;
                msg.setContentObject(new UpdateData(next,f.getCap_fuel(),f.getCap_water(),ocu));
                msg.addReceiver(f.getStation());
                this.myAgent.send(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
