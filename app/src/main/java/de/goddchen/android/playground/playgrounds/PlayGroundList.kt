package de.goddchen.android.playground.playgrounds

class PlayGroundList {
    companion object {
        val playGrounds = listOf(
            PlayGround("AndroidX: Room", "de.goddchen.android.playground.playgrounds.room.RoomFragment"),
            PlayGround(
                "AndroidX: Biometrics",
                "de.goddchen.android.playground.playgrounds.biometrics.BiometricsFragment"
            )
        )
    }
}