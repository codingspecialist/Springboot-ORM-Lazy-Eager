package shop.mtcoding.hiberpc.model.reply;


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

    @Test
    public void findById_test() {

    }

    @Test
    public void save_test() {

    }

    @Test
    public void update_test() {

    }

    @Test
    public void delete_test() {

    }

    @Test
    public void findAll_test() {

    }

    @BeforeEach
    public void setUp() {
        User ssarPS = userRepository.save(newUser("ssar"));
        User cosPS = userRepository.save(newUser("cos"));
        User lovePS = userRepository.save(newUser("love"));
        Board ssarBoardPS = boardRepository.save(newBoard("제목1", ssarPS));
        Board cosBoardPS = boardRepository.save(newBoard("제목2", cosPS));
        Board loveBoardPS = boardRepository.save(newBoard("제목3", lovePS));
        ssarBoardPS.addReply(newReply("ssar 글 최고 form love", lovePS));
        ssarBoardPS.addReply(newReply("ssar 글 최고 form cos", cosPS));
        cosBoardPS.addReply(newReply("cos 글 최고 form ssar", ssarPS));
        cosBoardPS.addReply(newReply("cos 글 최고 form love", lovePS));
        loveBoardPS.addReply(newReply("love 글 최고 form ssar", ssarPS));

        em.flush();
        em.clear();

        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE board_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE reply_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }
}
