package GUI;

import Agents.AgentData;
import Logic.Fire;
import Logic.World;
import Util.FiremanType;
import Util.Position;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Map {
    private JFrame frame;
    private JPanel panel;
    private JTable table;
    private JScrollPane tableContainer;
    public Map(World world) {
        this.frame = new JFrame("Fire Simulation");
        this.panel = new JPanel();
        this.panel.setLayout(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel();
        this.table = new JTable( model )
        {
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };
        this.table.setPreferredScrollableViewportSize(table.getPreferredSize());

        this.table.setDefaultEditor(Object.class, null);
        this.tableContainer = new JScrollPane(table);
        this.table.setTableHeader(null);

        this.panel.add(tableContainer, BorderLayout.CENTER);
        this.frame.getContentPane().add(panel);

        this.frame.pack();
        this.frame.setVisible(true);
        update(world);
    }

    public void update(World world) {
        DefaultTableModel model = (DefaultTableModel)this.table.getModel();
        ImageIcon house = new ImageIcon("/assets/images/house.png");
        ImageIcon water = new ImageIcon("/assets/images/water.png");
        ImageIcon fuel = new ImageIcon("/assets/images/fuel.png");
        ImageIcon fire = new ImageIcon("/assets/images/fire.png");
        ImageIcon truck = new ImageIcon("/assets/images/truck.png");
        ImageIcon drone = new ImageIcon("/assets/images/drone.png");
        ImageIcon aircraft = new ImageIcon("/assets/images/aircraft.png");

        ImageIcon[][] data = new ImageIcon[world.dimension][world.dimension];
        //Populate Buildings
        for(Position p : world.getHouses()){
            data[p.getY()][p.getX()] = house;
        }
        //Populate Water
        for(Position p : world.getWater()){
            data[p.getY()][p.getX()] = water;
        }
        //Populate Gas
        for(Position p : world.getFuel()){
            data[p.getY()][p.getX()] = fuel;
        }
        //Populate Fires
        for(Fire f : world.getFire()){
            for(Position p : f.getPositions())
                data[p.getY()][p.getX()] = fire;
        }
        //Populate Firemans
        for(AgentData a : world.getFireman().values()){
            Position p = a.getActual_position();
            if(a.getFiremanType() == FiremanType.AIRCRAFT)
                data[p.getY()][p.getX()] = aircraft;
            else if(a.getFiremanType() == FiremanType.DRONE)
                data[p.getY()][p.getX()] = drone;
            else
                data[p.getY()][p.getX()] = truck;
        }
        model.setRowCount(0);
        for(int i = 0; i < world.dimension; i++){

            model.setValueAt();
        }

    }
}