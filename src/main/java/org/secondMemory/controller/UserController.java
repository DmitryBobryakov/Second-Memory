package org.secondMemory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.secondMemory.controller.response.AuthenticationResponse;
import org.secondMemory.controller.response.ErrorResponse;
import org.secondMemory.controller.response.RegistrationResponse;
import org.secondMemory.exception.AuthenticationException;
import org.secondMemory.exception.IllegalPasswordException;
import org.secondMemory.exception.PasswordsDontMatchExcpetion;
import org.secondMemory.exception.RegistrationException;
import org.secondMemory.service.UserService;
import spark.Request;
import spark.Response;
import spark.Service;

/** Контроллер, работает с endpoint-ами. Аутентификация, регистрация. */
@Slf4j
public class UserController implements Controller {
  private final UserService userService;
  private final ObjectMapper objectMapper;
  private final Service service;

  public UserController(Service service, UserService userService, ObjectMapper objectMapper) {
    this.service = service;
    this.userService = userService;
    this.objectMapper = objectMapper;
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
          response.type("application/json");
          String requestMessage = request.body();
          System.out.println(requestMessage);
          String[] data = requestMessage.split("\\r\\n|\\r|\\n");
          System.out.println(Arrays.toString(data));
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
            return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
          }
        });
  }

  // Endpoint регистрации
  private void registerUser() {
    service.post(
        "/SecondMemory/signup",
        (Request request, Response response) -> {
          response.type("application/json");
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
            return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
          }
        });
  }
}
