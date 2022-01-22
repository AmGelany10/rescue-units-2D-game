package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.infrastructure.ResidentialBuilding;


public class GasLeak extends Disaster {

	public GasLeak(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);
	}
	@Override
	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException 
	{
		
		ResidentialBuilding target= (ResidentialBuilding)getTarget();
		target.setGasLevel(target.getGasLevel()+10);
		super.strike();
		for(int i = 0 ; i<target.getOccupants().size();i++)
		{
			target.getOccupants().get(i).setOxygenLevel(target.getOccupants().get(i).getOxygenLevel()-15);
		}
	}
	@Override
	public void cycleStep() {
		ResidentialBuilding target= (ResidentialBuilding)getTarget();
		target.setGasLevel(target.getGasLevel()+15);
		for(int i = 0 ; i<target.getOccupants().size();i++)
		{
			target.getOccupants().get(i).setOxygenLevel(target.getOccupants().get(i).getOxygenLevel()-15);
		}
	}
	public String toString() {
		return "Gas Leak";
	}

}
