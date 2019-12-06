package GUI;

import Agents.AgentData;
import Logic.World;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AgentState {
    private JFrame frame;
    private JPanel panel;
    private JTable table;
    private JScrollPane tableContainer;
    private DefaultTableModel model_table;

    public AgentState(World world){
        this.frame = new JFrame("Agent State");
        this.panel = new JPanel();
        this.panel.setLayout(new BorderLayout());
        this.model_table = new DefaultTableModel(world.getFireman().size(),4);
        this.table = new JTable(this.model_table);
        update(world);

        this.table.setPreferredScrollableViewportSize(table.getPreferredSize());

        this.table.setDefaultEditor(Object.class, null);
        this.tableContainer = new JScrollPane(table);
        this.table.setTableHeader(null);

        this.panel.add(tableContainer, BorderLayout.CENTER);
        this.frame.getContentPane().add(panel);

        this.frame.pack();
        this.frame.setBounds(100,100,600,500);
        this.frame.setVisible(true);
    }

    public void update(World world) {
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();
        model.setNumRows(0);
        model.addRow(new String[]{"Agent AID", "Ocupation", "Actual Position","Treating Fire?"});

        for(AgentData ag : world.getFireman().values()){
            if(ag.getTreating_fire() != null)
                model.addRow(new String[]{ag.getAid().getName(), ag.getOcupation().toString(), ag.getActual_position().toString(), ag.getTreating_fire().getPositions().get(0).toString()});
            else
                model.addRow(new String[]{ag.getAid().getName(), ag.getOcupation().toString(), ag.getActual_position().toString(), "Null"});
        }
    }

    public void updateGUI(){
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();
        model.fireTableDataChanged();
    }

}
