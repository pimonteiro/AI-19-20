package Logic;

import Util.Position;
import Util.Risk;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Fire implements Serializable {
    private List<Position> positions;
    private int zone_id;
    private Risk risk;
    private int duration_time;
    private double base_expansion_rate;

    public Fire(List<Position> positions, double base_expansion_rate, int zone_id) {
        this.zone_id = zone_id;
        this.positions = positions;
        this.risk = Risk.LOW;
        this.duration_time = 0;
        this.base_expansion_rate = base_expansion_rate;
    }

    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object == null || (this.getClass() != object.getClass()))
            return false;

        Fire fire = (Fire) object;

        return  this.positions.equals(fire.getPositions()) &&
                this.risk.equals(fire.getRisk()) &&
                this.duration_time == fire.getDuration_time() &&
                this.base_expansion_rate == fire.getBase_expansion_rate() &&
                this.zone_id == fire.getZone_id();
    }

    public int getZone_id() {
        return zone_id;
    }

    public void setZone_id(int zone_id) {
        this.zone_id = zone_id;
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

    @Override
    public String toString() {
        return "Fire{" +
                "positions=" + positions +
                ", risk=" + risk +
                ", duration_time=" + duration_time +
                ", base_expansion_rate=" + base_expansion_rate +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(positions, risk, duration_time, base_expansion_rate);
    }

    public void increaseTime() {
        this.duration_time++;
    }
}