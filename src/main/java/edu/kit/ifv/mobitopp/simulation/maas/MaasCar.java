package edu.kit.ifv.mobitopp.simulation.maas;

import edu.kit.ifv.mobitopp.simulation.Car;
import edu.kit.ifv.mobitopp.simulation.ImpedanceIfc;
import edu.kit.ifv.mobitopp.simulation.Mode;
import edu.kit.ifv.mobitopp.time.Time;

public interface MaasCar
	extends Car {

	public MaasMobilityServiceProvider owner();

	public String forLogging();
	
	public int getNumberofAvailableSeats();
	
	public int getNumberofPassengers();
	
	public float getWaitingTimeToZone(int startingZone);
	
	public float getTravelCost(ImpedanceIfc impedance, int source, int target);
	
	public float getTravelTime(ImpedanceIfc impedance, int source, int target, Time date, Mode mode);
	
}

