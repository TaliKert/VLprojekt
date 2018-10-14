package com.kmk.imageboard.repository;

import com.kmk.imageboard.model.RequestData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticRepository extends JpaRepository<RequestData, Long> {

    RequestData findBySessionId(String sessionId);
}
