package Agents.Behaviours;

import Agents.AgentData;
import Agents.Messages.FireExtinguished;
import Agents.Station;

import Logic.Fire;
import Util.Ocupation;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.Map;
import java.util.stream.Collectors;

public class HandleStationMessages extends CyclicBehaviour {
    @Override
    public void action() {
        Station s = (Station) myAgent;
        ACLMessage msg = myAgent.receive();
        if(msg == null){
            block();
            return;
        }

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

    private void handleFireExtinguished(Station s, ACLMessage msg) throws UnreadableException {
        AID aid = msg.getSender();

        Map<AgentData, Fire> agentAndFire = s.getTreatment_fire().entrySet().stream()
                .filter(a -> a.getKey().getAid() == aid)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (!agentAndFire.isEmpty()) {
            Fire extinguishedFire = agentAndFire.get(0);
            AgentData agentData = (AgentData) agentAndFire.keySet().toArray()[0];

            //eliminar o fire do World
            s.getWorld().getFire().remove(extinguishedFire);
            //eliminar o par agente&fire do treatment_fire
            s.getTreatment_fire().remove(agentData);
            //alterar o estado do fireman para "a regressar"
            agentData.setOcupation(Ocupation.RETURNING);
            //eliminar treatment fire do fireman
            agentData.setTreating_fire(null);
        }
    }
}
