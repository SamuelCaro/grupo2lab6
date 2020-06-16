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
import java.util.List;

@Controller
public class PostController {


    @Autowired
    PostRepository postRepository;

    @GetMapping(value = {"", "/"})
    public String listPost(Model model) {
        List<Post> listaPosts = postRepository.findAll();
        model.addAttribute("listaPosts", listaPosts);
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
