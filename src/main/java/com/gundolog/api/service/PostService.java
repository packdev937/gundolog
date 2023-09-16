package com.gundolog.api.service;

import com.gundolog.api.entity.Post;
import com.gundolog.api.repository.PostRepository;
import com.gundolog.api.request.PostCreate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        // Entity로 변환
        Post newPost = Post.builder()
            .title(postCreate.getTitle())
            .content(postCreate.getContent())
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

        postRepository.save(newPost);
    }
}
