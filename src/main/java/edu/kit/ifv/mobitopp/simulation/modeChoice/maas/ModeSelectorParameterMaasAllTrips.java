package edu.kit.ifv.mobitopp.simulation.modeChoice.maas;


import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import edu.kit.ifv.mobitopp.data.Zone;
import edu.kit.ifv.mobitopp.simulation.Car;
import edu.kit.ifv.mobitopp.simulation.Household;
import edu.kit.ifv.mobitopp.simulation.ImpedanceIfc;
import edu.kit.ifv.mobitopp.simulation.Mode;
import edu.kit.ifv.mobitopp.simulation.Person;
import edu.kit.ifv.mobitopp.simulation.activityschedule.ActivityIfc;
import edu.kit.ifv.mobitopp.simulation.car.CarPosition;
import edu.kit.ifv.mobitopp.simulation.car.ConventionalCar;
import edu.kit.ifv.mobitopp.simulation.maas.*;
import edu.kit.ifv.mobitopp.simulation.modeChoice.ModeChoiceParameter;
import edu.kit.ifv.mobitopp.time.Time;
import edu.kit.ifv.mobitopp.util.parameter.ParameterFormularParser;

public class ModeSelectorParameterMaasAllTrips 
	implements ModeChoiceParameter
{

	public final Double ASC_WALK 	= Double.NaN;
	public final Double ASC_BIKE	= Double.NaN;
	public final Double ASC_PASS 	= Double.NaN;
	public final Double ASC_PT		= Double.NaN;
	public final Double ASC_CAR		= Double.NaN;
	public final Double ASC_ACS		= Double.NaN;
	public final Double ASC_DRS		= Double.NaN;
	
	public final Double SCALE 		= Double.NaN;
	public final Double TT 				= Double.NaN;
	public final Double TC 				= Double.NaN;
	

//	public final Double cost_km			= Double.NaN;
/*	
	public final Double PVM_WALK_WALK = Double.NaN;
	public final Double PVM_BIKE_BIKE	= Double.NaN;
	public final Double PVM_PASS_PASS	= Double.NaN;
	public final Double PVM_PT_PT			= Double.NaN;
		
	public final Double TC_PKW 	= Double.NaN;
	public final Double TC_PT 	= Double.NaN;
	public final Double TC_ACS	= Double.NaN;
	public final Double TC_DRS	= Double.NaN;

	public final Double PKWVERF_WALK	= Double.NaN;
	public final Double PKWVERF_BIKE	= Double.NaN;
	public final Double PKWVERF_PASS	= Double.NaN;
	public final Double PKWVERF_PT		= Double.NaN;
	public final Double PKWVERF_ACS		= Double.NaN;
	public final Double PKWVERF_DRS		= Double.NaN;

	public final Double ZEITKRT_WALK	= Double.NaN;
	public final Double ZEITKRT_BIKE	= Double.NaN;
	public final Double ZEITKRT_PASS	= Double.NaN;
	public final Double ZEITKRT_PT		= Double.NaN;
	public final Double ZEITKRT_ACS		= Double.NaN;
	public final Double ZEITKRT_DRS		= Double.NaN;
*/	

	private final Map<Mode,Map<String,Double>> parameters;

	public ModeSelectorParameterMaasAllTrips() {
		super();
		new ParameterFormularParser().parseConfig(defaultParameters(), this);
		this.parameters = Collections.unmodifiableMap(createParameterMaps());
	}
	
	private InputStream defaultParameters() {
		return ModeSelectorParameterMaasAllTrips.class
				.getResourceAsStream("default-mode-selector-parameter-first-trip.txt");
	}

	public ModeSelectorParameterMaasAllTrips(File file) {
		super();
		new ParameterFormularParser().parseConfig(file, this);
		this.parameters = Collections.unmodifiableMap(createParameterMaps());
	}

	private Map<Mode,Map<String,Double>> createParameterMaps() {

		Map<String,Double> parameterWalk 			= new LinkedHashMap<String,Double>();
		Map<String,Double> parameterBike 			= new LinkedHashMap<String,Double>();
		Map<String,Double> parameterPass 			= new LinkedHashMap<String,Double>();
		Map<String,Double> parameterCar 			= new LinkedHashMap<String,Double>();
		Map<String,Double> parameterPt 				= new LinkedHashMap<String,Double>();
		
		Map<String,Double> parameterRidepooling 		= new LinkedHashMap<String,Double>();
		Map<String,Double> parameterAutonomousTaxi	= new LinkedHashMap<String,Double>();
		
		
		

		// Walk
		parameterWalk.put("CONST", 								ASC_WALK);
/*		parameterWalk.put("COMMUTERTICKET_TRUE", 	ZEITKRT_WALK);
		parameterWalk.put("CARAVAILABLE_YES", 		PKWVERF_WALK);
		parameterWalk.put("LAST_MODE_WALK",				PVM_WALK_WALK);
*/
		parameterWalk.put("TIME", 		TT);

		
		// Bike
		parameterBike.put("CONST", 								ASC_BIKE);
/*		parameterBike.put("COMMUTERTICKET_TRUE", 	ZEITKRT_BIKE);
		parameterBike.put("CARAVAILABLE_YES", 		PKWVERF_BIKE);
		parameterBike.put("LAST_MODE_BIKE",				PVM_BIKE_BIKE);
*/
		parameterBike.put("TIME", 		TT);

		
		// Passenger
		parameterPass.put("CONST", 								ASC_PASS);
/*		parameterPass.put("COMMUTERTICKET_TRUE", 	ZEITKRT_PASS);
		parameterPass.put("CARAVAILABLE_YES", 		PKWVERF_PASS);
		parameterPass.put("LAST_MODE_PASS",				PVM_PASS_PASS);
*/
		parameterPass.put("TIME", 		TT);

		
		// Public Transport
		parameterPt.put("CONST", 								ASC_PT);
		parameterPt.put("COST", 							TC);
/*		parameterPt.put("COMMUTERTICKET_TRUE", 	ZEITKRT_PT);
		parameterPt.put("CARAVAILABLE_YES", 		PKWVERF_PT);
		parameterPt.put("LAST_MODE_PT",					PVM_PT_PT);
*/
		parameterPt.put("TIME", 		TT);


		// Car
		parameterCar.put("CONST", 							ASC_CAR);
		parameterCar.put("COST", 					TC);
		
		parameterCar.put("TIME", 		TT);
			
		// AutonomousTaxi / ACS
		parameterAutonomousTaxi.put("CONST", 								ASC_ACS*SCALE);
		parameterAutonomousTaxi.put("COST", 							TC*SCALE);
/*		parameterAutonomousTaxi.put("COMMUTERTICKET_TRUE", 	ZEITKRT_ACS*SCALE);
		parameterAutonomousTaxi.put("CARAVAILABLE_YES", 		PKWVERF_ACS*SCALE);
*/		
		parameterAutonomousTaxi.put("TIME", 		TT*SCALE);
	
		
		// Ridepooling / DRS
		parameterRidepooling.put("CONST", 							ASC_DRS*SCALE);
		parameterRidepooling.put("COST", 						TC*SCALE);
/*		parameterRidepooling.put("COMMUTERTICKET_TRUE", ZEITKRT_DRS*SCALE);
		parameterRidepooling.put("CARAVAILABLE_YES", 		PKWVERF_DRS*SCALE);
*/		
		parameterRidepooling.put("TIME", 		TT*SCALE);

		


		Map<Mode,Map<String,Double>> parameterForMode = new LinkedHashMap<Mode,Map<String,Double>>();

		parameterForMode.put(Mode.PEDESTRIAN, 				Collections.unmodifiableMap(parameterWalk));
		parameterForMode.put(Mode.BIKE, 							Collections.unmodifiableMap(parameterBike));
		parameterForMode.put(Mode.PASSENGER,					Collections.unmodifiableMap(parameterBike));
		parameterForMode.put(Mode.CAR, 								Collections.unmodifiableMap(parameterCar));
		parameterForMode.put(Mode.PUBLICTRANSPORT, 		Collections.unmodifiableMap(parameterPt));
		parameterForMode.put(Mode.RIDE_POOLING, 			Collections.unmodifiableMap(parameterRidepooling));
		parameterForMode.put(Mode.AUTONOMOUS_TAXI, 		Collections.unmodifiableMap(parameterAutonomousTaxi));
		
		return parameterForMode;
	}


	public Map<Mode,Map<String,Double>> gatherAttributes(
		Person person,
		Set<Mode> modes,
		Zone sourceZone,
		Zone targetZone,
		ActivityIfc previousActivity,
		ActivityIfc nextActivity,
		ImpedanceIfc impedance
	) {

		Time date = nextActivity.startDate();
		Household hh = person.household();
		
		int source = sourceZone.getOid();
		int target = targetZone.getOid();
		
		
		/*
		 * creation of cars need to shifted to other parts, only temporary as dummy coding to show functionality
		 */
		
		// 1. create MobilityServiceProviders with names and pricing schemes
		MaasMobilityServiceProvider drs_provider = new MaasMobilityServiceProvider("drs_dummyprovider", 1.0f, 0.45f, 0.0f);
		MaasMobilityServiceProvider acs_provider = new MaasMobilityServiceProvider("acs_dummyprovider", 1.0f, 0.45f, 0.0f);
	
		// 2. create car fleets for the given providers
		for (int i=1; i<=10; i++)
		{
			CarPosition dummy_car_position = new CarPosition(hh.homeZone(), hh.homeLocation());
			
			new RidePoolingMaasCar(
					new ConventionalCar(i, dummy_car_position, Car.Segment.MIDSIZE,5,0,1,10000),drs_provider
				);
			
			new DefaultMaasCar(
					new ConventionalCar(i, dummy_car_position, Car.Segment.MIDSIZE,5,0,1,10000),acs_provider
				);			
		}
		
		/*
		 * END of dummy coding to show car creation
		 */
		
		MaasCar ridepoolingCar = null;
		MaasCar automatedtaxiCar = null;		
				
		if (modes.contains(Mode.RIDE_POOLING))
		{
			if (drs_provider.isCarAvailableForZoneInGivenTime(source, 10))
			{
				ridepoolingCar = drs_provider.getNextAvailableCarForZone(source);
			}
			else
			{
				/*
				 *  no ridepooling vehicle is available in the given waiting time
				 *  check if method needs to be shifted to another location
				 */
				modes.remove(Mode.RIDE_POOLING);
			}
		}
				
		if (modes.contains(Mode.AUTONOMOUS_TAXI))
		{
			if (acs_provider.isCarAvailableForZoneInGivenTime(source, 10))
			{
				automatedtaxiCar = acs_provider.getNextAvailableCarForZone(source);
			}
			else
			{
				/*
				 *  no autonomous taxi vehicle is available in the given waiting time
				 *  check if method needs to be shifted to another location
				 */
				modes.remove(Mode.AUTONOMOUS_TAXI);
			}
		}
		
/*
		double caravailable_true = person.hasAccessToCar() ? 1 : 0;

		double commticket_true			= person.hasCommuterTicket() ? 1 : 0;

		double distance	= (float) Math.max(0.1,impedance.getDistance(source, target)/1000.0);
		
		double last_mode_car 	= previousActivity.mode() == Mode.CAR
				|| previousActivity.mode() == Mode.CARSHARING_STATION
				|| previousActivity.mode() == Mode.CARSHARING_FREE
				|| previousActivity.mode() == Mode.RIDE_POOLING
				|| previousActivity.mode() == Mode.AUTONOMOUS_TAXI 
																											? 1.0f : 0.0f;
		double last_mode_foot 	= previousActivity.mode() == Mode.PEDESTRIAN ? 1.0f : 0.0f;
		double last_mode_pt 		= previousActivity.mode() == Mode.PUBLICTRANSPORT ? 1.0f : 0.0f;
		double last_mode_pass 	= previousActivity.mode() == Mode.PASSENGER ? 1.0f : 0.0f;
		double last_mode_bike 	= previousActivity.mode() == Mode.BIKE ? 1.0f : 0.0f;
*/

		Map<String,Double> attributes = new LinkedHashMap<String,Double>();

		attributes.put("CONST", 	1.0);
/*			
		attributes.put("CARAVAILABLE_YES", 					caravailable_true);

		attributes.put("COMMUTERTICKET_TRUE", 			commticket_true);

		attributes.put("LAST_MODE_WALK",			last_mode_foot);
		attributes.put("LAST_MODE_BIKE",			last_mode_bike);
		attributes.put("LAST_MODE_CAR",				last_mode_car);
		attributes.put("LAST_MODE_PASSENGER",	last_mode_pass);
		attributes.put("LAST_MODE_PT",				last_mode_pt);
*/		

		Map<Mode,Map<String,Double>> modeAttributes = new LinkedHashMap<Mode,Map<String,Double>>();

		for (Mode mode : modes) {

			Map<String,Double> attrib = new LinkedHashMap<String,Double>(attributes);

			double time;
			
			switch (mode)
			{
				case RIDE_POOLING:
					time 	= ridepoolingCar.getTravelTime(impedance, source, target, date, mode);
					break;
				case AUTONOMOUS_TAXI:
					time 	= automatedtaxiCar.getTravelTime(impedance, source, target, date, mode);
					break;
				default:
					time 	= impedance.getTravelTime(source, target, mode, date);
					break;
			}

			
			double cost;
			
			switch (mode)
			{
				case PUBLICTRANSPORT:
					cost = person.hasCommuterTicket() ? 0.0 : impedance.getTravelCost(source, target, mode, date);
					break;
				case RIDE_POOLING:
					cost = ridepoolingCar.getTravelCost(impedance, source, target);
					break;
				case AUTONOMOUS_TAXI:
					cost = automatedtaxiCar.getTravelCost(impedance, source, target);
					break;					
				default:
					cost = impedance.getTravelCost(source, target, mode, date);
					break;
			}
		
		 	attrib.put("TIME", 		time);
			attrib.put("COST", cost);

			modeAttributes.put(mode, attrib);
		}

		return  modeAttributes;
	}

	public Map<String,Double> parameterForMode(Mode mode) {
	
		assert this.parameters.containsKey(mode);

		return this.parameters.get(mode);
	}
}
