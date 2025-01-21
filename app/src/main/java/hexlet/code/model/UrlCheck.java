package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class UrlCheck {
    private Long id;
    private Timestamp createdAt;
    private Integer statusCode;
    private String title;
    private String h1;
    private String description;
    private Long urlId;

    public UrlCheck(int statusCode, String title, String h1, String description, Long urlId) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.urlId = urlId;
    }
}