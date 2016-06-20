package com.hexbugsrnr.tinyagv.vehicle.state;

import com.hexbugsrnr.tinyagv.vehicle.state.Coordinates;
import com.hexbugsrnr.tinyagv.vehicle.state.Direction;

/**
 * Class intended for encapsulation of the vehicle state and behavior.
 */
public interface VehicleState
{
	void setDirection(Direction direction);

	Direction getDirection();

	Coordinates getCoordinates();
}
