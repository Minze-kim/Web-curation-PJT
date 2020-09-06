
package com.nuga.curation.repository.user;

import com.nuga.curation.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {
    User findUserByNickName(String nickName);
    User getUserByEmail(String email);
    User findUserByEmail(String email);
    User getUserByEmailOrNickName(String email,String nickname);
    Optional<User> findUserByEmailAndPassword(String email, String password);
    List<User> findUserByNickNameContaining(String nickName);
}
