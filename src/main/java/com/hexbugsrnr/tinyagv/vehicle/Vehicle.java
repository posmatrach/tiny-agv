package com.hexbugsrnr.tinyagv.vehicle;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.hexbugsrnr.tinyagv.util.ObservableQueue;
import com.hexbugsrnr.tinyagv.vehicle.protocol.VehicleStatus;
import com.hexbugsrnr.tinyagv.vehicle.protocol.interfaces.CommandStateHandler;
import com.hexbugsrnr.tinyagv.vehicle.protocol.interfaces.Message;
import com.hexbugsrnr.tinyagv.vehicle.state.VehicleState;
import com.hexbugsrnr.tinyagv.vehicle.state.Coordinates;
import com.hexbugsrnr.tinyagv.vehicle.state.StoppedVehicleState;

import java.util.concurrent.BlockingQueue;
import java.util.Map;

/**
 * AGV Vehicle implementation.
 *
 * Simple vehicle implementation that uses queues for asynchronous communication with the environment.
 *
 */
public class Vehicle extends Thread
{
	private final Map<Class<? extends Message>, CommandStateHandler> stateHandlerMap;

	private final String id;

	private volatile VehicleState state;

	private final BlockingQueue<Message> commandQueue;

	private final ObservableQueue<VehicleStatus> eventQueue;

	@Inject
	public Vehicle(@Assisted final String id, @Assisted final Coordinates coordinates, @Assisted final BlockingQueue<Message> commandQueue,
	               Map<Class<? extends Message>, CommandStateHandler> stateHandlerMap, final ObservableQueue<VehicleStatus> eventQueue)
	{
		super(id + "_THREAD");
		this.id = id;
		this.stateHandlerMap = stateHandlerMap;
		this.state = new StoppedVehicleState(coordinates);
		this.commandQueue = commandQueue;
		this.eventQueue = eventQueue;
	}

	@Override
	public void run()
	{
		try
		{
			while(state != null)
			{
				Message message = commandQueue.take();
				System.out.println("[" + id + "] Received command: " + message.toString());
				CommandStateHandler stateHandler = stateHandlerMap.get(message.getClass());
				if (stateHandler != null)
					this.state = stateHandler.transformState(id, message, state, eventQueue);
			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
