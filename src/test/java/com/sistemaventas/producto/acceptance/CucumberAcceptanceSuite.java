package com.sistemaventas.producto.acceptance;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Suite JUnit 5 Platform que ejecuta las pruebas de aceptación Cucumber
 * (features en src/test/resources/features, glue en este mismo paquete).
 * Es el único punto de entrada de las pruebas de aceptación: la task
 * "acceptanceTest" de Gradle corre solo esta clase.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "com.sistemaventas.producto.acceptance")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty")
public class CucumberAcceptanceSuite {
}
