package com.example.finddoctor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.finddoctor.composeUI.AppBar
import com.example.finddoctor.modalClass.PatientData
import com.example.finddoctor.modalClass.SelectionDetails
import com.example.finddoctor.modalClass.Token
import com.example.finddoctor.ui.theme.FindDoctorTheme

class BookedSlotsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FindDoctorTheme {
                TokenListScreen(::onBackClicked,::onLogoutClicked)
            }
        }
    }

    private fun onLogoutClicked() {
        Toast.makeText(this,"Logout Successfull",Toast.LENGTH_SHORT).show()
        PatientData.saveLoginStatus(this,false)

        val intent = Intent(this, AuthenticationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun onBackClicked() {
        finish()
    }
}

@Composable
fun TokenListScreen(onBackClicked: () -> Unit,onLogoutClicked: () -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Spacer(modifier = Modifier.height(36.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Blue)
                .padding(vertical = 6.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "Back Arrow",
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp)
                    .clickable {
                        onBackClicked.invoke()
                    }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "History",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.baseline_logout_36),
                contentDescription = "Logout",
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp)
                    .clickable {
                        onLogoutClicked.invoke()
                    }
            )
        }


        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(SelectionDetails.bookedTokens.size) { token ->
                TokenItem(token = SelectionDetails.bookedTokens[token])
            }
        }

    }

}

@Composable
fun TokenItem(token: Token) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Speciality: ${token.Speciality}")
            Text(text = "Doctor: ${token.Doctor}")
            Text(text = "Slot Time: ${token.SlotTime}")
            Text(text = "Patient Name: ${token.PatientName}")
            Text(text = "Disease: ${token.Disease}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TokenListScreenPreview() {

}

