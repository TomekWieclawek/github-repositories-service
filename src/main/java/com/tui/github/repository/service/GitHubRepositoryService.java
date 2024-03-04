package com.tui.github.repository.service;

import com.tui.github.repository.model.Repository;
import com.tui.github.repository.service.client.GitHubClient;
import com.tui.github.repository.service.mapper.GitHubRepositoryMapper;
import com.tui.github.repository.service.client.model.GitHubRepositoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GitHubRepositoryService {


    /**
     * Setting maximal 10 http requests in parallel - default is 256
     */
    public static final int CONCURRENCY = 10;
    private final GitHubClient githubClient;
    private final GitHubRepositoryMapper mapper = new GitHubRepositoryMapper();

    public Flux<Repository> getRepositories(String username) {
        Flux<GitHubRepositoryDto> gitHubRepositories = githubClient.getRepositories(username);
        return gitHubRepositories
                .filter(repository -> !repository.fork())
                .flatMap(repository ->
                        githubClient.getBranches(username, repository.name())
                                .collectList()
                                .map(branches -> mapper.toRepository(repository, branches)), CONCURRENCY);
    }
}
