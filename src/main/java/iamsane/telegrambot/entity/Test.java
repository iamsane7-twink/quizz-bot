package iamsane.telegrambot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Test {
    @Id
    @GeneratedValue
    private Long id;

    private String chatId;

    private int currentQuestion;

    private int correctAnswers;

    private boolean isFinished = false;

    @CreationTimestamp
    private Instant instant;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @OrderColumn(name = "ord")
    private List<Question> questions;

    @ManyToMany
    @OrderColumn(name = "ord")
    private List<Answer> answers;
}
