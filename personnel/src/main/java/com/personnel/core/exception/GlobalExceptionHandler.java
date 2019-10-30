package com.personnel.core.exception;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler  {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * 所有异常报错
     * @param request
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value=Exception.class)
    public ResponseResult allExceptionHandler(HttpServletRequest request, Exception exception) throws Exception
    {
        System.out.println(request);
        PageData pageData = new PageData(request);
        System.out.println(pageData);
        logger.error("====================================================开始打印异常===============================================================");
        exception.printStackTrace();
        logger.error(exception.getLocalizedMessage());
        logger.error(String.valueOf(exception.getCause()));
        logger.error(String.valueOf(exception.getSuppressed()));
        logger.error(exception.getMessage());
        logger.error(String.valueOf(exception.getClass().getAnnotatedSuperclass()));
        logger.error(String.valueOf(exception.getStackTrace()));
        logger.error("======================================================end=====================================================================");
         if(exception instanceof AccessDeniedException){
            return ResponseResult.error("无权限");
        }
        if(exception instanceof MethodArgumentNotValidException){
            BindingResult bindingResult = ((MethodArgumentNotValidException) exception).getBindingResult();
            StringBuilder errorMessage = new StringBuilder(bindingResult.getFieldErrors().size() * 16);
            errorMessage.append("Invalid Request:");
            for (int i = 0;i<bindingResult.getFieldErrors().size();++i) {
                if(i>0){
                    errorMessage.append(",");
                }
                FieldError fieldError = bindingResult.getFieldErrors().get(i);
                errorMessage.append(fieldError.getField());
                errorMessage.append(":");
                errorMessage.append(fieldError.getDefaultMessage());
            }
            return ResponseResult.error(errorMessage.toString(),100);
        }
        if(exception instanceof RuntimeException){
            return ResponseResult.error("未知错误，请稍后再试", 100);
        }
        return ResponseResult.error("服务器发生异常，请联系管理员",100);
    }





}
