package org.example

import org.example.Factory.EmployeesRepository
import java.nio.file.Path

fun main() {
    val path = Path.of("src\\datosEmpleados")
    val employesInfo = EmployeesRepository(path)
    //employesInfo.forEach { println(it.toString()) }

    //employesInfo.MadeXML()

    println(employesInfo.UpdateXML("1","12333.0"))
}