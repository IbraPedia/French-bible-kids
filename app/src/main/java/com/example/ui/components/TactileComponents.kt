package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.HoneyGold
import com.example.ui.theme.HoneyGoldDark
import com.example.ui.theme.OutlineSand
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.WarmInk
import com.example.ui.theme.WarmPaper

@Composable
fun TactileButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = HoneyGold,
    shadowColor: Color = HoneyGoldDark,
    contentColor: Color = WarmInk,
    enabled: Boolean = true,
    testTag: String = "tactile_button",
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressOffset by animateDpAsState(if (isPressed && enabled) 4.dp else 0.dp, label = "press")

    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .testTag(testTag)
    ) {
        // Bottom 3D bevel layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 5.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(if (enabled) shadowColor else OutlineSand)
        )
        // Top tactile surface
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = pressOffset)
                .clip(RoundedCornerShape(18.dp))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    onClick = onClick
                ),
            shape = RoundedCornerShape(18.dp),
            color = if (enabled) containerColor else Color(0xFFEAE0D0),
            contentColor = contentColor
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

@Composable
fun StorybookPill(
    emoji: String,
    text: String,
    backgroundColor: Color = WarmPaper,
    borderColor: Color = OutlineSand,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = backgroundColor,
        border = BorderStroke(1.5.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(text = emoji)
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = WarmInk
            )
        }
    }
}

@Composable
fun BibleBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        Triple("home", "Histoires", Pair(Icons.Filled.AutoStories, Icons.Outlined.AutoStories)),
        Triple("badges", "Badges", Pair(Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents)),
        Triple("dashboard", "Progrès", Pair(Icons.Filled.Face, Icons.Outlined.Face)),
        Triple("parents", "Parents", Pair(Icons.Filled.Shield, Icons.Outlined.Shield))
    )

    NavigationBar(
        containerColor = ParchmentCream,
        tonalElevation = 6.dp,
        windowInsets = WindowInsets.navigationBars
    ) {
        items.forEach { (route, label, icons) ->
            val selected = currentRoute == route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(route) },
                modifier = Modifier.testTag("nav_$route"),
                icon = {
                    Icon(
                        imageVector = if (selected) icons.first else icons.second,
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = WarmInk,
                    selectedTextColor = WarmInk,
                    indicatorColor = HoneyGold.copy(alpha = 0.35f)
                )
            )
        }
    }
}
