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
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Map {
    private JFrame frame;
    private JPanel panel;
    private JTable table;
    private JScrollPane tableContainer;
    private DefaultTableModel model_table;
    private Icon[][] data;

    public Map(World world) {
        String[] colors = {
                 "#f9ebea",
                 "#fdedec",
                 "#f5eef8",
                 "#f4ecf7",
                 "#f2d7d5",
                 "#fadbd8",
                 "#ebdef0",
                 "#e8daef",
                 "#e6b0aa",
                 "#f5b7b1",
                 "#d7bde2",
                 "#d2b4de",
        };

        this.frame = new JFrame("Fire Simulation");
        this.panel = new JPanel();
        this.panel.setLayout(new BorderLayout());
        this.data = new ImageIcon[World.dimension][World.dimension];
        this.model_table = new DefaultTableModel(World.dimension,World.dimension);
        update(world);
        this.table = new JTable(this.model_table)
        {
            public Class getColumnClass(int column)
            {
                return Icon.class;
            }
        };

        HashMap<Position,Color> zones = new HashMap<>();
        int j = 0;
        for(Zone z : world.getZones()){
            ArrayList<Position> pos = z.getAllPositions();
            for (Position p : pos) {
                zones.put(p, Color.decode(colors[j]));
            }
            j++;
        }
        CellColorRenderer renderer = new CellColorRenderer(zones);
        this.table.setRowHeight(30);
        TableColumnModel columnModel = this.table.getColumnModel();
        for(int i = 0; i < World.dimension; i++){
            columnModel.getColumn(i).setCellRenderer(renderer);
            columnModel.getColumn(i).setPreferredWidth(30);
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
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("assets/house.png")); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(30,30,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        Icon house = new ImageIcon(newimg);  // transform it back

        imageIcon = new ImageIcon(getClass().getResource("assets/water.png"));
        image = imageIcon.getImage();
        newimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
        Icon water = new ImageIcon(newimg);

        imageIcon = new ImageIcon(getClass().getResource("assets/fuel.png"));
        image = imageIcon.getImage();
        newimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
        Icon fuel = new ImageIcon(newimg);

        imageIcon = new ImageIcon(getClass().getResource("assets/fire.png"));
        image = imageIcon.getImage();
        newimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
        Icon fire = new ImageIcon(newimg);

        imageIcon = new ImageIcon(getClass().getResource("assets/truck.png"));
        image = imageIcon.getImage();
        newimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
        Icon truck = new ImageIcon(newimg);

        imageIcon = new ImageIcon(getClass().getResource("assets/drone.png"));
        image = imageIcon.getImage();
        newimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
        Icon drone = new ImageIcon(newimg);

        imageIcon = new ImageIcon(getClass().getResource("assets/aircraft.png"));
        image = imageIcon.getImage();
        newimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
        Icon aircraft = new ImageIcon(newimg);

        for(int i = 0; i < World.dimension; i++){
            for(int j = 0; j < World.dimension; j++) {
                this.data[i][j] = null;
            }
        }

        //Populate Buildings
        for(Position p : world.getHouses()){
            this.data[p.getY()][p.getX()] = house;
        }
        //Populate Water
        for(Position p : world.getWater()){
            this.data[p.getY()][p.getX()] = water;
        }
        //Populate Gas
        for(Position p : world.getFuel()){
            this.data[p.getY()][p.getX()] = fuel;
        }
        //Populate Fires
        for(Fire f : world.getFire()){
            for(Position p : f.getPositions())
                this.data[p.getY()][p.getX()] = fire;
        }
        //Populate Firemans
        for(AgentData a : world.getFireman().values()){
            Position p = a.getActual_position();
            if(a.getFiremanType() == FiremanType.AIRCRAFT)
                this.data[p.getY()][p.getX()] = aircraft;
            else if(a.getFiremanType() == FiremanType.DRONE)
                this.data[p.getY()][p.getX()] = drone;
            else
                this.data[p.getY()][p.getX()] = truck;
        }

        for(int i = 0; i < World.dimension; i++){
            for(int j = 0; j < World.dimension; j++) {
                if ((data[i][j] != null)) {
                    this.model_table.setValueAt(data[i][j], i, j);
                 }
            }
        }
    }
}

class CellColorRenderer extends DefaultTableCellRenderer {
    private HashMap<Position, Color> zones;
    CellColorRenderer(HashMap<Position, Color> zones) {
        super();
        this.zones = zones;
    }
    //TODO Fix not appearing the images
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,   boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setBackground(this.zones.get(new Position(column,row)));
        return cell;
    }
}
