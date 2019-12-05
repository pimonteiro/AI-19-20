import Logic.World;
import Logic.Simulator;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class MainContainer {

    Runtime rt;
    ContainerController container;

    public ContainerController initContainerInPlatform(String host, String port, String containerName) {
        // Get the JADE runtime interface (singleton)
        this.rt = Runtime.instance();

        // Create a Profile, where the launch arguments are stored
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.CONTAINER_NAME, containerName);
        profile.setParameter(Profile.MAIN_HOST, host);
        profile.setParameter(Profile.MAIN_PORT, port);
        // create a non-main agent container
        ContainerController container = rt.createAgentContainer(profile);
        return container;
    }

    public void initMainContainerInPlatform(String host, String port, String containerName) {

        // Get the JADE runtime interface (singleton)
        this.rt = Runtime.instance();

        // Create a Profile, where the launch arguments are stored
        Profile prof = new ProfileImpl();
        prof.setParameter(Profile.CONTAINER_NAME, containerName);
        prof.setParameter(Profile.MAIN_HOST, host);
        prof.setParameter(Profile.MAIN_PORT, port);
        prof.setParameter(Profile.MAIN, "true");
        prof.setParameter(Profile.GUI, "true");

        // create a main agent container
        this.container = rt.createMainContainer(prof);
        rt.setCloseVM(true);

    }

    public void startAgentInPlatform(String name, String classpath) {
        try {
            AgentController ac = container.createNewAgent(name, classpath, new Object[0]);
            ac.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MainContainer a = new MainContainer();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        a.initMainContainerInPlatform("localhost", "9090", "MainContainer");

        //Agents and World Initialization
        int n_aircraft = 9;
        int n_drone = 9;
        int n_truck = 9;
        int n_water = 3;
        int n_fuel = 3;
        int n_houses = 2;
        int n_zones = 4;
        World world = new World(20);
        Simulator.startSimulation_v1(world,n_fuel,n_houses,n_water,n_zones);
        ContainerController agentContainer = a.initContainerInPlatform("localhost", "9888", "AgentsContainer");

        try {
            for(int i = 0; i < n_aircraft; i++){
                AgentController ag = agentContainer.createNewAgent("aircraft_"+i, "Agents.Aircraft", new Object[] {world});// arguments
                ag.start();
            }
            for(int i = 0; i < n_drone; i++){
                AgentController ag = agentContainer.createNewAgent("drone_"+i, "Agents.Drone", new Object[] {world});// arguments
                ag.start();
            }
            for(int i = 0; i < n_truck; i++){
                AgentController ag = agentContainer.createNewAgent("truck_"+i, "Agents.FireTruck", new Object[] {world});// arguments
                ag.start();
            }
            AgentController ag1 = agentContainer.createNewAgent("station", "Agents.Station", new Object[] {world});// arguments
            ag1.start();

            Thread.sleep(5000);

            AgentController ag2 = agentContainer.createNewAgent("firestarter", "Agents.FireStarter", new Object[] {world});// arguments
            ag2.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}