package com.toughegg.teorderpo.modle.entry;

import java.util.HashMap;

public class JsonEntry {
    private HashMap args;
    private HashMap session;

    public HashMap getSession() {
        return session;
    }

    public void setSession(HashMap session) {
        this.session = session;
    }

    public HashMap getArgs() {
        return args;
    }

    public void setArgs(HashMap args) {
        this.args = args;
    }
}