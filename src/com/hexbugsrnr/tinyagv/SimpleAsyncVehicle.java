package com.hexbugsrnr.tinyagv;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by null on 9/06/16.
 */
public class SimpleAsyncVehicle implements Runnable
{
	private String id;

	private Coordinates coordinates;

	private volatile Direction direction;

	private BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();

	private ConcurrentLinkedQueue<EnvironmentMessage> enviroQueue;

	private volatile boolean stopped = false;

	public SimpleAsyncVehicle(String id, Coordinates coordinates, ConcurrentLinkedQueue<EnvironmentMessage> enviroQueue)
	{
		this.coordinates = coordinates;
		this.id = id;
		this.enviroQueue = enviroQueue;
		new Thread(this).start();
	}

	public synchronized void sendMessage(Message message) throws InterruptedException
	{
		messageQueue.put(message);
	}

	@Override
	public void run()
	{
		try
		{
			while(!stopped)
			{
				Message m = messageQueue.take();
				System.out.println("[" + id.toString() + "]" + "Received message: " + m.getType() + "; Value: " + m.getValue());
				if(m.getType() == MessageType.DIRECTION)
					this.direction = (Direction) m.getValue();
				else if(m.getType() == MessageType.START && null != direction)
					move();
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
				System.out.println("[" + id.toString() + "]" + " X:" + coordinates.getX() + "; Y:" + coordinates.getY());
				synchronized (coordinates)
				{
					coordinates.setX(coordinates.getX() + direction.getDx());
					coordinates.setY(coordinates.getY() + direction.getDy());
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
