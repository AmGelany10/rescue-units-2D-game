package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.text.IconView;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.IncompatibleTargetException;
import model.events.SOSListener;
import model.events.UpdateListener;
import model.events.WorldListener;
import model.events.log;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Unit;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;
import simulation.Simulator;
import view.GUI;

public class CommandCenter implements SOSListener, ActionListener,UpdateListener{

	private Simulator engine;
	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
    private Unit runit;
    private Rescuable target;
    private ResidentialBuilding chosen;
	@SuppressWarnings("unused")
	private ArrayList<Unit> emergencyUnits  ;
	private GUI g ;

//private ArrayList<JButton> btns ;
//	
//	
//	public void actionPerformed(ActionEvent e) {
//		
//		JButton btn = (JButton) e.getSource();
//		int productIndex = btns.indexOf(btn);
//		Unit u = emergencyUnits.get(productIndex);
//		u.respond();
//	}
	
	
	public CommandCenter() throws Exception{
		g = new GUI(); 
//		Citizen c = new Citizen(new Address(1, 2),"43-15747", "NOUR",20,engine);
//		g.writeInfo(c);
		
		engine = new Simulator(this);
		engine.updatelog(g);
		g.writeCacualties(engine.calculateCasualties(),0);
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		emergencyUnits = engine.getEmergencyUnits();
		JButton Cu = new JButton("Next Cycle");
		JButton re = new JButton("Respond");
		Cu.addActionListener(this);
		re.addActionListener(this);
		Cu.setVisible(true);
		re.setVisible(true);
		re.setPreferredSize(new Dimension(100,25));
		Cu.setPreferredSize(new Dimension(100,25));
		//pack();
		g.getUBtns().setBackground(Color.white);
		g.getSimBtns().setLayout(new GridLayout(0,2));
		g.getSimBtns().add(Cu, BorderLayout.NORTH);
		g.getSimBtns().add(re,BorderLayout.NORTH);
		
		for(Unit unit : emergencyUnits )
		{
			JButton unitbt = new JButton();
			unitbt.setSize(new Dimension(100,100));
			//unitbt.setSize(new Dimension(g,50));
			unitbt.setText(unit.toString());
			ImageIcon c = unit.getIcon();
			Image img = c.getImage() ;  
			//	System.out.println(unitbt.getWidth() +"   "+ unitbt.getHeight());
			   Image newimg = img.getScaledInstance( unitbt.getWidth(), unitbt.getHeight(),  Image.SCALE_SMOOTH ) ;  
			  c = new ImageIcon( newimg );
			unitbt.setIcon(c);
			unitbt.addActionListener(this);
			g.addUnit(unitbt);
			
		}
		for(int i =0;i<10;i++)
			for(int j=0;j<10;j++) {
				JButton ad =new JButton();
				ad.addActionListener(this);
				ad.setText(i+" "+j);
				ad.setSize(new Dimension(110,80));
				ImageIcon ic = new ImageIcon("em.JPG");
				Image img = ic.getImage() ; 
				Image newimg = img.getScaledInstance( ad.getWidth(), ad.getHeight(),  Image.SCALE_SMOOTH ) ;  
				ic = new ImageIcon( newimg );
				ad.setIcon(ic);
				g.addAdrress(ad);
			}
			
		
		
		g.pack();

	}

	@Override
	public void receiveSOSCall(Rescuable r) {
		
		if (r instanceof ResidentialBuilding) {
			
			if (!visibleBuildings.contains(r))
				visibleBuildings.add((ResidentialBuilding) r);
			
		} else {
			
			if (!visibleCitizens.contains(r))
				visibleCitizens.add((Citizen) r);
		}

	}
	public static void main(String[]args) throws Exception {
		new CommandCenter();
		}
	
	public Unit getUnit(String s) {
		for(Unit u : emergencyUnits )
			if(u.toString().equals(s))
				return u ;
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
		boolean flag = false;
		
		JButton b = (JButton) e.getSource();
		String s = b.getText();
		//System.out.println(s);
		//System.out.println("her");
		
		if(s.equals("Next Cycle")) {
			if(!engine.checkGameOver()) {
			try {
				//btnUpdate();
				engine.nextCycle();
				g.clear();
			} catch (CitizenAlreadyDeadException e1) {
			// TODO Auto-generated catch block
				g.write("Citezen already died");

			} catch (BuildingAlreadyCollapsedException e1) {
				// TODO Auto-generated catch block
				g.write("Building Alredy colappsed");

			} catch (CannotTreatException e1) {
				// TODO Auto-generated catch block
				g.write("Cannot treat this target");

			} catch (IncompatibleTargetException e1) {
				// TODO Auto-generated catch block
				g.write("please choose a vaild unit to the target");
			} 
			}
			else {
				int count = visibleBuildings.size();
				for(ResidentialBuilding j : visibleBuildings)
					count+=j.getOccupants().size();
				g.calculateScore(engine.calculateCasualties(),count-engine.calculateCasualties());
			}
		}
				
		else {
			if (s.equals("Respond")) {
				try {
					btnUpdate();
					runit.respond(target);
					g.writeInfo(runit);
				}
			catch(NullPointerException e1){
				g.write("Please choose a unit and a target");
			}
		catch(IncompatibleTargetException e1 ) {
			g.write("please choose a vaild unit to the target");
			}
			catch(CannotTreatException e1) {
				g.write("Cannot treat this target");
			}
		}
			else {
				if(s.equals("Evacuator")||s.equals("DiseaseControlUnit")||s.equals("Ambulance")||s.equals("GasControlUnit")||s.equals("FIre Truck")) {
					runit= getUnit(s);
					g.writeInfo(runit);
					btnUpdate();
				}
				else {
					try {
						 Integer.parseInt(s);
						 
						 for(Citizen c : visibleCitizens ) 
							 if (s.equals(c.getNationalID())) {
								 g.writeInfo(c);
								 target=c;
								 btnUpdate();
								 }
						 for(Citizen c1 : chosen.getOccupants())
							 if(s.equals(c1.getNationalID())) {
								 g.writeInfo(c1);
								 target=c1;
								 btnUpdate(chosen);
								 flag= true;
							 }
						 
					}
					catch (NumberFormatException e2) {
						for(ResidentialBuilding rb : visibleBuildings)
							if(rb.getLocation().toString().equals(s)) {
								target=rb;
								chosen = rb;
								g.writeInfo(rb);
								flag=true;
								btnUpdate(rb);
							}
						
						
					}
				}
			}
		}
		if(!flag ) {
			btnUpdate();
		}
		} 
		catch(Exception e3) {
			g.write(e3.getMessage());
		}
	}
	private Citizen checkGridc(String s) {
		for(Citizen c : visibleCitizens)
			if(c.getLocation().toString().equals(s))
				return c;
		return null;
	}
	private ResidentialBuilding checkGridb(String s) {
		for(ResidentialBuilding b : visibleBuildings)
			if(b.getLocation().toString().equals(s))
				return b;
		return null;
	}
