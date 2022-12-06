package ru.hh.toolbar

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize
import ru.hh.toolbar.custom_toolbar.CollapsingTitle
import ru.hh.toolbar.custom_toolbar.CustomToolbar
import ru.hh.toolbar.custom_toolbar.rememberToolbarScrollBehavior

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CustomToolbarDemo() {
    val toolbarDemoSettingsState = rememberSaveable { mutableStateOf(ToolbarDemoSettings()) }

    val scrollBehavior = rememberToolbarScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomToolbar(
                navigationIcon = getNavigationIconSlot(toolbarDemoSettingsState.value),
                actions = getActionsSlot(toolbarDemoSettingsState.value),
                collapsingTitle = getCollapsingTitle(toolbarDemoSettingsState.value),
                centralContent = getCentralContentSlot(toolbarDemoSettingsState.value),
                additionalContent = getAdditionalContentSlot(toolbarDemoSettingsState.value),
                scrollBehavior = scrollBehavior
            )
        },
    ) { contentPadding ->
        LazyColumn(modifier = Modifier.padding(contentPadding)) {
            item("collapsing_title_settings") {
                Spacer(modifier = Modifier.height(16.dp))
                CollapsingTitleSettings(toolbarDemoSettingsState)
            }
            item("back_navigation_settings") {
                Spacer(modifier = Modifier.height(16.dp))
                BackNavigationSettings(toolbarDemoSettingsState)
            }
            item("actions_settings") {
                Spacer(modifier = Modifier.height(16.dp))
                ActionsContentSettings(toolbarDemoSettingsState)
            }
            item("central_content_settings") {
                Spacer(modifier = Modifier.height(16.dp))
                CentralContentSettings(toolbarDemoSettingsState)
            }
            item("additional_content_settings") {
                Spacer(modifier = Modifier.height(16.dp))
                AdditionalContentSettings(toolbarDemoSettingsState)
                Spacer(modifier = Modifier.height(24.dp))
            }
            scrollableItemsForSample()
        }
    }
}

private fun getNavigationIconSlot(toolbarDemoSettings: ToolbarDemoSettings): (@Composable () -> Unit)? {
    return when (toolbarDemoSettings.backNavigationMode) {
        BackNavigationMode.BackArrow -> {
            { Icon(Icons.Default.ArrowBack, null) }
        }

        BackNavigationMode.None -> {
            null
        }
    }
}

private fun getActionsSlot(toolbarDemoSettings: ToolbarDemoSettings): (@Composable RowScope.() -> Unit)? {
    return when (toolbarDemoSettings.actionMode) {
        ActionMode.Icon -> {
            {
                Icon(Icons.Default.Favorite, null)
            }
        }

        ActionMode.Text -> {
            {
                Text("Button")
            }
        }

        ActionMode.None -> {
            null
        }
    }
}

private fun getCentralContentSlot(toolbarDemoSettings: ToolbarDemoSettings): (@Composable () -> Unit)? {
    return when (toolbarDemoSettings.centralContentMode) {
        CentralContentMode.ProgressBar -> {
            {
                // progress-bar style widget
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color = Color.Gray)
                ) {
                    Box(modifier = Modifier
                        .width(100.dp)
                        .fillMaxHeight()
                        .background(color = Color.Red))
                }
            }
        }

        CentralContentMode.Title -> {
            {
                Text(text = "Screen title", style = MaterialTheme.typography.titleLarge)
            }
        }

        CentralContentMode.TitleSubtitle -> {
            {
                Column {
                    Text(text = "Screen title", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Subtitle", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        CentralContentMode.None -> {
            null
        }
    }
}

@Composable
private fun getCollapsingTitle(toolbarDemoSettings: ToolbarDemoSettings): CollapsingTitle? {
    return when (toolbarDemoSettings.collapsingTitleMode) {
        CollapsingTitleMode.SectionTitle -> {
            CollapsingTitle.large("Section title")
        }

        CollapsingTitleMode.SubsectionTitle -> {
            CollapsingTitle.medium("Subsection title")
        }

        CollapsingTitleMode.SectionTitleMultiLine -> {
            CollapsingTitle.large("Section title with large multiline text")
        }

        CollapsingTitleMode.SubsectionTitleMultiLine -> {
            CollapsingTitle.medium("Subsection title with large multiline text")
        }

        CollapsingTitleMode.None -> {
            null
        }
    }
}

private fun getAdditionalContentSlot(toolbarDemoSettings: ToolbarDemoSettings): (@Composable () -> Unit)? {
    return when (toolbarDemoSettings.additionalContentMode) {
        AdditionalContentMode.Tabs -> {
            {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    TabRow(
                        selectedTabIndex = 0,
                        tabs = {
                            Tab(text = { Text("Favorites") }, selected = true, onClick = { })
                            Tab(text = { Text("Subscriptions") }, selected = false, onClick = { })
                        }
                    )
                }
            }
        }
        AdditionalContentMode.None -> {
            null
        }
    }
}

