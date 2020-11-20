package com.polimi.pipelineactors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.*;

import static com.polimi.pipelineactors.Main.exec;

public class AddActor extends AbstractActor {

	private static final int WINDOW2 = 100;
	private static final int REPLICAS = 4;
	private static final int MAXACCUMULATE = 2;
	private List<Integer> clusteredValues;
	public static Vector<ActorRef> nextStep;

	public AddActor() {
		this.clusteredValues = new ArrayList<>();
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(SimpleMessage.class, this::onMessage)
				.build();
	}

	void onMessage(SimpleMessage msg){
		this.clusteredValues.add(msg.getValue());
		if(this.clusteredValues.size()==MAXACCUMULATE){
			Integer result =this.clusteredValues.stream().reduce(0, Integer::sum);
			nextStep.get(msg.getKey()%REPLICAS).tell(new SimpleMessage(new Random().nextInt(WINDOW2),result), this.getSelf());
			//System.out.println("->Multiply, sending : "+ result);
			this.clusteredValues.clear();
		}
	}


	static Props props() {
		return Props.create(AddActor.class);
	}

}
