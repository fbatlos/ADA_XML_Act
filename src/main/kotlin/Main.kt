package org.example

import org.example.Factory.EmployeesRepository
import java.nio.file.Path

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val path = Path.of("src\\datosEmpleados")
    val employesInfo = EmployeesRepository(path).GetEmployees()
    employesInfo.forEach { println(it.id) }
}