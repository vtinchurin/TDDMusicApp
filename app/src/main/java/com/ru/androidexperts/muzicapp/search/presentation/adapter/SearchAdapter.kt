package com.ru.androidexperts.muzicapp.search.presentation.adapter

import com.ru.androidexperts.muzicapp.core.adapter.GenericAdapter

class SearchAdapter(
    clickActions: SearchScreenActions.Mutable,
    typeList: List<SearchItemType> = listOf(
        SearchItemType.Track,
        SearchItemType.Progress,
        SearchItemType.Error,
        SearchItemType.NoTrack,
    ),
) : GenericAdapter.Abstract<SearchScreenActions.Mutable>(clickActions, typeList)