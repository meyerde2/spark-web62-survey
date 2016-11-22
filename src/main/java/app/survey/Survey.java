package app.survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class Survey {

    @Getter @Setter int surveyId;
    @Getter @Setter String surveyTitle;
    @Getter @Setter int userId;
    @Getter @Setter boolean ipAddress;
    @Getter @Setter boolean sessionId;
    @Getter @Setter boolean isPublished;

}
