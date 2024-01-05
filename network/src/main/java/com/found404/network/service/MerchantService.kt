package com.found404.network.service

import android.content.Context
import com.Found404.paypro.AuthDependencyProvider
import com.Found404.paypro.AuthServiceImpl
import com.Found404.paypro.responses.RegistrationResponse
import com.found404.core.models.Merchant
import com.found404.core.models.MerchantResponse
import com.found404.core.models.Terminal
import com.found404.network.result.AddingMerchantsResult
import com.found404.network.service.implementation.AddingMerchantsResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import responses.ApiResponse

class MerchantService {
    private val gson = Gson()
    private val client = OkHttpClient()
    private val dependencyProvider = AuthDependencyProvider.getInstance()
    private val authService = dependencyProvider.getAuthService()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    fun getMerchantsForUser(context: Context, callback: (List<MerchantResponse>?, String?) -> Unit) {
        coroutineScope.launch {
            try {
                val user = authService.getLoggedInUser(context)
                val userID = user.userId
                val url = "http://158.220.113.254:8086/api/merchant/${userID}"
                val jwtToken = authService.getAuthToken(context)

                val request = Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer $jwtToken")
                    .get()
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                val apiResponseType = object : TypeToken<ApiResponse<List<MerchantResponse>>>() {}.type
                val apiResponse = gson.fromJson<ApiResponse<List<MerchantResponse>>>(responseBody, apiResponseType)

                withContext(Dispatchers.Main) {
                    if (apiResponse.success) {
                        callback(apiResponse.data, null)
                    } else {
                        callback(null, apiResponse.errorMessage)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback(null, e.message)
                }
            }
        }
    }


//    suspend fun getTerminalsForAllMerchants(context: Context): List<Terminal>? = withContext(Dispatchers.IO) {
//        val merchants = getMerchantsForUser(context) ?: return@withContext emptyList()
//        merchants.mapNotNull { merchant ->
//            getTerminalsForMerchant(merchant.id.toString(), context)
//        }.flatten()
//    }
//
//    private suspend fun getTerminalsForMerchant(merchantId: String, context: Context): List<Terminal>? {
//        val url = "http://158.220.113.254:8086/api/merchant/${merchantId}/terminal"
//        val jwtToken = authService.getAuthToken(context)
//
//        val request = Request.Builder()
//            .url(url)
//            .header("Authorization", "Bearer $jwtToken")
//            .get()
//            .build()
//
//        return try {
//            val response = client.newCall(request).execute()
//            if (!response.isSuccessful) return null
//            val responseBody = response.body?.string()
//            val type = object : TypeToken<List<Terminal>>() {}.type
//            gson.fromJson(responseBody, type)
//        } catch (e: Exception) {
//            null
//        }
//    }

    suspend fun deleteTerminal(merchantId: Int, terminalId: String, context: Context): ApiResponse<Unit>? = withContext(Dispatchers.IO) {
        val url = "http://158.220.113.254:8086/api/merchant/${merchantId}/terminal/${terminalId}"
        val jwtToken = authService.getAuthToken(context)

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $jwtToken")
            .delete()
            .build()

        return@withContext try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            val apiResponseType = object : TypeToken<ApiResponse<Unit>>() {}.type
            gson.fromJson<ApiResponse<Unit>>(responseBody, apiResponseType).also {
                if (!response.isSuccessful) {
                    println("Error: ${it.errorMessage}")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun deleteMerchant(merchantId: Int, context: Context): RegistrationResponse? = withContext(Dispatchers.IO) {
        val url = "http://158.220.113.254:8086/api/merchant/$merchantId"
        val jwtToken = authService.getAuthToken(context)

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $jwtToken")
            .delete()
            .build()

        return@withContext try {
            val response = client.newCall(request).execute()

            val responseBody = response.body?.string()
            gson.fromJson(responseBody, RegistrationResponse::class.java).also {
                if (!response.isSuccessful) {
                    println("Error: ${it.errorMessage}")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun editMerchant(
        context: Context,
        merchantId: Int,
        merchantName: String,
        merchantStreetName: String,
        merchantCityName: String,
        merchantPostCode: Int,
        merchantStreetNumber: Int,
    ): AddingMerchantsResult = withContext(Dispatchers.IO) {

        val currentUser = authService.getLoggedInUser(context)

        val requestBody = gson.toJson(
            mapOf(
                "merchantId" to merchantId,
                "merchantName" to merchantName,
                "address" to mapOf(
                    "city" to merchantCityName,
                    "streetName" to merchantStreetName,
                    "streetNumber" to merchantStreetNumber.toString(),
                    "postalCode" to merchantPostCode.toString()
                )
            )
        ).toRequestBody("application/json".toMediaType())
        println("requestbody " + gson.toJson(
            mapOf(
                "merchantId" to merchantId,
                "merchantName" to merchantName,
                "address" to mapOf(
                    "city" to merchantCityName,
                    "streetName" to merchantStreetName,
                    "streetNumber" to merchantStreetNumber.toString(),
                    "postalCode" to merchantPostCode.toString()
                )
            )
        ).toString())

        val jwtToken = authService.getAuthToken(context)
        val request = Request.Builder()
            .url("http://158.220.113.254:8086/api/merchant/${currentUser.userId}")//TODO CHANGE TO EDIT API
            .header("Authorization", "Bearer $jwtToken")
            .post(requestBody)
            .build()

        return@withContext try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            val result = gson.fromJson(responseBody, AddingMerchantsResponse::class.java)
            println("success " + result.success)
            println("message " + result.message)
            println("errorCode " + result.errorCode)
            println("errorMessage " + result.errorMessage)
            AddingMerchantsResult(result.success, result.message, result.errorCode, result.errorMessage)
        } catch (e: Exception) {
            AddingMerchantsResult(false, "Editing merchant failed", error = e.message)
        }
    }

}

