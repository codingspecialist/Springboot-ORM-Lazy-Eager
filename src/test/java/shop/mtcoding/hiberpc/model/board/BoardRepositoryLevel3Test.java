package shop.mtcoding.hiberpc.model.board;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.mtcoding.hiberpc.dto.BoardRespDto;
import shop.mtcoding.hiberpc.model.MyDummyEntity;
import shop.mtcoding.hiberpc.model.reply.ReplyRepository;
import shop.mtcoding.hiberpc.model.user.User;
import shop.mtcoding.hiberpc.model.user.UserRepository;

import javax.persistence.EntityManager;

@Import({UserRepository.class, BoardRepository.class, ReplyRepository.class})
@DataJpaTest
public class BoardRepositoryLevel3Test extends MyDummyEntity {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private EntityManager em;

    @Test
    public void setUp_test(){}

    @Test
    public void findByIdTwoWay_test(){ // @ManyToOne(User, Eager), @OneToMany(Reply, Lazy)
        // given
        int id = 1;

        // when
        boardRepository.findById(id);

        // then
    }

    @Test
    public void findByIdTwoWayEager_test(){ // @ManyToOne(User, Eager), @OneToMany(Reply, Eager)
        // given
        int id = 1;

        // when
        boardRepository.findById(id);

        // then
    }

    // @ManyToOne(User, Eager), @OneToMany(Reply, Lazy)
    @Test
    public void findByIdTwoWayJsonFail_test() throws Exception {
        // given
        int id = 1;

        // when
        Board boardPS = boardRepository.findById(id);

        // then
        try {
            ObjectMapper om = new ObjectMapper();
            String boardJson = om.writeValueAsString(boardPS);
            System.out.println(boardJson);
        }catch (Exception e){
            System.out.println("테스트 : 무한루프 "+e.getMessage());
        }
    }

    // @JsonIgnoreProperties("board")
    // @ManyToOne(User, Eager), @OneToMany(Reply, Lazy)
    @Test
    public void findByIdTwoWayJsonIgnoreProperties_test() throws Exception {
        // given
        int id = 1;

        // when
        Board boardPS = boardRepository.findById(id);

        // then
        ObjectMapper om = new ObjectMapper();
        String boardJson = om.writeValueAsString(boardPS);
        System.out.println("테스트 : "+boardJson);
    }

    // Dto
    // @ManyToOne(User, Eager), @OneToMany(Reply, Lazy)
    @Test
    public void findByIdTwoWayJsonDto_test() throws Exception {
        // given
        int id = 1;

        // when
        Board boardPS = boardRepository.findById(id);

        // then
        BoardRespDto boardRespDto = new BoardRespDto(boardPS);
        ObjectMapper om = new ObjectMapper();
        String boardRespJson = om.writeValueAsString(boardRespDto);
        System.out.println("테스트 : "+boardRespJson);
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
