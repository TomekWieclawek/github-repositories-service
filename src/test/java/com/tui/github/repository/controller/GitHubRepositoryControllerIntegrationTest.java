package com.tui.github.repository.controller;

import com.tui.github.repository.shared.AbstractWiremockTest;
import org.junit.jupiter.api.Test;
import org.openapitools.model.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.tui.github.repository.shared.GitHubRepositoryFixture.getBranchesUrl;
import static com.tui.github.repository.shared.GitHubRepositoryFixture.getRepositoriesUrl;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class GitHubRepositoryControllerIntegrationTest extends AbstractWiremockTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldReturnRepositoriesForGivenUser() throws IOException {
        // given
        var user = "octocat";
        var repositoryResponse = getFixture("controller/github-repository-response.json");
        var branchesResponse = getFixture("controller/github-branches-response.json");
        prepareGithubClientStubFor(getRepositoriesUrl(user), HttpStatus.OK, repositoryResponse);
        prepareGithubClientStubFor(getBranchesUrl(user, "Hello-World"), HttpStatus.OK, branchesResponse);

        // when
        // then
        webTestClient.get().uri("/api/v1/users/{username}/repositories", user)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("Hello-World")
                .jsonPath("$[0].branches[0].name").isEqualTo("master")
                .jsonPath("$[0].branches[0].lastCommitSha").isEqualTo("c5b97d5ae6c19d5c5df71a34c7fbeeda2479ccbc")
                .jsonPath("$[0].ownerLogin").isEqualTo(user);

        verify(exactly(1), getRequestedFor(urlEqualTo(getRepositoriesUrl(user))));
        verify(exactly(1), getRequestedFor(urlEqualTo(getBranchesUrl(user, "Hello-World"))));
    }

    @Test
    void shouldReturnHttp404WhenUserNotFound() {
        // given
        var user = "user";
        prepareGithubClientStubFor(getRepositoriesUrl(user), HttpStatus.NOT_FOUND, null);

        // when
        // then
        webTestClient.get().uri("/api/v1/users/{username}/repositories", user)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectBodyList(ErrorResponse.class)
                .returnResult().getResponseBody();

        verify(exactly(1), getRequestedFor(urlEqualTo(getRepositoriesUrl(user))));
    }

    @Test
    void shouldReturnHttp406WhenInvalidXmlHeaderProvided() {
        // given
        var invalidMediaType = MediaType.APPLICATION_XML;
        var errorResponse = new ErrorResponse()
                .status(HttpStatus.NOT_ACCEPTABLE.value())
                .message("Could not find acceptable representation");

        // when
        // then
        webTestClient.get().uri("/api/v1/users/{username}/repositories", "test")
                .accept(invalidMediaType)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE)
                .expectBody(ErrorResponse.class)
                .isEqualTo(errorResponse);
    }
}
