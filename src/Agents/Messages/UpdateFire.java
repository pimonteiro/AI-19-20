package Agents.Messages;
import Util.Position;
import java.io.Serializable;
import java.util.ArrayList;

public class UpdateFire implements Serializable {
    private ArrayList<Position> pos;
    private boolean type;

    public UpdateFire(ArrayList<Position> pos, boolean type) {
        this.pos = pos;
        this.type = type;
    }

    public ArrayList<Position> getPos() {
        return pos;
    }

    public void setPos(ArrayList<Position> pos) {
        this.pos = pos;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}