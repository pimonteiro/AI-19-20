package Agents;

import Agents.Behaviours.SendFirePosition;
import Logic.Fire;
import Logic.World;
import Util.Position;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FireStarter extends Agent {
    private World world;

    private static final double startFireProbability = 0.2;

    public void setup(){
        super.setup();
        Object[] args = getArguments();
        this.world = (World) args[0];
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("FireStarter");
        dfd.addServices(sd);

        try {
            DFService.register(this,dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        this.addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                Random randomGenerator = new Random();
                boolean startFire = randomGenerator.nextFloat() < startFireProbability;

                if(startFire) {
                    int randomX = 0;
                    int randomY = 0;
                    Position position = new Position(randomX, randomY);
                    do {
                        randomX = randomGenerator.nextInt(World.dimension);
                        randomY = randomGenerator.nextInt(World.dimension);
                        position.setX(randomX);
                        position.setY(randomY);
                    } while (position.isValid(world.getFire(), world.getFuel(), world.getWater(),
                                              world.getHouses(), new ArrayList<>(world.getFireman().values())));
                    startFire(position);
                }
            }
        });
    }

    public void startFire(Position position) {
        int x = position.getX();
        int y = position.getY();

        //averiguar se já existe um fogo vizinho
        Fire fire = world.getFire().stream()
                .filter(f -> f.getPositions().stream().
                        anyMatch(p -> (p.getX() >= x - 1 && p.getX() <= x + 1 && p.getY() >= y - 1 && p.getY() <= y + 1)))
                .findFirst().orElse(null);

        if (fire != null) {
            //expandir fogo
            world.expandFire(fire, position);
        } else {
            //criar novo fogo
            List<Position> l = new ArrayList<>();
            l.add(position);
            Fire newFire = new Fire(l, calculateBaseExpansionRate(x, y));
            world.getFire().add(newFire);
            this.addBehaviour(new SendFirePosition(newFire));
        }
    }

    //Quanto mais próximo de água, menor probabilidade (retorna a percentagem)
    public double calculateBaseExpansionRate(int x, int y){
        for(int i = 1; i <= 3; i++) {
            int rate = i;
            if (world.getWater().stream().filter(p -> (p.getX() >= x - rate && p.getX() <= x + rate &&
                    p.getY() >= y - rate && p.getY() <= y + rate)).count() > 0) {
                return rate * 0.1;
            }
        }

        return 0.4;
    }

    public void takeDown(){

    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}