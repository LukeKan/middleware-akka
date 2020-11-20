package com.polimi.counter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Main {

	private static final int numThreads = 16;
	private static final int numMessages = 1000;

	public static void main(String[] args) {

		final ActorSystem sys = ActorSystem.create("System");
		final ActorRef counter = sys.actorOf(CounterActor.props(), "counter");

		// Send messages from multiple threads in parallel
		final ExecutorService exec = Executors.newFixedThreadPool(numThreads);

		for (int i = 0; i < numMessages; i++) {
			exec.submit(() -> counter.tell(new IncrementMessage(), ActorRef.noSender()));
		}
		for (int i = 0; i < numMessages; i++) {
			exec.submit(() -> counter.tell(new DecrementMessage(), ActorRef.noSender()));
		}
		// Wait for all messages to be sent and received
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		exec.shutdown();
		sys.terminate();

	}

}
