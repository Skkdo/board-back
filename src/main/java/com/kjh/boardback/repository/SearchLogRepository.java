package com.kjh.boardback.repository;

import com.kjh.boardback.entity.SearchLog;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchLogRepository extends JpaRepository<SearchLog, Integer> {

    @Query("SELECT l FROM SearchLog l " +
            "WHERE l.relation = false " +
            "GROUP BY l.searchWord " +
            "ORDER BY COUNT(l.searchWord) DESC ")
    List<SearchLog> getPopularList();

    @Query("SELECT DISTINCT l.relationWord FROM SearchLog l " +
            "WHERE (l.searchWord = :searchWord AND l.relationWord IS NOT NULL) " +
            "GROUP BY l.relationWord " +
            "ORDER BY COUNT(l.relationWord) DESC")
    List<String> getRelationList(@Param("searchWord") String SearchWord);
}
