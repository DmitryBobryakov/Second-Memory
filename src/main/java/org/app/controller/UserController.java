package org.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.app.service.UserService;
import org.app.controller.request.AuthenticationRequest;
import org.app.controller.request.RegistrationRequest;
import org.app.controller.response.AuthenticationResponse;
import org.app.controller.response.ErrorResponse;
import org.app.controller.response.RegistrationResponse;
import org.app.exception.AuthenticationException;
import org.app.exception.RegistrationException;
import spark.Request;
import spark.Response;
import spark.Service;

/**
 * Контроллер, работает с endpoint-ами. Аутентификация, регистрация.
 *
 * @author Samyrai47
 */
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
    service.get(
        "/SecondMemory/signin",
        (Request request, Response response) -> {
          response.type("application/json");
          AuthenticationRequest authenticateRequest =
              objectMapper.readValue(request.body(), AuthenticationRequest.class);
          try {
            userService.authenticate(authenticateRequest.email(), authenticateRequest.password());
            response.status(200);
            return objectMapper.writeValueAsString(
                new AuthenticationResponse("You have successfully logged in."));
          } catch (AuthenticationException e) {
            response.status(401);
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
            return objectMapper.writeValueAsString(
                new RegistrationResponse("You have successfully registered!"));
          } catch (RegistrationException e) {
            response.status(400);
            return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
          }
        });
  }
}
