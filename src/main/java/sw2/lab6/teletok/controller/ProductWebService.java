package sw2.lab6.teletok.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sw2.lab6.teletok.repository.UserRepository;

import java.util.HashMap;
@RestController
@CrossOrigin
public class ProductWebService {

    @Autowired
    UserRepository userRepository;

    @PostMapping(value = "/user/signIn", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity signin(
            @RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {

        HashMap<String, Object> responseMap = new HashMap<>();

        if (username.isEmpty() || password.isEmpty()) {
            responseMap.put("error", "AUTH_FAILED");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
        userRepository.findByUsername(username);

        responseMap.put("estado", "creado");
        return new ResponseEntity(responseMap, HttpStatus.CREATED);

    }
}
