package model.units;

import javax.swing.ImageIcon;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.GasLeak;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class GasControlUnit extends FireUnit {

	public GasControlUnit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}
	public boolean canTreat(Rescuable r) {
		
		ResidentialBuilding a = (ResidentialBuilding) r ;
		if(a.getStructuralIntegrity() == 0 || r.getDisaster() == null || ! r.getDisaster().isActive() ||!( a.getDisaster() instanceof GasLeak))
			return false;
		else
			return true;
		
		}
	
	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException{
		if(r instanceof ResidentialBuilding) {
		
		if(canTreat(r) )
		{
		if (getTarget() != null && getState() == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);
		}
		else
		{
			throw new CannotTreatException(this, r);
		}
		}
		else 
			throw new IncompatibleTargetException(this, r);
	}
	public void treat()  {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (canTreat(target)|| !( target.getDisaster().isActive()))
		{
			if (target.getStructuralIntegrity() == 0) {
				jobsDone();
				return;
			} else if (target.getGasLevel() > 0) 
				target.setGasLevel(target.getGasLevel() - 10);
	
			if (target.getGasLevel() == 0)
				jobsDone();
			for(int i = 0 ; i<target.getOccupants().size();i++)
			{
				target.getOccupants().get(i).setOxygenLevel(target.getOccupants().get(i).getOxygenLevel()+15);
			}
		}
	}
	@Override
	public ImageIcon getIcon() {
		ImageIcon gas = new ImageIcon("gas.JPG");
		return gas;
	}

}
