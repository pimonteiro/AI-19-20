package Agents;

import Logic.Zone;
import Util.FiremanType;
import Util.Ocupation;
import Util.Position;

import java.util.List;

public class AgentData {
    private FiremanType firemanType;
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
