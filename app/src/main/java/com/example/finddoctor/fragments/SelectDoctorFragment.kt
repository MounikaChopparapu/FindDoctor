package com.example.finddoctor.fragments


import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.finddoctor.R
import com.example.finddoctor.composeUI.AppBar
import com.example.finddoctor.modalClass.Doctor
import com.example.finddoctor.modalClass.SelectionDetails
import com.example.finddoctor.ui.theme.FindDoctorTheme


class SelectDoctorFragment : Fragment(R.layout.fragment_select_doctor) {
    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.composeViewSelDoctor).setContent {
            FindDoctorTheme {
                SelectDoctorScreen(::navigateToBookSlotFragment,::onBackClicked)
            }

        }
    }

    private fun onBackClicked()
    {
        parentFragmentManager.popBackStack()
    }

    private fun navigateToBookSlotFragment(doctor: Doctor) {
        SelectionDetails.selectedDoctor = doctor
        parentFragmentManager.beginTransaction()
            .replace(R.id.main, BookSlotFragment())
            .addToBackStack(null)
            .commit()
    }

}

@Composable
fun SelectDoctorScreen(onSpecialitySelected: (doctor: Doctor) -> Unit,onBackClicked:() -> Unit) {
    // Fetch doctors based on the selected speciality
    val doctors = getDoctorsBySpeciality(SelectionDetails.selectedSpeciality ?: "")

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        AppBar(onBackClicked = onBackClicked, title = "Select Doctor")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            items(doctors.chunked(2)) { rowDoctors ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowDoctors.forEach { doctor ->
                        DoctorCard(doctor, onSpecialitySelected, modifier = Modifier.weight(1f))
                    }
                }
            }
        }

    }
}

@Composable
fun DoctorCard(
    doctor: Doctor,
    onSpecialitySelected: (doctor: Doctor) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { onSpecialitySelected.invoke(doctor) },
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = doctor.image),
                contentDescription = doctor.name,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = doctor.name, style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = doctor.phoneNumber, style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

fun getDoctorsBySpeciality(speciality: String): List<Doctor> {
    return when (speciality) {
        "Paediatric" -> listOf(
            Doctor("Dr Fattma Abdel-Salam", "Paediatric", "01642 282753", R.drawable.ic_male),
            Doctor("Dr Ruth Barron", "Paediatric", "01642 854115", R.drawable.ic_male),
            Doctor("Dr Mark Burns", "Paediatric", "01642 850850 ext 57187", R.drawable.ic_male)
        )

        "Kidney" -> listOf(
            Doctor("Dr Mohammed Abdelmahamoud", "Kidney", "01642 835853", R.drawable.ic_female)
        )

        "Diabetes" -> listOf(
            Doctor("Dr Mona Abouzaid", "Diabetes and endocrinology", "01642 282739", R.drawable.ic_female),
            Doctor("Dr Simon Ashwell", "Diabetes and endocrinology", "01642 854307 ext 54307", R.drawable.ic_male)
        )

        "Orthopaedic" -> listOf(
            Doctor("Mr Akinwande Adedapo", "Orthopaedic surgery", "01642 850850 ext 55513", R.drawable.ic_male)
        )

        "Cancer" -> listOf(
            Doctor("Dr Madhavi Adusumalli", "Cancer", "01642 854750", R.drawable.ic_female),
            Doctor("Mr Andrew Bartram", "Cancer", "01642 852681", R.drawable.ic_male),
            Doctor("Dr Eleanor Aynsley", "Cancer", "01642 854911", R.drawable.ic_female)
        )

        "Neurology" -> listOf(
            Doctor("Dr Deborah Bathgate", "Neurology", "01642 854408", R.drawable.ic_female),
            Doctor("Dr Adrian Bergin", "Neurology", "01642 854723", R.drawable.ic_male)
        )

        "Radiology" -> listOf(
            Doctor("Dr Mohanad Kareem Aftan", "Radiology", "01642 854786", R.drawable.ic_male),
            Doctor("Matthew Burgess", "Radiology", "01642 850850 ext 54457", R.drawable.ic_male)
        )

        "Dermatology" -> listOf(
            Doctor("Dr Andrew J Carmichael", "Dermatology", "01642 854721", R.drawable.ic_male),
            Doctor("Dr Jaskiran Azad", "Dermatology", "01642 854709", R.drawable.ic_female)
        )

        "Urology" -> listOf(
            Doctor("Mr Chandrasekharan Badrakumar", "Urology", "01642 854712", R.drawable.ic_male)
        )

        else -> emptyList()
    }

}