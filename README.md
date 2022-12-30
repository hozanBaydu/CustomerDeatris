# CustomerDeatris


Coroutine ile verileri alma örneği.


```sh
interface CryptoAPI {
    @GET("https://maps.googleapis.com/")
    suspend fun getData(): Response<List<CyriptoModel>>
}

```


```sh
 private fun getData() {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoAPI::class.java)

        job = CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            val response = retrofit.getData()

            withContext(Dispatchers.Main){
                if(response.isSuccessful) {
                    response.body()?.let {
                        cyriptoModel = ArrayList(it)
                        cyriptoModel?.let {
                            recyclerViewAdapter = RecyclerViewAdapter(it,this@MainActivity)
                            binding.recyclerView.adapter = recyclerViewAdapter
                        }
                    }
                }
            }
        }
    }
    
    ```
