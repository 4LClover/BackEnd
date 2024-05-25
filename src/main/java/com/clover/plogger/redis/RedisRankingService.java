package com.clover.plogger.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisRankingService {

    private static final String RANKING_KEY = "userRanking";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final ZSetOperations<String, Object> zSetOperations;

    @Autowired
    public RedisRankingService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.zSetOperations = redisTemplate.opsForZSet();
    }

    // 주어진 사용자의 ID와 점수를 받아서 Redis의 Sorted Set에 사용자를 추가
    public void addUserScore(String userId, double score) {
        zSetOperations.add(RANKING_KEY, userId, score);
    }

    // 상위 n개의 사용자를 조회하여 반환
    public Set<Object> getTopUsers(int count) {
        return zSetOperations.reverseRange(RANKING_KEY, 0, count - 1);
    }

    // 주어진 사용자의 랭킹을 조회하여 반환
    public Long getUserRank(String userId) {
        return zSetOperations.reverseRank(RANKING_KEY, userId);
    }

    // 주어진 사용자의 점수를 조회하여 반환
    public Double getUserScore(String userId) {
        return zSetOperations.score(RANKING_KEY, userId);
    }

    // 사용자의 점수를 업데이트
    public void updateUserScore(String userId, double score) {
        zSetOperations.add(RANKING_KEY, userId, score);
    }
}
