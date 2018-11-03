package com.kmk.imageboard.service;

import com.kmk.imageboard.repository.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticService {

    @Autowired
    StatisticRepository statisticRepository;

    public List<Object> getTopBrowsers() {
        return statisticRepository.getDistinctTopByBrowser();
    }

    public List<Object> getTopOSs() {
        return statisticRepository.getDistinctTopByOs();
    }

    public List<Integer> getTrafficByHour() {
        List<Integer> trafficList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            trafficList.add(statisticRepository.getRequestsByHour(i));
        }
        return trafficList;
    }
}
