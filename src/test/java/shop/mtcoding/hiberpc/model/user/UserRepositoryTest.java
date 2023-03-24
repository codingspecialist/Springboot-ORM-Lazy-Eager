package shop.mtcoding.hiberpc.model.user;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.mtcoding.hiberpc.model.MyDummyEntity;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(UserRepository.class)
@DataJpaTest
public class UserRepositoryTest extends MyDummyEntity {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp(){
        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @Test
    public void save_test(){
        // given
        User user = newUser("ssar");

        // when
        User userPS = userRepository.save(user);

        // then
        assertThat(userPS.getId()).isEqualTo(1);
    }

    @Test
    public void findById_test(){
        // given 1
        userRepository.save(newUser("ssar"));
        em.clear();

        // given 2
        int id = 1;

        // when
        User userPS = userRepository.findById(id);

        // then
        assertThat(userPS.getUsername()).isEqualTo("ssar");
    }

    @Test
    public void update_test(){
        // given 1
        userRepository.save(newUser("ssar"));
        em.clear(); // 준영속

        // given 2
        String password = "5678";
        String email = "ssar@gmail.com";

        // when
        // update 하는 법 : 1. 영속화 시킨다, 2. 변경한다. 3. 트랜잭션이 종료된다.(변경감지, flush)
        User userPS = userRepository.findById(1); // 캐싱을 못했음.
        userPS.update(password, email); // 영속화 된것을 변경
        User updateUserPS = userRepository.save(userPS);
        em.flush(); // hibernate가 변경감지

        // then
        assertThat(updateUserPS.getPassword()).isEqualTo("5678");
    } // rollback

    @Test
    public void update_dutty_checking_test(){
        // given 1
        userRepository.save(newUser("ssar"));
        em.clear();

        // given 2
        String password = "5678";
        String email = "ssar@gmail.com";

        // when
        User userPS = userRepository.findById(1);
        userPS.update(password, email);
        em.flush();

        // then
        User updateUserPS = userRepository.findById(1);
        assertThat(updateUserPS.getPassword()).isEqualTo("5678");
        assertThat(updateUserPS.getEmail()).isEqualTo("ssar@gmail.com");
    }

    @Test
    public void delete_test(){
        // given 1
        userRepository.save(newUser("ssar"));
        em.clear();

        // given 2
        int id = 1;
        User findUserPS = userRepository.findById(id);

        // when
        userRepository.delete(findUserPS);

        // then
        User deleteUserPS = userRepository.findById(1);
        Assertions.assertThat(deleteUserPS).isNull();
    } // rollback

    @Test
    public void findAll_test(){
        // given
        List<User> userList = Arrays.asList(newUser("ssar"), newUser("cos"));
        userList.stream().forEach((user)->{
            userRepository.save(user);
        });

        // when
        List<User> userListPS = userRepository.findAll();

        // then
        assertThat(userListPS.size()).isEqualTo(2);
    }
}
