package ru.skypro.homework.filter;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр для обработки CORS-запросов с поддержкой учётных данных (например, cookies).
 * Добавляет заголовок "Access-Control-Allow-Credentials" к каждому HTTP-ответу,
 * чтобы разрешить браузерам использовать учётные данные в кросс-доменных запросах.
 * Этот фильтр применяется один раз для каждого запроса.
 */
@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    /**
     * Обрабатывает каждый запрос, добавляя заголовок "Access-Control-Allow-Credentials" к ответу.
     *
     * @param httpServletRequest  HTTP-запрос.
     * @param httpServletResponse HTTP-ответ.
     * @param filterChain         Цепочка фильтров для передачи запроса и ответа дальше.
     * @throws ServletException Если возникает ошибка при обработке запроса.
     * @throws IOException      Если возникает ошибка ввода-вывода при обработке запроса.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // Добавляем заголовок для поддержки учётных данных в CORS-запросах
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");

        // Передаём запрос и ответ дальше по цепочке фильтров
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
