package com.example.xentlyspringdatatestcontainers.exceptions.advices

import com.example.xentlyspringdatatestcontainers.exceptions.XentlyHttpException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import java.net.URI
import java.time.Instant

@ControllerAdvice
object XentlyHttpExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(XentlyHttpException::class)
    operator fun invoke(exception: XentlyHttpException, request: HttpServletRequest): ResponseEntity<Problem> {
        val problem = Problem.create()
            .withStatus(exception.status)
            .withProperties(
                mapOf(
                    "timestamp" to Instant.now(),
                    "message" to exception.localizedMessage,
                ),
            )
            .withDetail(exception.localizedMessage)
            .withInstance(URI.create(request.requestURI))

        return ResponseEntity.status(exception.status)
            .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
            .body(problem)
    }
}