plugins {
    `java-library`
    `azure-maven`
    project
}

subprojects {
    apply(plugin = "java-library")

    group = "com.foodtruck"
    version = "1.0.0"

    repositories {
        maven {
            url = uri("https://neowu.github.io/maven-repo/")
            content {
                includeGroupByRegex("core\\.framework.*")
            }
        }
        google()
        mavenCentral()
    }

    configure(subprojects.filter { (it.name.endsWith("-interface")) }) {
        java {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }

    dependencies {
        implementation(platform("com.wonder:wonder-dependencies:3.0.+"))
    }
}

configure(subprojects.filter { it.name.endsWith("-db-migration") }) {
    apply(plugin = "db-migration")

    dependencies {
        implementation("com.wonder:core-ng")
        runtimeOnly("com.mysql:mysql-connector-j")
    }
}

configure(subprojects.filter { (it.name.endsWith("-interface") || it.name.endsWith("-interface-v2")) }) {
    apply(plugin = "lib")
    dependencies {
        implementation("com.wonder:core-ng-api")
    }
}

configure(subprojects.filter { it.name.endsWith("-service") }) {
    apply(plugin = "app")
    dependencies {
        "implementation"("com.wonder:core-ng")
        "testImplementation"("com.wonder:core-ng-test")
        "implementation"("com.wonder:core-ext-open-api")
        "runtimeOnly"("com.mysql:mysql-connector-j")
        "testRuntimeOnly"("org.hsqldb:hsqldb")
    }
}

configure(
    listOf(
        project(":backend:user-service"),
        project(":backend:resource-service"),
        project(":backend:reservation-service"),
        project(":backend:notification-service"),
        project(":backend:scheduler-service")
    )
) {
    dependencies {
        runtimeOnly("com.mysql:mysql-connector-j")
        testRuntimeOnly("org.hsqldb:hsqldb")
    }
}

configure(
    listOf(
        project(":frontend:website"),
        project(":frontend:backoffice")
    )
) {
    apply(plugin = "app")
    dependencies {
        "implementation"("com.wonder:core-ng")
    }
}

project(":backend:user-service") {
    dependencies {
        "implementation"(project(":backend:user-service-interface"))
    }
}

project(":backend:resource-service") {
    dependencies {
        "implementation"(project(":backend:resource-service-interface"))
    }
}

project(":backend:meeting-room-service") {
    dependencies {
        "implementation"(project(":backend:meeting-room-service-interface"))
    }
}

project(":backend:reservation-service") {
    dependencies {
        "implementation"(project(":backend:reservation-service-interface"))
        "implementation"("com.wonder:core-ng-mongo")
    }
}

project(":backend:notification-service") {
    dependencies {
        "implementation"(project(":backend:reservation-service-interface"))
    }
}

project(":backend:scheduler-service") {
    dependencies {
        "implementation"(project(":backend:reservation-service-interface"))
    }
}

project(":frontend:website") {
    dependencies {
        "implementation"(project(":frontend:website-interface"))
        "implementation"(project(":backend:user-service-interface"))
        "implementation"(project(":backend:resource-service-interface"))
        "implementation"(project(":backend:reservation-service-interface"))
    }
}

project(":frontend:backoffice") {
    dependencies {
        "implementation"(project(":frontend:backoffice-interface"))
        "implementation"(project(":backend:user-service-interface"))
        "implementation"(project(":backend:resource-service-interface"))
        "implementation"(project(":backend:reservation-service-interface"))
    }
}