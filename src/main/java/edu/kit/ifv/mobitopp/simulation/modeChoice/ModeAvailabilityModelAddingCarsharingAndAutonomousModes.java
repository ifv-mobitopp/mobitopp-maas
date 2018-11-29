package edu.kit.ifv.mobitopp.simulation.modeChoice;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.kit.ifv.mobitopp.data.Zone;
import edu.kit.ifv.mobitopp.simulation.ImpedanceIfc;
import edu.kit.ifv.mobitopp.simulation.Mode;
import edu.kit.ifv.mobitopp.simulation.Person;
import edu.kit.ifv.mobitopp.simulation.activityschedule.ActivityIfc;
import edu.kit.ifv.mobitopp.simulation.carsharing.CarSharingCar;
import edu.kit.ifv.mobitopp.simulation.carsharing.CarSharingPerson;

public class ModeAvailabilityModelAddingCarsharingAndAutonomousModes
	extends BasicModeAvailabilityModel
	implements ModeAvailabilityModel
{

	public ModeAvailabilityModelAddingCarsharingAndAutonomousModes(
		ImpedanceIfc impedance
	) {
		super(impedance);
	}

	@Override
	public Set<Mode> availableModes(
		Person person,
		Zone currentZone,
		ActivityIfc previousActivity,
		Collection<Mode> allModes
	) {

		// get available modes from basic mode availability model
		Set<Mode> choiceSet = new LinkedHashSet<Mode>(
																super.availableModes(person, currentZone, previousActivity, allModes) 
													 );
		

		assert choiceSet != null;
		
		boolean modefixed = false;
		
		
		if (!(person instanceof CarSharingPerson)) {
			Mode previousMode = previousActivity.mode();
			if(previousMode==Mode.CAR || previousMode==Mode.BIKE) 
			{
				modefixed=true;
			}
		}

		if (person instanceof CarSharingPerson) {

			if (isAtHome(previousActivity)) 
			{

				if (person.hasDrivingLicense() && person.household().getNumberOfAvailableCars() == 0) {
	
					if(currentZone.carSharing().isStationBasedCarSharingCarAvailable((CarSharingPerson)person)) {
						choiceSet.add(Mode.CARSHARING_STATION);
					}

					if( currentZone.carSharing().isFreeFloatingCarSharingCarAvailable((CarSharingPerson)person)) {
						choiceSet.add(Mode.CARSHARING_FREE);
					}
				}
	
				if(previousActivity.isModeSet()
						&& previousActivity.mode()==Mode.CARSHARING_FREE &&  person.isCarDriver()) {
	
					assert !currentZone.carSharing().isFreeFloatingZone((CarSharingCar)person.whichCar());

					choiceSet = Collections.singleton(Mode.CARSHARING_FREE);
					modefixed=true;
				} 
			} 
			else 
			{
	
				Mode previousMode = previousActivity.mode();
	
				if(previousMode==Mode.CARSHARING_STATION) 
				{
						choiceSet = Collections.singleton(Mode.CARSHARING_STATION);
						modefixed=true;
				} 
				else if(previousMode==Mode.CARSHARING_FREE) 
				{
					if(person.isCarDriver()) 
					{
						assert !currentZone.carSharing().isFreeFloatingZone((CarSharingCar)person.whichCar());
		
						choiceSet = Collections.singleton(Mode.CARSHARING_FREE);
						modefixed=true;
					} 
					else 
					{
						choiceSet.remove(Mode.CAR);
						choiceSet.remove(Mode.BIKE);
		
						assert person instanceof CarSharingPerson : person.getClass();
		
						if(currentZone.carSharing().isFreeFloatingCarSharingCarAvailable((CarSharingPerson)person)) {
							choiceSet.add(Mode.CARSHARING_FREE);
						}
					}
				}
				else if(previousMode==Mode.CAR || previousMode==Mode.BIKE) 
				{
					modefixed=true;
				}
			}
		}
		
		if (!modefixed)
		{
			//TODO membership modeling!		
			choiceSet.add(Mode.AUTONOMOUS_TAXI);
			choiceSet.add(Mode.RIDE_POOLING);
		}

		return choiceSet;
	}
	
	

}
