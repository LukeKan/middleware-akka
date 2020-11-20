package com.polimi.supervisorcounter;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.polimi.counter.DecrementMessage;
import com.polimi.counter.IncrementMessage;
import com.polimi.supervisorcounter.exceptions.EscalateException;
import com.polimi.supervisorcounter.exceptions.FaultException;
import com.polimi.supervisorcounter.exceptions.ResumeException;
import com.polimi.supervisorcounter.exceptions.StopException;

import java.util.Optional;

public class CounterActor extends AbstractActor {

	private int counter;

	public CounterActor() {
		this.counter = 0;
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(SimpleMessage.class, this::onMessage)
				.build();
	}

	void onMessage(SimpleMessage msg) throws Exception {
		 if (msg.getCode() == Code.FAULT) {
			System.out.println("I am emulating a FAULT!");
			throw new FaultException();
		} else if (msg.getCode() == Code.INCREMENT) {
			System.out.println("Incrementing!");
			counter++;
			System.out.println("Value ="+counter);
		} else if(msg.getCode() == Code.DECREMENT){
			counter--;
			System.out.println("Value ="+counter);
		} else if(msg.getCode() == Code.STOP){
			 System.out.println("STOPPING...");
			 throw new StopException();
		 } else if(msg.getCode() == Code.RESUME){
			 System.out.println("RESUMING...");
			 throw new ResumeException();
		 } else if(msg.getCode() == Code.ESCALATE){
			 System.out.println("ESCALATING...");
			 throw new EscalateException();
		 }

	}

	@Override
	public void preRestart(Throwable reason, Optional<Object> message) {
		System.out.print("Preparing to restart...");
	}
	@Override
	public void postRestart(Throwable reason) {
		System.out.println("...now restarted!");
	}


	static Props props() {
		return Props.create(CounterActor.class);
	}

}
