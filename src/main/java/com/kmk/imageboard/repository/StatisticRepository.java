package com.kmk.imageboard.repository;

import com.kmk.imageboard.model.RequestData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatisticRepository extends JpaRepository<RequestData, Long> {

    RequestData findBySessionId(String sessionId);

    @Query(nativeQuery = true,
            value = "select distinct browser, count(browser) from request_data group by browser order by count(browser) desc limit 5")
    List<Object> getDistinctTopByBrowser();

    @Query(nativeQuery = true,
            value = "select distinct os, count(os) from request_data group by os order by count(os) desc limit 5")
    List<Object> getDistinctTopByOs();

    @Query(nativeQuery = true,
            value = "select count(*) from request_data where hour(time_of_request) = :hour")
    Integer getRequestsByHour(@Param("hour") Integer hour);
}
