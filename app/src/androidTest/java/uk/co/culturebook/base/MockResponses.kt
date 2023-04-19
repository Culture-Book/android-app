package uk.co.culturebook.base

import okhttp3.mockwebserver.MockResponse
import uk.co.culturebook.data.models.authentication.PublicJWT
import uk.co.culturebook.data.models.authentication.UserSession
import uk.co.culturebook.nav.toJsonString

object MockResponses {
    object Auth {
        private const val PublicKey =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3NK3kjPpIOM+OaVUMc2qTTmmn1GnXmrh40bSIHDDLuyCnbQKvM2u5g0xsmfjt9mx7gSjIrcI2AXh0WhzCfMFciq6L0mMtO9a9A3ZN3d1EYaVnCBH8sP/1srrWacTF96wQ5OuB/voptnQ3HO9kxNT++Y+gZS54NPmsDsPQQ+osXGgjq+wRavb2Vs+4LYrwLeaUpMWcEvsnJc4csGizBapvXLLUkTnAnJ6sBXaRFK/l3zsdSfn4FfIG8H1XZMOzfwDALbOm67EOmhGX5kVUHx2ux1pI3Ej1NvvAI98cAXdeF4uqgSKbRaXpTiF4gEDHJoPnlSV8Qu+vEJVFRe/fTAEnwIDAQAB"
        val successfulPublicKeyResponse = PublicJWT(PublicKey)
        val successfulLoginResponse = UserSession("token", "refreshToken")
    }
    /**
     *
    "/auth/v1/tos"
    "/add_new/v1/cultures"
    "/add_new/v1/culture"
    "/add_new/v1/element/duplicate"
    "/auth/v1/user/password"
    "/auth/v1/user"
    "/auth/v1/user"
    "/auth/v1/user"
    "/auth/v1/user/profile_picture"
    "/auth/v1/user/profile_picture"
    "/auth/v1/user/verification_status"
    "/add_new/v1/element/submit"
    "/add_new/v1/contribution/duplicate"
    "/add_new/v1/contribution/submit"
    "/elements/v1/elements"
    "/elements/v1/elements/user"
    "/elements/v1/cultures"
    "/elements/v1/cultures/user"
    "/elements/v1/elements/media"
    "/elements/v1/contributions"
    "/elements/v1/contributions"
    "/elements/v1/cultures"
    "/elements/v1/element"
    "/elements/v1/contributions/user"
    "/elements/v1/contributions/favourite"
    "/elements/v1/cultures/favourite"
    "/elements/v1/elements/favourite"
    "/elements/v1/contributions/media"
    "/elements/v1/element"
    "/elements/v1/contribution"
    "/elements/v1/block/element"
    "/elements/v1/block/contribution"
    "/elements/v1/block/culture"
    "/elements/v1/block/element"
    "/elements/v1/block/contribution"
    "/elements/v1/block/culture"
    "/auth/v1/.well-known/oauth/public"
    "/auth/v1/register"
    "/auth/v1/login"
    "/auth/v1/register-login"
    "/auth/v1/jwt-refresh"
    "/auth/v1/forgot"
    "/auth/v1/reset-password"
     */

    /**
     *
    /auth/v1/tos
    /add_new/v1/cultures
    /add_new/v1/culture
    /add_new/v1/element/duplicate
    /auth/v1/user/password
    /auth/v1/user
    /auth/v1/user
    /auth/v1/user
    /auth/v1/user/profile_picture
    /auth/v1/user/profile_picture
    /auth/v1/user/verification_status
    /add_new/v1/element/submit
    /add_new/v1/contribution/duplicate
    /add_new/v1/contribution/submit
    /elements/v1/elements
    /elements/v1/elements/user
    /elements/v1/cultures
    /elements/v1/cultures/user
    /elements/v1/elements/media
    /elements/v1/contributions
    /elements/v1/contributions
    /elements/v1/cultures
    /elements/v1/element
    /elements/v1/contributions/user
    /elements/v1/contributions/favourite
    /elements/v1/cultures/favourite
    /elements/v1/elements/favourite
    /elements/v1/contributions/media
    /elements/v1/element
    /elements/v1/contribution
    /elements/v1/block/element
    /elements/v1/block/contribution
    /elements/v1/block/culture
    /elements/v1/block/element
    /elements/v1/block/contribution
    /elements/v1/block/culture
     */
}