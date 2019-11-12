package Agents;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class Aircraft extends Fireman {
    public final static int MAX_WATER = 15;
    public final static int MAX_FUEL = 20;
    public final static int VEL = 2;

    public void setup(){
        super.setup();
        super.setCap_fuel(MAX_FUEL);
        super.setCap_max_fuel(MAX_FUEL);
        super.setCap_max_water(MAX_WATER);
        super.setCap_water(MAX_WATER);
        super.setVel(VEL);

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("Aircraft");
        dfd.addServices(sd);

        try {
            DFService.register(this,dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }
}