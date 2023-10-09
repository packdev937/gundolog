package com.gundolog.api.repository;

import com.gundolog.api.entity.Session;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findByAccessToken(String accessToken);
}
