package app.evaluation.elements.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor

public class ClosedAnswerCounter {
    @Getter @Setter Integer answer1c;
    @Getter @Setter Integer answer2c;
    @Getter @Setter Integer answer3c;
    @Getter @Setter Integer answer4c;
    @Getter @Setter Integer answer5c;
    @Getter @Setter Integer answer6c;

    @Getter @Setter String answer1;
    @Getter @Setter String answer2;
    @Getter @Setter String answer3;
    @Getter @Setter String answer4;
    @Getter @Setter String answer5;
    @Getter @Setter String answer6;
}
