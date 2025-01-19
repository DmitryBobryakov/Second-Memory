package org.secondmemory;

import java.util.List;
import org.secondmemory.controller.Controller;

public class Application {

    private final List<Controller> controllers;

    public Application(List<Controller> controllers) {
        this.controllers = controllers;
    }

    public void start() {
        for (Controller controller : controllers) {
            controller.initializeEndpoints();
        }
    }
}