package com.tui.github.repository.shared;

import com.tui.github.repository.model.Branch;
import com.tui.github.repository.model.Repository;
import com.tui.github.repository.service.client.model.GitHubBranchCommitDto;
import com.tui.github.repository.service.client.model.GitHubBranchDto;
import com.tui.github.repository.service.client.model.GitHubRepositoryDto;
import com.tui.github.repository.service.client.model.GitHubRepositoryOwnerDto;

import java.util.List;

public interface GitHubRepositoryFixture {
    String USERNAME = "user";
    String REPOSITORY_A_NAME = "Repo_A";
    String REPOSITORY_B_NAME = "Repo_B";
    String BRANCH_A = "Branch_A";
    String BRANCH_B = "Branch_B";
    String COMMIT_SHA = "1";


    GitHubRepositoryDto CLIENT_REPOSITORY_DTO = GitHubRepositoryDto.builder()
            .name(REPOSITORY_A_NAME)
            .fork(false)
            .owner(new GitHubRepositoryOwnerDto(USERNAME))
            .build();

    List<GitHubRepositoryDto> CLIENT_REPOSITORY_DTO_LIST = List.of(
            CLIENT_REPOSITORY_DTO,
            GitHubRepositoryDto.builder()
                    .name(REPOSITORY_B_NAME)
                    .fork(true)
                    .owner(new GitHubRepositoryOwnerDto(USERNAME))
                    .build()
    );

    List<GitHubBranchDto> CLIENT_BRANCH_DTO_LIST = List.of(
            GitHubBranchDto.builder()
                    .name(BRANCH_A)
                    .commit(new GitHubBranchCommitDto(COMMIT_SHA))
                    .build(),
            GitHubBranchDto.builder()
                    .name(BRANCH_B)
                    .commit(new GitHubBranchCommitDto(COMMIT_SHA))
                    .build()
    );

    Repository SERVICE_REPOSITORY = Repository.builder()
            .name(REPOSITORY_A_NAME)
            .ownerLogin(USERNAME)
            .branches(List.of(
                    Branch.builder()
                            .name(BRANCH_A)
                            .lastCommitSha(COMMIT_SHA)
                            .build(),
                    Branch.builder()
                            .name(BRANCH_B)
                            .lastCommitSha(COMMIT_SHA)
                            .build()))
            .build();

    List<Repository> SERVICE_REPOSITORY_LIST = List.of(SERVICE_REPOSITORY);

    static String getBranchesUrl(String user, String repositoryName) {
        return String.format("/repos/%s/%s/branches", user, repositoryName);
    }

    static String getRepositoriesUrl(String user) {
        return String.format("/users/%s/repos", user);
    }
}
