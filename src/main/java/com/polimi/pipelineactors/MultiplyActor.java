package com.polimi.pipelineactors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.*;

import static com.polimi.pipelineactors.Main.exec;

public class MultiplyActor extends AbstractActor {
	private static final int WINDOW3 = 50;
	private static final int REPLICAS = 4;
	private static final int MAXACCUMULATE = 10;
	private List<Integer> clusteredValues;
	public static Vector<ActorRef> nextStep;

	public MultiplyActor() {
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
			Integer result =this.clusteredValues.stream().reduce(1, (subtotal, next ) -> subtotal * next);
			nextStep.get(msg.getKey()%REPLICAS).tell(new SimpleMessage(new Random().nextInt(WINDOW3),result), this.getSelf());
			//System.out.println("->Average, sending : "+ result);
			this.clusteredValues.clear();
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
		return Props.create(MultiplyActor.class);
	}

}
