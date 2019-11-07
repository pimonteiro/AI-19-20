package Logic;

import Agents.Fireman;
import Util.Position;

import java.util.ArrayList;
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
        return (fuel.stream().filter(p -> p.getX() == x && p.getY() == y).count() > 0) &&
               (water.stream().filter(p -> p.getX() == x && p.getY() == y).count() > 0) &&
               (houses.stream().filter(p -> p.getX() == x && p.getY() == y).count() > 0) &&
               (fire.stream().filter(f -> f.getPositions().stream().filter(p -> p.getX() == x &&
                       p.getY() == y).count() > 0).count() > 0) &&
               (fireman.stream().filter(f -> f.getActual_position().getX() == x &&
                       f.getActual_position().getY() == y).count() > 0);
    }

    public void addFire(Fire newFire){
        this.fire.add(newFire);
    }

    public void expandFire(Fire activeFire, Position newPosition){
        //encontrar fogo no array do fogos
        /*
        Fire element = this.fire.stream()
                           .filter(f -> f.equals(activeFire))
                           .findAny()
                           .orElse(null);
*/


        activeFire.addFirePosition(newPosition);
    }

    public World() {
        this.fireman = new ArrayList<>();
        this.fire = new ArrayList<>();
        this.fuel = new ArrayList<>();
        this.water = new ArrayList<>();
        this.houses = new ArrayList<>();
        this.zones = new ArrayList<>();
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