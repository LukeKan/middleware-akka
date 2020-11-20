package com.polimi.pipelineactors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.polimi.supervisorcounter.Code;
import com.polimi.supervisorcounter.SupervisorCounterActor;

import java.sql.Time;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import static akka.pattern.Patterns.ask;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Main {

	private static final int WINDOW1 = 200;
	private static final int REPLICAS = 4;
	private static final int MESSAGENUM = 100000;
	public final static ExecutorService exec = Executors.newFixedThreadPool(8);

	public static void main(String[] args) {

		final ActorSystem sys = ActorSystem.create("System");
		Vector<ActorRef> addStep = new Vector<>();
		Vector<ActorRef> multiplyStep = new Vector<>();
		Vector<ActorRef> averageStep = new Vector<>();


		for (int i=0;i<REPLICAS;i++){
			addStep.add(sys.actorOf(AddActor.props(), "add-"+i));
			multiplyStep.add(sys.actorOf(MultiplyActor.props(), "mult-"+i));
			averageStep.add(sys.actorOf(AverageActor.props(), "avg-"+i));
		}
		AddActor.nextStep=multiplyStep;
		MultiplyActor.nextStep=averageStep;
		SimpleMessage msg;
		for(int i=0;i<MESSAGENUM;i++){
			msg=new SimpleMessage(new Random().nextInt(WINDOW1),new Random().nextInt(5));
			SimpleMessage finalMsg = msg;
			exec.submit(()->addStep.get(finalMsg.getKey()%REPLICAS).tell(finalMsg,ActorRef.noSender()));
		}


		exec.shutdown();
		sys.terminate();
	}

}
