package com.hexbugsrnr.tinyagv.vehicle.state;

/**
 * Default (non-moving) state of the vehicle.
 */
public class StoppedVehicleState implements VehicleState
{
	private volatile Direction direction;

	private final Coordinates coordinates;

	public StoppedVehicleState(Direction direction, Coordinates coordinates)
	{
		this.direction = direction;
		this.coordinates = coordinates;
	}

	public StoppedVehicleState(Coordinates coordinates)
	{
		this(null, coordinates);
	}

	@Override
	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}

	@Override
	public Direction getDirection()
	{
		return direction;
	}

	@Override
	public Coordinates getCoordinates()
	{
		return coordinates;
	}
}
