package Agents;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class FireTruck extends Fireman {
    public static int MAX_WATER;
    public static int MAX_FUEL;
    public static int VEL;

    public void setup(){
        super.setup();
        MAX_FUEL = super.getDimension()/2;
        MAX_WATER = super.getDimension()/2;
        VEL = super.getDimension()/6;
        super.setCap_fuel(MAX_FUEL);
        super.setCap_max_fuel(MAX_FUEL);
        super.setCap_max_water(MAX_WATER);
        super.setCap_water(MAX_WATER);
        super.setVel(VEL);

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("FireTruck");
        dfd.addServices(sd);

        try {
            DFService.register(this,dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }
}
