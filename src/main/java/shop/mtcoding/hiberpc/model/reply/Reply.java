package shop.mtcoding.hiberpc.model.reply;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.hiberpc.model.board.Board;
import shop.mtcoding.hiberpc.model.user.User;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Table(name = "reply_tb")
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;
    @ManyToOne
    private Board board;

    private String comment;

    @CreationTimestamp
    private Timestamp createdAt;

    public void syncBoard(Board board){
        this.board = board;
    }

    @Builder
    public Reply(Integer id, User user, String comment, Timestamp createdAt) {
        this.id = id;
        this.user = user;
        this.comment = comment;
        this.createdAt = createdAt;
    }
}
