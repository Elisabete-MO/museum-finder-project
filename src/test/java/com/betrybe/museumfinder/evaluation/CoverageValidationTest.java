package com.betrybe.museumfinder.evaluation;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.betrybe.museumfinder.evaluation.utils.CodeCoverageRunner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@DisplayName("Req 08-09")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoverageValidationTest {
  @Autowired
  @Qualifier("requestMappingHandlerMapping")
  RequestMappingHandlerMapping handlerMapping;

  @Test
  @DisplayName("08 - Cobertura mínima de 80% das linhas em CollectionTypeController e CollectionTypeService")
  public void testCollectionTypeCoverage() {
    CodeCoverageRunner codeCoverage =
        new CodeCoverageRunner("target-coverage-req-08", "collectionTypeCoverage");

    double minExpectedCoverage = 80;
    double actualCoverage = codeCoverage.run();

    checkCodeCoverage(minExpectedCoverage, actualCoverage);
  }

  @Test
  @DisplayName("09 - Controller, service e testes para /museums/{id} implementados")
  public void testMuseumId() {
    checkControllerImplemented();

    CodeCoverageRunner codeCoverage =
        new CodeCoverageRunner("target-coverage-req-09", "museumCoverage");

    double minExpectedCoverage = 80;
    double actualCoverage = codeCoverage.run();

    checkCodeCoverage(minExpectedCoverage, actualCoverage);
  }

  private void checkCodeCoverage(double minExpected, double actual) {
    assertTrue(
        actual >= minExpected,
        String.format(
            "Cobertura atual é de %.1f%%,"
                + " mas deveria ser de no mínimo %.1f%%",
            actual, minExpected)
    );
  }

  private void checkControllerImplemented() {
    String path = "/museums/{id}";
    RequestMethod method = RequestMethod.GET;

    for (RequestMappingInfo info : handlerMapping.getHandlerMethods().keySet()) {
      PathPatternsRequestCondition patterns = info.getPathPatternsCondition();
      if (patterns == null) {
        continue;
      }

      long machingPatterns = patterns.getPatterns().stream().filter(
          (pattern) -> pattern.getPatternString().contains(path)
      ).count();

      if (machingPatterns > 0 &&
          info.getMethodsCondition().getMethods().contains(method)) {
        return;
      }
    }
    fail("Endpoint %s %s não encontrado!".formatted(method, path));
  }
}