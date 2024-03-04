package com.tui.github.repository.shared;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

@AutoConfigureWireMock(port = 0)
public abstract class AbstractWiremockTest {

    @BeforeEach
    public void setUp() {
        WireMock.resetAllRequests();
    }

    protected void prepareGithubClientStubFor(String url, HttpStatus status, String responseBody) {
        stubFor(get(url).willReturn(aResponse()
                .withStatus(status.value())
                .withHeader("Content-Type", "application/json")
                .withBody(responseBody)
        ));
    }

    protected String getFixture(String fileName) throws IOException {
        var file = Path.of("", "src/test/resources").resolve(fileName);
        return Files.readString(file);
    }
}
