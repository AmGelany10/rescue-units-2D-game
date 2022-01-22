package simulation;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;

public interface Simulatable {
public void cycleStep() throws CannotTreatException, IncompatibleTargetException;


}
