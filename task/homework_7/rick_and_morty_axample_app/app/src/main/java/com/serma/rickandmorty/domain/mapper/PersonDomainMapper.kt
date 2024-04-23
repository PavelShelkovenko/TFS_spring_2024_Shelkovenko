package com.serma.rickandmorty.domain.mapper

import com.serma.rickandmorty.data.model.Person
import com.serma.rickandmorty.domain.model.PersonDomain

fun Person.toDomain(page: Int): PersonDomain {
    return PersonDomain(
        id = this.id,
        page = page,
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type,
        gender = this.gender,
        origin = this.origin,
        location = this.location,
        image = this.image,
        episode = this.episode,
        url = this.url,
        created = this.created
    )
}