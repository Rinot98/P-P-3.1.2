package ru.kata.spring.boot_security.demo.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException ex, Model model) {
        model.addAttribute("message", "У вас нет прав доступа к этой странице.");
        return "403";
    }

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model, HttpServletRequest request) {
        model.addAttribute("timestamp", new Date());
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("status", 500);
        return "error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(Exception ex, Model model, HttpServletRequest request) {
        model.addAttribute("timestamp", new Date());
        model.addAttribute("error", "Page not found" + ex.getMessage());
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("status", 404);
        return "error";
    }
}