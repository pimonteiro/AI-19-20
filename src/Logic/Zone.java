package Logic;

import Util.Position;

public class Zone {
    private int id;
    private Position p1;
    private Position p2;
    private Position p3;
    private Position p4;
    private float ocupation_rate;

    public Zone(int id, Position p1, Position p2, Position p3, Position p4) {
        this.id = id;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.ocupation_rate = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Position getP1() {
        return p1;
    }

    public void setP1(Position p1) {
        this.p1 = p1;
    }

    public Position getP2() {
        return p2;
    }

    public void setP2(Position p2) {
        this.p2 = p2;
    }

    public Position getP3() {
        return p3;
    }

    public void setP3(Position p3) {
        this.p3 = p3;
    }

    public Position getP4() {
        return p4;
    }

    public void setP4(Position p4) {
        this.p4 = p4;
    }

    public float getOcupation_rate() {
        return ocupation_rate;
    }

    public void setOcupation_rate(float ocupation_rate) {
        this.ocupation_rate = ocupation_rate;
    }
}