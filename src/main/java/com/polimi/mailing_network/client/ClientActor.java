package com.polimi.mailing_network.client;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.Props;
import com.polimi.mailing.GetMessage;
import com.polimi.mailing.PutMessage;
import com.polimi.mailing_network.DefaultMessage;
import com.polimi.mailing_network.ServerMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientActor extends AbstractActor {

    private final String clientLog= "[ClientActor]: ";;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ServerMessage.class, this::onServerMessage)
                .match(DefaultMessage.class, this::onDefault)
                .build();
    }

    public void onServerMessage(ServerMessage msg) {
        System.out.println(clientLog+msg.getName());
    }

    public void onDefault(DefaultMessage defaultMessage){
        String serverAddr = "akka://server@127.0.0.1:2552/user/server-actor";
        ActorSelection server = context().actorSelection(serverAddr);
        server.tell(defaultMessage, this.getSelf());
    }


    static Props props() {
        return Props.create(ClientActor.class);
    }
}
