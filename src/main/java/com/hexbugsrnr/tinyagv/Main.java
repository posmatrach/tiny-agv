package com.hexbugsrnr.tinyagv;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.hexbugsrnr.tinyagv.cli.CLICommand;
import com.hexbugsrnr.tinyagv.util.ObservableQueue;
import com.hexbugsrnr.tinyagv.vehicle.VehicleDispatcher;
import com.hexbugsrnr.tinyagv.vehicle.di.VehicleModule;
import com.hexbugsrnr.tinyagv.vehicle.protocol.*;
import com.hexbugsrnr.tinyagv.vehicle.protocol.interfaces.Message;
import com.hexbugsrnr.tinyagv.vehicle.state.Coordinates;
import com.hexbugsrnr.tinyagv.vehicle.state.Direction;
import rx.Observer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main class.
 */
public class Main
{
	private static Map<String, VehicleStatus> VEHICLE_STATS;

	static
	{
		VEHICLE_STATS = new ConcurrentHashMap<>();
	}

	public static void main(String[] args)
	{
		// Parts of the vehicle system are wired using Guice.
		// Initialize the DI
		Injector injector = Guice.createInjector(new VehicleModule());
		// Inject the core of the system
		VehicleDispatcher dispatcher = injector.getInstance(VehicleDispatcher.class);

		// Fetch the event queue of the vehicle system.
		ObservableQueue<VehicleStatus> eventQueue = injector.getInstance(Key.get(new TypeLiteral<ObservableQueue<VehicleStatus>>(){}));

		// Subscribe to the events:
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
					System.out.println("Error: " + e.getMessage());
				}

				@Override
				public void onNext(VehicleStatus vehicleStatus)
				{
					VEHICLE_STATS.put(vehicleStatus.id, vehicleStatus);
				}
			});

		Scanner scanner = new Scanner(System.in);

		// Output some shiny nice interface messages:
		printBanner();
		printCommandList();

		// If we receive QUIT command we will stop the input loop.
		boolean stopped = false;

		// Main input loop:
		while(!stopped)
		{
			String commandString = scanner.nextLine();
			List<String> commands = Arrays.asList(commandString.split(" "));

			if(commands.size() < 1)
				System.out.println("> Invalid command!");

			String command = commands.get(0).toUpperCase();

			if("QUIT".equals(command))
			{
				// Shut down all the vehicles
				for(String key : VEHICLE_STATS.keySet())
				{
					BlockingQueue<Message> queue = dispatcher.getVehicleCommandQueue(key);
					if(queue != null)
						queue.offer(new DestroyVehicle());
				}
				// Finally set the flag
				stopped = true;
			}
			else if("FLEET".equals(command))
				System.out.println("> Current number of vehicles: " + dispatcher.getNumberOfVehicles());
			else if("ADD".equals(command))
			{
				String vID = dispatcher.createNewVehicle();
				System.out.println("> Added vehicle with ID: " + vID);
			}
			else
			{
				if(!dispatcher.vehicleIdExists(command))
				{
					System.out.println("> Invalid vehicle ID: " + command);
					continue;
				}

				if(commands.size() > 1)
					processVehicleCommand(commands, dispatcher);
				else
					queryVehicleStats(command);
			}
		}

		System.out.println("> TinyAGV terminating...");
		System.exit(0);
	}

	private static void processVehicleCommand(List<String> commands, VehicleDispatcher dispatcher)
	{
		String vehicleID = commands.get(0).toUpperCase();
		CLICommand cliCommand;

		BlockingQueue<Message> vehicleMessageQueue = dispatcher.getVehicleCommandQueue(vehicleID);
		if (vehicleMessageQueue == null)
			return;
		try
		{
			cliCommand = CLICommand.valueOf(commands.get(1).toUpperCase());
			if (commands.size() == 3 && cliCommand == CLICommand.DIRECTION)
			{
				Direction direction = Direction.valueOf(commands.get(2).toUpperCase());
				vehicleMessageQueue.offer(new SetDirection(direction));
			}
			else if(cliCommand == CLICommand.START)
				vehicleMessageQueue.offer(new StartVehicle());
			else if(cliCommand == CLICommand.STOP)
				vehicleMessageQueue.offer(new StopVehicle());
			else
				vehicleMessageQueue.offer(new DestroyVehicle());

		}
		catch(IllegalArgumentException e)
		{
			System.out.println("> Unrecognized command.");
		}
	}

	private static void queryVehicleStats(String vehicleID)
	{
		VehicleStatus status = VEHICLE_STATS.get(vehicleID);

		Coordinates l = status == null ? null : status.coordinates;
		Direction d = status == null ? null : status.direction;

		System.out.println("> Vehicle: " + vehicleID);
		System.out.println(l == null ? "> Location: unknown" : "> Location: (" + l.getX() + ", " + l.getY() + ")");
		System.out.println(d == null ? "> Direction: unknown" : "> Direction: " + d);
	}

	private static void printBanner()
	{
		System.out.println("****************************************");
		System.out.println("***                                  ***");
		System.out.println("***             TinyAGV              ***");
		System.out.println("***                                  ***");
		System.out.println("****************************************");
		System.out.println("");
	}

	private static void printCommandList()
	{
		System.out.println("> Usage");
		System.out.println(">> FLEET: Displays number of vehicle instances.");
		System.out.println(">> ADD: Creates a new vehicle.");
		System.out.println(">> {VEHICLE_ID}: Displays status of the vehicle.");
		System.out.println(">> {VEHICLE_ID} DIRECTION {NORTH|NORTHEAST|EAST|SOUTHEAST|SOUTH|SOUTHWEST|WEST|NORTHWEST}: Sets direction of the vehicle.");
		System.out.println(">> {VEHICLE_ID} START: Sets vehicle in motion.");
		System.out.println(">> {VEHICLE_ID} STOP: Stops the vehicle.");
		System.out.println(">> {VEHICLE_ID} DESTROY: Signals the vehicle to stop its main process loop.");
		System.out.println(">> QUIT: Quits the application.");
		System.out.println("");
		System.out.println("> Awaiting command...");
	}
}
