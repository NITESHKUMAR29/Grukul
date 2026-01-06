package com.example.feature_class.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.core_common.resut.UiState
import com.example.core_ui.components.buttons.GurukulPrimaryButton
import com.example.core_ui.components.state.GurukulEmptyState
import com.example.core_ui.components.search.GurukulSearchBar
import com.example.core_ui.components.state.GurukulErrorState
import com.example.core_ui.components.state.GurukulLoadingState
import com.example.core_ui.ui.theme.GurukulTheme
import com.example.feature_class.domain.models.ClassModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ClassScreen(
    viewModel: ClassListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val searchQuery by viewModel.searchText.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val selectedGender by viewModel.selectedGender.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()
    val selectedBatchStatus by viewModel.selectedBatchStatus.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }

    // Calculate active filter count
    val activeFilterCount = listOfNotNull(
        if (selectedStatus != ClassListViewModel.StatusFilter.ALL) 1 else null,
        if (selectedGender != "All") 1 else null,
        if (selectedDay != null) 1 else null,
        if (selectedBatchStatus != ClassListViewModel.BatchStatusFilter.ALL) 1 else null
    ).size

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = viewModel::refresh
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        Column {
            // Search Bar
            GurukulSearchBar(
                query = searchQuery,
                onQueryChange = viewModel::updateSearch,
                placeholder = "Search class",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Active Filter Chips Row
            if (activeFilterCount > 0) {
                ActiveFilterChips(
                    selectedGender = selectedGender,
                    selectedStatus = selectedStatus,
                    selectedDay = selectedDay,
                    selectedBatchStatus = selectedBatchStatus,
                    onClearGender = { viewModel.selectGender("All") },
                    onClearStatus = { viewModel.changeStatusFilter(ClassListViewModel.StatusFilter.ALL) },
                    onClearDay = { viewModel.selectDay(null) },
                    onClearBatchStatus = { viewModel.changeBatchStatusFilter(ClassListViewModel.BatchStatusFilter.ALL) },
                    onClearAll = {
                        viewModel.selectGender("All")
                        viewModel.changeStatusFilter(ClassListViewModel.StatusFilter.ALL)
                        viewModel.selectDay(null)
                        viewModel.changeBatchStatusFilter(ClassListViewModel.BatchStatusFilter.ALL)
                    }
                )
            }

            // Filter Button Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Filter Button with Badge
                FilterButton(
                    activeFilterCount = activeFilterCount,
                    onClick = { showFilterSheet = true }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Content
            when (uiState) {
                UiState.Loading -> GurukulLoadingState()

                UiState.Empty -> GurukulEmptyState(
                    title = "No Classes Found",
                    description = "Start by creating your first class"
                )

                is UiState.Success -> {
                    val classes = (uiState as UiState.Success).data
                    LazyColumn {
                        items(classes, key = { it.id }) {
                            ClassRow(it)
                        }
                    }
                }

                is UiState.Error -> {
                    GurukulErrorState(
                        message = (uiState as UiState.Error).message,
                        onRetry = viewModel::refresh
                    )
                }

                else -> {}
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

    // Filter Bottom Sheet
    if (showFilterSheet) {
        FilterBottomSheet(
            selectedGender = selectedGender,
            selectedStatus = selectedStatus,
            selectedDay = selectedDay,
            selectedBatchStatus = selectedBatchStatus,
            onGenderSelected = viewModel::selectGender,
            onStatusSelected = viewModel::changeStatusFilter,
            onDaySelected = viewModel::selectDay,
            onBatchStatusSelected = viewModel::changeBatchStatusFilter,
            onDismiss = { showFilterSheet = false },
            onClearAll = {
                viewModel.selectGender("All")
                viewModel.changeStatusFilter(ClassListViewModel.StatusFilter.ALL)
                viewModel.selectDay(null)
                viewModel.changeBatchStatusFilter(ClassListViewModel.BatchStatusFilter.ALL)
            }
        )
    }
}

@Composable
fun FilterButton(
    activeFilterCount: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.FilterList,
            contentDescription = "Filter",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (activeFilterCount > 0) "Filters ($activeFilterCount)" else "Filters",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ActiveFilterChips(
    selectedGender: String,
    selectedStatus: ClassListViewModel.StatusFilter,
    selectedDay: Int?,
    selectedBatchStatus: ClassListViewModel.BatchStatusFilter,
    onClearGender: () -> Unit,
    onClearStatus: () -> Unit,
    onClearDay: () -> Unit,
    onClearBatchStatus: () -> Unit,
    onClearAll: () -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .clickable { onClearAll() }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Clear all",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        if (selectedGender != "All") {
            item {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { onClearGender() }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = selectedGender,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }

        if (selectedStatus != ClassListViewModel.StatusFilter.ALL) {
            item {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { onClearStatus() }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = when (selectedStatus) {
                                ClassListViewModel.StatusFilter.ACTIVE -> "Active"
                                ClassListViewModel.StatusFilter.INACTIVE -> "Inactive"
                                else -> "All"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }

        if (selectedBatchStatus != ClassListViewModel.BatchStatusFilter.ALL) {
            item {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { onClearBatchStatus() }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = when (selectedBatchStatus) {
                                ClassListViewModel.BatchStatusFilter.ONGOING -> "Ongoing"
                                ClassListViewModel.BatchStatusFilter.UPCOMING -> "Upcoming"
                                else -> "All"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }

        if (selectedDay != null) {
            item {
                val dayLabel = when (selectedDay) {
                    1 -> "Mon"
                    2 -> "Tue"
                    3 -> "Wed"
                    4 -> "Thu"
                    5 -> "Fri"
                    6 -> "Sat"
                    7 -> "Sun"
                    else -> ""
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { onClearDay() }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = dayLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    selectedGender: String,
    selectedStatus: ClassListViewModel.StatusFilter,
    selectedDay: Int?,
    selectedBatchStatus: ClassListViewModel.BatchStatusFilter,
    onGenderSelected: (String) -> Unit,
    onStatusSelected: (ClassListViewModel.StatusFilter) -> Unit,
    onDaySelected: (Int?) -> Unit,
    onBatchStatusSelected: (ClassListViewModel.BatchStatusFilter) -> Unit,
    onDismiss: () -> Unit,
    onClearAll: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.titleLarge,
                )
                TextButton(onClick = onClearAll) {
                    Text("Clear All", style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Gender Filter
            Text(
                text = "Gender",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("All", "Boys", "Girls").forEach { gender ->
                    val selected = selectedGender == gender
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (selected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { onGenderSelected(gender) }
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = gender,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (selected)
                                Color.White
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Status Filter
            Text(
                text = "Status",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ClassListViewModel.StatusFilter.entries.forEach { status ->
                    val selected = selectedStatus == status
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (selected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { onStatusSelected(status) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = when (status) {
                                ClassListViewModel.StatusFilter.ALL -> "All"
                                ClassListViewModel.StatusFilter.ACTIVE -> "Active"
                                ClassListViewModel.StatusFilter.INACTIVE -> "Inactive"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = if (selected)
                                Color.White
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Batch Status Filter
            Text(
                text = "Batch Status",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ClassListViewModel.BatchStatusFilter.entries.forEach { batchStatus ->
                    val selected = selectedBatchStatus == batchStatus
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (selected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { onBatchStatusSelected(batchStatus) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = when (batchStatus) {
                                ClassListViewModel.BatchStatusFilter.ALL -> "All"
                                ClassListViewModel.BatchStatusFilter.ONGOING -> "Ongoing"
                                ClassListViewModel.BatchStatusFilter.UPCOMING -> "Upcoming"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = if (selected)
                                Color.White
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Day Filter
            Text(
                text = "Day of Week",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            DayFilterGrid(
                selectedDay = selectedDay,
                onDaySelected = onDaySelected
            )

            Spacer(modifier = Modifier.height(24.dp))

            GurukulPrimaryButton(
                onClick = onDismiss,
                text = "Apply Filters",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DayFilterGrid(
    selectedDay: Int?,
    onDaySelected: (Int?) -> Unit
) {
    val days = listOf(
        null to "All",
        1 to "Mon", 2 to "Tue", 3 to "Wed",
        4 to "Thu", 5 to "Fri", 6 to "Sat", 7 to "Sun"
    )

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        days.forEach { (dayIndex, label) ->
            val selected = selectedDay == dayIndex

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (selected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { onDaySelected(dayIndex) }
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selected)
                        Color.White
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ClassRow(
    classModel: ClassModel,
    studentCount: Int = 15,
) {
    val weekDayMap = mapOf(1 to "M", 2 to "T", 3 to "W", 4 to "Th", 5 to "F", 6 to "S", 7 to "Su")

    val formattedDays = classModel.days
        .sorted()
        .mapNotNull { weekDayMap[it] }
        .joinToString(", ")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            bottomStart = 12.dp
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = classModel.className,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = if (formattedDays.isEmpty()) "No days scheduled" else "Scheduled: $formattedDays",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = if (classModel.isActive) "Active" else "Inactive",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (classModel.isActive)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error,
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Group,
                        contentDescription = "Students",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = studentCount.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClassRowPreview() {
    GurukulTheme {
        FilterBottomSheet(
            selectedGender = "All",
            selectedStatus = ClassListViewModel.StatusFilter.ALL,
            selectedDay = null,
            selectedBatchStatus = ClassListViewModel.BatchStatusFilter.ALL,
            onGenderSelected = {},
            onStatusSelected = {},
            onDaySelected = {},
            onBatchStatusSelected = {},
            onDismiss = {},
            onClearAll = {}
        )
    }
}