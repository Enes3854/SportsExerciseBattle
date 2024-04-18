package com.seb.service;

import com.seb.dao.PushupRecordDao;
import com.seb.model.PushupRecord;
import com.seb.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsService {

    private final UserService userService = new UserService();
    private final PushupRecordDao pushupRecordDao = new PushupRecordDao();

    public Map<Long, Integer> getTotalPushupsPerUser() {
        List<PushupRecord> allRecords = pushupRecordDao.findAllRecords();
        Map<Long, Integer> totals = new HashMap<>();
        for (PushupRecord record : allRecords) {
            totals.merge(record.getUserId(), record.getCount(), Integer::sum);
        }
        return totals;
    }

    public int getTotalPushupsForUser(Long userId) {
        return pushupRecordDao.findRecordsByUserId(userId).stream()
                .mapToInt(PushupRecord::getCount)
                .sum();
    }

    public List<PushupRecord> getUserHistory(Long userId) {
        return pushupRecordDao.findRecordsByUserId(userId);
    }

    public Map<String, Map<String, Object>> getScoreboard() {
        List<User> users = userService.findAllUsers();
        Map<String, Map<String, Object>> scoreboard = new HashMap<>();

        for (User user : users) {
            int totalPushups = pushupRecordDao.findRecordsByUserId(user.getId()).stream()
                    .mapToInt(PushupRecord::getCount)
                    .sum();
            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("eloScore", user.getEloScore());
            userDetails.put("totalPushups", totalPushups);

            scoreboard.put(user.getUsername(), userDetails);
        }

        return scoreboard;
    }

}
