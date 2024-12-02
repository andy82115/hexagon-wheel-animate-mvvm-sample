package com.example.moviefiltersample.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.moviefiltersample.R
import com.example.moviefiltersample.viewmodels.ImageLinkPrefix

@Composable
fun ResultCardItem(
    movieId: Int,
    imgUrl: String,
    title: String,
    overview: String,
    onResultItemClick: (Int) -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(20.dp)
            .clickable {
                onResultItemClick(movieId)
            },
        colors = CardColors(
            contentColor = Color.Gray,
            disabledContentColor = Color.Gray,
            disabledContainerColor = Color(0xFFE7E2E1),
            containerColor = Color(0xFFE7E2E1),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp
        )
    ) {
        Row {
            AsyncImage(
                model = "$ImageLinkPrefix${imgUrl}",
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .aspectRatio(2f / 3f)
                    .align(Alignment.CenterVertically),
                contentDescription = "Result Item Image",
                placeholder = painterResource(id = R.drawable.ic_result_placeholder),
                error = painterResource(id = R.drawable.ic_result_placeholder)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        text = title,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        color = Color.Black
                    )
                }
                HorizontalDivider(
                    color = Color.LightGray,
                    thickness = 0.5.dp,
                )
                Text(
                    modifier = Modifier.padding(all = 5.dp),
                    text = overview,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 4,
                    textAlign = TextAlign.Left
                )
            }
        }
    }
}