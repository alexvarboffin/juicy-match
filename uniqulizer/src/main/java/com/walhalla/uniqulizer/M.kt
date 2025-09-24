package com.walhalla.uniqulizer

import java.io.File

class M {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val projectRoot = "C:\\Users\\combo\\Desktop\\juicy-match"
            //val module = "zsdk"
            val module = "02_football"

            val layoutDir = File(projectRoot, "$module/src/main/res/layout")

            val renamingMap = mapOf(
                "dialog_abc_abc_" to "dialog_abc_",
                //"activity_" to "match_activity_"
            )

            println("Starting refactoring...")

            // Step 1: Rename files
            val layoutFiles = layoutDir.listFiles()
            if (layoutFiles == null) {
                println("ERROR: Layout directory not found or is empty.")
                return
            }

            for (file in layoutFiles) {
                for ((oldPrefix, newPrefix) in renamingMap) {
                    if (file.name.startsWith(oldPrefix)) {
                        val oldNameWithoutExtension = file.name.removeSuffix(".xml")
                        val newName = file.name.replaceFirst(oldPrefix, newPrefix)
                        val newFile = File(file.parent, newName)

                        if (file.renameTo(newFile)) {
                            println("Renamed ${file.name} to $newName")

                            // Step 2: Update references
                            val oldReference = "." + oldNameWithoutExtension
                            val newReference = "." + newName.removeSuffix(".xml")

                            File(projectRoot).walkTopDown().forEach { codeFile ->
                                if (codeFile.isFile && (codeFile.extension == "kt" || codeFile.extension == "java" || codeFile.extension == "xml")) {
                                    try {
                                        var content = codeFile.readText()
                                        if (content.contains(oldReference)) {
                                            content = content.replace(oldReference, newReference)
                                            codeFile.writeText(content)
                                            println("  Updated reference in ${codeFile.relativeTo(File(projectRoot))}")
                                        }
                                    } catch (e: Exception) {
                                        println("ERROR: Could not process file ${codeFile.path}: ${e.message}")
                                    }
                                }
                            }
                        } else {
                            println("ERROR: Failed to rename ${file.name}")
                        }
                    }
                }
            }

            println("Refactoring complete.")
        }
    }
}
