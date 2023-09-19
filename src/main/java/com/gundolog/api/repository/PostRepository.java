package com.gundolog.api.repository;

import com.gundolog.api.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

}

// PostRepositoryImpl 기능들이 자동으로 주입이 된다 -> 어떻게?