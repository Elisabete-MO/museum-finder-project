package com.betrybe.museumfinder.evaluation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Req 10")
class DockerfileTest {

  Pattern commentRegex = Pattern.compile("^\\s*#");
  Pattern cmdRegex = Pattern.compile("^\\s*(?<cmd>[^#\\s]*)(?<params>\\s+.*)?$");
  List<DockerCommand> dockerfileCommands;

  @BeforeAll
  void loadDockerfileCommands() throws IOException {
    dockerfileCommands = Files.readAllLines(Path.of("Dockerfile"))
        .stream()
        .filter((line) -> !isComment(line) && !line.strip().isEmpty())
        .map((line) -> parseCommand(line))
        .collect(Collectors.toList());
  }

  private boolean isComment(String line) {
    return commentRegex.matcher(line).find();
  }

  private DockerCommand parseCommand(String line) {
    Matcher matcher = cmdRegex.matcher(line);

    if (!matcher.find()) {
      throw new RuntimeException("Could not parse docker command: " + line);
    }

    DockerCommand dockerCommand = new DockerCommand(
        matcher.group("cmd"),
        Optional.ofNullable(matcher.group("params"))
    );
    return dockerCommand;
  }

  @Test
  @DisplayName("10 - Dockerfile configurado corretamente")
  void testDockerfileConfiguredCorrectly() {
    // Tests about build-image
    assertAtLeastOneCommandMatch((dc) -> (
        "FROM".equals(dc.cmd)
            && dc.parameters.filter((v) -> v.contains("build-image")).isPresent()
    ), "Configuração da imagem 'build-image' não encontrada ou incorreta");

    assertAtLeastOneCommandMatch((dc) -> (
        "WORKDIR".equals(dc.cmd)
            && dc.parameters.filter((v) -> v.contains("/to-build-app")).isPresent()
    ), "Configuração do workdir da build-image não encontrada ou incorreta");

    assertAtLeastOneCommandMatch((dc) -> (
        "COPY".equals(dc.cmd)
            && dc.parameters.filter((v) -> v.contains("--from=build-image")).isEmpty()
    ), "Configuração de cópia para build-image não encontrada ou incorreta");

    assertAtLeastOneCommandMatch((dc) -> (
        "RUN".equals(dc.cmd)
            && dc.parameters
            .filter((v) -> v.contains("mvn"))
            .filter((v) -> v.contains("install") || v.contains("dependency") || v.contains("package"))
            .isPresent()
    ), "Configuração de resolução de dependências da build-image não encontrada ou incorreta");

    assertAtLeastOneCommandMatch((dc) -> (
        "RUN".equals(dc.cmd)
            && dc.parameters
            .filter((v) -> v.contains("mvn"))
            .filter((v) -> v.contains("package"))
            .isPresent()
    ), "Configuração de construção do pacote na build-image não encontrada ou incorreta");

    // Tests about the final image
    assertAtLeastOneCommandMatch((dc) -> (
        "FROM".equals(dc.cmd)
            && dc.parameters.filter((v) -> v.contains("build-image")).isEmpty()
    ), "Configuração da imagem final não encontrada ou incorreta");

    assertAtLeastOneCommandMatch((dc) -> (
        "COPY".equals(dc.cmd)
            && dc.parameters.filter((v) -> v.contains("--from=build-image")).isPresent()
    ), "Configuração de cópia da build-image para a imagem final não encontrada ou incorreta");

    assertAtLeastOneCommandMatch((dc) -> (
        "EXPOSE".equals(dc.cmd)
            && dc.parameters.filter((v) -> "8080".equals(v.strip())).isPresent()
    ), "Configuração de exposição de porta na imagem final não encontrada ou incorreta");

    assertAtLeastOneCommandMatch((dc) -> (
        "ENTRYPOINT".equals(dc.cmd)
            && dc.parameters
            .filter((v) -> (
                v.contains("java")
                    && v.contains("-jar")
                    && v.contains(".jar")
            ))
            .isPresent()
    ), "Configuração de entrypoing na imagem final não encontrada ou incorreta");
  }

  private void assertAtLeastOneCommandMatch(Predicate<DockerCommand> matcher, String message) {
    assertTrue(
        dockerfileCommands.stream().filter(matcher).findAny().isPresent(),
        message
    );
  }

  record DockerCommand(String cmd, Optional<String> parameters) {

  }
}

