package org.example.Factory

import org.example.Model.EmployeesModel
import java.io.BufferedReader
import java.nio.file.Files
import java.nio.file.Path

class EmployeesRepository (val path:Path) {

    val employees = mutableListOf<EmployeesModel>()


    fun GetEmployees() :MutableList<EmployeesModel>{

        val destiny = path.resolve("empleados.csv")

        if (Files.notExists(destiny)) {

            Files.createFile(destiny)

        }

        val br: BufferedReader = Files.newBufferedReader(destiny)
        br.lines()
        br.use {
            it.forEachLine { line ->
                val infoEmployees = line.split(",")

                employees.add(EmployeesModel(infoEmployees[0], infoEmployees[1],
                    infoEmployees[2], infoEmployees[3]))
            }

        }

        return employees
    }

    fun MadeXML(){
        val destiny = path.resolve("empleados.xml")


    }

}