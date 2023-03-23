package shop.mtcoding.hiberpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import shop.mtcoding.hiberpc.model.board.Board;
import shop.mtcoding.hiberpc.model.board.BoardRepository;
import shop.mtcoding.hiberpc.model.reply.Reply;
import shop.mtcoding.hiberpc.model.reply.ReplyRepository;
import shop.mtcoding.hiberpc.model.user.User;
import shop.mtcoding.hiberpc.model.user.UserRepository;

import javax.persistence.EntityManager;
import java.util.Map;


@SpringBootApplication
public class HiberpcApplication extends MyDummyEntity{

    @Autowired
    private EntityManager em;


    @Bean
    CommandLineRunner init(UserRepository userRepository, BoardRepository boardRepository, ReplyRepository replyRepository){
        return (args)->{
            User ssarPS = userRepository.save(newUser("ssar"));
            User cosPS = userRepository.save(newUser("cos"));
            User lovePS = userRepository.save(newUser("love"));
            Board ssarBoardPS = boardRepository.save(newBoard("제목1", ssarPS));
            Board cosBoardPS = boardRepository.save(newBoard("제목2", cosPS));
            Board loveBoardPS = boardRepository.save(newBoard("제목3", lovePS));

            replyRepository.save(newReply("ssar 글 최고 form love", lovePS, ssarBoardPS));
            replyRepository.save(newReply("ssar 글 최고 form cos", cosPS, ssarBoardPS));
            replyRepository.save(newReply("cos 글 최고 form ssar", ssarPS, cosBoardPS));
            replyRepository.save(newReply("cos 글 최고 form love", lovePS, cosBoardPS));
            replyRepository.save(newReply("love 글 최고 form ssar", ssarPS, loveBoardPS));
        };
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HiberpcApplication.class, args);
        Map<String, Object> repositories = context.getBeansWithAnnotation(Repository.class);
        for (String key : repositories.keySet()) {
            System.out.println(key + " : " + repositories.get(key));
        }
    }

}
