package com.hexbugsrnr.tinyagv;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.hexbugsrnr.tinyagv.util.ObservableQueue;
import com.hexbugsrnr.tinyagv.vehicle.VehicleDispatcher;
import com.hexbugsrnr.tinyagv.vehicle.di.VehicleModule;
import com.hexbugsrnr.tinyagv.vehicle.protocol.*;
import com.hexbugsrnr.tinyagv.vehicle.state.Direction;
import rx.Observer;

/**
 * Created by null on 19/06/16.
 */
public class WebMain
{
	public static void main(String[] args)
	{
		//get("/hello", (req, res) -> "Hello World");

		Injector injector = Guice.createInjector(new VehicleModule());
		VehicleDispatcher dispatcher = injector.getInstance(VehicleDispatcher.class);

		System.out.println(dispatcher.toString());

		ObservableQueue<VehicleStatus> eventQueue = injector.getInstance(Key.get(new TypeLiteral<ObservableQueue<VehicleStatus>>(){}));

		if (eventQueue != null)
			eventQueue.asObservable().subscribe(new Observer<VehicleStatus>()
			{
				@Override
				public void onCompleted()
				{
					System.out.println("Observable completed.");
				}

				@Override
				public void onError(Throwable e)
				{
					System.out.println("Fuckage: " + e.getMessage());
				}

				@Override
				public void onNext(VehicleStatus vehicleStatus)
				{
					System.out.println(vehicleStatus.toString());
				}
			});


		String v1 = dispatcher.createNewVehicle();
		String v2 = dispatcher.createNewVehicle();

		dispatcher.getVehicleCommandQueue(v1).offer(new SetDirection(Direction.EAST));
		dispatcher.getVehicleCommandQueue(v2).offer(new SetDirection(Direction.SOUTHEAST));
		dispatcher.getVehicleCommandQueue(v1).offer(new StartVehicle());
		dispatcher.getVehicleCommandQueue(v2).offer(new StartVehicle());

		try
		{
			Thread.sleep(3000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		dispatcher.getVehicleCommandQueue(v1).offer(new DestroyVehicle());
		dispatcher.getVehicleCommandQueue(v2).offer(new DestroyVehicle());
	}

}
