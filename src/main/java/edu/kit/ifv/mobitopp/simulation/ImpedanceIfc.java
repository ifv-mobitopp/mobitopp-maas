package edu.kit.ifv.mobitopp.simulation;

import java.util.Optional;

import edu.kit.ifv.mobitopp.data.Zone;
import edu.kit.ifv.mobitopp.publictransport.connectionscan.PublicTransportRoute;
import edu.kit.ifv.mobitopp.time.Time;
import edu.kit.ifv.mobitopp.publictransport.model.Stop;
import edu.kit.ifv.mobitopp.simulation.maas.MaasCar;

public interface ImpedanceIfc
{


	public float getTravelTime(int origin, int destination, Mode mode, Time date);
	
	public float getTravelTime(int origin, int destination, Mode mode, Time date, MaasCar car);

	public Optional<PublicTransportRoute> getPublicTransportRoute(Location origin, Location destination, Mode mode, Time date);

	public float getTravelCost(int origin, int destination, Mode mode, Time date);
	
	public float getTravelCost(int origin, int destination, Mode mode, Time date, MaasCar car);

	public float getDistance(int origin, int destination);

	public float getParkingCost(int destination, Time date);

	public float getParkingStress(int destination, Time date);

	public float getConstant(int origin, int destination, Time date);

	public float getOpportunities(ActivityType activityType, int zoneOid);
	

	default public float getTravelTime(Zone origin, Zone destination, Mode mode, Time date) {		
		return getTravelTime(origin.getOid(), destination.getOid(), mode, date);
	}

	Optional<PublicTransportRoute> getPublicTransportRoute(Stop start, Stop end, Mode mode, Time date);

}
