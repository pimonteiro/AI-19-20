package Logic;

import Agents.Fireman;
import Util.Position;

import java.util.List;

public class World {
    private List<Fireman> fireman;
    private List<Fire> fire;
    private List<Position> fuel;
    private List<Position> water;
    private List<Position> houses;
    private List<Zone> zones;

    public static final int dimension = 40;

    public boolean isValid(int x, int y){
        //TODO
        //percorrer os 5 arrays para perceber se a posição está vazia
        return false;
    }

    public List<Fireman> getFireman() {
        return fireman;
    }

    public List<Fire> getFire() {
        return fire;
    }

    public List<Position> getFuel() {
        return fuel;
    }

    public List<Position> getWater() {
        return water;
    }

    public List<Position> getHouses() {
        return houses;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void setFireman(List<Fireman> fireman) {
        this.fireman = fireman;
    }

    public void setFire(List<Fire> fire) {
        this.fire = fire;
    }

    public void setFuel(List<Position> fuel) {
        this.fuel = fuel;
    }

    public void setWater(List<Position> water) {
        this.water = water;
    }

    public void setHouses(List<Position> houses) {
        this.houses = houses;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }
}