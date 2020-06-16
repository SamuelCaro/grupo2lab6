package sw2.lab6.teletok.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sw2.lab6.teletok.entity.Post;
import sw2.lab6.teletok.repository.PostRepository;
import java.util.Date;
import org.springframework.validation.BindingResult;
import sw2.lab6.teletok.entity.PostComment;
import sw2.lab6.teletok.entity.User;
import sw2.lab6.teletok.repository.PostCommentRepository;
import sw2.lab6.teletok.repository.PostLikeRepository;
import sw2.lab6.teletok.repository.UserRepository;

import javax.servlet.http.HttpSession;
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
