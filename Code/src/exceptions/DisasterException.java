package exceptions;

import model.disasters.Disaster;

abstract public class DisasterException  extends SimulationException
{
	private Disaster disaster;
	public DisasterException(Disaster disaster)
	{
		super();
		this.disaster = disaster;
	}
	public DisasterException(Disaster disaster,String message)
	{
		super(message);
		this.disaster = disaster;
	}
	public Disaster getDisaster() 
	{
		return disaster;
	}
}
