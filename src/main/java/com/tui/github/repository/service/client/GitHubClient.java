package com.tui.github.repository.service.client;

import com.tui.github.repository.exception.NotFoundException;
import com.tui.github.repository.service.client.model.GitHubBranchDto;
import com.tui.github.repository.service.client.model.GitHubRepositoryDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class GitHubClient {

    private final WebClient webClient;

    public Flux<GitHubRepositoryDto> getRepositories(final String username) {
        return webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .onStatus(
                        HttpStatus.NOT_FOUND::equals,
                        error -> Mono.error(new NotFoundException(String.format("User %s not found", username)))
                )
                .bodyToFlux(GitHubRepositoryDto.class);
    }


    public Flux<GitHubBranchDto> getBranches(final String username, final String repository) {
        return webClient.get()
                .uri("/repos/{username}/{repositoryName}/branches", username, repository)
                .retrieve()
                .onStatus(
                        HttpStatus.NOT_FOUND::equals,
                        error -> Mono.error(
                                new NotFoundException(
                                        String.format("Branch for user %s and repository %s not found", username, repository)
                                )
                        )
                )
                .bodyToFlux(GitHubBranchDto.class);
    }
}
