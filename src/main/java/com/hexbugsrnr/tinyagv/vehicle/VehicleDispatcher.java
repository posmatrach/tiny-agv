package com.hexbugsrnr.tinyagv.vehicle;

import com.google.inject.Singleton;
import com.hexbugsrnr.tinyagv.vehicle.di.CommandQueueFactory;
import com.hexbugsrnr.tinyagv.vehicle.di.VehicleFactory;
import com.hexbugsrnr.tinyagv.vehicle.protocol.interfaces.Message;
import com.hexbugsrnr.tinyagv.vehicle.state.Coordinates;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main class of the Tiny-AGV system.
 *
 * Vehicle Dispatcher is in charge of creating vehicles and keeping
 * references to message (command) queues of every created vehicle.
 *
 * Separate input queue is used for each vehicle for the sakes of simplicity,
 * and to reduce filtering overhead, when processing commands.
 *
 * Queue of the events emitted by vehicles is shared, highly performant and
 * flexible @{{@link com.hexbugsrnr.tinyagv.util.ObservableQueue}} that allows
 * both classic Java queue operations, as well as RxJava style @{{@link rx.Observable}}
 * subscriptions, streaming and transformation.
 *
 * Intended as singleton, it should be obtained via @{{@link com.google.inject.Injector}}
 * Instantiated by @{{@link com.hexbugsrnr.tinyagv.vehicle.di.VehicleDispatcherProvider}}
 */
@Singleton
public class VehicleDispatcher
{
	static private final String VEHICLE_ID_PREFIX = "AGV_";

	private VehicleFactory vehicleFactory;

	private CommandQueueFactory commandQueueFactory;

	private AtomicInteger vehicleCount = new AtomicInteger(0);

	private Map<String, BlockingQueue<Message>> vehicleCommandQueues = new ConcurrentHashMap<>();

	public VehicleDispatcher(VehicleFactory vehicleFactory, CommandQueueFactory commandQueueFactory)
	{
		this.vehicleFactory = vehicleFactory;
		this.commandQueueFactory = commandQueueFactory;
	}

	/**
	 * Given vehicle ID, returns that vehicle's input message queue.
	 *
	 * @param vehicleId
	 * @return Command queue of the respective vehicle
	 */
	public BlockingQueue<Message> getVehicleCommandQueue(String vehicleId)
	{
		return vehicleCommandQueues.get(vehicleId);
	}

	/**
	 * Creates new vehicle and returns a String ID
	 * that can be use to retrieve a message queue for
	 * sending commands to the vehicle.
	 *
	 * @return @{{@link String}} ID of the created vehicle
	 */
	public String createNewVehicle()
	{
		String id = VEHICLE_ID_PREFIX + vehicleCount.getAndIncrement();
		BlockingQueue<Message> queue = commandQueueFactory.create();
		vehicleCommandQueues.put(id, queue);
		vehicleFactory.create(id, new Coordinates(0, 0), queue).start();
		return id;
	}
}
