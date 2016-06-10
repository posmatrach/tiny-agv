package com.hexbugsrnr.tinyagv;

/**
 * Created by null on 9/06/16.
 */
public interface Vehicle
{
	void setDirection(Direction direction);

	Direction getDirection();

	Coordinates getCoordinates();

	void start();

	void stop();
}
