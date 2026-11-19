package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.Testament
import com.example.ui.theme.HoneyGold
import com.example.ui.theme.MeadowGreen
import com.example.ui.theme.OutlineSand
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.WarmPaper
import com.example.ui.viewmodel.MainUiState

@Composable
fun HomeScreen(
    uiState: MainUiState,
    onSelectTestament: (Testament?) -> Unit,
    onSearchChange: (String) -> Unit,
    onOpenStory: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredStories = uiState.stories.filter { story ->
        val matchesTestament = uiState.selectedTestament == null || story.testament == uiState.selectedTestament
        val q = uiState.searchQuery.trim()
        val matchesQuery = q.isEmpty() ||
            story.title.contains(q, ignoreCase = true) ||
            story.reference.contains(q, ignoreCase = true) ||
            story.id.toString() == q
        matchesTestament && matchesQuery
    }

    val visibleModules = uiState.modules.filter { mod ->
        filteredStories.any { it.moduleId == mod.id }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ParchmentCream),
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {
        item {
            HomeTopHeader(uiState = uiState, onOpenStory = onOpenStory)
        }
        item {
            WorldCardsCarousel(
                selectedTestament = uiState.selectedTestament,
                onSelectTestament = onSelectTestament
            )
        }
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier.fillMaxWidth().testTag("search_stories_input"),
                    placeholder = { Text("Rechercher une histoire (ex: Joseph, David, Noé)...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Rechercher") },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(Icons.Filled.Close, contentDescription = "Effacer")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = WarmPaper,
                        unfocusedContainerColor = WarmPaper,
                        focusedBorderColor = HoneyGold,
                        unfocusedBorderColor = OutlineSand
                    )
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = uiState.selectedTestament == null,
                        onClick = { onSelectTestament(null) },
                        label = { Text("Toutes (100)") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = HoneyGold.copy(alpha = 0.35f))
                    )
                    FilterChip(
                        selected = uiState.selectedTestament == Testament.ANCIEN_TESTAMENT,
                        onClick = { onSelectTestament(Testament.ANCIEN_TESTAMENT) },
                        label = { Text("Ancien (50)") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MeadowGreen.copy(alpha = 0.25f))
                    )
                    FilterChip(
                        selected = uiState.selectedTestament == Testament.NOUVEAU_TESTAMENT,
                        onClick = { onSelectTestament(Testament.NOUVEAU_TESTAMENT) },
                        label = { Text("Nouveau (50)") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = SkyBlue.copy(alpha = 0.25f))
                    )
                }
            }
        }

        visibleModules.forEach { module ->
            val moduleStories = filteredStories.filter { it.moduleId == module.id }
            val completedInMod = moduleStories.count { uiState.progressMap[it.id]?.isRead == true }

            item(key = "mod_${module.id}") {
                ModuleHeaderBanner(
                    module = module,
                    completedCount = completedInMod,
                    totalCount = moduleStories.size
                )
            }

            itemsIndexed(items = moduleStories, key = { _, s -> "story_${s.id}" }) { idx, story ->
                StoryPathNode(
                    story = story,
                    indexInModule = idx,
                    moduleColorHex = module.colorHex,
                    progress = uiState.progressMap[story.id],
                    isNextSuggested = story.id == uiState.nextSuggestedStoryId,
                    onStoryClick = onOpenStory
                )
            }
        }
    }
}
