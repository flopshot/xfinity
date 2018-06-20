package com.xfinity.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class CharacterEntity(@PrimaryKey var description: String,
                           var pictureUrl: String,
                           var name: String,
                           var isFavorite: Boolean = false): Parcelable
