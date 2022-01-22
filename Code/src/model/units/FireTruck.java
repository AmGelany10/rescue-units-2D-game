package model.units;

import javax.swing.ImageIcon;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Fire;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class FireTruck extends FireUnit {

	public FireTruck(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	} 
	public boolean canTreat(Rescuable r) {
		ResidentialBuilding a = (ResidentialBuilding) r ;
		if(a.getStructuralIntegrity() == 0 || r.getDisaster() == null || ! r.getDisaster().isActive() ||!( a.getDisaster() instanceof Fire))
			return false;
		else
			return true;
		
		}
	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException{
	
		if(r instanceof ResidentialBuilding)
		{
			if(canTreat(r))
			{
			if (getTarget() != null && getState() == UnitState.TREATING)
				reactivateDisaster();
			finishRespond(r);
			}
			else
			{
				throw new CannotTreatException(this, r);
		}
		}else 
		{
			throw new IncompatibleTargetException(this, r);
		}

	}
	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (canTreat(target) || !target.getDisaster().isActive())
		{
			if (target.getStructuralIntegrity() == 0) {
				jobsDone();
				return;
			} else if (target.getFireDamage() > 0)
	
				target.setFireDamage(target.getFireDamage() - 10);
	
			if (target.getFireDamage() == 0)
	
				jobsDone();
		}

	}
	@Override
	public ImageIcon getIcon() {
		ImageIcon fire  = new ImageIcon("fire.JPG");
		return fire;
	}

}
