package Agents.Behaviours;

import Agents.Station;
import Logic.Zone;
import jade.core.behaviours.TickerBehaviour;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.ApplicationFrame;

import java.awt.*;

public class MetricController extends TickerBehaviour {
    private Station s;
    private DefaultCategoryDataset datasetFires;
    private DefaultPieDataset datasetFiresZones;
    private ApplicationFrame window;

    public MetricController(Station s, int i) {
        super(s, i);
        this.s = s;
        this.datasetFires = new DefaultCategoryDataset();
        this.datasetFiresZones = new DefaultPieDataset();
        this.window = new ApplicationFrame("Statistics");
    }

    @Override
    protected void onTick() {
        int treating_fires = s.getTreatment_fire().size();
        int waiting_fires = s.getQuestioning().size() + s.getWaiting_fire().size();
        String time = String.valueOf((this.getPeriod()*this.getTickCount()) / 1000);

        //Ratio Fogos em tratamento/em espera
        datasetFires.addValue(treating_fires, "fogos em tratamento", time);
        datasetFires.addValue(waiting_fires,"fogos em espera", time);
        if(datasetFires.getColumnCount() > 15){
            datasetFires.removeColumn(0);
        }
        JFreeChart fires_chart = ChartFactory.createLineChart(
                "Fogos no Mundo",
                "Tempo (s)",
                "Número de Fogos",
                this.datasetFires,
                PlotOrientation.VERTICAL,
                true,true,false);
        ChartPanel chartPanel_fires_chart = new ChartPanel( fires_chart );
        chartPanel_fires_chart.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        window.getContentPane().add(chartPanel_fires_chart, BorderLayout.WEST);

        //Fogos por zona
        datasetFiresZones.clear();
        for(Zone z : this.s.getWorld().getZones()){
            datasetFiresZones.setValue("Zona " + z.getId(),
                    this.s.getWorld().getFire().stream().filter(f -> f.getZone_id() == z.getId()).count()
                    );
        }
        JFreeChart fires_zones_chart = ChartFactory.createPieChart(
                "Fogos por Zona",
                this.datasetFiresZones,
                true, true, false);
        ChartPanel chartPanel_firesZones_chart = new ChartPanel( fires_zones_chart );

        chartPanel_firesZones_chart.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        window.getContentPane().add(chartPanel_firesZones_chart, BorderLayout.EAST);

        //Utilizaçao de cada tipo de veiculo
        //Quantidade de combustível usado



        this.window.setSize( 1024 , 768 );
        this.window.setVisible( true );
    }
}
