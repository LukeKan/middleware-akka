package com.polimi.mailing_network.server;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.io.IOException;

public class Main {

	private static final int numThreads = 16;
	private static final int numMessages = 1000;

	public static void main(String[] args) {

		Config conf = ConfigFactory.parseFile(new File("conf"));
		ActorSystem sys = ActorSystem.create("server", conf);
		ActorRef server = sys.actorOf(ServerActor.props(), "server-actor");


		try {
			System.in.read();
			System.out.println(server.isTerminated());
		} catch (IOException e) {
			e.printStackTrace();
		}
		//sys.terminate();

	}

}
