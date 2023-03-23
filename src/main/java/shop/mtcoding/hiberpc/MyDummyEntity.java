package shop.mtcoding.hiberpc;

import shop.mtcoding.hiberpc.model.board.Board;
import shop.mtcoding.hiberpc.model.reply.Reply;
import shop.mtcoding.hiberpc.model.user.User;

public class MyDummyEntity {

    protected User newUser(String username){
        return User.builder()
                .username(username)
                .password("1234")
                .email(username+"@nate.com")
                .build();
    }


    protected Board newBoard(String title, User userPS){

        if(userPS.getId() == null){
            System.out.println("영속화해서 넣어라!!");
            return null;
        }

        return Board.builder()
                .title(title)
                .content(title)
                .user(userPS)
                .build();
    }

    protected Reply newReply(String comment, User userPS, Board board){
        if(userPS.getId() == null){
            System.out.println("영속화해서 넣어라!!");
            return null;
        }

        return Reply.builder()
                .user(userPS)

                .comment(comment)
                .build();
    }
}
