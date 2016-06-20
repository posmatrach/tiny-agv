package com.hexbugsrnr.tinyagv.vehicle.protocol;

import com.hexbugsrnr.tinyagv.vehicle.protocol.interfaces.Message;

/**
 * Part of the vehicle communication protocol.
 * After receiving this message, vehicle should terminate
 * its main processing loop.
 *
 * Created by null on 20/06/16.
 */
public class DestroyVehicle implements Message
{
	@Override
	public String toString()
	{
		return "DESTROY";
	}
}
