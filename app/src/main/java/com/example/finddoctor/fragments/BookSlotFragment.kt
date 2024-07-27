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
import com.example.finddoctor.modalClass.Slot
import com.example.finddoctor.ui.theme.FindDoctorTheme


class BookSlotFragment : Fragment(R.layout.fragment_book_slot) {
    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.composeViewSelSlot).setContent {
            FindDoctorTheme {
                BookSlotScreen(::navigateToBookSlotFragment,::onBackClicked)
            }

        }
    }

    private fun onBackClicked()
    {
        parentFragmentManager.popBackStack()
    }

    private fun navigateToBookSlotFragment(slot: Slot) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main, SpecalityFragment())
            .commit()
    }

}


@Composable
fun BookSlotScreen(onSlotSelected: (slot: Slot) -> Unit,onBackClicked:() -> Unit) {
    // Fetch doctors based on the selected speciality
    val slots = getSlots()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        AppBar(onBackClicked = onBackClicked, title = "Select Slot")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            items(slots.chunked(2)) { rowDoctors ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowDoctors.forEach { doctor ->
                        SlotCard(doctor, onSlotSelected, modifier = Modifier.weight(1f))
                    }
                }
            }
        }

    }
}

@Composable
fun SlotCard(
    slot: Slot,
    onSlotSelected: (slot: Slot) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { onSlotSelected.invoke(slot) },
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_access_time_36),
                contentDescription = "time",
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = slot.tokenTime, style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
    }
}

fun getSlots(): List<Slot> {
    return listOf(
        Slot(1, "7:00 AM - 7:15 AM"),
        Slot(2, "7:15 AM - 7:30 AM"),
        Slot(3, "7:30 AM - 7:45 AM"),
        Slot(4, "7:45 AM - 8:00 AM"),
        Slot(5, "8:00 AM - 8:15 AM"),
        Slot(6, "8:15 AM - 8:30 AM"),
        Slot(7, "8:30 AM - 8:45 AM"),
        Slot(8, "8:45 AM - 9:00 AM"),
    )

}
