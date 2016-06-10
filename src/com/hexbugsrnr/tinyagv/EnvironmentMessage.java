package com.hexbugsrnr.tinyagv;

/**
 * Created by null on 10/06/16.
 */
public class EnvironmentMessage
{
	public final String vehicleID;

	public final Coordinates coordinates;

	public EnvironmentMessage(String vehicleID, Coordinates coordinates)
	{
		this.vehicleID = vehicleID;
		this.coordinates = coordinates;
	}
}
