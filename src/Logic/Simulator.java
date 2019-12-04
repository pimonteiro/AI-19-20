package Logic;


import Util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulator {
    public static void startSimulation_v1(World world, int n_fuel, int n_houses, int n_water, int n_zones){
        Random r = new Random();
        int zone_side = (int) Math.sqrt(n_zones);
        List<Position> water = new ArrayList<>();
        List<Position> fuel = new ArrayList<>();
        List<Position> houses = new ArrayList<>();
        List<Zone> zones = new ArrayList<>();

        int n = World.dimension / zone_side;
        int index = 0;
        for(int i = 0; i < zone_side; i++){
            int x = i * n;
            for(int h = 0; h < zone_side; h++){
                int y = h * n;
                Position p1 = new Position(x,y);
                Position p2 = new Position(x + n,y + n);
                Position p3 = new Position(x + n,y);
                Position p4 = new Position(x,y + n);
                Zone z = new Zone(index++,p1,p2,p3,p4);
                zones.add(z);

                int j = 0;
                while(j < n_water / n){
                    int tx = r.nextInt(((x+n) - x)) + x;
                    int ty = r.nextInt(((y+n) - y)) + y;
                    Position p = new Position(tx,ty);
                    if(!water.contains(p)){
                        water.add(p);
                        j++;
                    }
                }
                j = 0;
                while(j < n_fuel / n){
                    int tx = r.nextInt(((x+n) - x)) + x;
                    int ty = r.nextInt(((y+n) - y)) + y;
                    Position p = new Position(tx,ty);
                    if(!(fuel.contains(p) || water.contains(p))){
                        fuel.add(p);
                        j++;
                    }
                }
                j = 0;
                while(j < n_houses / n){
                    int tx = r.nextInt(((x+n) - x)) + x;
                    int ty = r.nextInt(((y+n) - y)) + y;
                    Position p = new Position(tx,ty);
                    if(!(fuel.contains(p) || water.contains(p) || houses.contains(p))){
                        houses.add(p);
                        j++;
                    }
                }
            }
        }
        world.setZones(zones);
        world.setFuel(fuel);
        world.setHouses(houses);
        world.setWater(water);
    }

}
