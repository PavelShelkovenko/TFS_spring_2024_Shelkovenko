package com.serma.rickandmorty.presentation.utils

sealed interface LceState<out R> {
    data class Content<out T>(val content: T) : LceState<T>
    data object Loading : LceState<Nothing>
    data class Error(val throwable: Throwable) : LceState<Nothing>
}