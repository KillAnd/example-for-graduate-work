package ru.skypro.homework.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр для добавления заголовка "Access-Control-Allow-Credentials" к каждому HTTP-ответу.
 * Этот фильтр позволяет браузеру использовать учетные данные (например, cookies) в CORS-запросах.
 */
@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    /**
     * Метод, который выполняется для каждого HTTP-запроса.
     * Добавляет заголовок "Access-Control-Allow-Credentials" со значением "true" к HTTP-ответу.
     *
     * @param httpServletRequest  HTTP-запрос
     * @param httpServletResponse HTTP-ответ
     * @param filterChain         цепочка фильтров
     * @throws ServletException если возникает ошибка при обработке запроса
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}