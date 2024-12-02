package com.example.moviefiltersample.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moviefiltersample.R
import com.example.moviefiltersample.api.data.Country
import com.example.moviefiltersample.api.data.Genres
import com.example.moviefiltersample.api.data.KeyWordResult
import com.example.moviefiltersample.api.data.SearchKeyword
import com.example.moviefiltersample.api.data.SearchMovieParam
import com.example.moviefiltersample.component.hexagonbtn.HexagonWheel
import com.example.moviefiltersample.component.hexagonbtn.WheelType
import com.example.moviefiltersample.component.numberpicker.InfiniteCircularList
import com.example.moviefiltersample.viewmodels.MainViewModel
import com.example.moviefiltersample.viewmodels.MovieFilterViewModel
import com.example.moviefiltersample.viewmodels.SettingAnswerType
import com.example.moviefiltersample.viewmodels.SortType
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun MovieFilterScreen(
    modifier: Modifier = Modifier,
    onNavigateToSearchResult: () -> Unit = {},
    mainViewModel: MainViewModel = hiltViewModel(),
    viewModel: MovieFilterViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black
    ) { innerPadding ->
        //Since The remember(Savable) is not work with unParcel data
        //And using Parcel data will make the api request not effective
        //So, I choose viewmodel way to do this.
        //Beyond this, I prefer to control data at one place only

        //List data
        val yearList by viewModel.yearList
        val countryList by viewModel.countryList
        val genres by viewModel.genres
        val sortTypes by viewModel.sortTypes
        val keywordResult by viewModel.keywordResult

        //Selected data
        val keywordSearchText by viewModel.keywordText
        val keywordSelected by viewModel.keywordSelected
        val yearText by viewModel.yearText
        val country by viewModel.country
        val genre by viewModel.genre
        val sortType by viewModel.sortType
        val adultType by viewModel.adultType

        //Selected Model
        val wheelType = rememberSaveable { mutableStateOf(WheelType.Keyword) }

        //Scope
        val viewModelScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            launch {
                mainViewModel.getAllCountryList().collect{ value ->
                    viewModel.setCountryList(value)
                    if (countryList.isNotEmpty()) {
                        viewModel.setCountry(countryList.first())
                    }
                }
            }
            launch {
                mainViewModel.getAllGenresList().collect{ value ->
                    viewModel.setGenreList(value)
                    if (genres != null) {
                        viewModel.setGenre(genres!!.genres.first())
                    }
                }
            }
            launch {
                viewModel.getSearchKeyword("hero").collect{ value ->
                    if (value != null){
                        viewModel.setSearchKeyword(value)
                        viewModel.setSearchKeywordSelected(keywordResult?.results?.first())
                    }
                }
            }
        }

        TopImage(innerPadding = innerPadding)

        Box(modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter){
            ConstraintLayout {
                val (HexagonWheel, DataSelect) = createRefs()

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .constrainAs(HexagonWheel) {
                        bottom.linkTo(parent.bottom, margin = 80.dp)
                    },
                    contentAlignment = Alignment.BottomCenter){
                    HexagonWheel(
                        modifier = Modifier
                            .height(200f.dp)
                            .width(200f.dp),
                        onClick = { index ->
                            wheelType.value = WheelType.None
                        },
                        onAnimationEnd = { type ->
                            wheelType.value = type
                        },
                        onCenterClick = { _ ->
                            mainViewModel.setShareSearchMovieData(SearchMovieParam(
                                keyWordId = keywordSelected!!.id,
                                genresId = genre!!.id,
                                sortBy = "${sortType}.desc",
                                includeAdult = adultType.ordinal == 1,
                                year = yearText.toInt(),
                                region = country!!.iso31661
                            ))
                            onNavigateToSearchResult()
                        }
                    )
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .constrainAs(DataSelect) {
                        bottom.linkTo(HexagonWheel.top, margin = 20.dp)
                    }
                ){
                    when(wheelType.value){
                        WheelType.None -> {
                            Box{}
                        }
                        WheelType.Keyword -> {
                            Column(
                                Modifier.wrapContentSize()
                            ){
                                SingleLineTextField(
                                    text = keywordSearchText,
                                    onValueChange = { updatedText ->
                                        viewModel.setSearchKeywordText(updatedText)
                                    },
                                    onSearchClick = {
                                        viewModelScope.launch {
                                            viewModel.getSearchKeyword(keywordSearchText).collect{ value ->
                                                if (value != null){
                                                    viewModel.setSearchKeyword(value)
                                                    viewModel.setSearchKeywordSelected(keywordResult?.results?.first())
                                                }
                                            }
                                        }
                                    }
                                )
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)){
                                    if (keywordResult == null) {
                                        Box{}
                                    }
                                    else {
                                        SingleCircularList(
                                            currentItem = keywordSelected!!.name,
                                            items = keywordResult!!.results.map { it.name },
                                            onItemSelected = { _, item ->
                                                viewModel.setSearchKeywordSelected(
                                                    keywordResult!!.results.find { it.name == item }
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        WheelType.Year -> {
                            SingleCircularList(
                                currentItem = yearText,
                                items = yearList,
                                onItemSelected = { _, item ->
                                    viewModel.setYearText(item)
                                }
                            )
                        }
                        WheelType.Country -> {
                            if (countryList.isEmpty()) {
                                Box{}
                            }
                            else {
                                SingleCircularList(
                                    currentItem = country!!.nativeName,
                                    items = countryList.map { it.nativeName },
                                    onItemSelected = { _, item ->
                                        viewModel.setCountry( countryList.find { it.nativeName == item } )
                                    }
                                )
                            }
                        }
                        WheelType.Sort -> {
                            SingleCircularList(
                                currentItem = sortType.toString(),
                                items = sortTypes.map { it.toString() },
                                onItemSelected = { _, item ->
                                    viewModel.setSortType(SortType.valueOf(item))
                                }
                            )
                        }
                        WheelType.Genre -> {
                            if (genres == null) {
                                Box{}
                            }
                            else {
                                SingleCircularList(
                                    currentItem = genre!!.name,
                                    items = genres!!.genres.map { it.name },
                                    onItemSelected = { _, item ->
                                        viewModel.setGenre(genres!!.genres.find { it.name == item }!!)
                                    }
                                )
                            }
                        }
                        WheelType.Adult -> {
                            Box(
                                Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ){
                                Column(
                                    Modifier.wrapContentSize(),
                                ){
                                    SingleRadioButton(
                                        selected = adultType == SettingAnswerType.Allow,
                                        onClick = {
                                            viewModel.setAdultType(SettingAnswerType.Allow)
                                        },
                                        title = SettingAnswerType.Allow.toString()
                                    )

                                    Spacer(modifier = Modifier.height(3.dp))

                                    SingleRadioButton(
                                        selected = adultType == SettingAnswerType.NotAllow,
                                        onClick = {
                                            viewModel.setAdultType(SettingAnswerType.NotAllow)
                                        },
                                        title = "Not Allow"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopImage(
    innerPadding: PaddingValues
){
    Image(
        painter = painterResource(id = R.drawable.ic_home_background), // Replace with your image
        contentDescription = "MovieTopBarImage",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
            .height(275.dp)
            .padding(innerPadding)
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ), // Adjust colors as needed
                        startY = 0f,
                        endY = size.height
                    )
                )
            }
    )
}

@Composable
private fun SingleCircularList(
    currentItem : String,
    items: List<String>,
    onItemSelected: (Int, String) -> Unit = {_,_ ->}
){
    InfiniteCircularList(
        modifier = Modifier.fillMaxWidth(),
        itemHeight = 30.dp,
        items = items,
        initialItem = currentItem,
        textStyle = TextStyle.Default,
        textColor = Color.Gray,
        textSize = 16,
        selectedTextColor = Color.White,
        onItemSelected = onItemSelected
    )
}

@Composable
fun SingleRadioButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    title: String = "Allow",
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(text = title, color = Color.Gray)
    }

}

@Preview
@Composable
fun SingleRadioButtonPreview(){
    SingleRadioButton(selected = true, onClick = {})
}


@Composable
private fun SingleLineTextField(
    text: String = "",
    onValueChange: (String) -> Unit = {_->},
    onSearchClick: () -> Unit = {},
){
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 60.dp, end = 60.dp, bottom = 50.dp),
        value = text,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = {
            Text(text = "Input Movie Keyword")
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.Gray,
            cursorColor = Color.Gray
        ),
        trailingIcon = {
            Button(
                onClick = onSearchClick,
                modifier =Modifier.size(50.dp),
                contentPadding = PaddingValues(5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                    contentDescription = "search icon",
                    tint = Color.Gray
                )
            }
        }
    )
}


