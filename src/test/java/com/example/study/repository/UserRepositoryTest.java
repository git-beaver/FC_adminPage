package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.Item;
import com.example.study.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public class UserRepositoryTest extends StudyApplicationTests {

    @Autowired //Dependency Injection(의존성 주입) 직접 객체를 만들지 않고 객체를 스프링이 직접 관리하고 주입해주는 것 > new 이런거 안쓰고 자동으로 됨
    private UserRepository userRepository;

    @Test
    public void create() {
        User user = new User();
        user.setAccount("Heezzi");
        user.setEmail("heezzi@gmail.com");
        user.setPhoneNumber("010-1111-2222");
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy("heezzi");

        User newUser = userRepository.save(user);
        System.out.println("newUser : " + newUser);
    }

    @Test
    @Transactional
    public void read() {
        Optional<User> user = userRepository.findById(7L);

        user.ifPresent(selectUser -> {

            selectUser.getOrderDetailList().stream().forEach(detail -> {
                Item item = detail.getItem();
                System.out.println(detail.getItem());
            });

        });
    }

    @Test
    public void update() { //특정 user를 찾고, ID가 존재하는지 찾고, 업데이트를 진행
        Optional<User> user = userRepository.findById(3L);

        user.ifPresent(selectUser -> {
            selectUser.setAccount("PPPP");
            selectUser.setUpdatedAt(LocalDateTime.now());
            selectUser.setUpdatedBy("update method()");

            userRepository.save(selectUser); //이미 존재하는 ID가 있을 때 그 값을 변경하면 새로운 유저가 아닌 update를 해줌
        });
    }

    /*@DeleteMapping("/api/user")
    public void delete(@RequestParam Long id) {

    }*/

    @Test
    @Transactional //테스트이기에 쿼리는 실행하지만 데이터는 그대로 남기기 위해 다시 rollback을 해줌
    public void delete() {
        Optional<User> user = userRepository.findById(1L);

        Assertions.assertTrue(user.isPresent()); //값이 있어야 delete를 하니까 값이 있어야한다는 의미

        user.ifPresent(selectUser -> {
            userRepository.delete(selectUser);
        });

        Optional<User> deleteUser = userRepository.findById(1L);

        Assertions.assertFalse(deleteUser.isPresent()); //delete를 했으면 값이 없어야하기에 false여야한다는 의미

    }


}
