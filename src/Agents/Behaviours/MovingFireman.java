package Agents.Behaviours;

import Agents.AgentData;
import Agents.Fireman;
import Agents.Messages.UpdateData;

import Logic.Fire;
import Util.FindShortestPath;
import Util.Ocupation;
import Util.Pair;
import Util.Position;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovingFireman extends TickerBehaviour {
    private Position destiny;
    private FindShortestPath fsp = new FindShortestPath();

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
                Position next = f.getActual_position();
                //A abastecer fuel
                if(f.getFuel().stream().filter(p -> p.equals(f.getActual_position())).count() > 0 && (f.getCap_fuel() != f.getCap_max_fuel())){
                    System.out.println("\n\n\nAbasteci!\n\n\n");
                    f.setCap_fuel(f.getCap_max_fuel());
                } else {
                    //Mover para uma posição do fogo
                    System.out.println("\nPosição atual: " + f.getActual_position().toString());
                    System.out.println("\nTenho combustível= " + f.getCap_fuel());
                    next = decideNewPosition(f, this.destiny);
                    System.out.println("\n------------------A ir para " + next.toString() + "------------------");
                    f.setActual_position(next);
                    f.setCap_fuel(f.getCap_fuel() - 1);
                }

                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                Ocupation ocu = Ocupation.MOVING;

                msg.setContentObject(new UpdateData(next,f.getCap_fuel(),f.getCap_water(),ocu));
                msg.addReceiver(f.getStation());
                this.myAgent.send(msg);

                //TODO COmo serapar os diferentes estados CATARINA

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
            List<Position> fuel_without_destiny = new ArrayList<>();
            fuel_without_destiny.addAll(fuel);
            fuel_without_destiny.remove(p);
            fsp.setPath(new ArrayList<>());
            fuel_path = fsp.findShortestPath(actual_position, p, velocity, fire, fuel_without_destiny, water, houses, fireman);
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
        int distance_fuel_fire;
        int distance_destiny;
        int distance_destiny_fuel;
        Position destiny_fuel_fire;
        Position destiny_fuel;
        Position destiny;

        int actual_cap_fuel = f.getCap_fuel();
        int velocity = f.getVel();
        Position actual_position = f.getActual_position();
        System.out.println("Estou a tentar a partir de " + actual_position);
        List<Position> fuel = f.getFuel();

        // Informações bombeiro apagar o fogo
        fsp.setPath(new ArrayList<>());
        Pair<Integer, Position> destiny_path = fsp.findShortestPath(actual_position, fire, velocity, new ArrayList<>(),
                                                f.getFuel(), f.getWater(), f.getHouses(), new ArrayList<>());
                                            //FIXME bombeiro precisa de saber onde estão os outros fogos
        distance_destiny = destiny_path.getFirst();
        destiny = destiny_path.getSecond();
        System.out.println("\n\n\n[FIREMAN] O fogo está a " + distance_destiny + " posições\n\n\n");

        // Informações bombeiro abastecer na bomba de gasolina mais próxima dela
        Pair<Integer, Position> destiny_fuel_path = maxPairValue(actual_position, velocity, new ArrayList<>(),
                f.getFuel(), f.getWater(), f.getHouses(), new ArrayList<>(), fuel);
                                          //FIXME bombeiro precisa de saber onde estão os outros fogos
        distance_fuel = destiny_fuel_path.getFirst();
        destiny_fuel = destiny_fuel_path.getSecond();
        System.out.println("\n\n\n[FIREMAN] A bomba de gasolina mais próxima está a " + distance_fuel + " posições\n\n\n");

        // Informações bombeiro abastecer na bomba de gasolina mais próxima do fogo
        Pair<Integer, Position> distance_destiny_fuel_path = maxPairValue(fire, velocity, new ArrayList<>(),
                f.getFuel(), f.getWater(), f.getHouses(), new ArrayList<>(), fuel);
                                        //FIXME bombeiro precisa de saber onde estão os outros fogos
        destiny_fuel_fire = distance_destiny_fuel_path.getSecond();
        distance_fuel_fire = distance_destiny + distance_destiny_fuel_path.getFirst();
        System.out.println("\n\n\n[FIREMAN] A bomba de gasolina mais próxima do fogo está a " + distance_fuel + " posições\n\n\n");

        // Informações bombeiro apagar fogo e abastecer no final
        distance_destiny_fuel = distance_destiny + distance_fuel_fire;

        // Se bombeiro tem fuel suficiente para apagar o fogo e abastecer
        System.out.println("Cap. fuel do bombeiro:" + actual_cap_fuel);
        System.out.println("Cap. fogo+fuel do bombeiro: (" + distance_destiny + " + " + distance_destiny_fuel_path.getFirst() + "=)" + distance_destiny_fuel);
        if (actual_cap_fuel >= distance_destiny_fuel) {
            System.out.println("\nVOU APAGAR O FOGOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO " + fuel.toString() + " : " + destiny.toString() + "\n");
            return destiny;
        }
        // Caso o bombeiro tenha que ir abastecer primeiro
        else if (actual_cap_fuel >= distance_fuel_fire){
            System.out.println("\nVOU METER GASOLINAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA (maix próx fogo): " + destiny_fuel.toString() + "\n");
            return destiny_fuel_fire;
        } else {
            System.out.println("\nVOU METER GASOLINAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA (mais próx): " + destiny_fuel.toString() + "\n");
            return destiny_fuel;
        }
    }
}
