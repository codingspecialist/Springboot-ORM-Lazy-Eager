package shop.mtcoding.hiberpc.dto;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.hiberpc.model.board.Board;
import shop.mtcoding.hiberpc.model.reply.Reply;
import shop.mtcoding.hiberpc.model.user.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * findByIdTwoWayJsonDto_test() 테스트할 때 사용
 * DTO는 재사용하지 않는 것을 추천한다. 화면은 계속 변한다.
 */
@Getter @Setter
public class BoardRespDto {
    private Integer id;
    private String title;
    private String content;
    private String createdAt;
    private UserDto user;
    private List<ReplyDto> replyList;

    public BoardRespDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt().toLocalDateTime().toString();
        this.user = new UserDto(board.getUser());
        this.replyList = board.getReplyList().stream().map(
                (reply)-> new ReplyDto(reply)
        ).collect(Collectors.toList());
    }

    @Getter @Setter
    class UserDto {
        private Integer id;
        private String username;
        private String email;
        private String createdAt;

        public UserDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.createdAt = user.getCreatedAt().toLocalDateTime().toString();
        }
    }

    @Getter @Setter
    class ReplyDto {
        private Integer id;
        private UserDto user;
        private String comment;
        private String createdAt;

        public ReplyDto(Reply reply) {
            this.id = reply.getId();
            this.user = new UserDto(reply.getUser());
            this.comment = reply.getComment();
            this.createdAt = reply.getCreatedAt().toLocalDateTime().toString();
        }
    }
}