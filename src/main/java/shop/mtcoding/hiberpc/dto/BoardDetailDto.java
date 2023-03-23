package shop.mtcoding.hiberpc.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter @Setter
public class BoardDetailDto {
    private Integer id;
    private String title;
    private String content;
    private Timestamp createdAt;
    private Integer uId;
    private String uUsername;
    private Integer rId;
    private String rComment;
    private Integer ruId;
    private String ruUsername;
}
