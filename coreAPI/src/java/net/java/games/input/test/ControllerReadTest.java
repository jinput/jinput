/*
 * ConrtollerReadTest.java
 *
 * Created on May 5, 2003, 3:15 PM
 */
/*****************************************************************************
 * Copyright (c) 2003 Sun Microsystems, Inc.  All Rights Reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistribution of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materails provided with the distribution.
 *
 * Neither the name Sun Microsystems, Inc. or the names of the contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANT OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMEN, ARE HEREBY EXCLUDED.  SUN MICROSYSTEMS, INC. ("SUN") AND
 * ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS
 * A RESULT OF USING, MODIFYING OR DESTRIBUTING THIS SOFTWARE OR ITS 
 * DERIVATIVES.  IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES.  HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OUR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for us in
 * the design, construction, operation or maintenance of any nuclear facility
 *
 *****************************************************************************/
package net.java.games.input.test;

/**
 *
 * @author  administrator
 */

import net.java.games.input.*;
import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;

abstract class AxisPanel extends JPanel{
    Axis axis;
    float data;
    
    public AxisPanel(Axis ax){
        axis = ax;
        setLayout(new BorderLayout());
        add(new JLabel(ax.getName()+"("+ax.getIdentifier()+")"),
            BorderLayout.NORTH);
    }
    
    public void poll(){
        data = axis.getPollData();
        renderData();
    }
    
    protected abstract void renderData();
}

class DigitalAxisPanel extends AxisPanel {
    JLabel digitalState = new JLabel("<unread>");
    
    public DigitalAxisPanel(Axis ax) {
        super(ax);
        add(digitalState,BorderLayout.CENTER);
    }
    
    protected void renderData(){
        if (data == 0.0f){
            digitalState.setBackground(getBackground());
            digitalState.setText("OFF");
        } else if ( data == 1.0f) {
            digitalState.setBackground(Color.green);
            digitalState.setText("ON");
        }else { // shoudl never happen
            digitalState.setBackground(Color.red);
            digitalState.setText("ERR:"+data);
        }
        digitalState.repaint();
    }
}

class DigitalHatPanel extends AxisPanel {
    JLabel digitalState = new JLabel("<unread>");
    
    public DigitalHatPanel(Axis ax) {
        super(ax);
        add(digitalState,BorderLayout.CENTER);
    }
    
    protected void renderData(){
        if (data == Axis.POV.OFF){
            digitalState.setBackground(getBackground());
            digitalState.setText("OFF");
        } else if ( data == Axis.POV.UP) {
            digitalState.setBackground(Color.green);
            digitalState.setText("UP");
        } else if ( data == Axis.POV.RIGHT) {
            digitalState.setBackground(Color.green);
            digitalState.setText("RIGHT");
        } else if ( data == Axis.POV.DOWN) {
            digitalState.setBackground(Color.green);
            digitalState.setText("DOWN");
        } else if ( data == Axis.POV.LEFT) {
            digitalState.setBackground(Color.green);
            digitalState.setText("LEFT");    
        }else { // shoudl never happen
            digitalState.setBackground(Color.red);
            digitalState.setText("ERR:"+data);
        }
        digitalState.repaint();
    }
}
class AnalogAxisPanel extends AxisPanel {
    JLabel analogState = new JLabel("<unread>");
    
    public AnalogAxisPanel(Axis ax) {
        super(ax);
        add(analogState,BorderLayout.CENTER);
    }
    
    protected void renderData(){
        analogState.setText(""+data);
        analogState.repaint();
    }
}



class ControllerWindow extends JFrame {
    Controller ca;
    List axisList = new ArrayList();
    boolean disabled = false;
    
    public ControllerWindow(JFrame frame,Controller ca){
        super(ca.getName());
        this.setName(ca.getName());
        this.ca = ca;
        Container c = this.getContentPane();
        c.setLayout(new BorderLayout());
        Axis[] axis = ca.getAxes();
        System.out.println("Axis count = "+axis.length);
        if (axis.length>0) {
            int width = (int)Math.ceil(Math.sqrt(axis.length));
            JPanel p = new JPanel();
            p.setLayout(new GridLayout(width,0));
            for(int j=0;j<axis.length;j++){
                addAxis(p,axis[j]);
            }  
            c.add(new JScrollPane(p),BorderLayout.CENTER);
        }
        setSize(400,400);
        setLocation(50,50);
        setVisible(true);
    }
    
    public boolean disabled() {
        return disabled;
    }
    
    private void setDisabled(boolean b){
        disabled = b;
        if (!disabled){
           this.setTitle(ca.getName()); 
           System.out.println(ca.getName()+" enabled");
        } else {
           this.setTitle(ca.getName()+" DISABLED!");
           System.out.println(ca.getName()+" disabled");
        }
        repaint();
    }
    
    private void addAxis(JPanel p, Axis ax){
        JPanel p2;
        if (ax.isAnalog()) {
            p2 = new AnalogAxisPanel(ax);
        } else {
            if (ax.getIdentifier() == Axis.Identifier.POV) {
                p2 = new DigitalHatPanel(ax);
            } else {     
                p2 = new DigitalAxisPanel(ax);
            }
        }
        p.add(p2);
        axisList.add(p2);
        ax.setPolling(true);
    }
    
    public void poll(){
        if (!ca.poll()) {
            if (!disabled()){
                setDisabled(true);
            }
            return;
        } 
        if (disabled()){
            setDisabled(false);
        }
        //System.out.println("Polled "+ca.getName());
        for(Iterator i =axisList.iterator();i.hasNext();){
            try {
                ((AxisPanel)i.next()).poll();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

public class ControllerReadTest extends JFrame{
        static final long HEARTBEATMS =100; // 10th of a second
        List controllers = new ArrayList();
    /** Creates a new instance of ConrtollerReadTest */
        public ControllerReadTest() {
            super("Controller Read Test");
            ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();        
            Controller[] ca = ce.getControllers();
            for(int i =0;i<ca.length;i++){
               makeController(ca[i]);
            }
        
        new Thread(new Runnable() {
            public void run(){
                try {
                    while(true){
                        for(Iterator i=controllers.iterator();i.hasNext();){
                            try {
                                ControllerWindow cw = (ControllerWindow)i.next();
                                cw.poll();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Thread.sleep(HEARTBEATMS);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        pack();
        setSize(400,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void makeController(Controller c) {
        Controller[] subControllers = c.getControllers();
        if (subControllers.length == 0 ) {
            createControllerWindow(c);
        } else {
            for(int i=0;i<subControllers.length;i++){
                makeController(subControllers[i]);
            }
        }
    }
    
    private void createControllerWindow(Controller c){
        controllers.add(new ControllerWindow(this,c));
    }    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ControllerReadTest().setVisible(true);
    }
    
}
