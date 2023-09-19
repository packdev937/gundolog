package com.gundolog.api.repository;

import com.gundolog.api.entity.Post;
import com.gundolog.api.entity.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(int page) {
        return jpaQueryFactory.selectFrom(QPost.post)
            .limit(10)
            .offset((long) (page - 1) * 10)
            .orderBy(QPost.post.id.desc())
            .fetch();
    }
}
