package org.secondMemory;

import java.util.List;
import org.secondMemory.controller.Controller;

/** Инкапсулирует список Controller, у каждого вызывает initializeEndpoints. */
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
