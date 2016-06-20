package com.hexbugsrnr.tinyagv.vehicle.di;

import com.hexbugsrnr.tinyagv.util.ObservableQueue;
import com.hexbugsrnr.tinyagv.vehicle.protocol.VehicleStatus;
import com.hexbugsrnr.tinyagv.vehicle.state.MovingVehicleState;
import com.hexbugsrnr.tinyagv.vehicle.protocol.interfaces.CommandStateHandler;
import com.hexbugsrnr.tinyagv.vehicle.protocol.interfaces.Message;
import com.hexbugsrnr.tinyagv.vehicle.state.VehicleState;

/**
 * Created by null on 20/06/16.
 */
public class StartCommandHandler implements CommandStateHandler
{
	@Override
	public VehicleState transformState(String id, Message message, VehicleState currentState, ObservableQueue<VehicleStatus> eventQueue)
	{
		if(currentState.getDirection() == null || (currentState instanceof MovingVehicleState))
			return currentState;

		return new MovingVehicleState(currentState, id, eventQueue);
	}
}
