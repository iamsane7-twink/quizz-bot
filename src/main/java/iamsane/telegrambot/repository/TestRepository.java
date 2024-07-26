package iamsane.telegrambot.repository;

import iamsane.telegrambot.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    Optional<Test> findByChatIdAndIsFinished(String chatId, boolean isFinished);

    boolean existsByChatIdAndIsFinished(String chatId, boolean isFinished);
}
