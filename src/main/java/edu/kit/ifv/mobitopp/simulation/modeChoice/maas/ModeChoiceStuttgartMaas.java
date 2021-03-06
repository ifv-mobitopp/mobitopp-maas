package edu.kit.ifv.mobitopp.simulation.modeChoice.maas;

import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.kit.ifv.mobitopp.data.Zone;
import edu.kit.ifv.mobitopp.simulation.Mode;
import edu.kit.ifv.mobitopp.simulation.Person;
import edu.kit.ifv.mobitopp.simulation.activityschedule.ActivityIfc;
import edu.kit.ifv.mobitopp.simulation.ImpedanceIfc;
import edu.kit.ifv.mobitopp.simulation.modeChoice.ModeChoiceParameter;
import edu.kit.ifv.mobitopp.simulation.modeChoice.ModeChoiceModel;

import edu.kit.ifv.mobitopp.util.logit.LogitModel;
import edu.kit.ifv.mobitopp.util.logit.DefaultLogitModel;
import edu.kit.ifv.mobitopp.util.logit.LinearUtilityFunction;

public class ModeChoiceStuttgartMaas
	implements ModeChoiceModel 
{

	protected final ImpedanceIfc impedance;

	protected final ModeChoiceParameter modeChoiceParameter;

	protected final LogitModel<Mode> logitModel = new DefaultLogitModel<Mode>();

	protected final Map<Mode,LinearUtilityFunction> utilityFunctions;

	public ModeChoiceStuttgartMaas(
		ImpedanceIfc impedance,
		ModeChoiceParameter modeChoiceParameter
	) {

		this.impedance = impedance;
		this.modeChoiceParameter = modeChoiceParameter;

	 	this.utilityFunctions = Collections.unmodifiableMap(makeUtilityFunctions());
	}

	protected Map<Mode,LinearUtilityFunction> makeUtilityFunctions() {

		Collection<Mode> modes = Arrays.asList(new Mode[] { 
															Mode.PEDESTRIAN ,
															Mode.PUBLICTRANSPORT,
															Mode.PASSENGER,
															Mode.BIKE,
															Mode.CAR,
															//Mode.CARSHARING_STATION,
															//Mode.CARSHARING_FREE,
															Mode.RIDE_POOLING,
															Mode.AUTONOMOUS_TAXI
														});

		Map<Mode,LinearUtilityFunction> utilityFunctions = new LinkedHashMap<Mode,LinearUtilityFunction>();

		for (Mode mode : modes) {

			LinearUtilityFunction uF = new LinearUtilityFunction(modeChoiceParameter.parameterForMode(mode));

			utilityFunctions.put(mode,uF);
		}

		return utilityFunctions;
	}



	public Mode selectMode(
		Person person,
		Zone source,
		Zone destination,
		ActivityIfc previousActivity,
		ActivityIfc nextActivity,
		Set<Mode> choiceSet,
		double randomNumber
	) {

		Map<Mode,Double> utilities = calculateUtilities(person, source, destination,
																														previousActivity, nextActivity, choiceSet);


		return logitModel.select(utilities, choiceSet, randomNumber);
	}


	protected Map<Mode,Double> calculateUtilities(
		Person person,
		Zone sourceZone,
		Zone targetZone,
		ActivityIfc previousActivity,
		ActivityIfc nextActivity,
		Collection<Mode> choiceSet
	) {

		Set<Mode> modes = new LinkedHashSet<>(Arrays.asList(new Mode[] { 
														Mode.PEDESTRIAN ,
														Mode.PUBLICTRANSPORT,
														Mode.PASSENGER,
														Mode.BIKE,
														Mode.CAR,
														//Mode.CARSHARING_STATION,
														//Mode.CARSHARING_FREE,
														Mode.RIDE_POOLING,
														Mode.AUTONOMOUS_TAXI
														}));

		modes.retainAll(choiceSet);

		Map<Mode,Map<String,Double>> attributes = modeChoiceParameter.gatherAttributes(
																													person, modes, sourceZone, targetZone,
																													previousActivity, nextActivity, this.impedance);

		Map<Mode,Double> utilities = new LinkedHashMap<Mode, Double>(); 

		for (Mode mode : modes) {
			LinearUtilityFunction uf = utilityFunctions.get(mode);
			Double utility = uf.calculateUtility(attributes.get(mode));	
		
			utilities.put(mode, utility);
		}
	
		return utilities;
	}
	
}
