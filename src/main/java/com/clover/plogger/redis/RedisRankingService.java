package com.clover.plogger.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisRankingService {

    private static final String RANKING_KEY = "userRanking";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final ZSetOperations<String, String> zSetOperations;

    @Autowired
    public RedisRankingService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.zSetOperations = redisTemplate.opsForZSet();
    }

    // 주어진 사용자의 ID와 점수를 받아서 Redis의 Sorted Set에 사용자를 추가
    public void addUserScore(String userId, double score) {
        zSetOperations.add(RANKING_KEY, userId, score);
    }

    // 상위 n개의 사용자를 조회하여 반환
    public Set<ZSetOperations.TypedTuple<String>> getTopUsersWithScores(int count) {
        return zSetOperations.reverseRangeWithScores(RANKING_KEY, 0, count - 1);
    }

    // 주어진 사용자의 랭킹을 조회하여 반환
    public Long getUserRank(String userId) {
        return zSetOperations.reverseRank(RANKING_KEY, userId);
    }

    // 사용자의 점수를 업데이트 (트랜잭션으로 처리)
    public void updateUserScore(String userId, double score) {
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi(); // 트랜잭션 시작
                zSetOperations.add(RANKING_KEY, userId, score);
                operations.exec(); // 트랜잭션 종료 및 실행
                return null;
            }
        });
    }

    // 주어진 사용자의 점수를 조회하여 반환
    public Double getUserScore(String userId) {
        return zSetOperations.score(RANKING_KEY, userId);
    }

    // 주어진 사용자의 점수보다 높은 랭킹을 가진 사용자들의 수를 반환
    public Long getCountOfHigherRankUsers(double userScore) {
        Long higherRankUsersCount = zSetOperations.count(RANKING_KEY, userScore + 1, Double.MAX_VALUE);
        return higherRankUsersCount != null ? higherRankUsersCount : 0;
    }

    public void removeUserScore(String userId) {
        zSetOperations.remove(RANKING_KEY, userId);
    }

    public void clearRanking() {
        redisTemplate.delete(RANKING_KEY);
    }

}
