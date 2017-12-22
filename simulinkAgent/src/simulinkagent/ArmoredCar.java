/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulinkagent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.util.Random;
/**
 *
 * @author xcszbdnl
 */
public class ArmoredCar extends Agent{
    private MessageInfo info;
    private AID[] peopleAgents;
    
    public ArmoredCar() {
        Random fRandom = new Random();
        double longti = 116 + fRandom.nextGaussian();
        double lati = 39 + fRandom.nextGaussian();
        info.setLati(lati);
        info.setLongti(longti);
        info.setStatus(AgentStatus.RUNNING);
    }
    public ArmoredCar(MessageInfo info) {
        this.info = info;
    }
    public void setup() {
        
    }
    
    private class WalkBehaviour extends Behaviour {
        public void action() {
            switch (info.getStatus()) {
                case AgentStatus.RUNNING:
                    Random fRandom = new Random();
                    double longti_delta = fRandom.nextGaussian();
                    double lati_delta = fRandom.nextGaussian();
                    info.walk(longti_delta, lati_delta);
                    System.out.println("Current info:" + info.getInformation());
                    break;
                case AgentStatus.BROKEN:
                    myAgent.addBehaviour(new RequestService());
                    break;
                default:
                    System.out.println("Fatal error: cars WalkBehaviour unexpected status");
                    break;
            }
        }
        public boolean done() {
            return info.getStatus() != AgentStatus.RUNNING;
        }
    }
    
    private class RequestService extends Behaviour {
        private AID closestPeople = null;
        private double closestDist = Float.POSITIVE_INFINITY;
        private int repliesCnt = 0;
        private int step = 0;
        private MessageTemplate mt;
        public void action() {
            switch (step) {
                case 0:
                    ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                    for (int i = 0; i < peopleAgents.length; i++) {
                        cfp.addReceiver(peopleAgents[i]);
                    }
                    cfp.setConversationId("repair");
                    cfp.setReplyWith("cfp" + System.currentTimeMillis());
                    myAgent.send(cfp);
                    mt = MessageTemplate.and(MessageTemplate.MatchConversationId("repair"), 
                            MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                    step = 1;
                    break;
                case 1:
                    try {
                        ACLMessage reply = myAgent.receive(mt);
                        if (reply != null) {
                            if (reply.getPerformative() == ACLMessage.PROPOSE) {
                                MessageInfo people_info = (MessageInfo)reply.getContentObject();
                                double dist = info.calDist(people_info);
                                if (dist < closestDist) {
                                    closestDist = dist;
                                    closestPeople = reply.getSender();
                                }
                            }
                            repliesCnt++;
                            if (repliesCnt >= peopleAgents.length) {
                                // receive all replies
                                step = 2;
                            }
                        }
                    }
                    catch (UnreadableException e) {
                        System.err.println("Catch an exception:" + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        ACLMessage request = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                        request.addReceiver(closestPeople);
                        request.setLanguage("javaObject");
                        request.setContentObject(info);
                        request.setConversationId("request-repair");
                        request.setReplyWith("request" + System.currentTimeMillis());
                        myAgent.send(request);
                        mt = MessageTemplate.and(MessageTemplate.MatchConversationId("request-repair"),
                                MessageTemplate.MatchInReplyTo(request.getReplyWith()));
                        step = 3;
                        break;
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                case 3:
                    ACLMessage reply = myAgent.receive(mt);
                    if (reply != null) {
                        if (reply.getPerformative() == ACLMessage.INFORM) {
                            System.out.println("Successfully get agent " + reply.getSender().getName());
                        }
                    }
                    else {
                        
                    }
            }
        }
        public boolean done() {
            if (step == 2 && closestPeople == null) {
                System.out.println("Attempt failed: no people available");
            }
            return ((step == 2 && closestPeople == null) || step == 4);
        }
    }
}
