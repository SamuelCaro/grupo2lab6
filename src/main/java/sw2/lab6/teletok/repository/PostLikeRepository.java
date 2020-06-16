package sw2.lab6.teletok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sw2.lab6.teletok.entity.PostLike;

import java.util.List;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {
    @Query(value = "SELECT COUNT(*) as cantidad FROM post_like WHERE post_id = ?1",nativeQuery = true)
   Integer cantidadLikes(int id);
}
