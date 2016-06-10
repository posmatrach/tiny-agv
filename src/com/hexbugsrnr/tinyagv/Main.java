package com.hexbugsrnr.tinyagv;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

	private static Direction getRandomDirection()
	{
		Random r = new Random();
		int i = r.nextInt(8);
		switch (i)
		{
			case 0: return Direction.NORTH;
			case 1: return Direction.NORTHEAST;
			case 2: return Direction.EAST;
			case 3: return Direction.SOUTHEAST;
			case 4: return Direction.SOUTH;
			case 5: return Direction.SOUTHWEST;
			case 6: return Direction.WEST;
			case 7: return Direction.NORTHWEST;
			default: return Direction.NORTH;
		}
	}

    public static void main(String[] args)
    {
        List<SimpleAsyncVehicle> vehicles = new ArrayList<>();

        if(args.length <= 0)
        {
            System.out.println("Numbers of vehicles not selected. Exiting...");
            System.exit(0);
        }

        int vehicleNum = Integer.parseInt(args[0]);

        for (int i = 0; i < vehicleNum; i++)
        {
            SimpleAsyncVehicle v = new SimpleAsyncVehicle("AGV_" + i, new Coordinates(0, 0), null);
            vehicles.add(v);
            try
            {
                v.sendMessage(new Message(MessageType.DIRECTION, getRandomDirection()));
                v.sendMessage(new Message(MessageType.START));
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        for (SimpleAsyncVehicle v : vehicles)
        {
            try
            {
	            System.out.println("Sending stop message.");
	            v.sendMessage(new Message(MessageType.STOP));
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
