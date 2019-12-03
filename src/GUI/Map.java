package GUI;

import Agents.AgentData;
import Logic.Fire;
import Logic.World;
import Logic.Zone;
import Util.FiremanType;
import Util.Position;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Map {
    private JFrame frame;
    private JPanel panel;
    private JTable table;
    private JScrollPane tableContainer;
    private DefaultTableModel model_table;
    private String[][] data;
    private HashMap<String,String> objects;
    private HashMap<Position,String> zones;


    public Map(World world) {
        String[] colors = {
                "#f9ebea",
                "#ebdef0",
                "#e8daef",
                "#fdedec",
                "#f5b7b1",
                "#d7bde2",
                "#f5eef8",
                "#f4ecf7",
                "#f2d7d5",
                "#fadbd8",
                "#ebdef0",
                "#e8daef",
                "#e6b0aa",
                "#d2b4de",
        };
        objects = new HashMap<>();
        objects.put("water", "#1446e2");
        objects.put("fuel", "#b4a18f");
        objects.put("house", "#ffffff");
        objects.put("fire", "#f20505");
        objects.put("drone", "#f7e52b");
        objects.put("aircraft", "#1de8cc");
        objects.put("truck", "#a336f9");
        this.zones = new HashMap<>();

        this.frame = new JFrame("Fire Simulation");
        this.panel = new JPanel();
        this.panel.setLayout(new BorderLayout());
        this.data = new String[World.dimension][World.dimension];
        this.model_table = new DefaultTableModel(World.dimension,World.dimension);
        int j = 0;
        for(Zone z : world.getZones()){
            ArrayList<Position> pos = z.getAllPositions();
            for (Position p : pos) {
                this.zones.put(p, colors[j]);
            }
            j++;
        }
        update(world);
        this.table = new JTable(this.model_table);

        CellColorRenderer renderer = new CellColorRenderer(this.data);
        this.table.setRowHeight(15);
        TableColumnModel columnModel = this.table.getColumnModel();
        for(int i = 0; i < World.dimension; i++){
            columnModel.getColumn(i).setCellRenderer(renderer);
            columnModel.getColumn(i).setPreferredWidth(15);
        }
        this.table.setPreferredScrollableViewportSize(table.getPreferredSize());

        this.table.setDefaultEditor(Object.class, null);
        this.tableContainer = new JScrollPane(table);
        this.table.setTableHeader(null);

        this.panel.add(tableContainer, BorderLayout.CENTER);
        this.frame.getContentPane().add(panel);

        this.frame.pack();
        this.frame.setVisible(true);
    }

    public void update(World world) {
        for(Position p : this.zones.keySet()){
            this.data[p.getY()][p.getX()] = this.zones.get(p);
        }
        //Populate Buildings
        for(Position p : world.getHouses()){
            this.data[p.getY()][p.getX()] = this.objects.get("house");
        }
        //Populate Water
        for(Position p : world.getWater()){
            this.data[p.getY()][p.getX()] = this.objects.get("water");
        }
        //Populate Gas
        for(Position p : world.getFuel()){
            this.data[p.getY()][p.getX()] = this.objects.get("fuel");
        }

        //Populate Firemans
        for(AgentData a : world.getFireman().values()){
            Position p = a.getActual_position();
            if(a.getFiremanType() == FiremanType.AIRCRAFT)
                this.data[p.getY()][p.getX()] = this.objects.get("aircraft");
            else if(a.getFiremanType() == FiremanType.DRONE)
                this.data[p.getY()][p.getX()] = this.objects.get("drone");
            else
                this.data[p.getY()][p.getX()] = this.objects.get("truck");
        }
        //Populate Fires
        for(Fire f : world.getFire()){
            for(Position p : f.getPositions())
                this.data[p.getY()][p.getX()] = this.objects.get("fire");
        }
    }

    public void updateGUI(){
        CellColorRenderer renderer = new CellColorRenderer(this.data);
        TableColumnModel columnModel = this.table.getColumnModel();
        for(int i = 0; i < World.dimension; i++){
            columnModel.getColumn(i).setCellRenderer(renderer);
        }
        DefaultTableModel tableModel = (DefaultTableModel) this.table.getModel();
        tableModel.fireTableDataChanged();
    }
}

class CellColorRenderer extends DefaultTableCellRenderer {
    private String[][] data;
    CellColorRenderer(String[][] data) {
        super();
        this.data = data;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,   boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setBackground(Color.decode(this.data[row][column]));
        return cell;
    }
}
