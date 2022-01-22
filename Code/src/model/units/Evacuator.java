package model.units;

import javax.swing.ImageIcon;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class Evacuator extends PoliceUnit {

	public Evacuator(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener, int maxCapacity) {
		super(unitID, location, stepsPerCycle, worldListener, maxCapacity);

	}
	
	public boolean canTreat(Rescuable r) {
	ResidentialBuilding a = (ResidentialBuilding) r ;
	if(a.getStructuralIntegrity() == 0 || r.getDisaster() == null || ! r.getDisaster().isActive() ||!( a.getDisaster() instanceof Collapse))
		return false;
	else
		return true;
	
	}
	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException{
		if(r instanceof ResidentialBuilding)
		{
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
		{
			throw new IncompatibleTargetException(this, getTarget());
		}
	}
	@Override
	public void treat() throws CannotTreatException, IncompatibleTargetException {
		
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
			if (target.getStructuralIntegrity() == 0
					|| target.getOccupants().size() == 0) {
				jobsDone();
				return;
			}
	
			for (int i = 0; getPassengers().size() != getMaxCapacity()
					&& i < target.getOccupants().size(); i++) {
				getPassengers().add(target.getOccupants().remove(i));
				i--;
			}
	
			setDistanceToBase(target.getLocation().getX()
					+ target.getLocation().getY());
			
		
			
	}

	@Override
	public ImageIcon getIcon() {
		ImageIcon eva= new ImageIcon("evac.JPG");
		return eva;
	}

}
