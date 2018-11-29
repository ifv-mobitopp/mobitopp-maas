package edu.kit.ifv.mobitopp.simulation.maas;

import java.io.Serializable;
import edu.kit.ifv.mobitopp.simulation.Car;

public class RidePoolingMaasCar extends DefaultMaasCar
		implements MaasCar, Car, Serializable {

	private static final long serialVersionUID = 1L;

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
}
