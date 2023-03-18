package shop.mtcoding.hiberpc.model.reply;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.mtcoding.hiberpc.model.MyDummyEntity;
import shop.mtcoding.hiberpc.model.board.Board;
import shop.mtcoding.hiberpc.model.board.BoardRepository;
import shop.mtcoding.hiberpc.model.user.User;
import shop.mtcoding.hiberpc.model.user.UserRepository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({UserRepository.class, BoardRepository.class, ReplyRepository.class})
@DataJpaTest
public class ReplyRepositoryTest extends MyDummyEntity {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp(){
        User userPS = userRepository.save(newUser("ssar"));
        Board boardPS = boardRepository.save(newBoard("제목1", userPS));
        replyRepository.save(newReply("댓글1", userPS, boardPS));
        replyRepository.save(newReply("댓글2", userPS, boardPS));
        em.clear(); // 영속성 컨텍스트 비우기 (쿼리테스트)

        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE board_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE reply_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @Test
    public void save_test(){

    }

    @Test
    public void update_test(){

    }

    @Test
    public void delete_test(){

    }

    @Test
    public void findById_test(){

    }

    @Test
    public void findAll_test(){

    }
}
