package com.polimi.mailing_network;

public class PutMessage extends DefaultMessage{
    private String name;
    private String address;

    public PutMessage(String name, String address, String serverPath) {
        super(serverPath);
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
