package com.gundolog.api.repository;

import com.gundolog.api.entity.Post;
import com.gundolog.api.request.PostSearch;
import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
