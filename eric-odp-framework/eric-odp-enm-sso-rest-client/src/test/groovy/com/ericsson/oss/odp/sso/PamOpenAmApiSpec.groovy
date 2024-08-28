package com.ericsson.oss.odp.sso

import com.ericsson.oss.invoker.ApiClient
import com.ericsson.oss.invoker.ApiException
import com.ericsson.oss.model.TokenOrMessageResponse
import com.ericsson.oss.model.TokenValidationResponse
import spock.lang.Specification

class PamOpenAmApiSpec extends Specification {

    def MESSAGE_AUTHENTICATION_FAILED = '<title>OpenAM (Authentication Failed)</title><h3>Authentication failed.</h3>'
    def MESSAGE_VALIDATION_FAILED = '{"message":"Username and token username mismatch"}'
    def MOCK_GENERATED_SSO_TOKEN = 'some-random-sso-token'

    ApiClient spiedApiClient
    PamOpenAmApiWithDefaults objectUnderTest

    def setup() {
        spiedApiClient = Spy()
        spiedApiClient.setBasePath("http://sso:8080/singlesignon")
        objectUnderTest = new PamOpenAmApiWithDefaults(spiedApiClient)
    }

    def 'User authenticates successfully using the PAM OpenAM REST authentication service'() {
        given: 'A username and password'
        def username = 'administrator'
        def password = 'TestPassw0rd'

        TokenOrMessageResponse tokenOrMessageResponseToReturn = new TokenOrMessageResponse()
        tokenOrMessageResponseToReturn.tokenId = MOCK_GENERATED_SSO_TOKEN

        spiedApiClient.invokeAPI(_, _, _, _, _, _, _, _, _, _) >> tokenOrMessageResponseToReturn

        when: 'user authenticates'
        TokenOrMessageResponse tokenOrMessageResponse =
                objectUnderTest.pamAuthenticate(username, password)

        then: 'the response contains a valid SSO token and the message field is null'
        tokenOrMessageResponse.tokenId
        !tokenOrMessageResponse.message
    }

    def 'User fails to authenticate using the PAM OpenAM REST authentication service'() {
        given: 'A username and password'
        def username = 'administrator'
        def password = 'WrongPassword'

        TokenOrMessageResponse tokenOrMessageResponseToReturn = new TokenOrMessageResponse()
        tokenOrMessageResponseToReturn.message = MESSAGE_AUTHENTICATION_FAILED

        spiedApiClient.invokeAPI(_, _, _, _, _, _, _, _, _, _) >> tokenOrMessageResponseToReturn

        when: 'user authenticates'
        TokenOrMessageResponse tokenOrMessageResponse =
                objectUnderTest.pamAuthenticate(username, password)

        then: 'the response does not contain a valid SSO token and the message field is populated with an error message'
        !tokenOrMessageResponse.tokenId
        tokenOrMessageResponse.message == MESSAGE_AUTHENTICATION_FAILED
    }

    def 'A previously generated SSO token is successfully validated using the PAM OpenAM REST authentication service'() {
        given: 'A username and a valid SSO token'
        def username = 'administrator'
        def ssoToken = MOCK_GENERATED_SSO_TOKEN

        TokenOrMessageResponse tokenOrMessageResponseToReturn = new TokenOrMessageResponse()
        tokenOrMessageResponseToReturn.tokenId = MOCK_GENERATED_SSO_TOKEN

        spiedApiClient.invokeAPI(_, _, _, _, _, _, _, _, _, _) >> tokenOrMessageResponseToReturn

        when: 'user authenticates'
        TokenOrMessageResponse tokenOrMessageResponse =
                objectUnderTest.pamAuthenticate(username, ssoToken)

        then: 'the response contains a valid SSO token and the message field is null'
        tokenOrMessageResponse.tokenId == ssoToken
        !tokenOrMessageResponse.message
    }

    def 'A previously generated SSO token fails to validate using the PAM OpenAM REST authentication service'() {
        given: 'A username and an SSO token generated previously'
        def username = 'administrator'
        def ssoToken = 'Invalidtoken'

        TokenOrMessageResponse tokenOrMessageResponseToReturn = new TokenOrMessageResponse()
        tokenOrMessageResponseToReturn.message = MESSAGE_AUTHENTICATION_FAILED

        spiedApiClient.invokeAPI(_, _, _, _, _, _, _, _, _, _) >> tokenOrMessageResponseToReturn

        when: 'user authenticates'
        TokenOrMessageResponse tokenOrMessageResponse =
                objectUnderTest.pamAuthenticate(username, ssoToken)

        then: 'the response does not contain a valid SSO token and the message field is populated with an error message'
        !tokenOrMessageResponse.tokenId
        tokenOrMessageResponse.message == MESSAGE_AUTHENTICATION_FAILED
    }

    def 'A previously generated SSO token is validated successfully using the PAM OpenAM REST validation service'() {
        given: 'A username and a valid SSO token'
        def username = 'administrator'
        def ssoToken = MOCK_GENERATED_SSO_TOKEN

        TokenValidationResponse tokenValidationResponseToReturn = new TokenValidationResponse()
        tokenValidationResponseToReturn.valid = true

        spiedApiClient.invokeAPI(_, _, _, _, _, _, _, _, _, _) >> tokenValidationResponseToReturn

        when: 'user validates SSO token'
        TokenValidationResponse tokenValidationResponse =
                objectUnderTest.pamValidate(username, ssoToken)

        then: 'the response contains a valid SSO token and the message field is null'
        tokenValidationResponse.valid
        !tokenValidationResponse.message
    }

    def 'A previously generated SSO token fails to validate using the PAM OpenAM REST validation service'() {
        given: 'A username and an invalid SSO token'
        def username = 'administrator'
        def ssoToken = 'Invalidtoken'

        spiedApiClient.invokeAPI(_, _, _, _, _, _, _, _, _, _) >> { throw new ApiException(MESSAGE_VALIDATION_FAILED) }

        when: 'user validates SSO token'
        TokenValidationResponse tokenValidationResponse =
                objectUnderTest.pamValidate(username, ssoToken)

        then: 'an exception is thrown and the exception contains an error message'
        ApiException apiException = thrown()
        apiException.message == MESSAGE_VALIDATION_FAILED
    }
}