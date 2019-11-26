package Agents.Messages;

import Util.Ocupation;
import Util.Position;

import java.io.Serializable;

public class UpdateData implements Serializable {
    private Position p;
    private int fuel;
    private int water;
    private Ocupation ocupation;

    public UpdateData(Position p, int fuel, int water, Ocupation ocu) {
        this.p = p;
        this.fuel = fuel;
        this.water = water;
        this.ocupation = ocu;
    }

    public Position getP() {
        return p;
    }

    public void setP(Position p) {
        this.p = p;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public Ocupation getOcupation() {
        return ocupation;
    }

    public void setOcupation(Ocupation ocupation) {
        this.ocupation = ocupation;
    }
}
