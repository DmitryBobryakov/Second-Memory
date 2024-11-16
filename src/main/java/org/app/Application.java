package org.app;

import java.util.List;
import org.app.controller.Controller;

/**
 * Инкапсулирует список Controller, у каждого вызывает initializeEndpoints.
 *
 * @author Samyrai47
 */
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
