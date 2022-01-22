package exceptions;

import simulation.Rescuable;
import model.units.Unit;

public class IncompatibleTargetException extends UnitException
{
	public IncompatibleTargetException(Unit unit, Rescuable target)
	{
		super(unit,target);
	}
	public IncompatibleTargetException(Unit unit, Rescuable target, String message)
	{
		super(unit, target, message);
	}
	
}
