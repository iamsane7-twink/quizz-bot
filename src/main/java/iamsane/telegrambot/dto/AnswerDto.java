package iamsane.telegrambot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerDto {
    private Long id;

    private String answerText;

    private String answerReply;

    private Boolean isCorrect = false;
}
