package com.example.feature_class.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.core_common.resut.UiState
import com.example.core_ui.components.state.GurukulEmptyState
import com.example.core_ui.components.filter.GurukulFilterDropdown
import com.example.core_ui.components.search.GurukulSearchBar
import com.example.core_ui.components.state.GurukulErrorState
import com.example.core_ui.components.state.GurukulLoadingState
import com.example.core_ui.components.toggle.GurukulSegmentedToggle
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

            GurukulSearchBar(
                query = searchQuery,
                onQueryChange = viewModel::updateSearch,
                placeholder = "Search class",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GurukulSegmentedToggle(
                    items = listOf("ALL", "Boys", "Girls"),
                    selectedItem = selectedGender,
                    onItemSelected = viewModel::selectGender
                )

                Spacer(modifier = Modifier.weight(1f))

                GurukulFilterDropdown(
                    selected = selectedStatus,
                    items = ClassListViewModel.StatusFilter.entries,
                    labelProvider = {
                        when (it) {
                            ClassListViewModel.StatusFilter.ALL -> "All"
                            ClassListViewModel.StatusFilter.ACTIVE -> "Active"
                            ClassListViewModel.StatusFilter.INACTIVE -> "Inactive"
                        }
                    },
                    onSelect = viewModel::changeStatusFilter
                )
            }

            Spacer(modifier = Modifier.height(8.dp))



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
}

@Composable
fun ClassRow(
    classModel: ClassModel,
    studentCount: Int = 15,
) {
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
                        text = "Scheduled: ${classModel.schedule}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = if (classModel.isActive) "Active" else "Inactive",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (classModel.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
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
    ClassRow(classModel = ClassModel(className = "Grade 10 - Maths - MA9", schedule = "\"M/W/F\""))
}






