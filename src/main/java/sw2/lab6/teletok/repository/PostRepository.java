package sw2.lab6.teletok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sw2.lab6.teletok.entity.Post;
import sw2.lab6.teletok.entity.PostComment;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {


    @Query(value = "SELECT * FROM post where description = ?1 ORDER BY creation_date DESC;",nativeQuery = true)
    List<Post> listarFechaDesc(String query);

    @Query(value = "SELECT count(*) as cantidadComments from post_comment where id=1?;",nativeQuery = true)
    Integer cantidadComments(int id);

    @Query(value = "SELECT COUNT(*) as cantidadLikes FROM post_like WHERE post_id = ?1",nativeQuery = true)
    Integer cantidadLikes(int id);



}
