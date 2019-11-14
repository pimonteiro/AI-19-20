package Agents;

import Logic.Zone;
import Util.FiremanType;
import Util.Ocupation;
import Util.Position;
import jade.core.AID;

public class AgentData {
    private AID aid;
    private FiremanType firemanType;
    private Position std_position;
    private Position actual_position;
    private Zone zone;
    private int cap_water;
    private int cap_fuel;
    private int cap_max_water;
    private int cap_max_fuel;
    private int vel;
    private Ocupation ocupation;

    public AgentData(AID aid, FiremanType firemanType, Position std_position, Position actual_position, Zone zone, int cap_water, int cap_fuel, int vel, Ocupation ocupation) {
        this.aid = aid;
        this.firemanType = firemanType;
        this.std_position = std_position;
        this.actual_position = actual_position;
        this.zone = zone;
        this.cap_water = cap_water;
        this.cap_fuel = cap_fuel;
        this.cap_max_water = cap_water;
        this.cap_max_fuel = cap_fuel;
        this.vel = vel;
        this.ocupation = ocupation;
    }

    public AID getAid() {
        return aid;
    }

    public void setAid(AID aid) {
        this.aid = aid;
    }

    public FiremanType getFiremanType() {
        return firemanType;
    }

    public void setFiremanType(FiremanType firemanType) {
        this.firemanType = firemanType;
    }

    public Position getStd_position() {
        return std_position;
    }

    public void setStd_position(Position std_position) {
        this.std_position = std_position;
    }

    public Position getActual_position() {
        return actual_position;
    }

    public void setActual_position(Position actual_position) {
        this.actual_position = actual_position;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public int getCap_water() {
        return cap_water;
    }

    public void setCap_water(int cap_water) {
        this.cap_water = cap_water;
    }

    public int getCap_fuel() {
        return cap_fuel;
    }

    public void setCap_fuel(int cap_fuel) {
        this.cap_fuel = cap_fuel;
    }

    public int getVel() {
        return vel;
    }

    public void setVel(int vel) {
        this.vel = vel;
    }

    public Ocupation getOcupation() {
        return ocupation;
    }

    public void setOcupation(Ocupation ocupation) {
        this.ocupation = ocupation;
    }

    public int getCap_max_water() {
        return cap_max_water;
    }

    public void setCap_max_water(int cap_max_water) {
        this.cap_max_water = cap_max_water;
    }

    public int getCap_max_fuel() {
        return cap_max_fuel;
    }

    public void setCap_max_fuel(int cap_max_fuel) {
        this.cap_max_fuel = cap_max_fuel;
    }
}
