/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulinkagent;

/**
 *
 * @author xcszbdnl
 */
public class AgentStatus {
    // waiting for message to repair something
    public static final int WAITING = 0;
    
    // routing to the broken equipment
    public static final int ROUTING = 1;
    
    // repairing the equipment
    public static final int REPAIRING = 2;
    
    // car is running
    public static final int RUNNING = 3;
    
    // car is broken
    public static final int BROKEN = 4;
}
