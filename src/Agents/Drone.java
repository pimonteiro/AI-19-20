package Agents;

public class Drone extends Fireman {
    public void setup(){
        super.setup();
        super.setCap_fuel(0);
        super.setCap_max_fuel(0);
        super.setCap_max_water(0);
        super.setCap_water(0);
    }
}