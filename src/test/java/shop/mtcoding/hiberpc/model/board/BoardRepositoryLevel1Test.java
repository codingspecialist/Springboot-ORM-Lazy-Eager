package shop.mtcoding.hiberpc.model.board;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.mtcoding.hiberpc.model.MyDummyEntity;
import shop.mtcoding.hiberpc.model.user.User;
import shop.mtcoding.hiberpc.model.user.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({UserRepository.class, BoardRepository.class})
@DataJpaTest
public class BoardRepositoryLevel1Test extends MyDummyEntity {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp(){
        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE board_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @Test
    public void save2_test(){
        userRepository.save(newUser("ssar"));
        User user = new User(); // 비영속
        user.setId(1);

        Board board = newBoard("제목1", user);
        boardRepository.save(board); // 제약조건 위배
    }

    @Test
    public void save_test(){ // @ManyToOne(User, Eager)
        // given
        userRepository.save(newUser("ssar"));
        em.clear();

        User userPS = userRepository.findById(1);
        Board board = newBoard("제목1", userPS); // 비영속

        // when
        Board boardPS = boardRepository.save(board);
        System.out.println("테스트 : "+boardPS);
//
//        // then
        assertThat(boardPS.getId()).isEqualTo(1);
        assertThat(boardPS.getUser().getId()).isEqualTo(1);
    }

    @Test
    public void findById_test() throws JsonProcessingException { // @ManyToOne(User, Eager)
        // given 1
        User userPS = userRepository.save(newUser("ssar"));
        boardRepository.save(newBoard("제목1", userPS));
        em.clear();

        // given 2
        int id = 1;

        // when
        // 연관관계의 주인은 조회
        // 1. 주인이 아닌애들이 있는지 찾는다. (User)
        // 2. 연관관계에 있는 애들을 조인해서 가져온다.
        // 3. Flat -> ORM
        Board boardPS = boardRepository.findById(id);
        //ObjectMapper om = new ObjectMapper();
        //String responseBody = om.writeValueAsString(boardPS);
        //System.out.println(responseBody);

        // then
        assertThat(boardPS.getTitle()).isEqualTo("제목1");
    }

    @Test
    public void update_test(){ // @ManyToOne(User, Eager)
        // given 1
        User userPS = userRepository.save(newUser("ssar"));
        boardRepository.save(newBoard("제목1", userPS));
        em.clear();

        // given 2
        String title = "제목12";
        String content = "내용12";

        // when
        Board boardPS = boardRepository.findById(1);
        boardPS.update(title, content);
        em.flush();
        em.clear();

        // then
        Board findBoardPS = boardRepository.findById(1);
        assertThat(findBoardPS.getContent()).isEqualTo("내용12");
    }

    @Test
    public void delete_test(){ // @ManyToOne(User, Eager)
        // given 1
        User userPS = userRepository.save(newUser("ssar"));
        boardRepository.save(newBoard("제목1", userPS));
        em.clear();

        // given 2
        int id = 1;
        Board findBoardPS = boardRepository.findById(id);

        // when
        boardRepository.delete(findBoardPS);
        em.flush();
        em.clear();

        // then
        Board deleteBoardPS = boardRepository.findById(1);
        Assertions.assertThat(deleteBoardPS).isNull();
    }



    // board 데이터 100건 (user가 다다름) -> board select, user select 100번
    @Test
    public void findAll_test(){ // @ManyToOne(User, Eager)
        // given
        findAll_given();

        // when
        // N + 1 문제 -> in query -> default_batch_fetch_size
        // 1. 주인만 select 한다.
        // 2. 주인이 아닌 애들은 각각 select 한다
        List<Board> boardListPS = boardRepository.findAll();
        System.out.println("테스트 : "+boardListPS);

        // then
        assertThat(boardListPS.size()).isEqualTo(3);
    }

    private void findAll_given(){
        User ssarPS = userRepository.save(newUser("ssar"));
        User cosPS = userRepository.save(newUser("cos"));
        User lovePS = userRepository.save(newUser("love"));
        List<Board> boardList = Arrays.asList(
                newBoard("제목1", ssarPS),
                newBoard("제목2", ssarPS),
                newBoard("제목3", lovePS));
        boardList.stream().forEach((board)->{
            boardRepository.save(board);
        });
        em.clear();
    }
}
