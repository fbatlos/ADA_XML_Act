package org.example.Output

class Console {

    fun PrintThis(text:String,line:Boolean=true){

        if (line){
            println(text)
        }else{
            print(text)
        }

    }
}