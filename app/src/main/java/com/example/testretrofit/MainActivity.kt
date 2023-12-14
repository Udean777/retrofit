package com.example.testretrofit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testretrofit.models.CatFact
import com.example.testretrofit.ui.theme.TestRetrofitTheme
import com.example.testretrofit.util.RetrofitInstance
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainActivity : ComponentActivity() {
    private var fact = mutableStateOf(CatFact())
    private var isLoading= mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestRetrofitTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val context = LocalContext.current
//                    var fact by remember {
//                        mutableStateOf(CatFact())
//                    }
//
//                    val scope = rememberCoroutineScope()
//                    LaunchedEffect(key1 = true){
//                        scope.launch(Dispatchers.IO) {
//                            val res = try {
//                                RetrofitInstance.api.getRandomFacts()
//                            }catch (err: HttpException){
//                                Toast.makeText(context,"HTTP Error ${err.message}", Toast.LENGTH_SHORT).show()
//                                return@launch
//                            }catch (err: IOException){
//                                Toast.makeText(context,"APP Error ${err.message}", Toast.LENGTH_SHORT).show()
//                                return@launch
//                            }
//                            if (res.isSuccessful && res.body() != null){
//                                withContext(Dispatchers.Main){
//                                    fact = res.body()!!
//                                }
//                            }
//                        }
//                    }
                    UiComponent(fact = fact, isLoading = isLoading)
                }
            }
        }
    }

@Composable
fun UiComponent(fact: MutableState<CatFact>, modifier: Modifier = Modifier, isLoading: MutableState<Boolean>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Cat Facts",
            fontSize = 26.sp,
            modifier = Modifier.padding(bottom = 25.dp)
        )
        Text(
            text = fact.value.fact,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 40.sp
        )
        Button(
            onClick = {
                if (!isLoading.value) {
                    sendReq()
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = if (isLoading.value) "Loading..." else "Randomize")
        }
    }
}

    @OptIn(DelicateCoroutinesApi::class)
    private fun sendReq() {
        isLoading.value = true
        GlobalScope.launch(Dispatchers.IO) {
            val res = try {
                RetrofitInstance.api.getRandomFacts()
            }catch (err: HttpException){
                Toast.makeText(applicationContext,"HTTP Error ${err.message}", Toast.LENGTH_SHORT).show()
                isLoading.value = false
                return@launch
            }catch (err: IOException){
                Toast.makeText(applicationContext,"APP Error ${err.message}", Toast.LENGTH_SHORT).show()
                isLoading.value = false
                return@launch
            }
            if (res.isSuccessful && res.body() != null){
                withContext(Dispatchers.Main){
                    fact.value = res.body()!!
                    isLoading.value= false
                }
            }
        }
    }
}
