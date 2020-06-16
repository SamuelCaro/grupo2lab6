package sw2.lab6.teletok.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sw2.lab6.teletok.entity.Post;
import sw2.lab6.teletok.entity.User;
import sw2.lab6.teletok.repository.PostRepository;
import sw2.lab6.teletok.service.StorageService;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sw2.lab6.teletok.entity.Post;
import sw2.lab6.teletok.entity.PostComment;
import sw2.lab6.teletok.entity.PostLike;
import sw2.lab6.teletok.entity.User;
import sw2.lab6.teletok.repository.PostCommentRepository;
import sw2.lab6.teletok.repository.PostLikeRepository;
import sw2.lab6.teletok.repository.PostRepository;
import sw2.lab6.teletok.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {


    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostLikeRepository postLikeRepository;

    @Autowired
    PostCommentRepository postCommentRepository;

    @GetMapping(value = {"", "/"})
    public String listPost() {
        return "post/list";
    }

    @GetMapping("/post/new")

    public String newPost(@ModelAttribute("post") Post post, Model model) {
        List<Post> listaPosts = postRepository.findAll();
        model.addAttribute("listaPosts", listaPosts);
        return "post/new";
    }

    @PostMapping("/post/save")
    public String savePost(@ModelAttribute("post") @Valid Post post, BindingResult bindingResult, Model model,
                           HttpSession session, RedirectAttributes attr,
                           @RequestParam("archivo") MultipartFile file) {

        if (bindingResult.hasErrors()) {
            List<Post> listaPosts = postRepository.findAll();
            model.addAttribute("listaPosts", listaPosts);
            return "post/new";
        } else {

            User u = (User) session.getAttribute("user");
            post.setUser(u);
            post.setCreationDate(new Date());
            StorageService storageService = new StorageService();
            HashMap<String, String> map = storageService.store(file);
            if (map.get("estado").equals("exito")) {
                post.setMediaUrl(map.get("fileName"));
                postRepository.save(post);
                return "redirect:/";

            } else {
                model.addAttribute("msg", map.get("msg"));
                return "post/new";
            }
        }
    }

    @GetMapping("/post/file/{media_url}")
    public String getFile() {
        return "";
    }

    @GetMapping("/post/{id}")
    public String viewPost(Model model, @RequestParam("id") int id, RedirectAttributes attr) {
        User user = postRepository.findById(id).get().getUser();
        Integer cantidadLikes = postLikeRepository.cantidadLikes(id);
        List<PostComment> listaComentario = postCommentRepository.findByPost(postRepository.findById(id).get());

        model.addAttribute("postAver", postRepository.findById(id));
        model.addAttribute("usuarioDePost", user);
        model.addAttribute("cantidadLikes", cantidadLikes);
        model.addAttribute("listaComentarios",listaComentario);

        return "post/view";
    }

    @PostMapping("/post/comment")
    public String postComment(@ModelAttribute("comentario") PostComment postComment,
                              BindingResult bindingResult,
                              RedirectAttributes attr,
                              Model model,
                              HttpSession session) {


        return "redirect:/post/{id}";
    }

    @PostMapping("/post/like")
    public String postLike() {


        return "";
    }
}
