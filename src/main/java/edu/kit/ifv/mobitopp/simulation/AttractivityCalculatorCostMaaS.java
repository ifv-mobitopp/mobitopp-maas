package edu.kit.ifv.mobitopp.simulation;

import java.util.Map;

import edu.kit.ifv.mobitopp.data.Zone;
import edu.kit.ifv.mobitopp.time.Time;

public class AttractivityCalculatorCostMaaS extends AttractivityCalculatorCostNextPole
		implements AttractivityCalculatorIfc {

	private final ImpedanceIfc impedance;

	public AttractivityCalculatorCostMaaS(Map<Integer, Zone> zones, ImpedanceIfc impedance, String filename,
			float poleSensitivity) {

		super(zones, impedance, filename, poleSensitivity);
		this.impedance = impedance;

	}

	protected float getTravelCost(Mode mode, int sourceZoneOid, int targetZoneOid, Time date, boolean commmuterTicket) {

		/*
		 * TODO
		 * 
		 * Keep car cost in future? We should check to change this to the cost depending
		 * on the concrete vehicle. problem: vehicle is still unknown in destination
		 * choice!
		 */
		if (mode == Mode.AUTONOMOUS_TAXI || mode == Mode.RIDE_POOLING) {
			return this.impedance.getTravelCost(sourceZoneOid, targetZoneOid, Mode.CAR, date);
		}

		return super.getTravelCost(mode, targetZoneOid, sourceZoneOid, date, commmuterTicket);
	}

	protected float getParkingCost(Mode mode, int targetZoneOid, Time date, int duration) {

		if (mode == Mode.RIDE_POOLING || mode == Mode.AUTONOMOUS_TAXI) {
			return 0.0f;
		}

		return super.getParkingCost(mode, targetZoneOid, date, duration);
	}

}
