package Agents;

import Agents.Behaviours.*;
import Logic.Fire;
import Logic.Metric;
import Logic.World;

import Logic.Zone;
import Util.Position;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.*;
import java.util.stream.Collectors;

public class Station extends Agent {
    private World world;
    private Map<AID, Fire> treatment_fire;
    private List<Fire> waiting_fire;
    private Map<Fire,List<AID>> questioning;
    private GUI.Map map_gui;
    private Metric metrics;

    public void setup() {
        super.setup();
        Object[] args = getArguments();
        this.world = (World) args[0];
        this.treatment_fire = new HashMap<>();
        this.waiting_fire = new ArrayList<>();
        this.metrics = new Metric();
        questioning = new HashMap<>();
        this.map_gui = new GUI.Map(world);

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
        this.addBehaviour(new HandleStationMessages());
        this.addBehaviour(new CheckWaitingFires());
        this.addBehaviour(new MetricController(this, 5000));
        this.addBehaviour(new CalculateRisk(this));
        this.addBehaviour(new TickerBehaviour(this,1000) {
            @Override
            protected void onTick() {
                //TODO expandir fogo
                //para cada fogo da lista, calcula a probabilidade de expandir e se sim expande
                treatment_fire.values().forEach(Fire::increaseTime);
                treatment_fire.values().forEach(Fire::increaseTimeBeingResolved);
                waiting_fire.forEach(Fire::increaseTime);
                questioning.keySet().forEach(Fire::increaseTime);
                System.out.println("-------Fires being treated-------");
                treatment_fire.keySet().forEach(a -> System.out.println("Fire " + treatment_fire.get(a).getPositions()
                .get(0).toString() + " by " + a.getName()));
                System.out.println("-------Fires waiting to be treated-------");
                waiting_fire.forEach(f -> System.out.println(f.toString()));
                System.out.println("-------Fires being questioned-------");
                questioning.keySet().forEach(f -> System.out.println(f.toString()));
                map_gui.update(world);
                map_gui.updateGUI();
            }
        });
    }

    public void takeDown(){

    }

    public World getWorld() {
        return world;
    }

    public Map<AID,Fire> getTreatment_fire() {
        return treatment_fire;
    }

    public List<Fire> getWaiting_fire() {
        return waiting_fire;
    }

    public Map<Fire, List<AID>> getQuestioning() {
        return questioning;
    }

    public Metric getMetrics() {
        return metrics;
    }

    // TODO o que acontece quando um fogo expande e alguem está a caminho/a tratar dele?
    public AID findBestFireman(Fire f, List<AID> unavailable){
        List<Position> p = f.getPositions();
        int size_of_fire = p.size();
        Zone z = this.world.findZoneOfFire(f);
        int fire_x = p.stream().map(Position::getX).reduce(0, Integer::sum) / size_of_fire;
        int fire_y = p.stream().map(Position::getY).reduce(0, Integer::sum) / size_of_fire;


        List<AgentData> firemans = this.world.getFireman().values().stream().filter(b -> b.getZone().getId() == z.getId()).collect(Collectors.toList());
        for(AID d : unavailable){
            firemans.removeIf(a -> a.getAid().equals(d));
        }
        firemans.sort((a1, a2) -> {
            Position p_a1 = a1.getActual_position();
            Position p_a2 = a2.getActual_position();
            int dist_a1 = (int) Math.sqrt(Math.sqrt(p_a1.getX() - fire_x) + Math.sqrt(p_a1.getY() - fire_y));
            int dist_a2 = (int) Math.sqrt(Math.sqrt(p_a2.getX() - fire_x) + Math.sqrt(p_a2.getY() - fire_y));
            int val1 = (dist_a1 / a1.getVel()) - (dist_a2 / a2.getVel());

            int w_a1 = a1.getCap_max_water();
            int w_a2 = a2.getCap_max_water();
            int r_a1 = w_a1 - size_of_fire;
            int r_a2 = w_a2 - size_of_fire;
            int val2;
            if (r_a1 > 0) {
                if (r_a2 > 0)
                    val2 = r_a1 - r_a2;
                else
                    val2 = r_a1;
            } else {
                val2 = Math.max(r_a2, 0);
            }
            return (int)(val1 * 0.9 + val2 * 0.1);
        });

        if(firemans.size() == 0){
            ArrayList<Zone> t = new ArrayList<>(world.getZones());
            t.sort((c1,c2) -> (int)(c1.getOcupation_rate() - c2.getOcupation_rate()));
            Zone n = t.get(0);
            List<AgentData> remaining = this.world.getFireman().values().stream().filter(b -> b.getZone().getId() == n.getId()).collect(Collectors.toList());
            remaining.sort((a1, a2) -> {
                Position p_a1 = a1.getActual_position();
                Position p_a2 = a2.getActual_position();
                int dist_a1 = (int) Math.sqrt(Math.sqrt(p_a1.getX() - fire_x) + Math.sqrt(p_a1.getY() - fire_y));
                int dist_a2 = (int) Math.sqrt(Math.sqrt(p_a2.getX() - fire_x) + Math.sqrt(p_a2.getY() - fire_y));

                return (dist_a1 / a1.getVel()) - (dist_a2 / a2.getVel());
            });
            return remaining.get(0).getAid();
        }
        else
            return firemans.get(0).getAid();
    }
}