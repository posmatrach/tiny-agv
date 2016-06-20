package com.hexbugsrnr.tinyagv.vehicle.di;

import com.hexbugsrnr.tinyagv.vehicle.protocol.interfaces.Message;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by null on 19/06/16.
 */
public interface CommandQueueFactory
{
	LinkedBlockingQueue<Message> create();
}
