package com.kjh.boardback.domain.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kjh.boardback.domain.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

    Optional<User> findByNickname(String nickname);
    Optional<User> findByTelNumber(String telNumber);
    Optional<User> findByEmail(String email);
    
}
