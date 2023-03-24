package shop.mtcoding.hiberpc.model.board;

import org.springframework.stereotype.Repository;
import shop.mtcoding.hiberpc.dto.BoardDetailDto;
import shop.mtcoding.hiberpc.dto.BoardDto;
import shop.mtcoding.hiberpc.model.MyRepository;
import shop.mtcoding.hiberpc.model.user.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class BoardRepository extends MyRepository<Board> {

    private EntityManager em;

    public BoardRepository(EntityManager em) {
        super(em);
        this.em = em;
    }

    public List<Board> findAllAndInQueryV2(){
        List<Object[]> rsBoardList = em.createNativeQuery("select id, user_id, title, content, created_at from board_tb").getResultList();
        List<BoardDto> boardDtoList = new ArrayList<>();
        for (Object[] rs : rsBoardList){
            BoardDto boardDto = new BoardDto();
            boardDto.setId((Integer) rs[0]);
            boardDto.setUserId((Integer) rs[1]);
            boardDto.setTitle((String) rs[2]);
            boardDto.setContent((String) rs[3]);
            boardDto.setCreatedAt((Timestamp) rs[4]);
            boardDtoList.add(boardDto);
        }

        List<Integer> userIds = boardDtoList.stream().map((dto)-> dto.getUserId()).collect(Collectors.toList());
        StringBuilder queryBuilder = new StringBuilder("select id, username, password, email, created_at from user_tb where id in (");
        for (int i = 0; i < userIds.size(); i++) {
            queryBuilder.append("?");
            if (i != userIds.size() - 1) {
                queryBuilder.append(",");
            }
        }
        queryBuilder.append(")");
        Query query = em.createNativeQuery(queryBuilder.toString());
        for (int i = 0; i < userIds.size(); i++) {
            query.setParameter(i + 1, userIds.get(i));
        }

        List<Object[]> rsUserList = query.getResultList();
        List<User> userList = new ArrayList<>();
        for (Object[] rs : rsUserList) {
            User user = User.builder()
                    .id((Integer) rs[0])
                    .username((String) rs[1])
                    .password((String) rs[2])
                    .email((String) rs[3])
                    .createdAt((Timestamp) rs[4]).build();
            userList.add(user);
        }

        List<Board> boardList = boardDtoList.stream().map((boardDto)->{
            User user =  userList.stream().filter((item)-> boardDto.getUserId().equals(item.getId())).findFirst().orElse(null);
            return boardDto.toEntity(user);
        }).collect(Collectors.toList());

        return boardList;
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
