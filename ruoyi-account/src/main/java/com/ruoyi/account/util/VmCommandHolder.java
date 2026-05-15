package com.ruoyi.account.util;

import org.springframework.stereotype.Component;

@Component
public class VmCommandHolder {

    private volatile String command = "";

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void clear() {
        this.command = "";
    }
}
