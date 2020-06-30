package sw2.lab6.teletok.controller;

import javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sw2.lab6.teletok.entity.Post;
import sw2.lab6.teletok.repository.PostRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import sw2.lab6.teletok.repository.UserRepository;


@RestController
@CrossOrigin
public class ProductWebService {

    @Autowired
    PostRepository postRepository;

    @GetMapping(value = "/ws/post/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listarPosts(@RequestParam("query") String query) {


        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("estado", "ok");
        List<Post> listaPosts = postRepository.listarFechaDesc(query);
        for (Post i : listaPosts) {

            responseMap.put("Comment Count", postRepository.cantidadComments(i.getId()));
            responseMap.put("Likes Count", postRepository.cantidadLikes(i.getId()));
        }
        responseMap.put("listaPosts", listaPosts);
        return new ResponseEntity(responseMap, HttpStatus.OK);


    }

    
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

