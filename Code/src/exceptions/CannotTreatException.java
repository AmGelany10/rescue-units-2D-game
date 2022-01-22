package exceptions;

import simulation.Rescuable;
import model.units.Unit;

public class CannotTreatException extends UnitException
{
	public CannotTreatException(Unit unit, Rescuable target)
	{
		super(unit, target);
	}
	public CannotTreatException(Unit unit, Rescuable target, String message)
	{
		super(unit, target, message);
	}
	
}
