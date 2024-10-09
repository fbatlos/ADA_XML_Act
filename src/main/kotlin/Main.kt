package org.example

import org.example.Factory.EmployeesRepository
import java.nio.file.Path

fun main() {
    val path = Path.of("src\\datosEmpleados")
    val employesInfo = EmployeesRepository(path)

    employesInfo.MadeXML()

    println(employesInfo.UpdateXML("2","12333.0"))

    employesInfo.GetEmployeesXML().forEach { println(it.toString()) }
}