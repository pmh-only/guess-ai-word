package codes.pmh.school.spring.guessaiword.common.repository;

import codes.pmh.school.spring.guessaiword.common.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    @Query("select g from Game g where g.id < :lastId and g.isFinished = true order by g.id desc limit :count")
    List<Game> findByLastIdAndCount (@Param("lastId") int lastId,
                                     @Param("count") int count);

    @Query("select g from Game g where g.isFinished = true order by g.id desc limit :count")
    List<Game> findByCount (@Param("count") int count);
}
