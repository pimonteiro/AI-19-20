package Agents.Behaviours;

import Agents.AgentData;
import Agents.Fireman;
import Agents.Messages.UpdateData;

import Logic.Fire;
import Util.Ocupation;

import Logic.World;
import Util.Pair;
import Util.Position;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                System.out.println("\nPosição atual: " + f.getActual_position().toString());
                Position next = decideNewPosition(f, this.destiny);
                System.out.println("\nA ir para " + next.toString());
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

    public Pair<Integer, Position> maxPairValue(Position actual_position, int velocity, List<Fire> fire, List<Position> fuel,
                                                List<Position> water, List<Position> houses, List<AgentData> fireman,
                                                List<Position> fuels_destiny){
        Pair<Integer, Position> fuel_path_aux = null;
        Pair<Integer, Position> fuel_path = null;
        int distance_fuel_aux = 0;

        for(Position p: fuels_destiny) {
            fuel_path = findShortestPath(actual_position, p, velocity, fire, fuel, water, houses, fireman);
            if(distance_fuel_aux == 0)
                distance_fuel_aux = fuel_path.getFirst();
            if(fuel_path.getFirst() < distance_fuel_aux){
                fuel_path_aux = fuel_path;
                distance_fuel_aux = fuel_path.getFirst();
            }
        }

        return fuel_path_aux;
    }

    public Position decideNewPosition(Fireman f, Position fire){
        int distance_fuel;
        int distance_destiny;
        int distance_destiny_fuel;
        Position destiny_fuel;
        Position destiny;

        int actual_cap_fuel = f.getCap_fuel();
        int velocity = f.getVel();
        Position actual_position = f.getActual_position();
        List<Position> fuel = f.getFuel();

        // Informações bombeiro apagar o fogo
        Pair<Integer, Position> destiny_path = findShortestPath(actual_position, fire, velocity, new ArrayList<>(),
                                                f.getFuel(), f.getWater(), f.getHouses(), new ArrayList<>());
                                            //FIXME bombeiro precisa de saber onde estão os outros fogos e bombeiros
        distance_destiny = destiny_path.getFirst();
        destiny = destiny_path.getSecond();
        System.out.println("[FIREMAN] O fogo está a " + distance_destiny + " posições");

        // Informações bombeiro abastecer
        Pair<Integer, Position> destiny_fuel_path = maxPairValue(actual_position, velocity, new ArrayList<>(),
                f.getFuel(), f.getWater(), f.getHouses(), new ArrayList<>(), fuel);
                                          //FIXME bombeiro precisa de saber onde estão os outros fogos e bombeiros
        distance_fuel = destiny_fuel_path.getFirst();
        destiny_fuel = destiny_fuel_path.getSecond();
        System.out.println("[FIREMAN] A bomba de gasolina está a " + distance_destiny + " posições");

        // Informações bombeiro apagar fogo + abastecer
        Pair<Integer, Position> distance_destiny_fuel_path = maxPairValue(fire, velocity, new ArrayList<>(),
                f.getFuel(), f.getWater(), f.getHouses(), new ArrayList<>(), fuel);
                                        //FIXME bombeiro precisa de saber onde estão os outros fogos e bombeiros
        distance_destiny_fuel = distance_destiny + distance_destiny_fuel_path.getFirst();

        // Se bombeiro tem fuel suficiente para apagar o fogo e abastecer
        if (actual_cap_fuel >= distance_destiny_fuel)
            return destiny;
        // Caso o bombeiro tenha que ir abastecer primeiro
        else
            return destiny_fuel;
    }
}
