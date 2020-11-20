package com.polimi.mailing;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.polimi.counter.IncrementMessage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	private static final int numThreads = 16;
	private static final int numMessages = 1000;

	public static void main(String[] args) {

		final ActorSystem sys = ActorSystem.create("System");
		final ActorRef server = sys.actorOf(ServerActor.props(), "Server");
		final ActorRef clientA = sys.actorOf(ClientActor.props(), "ClientA");
		final ActorRef clientB = sys.actorOf(ClientActor.props(), "ClientB");

		// Send messages from multiple threads in parallel
		final ExecutorService exec = Executors.newFixedThreadPool(numThreads);

		for(int i=0;i<numMessages;i++){
			exec.submit(() -> server.tell(new PutMessage("Client B","clientb@foo.com"), clientA));
			exec.submit(() -> server.tell(new GetMessage("Client B"), clientB));
			exec.submit(() -> server.tell(new PutMessage("Client A","clienta@foo.com"), clientB));
			exec.submit(() -> server.tell(new GetMessage("Client A"), clientA));
		}

		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		exec.shutdown();
		sys.terminate();

	}

}
