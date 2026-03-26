package com.example.mod.command;

import java.util.ArrayList;
import java.util.List;

public interface Command {
    List<String> names();

    void runCommand(ArrayList<String> args);
}
