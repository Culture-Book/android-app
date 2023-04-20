package uk.co.culturebook.base

import uk.co.culturebook.data.models.authentication.PublicJWT
import uk.co.culturebook.data.models.authentication.User
import uk.co.culturebook.data.models.authentication.UserSession
import uk.co.culturebook.data.models.cultural.Comment
import uk.co.culturebook.data.models.cultural.Contribution
import uk.co.culturebook.data.models.cultural.Culture
import uk.co.culturebook.data.models.cultural.CultureResponse
import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.data.models.cultural.Location
import uk.co.culturebook.data.models.cultural.Media
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import java.util.UUID

object MockResponses {
    object Auth {
        private const val PublicKey =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3NK3kjPpIOM+OaVUMc2qTTmmn1GnXmrh40bSIHDDLuyCnbQKvM2u5g0xsmfjt9mx7gSjIrcI2AXh0WhzCfMFciq6L0mMtO9a9A3ZN3d1EYaVnCBH8sP/1srrWacTF96wQ5OuB/voptnQ3HO9kxNT++Y+gZS54NPmsDsPQQ+osXGgjq+wRavb2Vs+4LYrwLeaUpMWcEvsnJc4csGizBapvXLLUkTnAnJ6sBXaRFK/l3zsdSfn4FfIG8H1XZMOzfwDALbOm67EOmhGX5kVUHx2ux1pI3Ej1NvvAI98cAXdeF4uqgSKbRaXpTiF4gEDHJoPnlSV8Qu+vEJVFRe/fTAEnwIDAQAB"
        val successfulPublicKeyResponse = ApiResponse.Success(PublicJWT(PublicKey))
        val successfulLoginResponse = ApiResponse.Success(UserSession("token", "refreshToken"))
        val failedLoginResponse = ApiResponse.Failure<UserSession>(400, "Invalid credentials")
    }

    object Api {
        val successfulUserResponse = ApiResponse.Success(
            User(
                profileUri = null,
                displayName = null,
                password = "",
                email = "",
            )
        )
        val successfulElementsResponse = ApiResponse.Success(
            listOf(
                Element(
                    id = UUID.randomUUID(),
                    name = "Element 1",
                    information = "Description 1",
                    cultureId = UUID.randomUUID(),
                    media = listOf(),
                    favourite = false,
                    location = Location(0.0, 0.0),
                    type = ElementType.Food
                ),
            )
        )
        val successfulElementsResponse1 = ApiResponse.Success(
            listOf(
                Element(
                    id = UUID.randomUUID(),
                    name = "Different Element 1",
                    information = "Description 1",
                    cultureId = UUID.randomUUID(),
                    media = listOf(),
                    favourite = true,
                    location = Location(0.0, 0.0),
                    type = ElementType.Event,
                    isVerified = true
                ),
            )
        )
        val successfulElementResponse = ApiResponse.Success(
            Element(
                id = UUID.randomUUID(),
                name = "Different Element 1",
                information = "Description 1",
                cultureId = UUID.randomUUID(),
                media = listOf(),
                favourite = true,
                location = Location(0.0, 0.0),
                type = ElementType.Music,
                isVerified = true
            )
        )

        val successfulCommentResponse = ApiResponse.Success(
            Comment(comment = "comment 4", isMine = true)
        )

        val successfulReactionResponse = ApiResponse.Success(true)

        val successfulCommentsResponse = ApiResponse.Success(
            listOf(
                Comment(comment = "comment 1"),
                Comment(comment = "comment 2"),
                Comment(comment = "comment 3"),
                Comment(comment = "comment 4", isMine = true),
            )
        )

        val successfulContributionsResponse = ApiResponse.Success(
            listOf(
                Contribution(
                    id = UUID.randomUUID(),
                    name = "Contribution 1",
                    information = "Description 1",
                    elementId = UUID.randomUUID(),
                    media = listOf(),
                    favourite = false,
                    location = Location(0.0, 0.0),
                    type = ElementType.Food
                ),
            )
        )
        val successfulContributionResponse = ApiResponse.Success(
            Contribution(
                id = UUID.randomUUID(),
                name = "Contribution 1",
                information = "Description 1",
                elementId = UUID.randomUUID(),
                media = listOf(),
                favourite = false,
                location = Location(0.0, 0.0),
                type = ElementType.Food
            ),
        )
        val successfulCultures = ApiResponse.Success(
            listOf(
                Culture(
                    id = UUID.randomUUID(),
                    name = "Culture 1",
                    favourite = false,
                    location = Location(0.0, 0.0),
                )
            )
        )
        val successfulCulturesResponse = ApiResponse.Success(
            CultureResponse(successfulCultures.data)
        )
        val successfulMediaResponse = ApiResponse.Success(listOf<Media>())

        val noDuplicateElementsResponse = ApiResponse.Success(listOf<Element>())
    }
}