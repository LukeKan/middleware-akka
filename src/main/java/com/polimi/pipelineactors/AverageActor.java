package com.polimi.pipelineactors;

import akka.actor.AbstractActor;
import akka.actor.Props;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class AverageActor extends AbstractActor {

	private static final int MAXACCUMULATE = 10;
	private List<Integer> clusteredValues;

	public AverageActor() {
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
			System.out.println("Result : "+this.clusteredValues.stream().reduce(0, Integer::sum)/this.clusteredValues.size());
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
		return Props.create(AverageActor.class);
	}

}
