package com.example.finddoctor

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.finddoctor.modalClass.Token
import com.example.finddoctor.ui.theme.FindDoctorTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookedSlotsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FindDoctorTheme {
                TokenListScreen(userEmail = "user1@gmail.com")
            }
        }
    }
}


@Composable
fun TokenListScreen(userEmail: String) {
    var tokenList by remember { mutableStateOf(listOf<Token>()) }

    // Fetch token details
    LaunchedEffect(userEmail) {
        fetchTokenDetails(userEmail) { tokens ->
            tokenList = tokens
        }
    }

    // Display the tokens in a LazyColumn
    LazyColumn {
        items(tokenList.size) { token ->
            TokenItem(tokenList[token])
        }
    }
}

@Composable
fun TokenItem(token: Token) {
    Log.e("Test",token.speciality)
    Column {
        Text(text = "Speciality: ${token.speciality}")
        Text(text = "Doctor: ${token.doctor}")
        Text(text = "Slot Time: ${token.slotTime}")
        Text(text = "Patient Name: ${token.patientName}")
        Text(text = "Disease: ${token.disease}")
    }
}

@Preview(showBackground = true)
@Composable
fun TokenListScreenPreview() {
//    TokenListScreen(userEmail = "example@gmail.com")
}


fun fetchTokenDetails(userEmail: String, onTokensFetched: (List<Token>) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference

    // Replace '.' with ',' in email to use as Firebase key
    val sanitizedEmail = userEmail.replace(".", ",")

    val tokenList = mutableListOf<Token>()

    databaseReference.child("users").child(sanitizedEmail).child("tokenDetails")
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (tokenSnapshot in snapshot.children) {
                    val token = tokenSnapshot.getValue(Token::class.java)
                    token?.let { tokenList.add(it) }
                }
                onTokensFetched(tokenList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                println("Failed to fetch token details: ${error.message}")
                onTokensFetched(emptyList())
            }
        })
}
