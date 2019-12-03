package Logic;

import Agents.AgentData;
import Util.Position;
import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class World {
    private HashMap<AID,AgentData> fireman;
    private List<Fire> fire;                //TODO REDUNDANTE?
    private List<Position> fuel;
    private List<Position> water;
    private List<Position> houses;
    private List<Zone> zones;

    public static int dimension;

    public void expandFire(Fire activeFire, Position newPosition){
        activeFire.getPositions().add(newPosition);
    }

    public World(int dimension) {
        this.fireman = new HashMap<>();
        this.fire = new ArrayList<>();
        this.fuel = new ArrayList<>();
        this.water = new ArrayList<>();
        this.houses = new ArrayList<>();
        this.zones = new ArrayList<>();
        World.dimension = dimension;
    }

    public HashMap<AID,AgentData> getFireman() {
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

    public void setFireman(HashMap<AID,AgentData> fireman) {
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

    public Zone findZoneOfFire(Fire f) {
        HashMap<Integer,Integer> total = new HashMap<>();
        this.zones.stream().forEach(z -> total.put(z.getId(), z.contains(f.getPositions())));
        int z = 0;
        for(Integer i : total.keySet()){
            if(total.get(i) > z)
                z = i;
        }
        return zones.get(z);
    }

    public int getZoneOfPosition(Position position) {
        ArrayList<Position> positions = new ArrayList<>();
        positions.add(position);
        for(Zone z : zones){
            if(z.contains(positions) > 0){
                return z.getId();
            }
        }
        return -1;
    }
}