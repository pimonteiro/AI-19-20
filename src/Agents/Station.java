package Agents;

import Agents.Behaviours.SendInitialInfo;
import Logic.Fire;
import Logic.World;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Station extends Agent {
    private World world;
    private Map<AID, Fire> treatment_fire;
    private List<Fire> waiting_fire;

    public void setup() {
        super.setup();
        Object[] args = getArguments();
        this.world = (World) args[0];
        this.treatment_fire = new HashMap<>();
        this.waiting_fire = new ArrayList<>();
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("Station");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        this.addBehaviour(new SendInitialInfo(this.world));
        this.addBehaviour(new TickerBehaviour(this,1000) {
            @Override
            protected void onTick() {
                //TODO expandir fogo
                //para cada fogo da lista, calcula a probabilidade de expandir e se sim expande
            }
        });

        //TODO executar a procura dos agentes e adicioná-los ao World

    }

    public void takeDown(){

    }

    //TODO calcular risco dentro de um incêndio
    //que varia de acordo com a distância às habitações, e que estará
    //constantemente a ser reavaliada

    public World getWorld() {
        return world;
    }

    public Map<AID,Fire> getTreatment_fire() {
        return treatment_fire;
    }

    public List<Fire> getWaiting_fire() {
        return waiting_fire;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setTreatment_fire(Map<AID,Fire> treatment_fire) {
        this.treatment_fire = treatment_fire;
    }

    public void setWaiting_fire(List<Fire> waiting_fire) {
        this.waiting_fire = waiting_fire;
    }
}