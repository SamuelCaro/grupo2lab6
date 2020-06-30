package sw2.lab6.teletok.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sw2.lab6.teletok.repository.PostRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
public class ProductWebService {

    @Autowired
    PostRepository postRepository;

    @GetMapping(value = "/ws/post/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listarPosts(@RequestParam("query") String query) {

        HashMap<String, Object> responseMap = new HashMap<>();

        responseMap.put("estado", "ok");
        responseMap.put("listaPosts", postRepository.listarFechaDesc());
        return new ResponseEntity(responseMap, HttpStatus.OK);


    }


}

