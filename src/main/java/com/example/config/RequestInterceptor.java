package com.example.config;

  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  import org.springframework.stereotype.Component;
  import org.springframework.web.servlet.HandlerInterceptor;

  import jakarta.servlet.http.HttpServletRequest;
  import jakarta.servlet.http.HttpServletResponse;

  @Component
  public class RequestInterceptor implements HandlerInterceptor {

      private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
          logger.info("Intercepted request: Method={}, URI={}, Query={}", 
              request.getMethod(), 
              request.getRequestURI(), 
              request.getQueryString() != null ? request.getQueryString() : "None");
          return true;
      }
  }