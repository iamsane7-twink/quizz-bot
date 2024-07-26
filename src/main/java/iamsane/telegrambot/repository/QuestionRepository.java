package iamsane.telegrambot.repository;

import iamsane.telegrambot.entity.Question;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT * FROM question LIMIT :count", nativeQuery = true)
    List<Question> findQuestions(int count);

    @Override
    @Query(value = "SELECT * FROM question WHERE deleted = false", nativeQuery = true)
    @NotNull
    List<Question> findAll();

    @Override
    @Query(value = "SELECT * FROM question WHERE id = :id AND deleted = false", nativeQuery = true)
    @NotNull
    Optional<Question> findById(@NotNull Long id);
}
