package org.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;

import java.util.Map;

public class UsersController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(UsersController.class);

    private final UserService userService;
    private final Service service;
    private final ObjectMapper objectMapper;

    public UsersController(Service service, UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @Override
    public void initializeEndpoints() {
        getAllUsersWithRoles();
        updateUserRoles();
    }

    private void getAllUsersWithRoles() {
        service.get("/users", (req, res) -> {
            log.info("Получен запрос GET /users");
            try {
                res.type("application/json");
                res.status(200);
                return objectMapper.writeValueAsString(userService.getAllUsersWithRoles());
            } catch (Exception e) {
                log.error("Ошибка при получении пользователей с ролями", e);
                res.status(500);
                return objectMapper.writeValueAsString(Map.of("error", "Ошибка при получении пользователей с ролями"));
            }
        });
    }

    private void updateUserRoles() {
        service.post("/users/updateRoles", (req, res) -> {
            log.info("Получен запрос POST /users/updateRoles");
            try {
                Map<Integer, String> userRoles = objectMapper.readValue(req.body(), new TypeReference<>() {});

                if (userRoles == null || userRoles.isEmpty()) {
                    res.status(400);
                    return objectMapper.writeValueAsString(Map.of("error", "Тело запроса не может быть пустым"));
                }

                userService.updateUserRoles(userRoles);
                res.status(200);
                return objectMapper.writeValueAsString(Map.of("message", "Роли успешно обновлены"));
            } catch (Exception e) {
                log.error("Ошибка при обновлении ролей пользователей", e);
                res.status(500);
                return objectMapper.writeValueAsString(Map.of("error", "Ошибка при обновлении ролей пользователей"));
            }
        });
    }
}
