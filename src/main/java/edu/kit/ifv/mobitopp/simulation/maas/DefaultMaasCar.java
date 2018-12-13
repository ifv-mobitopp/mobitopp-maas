package edu.kit.ifv.mobitopp.simulation.maas;

import java.io.Serializable;
import java.util.ArrayList;

import edu.kit.ifv.mobitopp.simulation.Car;
import edu.kit.ifv.mobitopp.simulation.ImpedanceIfc;
import edu.kit.ifv.mobitopp.simulation.Mode;
import edu.kit.ifv.mobitopp.simulation.Person;
import edu.kit.ifv.mobitopp.simulation.car.CarDecorator;
import edu.kit.ifv.mobitopp.time.Time;



public class DefaultMaasCar extends CarDecorator implements MaasCar, Car, Serializable {


	private static final long serialVersionUID = 1L;
	
	protected final static float MAAS_CAR_DEFAULT_WAITING_TIME_MIN = 4.00f;		
	protected final static float MAAS_CAR_DEFAULT_ACCESS_TIME_MIN = 1.00f;	
	
	private MaasMobilityServiceProvider owner;
	private ArrayList<Person> currentlyBookedPassengers;
	
	
	public DefaultMaasCar(
		Car car,
		MaasMobilityServiceProvider owner
	) {
		super(car);
	
		this.owner = owner;
		this.currentlyBookedPassengers = new ArrayList<Person>();
		owner.registerMaasCar(this);
	}

	
	public MaasMobilityServiceProvider owner() {
		return this.owner;
	}


	public String forLogging() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("C; ");
		buffer.append(owner.name() + "; ");
		buffer.append("; ");
		buffer.append(car.forLogging());

		return buffer.toString();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
	
		buffer.append("(DefaultRidePoolingCar: ");
		buffer.append(car.id() + ",");
		buffer.append(car.carSegment() + ",");
		buffer.append(car.getType().substring(0,1).toUpperCase() + ",");
		buffer.append(car.maxRange() + ",");
		buffer.append(car.currentMileage() + ",");
		buffer.append(car.currentFuelLevel() + ",");
		buffer.append(owner.name() + ")");

		return buffer.toString();
	}
	
	public int getNumberofPassengers()
	{
		
		return currentlyBookedPassengers.size();
	}
	
	public int getNumberofAvailableSeats()
	{
		return capacity() - getNumberofPassengers();
	}
	
	public float getWaitingTimeToZone(int desiredStartingZone)
	{
		/*
		 * dummy coding to enable general functionality
		 * functionality needed to be dependent from actual position of all cars
		 */
		return MAAS_CAR_DEFAULT_WAITING_TIME_MIN;
	}
	
	
	public float getTravelCost(ImpedanceIfc impedance, int source, int target)
	{
		float cost=-1;
		float distanceKm = impedance.getDistance(source, target)/1000.0f;
		cost = owner().getCost_basefee_eur() + (owner().getCost_eur_per_km_solo()*distanceKm);
	
		assert cost>=0 : "no costs available";
		return cost;
	}
	
	
	public float getTravelTime(ImpedanceIfc impedance, int source, int target, Time date, Mode mode) 
	{
		float time = impedance.getTravelTime(source, target, mode, date);
	
		float accessTime = MAAS_CAR_DEFAULT_ACCESS_TIME_MIN;
		float waitingTime = getWaitingTimeToZone(source);
				
		time += accessTime + waitingTime;

		return time;
	}
	
}


