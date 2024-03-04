package com.tui.github.repository.service.mapper;

import com.tui.github.repository.service.client.model.GitHubBranchDto;
import com.tui.github.repository.service.client.model.GitHubRepositoryDto;
import com.tui.github.repository.shared.GitHubRepositoryFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.tui.github.repository.shared.GitHubRepositoryFixture.SERVICE_REPOSITORY;

class GitHubRepositoryMapperTest {

    private final GitHubRepositoryMapper mapper = new GitHubRepositoryMapper();

    @Test
    void shouldMapRepositoryCorrectly() {
        // given
        GitHubRepositoryDto repo = GitHubRepositoryFixture.CLIENT_REPOSITORY_DTO;
        List<GitHubBranchDto> branches = GitHubRepositoryFixture.CLIENT_BRANCH_DTO_LIST;

        // when
        var result = mapper.toRepository(repo, branches);

        // then
        Assertions.assertEquals(result, SERVICE_REPOSITORY);
    }

}