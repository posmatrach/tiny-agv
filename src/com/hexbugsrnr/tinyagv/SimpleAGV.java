package com.hexbugsrnr.tinyagv;

import java.util.concurrent.BlockingQueue;
import java.util.Map;

/**
 * Created by null on 9/06/16.
 */
public class SimpleAGV extends Thread
{
	private final String id;

	private final BlockingQueue<Message> commQueue;

	private final Map<String, Coordinates> locationMap;

	private final Map<String, Direction> directionMap;

	private volatile Coordinates coordinates;

	private volatile Direction direction;

	private volatile boolean stopped = false;

	public SimpleAGV(String id, Coordinates coordinates, BlockingQueue<Message> commQueue, Map<String, Coordinates> locationMap, Map<String, Direction> directionMap)
	{
		super(id + "_THREAD");
		this.id = id;
		this.coordinates = coordinates;
		this.commQueue = commQueue;
		this.locationMap = locationMap;
		this.directionMap = directionMap;
		this.locationMap.put(id, coordinates);
	}

	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				Message m = commQueue.take();
				System.out.println("[" + id.toString() + "]" + "> Received command: " + m.getType() + "; Value: " + m.getValue());

				if(m.getType() == MessageType.DIRECTION)
				{
					this.direction = (Direction) m.getValue();
					directionMap.put(this.id, this.direction);
				}
				else if(m.getType() == MessageType.START && null != direction)
				{
					stopped = false;
					move();
				}
				else
					this.stopped = (m.getType() == MessageType.STOP);
			}

		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private void move()
	{
		Thread t = new Thread(() -> {
			while(!stopped)
			{
				if(null == coordinates || null == direction)
					break;
				synchronized (direction)
				{
					coordinates = new Coordinates(coordinates.getX() + direction.getDx(), coordinates.getY() + direction.getDy());
					locationMap.put(id, this.coordinates);
				}
				try
				{
					Thread.sleep(250);
				} catch (InterruptedException e)
				{
					break;
				}
			}
		});
		t.start();
	}
}
