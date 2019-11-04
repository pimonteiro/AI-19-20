package Logic;

import Util.Position;
import Util.Risk;

import java.util.List;

public class Fire {
    private List<Position> positions;
    private Risk risk;
    private int duration_time;
    private double base_expansion_rate;

    public Fire(List<Position> positions, Risk risk, int duration_time, double base_expansion_rate) {
        this.positions = positions;
        this.risk = risk;
        this.duration_time = duration_time;
        this.base_expansion_rate = base_expansion_rate;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public Risk getRisk() {
        return risk;
    }

    public int getDuration_time() {
        return duration_time;
    }

    public double getBase_expansion_rate() {
        return base_expansion_rate;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public void setRisk(Risk risk) {
        this.risk = risk;
    }

    public void setDuration_time(int duration_time) {
        this.duration_time = duration_time;
    }

    public void setBase_expansion_rate(double base_expansion_rate) {
        this.base_expansion_rate = base_expansion_rate;
    }
}