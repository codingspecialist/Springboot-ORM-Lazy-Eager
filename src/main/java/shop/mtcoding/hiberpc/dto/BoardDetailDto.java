package shop.mtcoding.hiberpc.dto;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.hiberpc.model.board.Board;
import shop.mtcoding.hiberpc.model.user.User;

import java.sql.Timestamp;

@Getter
@Setter
public class BoardDetailDto {
    private Integer id;
    private String title;
    private String content;
    private Timestamp createdAt;
    private Integer uId;
    private String uUsername;

    public Board toEntity(){
        return Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .createdAt(createdAt)
                .user(User.builder().id(uId).username(uUsername).build())
                .build();
    }
}
