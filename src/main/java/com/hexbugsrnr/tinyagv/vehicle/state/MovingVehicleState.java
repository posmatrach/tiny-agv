package com.hexbugsrnr.tinyagv.vehicle.state;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.hexbugsrnr.tinyagv.util.ObservableQueue;
import com.hexbugsrnr.tinyagv.vehicle.protocol.VehicleStatus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Class representing the vehicle state while in motion.
 * While in this state, the vehicle will calculate new
 * coordinates every 500ms and emit an event via provided
 * observable queue.
 */
public class MovingVehicleState implements VehicleState, Runnable
{
	private VehicleState baseState;

	private final ScheduledExecutorService scheduler;

	private ScheduledFuture<?> moveHandle;

	private ObservableQueue<VehicleStatus> eventQueue;

	private String id;

	@Inject
	public MovingVehicleState(@Assisted VehicleState baseState, @Assisted String id, ObservableQueue<VehicleStatus> eventStream)
	{
		this.baseState = baseState;
		this.scheduler = Executors.newScheduledThreadPool(1);
		this.eventQueue = eventStream;
		this.id = id;

		// Using ScheduleExecutorService to "slow down" the vehicle.
		// This is probably a better option than Thread.sleep.
		moveHandle = scheduler.scheduleAtFixedRate(this, 0, 500, TimeUnit.MILLISECONDS);
	}

	@Override
	public void setDirection(Direction direction)
	{
		// We want to lock on the wrapped state object to avoid any inconsistencies.
		// VehicleState#Direction is volatile, but one can't be paranoid enough.
		synchronized (baseState)
		{
			baseState.setDirection(direction);
		}
	}

	@Override
	public Direction getDirection()
	{
		return baseState.getDirection();
	}

	@Override
	public Coordinates getCoordinates()
	{
		return baseState.getCoordinates();
	}

	@Override
	public void run()
	{
		// Here we definitely want some atomicity when updating things.
		synchronized (baseState)
		{
			Coordinates c = this.getCoordinates();
			Direction d = this.getDirection();
			c.setX(c.getX() + d.getDx());
			c.setY(c.getY() + d.getDy());
			eventQueue.offer(new VehicleStatus(id, baseState.getDirection(), baseState.getCoordinates()));
		}
	}

	/**
	 * Invoking this method will cancel the scheduled location calculation
	 * scheduled task, perform graceful shutdown of the ExecutorService and
	 * return the (un)wrapped state.
	 *
	 * @return State object with last calculated coordinates and direction
	 */
	public VehicleState stop()
	{
		moveHandle.cancel(true);
		scheduler.shutdownNow();
		return this.baseState;
	}
}
