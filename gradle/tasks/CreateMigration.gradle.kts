import java.time.LocalDateTime
import java.time.format.DateTimeFormatter;


tasks.register("createMigration") {
    val migration: String by project

    val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
    val file = file("$projectDir/src/main/resources/db/migration/V${timestamp}__${migration}.sql")

    file.createNewFile();
}
