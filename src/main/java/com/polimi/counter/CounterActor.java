package com.polimi.counter;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class CounterActor extends AbstractActor {

	private int counter;

	public CounterActor() {
		this.counter = 0;
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(DecrementMessage.class, this::onDecrement)
				.match(IncrementMessage.class, this::onIncrement)
				.build();
	}

	void onIncrement(IncrementMessage msg) {
		++counter;
		System.out.println("Counter increased to " + counter);
	}

	void onDecrement(DecrementMessage msg) {
		--counter;
		System.out.println("Counter increased to " + counter);
	}

	static Props props() {
		return Props.create(CounterActor.class);
	}

}
