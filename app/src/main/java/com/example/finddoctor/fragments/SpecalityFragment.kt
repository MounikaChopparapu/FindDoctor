package com.example.finddoctor.fragments

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.finddoctor.BookedSlotsActivity
import com.example.finddoctor.R
import com.example.finddoctor.modalClass.PatientData
import com.example.finddoctor.modalClass.SelectionDetails
import com.example.finddoctor.modalClass.Token
import com.example.finddoctor.ui.theme.FindDoctorTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SpecalityFragment : Fragment(R.layout.fragment_specality) {
    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.composeViewSpecialty).setContent {
            FindDoctorTheme {
                SpecialitiesScreen(::navigateToHomeFragment, ::gotoBookedSlots)
            }

        }
    }

    override fun onResume() {
        super.onResume()

        fetchTokenDetails(PatientData.getUserMail(requireContext())!!)
    }

    private fun gotoBookedSlots() {
        startActivity(android.content.Intent(requireContext(), BookedSlotsActivity::class.java))
    }

    private fun navigateToHomeFragment(speciality: String) {

        SelectionDetails.selectedSpeciality = speciality
        parentFragmentManager.beginTransaction()
            .replace(R.id.main, SelectDoctorFragment())
            .addToBackStack(null)
            .commit()
    }


}

@Composable
fun SpecialitiesScreen(
    onSpecialitySelected: (speciality: String) -> Unit,
    onShowBookedSlotsClicked: () -> Unit
) {
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
            Text(
                text = "Find Your Doctor",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.ic_history),
                contentDescription = "History",
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp)
                    .clickable {
                        onShowBookedSlotsClicked.invoke()
                    }
            )

        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(specialities.size) { index ->
                SpecialityCard(specialities[index], onSpecialitySelected)
            }
        }

    }

}

@Composable
fun SpecialityCard(title: String, onSpecialitySelected: (speciality: String) -> Unit) {
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
    SpecialitiesScreen(onSpecialitySelected = {}, onShowBookedSlotsClicked = {})
}



fun fetchTokenDetails(userEmail: String) {
    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference

    // Replace '.' with ',' in email to use as Firebase key
    val sanitizedEmail = userEmail.replace(".", ",")

    databaseReference.child("users").child(sanitizedEmail).child("tokenDetails")
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tokenList = mutableListOf<Token>()
                for (tokenSnapshot in snapshot.children) {
                    val token = tokenSnapshot.getValue(Token::class.java)
                    token?.let { tokenList.add(it) }
                }
                SelectionDetails.bookedTokens=tokenList as ArrayList<Token>
                for (token in tokenList)
                {
                    Log.e("Test",token.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e("Firebase", "Failed to fetch token details: ${error.message}")
            }
        })
}
