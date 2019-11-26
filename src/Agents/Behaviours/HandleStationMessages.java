package Agents.Behaviours;

import Agents.AgentData;
import Agents.Messages.*;
import Agents.Station;
import Logic.Fire;
import Util.Ocupation;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HandleStationMessages extends CyclicBehaviour {
    @Override
    public void action() {
        Station s = (Station) myAgent;
        ACLMessage msg = myAgent.receive();
        if (msg == null) {
            block();
            return;
        }
        try {
            Object content = msg.getContentObject();
            switch (msg.getPerformative()) {
                case (ACLMessage.INFORM):
                    if (content instanceof UpdateData)
                        handleUpdateData(s, msg);
                    else if (content instanceof FireExtinguished) {
                        handleFireExtinguished(s, msg);
                    } else if (content instanceof StartedFire) {
                        handleFireStarted(s, msg);
                    } else if (content instanceof FireOnTheWay){
                        handleFireOnTheWay(s, msg);
                    }
                    break;
                case (ACLMessage.AGREE):
                    if (content instanceof ExtinguishFireData)
                        handleAcceptRequest(s, msg);
                    break;
                case (ACLMessage.REFUSE):
                    if (content instanceof ExtinguishFireData)
                        handleRefuseFireRequest(s, msg);
                    break;
                default:
                    System.out.println("Wrong message content.");
                    break;
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    private  void informFireHandled(Station s, ACLMessage msg){
        try {
            FireAlreadyHandled fire = (FireAlreadyHandled) msg.getContentObject();
            Set<AID> aids = s.getTreatment_fire().keySet();
            int flag = 0;
            for (AID aid: aids) {
                if(s.getTreatment_fire().get(aid) == fire.getFire()){
                    s.getTreatment_fire().remove(aid);
                    ACLMessage message = new ACLMessage(ACLMessage.CANCEL);
                    message.addReceiver(aid);
                    this.myAgent.send(message);
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                AID aid = s.getQuestioning().get(fire.getFire()).get(s.getQuestioning().get(fire.getFire()).size()-1);
                ACLMessage message = new ACLMessage(ACLMessage.CANCEL);
                message.addReceiver(aid);
                this.myAgent.send(message);
                s.getQuestioning().remove(fire.getFire());
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    //TODO verificar quando nao há ninguem que resolva o fogo
    private void handleRefuseFireRequest(Station s, ACLMessage msg) {
        try {
            ExtinguishFireData cont = (ExtinguishFireData) msg.getContentObject();
            Fire f = cont.getFire();
            Map<Fire, List<AID>> questioning = s.getQuestioning();
            List<AID> unavailable = questioning.get(f);
            System.out.println("Someone refused, searching another one...");
            AID agent = s.findBestFireman(f,unavailable);

            ACLMessage res = new ACLMessage(ACLMessage.REQUEST);
            res.setContentObject(cont);
            res.addReceiver(agent);
            this.myAgent.send(res);

            unavailable.add(agent);
        } catch (UnreadableException | IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAcceptRequest(Station s, ACLMessage msg) {
        try {
            ExtinguishFireData cont = (ExtinguishFireData) msg.getContentObject();
            Map<AID, Fire> treatment_fire = s.getTreatment_fire();
            treatment_fire.put(msg.getSender(), cont.getFire());
            AgentData ag = s.getWorld().getFireman().get(msg.getSender());
            ag.setOcupation(Ocupation.MOVING);
            ag.setTreating_fire(cont.getFire());

            s.getQuestioning().remove(cont.getFire());
            s.getWaiting_fire().remove(cont.getFire());

        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    public void handleUpdateData(Station s, ACLMessage msg){
        try {
            UpdateData cont = (UpdateData) msg.getContentObject();
            AgentData ag = s.getWorld().getFireman().get(msg.getSender());
            ag.setActual_position(cont.getP());
            ag.setCap_fuel(cont.getFuel());
            ag.setCap_water(cont.getWater());
            ag.setOcupation(cont.getOcupation());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    private void handleFireStarted(Station s, ACLMessage msg){
        try {
            StartedFire cont = (StartedFire) msg.getContentObject();
            s.getWaiting_fire().add(cont.getFire());
            System.out.println("[STATION] Novo fogo: " + cont.getFire().toString());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }
                
    private void handleFireExtinguished(Station s, ACLMessage msg){
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

    private void handleFireOnTheWay(Station s, ACLMessage msg){
        try {
            FireOnTheWay fire = (FireOnTheWay) msg.getContentObject();
            s.getTreatment_fire().put(msg.getSender(), fire.getFire());
            s.getWorld().getFireman().get(msg.getSender()).setOcupation(Ocupation.IN_ACTION);
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }
}
