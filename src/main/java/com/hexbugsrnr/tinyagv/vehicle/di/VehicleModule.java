package com.hexbugsrnr.tinyagv.vehicle.di;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;
import com.hexbugsrnr.tinyagv.util.ObservableQueue;
import com.hexbugsrnr.tinyagv.vehicle.VehicleDispatcher;
import com.hexbugsrnr.tinyagv.vehicle.state.Direction;
import com.hexbugsrnr.tinyagv.vehicle.protocol.*;
import com.hexbugsrnr.tinyagv.vehicle.protocol.interfaces.CommandStateHandler;
import com.hexbugsrnr.tinyagv.vehicle.protocol.interfaces.Message;
import com.hexbugsrnr.tinyagv.vehicle.state.VehicleState;
import com.hexbugsrnr.tinyagv.vehicle.state.MovingVehicleState;

/**
 * Created by null on 19/06/16.
 */
public class VehicleModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		install(new FactoryModuleBuilder().build(CommandQueueFactory.class));

		install(new FactoryModuleBuilder().build(VehicleFactory.class));

		bind(new TypeLiteral<ObservableQueue<VehicleStatus>>() {}).toProvider(ObservableQueueProvider.class).in(Singleton.class);

		bind(VehicleDispatcher.class).toProvider(VehicleDispatcherProvider.class).in(Singleton.class);

		MapBinder<Class<? extends Message>, CommandStateHandler> commandStateHandler =
				MapBinder.newMapBinder(binder(), new TypeLiteral<Class<? extends Message>>() {}, new TypeLiteral<CommandStateHandler>() {});

		commandStateHandler.addBinding(StartVehicle.class).to(StartCommandHandler.class);

		commandStateHandler.addBinding(StopVehicle.class).toInstance((String id, Message m, VehicleState s, ObservableQueue<VehicleStatus> q) -> {
			if(s instanceof MovingVehicleState)
				return ((MovingVehicleState) s).stop();
			else
				return s;
		});

		commandStateHandler.addBinding(SetDirection.class).toInstance((String id, Message m, VehicleState s, ObservableQueue<VehicleStatus> q) -> {
			Direction d = ((SetDirection) m).getDirection();
			s.setDirection(d);
			return s;
		});

		commandStateHandler.addBinding(DestroyVehicle.class).toInstance((String id, Message m, VehicleState s, ObservableQueue<VehicleStatus> q) -> {
			if(s instanceof MovingVehicleState)
				// In order to stop the ScheduleExecutorService calculating vehicle position
				((MovingVehicleState) s).stop();
			return null;
		});
	}
}
