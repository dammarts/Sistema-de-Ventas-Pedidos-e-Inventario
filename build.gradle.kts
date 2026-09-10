plugins {
    id("java")
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

    // JUnit 5 (Jupiter) - reemplaza JUnit 4 según el ejemplo del profesor
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

    // PIT (mutation testing)
    pitest(libs.pitest.junit5.plugin)

    // Cucumber (pruebas de aceptación BDD)
    testImplementation(libs.cucumber.java)
    testImplementation(libs.cucumber.junit.platform.engine)
}

sonar {
    properties {
        property("sonar.projectKey", "Sistema-de-Ventas-Pedidos-e-Inventario")
        property("sonar.projectName", "Proyecto Demo Calidad")
        property("sonar.host.url", "http://localhost:9000")
        // Token leído de la variable de entorno SONARQUBE_TOKEN (setx),
        // nunca queda escrito en texto plano en este archivo ni en git.
        property("sonar.token", System.getenv("SONARQUBE_TOKEN") ?: "")
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
    useJUnitPlatform()
}

// Corre solo las pruebas de aceptación en Cucumber, separadas de los tests unitarios.
// Todavía no hay archivos .feature ni step definitions - se agregan cuando el
// profesor defina los escenarios concretos, o al atacar Pedido/Inventario.
tasks.register<Test>("acceptanceTest") {
    useJUnitPlatform()
    description = "Corre las pruebas de aceptación en Cucumber."
    group = "verification"
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
}
