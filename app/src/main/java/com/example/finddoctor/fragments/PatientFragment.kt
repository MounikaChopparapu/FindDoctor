package com.example.finddoctor.fragments

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.finddoctor.R
import com.example.finddoctor.composeUI.AppBar
import com.example.finddoctor.modalClass.Slot
import com.example.finddoctor.ui.theme.FindDoctorTheme


class PatientFragment : Fragment(R.layout.fragment_patient) {
    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.composeViewSelPatient).setContent {
            FindDoctorTheme {
                BookSlotScreen(::navigateToBookSlotFragment, ::onBackClicked)
            }

        }
    }

    private fun onBackClicked() {
        parentFragmentManager.popBackStack()
    }

    private fun navigateToBookSlotFragment(slot: Slot) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main, SpecalityFragment())
            .commit()
    }

}

@Composable
fun PatientScreen(onSlotSelected: () -> Unit, onBackClicked: () -> Unit) {
    // Fetch doctors based on the selected speciality

    var patientName by remember { mutableStateOf("") }
    var diseaseName by remember { mutableStateOf("") }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        AppBar(onBackClicked = onBackClicked, title = "Select Slot")
       Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Patient Details",
        )

        OutlinedTextField(
            value = patientName,
            onValueChange = { patientName = it },
            label = { Text("Enter PatientName") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = diseaseName,
            onValueChange = { diseaseName = it },
            label = { Text("Enter Disease") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PatientScreenScreen() {
    PatientScreen(onSlotSelected = {}, onBackClicked = {})
}