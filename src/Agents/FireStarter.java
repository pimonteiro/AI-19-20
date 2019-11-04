package Agents;

import Logic.World;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

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

                    //TODO mete fogo
                }
            }
        });
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