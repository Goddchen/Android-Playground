package de.goddchen.android.playground.playgrounds

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class PlayGround(
    val title: String,
    val framgentClassName: String
) : Parcelable