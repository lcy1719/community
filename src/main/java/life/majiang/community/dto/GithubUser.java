package life.majiang.community.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;//描述
    private String avatar_url;//头像
}
