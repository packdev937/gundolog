package com.gundolog.api.repository;

import com.gundolog.api.entity.Post;
import com.gundolog.api.entity.QPost;
import com.gundolog.api.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(QPost.post)
            .limit(postSearch.getSize())
            .offset(postSearch.getOffset())
            .orderBy(QPost.post.id.desc())
            .fetch();
    }
}
