package com.vp.cardmusic.core.network

import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (cancelled: CancellationException) {
    throw cancelled
} catch (http: HttpException) {
    val message = when (http.code()) {
        401 -> "HTTP 401 — API key tidak valid atau tidak memiliki akses."
        403 -> "HTTP 403 — akses ke endpoint ini ditolak oleh API."
        404 -> "HTTP 404 — data atau endpoint tidak ditemukan."
        429 -> "HTTP 429 — batas request API tercapai. Coba lagi nanti."
        in 500..599 -> "Server API sedang bermasalah (${http.code()}). Coba lagi nanti."
        else -> "Request gagal dengan HTTP ${http.code()}."
    }
    Result.failure(IllegalStateException(message, http))
} catch (io: IOException) {
    Result.failure(IllegalStateException("Tidak dapat terhubung ke server. Periksa koneksi internet.", io))
} catch (error: Exception) {
    Result.failure(IllegalStateException(error.message ?: "Terjadi kesalahan yang tidak diketahui.", error))
}

fun Throwable.toDisplayMessage(): String =
    message?.takeIf { it.isNotBlank() } ?: "Terjadi kesalahan. Silakan coba lagi."
