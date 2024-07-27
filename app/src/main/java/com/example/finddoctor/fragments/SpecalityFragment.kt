package com.example.finddoctor.fragments

import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.finddoctor.R
import com.example.finddoctor.modalClass.SelectionDetails
import com.example.finddoctor.ui.theme.FindDoctorTheme

class SpecalityFragment : Fragment(R.layout.fragment_specality) {
    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.composeViewSpecialty).setContent {
            FindDoctorTheme {
                SpecialitiesScreen(::navigateToHomeFragment)
            }

        }
    }

    private fun navigateToHomeFragment(speciality: String) {

        SelectionDetails.selectedSpeciality = speciality
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right, R.anim.slide_out_left,
                R.anim.slide_in_left, R.anim.slide_out_right
            )
            .replace(R.id.main, SelectDoctorFragment())
            .addToBackStack(null)
            .commit()
    }

}

@Composable
fun SpecialitiesScreen(onSpecialitySelected: (speciality: String) -> Unit) {
    val specialities = listOf(
        "Paediatric",
        "Kidney",
        "Diabetes",
        "Orthopaedic",
        "Cancer",
        "Neurology",
        "Radiology",
        "Dermatology",
        "Urology"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(specialities.size) { index ->
            SpecialityCard(specialities[index],onSpecialitySelected)
//            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun SpecialityCard(title: String,onSpecialitySelected: (speciality: String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onSpecialitySelected(title) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Arrow Forward"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSpecialitiesScreen() {
    SpecialitiesScreen(onSpecialitySelected = {})
}
