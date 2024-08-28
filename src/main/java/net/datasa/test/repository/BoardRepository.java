package net.datasa.test.repository;

import net.datasa.test.domain.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 게시판 관련 repository
 */

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {

	@Query("""
			select b from BoardEntity b
        where b.category = :category
        and b.title like %:searchWord%
			""")
	List<BoardEntity> findByCategoryAndTitleContaining(@Param("category") String c, @Param("searchWord") String s);
	
	List<BoardEntity> findByTitleContaining(String searchWord);
}
