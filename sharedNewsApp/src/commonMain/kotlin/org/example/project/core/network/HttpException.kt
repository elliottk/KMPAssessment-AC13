package org.example.project.core.network

class HttpException(val statusCode: Int, message: String, cause: Throwable? = null) : Throwable(message, cause)
