package com.hexbugsrnr.tinyagv.vehicle.state;

/**
 * Created by null on 9/06/16.
 */
public class Coordinates
{
	private int x;

	private int y;

	public Coordinates(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}
}
