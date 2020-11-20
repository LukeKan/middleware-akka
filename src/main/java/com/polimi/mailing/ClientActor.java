package com.polimi.mailing;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class ClientActor extends AbstractActor {

    private final String clientLog= "[ClientActor]: ";;


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ServerMessage.class, this::onServerMessage)
                .build();
    }

    public void onServerMessage(ServerMessage msg) {
        System.out.println(clientLog+msg.getName());
    }

    static Props props() {
        return Props.create(ClientActor.class);
    }
}
