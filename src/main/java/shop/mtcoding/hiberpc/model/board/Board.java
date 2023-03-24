package shop.mtcoding.hiberpc.model.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.hiberpc.model.user.User;

import javax.persistence.*;
import java.sql.Timestamp;

@Setter
@NoArgsConstructor
@Getter
@Table(name = "board_tb")
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // OneToMany는 디폴트 전략이 Lazy 이다.
    @ManyToOne(fetch = FetchType.LAZY)
    //@ManyToOne // 디폴트 전략은 EAGER
    private User user; // DB의 모든 칼럼은 데이터가 스칼라이다. (원자성) -> db에는 user_id

    //@JsonIgnoreProperties("board")
    // @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    //@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Reply> replyList = new ArrayList<>();

    private String title;
    private String content;

    @CreationTimestamp
    private Timestamp createdAt;

//    public void addReply(Reply reply){
//        replyList.add(reply);
//        reply.syncBoard(this);
//    }
//
//    public void removeReply(Reply reply){
//        replyList.remove(reply);
//        reply.syncBoard(null);
//    }

    @Builder
    public Board(Integer id, User user, String title, String content, Timestamp createdAt) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