@Composable
private fun MutableState<ToolbarDemoSettings>.ToolbarDemoSettingsRadio(
    name: String,
    isSelected: (ToolbarDemoSettings) -> Boolean,
    onSelect: (ToolbarDemoSettings) -> ToolbarDemoSettings,
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { value = onSelect(value) }
        .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        RadioButton(selected = isSelected(value), onClick = null)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = name,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ToolbarDemoSettingsTitle(text: String) {
    Text(modifier = Modifier.padding(16.dp), text = text, style = MaterialTheme.typography.titleSmall)
}

@Composable
private fun ActionsContentSettings(toolbarDemoSettingsState: MutableState<ToolbarDemoSettings>) {
    with(toolbarDemoSettingsState) {
        ToolbarDemoSettingsTitle("Actions content")
        ToolbarDemoSettingsRadio(
            name = "Icon",
            isSelected = { it.actionMode == ActionMode.Icon },
            onSelect = { it.copy(actionMode = ActionMode.Icon) }
        )
        ToolbarDemoSettingsRadio(
            name = "Text",
            isSelected = { it.actionMode == ActionMode.Text },
            onSelect = { it.copy(actionMode = ActionMode.Text) }
        )
        ToolbarDemoSettingsRadio(
            name = "None",
            isSelected = { it.actionMode == ActionMode.None },
            onSelect = { it.copy(actionMode = ActionMode.None) }
        )
    }
}

@Composable
private fun BackNavigationSettings(toolbarDemoSettingsState: MutableState<ToolbarDemoSettings>) {
    with(toolbarDemoSettingsState) {
        ToolbarDemoSettingsTitle("Back navigation content")
        ToolbarDemoSettingsRadio(
            name = "Back Navigation",
            isSelected = { it.backNavigationMode == BackNavigationMode.BackArrow },
            onSelect = { it.copy(backNavigationMode = BackNavigationMode.BackArrow) }
        )
        ToolbarDemoSettingsRadio(
            name = "None",
            isSelected = { it.backNavigationMode == BackNavigationMode.None },
            onSelect = { it.copy(backNavigationMode = BackNavigationMode.None) }
        )
    }
}

@Composable
private fun AdditionalContentSettings(toolbarDemoSettingsState: MutableState<ToolbarDemoSettings>) {
    with(toolbarDemoSettingsState) {
        ToolbarDemoSettingsTitle("Additional content")
        ToolbarDemoSettingsRadio(
            name = "Tabs",
            isSelected = { it.additionalContentMode == AdditionalContentMode.Tabs },
            onSelect = {
                it.copy(additionalContentMode = AdditionalContentMode.Tabs)
            }
        )
        ToolbarDemoSettingsRadio(
            name = "None",
            isSelected = { it.additionalContentMode == AdditionalContentMode.None },
            onSelect = { it.copy(additionalContentMode = AdditionalContentMode.None) }
        )
    }
}

@Composable
private fun CentralContentSettings(toolbarDemoSettingsState: MutableState<ToolbarDemoSettings>) {
    with(toolbarDemoSettingsState) {
        ToolbarDemoSettingsTitle("Central content")
        ToolbarDemoSettingsRadio(
            name = "Title",
            isSelected = { it.centralContentMode == CentralContentMode.Title },
            onSelect = {
                it.copy(centralContentMode = CentralContentMode.Title)
            }
        )
        ToolbarDemoSettingsRadio(
            name = "Title + Subtitle",
            isSelected = { it.centralContentMode == CentralContentMode.TitleSubtitle },
            onSelect = {
                it.copy(centralContentMode = CentralContentMode.TitleSubtitle)
            }
        )
        ToolbarDemoSettingsRadio(
            name = "ProgressBar",
            isSelected = { it.centralContentMode == CentralContentMode.ProgressBar },
            onSelect = {
                it.copy(centralContentMode = CentralContentMode.ProgressBar)
            }
        )
        ToolbarDemoSettingsRadio(
            name = "None",
            isSelected = { it.centralContentMode == CentralContentMode.None },
            onSelect = { it.copy(centralContentMode = CentralContentMode.None) }
        )
    }
}

@Composable
private fun CollapsingTitleSettings(toolbarDemoSettingsState: MutableState<ToolbarDemoSettings>) {
    with(toolbarDemoSettingsState) {
        ToolbarDemoSettingsTitle("Collapsing title")
        ToolbarDemoSettingsRadio(
            name = "Section title",
            isSelected = { it.collapsingTitleMode == CollapsingTitleMode.SectionTitle },
            onSelect = {
                it.copy(collapsingTitleMode = CollapsingTitleMode.SectionTitle)
            }
        )
        ToolbarDemoSettingsRadio(
            name = "Subsection title",
            isSelected = { it.collapsingTitleMode == CollapsingTitleMode.SubsectionTitle },
            onSelect = {
                it.copy(collapsingTitleMode = CollapsingTitleMode.SubsectionTitle)
            }
        )
        ToolbarDemoSettingsRadio(
            name = "Section title multiline",
            isSelected = { it.collapsingTitleMode == CollapsingTitleMode.SectionTitleMultiLine },
            onSelect = {
                it.copy(collapsingTitleMode = CollapsingTitleMode.SectionTitleMultiLine)
            }
        )
        ToolbarDemoSettingsRadio(
            name = "Subsection title multiline",
            isSelected = { it.collapsingTitleMode == CollapsingTitleMode.SubsectionTitleMultiLine },
            onSelect = {
                it.copy(collapsingTitleMode = CollapsingTitleMode.SubsectionTitleMultiLine)
            }
        )
        ToolbarDemoSettingsRadio(
            name = "None",
            isSelected = { it.collapsingTitleMode == CollapsingTitleMode.None },
            onSelect = {
                it.copy(collapsingTitleMode = CollapsingTitleMode.None)
            }
        )
    }
}

private fun LazyListScope.scrollableItemsForSample() {
    for (i in 0..100) {
        item("scroll_test_$i") {
            Text(modifier = Modifier.fillMaxWidth().padding(16.dp), text = "Item for scroll testing #$i")
        }
    }
}

private enum class BackNavigationMode {
    BackArrow, None
}

private enum class ActionMode {
    Icon, Text, None
}

private enum class CentralContentMode {
    ProgressBar, Title, TitleSubtitle, None
}

private enum class CollapsingTitleMode {
    SectionTitle, SubsectionTitle, SectionTitleMultiLine, SubsectionTitleMultiLine, None
}

private enum class AdditionalContentMode {
    Tabs, None
}

@Parcelize
private data class ToolbarDemoSettings(
    val backNavigationMode: BackNavigationMode = BackNavigationMode.BackArrow,
    val actionMode: ActionMode = ActionMode.Icon,
    val centralContentMode: CentralContentMode = CentralContentMode.None,
    val collapsingTitleMode: CollapsingTitleMode = CollapsingTitleMode.SectionTitle,
    val additionalContentMode: AdditionalContentMode = AdditionalContentMode.None,
) : Parcelable
