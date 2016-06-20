package com.hexbugsrnr.tinyagv.vehicle.protocol;

import com.hexbugsrnr.tinyagv.vehicle.state.Coordinates;
import com.hexbugsrnr.tinyagv.vehicle.state.Direction;

/**
 * Class representing an event emitted by vehicles.
 */
public class VehicleStatus
{
	public final String id;

	public final Direction direction;

	public final Coordinates coordinates;

	public VehicleStatus(String id, Direction direction, Coordinates coordinates)
	{
		this.id = id;
		this.direction = direction;
		this.coordinates = coordinates;
	}

	@Override
	public String toString()
	{
		return "Vehicle ID: " + id + " | Direction: " + direction + " | Coordinates: " + coordinates.getX() + ", "
				+ coordinates.getY();
	}
}
