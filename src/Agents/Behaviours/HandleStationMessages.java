package Agents.Behaviours;

import Agents.AgentData;
import Agents.Behaviours.Handlers.*;
import Agents.Messages.*;
import Agents.Station;
import Logic.Fire;
import Logic.Metric;
import Util.Ocupation;

import Util.Position;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.util.ArrayList;
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
                        s.addBehaviour(new HandleUpdateData(s, msg));
                    else if (content instanceof FireExtinguished) {
                        s.addBehaviour(new HandleFireExtinguished(s, msg));
                    } else if (content instanceof StartedFire) {
                        s.addBehaviour(new HandleFireStarted(s, msg));
                    } else if (content instanceof FireOnTheWay){
                        s.addBehaviour(new HandleFireOnTheWay(s, msg));
                    }
                    break;
                case (ACLMessage.AGREE):
                    if (content instanceof ExtinguishFireData)
                        s.addBehaviour(new HandleAcceptRequest(s,msg));
                    break;
                case (ACLMessage.REFUSE):
                    if (content instanceof ExtinguishFireData)
                        s.addBehaviour(new HandleRefuseFireRequest(s,msg));
                    break;
                default:
                    System.out.println("Wrong message content.");
                    break;
            }
        } catch (UnreadableException e) {
            System.out.println("Error Station on msg: " + msg.getPerformative() + " -> " + msg.getContent());
        }
    }
}
