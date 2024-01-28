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
package net.java.games.input;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public final class ControllerEventTest extends JFrame {
	@Serial
	private static final long serialVersionUID = -8266185848160199092L;

	private static abstract class AxisPanel extends JPanel {
		@Serial
		private static final long serialVersionUID = -6200599064870672000L;
		transient Component axis;
		float data;

		public AxisPanel(Component ax){
			axis = ax;
			setLayout(new BorderLayout());
			add(new JLabel(ax.getName()+"("+ax.getIdentifier()+")"),
					BorderLayout.NORTH);
		}

		public void setPollData(float data){
			this.data = data;
			renderData();
		}

		public Component getAxis() {
			return axis;
		}

		protected abstract void renderData();
	}

	private static class DigitalAxisPanel extends AxisPanel {
		@Serial
		private static final long serialVersionUID = -4729666037860134626L;
		JLabel digitalState = new JLabel("<unread>");

		public DigitalAxisPanel(Component ax) {
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

	private static class DigitalHatPanel extends AxisPanel {
		@Serial
		private static final long serialVersionUID = -6582605379682496832L;
		JLabel digitalState = new JLabel("<unread>");

		public DigitalHatPanel(Component ax) {
			super(ax);
			add(digitalState,BorderLayout.CENTER);
		}

		protected void renderData(){
			if (data == Component.POV.OFF){
				digitalState.setBackground(getBackground());
				digitalState.setText("OFF");
			} else if ( data == Component.POV.UP) {
				digitalState.setBackground(Color.green);
				digitalState.setText("UP");
			} else if ( data == Component.POV.UP_RIGHT) {
				digitalState.setBackground(Color.green);
				digitalState.setText("UP+RIGHT");
			} else if ( data == Component.POV.RIGHT) {
				digitalState.setBackground(Color.green);
				digitalState.setText("RIGHT");
			} else if ( data == Component.POV.DOWN_RIGHT) {
				digitalState.setBackground(Color.green);
				digitalState.setText("DOWN+RIGHT");
			} else if ( data == Component.POV.DOWN) {
				digitalState.setBackground(Color.green);
				digitalState.setText("DOWN");
			} else if ( data == Component.POV.DOWN_LEFT) {
				digitalState.setBackground(Color.green);
				digitalState.setText("DOWN+LEFT");
			} else if ( data == Component.POV.LEFT) {
				digitalState.setBackground(Color.green);
				digitalState.setText("LEFT");    
			} else if ( data == Component.POV.UP_LEFT) {
				digitalState.setBackground(Color.green);
				digitalState.setText("UP+LEFT");
			}else { // shoudl never happen
				digitalState.setBackground(Color.red);
				digitalState.setText("ERR:"+data);
			}
			digitalState.repaint();
		}
	}
	private static class AnalogAxisPanel extends AxisPanel {
		@Serial private static final long serialVersionUID = 7536173405896285590L;
		JLabel analogState = new JLabel("<unread>");

		public AnalogAxisPanel(Component ax) {
			super(ax);
			add(analogState,BorderLayout.CENTER);
		}

		protected void renderData(){
			String extra = "";
			if (getAxis().getDeadZone() >= Math.abs(data))
				extra = " (DEADZONE)";
			analogState.setText(""+data+extra);
			analogState.repaint();
		}
	}



	private static class ControllerWindow extends JFrame {
		@Serial
		private static final long serialVersionUID = 8623977198558568961L;
		transient Controller ca;
		transient Map<Component, AxisPanel> axes_to_panels = new HashMap<>();
		boolean disabled = false;

		public ControllerWindow(JFrame frame,Controller ca){
			super(ca.getName());
			this.setName(ca.getName());
			this.ca = ca;
			Container c = this.getContentPane();
			c.setLayout(new BorderLayout());
			Component[] components = ca.getComponents();
			System.out.println("Component count = "+components.length);
			if (components.length>0) {
				int width = (int)Math.ceil(Math.sqrt(components.length));
				JPanel p = new JPanel();
				p.setLayout(new GridLayout(width,0));
				for(int j=0;j<components.length;j++){
					addAxis(p,components[j]);
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

		private void addAxis(JPanel p, Component ax){
			AxisPanel p2;
			if (ax.isAnalog()) {
				p2 = new AnalogAxisPanel(ax);
			} else {
				if (ax.getIdentifier() == Component.Identifier.Axis.POV) {
					p2 = new DigitalHatPanel(ax);
				} else {     
					p2 = new DigitalAxisPanel(ax);
				}
			}
			p.add(p2);
			axes_to_panels.put(ax, p2);
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
			EventQueue event_queue = ca.getEventQueue();
			Event event = new Event();
			while (event_queue.getNextEvent(event)) {
				AxisPanel panel = axes_to_panels.get(event.getComponent());
				panel.setPollData(event.getValue());
			}
		}
	}

	static final long HEARTBEATMS =100; // 10th of a second
	transient List<ControllerWindow> controllers = new ArrayList<>();

	public ControllerEventTest() {
		super("Controller Event Test. Version: " + Version.getVersion());
		ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
		Controller[] ca = ce.getControllers();
		for(int i =0;i<ca.length;i++){
			makeController(ca[i]);
		}

		new Thread(() -> {
				try {
					while(true){
						for(Iterator<ControllerWindow> i=controllers.iterator();i.hasNext();){
							try {
								i.next().poll();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						Thread.sleep(HEARTBEATMS);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		}).start();
		pack();
		setSize(400,400);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
		new ControllerEventTest().setVisible(true);
	}

}
