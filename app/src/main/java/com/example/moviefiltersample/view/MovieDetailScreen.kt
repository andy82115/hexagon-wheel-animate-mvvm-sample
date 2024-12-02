package com.example.moviefiltersample.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.moviefiltersample.R
import com.example.moviefiltersample.viewmodels.ImageLinkPrefix
import com.example.moviefiltersample.viewmodels.MovieDetailViewModel
import kotlin.random.Random

@Composable
fun MovieDetailScreen(
    viewModel: MovieDetailViewModel = hiltViewModel(),
    movieId : Int
) {
    val data by viewModel.searchMovieList

    LaunchedEffect(Unit) {
        viewModel.updateMovieDetail(movieId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MovieDetailTopPart(
                imgUrl = data?.posterPath ?: "",
                title = data?.title ?: "loading",
                runtime = data?.runtime ?: 0,
                revenue = data?.revenue ?: 0,
                popularity = data?.popularity ?: 0.0,
                voteAverage = data?.voteAverage ?: 0.0
            )
            BigHorizontalDivider()
            if (data != null){
                MovieDetailBottomPart(
                    genreList = data!!.genres.map { it.name },
                    describe = data!!.overview
                )
            }
        }
    }
}

@Composable
private fun MovieDetailTopPart(
    imgUrl: String,
    title: String,
    runtime: Int,
    revenue: Int,
    popularity: Double,
    voteAverage: Double){
    Row(
        Modifier.background(Color.Black)
    ) {
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
                    color = Color.White
                )
            }
            HorizontalDivider(
                color = Color.LightGray,
                thickness = 0.5.dp,
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                modifier = Modifier.padding(all = 5.dp),
                text = "Playtime: $runtime mins",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                textAlign = TextAlign.Left,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                modifier = Modifier.padding(all = 5.dp),
                text = "Popularity: $popularity",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                textAlign = TextAlign.Left,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                modifier = Modifier.padding(all = 5.dp),
                text = "Revenue: $revenue",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                textAlign = TextAlign.Left,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                modifier = Modifier.padding(all = 5.dp),
                text = "Vote: $voteAverage/10",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                textAlign = TextAlign.Left,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(1.dp))
        }
    }
}

@Composable
private fun MovieDetailBottomPart(
    genreList: List<String>,
    describe: String,
){
    val describeText = buildAnnotatedString {
        withStyle(style = SpanStyle(letterSpacing = 1.sp)) {
            val sentences = describe.split(".").map { "$it\n\n" }.joinToString("")
            append("Overview: \n\n $sentences")}
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.Black)) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                text = "Genres",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = Color.White
            )
        }
        SmallHorizontalDivider()
        Spacer(modifier = Modifier.height(5.dp))
        LazyRow {
            items (
                count = genreList.size,
                itemContent = { index ->
                    Row{
                        Spacer(modifier = Modifier.width(5.dp))
                        GenreTag(text = genreList[index])
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        BigHorizontalDivider()
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                text = "Describe",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = Color.White
            )
        }
        SmallHorizontalDivider()
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 20.dp, end = 20.dp),
            text = describeText,
            softWrap = true,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Left,
            color = Color.White
        )
    }
}

@Composable
private fun SmallHorizontalDivider(){
    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp,)
}

@Composable
private fun BigHorizontalDivider(){
    HorizontalDivider(color = Color.LightGray, thickness = 1.dp,)
}

@Composable
private fun GenreTag(text: String){
    Box(
        modifier = Modifier
            .background(randomDeepColor(), shape = CircleShape)
            .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)
    ) {
        Text(
            text = text,
            color =Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

private fun randomDeepColor(): Color {
    val random = Random
    val red = random.nextInt(100) // Range 0-99
    val green = random.nextInt(100) // Range0-99
    val blue = random.nextInt(150)
    return Color(red, green, blue)
}

//@Preview
//@Composable
//fun MovieDetailScreenPreview() {
//    MovieDetailScreen(5335223)
//}