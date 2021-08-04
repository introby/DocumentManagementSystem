package by.intro.dms.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomExceptionResponse {

    private int status;
    private String error;
    private String path;
    private String message;
}
