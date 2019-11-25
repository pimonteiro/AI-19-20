package Agents.Behaviours;

import Agents.Messages.StartedFire;
import Agents.Station;
import Logic.Fire;
import Util.Position;
import Agents.AgentData;
import Agents.Messages.FireExtinguished;
import Agents.Station;

import Logic.Fire;
import Util.Ocupation;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HandleStationMessages extends CyclicBehaviour {
    @Override
    public void action() {
        Station station = (Station) myAgent;
        Station s = (Station) myAgent;
        ACLMessage msg = myAgent.receive();
        if(msg == null){
            block();
            return;
        }
        try{
            Object content = msg.getContentObject();
            switch (msg.getPerformative()){
                case(ACLMessage.INFORM):
                    if(content instanceof StartedFire) {
                        handleFireStarted(station, msg);
                    }
                    else{

        try {
            Object content = msg.getContentObject();
            switch (msg.getPerformative()){
                case(ACLMessage.INFORM):
                    if(content instanceof FireExtinguished){
                        handleFireExtinguished(s, msg);
                    } else {
                        System.out.println("Wrong message content.");
                    }
                    break;
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    private void handleFireStarted(Station s, ACLMessage msg){
        try {
            StartedFire cont = (StartedFire) msg.getContentObject();
            s.getWaiting_fire().add(cont.getFire());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }
                
    private void handleFireExtinguished(Station s, ACLMessage msg) throws UnreadableException {
        AID aid = msg.getSender();

        Map<AID, Fire> agentAndFire = s.getTreatment_fire().entrySet().stream()
                .filter(a -> a.getKey() == aid)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (!agentAndFire.isEmpty()) {
            Fire extinguishedFire = agentAndFire.get(0);
            AgentData agentData = s.getWorld().getFireman().get(aid);

            //eliminar o fire do World
            s.getWorld().getFire().remove(extinguishedFire);
            //eliminar o par agente&fire do treatment_fire
            s.getTreatment_fire().remove(aid);
            //alterar o estado do fireman para "a regressar"
            agentData.setOcupation(Ocupation.RETURNING);
            //eliminar treatment fire do fireman
            agentData.setTreating_fire(null);
        }
    }
}
