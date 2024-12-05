package org.app;

import java.util.List;
import org.app.controller.FilesController;

public class Main {

  public static void main(String[] args) {
    Application application = new Application(List.of(new FilesController()));
    application.start();
  }
}