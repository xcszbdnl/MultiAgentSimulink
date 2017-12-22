/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulinkagent;

import jade.core.Agent;
import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.io.IOException;

/**
 *
 * @author xcszbdnl
 */
public class people extends Agent{
    
    private MessageInfo info;
    public people(MessageInfo info) {
        this.info = info;
    }
    public void setup() {
        info.setStatus(AgentStatus.WAITING);
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("repair");
        sd.setName("jade-car-repair");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        addBehaviour(new OfferRequest());
    }
    private class OfferRequest extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive();
            ACLMessage reply = msg.createReply();
            if (msg != null) {
                if (info.getStatus() == AgentStatus.WAITING) {
                    try {
                        reply.setPerformative(ACLMessage.PROPOSE);
                        reply.setLanguage("javaObject");
                        reply.setContentObject(info);
                        myAgent.send(reply);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    reply.setPerformative(ACLMessage.REFUSE);
                }
            }
            else {
                block();
            }
        }
    }
}
