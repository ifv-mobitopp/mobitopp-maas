package edu.kit.ifv.mobitopp.simulation.maas;

import java.util.List;
import java.util.ArrayList;

import java.io.Serializable;


public class MaasMobilityServiceProvider extends BaseMobilityServiceProvider 
	implements MobilityServiceProvider 
	, Serializable
{
	public final static long serialVersionUID =  -8230694404457066663L;

	public List<MaasCar> ownedCars = new ArrayList<MaasCar>();

	private float cost_basefee_eur	 			= Float.NaN;
	private float cost_eur_per_km_solo		= Float.NaN;
	private float cost_eur_per_km_minimum	= Float.NaN;
	

	public MaasMobilityServiceProvider(String name, float cost_basefee_eur, float cost_eur_per_km_solo, float cost_eur_per_km_minimum) {
		super(name);
		this.cost_basefee_eur = cost_basefee_eur;
		this.cost_eur_per_km_solo = cost_eur_per_km_solo;
		this.cost_eur_per_km_minimum = cost_eur_per_km_minimum;
	}

	
	public void registerMaasCar(MaasCar car) {
		ownedCars.add(car);
	}
	
	public boolean isCarAvailableForZoneInGivenTime(int zone, float maximalWaitingTime)
	{
		/*
		 * dummy coding to enable general functionality
		 * functionality needed to be dependent from actual position of all cars and 
		 * current occupancy rate of these vehicles.
		 */
		double random = Math.random();
		return (random>0.5 ? true : false);
	}
	
	public MaasCar getNextAvailableCarForZone(int zone)
	{
		/*
		 * dummy coding to enable general functionality
		 * functionality needed to be dependent from actual position of all cars
		 */
		int randomindex = (int) Math.random()*ownedCars.size();
		return ownedCars.get(randomindex);
	}


	public float getCost_basefee_eur() {
		return cost_basefee_eur;
	}

	public float getCost_eur_per_km_solo() {
		return cost_eur_per_km_solo;
	}
	
	public float getCost_eur_per_km_minimum() {
		return cost_eur_per_km_minimum;
	}

}

