group = "com.willfp"
version = rootProject.version

val spigotVersion = "1.19.1-R0.1-SNAPSHOT"

dependencies {
    compileOnly("org.spigotmc:spigot:$spigotVersion")
    compileOnly("org.spigotmc:spigot-api:$spigotVersion")
}

configurations.compileOnly {
    resolutionStrategy {
        force("org.spigotmc:spigot:$spigotVersion")
    }
}