package org.secondMemory.controller;

import java.util.HashMap;
import java.util.Map;
import org.secondMemory.service.UserService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Service;
import spark.template.freemarker.FreeMarkerEngine;

public class FreemarkerController implements Controller {

  private final Service service;
  private final UserService userService;
  private final FreeMarkerEngine freeMarkerEngine;

  public FreemarkerController(
      Service service, UserService userService, FreeMarkerEngine freeMarkerEngine) {
    this.service = service;
    this.userService = userService;
    this.freeMarkerEngine = freeMarkerEngine;
  }

  @Override
  public void initializeEndpoints() {
    startApp();
    login();
    register();
  }

  private void startApp() {
    service.get(
        "/",
        (Request request, Response response) -> {
          response.type("text/html; charset=utf-8");
          Map<String, Object> model = new HashMap<>();
          return freeMarkerEngine.render(new ModelAndView(model, "hero.ftl"));
        });
  }

  private void login() {
    service.get(
        "/login.html",
        (Request request, Response response) -> {
          response.type("text/html; charset=utf-8");
          Map<String, Object> model = new HashMap<>();
          return freeMarkerEngine.render(new ModelAndView(model, "login.ftl"));
        });
  }

  private void register() {
    service.get(
        "/register.html",
        (Request request, Response response) -> {
          response.type("text/html; charset=utf-8");
          Map<String, Object> model = new HashMap<>();
          return freeMarkerEngine.render(new ModelAndView(model, "register.ftl"));
        });
  }
}
