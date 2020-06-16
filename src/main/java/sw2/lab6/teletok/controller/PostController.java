package sw2.lab6.teletok.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sw2.lab6.teletok.entity.Post;
import sw2.lab6.teletok.repository.PostRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    PostRepository postRepository;

    @GetMapping(value = {"", "/"})
    public String listPost(Model model, RedirectAttributes attr) {
        List<Post> listapost = postRepository.findAll();
        Date now = new Date();// extraer fecha de hoy
        for (Post post : listapost) {
            Date dateresult = new Date((now.getTime() - post.getCreationDate().getTime()));
            post.setCreationDate(dateresult);
        }
        model.addAttribute("listapost", listapost);
        return "post/list";
    }

    @GetMapping("/post/new")
    public String newPost() {
        return "post/new";
    }

    @PostMapping("/post/save")
    public String savePost() {
        return "redirect:/";
    }

    @GetMapping("/post/file/{media_url}")
    public String getFile() {
        return "";
    }

    @GetMapping("/post/{id}")
    public String viewPost() {
        return "post/view";
    }

    @PostMapping("/post/comment")
    public String postComment() {
        return "";
    }

    @PostMapping("/post/like")
    public String postLike() {
        return "";
    }
}
