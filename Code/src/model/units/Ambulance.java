package model.units;

import javax.swing.ImageIcon;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class Ambulance extends MedicalUnit {
	
	public Ambulance(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
		
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);

		Citizen target = (Citizen) getTarget();
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getBloodLoss() > 0) {
			target.setBloodLoss(target.getBloodLoss() - getTreatmentAmount());
			if (target.getBloodLoss() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getBloodLoss() == 0)

			heal();

	}
	public boolean canTreat(Rescuable r)
	{
		Citizen c = (Citizen)r;
		if(c.getState()==CitizenState.SAFE || c.getDisaster() instanceof Infection)
			return false ;
		return true;
	}
	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException {
		
			if(r instanceof Citizen)
			{
				if(canTreat(r))
				{
				if (getTarget() != null && ((Citizen) getTarget()).getBloodLoss() > 0
						&& getState() == UnitState.TREATING)
					reactivateDisaster();
				finishRespond(r);
				}else 
				{
					throw new CannotTreatException(this, r);
				}
			}else
			{
				throw new IncompatibleTargetException(this, getTarget());
			}
		
	}

	@Override
	public ImageIcon getIcon() {
		ImageIcon amb = new ImageIcon("amp.JPG");
		return amb;
	}

}
