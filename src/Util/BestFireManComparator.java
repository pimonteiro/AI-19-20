package Util;

import Agents.AgentData;

import java.util.Comparator;

public class BestFireManComparator implements Comparator<AgentData> {
    private int fire_x;
    private int fire_y;

    public BestFireManComparator(int fire_x, int fire_y) {
        this.fire_x = fire_x;
        this.fire_y = fire_y;
    }

    @Override
    public int compare(AgentData a1, AgentData a2) {
        Position p_a1 = a1.getActual_position();
        Position p_a2 = a2.getActual_position();
        int dist_a1 = (int) Math.sqrt(Math.sqrt(p_a1.getX() - fire_x) + Math.sqrt(p_a1.getY() - fire_y));
        int dist_a2 = (int) Math.sqrt(Math.sqrt(p_a2.getX() - fire_x) + Math.sqrt(p_a2.getY() - fire_y));
        int val1 = (dist_a1 / a1.getVel()) - (dist_a2 / a2.getVel());

        return val1;
    }
}
