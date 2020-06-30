package sw2.lab6.teletok.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
@RestController
@CrossOrigin
public class ProductWebService {



    @PostMapping(value = "/user/signIn", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity signin(
            @RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {

        HashMap<String, Object> responseMap = new HashMap<>();


        if (username.isEmpty() || password.isEmpty()) {
            responseMap.put("id", product.getId());
        }
        responseMap.put("estado", "creado");
        return new ResponseEntity(responseMap, HttpStatus.CREATED);

    }
}
