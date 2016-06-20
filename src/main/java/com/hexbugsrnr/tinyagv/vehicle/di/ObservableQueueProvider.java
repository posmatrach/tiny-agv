package com.hexbugsrnr.tinyagv.vehicle.di;

import com.google.inject.Provider;
import com.hexbugsrnr.tinyagv.util.ObservableQueue;
import com.hexbugsrnr.tinyagv.vehicle.protocol.VehicleStatus;
import rx.subjects.PublishSubject;

/**
 * Created by null on 19/06/16.
 */
public class ObservableQueueProvider implements Provider<ObservableQueue<VehicleStatus>>
{
	@Override
	public ObservableQueue<VehicleStatus> get()
	{
		return new ObservableQueue<>();
	}
}
