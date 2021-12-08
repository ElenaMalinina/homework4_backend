package malinina.dto;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFollow {
    @JsonProperty("status")
    public Boolean status;

}
