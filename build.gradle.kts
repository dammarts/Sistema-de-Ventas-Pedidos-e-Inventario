plugins {
    id("java")
    alias(libs.plugins.sonarqube)
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit)
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
