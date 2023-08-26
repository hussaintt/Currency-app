import com.hussaintt55.currencyapp.api.MyRetrofitBuider
import com.hussaintt55.currencyapp.model.CurrencyList.CurrencyListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

object DataRepository {

    suspend fun fetchData(): Result<CurrencyListResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = MyRetrofitBuider.apiService.getCurrencyList("182a31e1cf69b50bba2621ee49965329")

                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        Result.success(data)
                    } else {
                        Result.failure(Throwable("Body is null"))
                    }
                } else {
                    Result.failure(Throwable("API Error: ${response.code()} - ${response.message()}"))
                }
            } catch (e: IOException) {
                Result.failure(Throwable("Network Error: ${e.message}"))
            } catch (e: HttpException) {
                Result.failure(Throwable("API Error: ${e.code()} - ${e.message()}"))
            }
        }
    }
}
