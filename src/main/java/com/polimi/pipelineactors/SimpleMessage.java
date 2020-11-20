package com.polimi.pipelineactors;

public class SimpleMessage {

    private Integer key;
    private Integer value;

    public SimpleMessage(Integer key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }
}
