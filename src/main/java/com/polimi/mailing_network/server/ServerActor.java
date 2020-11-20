package com.polimi.mailing_network.server;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.polimi.mailing_network.GetMessage;
import com.polimi.mailing_network.PutMessage;
import com.polimi.mailing_network.ServerMessage;

import java.util.HashMap;

public class ServerActor extends AbstractActor {

	private static final String serverLog = "[ServerActor]: ";

	private HashMap<String, String> mailing;

	public ServerActor() {
		this.mailing = new HashMap<>();
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(GetMessage.class, this::onGetMessage)
				.match(PutMessage.class, this::onPutMessage)
				.build();
	}

	void onGetMessage(GetMessage msg) {
		if(mailing.containsKey(msg.getName())) {
			this.sender().tell(new ServerMessage(mailing.get(msg.getName())), this.self());
			System.out.println(serverLog+"List sent to client");
		}
		else System.out.println(serverLog+"No name associated to this server");
	}

	void onPutMessage(PutMessage msg) {
		mailing.putIfAbsent(msg.getName(), msg.getAddress());
		System.out.println(serverLog+"Put name: " + msg.getName() + ", at address: " + msg.getAddress());
	}


	static Props props() {
		return Props.create(ServerActor.class);
	}

}
