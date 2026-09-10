plugins {
    id("java")
    id("jacoco")
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.pitest)
}

group = "com.sistemaventas"
version = "0.1.0"

// JDK 21 instalado localmente ya cumple "Java 17 o superior" pedido por el profesor;
// no se fija un toolchain a 17 exacto para evitar que Gradle intente descargar un JDK.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Dependencias agrupadas por destino (regla kotlin:S6629): todo lo de
    // testImplementation junto, luego testRuntimeOnly, luego pitest.
    // JUnit 5 (Jupiter) - reemplaza JUnit 4 según el ejemplo del profesor
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    // Cucumber (pruebas de aceptación BDD)
    testImplementation(libs.cucumber.java)
    testImplementation(libs.cucumber.junit.platform.engine)
    testImplementation(libs.junit.platform.suite)
    testRuntimeOnly(libs.junit.platform.launcher)

    // PIT (mutation testing)
    pitest(libs.pitest.junit5.plugin)
}

sonar {
    properties {
        property("sonar.projectKey", "Sistema-de-Ventas-Pedidos-e-Inventario")
        property("sonar.projectName", "Proyecto Demo Calidad")
        property("sonar.host.url", "http://localhost:9000")
        // Token leído de la variable de entorno SONARQUBE_TOKEN (setx),
        // nunca queda escrito en texto plano en este archivo ni en git.
        property("sonar.token", System.getenv("SONARQUBE_TOKEN") ?: "")
        // Reporte XML de JaCoCo (unitarios + aceptación combinados, ver jacocoTestReport).
        property("sonar.coverage.jacoco.xmlReportPaths",
            layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml").get().asFile.path)
    }
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

// El reporte agregado de JaCoCo combina la ejecución de los tests unitarios
// (tasks.test) y de aceptación (acceptanceTest) para que la cobertura que ve
// SonarQube refleje ambas suites, no solo la unitaria.
tasks.jacocoTestReport {
    dependsOn(tasks.test, tasks.named("acceptanceTest"))
    executionData.setFrom(
        fileTree(layout.buildDirectory.dir("jacoco")) {
            include("*.exec")
        }
    )
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

pitest {
    pitestVersion.set(libs.versions.pitestPlugin.get())
    junit5PluginVersion.set(libs.versions.pitestJunit5.get())
    targetClasses.set(listOf("com.sistemaventas.*"))
    targetTests.set(listOf("com.sistemaventas.*"))
    threads.set(4)
    outputFormats.set(listOf("HTML"))
    timestampedReports.set(false)
}

tasks.test {
    // Cucumber vive en el mismo sourceSet de test (comparte classpath con los
    // unitarios), así que se excluyen explícitamente sus dos motores para que
    // esta task corra solo JUnit Jupiter: el motor "cucumber" (que si no se
    // excluye auto-descubre los .feature del classpath sin pasar por la suite)
    // y "junit-platform-suite" (que ejecutaría CucumberAcceptanceSuite).
    useJUnitPlatform {
        excludeEngines("cucumber", "junit-platform-suite")
    }
    finalizedBy(tasks.jacocoTestReport)
}

// Corre solo las pruebas de aceptación en Cucumber (features en
// src/test/resources/features, step definitions y suite en
// com.sistemaventas.producto.acceptance), separadas de los tests unitarios.
// Se restringe la selección de clases a la propia suite: así el motor
// "junit-platform-suite" es el único que dispara escenarios de Cucumber y
// JUnit Jupiter no encuentra nada que ejecutar en esta task.
tasks.register<Test>("acceptanceTest") {
    description = "Corre las pruebas de aceptación en Cucumber."
    group = "verification"
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
    include("**/CucumberAcceptanceSuite.class")
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}
