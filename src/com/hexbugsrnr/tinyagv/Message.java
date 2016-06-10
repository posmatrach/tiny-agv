package com.hexbugsrnr.tinyagv;

/**
 * Created by null on 10/06/16.
 */
public class Message
{
	private final MessageType type;

	private final Object value;

	public Message(final MessageType messageType)
	{
		this(messageType, null);
	}

	public Message(final MessageType type, final Object value)
	{
		this.type = type;
		this.value = value;
	}

	public MessageType getType()
	{
		return type;
	}

	public Object getValue()
	{
		return value;
	}
}
