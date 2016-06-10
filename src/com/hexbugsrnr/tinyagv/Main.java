package com.hexbugsrnr.tinyagv;


import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)
    {
	    Scanner scanner = new Scanner(System.in);

	    printBanner();
	    printCommandList();

	    boolean stopped = false;

	    while(!stopped)
	    {
		    String commandString = scanner.nextLine();
		    List<String> commands = Arrays.asList(commandString.split(" "));

		    if(commands.size() < 1)
			    System.out.println("> Invalid command!");

		    String command = commands.get(0).toUpperCase();

		    if("QUIT".equals(command))
		    {
				stopped = true;
		    }
		    else if("FLEET".equals(command))
			    System.out.println("> Current number of vehicles: " + AGVControlCenter.getNumberOfVehicles());
		    else if("ADD".equals(command))
		    {
			    String vID = AGVControlCenter.newAGV();
			    System.out.println("> Added vehicle with ID: " + vID);
		    }
		    else
		    {
			    if(!AGVControlCenter.vehicleExists(command))
			    {
				    System.out.println("> Invalid vehicle ID: " + command);
				    continue;
			    }

			    if(commands.size() > 1)
			        processVehicleCommand(commands);
			    else
				    queryVehicleStats(command);
		    }
	    }

	    System.out.println("> TinyAGV terminating...");
	    System.exit(0);
	}

	private static void processVehicleCommand(List<String> commands)
	{
		String vehicleID = commands.get(0).toUpperCase();
		MessageType t;
		Direction d = null;
		try
		{
			t = MessageType.valueOf(commands.get(1).toUpperCase());
			if (commands.size() == 3)
				d = Direction.valueOf(commands.get(2).toUpperCase());
			AGVControlCenter.sendMessage(vehicleID, new Message(t, d));
		}
		catch(IllegalArgumentException e)
		{
			System.out.println("> Unrecognized command.");
		}
	}
	
	private static void queryVehicleStats(String vehicleID)
	{
		Direction d = AGVControlCenter.getDirection(vehicleID);
		Coordinates l = AGVControlCenter.getLocation(vehicleID);

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
		System.out.println(">> QUIT: Quits the application.");
		System.out.println("");
		System.out.println("> Awaiting command...");
	}
}
