package sw2.lab6.teletok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sw2.lab6.teletok.entity.Token;
import sw2.lab6.teletok.entity.User;

import java.util.Optional;

import java.util.Optional;

@Repository
public interface TokenRepository  extends JpaRepository<Token,Integer> {
    public Optional<Token> findByCode(String code);
}
