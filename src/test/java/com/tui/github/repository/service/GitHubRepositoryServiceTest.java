package com.tui.github.repository.service;

import com.tui.github.repository.model.Repository;
import com.tui.github.repository.service.client.GitHubClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static com.tui.github.repository.shared.GitHubRepositoryFixture.CLIENT_BRANCH_DTO_LIST;
import static com.tui.github.repository.shared.GitHubRepositoryFixture.CLIENT_REPOSITORY_DTO_LIST;
import static com.tui.github.repository.shared.GitHubRepositoryFixture.REPOSITORY_A_NAME;
import static com.tui.github.repository.shared.GitHubRepositoryFixture.SERVICE_REPOSITORY_LIST;
import static com.tui.github.repository.shared.GitHubRepositoryFixture.USERNAME;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class GitHubRepositoryServiceTest {

    private final GitHubClient githubClient = Mockito.mock(GitHubClient.class);
    private final GitHubRepositoryService service = new GitHubRepositoryService(githubClient);

    @Test
    void shouldReturnRepositoryWithBranches() {
        // given 'repository with two branches'
        when(githubClient.getRepositories(USERNAME))
                .thenReturn(Flux.fromIterable(CLIENT_REPOSITORY_DTO_LIST));

        // and 'not forked repository contains two branches'
        when(githubClient.getBranches(USERNAME, REPOSITORY_A_NAME))
                .thenReturn(Flux.fromIterable(CLIENT_BRANCH_DTO_LIST));

        // when 'service is asked for repositories for given user'
        Flux<Repository> result = service.getRepositories(USERNAME);

        // then 'only one repository with branches is returned'
        StepVerifier.create(result)
                .expectNextSequence(SERVICE_REPOSITORY_LIST)
                .verifyComplete();

        Mockito.verify(githubClient, times(1)).getRepositories(eq(USERNAME));
        Mockito.verify(githubClient, times(1)).getBranches(eq(USERNAME), eq(REPOSITORY_A_NAME));
    }

    @Test
    void shouldNotReturnRepositoryIfBranchRequestWillFail() {
        // given: 'there are 2 repositories (one is forked)'
        when(githubClient.getRepositories(USERNAME)).thenReturn(Flux.fromIterable(CLIENT_REPOSITORY_DTO_LIST));

        // and: 'and there is connection issue with branches endpoint'
        when(githubClient.getBranches(USERNAME, REPOSITORY_A_NAME)).thenThrow(new RuntimeException("Connect exception"));

        // when: 'service is asked for branch for given user'
        Flux<Repository> result = service.getRepositories(USERNAME);

        // then: 'exception is returned and processing is stopped'
        StepVerifier.create(result)
                .verifyError(RuntimeException.class);
    }
}
