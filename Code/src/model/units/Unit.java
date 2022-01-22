package model.units;

import javax.swing.ImageIcon;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Disaster;
import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable, SOSResponder {
	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	public int stepsPerCycle;
	private WorldListener worldListener;

	public Unit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		this.unitID = unitID;
		this.location = location;
		this.stepsPerCycle = stepsPerCycle;
		this.state = UnitState.IDLE;
		this.worldListener = worldListener;
	}
	public  ImageIcon getIcon() {
		return null;
	}
	public boolean canTreat(Rescuable r)
	{
		if(r instanceof Citizen)
		{
			Citizen a = (Citizen) r;
			if(a.getState() == CitizenState.SAFE )
			{
				return false;
			}
			else
				return true;
		}else
		{
		ResidentialBuilding a = (ResidentialBuilding) r ;
		if(a.getStructuralIntegrity() == 0 || r.getDisaster() == null || ! r.getDisaster().isActive())
			return false;
		else
			return true;
		}
	}
	public void setWorldListener(WorldListener listener) {
		this.worldListener = listener;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getUnitID() {
		return unitID;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}

	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}

	@Override
	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException{
		if(canTreat(r))
		{
		if (target != null && state == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);
		}
		else
		{
			throw new CannotTreatException(this, r);
		}

	}

	public void reactivateDisaster() {
		Disaster curr = target.getDisaster();
		curr.setActive(true);
	}

	public void finishRespond(Rescuable r) {
		target = r;
		state = UnitState.RESPONDING;
		Address t = r.getLocation();
		distanceToTarget = Math.abs(t.getX() - location.getX())
				+ Math.abs(t.getY() - location.getY());

	}

	public abstract void treat() throws CannotTreatException, IncompatibleTargetException;

	public void cycleStep() throws CannotTreatException, IncompatibleTargetException {
		if (state == UnitState.IDLE)
			return;
		if (distanceToTarget > 0) {
			distanceToTarget = distanceToTarget - stepsPerCycle;
			if (distanceToTarget <= 0) {
				distanceToTarget = 0;
				Address t = target.getLocation();
				worldListener.assignAddress(this, t.getX(), t.getY());
			}
		} else {
			state = UnitState.TREATING;
			treat();
		}
	}

	public void jobsDone() {
		target = null;
		state = UnitState.IDLE;

	}
	public String toString() {
		if(this instanceof Evacuator)
			return "Evacuator";
		if(this instanceof DiseaseControlUnit)
			return "DiseaseControlUnit";
		if(this instanceof Ambulance)
			return "Ambulance";
		if(this instanceof GasControlUnit)
			return "GasControlUnit";
		else 
			return "FIre Truck" ;
			
		
	}
	public int getDistancetoTarget() {
	return distanceToTarget;
	}
}
