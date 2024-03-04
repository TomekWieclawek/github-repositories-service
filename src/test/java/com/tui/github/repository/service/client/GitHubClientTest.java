package com.tui.github.repository.service.client;

import com.tui.github.repository.exception.NotFoundException;
import com.tui.github.repository.service.client.model.GitHubBranchCommitDto;
import com.tui.github.repository.service.client.model.GitHubBranchDto;
import com.tui.github.repository.service.client.model.GitHubRepositoryDto;
import com.tui.github.repository.service.client.model.GitHubRepositoryOwnerDto;
import com.tui.github.repository.shared.AbstractWiremockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.tui.github.repository.shared.GitHubRepositoryFixture.BRANCH_A;
import static com.tui.github.repository.shared.GitHubRepositoryFixture.BRANCH_B;
import static com.tui.github.repository.shared.GitHubRepositoryFixture.COMMIT_SHA;
import static com.tui.github.repository.shared.GitHubRepositoryFixture.REPOSITORY_A_NAME;
import static com.tui.github.repository.shared.GitHubRepositoryFixture.REPOSITORY_B_NAME;
import static com.tui.github.repository.shared.GitHubRepositoryFixture.USERNAME;
import static com.tui.github.repository.shared.GitHubRepositoryFixture.getBranchesUrl;
import static com.tui.github.repository.shared.GitHubRepositoryFixture.getRepositoriesUrl;

@SpringBootTest
class GitHubClientTest extends AbstractWiremockTest {

    @Autowired
    private GitHubClient gitHubClient;

    @Test
    void shouldGetRepositoriesForGivenUser() throws IOException {

        // given
        List<GitHubRepositoryDto> expectedRepositories = List.of(
                new GitHubRepositoryDto(REPOSITORY_A_NAME, new GitHubRepositoryOwnerDto(USERNAME), false),
                new GitHubRepositoryDto(REPOSITORY_B_NAME, new GitHubRepositoryOwnerDto(USERNAME), true));
        prepareGithubClientStubFor(getRepositoriesUrl(USERNAME), HttpStatus.OK, getFixture("service/client/github-repositories.json"));

        // when
        var result = gitHubClient.getRepositories(USERNAME);

        // then
        StepVerifier.create(result)
                .expectNextSequence(expectedRepositories)
                .verifyComplete();

        verify(exactly(1), getRequestedFor(urlEqualTo(getRepositoriesUrl(USERNAME))));
    }

    @Test
    void shouldGetBranchesForGivenRepository() throws IOException {
        // given
        List<GitHubBranchDto> expectedRepositories = List.of(
                new GitHubBranchDto(BRANCH_A, new GitHubBranchCommitDto(COMMIT_SHA)),
                new GitHubBranchDto(BRANCH_B, new GitHubBranchCommitDto(COMMIT_SHA)));

        prepareGithubClientStubFor(getBranchesUrl(USERNAME, REPOSITORY_A_NAME), HttpStatus.OK, getFixture("service/client/github-branches.json"));

        // when
        var result = gitHubClient.getBranches(USERNAME, REPOSITORY_A_NAME);

        // then
        StepVerifier.create(result)
                .expectNextSequence(expectedRepositories)
                .verifyComplete();

        verify(exactly(1), getRequestedFor(urlEqualTo(getBranchesUrl(USERNAME, REPOSITORY_A_NAME))));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserNotFound() {
        // given
        prepareGithubClientStubFor(getRepositoriesUrl(USERNAME), HttpStatus.NOT_FOUND, null);

        // when
        var result = gitHubClient.getRepositories(USERNAME);

        // then
        StepVerifier.create(result)
                .verifyError(NotFoundException.class);

        verify(exactly(1), getRequestedFor(urlEqualTo(getRepositoriesUrl(USERNAME))));

    }

    @Test
    void shouldThrowNotFoundExceptionWhenBranchNotFound() {
        // given
        prepareGithubClientStubFor(getBranchesUrl(USERNAME, REPOSITORY_A_NAME), HttpStatus.NOT_FOUND, null);

        // when
        var result = gitHubClient.getBranches(USERNAME, REPOSITORY_A_NAME);

        // then
        StepVerifier.create(result)
                .verifyError(NotFoundException.class);

        verify(exactly(1), getRequestedFor(urlEqualTo(getBranchesUrl(USERNAME, REPOSITORY_A_NAME))));
    }
}
