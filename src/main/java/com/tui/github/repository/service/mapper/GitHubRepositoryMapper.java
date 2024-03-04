package com.tui.github.repository.service.mapper;

import com.tui.github.repository.model.Branch;
import com.tui.github.repository.model.Repository;
import com.tui.github.repository.service.client.model.GitHubBranchDto;
import com.tui.github.repository.service.client.model.GitHubRepositoryDto;

import java.util.List;

public class GitHubRepositoryMapper {

    public Repository toRepository(GitHubRepositoryDto repositoryDto, List<GitHubBranchDto> branches) {
        return Repository.builder()
                .name(repositoryDto.name())
                .ownerLogin(getLogin(repositoryDto))
                .branches(toBranches(branches))
                .build();
    }

    private String getLogin(GitHubRepositoryDto repositoryDto) {
        return repositoryDto.owner() != null ? repositoryDto.owner().login() : null;
    }

    private List<Branch> toBranches(List<GitHubBranchDto> branches) {
        return branches.stream()
                .map(branch -> Branch.builder()
                        .name(branch.name())
                        .lastCommitSha(getSha(branch))
                        .build())
                .toList();
    }

    private String getSha(GitHubBranchDto branch) {
        return branch != null ? branch.commit().sha() : null;
    }
}
