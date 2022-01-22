package model.units;

import java.lang.annotation.Target;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.events.WorldListener;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;

public abstract class MedicalUnit extends Unit {
	private int healingAmount;
	private int treatmentAmount;
	public MedicalUnit(String unitID, Address location, int stepsPerCycle,WorldListener worldListener) {
		super(unitID, location, stepsPerCycle,worldListener);
		healingAmount=10;
		treatmentAmount=10;
	}
	
	public int getTreatmentAmount() {
		return treatmentAmount;
	}
	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException{
		if(r instanceof Citizen)
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
			throw new IncompatibleTargetException(this, r);

	}
	public  void heal() 
	{
		if (getTarget() instanceof Citizen)
		{
		Citizen target = (Citizen)getTarget();
		if(target.getHp()<100)
			target.setHp(target.getHp()+healingAmount);
		
		
		if(target.getHp() == 100)	
			jobsDone();
		}
	}
	
	
}
