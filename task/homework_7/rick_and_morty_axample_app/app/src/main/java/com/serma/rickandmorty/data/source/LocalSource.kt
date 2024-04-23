package com.serma.rickandmorty.data.source

import com.serma.rickandmorty.data.model.Person

class LocalSource {

    private val data: MutableList<Person> = mutableListOf()
    fun saveData(person: Person) {
        data.add(person)
    }

    fun getData(position: Int): Person? {
        return data.getOrNull(position)
    }
}