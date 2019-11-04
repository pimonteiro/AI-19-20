package Agents;

import Logic.Fire;
import Logic.World;
import jade.core.Agent;

import java.util.List;
import java.util.Map;

public class Station extends Agent {
    private World world;
    private Map<Fire, Fireman> treatment_fire;
    private List<Fire> waiting_fire;
}