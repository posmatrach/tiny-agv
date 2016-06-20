package com.hexbugsrnr.tinyagv.vehicle.protocol;

import com.hexbugsrnr.tinyagv.vehicle.state.Direction;
import com.hexbugsrnr.tinyagv.vehicle.protocol.interfaces.Message;

/**
 * Vehicle command for setting new direction.
 */
public class SetDirection implements Message
{
	private final Direction direction;

	public SetDirection(Direction direction)
	{
		this.direction = direction;
	}

	public Direction getDirection()
	{
		return direction;
	}

	@Override
	public String toString()
	{
		return "DIRECTION - " + direction;
	}
}
