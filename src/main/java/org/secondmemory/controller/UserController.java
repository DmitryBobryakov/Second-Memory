package org.secondmemory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.secondmemory.controller.response.AuthenticationResponse;
import org.secondmemory.controller.response.RegistrationResponse;
import org.secondmemory.exception.AuthenticationException;
import org.secondmemory.exception.IllegalPasswordException;
import org.secondmemory.exception.PasswordsDontMatchExcpetion;
import org.secondmemory.exception.RegistrationException;
import org.secondmemory.service.UserService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Service;
import spark.template.freemarker.FreeMarkerEngine;

/** Контроллер, работает с endpoint-ами. Аутентификация, регистрация. */
@Slf4j
public class UserController implements Controller {
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final Service service;
    private final FreeMarkerEngine freeMarkerEngine;

    public UserController(
            Service service,
            UserService userService,
            ObjectMapper objectMapper,
            FreeMarkerEngine freeMarkerEngine) {
        this.service = service;
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.freeMarkerEngine = freeMarkerEngine;
    }

    @Override
    public void initializeEndpoints() {
        authenticate();
        registerUser();
    }

    // Endpoint аутентификации
    private void authenticate() {
        service.post(
                "/SecondMemory/signin",
                (Request request, Response response) -> {
                    response.type("text/html; charset=utf-8");
                    String requestMessage = request.body();
                    String[] data = requestMessage.split("\\r\\n|\\r|\\n");
                    Map<String, String> map = new HashMap<>();
                    for (String keyValue : data) {
                        String[] parts = keyValue.split("=");
                        map.put(parts[0], parts[1]);
                    }
                    try {
                        userService.authenticate(map.get("email"), map.get("password"));
                        response.status(200);
                        log.info("Successfully logged in with email {}", map.get("email"));
                        return objectMapper.writeValueAsString(
                                new AuthenticationResponse("You have successfully logged in."));
                    } catch (AuthenticationException e) {
                        response.status(401);
                        log.error("Failed authentication attempt for {}", map.get("email"), e);
                        Map<String, Object> model = new HashMap<>();
                        model.put("message", e.getMessage());
                        model.put("link", "/login.html");
                        return freeMarkerEngine.render(new ModelAndView(model, "error.ftl"));
                    }
                });
    }

    // Endpoint регистрации
    private void registerUser() {
        service.post(
                "/SecondMemory/signup",
                (Request request, Response response) -> {
                    response.type("text/html; charset=utf-8");
                    String requestMessage = request.body();
                    String[] data = requestMessage.split("\\r\\n|\\r|\\n");
                    Map<String, String> map = new HashMap<>();
                    for (String keyValue : data) {
                        String[] parts = keyValue.split("=");
                        map.put(parts[0], parts[1]);
                    }
                    try {
                        userService.registerUser(
                                map.get("email"),
                                map.get("password"),
                                map.get("repeatPassword"),
                                map.get("username"));
                        response.status(201);
                        log.info("Successfully registered with email {}", map.get("email"));
                        return objectMapper.writeValueAsString(
                                new RegistrationResponse("You have successfully registered!"));
                    } catch (RegistrationException
                             | IllegalPasswordException
                             | PasswordsDontMatchExcpetion e) {
                        response.status(400);
                        log.error("Failed register attempt with email {}", map.get("email"), e);
                        Map<String, Object> model = new HashMap<>();
                        model.put("message", e.getMessage());
                        model.put("link", "/register.html");
                        return freeMarkerEngine.render(new ModelAndView(model, "error.ftl"));
                    }
                });
    }
}