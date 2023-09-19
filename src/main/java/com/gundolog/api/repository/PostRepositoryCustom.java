package com.gundolog.api.repository;

import com.gundolog.api.entity.Post;
import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(int page);
}
