package com.example.data.mapper

import com.example.data.remote.model.UserDto
import com.example.data.remote.model.AddressDto
import com.example.data.remote.model.CompanyDto
import com.example.data.remote.model.GeoDto
import com.example.domain.model.User
import com.example.domain.model.Address
import com.example.domain.model.Company
import com.example.domain.model.Geo

/**
 * Mapper functions for converting between UserDto and User domain model
 */
fun UserDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        username = username,
        email = email,
        phone = phone,
        website = website,
        address = address.toDomain(),
        company = company.toDomain()
    )
}

fun AddressDto.toDomain(): Address {
    return Address(
        street = street,
        suite = suite,
        city = city,
        zipcode = zipcode,
        geo = geo.toDomain()
    )
}

fun GeoDto.toDomain(): Geo {
    return Geo(
        lat = lat,
        lng = lng
    )
}

fun CompanyDto.toDomain(): Company {
    return Company(
        name = name,
        catchPhrase = catchPhrase,
        bs = bs
    )
} 