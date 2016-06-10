package com.hexbugsrnr.tinyagv;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by null on 10/06/16.
 */
public class AGVControlCenter
{
	static private Map<String, BlockingQueue<Message>> AGV_IN_COMM;

	static private Map<String, Coordinates> AGV_LOCATION;

	static private Map<String, Direction> AGV_DIRECTION;

	static
	{
		AGV_IN_COMM = new ConcurrentHashMap<>();
		AGV_DIRECTION = new ConcurrentHashMap<>();
		AGV_LOCATION = new ConcurrentHashMap<>();
	}

	static public String newAGV()
	{
		String id = newVehicleId();
		BlockingQueue<Message> commQueue = new LinkedBlockingQueue<>();
		new SimpleAGV(id, new Coordinates(0, 0), commQueue, AGV_LOCATION, AGV_DIRECTION).start();
		AGV_IN_COMM.put(id, commQueue);
		return id;
	}

	static public boolean sendMessage(String vehicleID, Message command)
	{
		BlockingQueue<Message> q = AGV_IN_COMM.get(vehicleID);
		if (null == q)
			return false;
		q.offer(command);
		return true;
	}

	static public Coordinates getLocation(String vehicleID)
	{
		return AGV_LOCATION.get(vehicleID);
	}

	static public Direction getDirection(String vehicleID)
	{
		return AGV_DIRECTION.get(vehicleID);
	}

	static public int getNumberOfVehicles()
	{
		return AGV_IN_COMM.size();
	}

	static public boolean vehicleExists(String vehicleID)
	{
		return AGV_IN_COMM.containsKey(vehicleID);
	}

	static private String newVehicleId()
	{
		return "AGV_" + AGV_IN_COMM.size();
	}
}
