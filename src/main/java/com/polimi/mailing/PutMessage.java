package com.polimi.mailing;

public class PutMessage {
    private String name;
    private String address;

    public PutMessage(String name, String address) {
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
