package exceptionhandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import utlis.Result;

@ControllerAdvice
public class GlobalExceptionHandler {

    //异常处理器
    @ExceptionHandler(Exception.class)
    public Result  error(Exception e){
        e.printStackTrace();
        return Result.error().message(e.getMessage());
    }
}
