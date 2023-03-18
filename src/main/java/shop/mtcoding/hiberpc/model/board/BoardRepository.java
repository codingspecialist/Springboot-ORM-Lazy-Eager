package shop.mtcoding.hiberpc.model.board;

import org.springframework.stereotype.Repository;
import shop.mtcoding.hiberpc.model.MyRepository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class BoardRepository extends MyRepository<Board> {

    private EntityManager em;

    public BoardRepository(EntityManager em) {
        super(em);
        this.em = em;
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
