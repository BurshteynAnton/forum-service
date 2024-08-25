package java53.forumservice.accounting.dto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserExistsException extends RuntimeException{
    private static long serialVersionUID = 4646804769778881046L;
}
