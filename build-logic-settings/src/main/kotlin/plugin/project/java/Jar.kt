package plugin.project.java

import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.withType

internal fun Project.configureJar() {
    tasks.withType<Jar> {

    }

//    tasks {
//        shadowJar {
//            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//            from(sourceSets["test"].output)
//            archiveBaseName.set("my-app")
//            mergeServiceFiles()
//            manifest {
//                attributes("Main-Class" to "com.example.MyApp")
//            }
//        }
//    }
}
