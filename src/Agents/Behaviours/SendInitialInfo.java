package Agents.Behaviours;

import Agents.AgentData;
import Agents.Aircraft;
import Agents.Messages.InitialData;
import Agents.Drone;
import Agents.FireTruck;
import Logic.World;
import Logic.Zone;
import Util.FiremanType;
import Util.Ocupation;
import Util.Position;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class SendInitialInfo extends OneShotBehaviour {
    private World world;

    public SendInitialInfo(World w){
        world = w;
    }

    @Override
    public void action() {
        DFAgentDescription template1 = new DFAgentDescription();
        ServiceDescription sd1 = new ServiceDescription();
        sd1.setType("FireTruck");
        template1.addServices(sd1);

        DFAgentDescription template2 = new DFAgentDescription();
        ServiceDescription sd2 = new ServiceDescription();
        sd2.setType("Drone");
        template2.addServices(sd2);

        DFAgentDescription template3 = new DFAgentDescription();
        ServiceDescription sd3 = new ServiceDescription();
        sd3.setType("Aircraft");
        template3.addServices(sd3);

        DFAgentDescription[] trucks;
        DFAgentDescription[] drones;
        DFAgentDescription[] aircrs;
        try{
            trucks = DFService.search(myAgent,template1);
            drones = DFService.search(myAgent,template2);
            aircrs = DFService.search(myAgent,template3);

            int t_l = trucks.length;
            int z_l = world.getZones().size();
            int per_zone_truck = world.getZones().size() / trucks.length;
            int per_zone_drone = world.getZones().size() / drones.length;
            int per_zone_aircr = world.getZones().size() / aircrs.length;

            Random r = new Random();
            int tr = 0;
            int dr = 0;
            int air = 0;
            HashMap<AID, AgentData> firemans = new HashMap<>();
            for(Zone z : world.getZones()) {
                int i = 0;
                while(i < per_zone_truck){ //TODO what if it is an odd number of agents per zone?
                    int x_max = z.getP2().getX();
                    int x_min = z.getP1().getX();
                    int y_max = z.getP2().getY();
                    int y_min = z.getP1().getY();
                    int tx = r.nextInt(x_max - x_min) + x_min;
                    int ty = r.nextInt(y_max - y_min) + y_min;

                    //Posição válida para um bombeiro
                    if(world.isValid(tx,ty)){
                        AgentData f = new AgentData(FiremanType.FIRETRUCK,new Position(tx,ty),new Position(tx,ty),
                                z,FireTruck.MAX_WATER, FireTruck.MAX_FUEL,FireTruck.VEL,Ocupation.RESTING);
                        firemans.put(trucks[tr].getName(),f);
                        //Send Message with data
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.setContentObject(new InitialData(new Position(tx,ty)));
                        msg.addReceiver(trucks[tr++].getName());
                        System.out.println("Enviando posicao para: " + trucks[tr-1].getName());
                        myAgent.send(msg);
                        i++;
                    }
                }
                i = 0;
                while(i < per_zone_drone){ //TODO what if it is an odd number of agents per zone?
                    int x_max = z.getP2().getX();
                    int x_min = z.getP1().getX();
                    int y_max = z.getP2().getY();
                    int y_min = z.getP1().getY();
                    int tx = r.nextInt(x_max - x_min) + x_min;
                    int ty = r.nextInt(y_max - y_min) + y_min;

                    //Posição válida para um bombeiro
                    if(world.isValid(tx,ty)){
                        AgentData f = new AgentData(FiremanType.DRONE,new Position(tx,ty),new Position(tx,ty),
                                z,Drone.MAX_WATER, Drone.MAX_FUEL,Drone.VEL,Ocupation.RESTING);
                        firemans.put(drones[dr].getName(),f);
                        //Send Message with data
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.setContentObject(new InitialData(new Position(tx,ty)));
                        msg.addReceiver(drones[dr++].getName());
                        System.out.println("Enviando posicao para: " + drones[tr-1].getName());
                        myAgent.send(msg);
                        i++;
                    }
                }
                i = 0;
                while(i < per_zone_aircr){ //TODO what if it is an odd number of agents per zone?
                    int x_max = z.getP2().getX();
                    int x_min = z.getP1().getX();
                    int y_max = z.getP2().getY();
                    int y_min = z.getP1().getY();
                    int tx = r.nextInt(x_max - x_min) + x_min;
                    int ty = r.nextInt(y_max - y_min) + y_min;

                    //Posição válida para um bombeiro
                    if(world.isValid(tx,ty)){
                        AgentData f = new AgentData(FiremanType.AIRCRAFT,new Position(tx,ty),new Position(tx,ty),
                                z,Aircraft.MAX_WATER, Aircraft.MAX_FUEL,Aircraft.VEL,Ocupation.RESTING);
                        firemans.put(trucks[air].getName(),f);
                        //Send Message with data
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.setContentObject(new InitialData(new Position(tx,ty)));
                        msg.addReceiver(aircrs[air++].getName());
                        System.out.println("Enviando posicao para: " + aircrs[tr-1].getName());
                        myAgent.send(msg);
                        i++;
                    }
                }
            }
            this.world.setFireman(firemans);
        } catch (FIPAException | IOException e) {
            e.printStackTrace();
        }
    }

}