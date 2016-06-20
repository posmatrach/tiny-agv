package com.hexbugsrnr.tinyagv.vehicle.di;

import com.hexbugsrnr.tinyagv.vehicle.Vehicle;
import com.hexbugsrnr.tinyagv.vehicle.protocol.interfaces.Message;
import com.hexbugsrnr.tinyagv.vehicle.state.Coordinates;

import java.util.concurrent.BlockingQueue;

/**
 * Created by null on 20/06/16.
 */
public interface VehicleFactory
{
	Vehicle create(String id, Coordinates coordinates, BlockingQueue<Message> commandQueue);
}
