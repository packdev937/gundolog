package com.gundolog.api.entity;


import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_at")
    private LocalDateTime createdDate;
    @Column(name = "updated_at")
    private LocalDateTime updatedDate;
    private String title;
    @Lob // Database에서 Long Text 형태로 갖고 있도록
    private String content;

    @Builder
    public Post(Long id, LocalDateTime createdDate, LocalDateTime updatedDate, String title,
        String content) {
        this.id = id;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.title = title;
        this.content = content;
    }

    public void change(String title, String content) {
        if (title != null) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
    }

    public PostEditor.PostEditorBuilder toEditor() {
        // 여기 있는건 디폴트 값인가?
        return PostEditor.builder()
            .title(title)
            .content(content);
    }

    public Post edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        content = postEditor.getContent();
        return this;
    }
}
