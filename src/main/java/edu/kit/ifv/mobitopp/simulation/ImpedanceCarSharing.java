package edu.kit.ifv.mobitopp.simulation;

import java.util.Optional;

import edu.kit.ifv.mobitopp.publictransport.connectionscan.PublicTransportRoute;
import edu.kit.ifv.mobitopp.publictransport.model.Stop;
import edu.kit.ifv.mobitopp.simulation.maas.MaasCar;
import edu.kit.ifv.mobitopp.time.Time;

public class ImpedanceCarSharing
	implements ImpedanceIfc 
{

	protected final static float MAAS_DEFAULT_ACCESS_TIME_MIN = 1.00f;	
	
	protected final static float MAAS_DEFAULT_DETOURFACTOR = 1.2f;	
	

	protected final static float CARSHARING_COST_FREE_FLOATING_EUR_PER_MINUTE = 0.29f;
	protected final static float CARSHARING_COST_FREE_FLOATING_EUR_PER_HOUR = 14.9f;
	protected final static float CARSHARING_COST_FREE_FLOATING_EUR_PER_DAY = 59.0f;

	public final static float CARSHARING_COST_STATION_BASED_EUR_PER_KM = 0.23f;
	public final static float CARSHARING_COST_STATION_BASED_EUR_PER_HOUR = 2.80f;
	public final static float CARSHARING_COST_STATION_BASED_EUR_PER_BOOKING = 0.0f;

	protected final static float CARSHARING_BOOKING_TIME_STATION_BASED = 0.0f;	

	private ImpedanceIfc impedance;


	public ImpedanceCarSharing(
		ImpedanceIfc impedance
	) {
		this.impedance = impedance;
	}

	public float getDistance(int source, int target) {
		return this.impedance.getDistance(source, target);
	}

	public float getConstant(int source, int target, Time date) {
		return this.impedance.getConstant(source, target, date);
	}

	public float getParkingCost(int target, Time date) {
		return this.impedance.getParkingCost(target, date);
	}

	public float getParkingStress(int target, Time date) {
		return this.impedance.getParkingStress(target, date);
	}

	public float getOpportunities(ActivityType activityType, int zoneOid) {	
		return this.impedance.getOpportunities(activityType, zoneOid);
	}


	public float getTravelTime(int source, int target, Mode mode, Time date) {

		if (mode == Mode.CARSHARING_FREE) {
			float time = this.impedance.getTravelTime(source, target, Mode.CAR, date);

			float parkingStress = getParkingStress(target, date);

			float accessTime = 11.0f - 0.5f*parkingStress;

			time += accessTime;

			assert accessTime >= 2.0f;
			assert accessTime <= 11.0f;
	
			return time;

		} else if (mode == Mode.CARSHARING_STATION) {
			float time = this.impedance.getTravelTime(source, target, Mode.CAR, date);

			float parkingStress = getParkingStress(target, date);

			float accessTime = 14.0f - 0.5f*parkingStress;

			time += accessTime + CARSHARING_BOOKING_TIME_STATION_BASED;

			assert accessTime >= 5.0f;
			assert accessTime <= 14.0f;

			return time;

		}	else {

			return this.impedance.getTravelTime(source, target, mode, date);
		}
	}
	
	public float getTravelTime(int source, int target, Mode mode, Time date, MaasCar car) {

		if (mode == Mode.RIDE_POOLING) {
			//TODO Shift this functionality to maas car class as travel time is dependent on the actual car
			float time = this.impedance.getTravelTime(source, target, Mode.RIDE_POOLING, date) * MAAS_DEFAULT_DETOURFACTOR;
		
			float accessTime = MAAS_DEFAULT_ACCESS_TIME_MIN;
			float waitingTime = car.getWaitingTimeToZone(source);
					
			time += accessTime + waitingTime;
	
			return time;
			
		} else if (mode == Mode.AUTONOMOUS_TAXI) {
			//TODO Shift this functionality to maas car class as travel time is dependent on the actual car
			float time = this.impedance.getTravelTime(source, target, Mode.AUTONOMOUS_TAXI, date);
		
			float accessTime = MAAS_DEFAULT_ACCESS_TIME_MIN;
			float waitingTime = car.getWaitingTimeToZone(source);
			
			time += accessTime + waitingTime;
			
			return time;
		}
			else {
				new RuntimeException("method call not available for this mode!");
				return 0;
		}
	}
	
	@Override
	public Optional<PublicTransportRoute> getPublicTransportRoute(Location source, Location target, Mode mode, Time date) {
		return Optional.empty();
	}

	@Override
	public Optional<PublicTransportRoute> getPublicTransportRoute(Stop start, Stop end, Mode mode, Time date) {
		return Optional.empty();
	}

	public float getTravelCost(int source, int target, Mode mode, Time date) { 

		if (mode == Mode.CARSHARING_STATION) {

			float distanceKm = getDistance(source, target)/1000.0f;
			float timeMinutes = getTravelTime(source, target, Mode.CAR, date);

			float billableDistanceKm = (float) Math.ceil(distanceKm);
			float billableTimeHours = (float) Math.ceil(timeMinutes/60.0);

			return CARSHARING_COST_STATION_BASED_EUR_PER_BOOKING
						+ CARSHARING_COST_STATION_BASED_EUR_PER_KM*billableDistanceKm
						+ CARSHARING_COST_STATION_BASED_EUR_PER_HOUR*billableTimeHours;

		} else if (mode == Mode.CARSHARING_FREE) {

			float time = getTravelTime(source, target, Mode.CAR, date);

			float cost = freeFloatingCost(time);

			return cost;

		} else {

			return this.impedance.getTravelCost(source, target, mode, date);
		}
	}
	
	public float getTravelCost(int source, int target, Mode mode, Time date, MaasCar car) { 

		float cost=-1;
		
		if (mode == Mode.RIDE_POOLING) {
			float distanceKm = getDistance(source, target)/1000.0f;
			cost = ridepoolingcosts(distanceKm, car);
			
		} else if (mode == Mode.AUTONOMOUS_TAXI) {
			float distanceKm = getDistance(source, target)/1000.0f;
			cost = autonomoustaxicosts(distanceKm, car);

		}	else {
			new RuntimeException("method call not available for this mode!");
		}
		
		assert cost>=0 : "no costs available";
		return cost;
		
	}

	private float freeFloatingCost(float time) {

		float cost;

		if (time < 60) {
			cost = CARSHARING_COST_FREE_FLOATING_EUR_PER_MINUTE*time;
		} else if (time >= 60 && time < 1440) {
			cost = CARSHARING_COST_FREE_FLOATING_EUR_PER_HOUR*time/60.0f;
		} else if (time >= 1440) {
			cost = CARSHARING_COST_FREE_FLOATING_EUR_PER_DAY*time/1440.0f;
		} else {
			throw new AssertionError();
		}

		return cost;
	}
	
	private float ridepoolingcosts(float distanceKm, MaasCar car) {

		float cost;

		int numberofpassengersinclperson = 1 + car.getNumberofPassengers();
		cost = car.owner().getCost_basefee_eur() + Math.max(car.owner().getCost_eur_per_km_minimum(),((car.owner().getCost_eur_per_km_solo()*distanceKm) / numberofpassengersinclperson));

		return cost;
	}
	
	private float autonomoustaxicosts(float distanceKm, MaasCar car) {

		return car.owner().getCost_basefee_eur() + (car.owner().getCost_eur_per_km_solo()*distanceKm);
		
	}

}
