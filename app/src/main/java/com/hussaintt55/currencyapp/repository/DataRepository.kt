import com.hussaintt55.currencyapp.api.MyRetrofitBuider
import com.hussaintt55.currencyapp.model.CurrencyList.CurrencyListResponse
import com.hussaintt55.currencyapp.model.Historical.HistoricalResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

object DataRepository {

    suspend fun fetchData(): Result<CurrencyListResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = MyRetrofitBuider.apiService.getCurrencyList(API_KEY)

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

    suspend fun fetchDataHistorical(date:String): Result<HistoricalResponse> {
        return withContext(Dispatchers.IO) {
            try {
                //required format "2013-03-16"
                val response = MyRetrofitBuider.apiService.getHistoricalCurrency(date, API_KEY)
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
     val popularCountries = arrayListOf("EGP","USD","SAR","CAD","GBP","INR","RUB","QAR","AED","AUD","KYD")
     private const val API_KEY = "182a31e1cf69b50bba2621ee49965329"
}
