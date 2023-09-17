package com.gundolog.api.service;

import com.gundolog.api.entity.Post;
import com.gundolog.api.repository.PostRepository;
import com.gundolog.api.request.PostCreate;
import com.gundolog.api.response.PostResponse;
import java.time.LocalDateTime;
import java.util.Optional;
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

    public PostResponse get(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 글입니다")
        );

        PostResponse response = PostResponse.builder()
            .title(post.getTitle())
            .content(post.getContent())
            .id(post.getId())
            .build();

        return response;
    }

}
