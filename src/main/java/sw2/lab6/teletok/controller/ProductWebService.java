package sw2.lab6.teletok.controller;

import javassist.expr.NewArray;
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
import sw2.lab6.teletok.DTOO.FotoDescripcionFoto;
import sw2.lab6.teletok.DTOO.TokenPostMsg;
import sw2.lab6.teletok.entity.Post;
import sw2.lab6.teletok.entity.PostComment;
import sw2.lab6.teletok.repository.PostCommentRepository;
import sw2.lab6.teletok.repository.PostRepository;

import java.util.HashMap;
import java.util.Optional;
import sw2.lab6.teletok.DTOO.LikeDto;
import sw2.lab6.teletok.entity.Post;
import sw2.lab6.teletok.entity.PostLike;
import sw2.lab6.teletok.repository.PostLikeRepository;
import sw2.lab6.teletok.repository.PostRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.List;

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
    @Autowired
    PostCommentRepository postCommentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    PostLikeRepository postLikeRepository;

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

        if(usuario == null){
            responseMap.put("error", "AUTH_FAILED");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
        boolean match = passwordEncoder.matches(password, usuario.getPassword());
        if(usuario.getUsername().equalsIgnoreCase(username) && match){
            String tokenx = getAlphaNumericString(36);
            Token token = new Token();
            token.setCode(tokenx);
            token.setUser(usuario);
            tokenRepository.save(token);
            responseMap.put("status", "AUTHENTICATED");
            responseMap.put("token", token.getCode());
            return new ResponseEntity(responseMap, HttpStatus.OK);
        } else {
            responseMap.put("error", "AUTH_FAILED");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
    }






//like
    @PostMapping(value = "/ws/post/like", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity likepost(
            @RequestBody LikeDto likeDto) {

        HashMap<String, Object> responseMap = new HashMap<>();
        Integer postId = likeDto.getPostId();
        String token = likeDto.getToken();

        if (token == null) {
            responseMap.put("error", "TOKEN_INVALID");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }if ( postId == null) {
            responseMap.put("error", "POST_NOT_FOUND");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
        Optional<Post> post = postRepository.findById(postId);
        if(!post.isPresent()){
            responseMap.put("error", "POST_NOT_FOUND");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
        Optional<Token> tokena = tokenRepository.findByCode(token);
        if(!tokena.isPresent()){
            responseMap.put("error", "TOKEN_INVALID");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }

        User usuario = tokena.get().getUser();
        for (PostLike postLike : post.get().getLikes()) {
            if(postLike.getUser() == tokena.get().getUser()){
                responseMap.put("error", "LIKE_ALREADY_EXISTS");
                return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
            }
        }
        PostLike postLike = new PostLike();
        postLike.setPost(post.get());
        postLike.setUser(usuario);

        postLikeRepository.save(postLike);


        responseMap.put("likeId", postLike.getId());
        responseMap.put("status", "LIKE_CREATED");
        return new ResponseEntity(responseMap, HttpStatus.OK);



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

    //GUARDAR UN POST
    @PostMapping(value = "/ws/post/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity guardarPost(
            @RequestBody FotoDescripcionFoto tokenDescripcionFoto
    ) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Post post = null;
        Optional<Token> opt = tokenRepository.findByCode(tokenDescripcionFoto.getCode());
        if (opt.isPresent()) {
            if (tokenDescripcionFoto.getDescription() != null && tokenDescripcionFoto.getMediaurl() != null) {
                post.setDescription(tokenDescripcionFoto.getDescription());
                post.setMediaUrl(tokenDescripcionFoto.getMediaurl());
                postRepository.save(post);
                responseMap.put("id", post.getId()); //si token es válido, se envía el id
                responseMap.put("status", "POST_CREATED"); //la variable de tipo HashMap envía la info
                return new ResponseEntity(responseMap, HttpStatus.OK);

            } else {
                if (tokenDescripcionFoto.getMediaurl() == null && tokenDescripcionFoto.getDescription() != null) {
                    responseMap.put("error", "EMPTY_FILE"); //la variable de tipo HashMap envía la info
                } else {
                    responseMap.put("error", "UPLOAD_ERROR"); //la variable de tipo HashMap envía la info
                }
                return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);

            }
        } else {
            //ERROR DE TOKEN
            responseMap.put("error", "TOKEN_INVALID"); //la variable de tipo HashMap envía la info
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);

        }
    }

    //GUARDAR UN COMENTARIO
    @PostMapping(value = "/ws/post/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity postComentario(
            @RequestBody TokenPostMsg tokenPostMsg) {

        HashMap<String, Object> responseMap = new HashMap<>();
        PostComment postComment = null;
        Optional<Token> opt = tokenRepository.findByCode(tokenPostMsg.getToken());

        if (opt.isPresent()) {
            postComment.setMessage(tokenPostMsg.getMessage());
            postCommentRepository.save(postComment);
            responseMap.put("commentId", postComment.getId());

            responseMap.put("status", "COMMENT_CREATED"); //la variable de tipo HashMap envía la info
            return new ResponseEntity(responseMap, HttpStatus.CREATED);

        } else {
            responseMap.put("error", "TOKEN_INVALID"); //la variable de tipo HashMap envía la info
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);

        }


    }
}
