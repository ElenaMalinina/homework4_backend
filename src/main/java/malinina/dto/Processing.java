package malinina.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Processing {
    @JsonProperty("status")
    public String status;
}
