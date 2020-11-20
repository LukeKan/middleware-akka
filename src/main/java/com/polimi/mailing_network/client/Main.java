package com.polimi.mailing_network.client;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import com.polimi.mailing_network.GetMessage;
import com.polimi.mailing_network.PutMessage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {


	private static final int numMessages = 1000;
	private static final int numThreads = 16;

	public static void main(String[] args) {

		String serverAddr = "akka.tcp://Server@127.0.0.1:6123/user/serverActor";
		final ActorSystem sys = ActorSystem.create("System");
		final ActorRef clientA = sys.actorOf(ClientActor.props(), "ClientActorA");

		final ActorRef clientB = sys.actorOf(ClientActor.props(), "ClientActorB");

		// Send messages from multiple threads in parallel


		final ExecutorService exec = Executors.newFixedThreadPool(numThreads);
		for(int i=0;i<numMessages;i++){
			exec.submit(() -> clientA.tell(new PutMessage("Client B","clientb@foo.com",serverAddr), clientA));
			exec.submit(() -> clientB.tell(new GetMessage("Client B",serverAddr), clientB));
			exec.submit(() -> clientB.tell(new PutMessage("Client A","clienta@foo.com",serverAddr), clientB));
			exec.submit(() -> clientA.tell(new GetMessage("Client A",serverAddr), clientA));
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
