package com.mymodels.ai.converter

import java.io.File

class GGUFConverter {

    fun convertToGGUF(modelFile: File, outputDir: File): File {

        val output = File(outputDir, modelFile.nameWithoutExtension + ".gguf")

        // Placeholder conversion
        modelFile.copyTo(output, overwrite = true)

        return output
    }
}
