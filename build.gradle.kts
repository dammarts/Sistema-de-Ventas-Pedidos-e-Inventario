plugins {
    id("java")
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.sonarqube)
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