public void update(int b) {
//	System.out.println(engine.checkGameOver());
	g.getWorld().removeAll();
	for(int i =0;i<10;i++)
		for(int j=0;j<10;j++) {
			JButton ad =new JButton();
			ad.addActionListener(this);
			String s = i+ " "+j;
			Citizen c = checkGridc(s);
			ResidentialBuilding rb = checkGridb(s);
			ad.setSize(new Dimension(110,80));
			if(c!=null) {
				ad.setText(c.getNationalID());
				
				ImageIcon ic = new ImageIcon("cit.JPG");
				Image img = ic.getImage() ; 
				Image newimg = img.getScaledInstance( ad.getWidth(), ad.getHeight(),  Image.SCALE_SMOOTH ) ;  
				ic = new ImageIcon( newimg );
				ad.setIcon(ic);
			}
			else {
				if(rb!=null) {
					ad.setText(rb.getLocation().toString());
					
					//ad.setSize(new Dimension(110,80));
					ImageIcon ic = new ImageIcon("b.JPG");
					Image img = ic.getImage() ; 
					Image newimg = img.getScaledInstance( ad.getWidth(), ad.getHeight(),  Image.SCALE_SMOOTH ) ;  
					ic = new ImageIcon( newimg );
					ad.setIcon(ic);
				}
				else {
					ad.setText(i+" "+j);
					ImageIcon ic = new ImageIcon("em.JPG");
					Image img = ic.getImage() ; 
					Image newimg = img.getScaledInstance( ad.getWidth(), ad.getHeight(),  Image.SCALE_SMOOTH ) ;  
					ic = new ImageIcon( newimg );
					ad.setIcon(ic);
					}
			}
			
			g.addAdrress(ad);
			
		}
//	for(Citizen c : visibleCitizens) {
//		
//		JButton cbt = new JButton();
//		cbt.addActionListener(this);
//		cbt.setText(c.getNationalID());
//		cbt.setSize(new Dimension(100,100));
//		ImageIcon ic = new ImageIcon("cit.JPG");
////		Image img = ic.getImage() ;  
////		   Image newimg = img.getScaledInstance( cbt.getWidth(), cbt.getHeight(),  Image.SCALE_SMOOTH ) ;  
////		  ic = new ImageIcon( newimg );
//		cbt.setIcon(ic);
//		g.addCitizen(cbt);
//	}
//	for(ResidentialBuilding br : visibleBuildings) {
//		JButton bbt = new JButton();
//		bbt.setText(br.getLocation().toString());
//		bbt.addActionListener(this);
//		g.addBuilding(bbt);
//	}
	
	g.writeCacualties(engine.calculateCasualties(), b);
}
private void btnUpdate() {
	g.getSimBtns().removeAll();
	JButton Cu = new JButton("Next Cycle");
	JButton re = new JButton("Respond");
	Cu.addActionListener(this);
	re.addActionListener(this);
	Cu.setVisible(true);
	re.setVisible(true);
	g.getUBtns().setBackground(Color.white);

	g.getSimBtns().add(Cu);
	g.getSimBtns().add(re);
	
	g.pack();
}
private void btnUpdate(ResidentialBuilding rb) {
	g.getSimBtns().removeAll();
	JButton Cu = new JButton("Next Cycle");
	JButton re = new JButton("Respond");
	Cu.addActionListener(this);
	re.addActionListener(this);
	Cu.setVisible(true);
	re.setVisible(true);
	g.getUBtns().setBackground(Color.white);

	g.getSimBtns().add(Cu);
	g.getSimBtns().add(re);
	for(Citizen c : rb.getOccupants()) {
		JButton ad = new JButton();
		ad.setSize(new Dimension(100,100));
		ImageIcon ic = new ImageIcon("cit2.JPG");
		Image img = ic.getImage() ; 
		Image newimg = img.getScaledInstance( ad.getWidth(), ad.getHeight(),  Image.SCALE_SMOOTH ) ;  
		ic = new ImageIcon( newimg );
		ad.setIcon(ic);
		ad.addActionListener(this);
		ad.setText(c.getNationalID());
		ad.setPreferredSize(new Dimension(100,25));
		g.getSimBtns().add(ad);
}
	//g.getSimBtns().setVisible(true);
	g.pack();
}



}
