package com.example.comicsapp.ui.view.components.expandedlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comicsapp.R
import com.example.comicsapp.ui.view.theme.ComicsAppTheme

@Composable
fun ExpandedList(
    text: String,
    expanded: Boolean = false,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
    style: ExpandedListStyle = ExpandedListStyle.default(),
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit
) {
    val isExpandedMenu = rememberSaveable { mutableStateOf(expanded) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(),
        colors = CardDefaults.cardColors(containerColor = style.backgroundColor),
        border = BorderStroke(style.borderWidth.dp, style.borderColor),
        shape = style.cornerBasedShape
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(style.headPadding)
            ) {
                Text(
                    text = text,
                    style = style.textStyle.copy(color = style.textColor),
                    modifier = Modifier.wrapContentHeight()
                )

                val iconResId = if (isExpandedMenu.value) {
                    R.drawable.arrow_top_svgrepo_com
                } else {
                    R.drawable.arrow_bottom_svgrepo_com
                }

                Icon(
                    imageVector = ImageVector.vectorResource(id = iconResId),
                    contentDescription = "Toggle Menu",
                    tint = style.textColor,
                    modifier = Modifier
                        .size(style.iconSize.dp)
                        .clickable { isExpandedMenu.value = !isExpandedMenu.value }
                )
            }

            if (isExpandedMenu.value) {
                HorizontalDivider(thickness = (style.borderWidth).dp, color = style.borderColor)
                Box(
                    contentAlignment = contentAlignment,
                    modifier = modifier.padding(style.contentPadding)
                ) {
                    content()
                }
            }
        }
    }
}


@Composable
@Preview
fun ExpandedListPreview() {
    ComicsAppTheme(1) {
        Column(modifier = Modifier.padding(16.dp)) {
            ExpandedList(
                text = "default Menu",
                style = ExpandedListStyle.default(),
                content = {
                    Text("default menu content")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExpandedList(
                text = "Secondary Menu",
                style = ExpandedListStyle.secondary(),
                content = {
                    Text("Secondary menu content")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExpandedList(
                text = "Success Menu",
                style = ExpandedListStyle.success(),
                content = {
                    Text("Success menu content")
                }
            )
        }
    }
}