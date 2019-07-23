package com.jimboss.deer.common.domain;

import java.util.HashMap;

public class DeerResponse extends HashMap<String, Object> {

    private static final long serialVersionUID = -8713837118340960775L;

    public DeerResponse message(String message) {
        this.put("message", message);
        return this;
    }

    public DeerResponse data(Object data) {
        this.put("data", data);
        return this;
    }

    @Override
    public DeerResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
