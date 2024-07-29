package com.example.finddoctor.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.finddoctor.R
import com.example.finddoctor.composeUI.AppBar
import com.example.finddoctor.modalClass.SelectionDetails
import com.example.finddoctor.ui.theme.FindDoctorTheme
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PatientFragment : Fragment(R.layout.fragment_patient) {
    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.composeViewSelPatient).setContent {
            FindDoctorTheme {
                PatientScreen(::onSubmitClicked, ::onBackClicked)
            }

        }
    }

    private fun onBackClicked() {
        parentFragmentManager.popBackStack()
    }

    private fun onSubmitClicked(patientName: String, disease: String) {
        //Save Detail to DB
        saveTokenDetails(
            "user1@gmail.com",
            SelectionDetails.selectedSpeciality!!,
            SelectionDetails.selectedDoctor.name,
            SelectionDetails.selectedSlot.tokenTime,
            patientName,
            disease
        )

        popAllFragments()

    }

    private fun popAllFragments() {
        val backStackEntryCount = parentFragmentManager.backStackEntryCount
        for (i in 0 until backStackEntryCount) {
            parentFragmentManager.popBackStack()
        }

        Toast.makeText(requireContext(),"Booking Done",Toast.LENGTH_SHORT)
            .show()

        parentFragmentManager.beginTransaction()
            .replace(R.id.main, SpecalityFragment())
            .commit()
    }

}

@Composable
fun PatientScreen(
    onSubmitClicked: (patientName: String, disease: String) -> Unit,
    onBackClicked: () -> Unit
) {
    // Fetch doctors based on the selected speciality

    var patientName by remember { mutableStateOf("") }
    var diseaseName by remember { mutableStateOf("") }

    val context = LocalContext.current


    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        AppBar(onBackClicked = onBackClicked, title = "Patient Details")
        Spacer(modifier = Modifier.height(36.dp))


        OutlinedTextField(
            value = patientName,
            onValueChange = { patientName = it },
            label = { Text("Enter PatientName") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )


        OutlinedTextField(
            value = diseaseName,
            onValueChange = { diseaseName = it },
            label = { Text("Enter Disease") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Button(
            onClick = {
                if (patientName.isNotEmpty() && diseaseName.isNotEmpty()) {
                    onSubmitClicked.invoke(patientName, diseaseName)
                } else {
                    Toast.makeText(context, "Please Enter All Details", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Submit")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatientScreenPreview() {
    PatientScreen(onSubmitClicked = { _, _ -> }, onBackClicked = {})

}

fun saveTokenDetails(
    userEmail: String,
    speciality: String,
    doctor: String,
    slotTime: String,
    patientName: String,
    disease: String
) {
    // Initialize Firebase Database
    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference

    // Replace '.' with ',' in email to use as Firebase key
    val sanitizedEmail = userEmail.replace(".", ",")

    // Get the current date and time without any special characters
    val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    val currentDateTime = dateFormat.format(Date())

    // Generate a unique key using current date and time
    val uniqueKey = databaseReference.child("users").child(sanitizedEmail).child("tokenDetails").push().key
    val uniqueTokenKey = "$currentDateTime-$uniqueKey"

    // Create a map of token details
    val tokenDetails = mapOf(
        "Speciality" to speciality,
        "Doctor" to doctor,
        "Slot Time" to slotTime,
        "Patient Name" to patientName,
        "Disease" to disease
    )

    // Save token details under the user's email with the unique token key
    uniqueKey?.let {
        databaseReference.child("users").child(sanitizedEmail).child("tokenDetails").child(uniqueTokenKey)
            .setValue(tokenDetails)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Data saved successfully
                    println("Token details saved successfully with key $uniqueTokenKey.")
                } else {
                    // Error occurred
                    task.exception?.let { exception ->
                        println("Failed to save token details: ${exception.message}")
                    }
                }
            }
    } ?: run {
        println("Failed to generate a unique key for token details.")
    }
}
