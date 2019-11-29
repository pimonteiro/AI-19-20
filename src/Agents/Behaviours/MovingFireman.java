package Agents.Behaviours;

import Agents.Fireman;
import Agents.Messages.UpdateData;

import Util.Ocupation;

import Logic.World;
import Util.Pair;
import Util.Position;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.IOException;

import static Util.FindShortestPath.findShortestPath;

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
            //alterar ocupação do FIREMAN .... não é bem só isso
        }
        else{
            try {
                //Position next = decideNewPosition(f, this.destiny);
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

    //TODO verificar combustivel, fogos à beira e se precisa de abastecer
    public Position decideNewPosition(World world, Fireman f, Position fire){
        int distance_fuel;
        int distance_destiny;
        int distance_destiny_fuel;
        Position fuel;
        Position destiny;
        int actual_cap_fuel = f.getCap_fuel();

        // Informações caso o bombeiro opte por ir apagar o fogo
        Pair<Integer, Position> destiny_path = findShortestPath(world, f.getActual_position(), fire, f.getVel());
        distance_destiny = destiny_path.getFirst();
        destiny = destiny_path.getSecond();

        // Informações caso o bombeiro opte por ir abastecer


        return new Position(0,0);
    }
}

