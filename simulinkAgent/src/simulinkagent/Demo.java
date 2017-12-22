/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulinkagent;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

/**
 *
 * @author xcszbdnl
 */
public class Demo {
    public Demo() {
        
    }
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl(null, 8888, null);
        System.out.println(p);
        AgentContainer ac = rt.createMainContainer(p);
        try {
            AgentController aa = ac.createNewAgent("agent1", "simulinkagent.simpleAgent", null);
            aa.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
