package org.secondMemory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.secondMemory.controller.request.AuthenticationRequest;
import org.secondMemory.controller.request.RegistrationRequest;
import org.secondMemory.controller.response.AuthenticationResponse;
import org.secondMemory.controller.response.ErrorResponse;
import org.secondMemory.controller.response.RegistrationResponse;
import org.secondMemory.exception.AuthenticationException;
import org.secondMemory.exception.IllegalPasswordException;
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
          AuthenticationRequest authenticateRequest =
              objectMapper.readValue(request.body(), AuthenticationRequest.class);
          try {
            userService.authenticate(authenticateRequest.email(), authenticateRequest.password());
            response.status(200);
            log.info("Successfully logged in with email {}", authenticateRequest.email());
            return objectMapper.writeValueAsString(
                new AuthenticationResponse("You have successfully logged in."));
          } catch (AuthenticationException e) {
            response.status(401);
            log.error("Failed authentication attempt for {}", authenticateRequest.email(), e);
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
          RegistrationRequest registrationRequest =
              objectMapper.readValue(request.body(), RegistrationRequest.class);
          try {
            userService.registerUser(
                registrationRequest.email(),
                registrationRequest.password(),
                registrationRequest.username());
            response.status(201);
            log.info("Successfully registered with email {}", registrationRequest.email());
            return objectMapper.writeValueAsString(
                new RegistrationResponse("You have successfully registered!"));
          } catch (RegistrationException | IllegalPasswordException e) {
            response.status(400);
            log.error("Failed register attempt with email {}", registrationRequest.email(), e);
            return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
          }
        });
  }
}
