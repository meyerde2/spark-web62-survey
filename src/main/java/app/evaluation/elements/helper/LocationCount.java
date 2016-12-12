package app.evaluation.elements.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@AllArgsConstructor
public class LocationCount {

    @Getter @Setter String location;
    @Getter @Setter int count;
}
