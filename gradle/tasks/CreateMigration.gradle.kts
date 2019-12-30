import java.time.LocalDateTime
import java.time.format.DateTimeFormatter;

val migrationName: String by project

tasks.register("createMigration") {
    if (project.hasProperty("migrationName")) {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
        val file = file("$projectDir/src/main/resources/db/migration/V${timestamp}__${migrationName}.sql")

        file.createNewFile();
    }
}
