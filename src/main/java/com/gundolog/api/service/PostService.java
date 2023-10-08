package com.gundolog.api.service;

import com.gundolog.api.entity.Post;
import com.gundolog.api.entity.PostEditor;
import com.gundolog.api.exception.PostNotFoundException;
import com.gundolog.api.repository.PostRepository;
import com.gundolog.api.request.PostCreate;
import com.gundolog.api.request.PostEdit;
import com.gundolog.api.request.PostSearch;
import com.gundolog.api.response.PostResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        return PostResponse.builder()
            .title(post.getTitle())
            .content(post.getContent())
            .id(post.getId())
            .build();
    }

    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
            .map(PostResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

        // null에 대한 검증이 필요

        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();

        PostEditor postEditor = postEditorBuilder
            .title(postEdit.getTitle())
            .content(postEdit.getContent())
            .build();

        Post editedPost = post.edit(postEditor);
        return PostResponse.builder()
            .id(editedPost.getId())
            .title(editedPost.getTitle())
            .content(editedPost.getContent())
            .build();
    }

    public void delete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        postRepository.delete(post);
    }
}