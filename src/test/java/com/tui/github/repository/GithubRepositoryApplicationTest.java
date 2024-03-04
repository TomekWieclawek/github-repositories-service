package com.tui.github.repository;

import com.tui.github.repository.service.GitHubRepositoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GithubRepositoryApplicationTest {

    @Autowired
    GitHubRepositoryService service;

    @Test
    void shouldLoadAppContext() {
        expect:
        Assertions.assertNotNull(service);
    }
}
