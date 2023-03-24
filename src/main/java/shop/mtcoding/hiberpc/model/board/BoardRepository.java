package shop.mtcoding.hiberpc.model.board;

import org.springframework.stereotype.Repository;
import shop.mtcoding.hiberpc.dto.BoardDetailDto;
import shop.mtcoding.hiberpc.model.MyRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BoardRepository extends MyRepository<Board> {

    private EntityManager em;

    public BoardRepository(EntityManager em) {
        super(em);
        this.em = em;
    }

    public Board findByIdNativeQueryV2(int id) {
        Query query = em.createNativeQuery(
                "select b.id, b.title, b.content, b.created_at, " +
                        "u.id u_id, u.username u_username " +
                        "from board_tb b " +
                        "inner join user_tb u on b.user_id = u.id " +
                        "where b.id = :id");
        query.setParameter("id", id);
        Object[] rs = (Object[]) query.getSingleResult();

        BoardDetailDto dto = new BoardDetailDto();
        dto.setId((Integer) rs[0]);
        dto.setTitle((String) rs[1]);
        dto.setContent((String) rs[2]);
        dto.setCreatedAt((Timestamp) rs[3]);
        dto.setUId((Integer) rs[4]);
        dto.setUUsername((String) rs[5]);


        Board board = dto.toEntity();
        // board에 직접 dto 값 매핑하기
        return board;
    }

    public List<Board> findAllJoin() {
        return em.createQuery("select b from Board b join b.user", getEntityClass()).getResultList();
    }

    public List<Board> findAllJoinFetch() {
        return em.createQuery("select b from Board b join fetch b.user", getEntityClass()).getResultList();
    }

    @Override
    protected Class<Board> getEntityClass() {
        return Board.class;
    }

    @Override
    protected String getEntityName() {
        return "Board";
    }
}
