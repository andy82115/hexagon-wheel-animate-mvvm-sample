package com.example.moviefiltersample.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moviefiltersample.R
import com.example.moviefiltersample.ScreenRouter
import com.example.moviefiltersample.layer1Router

@Composable
fun SideTab(
    modifier: Modifier = Modifier,
    allRouters: List<ScreenRouter>,
    currentRouter: ScreenRouter,
    onTabSelected: (ScreenRouter) -> Unit
) {
    Surface(
        modifier = modifier.wrapContentSize(),
        color = Color.Transparent,
    ) {
        Column(Modifier.selectableGroup()) {
            Spacer(modifier = Modifier.height(5.dp))
            allRouters.forEach { router ->
                SideTabButton(
                    icon = router.icon,
                    onClick = { onTabSelected(router) },
                    selected = currentRouter == router
                )

                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

@Composable
private fun SideTabButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Transparent,
    icon: Int = R.drawable.ic_movie_filter,
    selected: Boolean
) {
    Button(
        onClick = onClick,
        modifier =modifier.size(30.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        border = BorderStroke(3.dp, if (selected) Color.Red else Color(0XFFb3d2ff))
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = "tab icon",
            tint = if (selected) Color.Red else Color(0XFFb3d2ff)
        )
    }
}

@Preview
@Composable
private fun SideTabButtonPreview(){
    SideTabButton(
        selected = true,
        onClick = {}
    )
}

@Preview
@Composable
private fun SideTabPreview(){
    SideTab(
        allRouters = layer1Router,
        currentRouter = layer1Router[0],
        onTabSelected = {}
    )
}

