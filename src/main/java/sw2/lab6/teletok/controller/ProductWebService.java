package sw2.lab6.teletok.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sw2.lab6.teletok.repository.PostRepository;
import java.util.HashMap;
import org.springframework.web.bind.annotation.*;
import sw2.lab6.teletok.DTOO.LoginDto;
import sw2.lab6.teletok.entity.Token;
import sw2.lab6.teletok.entity.User;
import sw2.lab6.teletok.repository.TokenRepository;
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
        responseMap.put("listaPosts", postRepository.listarFechaDesc());
        return new ResponseEntity(responseMap, HttpStatus.OK);


    }

    UserRepository userRepository;
    @Autowired
    TokenRepository tokenRepository;

    @PostMapping(value = "ws/user/signIn", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity signin(
            @RequestBody LoginDto loginDto) {

        HashMap<String, Object> responseMap = new HashMap<>();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        if (loginDto.getUsername() == null || loginDto.getPassword() == null) {
            responseMap.put("error", "AUTH_FAILED");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
        User usuario = userRepository.findByUsername(username);
        boolean match = true; //passwordEncoder.matches(usuario.getPassword(), password);
        if(usuario.getUsername().equalsIgnoreCase(username) && match){
            String tokenx = getAlphaNumericString(36);
            Token token = new Token();
            token.setCode(tokenx);
            token.setUser(usuario);
            tokenRepository.save(token);
            responseMap.put("status", "AUTHENTICATED");
            responseMap.put("token", token.getCode());
            return new ResponseEntity(responseMap, HttpStatus.OK);
        }else{
            responseMap.put("error", "AUTH_FAILED");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
    }




    public String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789" + "-"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}

