package com.polimi.supervisorcounter;

import akka.actor.AbstractActor;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import com.polimi.supervisorcounter.exceptions.EscalateException;
import com.polimi.supervisorcounter.exceptions.FaultException;
import com.polimi.supervisorcounter.exceptions.ResumeException;
import com.polimi.supervisorcounter.exceptions.StopException;

import java.time.Duration;

public class SupervisorCounterActor extends AbstractActor {

    private static SupervisorStrategy strategy =
            new OneForOneStrategy(
                    10,
                    Duration.ofMinutes(1),
                    DeciderBuilder
                            .match(FaultException.class, e ->
                                    SupervisorStrategy.restart())
                            .match(StopException.class, stop ->
                                    SupervisorStrategy.stop())
                            .match(ResumeException.class, resume ->
                                    SupervisorStrategy.resume())
                            .match(EscalateException.class, escalate ->
                                    SupervisorStrategy.escalate())
                            .build());
    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        Props.class,
                        props -> {
                            getSender().tell(getContext().actorOf(props), getSelf());
                        })
                .match(SimpleMessage.class, this::onResume)
                .build();
    }

    public void onResume(SimpleMessage msg){
        if(msg.getCode()== Code.RESUME)
            SupervisorStrategy.resume();
    }

    static Props props() {
        return Props.create(SupervisorCounterActor.class);
    }
}
