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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finddoctor.modalClass.PatientData
import com.example.finddoctor.ui.theme.FindDoctorTheme
import com.google.firebase.auth.FirebaseAuth


class AuthenticationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FindDoctorTheme {
                AuthScreen(::gotoHomeActivity)
            }
        }
    }

    private fun gotoHomeActivity() {
        startActivity(Intent(this, BaseActivity::class.java))
    }
}

@Composable
fun AuthScreen(onLoginSucessfully: () -> Unit) {
    var isLogin by remember { mutableStateOf(true) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.find_doctor),
            contentDescription = "Find Doctor",
        )

        Text(
            text = context.getString(R.string.app_title),
            color = Color.Blue,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = if (isLogin) "Login" else "Register",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter Patient Mail") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    if (!isValidEmail(email)) {
                        Toast.makeText(context, "Please enter a valid mail", Toast.LENGTH_SHORT).show()
                    } else if (password.length < 6) {
                        Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                    } else {
                        if (isLogin) {
                            signInUser(email, password) { isSuccess ->
                                if (isSuccess) {
                                    PatientData.saveLoginStatus(context, true)
                                    PatientData.saveUserMail(context, email)
                                    onLoginSucessfully.invoke()
                                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            signUpUser(email, password) { isSuccess ->
                                if (isSuccess) {
                                    PatientData.saveLoginStatus(context, true)
                                    PatientData.saveUserMail(context, email)
                                    onLoginSucessfully.invoke()
                                    Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(top = 16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(if (isLogin) "Login" else "Register")
        }

        TextButton(
            onClick = { isLogin = !isLogin },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(if (isLogin) "Don't have an account? Register" else "Already have an account? Login")
        }
    }
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun showFields()
{

}

fun signUpUser(email: String, password: String, onComplete: (Boolean) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Firebase", "Sign up successful")
                onComplete(true)
            } else {
                Log.e("Firebase", "Sign up failed: ${task.exception?.message}")
                onComplete(false)
            }
        }
}

fun signInUser(email: String, password: String, onComplete: (Boolean) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Firebase", "Sign in successful")
                onComplete(true)
            } else {
                Log.e("Firebase", "Sign in failed: ${task.exception?.message}")
                onComplete(false)
            }
        }
}