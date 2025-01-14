package com.kjh.boardback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kjh.boardback.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

    
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByTelNumber(String telNumber);
    User findByEmail(String email);
    
}
