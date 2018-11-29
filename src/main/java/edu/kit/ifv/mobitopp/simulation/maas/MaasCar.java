package edu.kit.ifv.mobitopp.simulation.maas;

import edu.kit.ifv.mobitopp.simulation.Car;

public interface MaasCar
	extends Car {

	public MaasMobilityServiceProvider owner();

	public String forLogging();
	
	public int getNumberofAvailableSeats();
	
	public int getNumberofPassengers();
	
	public float getWaitingTimeToZone(int startingZone);
	
}

