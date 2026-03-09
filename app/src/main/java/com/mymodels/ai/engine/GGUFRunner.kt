package com.mymodels.ai.engine

class GGUFRunner {

fun run(model:String,prompt:String):String{

val process=Runtime.getRuntime().exec(
"./llama-cli -m $model -p \"$prompt\""
)

return process.inputStream.bufferedReader().readText()

}

}
