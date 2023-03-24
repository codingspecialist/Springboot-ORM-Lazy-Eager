package shop.mtcoding.hiberpc.dto;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.hiberpc.model.board.Board;
import shop.mtcoding.hiberpc.model.user.User;

import java.sql.Timestamp;
import java.util.List;

@Getter @Setter
public class BoardDto {
    private Integer id;
    private String title;
    private String content;
    private Integer userId;
    private Timestamp createdAt;

    public Board toEntity(User user){
        return Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .createdAt(createdAt)
                .user(user)
                .build();
    }
}
