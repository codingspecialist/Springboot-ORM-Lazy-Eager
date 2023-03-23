package shop.mtcoding.hiberpc.model.board;

import org.springframework.stereotype.Repository;
import shop.mtcoding.hiberpc.dto.BoardDetailDto;
import shop.mtcoding.hiberpc.model.MyRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BoardRepository extends MyRepository<Board> {

    private EntityManager em;

    public BoardRepository(EntityManager em) {
        super(em);
        this.em = em;
    }

    public Board findByIdJPQL(int id){
        return em.createQuery("select b from Board b join fetch b.user left join fetch b.replyList where b.id = :id", getEntityClass()).setParameter("id", id).getSingleResult();
    }

    public List<BoardDetailDto> findByIdNativeQuery(int id){
        Query query = em.createNativeQuery(
                        "select b.id, b.title, b.content, b.created_at," +
                                "u.id u_id, u.username u_username," +
                                "r.id r_id, r.comment r_comment," +
                                "ru.id ru_id, ru.username ru_username " +
                                "from board_tb b " +
                                "inner join user_tb u on b.user_id = u.id " +
                                "left outer join reply_tb r on b.id = r.user_id " +
                                "inner join user_tb ru on r.user_id = ru.id " +
                                "where b.id = :id");
        query.setParameter("id", id);
        List<Object[]> rsList = query.getResultList();

        List<BoardDetailDto> dtos = new ArrayList<>();
        for (Object[] rs: rsList) {
            BoardDetailDto dto = new BoardDetailDto();
            dto.setId((Integer)rs[0]);
            dto.setTitle((String)rs[1]);
            dto.setContent((String)rs[2]);
            dto.setCreatedAt((Timestamp)rs[3]);
            dto.setUId((Integer)rs[4]);
            dto.setUUsername((String)rs[5]);
            dto.setRId((Integer)rs[6]);
            dto.setRComment((String)rs[7]);
            dto.setRuId((Integer)rs[8]);
            dto.setRuUsername((String)rs[9]);
            dtos.add(dto);
        }
        return dtos;
    }

    public List<Board> findAllJoin(){
        return em.createQuery("select b from Board b join b.user", getEntityClass()).getResultList();
    }

    public List<Board> findAllJoinFetch(){
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
