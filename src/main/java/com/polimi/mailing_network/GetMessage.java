package com.polimi.mailing_network;

public class GetMessage extends DefaultMessage{
    private String name;

    public GetMessage(String name, String serverPath) {
        super(serverPath);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
