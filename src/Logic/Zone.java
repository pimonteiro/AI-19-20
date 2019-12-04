package Logic;

import Util.Position;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

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

    public Integer contains(List<Position> positions) {
        int total = 0;
        for (Position p : positions) {
            if (p.getX() > p1.getX() && p.getX() < p2.getX()) {
                if (p.getY() > p1.getY() && p.getY() < p2.getY()) {
                    total++;
                }
            }
        }
        return total;
    }

    public ArrayList<Position> getAllPositions() {
        ArrayList<Position> ret = new ArrayList<>();
        for(int i = this.p1.getX(); i <= this.p2.getX(); i++){
            for(int j = this.p1.getY(); j <= this.p2.getY(); j++){
                ret.add(new Position(i,j));
            }
        }
        return ret;
    }

    public int zoneArea(){
        int side1 = -1;
        int side2 = -1;
        if(p1.getX() == p2.getX() && p3.getY() == p4.getY()){
            if(p1.getY() < p2.getY()){
                side1 = p2.getY() - p1.getY();
            } else
                side1 = p1.getY() - p2.getY();
            if(p3.getX() < p4.getX()){
                side2 = p4.getX() - p3.getX();
            } else
                side2 = p3.getX() - p4.getX();
        }
        if(p1.getX() == p4.getX() && p3.getY() == p2.getY()){
            if(p1.getY() < p4.getY()){
                side1 = p4.getY() - p1.getY();
            } else
                side1 = p1.getY() - p4.getY();
            if(p3.getX() < p4.getX()){
                side2 = p2.getX() - p3.getX();
            } else
                side2 = p3.getX() - p2.getX();
        }
        if(p1.getX() == p3.getX() && p2.getY() == p4.getY()){
            if(p1.getY() < p3.getY()){
                side1 = p3.getY() - p1.getY();
            } else
                side1 = p1.getY() - p3.getY();
            if(p2.getX() < p4.getX()){
                side2 = p4.getX() - p2.getX();
            } else
                side2 = p2.getX() - p4.getX();
        }
        if(p4.getX() == p2.getX() && p3.getY() == p1.getY()){
            if(p4.getY() < p2.getY()){
                side1 = p2.getY() - p4.getY();
            } else
                side1 = p4.getY() - p2.getY();
            if(p3.getX() < p4.getX()){
                side2 = p1.getX() - p3.getX();
            } else
                side2 = p3.getX() - p1.getX();
        }
        if(p2.getX() == p3.getX() && p3.getY() == p1.getY()){
            if(p1.getY() < p4.getY()){
                side1 = p3.getY() - p2.getY();
            } else
                side1 = p2.getY() - p3.getY();
            if(p3.getX() < p4.getX()){
                side2 = p1.getX() - p3.getX();
            } else
                side2 = p3.getX() - p1.getX();
        }
        if(p4.getX() == p3.getX() && p2.getY() == p1.getY()){
            if(p1.getY() < p3.getY()){
                side1 = p3.getY() - p4.getY();
            } else
                side1 = p4.getY() - p3.getY();
            if(p2.getX() < p4.getX()){
                side2 = p1.getX() - p2.getX();
            } else
                side2 = p2.getX() - p1.getX();
        }
        return side1*side2;
    }
}