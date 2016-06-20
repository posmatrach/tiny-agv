package com.hexbugsrnr.tinyagv.vehicle.protocol.interfaces;

import com.hexbugsrnr.tinyagv.util.ObservableQueue;
import com.hexbugsrnr.tinyagv.vehicle.protocol.VehicleStatus;
import com.hexbugsrnr.tinyagv.vehicle.state.VehicleState;

/**
 * Created by null on 19/06/16.
 */
public interface CommandStateHandler
{
	VehicleState transformState(String id, Message message, VehicleState currentState, ObservableQueue<VehicleStatus> eventQueue);
}
