package Agents;

import Agents.Behaviours.HandleFiremanMessages;
import Logic.World;
import Logic.Zone;
import Util.Ocupation;
import Util.Position;
import jade.core.AID;
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
    private AID station;

    public void setup(){
        super.setup();
        Object[] args = getArguments();
        World world = (World) args[0];
        this.fuel = world.getFuel();
        this.water = world.getWater();
        this.std_position = new Position(0,0);
        this.actual_position = new Position(0,0);

        this.addBehaviour(new HandleFiremanMessages());
    }

    public void takeDown(){
        //System.out.println(fuel.toString());
    }

    public Position getStd_position() {
        return std_position;
    }

    public Position getActual_position() {
        return actual_position;
    }

    public Zone getZone() {
        return zone;
    }

    public List<Position> getFuel() {
        return fuel;
    }

    public List<Position> getWater() {
        return water;
    }

    public int getCap_max_water() {
        return cap_max_water;
    }

    public int getCap_max_fuel() {
        return cap_max_fuel;
    }

    public int getCap_water() {
        return cap_water;
    }

    public int getCap_fuel() {
        return cap_fuel;
    }

    public int getVel() {
        return vel;
    }

    public Ocupation getOcupation() {
        return ocupation;
    }

    public void setStd_position(Position std_position) {
        this.std_position = std_position;
    }

    public void setActual_position(Position actual_position) {
        this.actual_position = actual_position;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public void setFuel(List<Position> fuel) {
        this.fuel = fuel;
    }

    public void setWater(List<Position> water) {
        this.water = water;
    }

    public void setCap_max_water(int cap_max_water) {
        this.cap_max_water = cap_max_water;
    }

    public void setCap_max_fuel(int cap_max_fuel) {
        this.cap_max_fuel = cap_max_fuel;
    }

    public void setCap_water(int cap_water) {
        this.cap_water = cap_water;
    }

    public void setCap_fuel(int cap_fuel) {
        this.cap_fuel = cap_fuel;
    }

    public void setVel(int vel) {
        this.vel = vel;
    }

    public void setOcupation(Ocupation ocupation) {
        this.ocupation = ocupation;
    }

    public AID getStation() {
        return station;
    }

    public void setStation(AID station) {
        this.station = station;
    }
}