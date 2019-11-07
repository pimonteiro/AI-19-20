package Agents;

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

public class FireStarter extends Agent {
    private World world;

    private static final double startFireProbability = 0.2;

    public void setup(){
        super.setup();
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
                    do {
                        randomX = randomGenerator.nextInt(World.dimension) + 1;
                        randomY = randomGenerator.nextInt(World.dimension) + 1;
                    } while (world.isValid(randomX, randomY));

                    startFire(randomX, randomY);
                }
            }
        });
    }

    public void startFire(int x, int y) {
        Position position = new Position(x, y);

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
            world.addFire(newFire);
        }

        //TODO avisar o quartel do que fez (fogo expandiu ou criou novo fogo) (e adicionar nos fogos em espera?)
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

    public FireStarter() {
        this.world = new World();
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}