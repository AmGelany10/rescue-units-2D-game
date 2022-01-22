package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.management.loading.PrivateClassLoader;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.Border;

import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Evacuator;
import model.units.Unit;
import model.units.UnitState;
import simulation.Rescuable;
import simulation.Simulatable;

public class GUI  extends JFrame implements  model.events.log{
	private JPanel world;
	public JPanel getWorld() {
		return world;
	}
	
	private JPanel info;
	private JScrollPane inf;
	private	JScrollPane log ;
    
	// Force the scrollbars to always be displayed
	 
	
	//private JPanel cacual;
	private JTextArea txtinfo;
	private JTextArea casualties;
	private JTextArea loginfo;
	private JPanel UBtns ; 
	public JPanel getUBtns() {
		return UBtns;
	}
	public JPanel getSimBtns() {
		return simBtns;
	}
	private JPanel Btns;
	private JPanel simBtns;
	public GUI() {
		super();
		loginfo = new JTextArea();
		loginfo.setPreferredSize(new Dimension(200,400));
		loginfo.setEditable(false);
		loginfo.setBackground(Color.YELLOW);
		loginfo.setVisible(true);
		log=new JScrollPane(loginfo);
		log.setPreferredSize(new Dimension(200,400));
		log.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		log.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		loginfo.setText("Log: ");
		//loginfo.setVisible(true);
		//log.setLayout(new ScrollPaneLayout());
		//log.add(loginfo, new ScrollPaneLayout());
		
		//log.setBackground(Color.orange);
		//log.setVisible(true);
		
		setTitle("Rescue Simulation");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(1500,800));
		UBtns = new JPanel();
		world = new JPanel();
		info = new JPanel();
		Btns = new JPanel();
		simBtns = new JPanel();
		Btns.setLayout(new BorderLayout());
		Btns.setSize(new Dimension(200,800));
		//cacual = new JPanel();
		UBtns.setLayout(new GridLayout(0,2));
		world.setLayout(new GridLayout(0,10));
		info.setLayout(new BorderLayout(2,2));
		//cacual.setLayout(new FlowLayout());
		Btns.add(simBtns,BorderLayout.CENTER);
		//cacual.setPreferredSize(new Dimension(1500, 100));
		world.setPreferredSize(new Dimension(1100, 800)); 
		world.setBackground(Color.black);
		UBtns.setPreferredSize(new Dimension(200, 300));
		info.setPreferredSize(new Dimension(200, 800));
		txtinfo = new JTextArea();
		txtinfo.setPreferredSize(new Dimension(200, 400));
		txtinfo.setEditable(false);
		txtinfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		txtinfo.setText("Info");
		txtinfo.setBackground(Color.magenta);
		txtinfo.setForeground(Color.white);
		inf = new JScrollPane(txtinfo);
		info.add(log,BorderLayout.NORTH);
		info.add(inf,BorderLayout.SOUTH);
		info.setBackground(Color.blue);
		casualties= new JTextArea();
		//info.add(casualties,BorderLayout.SOUTH);
		casualties.setPreferredSize(new Dimension(200, 200));
		casualties.setEditable(false);
		casualties.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		casualties.setBackground(Color.blue);
		//casualties.setText("Hello");
		casualties.setForeground(Color.white);
		//cacual.add(casualties );
		info.setBackground(Color.black);
		world.setVisible(true);
		UBtns.setVisible(true);
		this.setVisible(true);
		Btns.add(UBtns,BorderLayout.NORTH);
		Btns.add(casualties,BorderLayout.SOUTH);
		this.getContentPane().add(info, BorderLayout.WEST);
		this.getContentPane().add(Btns, BorderLayout.EAST);
		this.getContentPane().add(world,BorderLayout.CENTER);
		//this.getContentPane().add(cacual,BorderLayout.SOUTH);
		
	}
	public void addUnit(JButton unit) {
		unit.setBackground(Color.black);
		UBtns.add(unit, new FlowLayout());
		
	}
	
	public void addAdrress(JButton ad) {
		ad.setBackground(Color.black);
		world.add(ad);
	}
	
	public void write(String s) {
		txtinfo.setText(s);
	}
	
	public void writeInfo(Simulatable r) {
		String s = "Info: "+"\n";
		if(r instanceof Citizen) {
			Citizen c = (Citizen) r;
			s += "Name: " +  c.getName() + "\n";
			s+= "Age: " + c.getAge()+"\n";
			s+= "ID: "+ c.getNationalID()+"\n";
			s+= "HP: "+c.getHp()+"\n";
			s+="State: "+c.getState()+"\n";
			if(c.getDisaster()!=null && c.getDisaster().isActive())
			s+= "Current Disaster: "+c.getDisaster()+"\n";
			s+= "Location: "+ "("+c.getLocation().getX()+","+c.getLocation().getY()+")"+"\n";
			s+= "Blood Loos: "+c.getBloodLoss()+"\n";
			s+="Toxicity: "+c.getToxicity()+"\n";
			}
		else 
			if(r instanceof Unit) {
				Unit t = (Unit) r;
				s+="ID: "+ t.getUnitID()+"\n";
				s+="Location: "+t.getLocation()+"\n";
				s+="Type: "+t+"\n";
				s+="Distance to target : "+t.getDistancetoTarget()+"\n";
				if(t.getTarget() !=null)
				s+="Target: "+t.getTarget().getLocation()+"\n";
				s+="State: "+t.getState()+"\n";
				if(t instanceof Evacuator) {
					Evacuator e = (Evacuator)t;
					s+="number of Passengers: "+e.getPassengers().size()+"\n";
					s+="Passengers Info: "+"\n";
					for(Citizen c : e.getPassengers()) {
						s+= "Name: " +  c.getName() + "\n";
						s+= "Age: " + c.getAge()+"\n";
						s+= "ID: "+ c.getNationalID()+"\n";
						s+= "HP: "+c.getHp()+"\n";
						s+="State: "+c.getState()+"\n";
						if(c.getDisaster()!=null && c.getDisaster().isActive())
						s+= "Current Disaster: "+c.getDisaster()+"\n";
						s+= "Location: "+ "("+c.getLocation().getX()+","+c.getLocation().getY()+")"+"\n";
						s+= "Blood Loos: "+c.getBloodLoss()+"\n";
						s+="Toxicity: "+c.getToxicity()+"\n";
					}
				}
				
			}
		else {
			ResidentialBuilding b = (ResidentialBuilding)r;
			s += "Location: "+b.getLocation()+"\n";
			s+= "Structural lIntegrity: "+b.getStructuralIntegrity()+"\n";
			s+= "Foundation Damage: "+b.getFoundationDamage()+"\n";
			if(b.getDisaster()!=null && b.getDisaster().isActive())
			s+="Current Disaster: "+b.getDisaster()+"\n";
			s+="Fire Damage: "+b.getFireDamage()+"\n";
			s+="Gas Level: "+ b.getGasLevel()+"\n";
			s+="Number Of Ocubants: "+b.getOccupants().size()+"\n"+"Occupants info: "+"\n";
			for(Citizen c : b.getOccupants()) {
				s+= "Name: " +  c.getName() + "\n";
				s+= "Age: " + c.getAge()+"\n";
				s+= "ID: "+ c.getNationalID()+"\n";
				s+= "HP: "+c.getHp()+"\n";
				s+="State: "+c.getState()+"\n";
				if(c.getDisaster()!=null && c.getDisaster().isActive())
				s+= "Current Disaster: "+c.getDisaster()+"\n";
				s+= "Location: "+c.getLocation()+"\n";
				s+= "Blood Loss: "+c.getBloodLoss()+"\n";
				s+="Toxicity: "+c.getToxicity()+"\n";
			}
			
		}
		txtinfo.setText(s);
	}
	public void writeCacualties(int n,int m) {
		String s="";
		 s+= "Current Cycle: " + m+"\n"; 
		 s += "Casualties so far: "+n;
		
		casualties.setText(s);
	}
public void calculateScore(int x,int y) {
		world.setVisible(false);
		Btns.setVisible(false);
		String s = "Your Score is "+"\n";
		s+= x +" Citizens died"+"\n";
		s+=y +" Citizens are alive"+"\n";
		txtinfo.setText(s);
		
		
		
	}
public void clear() {
	txtinfo.setText("Info");
}
public void updatelog(String s) {
	loginfo.setText(loginfo.getText()+"\n"+s);
	
}

}
