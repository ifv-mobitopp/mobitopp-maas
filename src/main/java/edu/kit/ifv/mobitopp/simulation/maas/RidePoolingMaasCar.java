package edu.kit.ifv.mobitopp.simulation.maas;

import java.io.Serializable;
import edu.kit.ifv.mobitopp.simulation.Car;
import edu.kit.ifv.mobitopp.simulation.ImpedanceIfc;
import edu.kit.ifv.mobitopp.simulation.Mode;
import edu.kit.ifv.mobitopp.time.Time;

public class RidePoolingMaasCar extends DefaultMaasCar
		implements MaasCar, Car, Serializable {

	private static final long serialVersionUID = 1L;
	
	protected final static float MAAS_CAR_RIDEPOOLING_DETOURFACTOR = 1.2f;	

	/*
	 * only temporary as long as number of booked passengers is not yet modeled using 
	 * ArrayList<Person> currentlyBookedPassengers from DefaultMaasCar
	 */
	private int actualnumberofpassengers=0;

	public RidePoolingMaasCar(
		Car car,
		MaasMobilityServiceProvider owner
	) {
		super(car, owner);
		
		/*
		 * Only for testing as long as "live availability" is not modeled 
		 * random number of passengers between 0 and maximal capacity-1
		 * as we suppose that the given vehicle has at least one seat available otherwise
		 * it would not have been suggested to use
		 */
		this.actualnumberofpassengers = (int) ((this.capacity()-1)*Math.random());
	}
	
	@Override
	public int getNumberofPassengers()
	{
		return this.actualnumberofpassengers;
	}
	
	@Override
	public float getTravelCost(ImpedanceIfc impedance, int source, int target)
	{
		float cost=-1;
		float distanceKm = impedance.getDistance(source, target)/1000.0f;
		
		int numberofpassengersinclperson = 1 + getNumberofPassengers();
		cost = owner().getCost_basefee_eur() + Math.max(owner().getCost_eur_per_km_minimum(),((owner().getCost_eur_per_km_solo()*distanceKm) / numberofpassengersinclperson));
	
		assert cost>=0 : "no costs available";
		return cost;
	}
	
	@Override
	public float getTravelTime(ImpedanceIfc impedance, int source, int target, Time date, Mode mode) 
	{
		float time = impedance.getTravelTime(source, target, mode, date) * MAAS_CAR_RIDEPOOLING_DETOURFACTOR;
	
		float accessTime = MAAS_CAR_DEFAULT_ACCESS_TIME_MIN;
		float waitingTime = getWaitingTimeToZone(source);
				
		time += accessTime + waitingTime;

		return time;
	}
	
}
