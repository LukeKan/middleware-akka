package com.polimi.supervisorcounter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;

import static akka.pattern.Patterns.ask;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Main {

	private static final int FAULTS = 2;

	public static void main(String[] args) {

		scala.concurrent.duration.Duration timeout = scala.concurrent.duration.Duration.create(5, SECONDS);

		final ActorSystem sys = ActorSystem.create("System");
		final ActorRef supervisor = sys.actorOf(SupervisorCounterActor.props(), "supervisor");

		ActorRef counter;
		try {
			scala.concurrent.Future<Object> waitingForCounter = ask(supervisor, Props.create(CounterActor.class), 5000);
			counter = (ActorRef) waitingForCounter.result(timeout, null);

			for (int i = 0; i < FAULTS ; i++)
				counter.tell(new SimpleMessage(Code.FAULT), ActorRef.noSender());

			counter.tell(new SimpleMessage(Code.RESUME),ActorRef.noSender());
			counter.tell(new SimpleMessage(Code.STOP), ActorRef.noSender());
			//supervisor.tell(new SimpleMessage(Code.RESUME), ActorRef.noSender());
			//counter.tell(new SimpleMessage(Code.INCREMENT), ActorRef.noSender());

			sys.terminate();

		} catch ( InterruptedException | TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
