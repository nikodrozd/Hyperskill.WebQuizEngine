package engine.controller;

import engine.entity.User;
import engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
public class UserRestController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/api/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void portRegister(@Valid @RequestBody User user) {
        userService.addUser(user);
    }
}
