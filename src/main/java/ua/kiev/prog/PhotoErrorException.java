package ua.kiev.prog;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//если появится исключение spring кинет пользователю статус BAD_REQUEST
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PhotoErrorException extends RuntimeException {}