package Agents;

import Logic.Zone;
import Util.Ocupation;
import Util.Position;
import jade.core.Agent;

import java.util.List;

public abstract class Fireman extends Agent {
    private Position std_position;
    private Position actual_position;
    private Zone zone;
    private List<Position> fuel;
    private List<Position> water;
    private int cap_max_water;
    private int cap_max_fuel;
    private int cap_water;
    private int cap_fuel;
    private int vel;
    private Ocupation ocupation;
}