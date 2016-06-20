package com.hexbugsrnr.tinyagv.vehicle.di;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.hexbugsrnr.tinyagv.vehicle.VehicleDispatcher;

/**
 * Created by null on 20/06/16.
 */
public class VehicleDispatcherProvider implements Provider<VehicleDispatcher>
{
	@Inject VehicleFactory vehicleFactory;

	@Inject CommandQueueFactory commandQueueFactory;

	@Override
	public VehicleDispatcher get()
	{
		return new VehicleDispatcher(vehicleFactory, commandQueueFactory);
	}
}
