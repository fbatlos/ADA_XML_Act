package org.example.Factory


import EmployeesModel
import org.w3c.dom.Element
import java.io.BufferedReader
import java.nio.file.Files
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class EmployeesRepository (val path:Path) {

    var employees = mutableListOf<EmployeesModel>()


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
        if (employees.isEmpty()){
            employees = GetEmployees()
        }
        val destiny = path.resolve("empleados.xml")
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()

        val imp = db.domImplementation

        val document = imp.createDocument(null,"employees",null)

        employees.removeFirst()

        employees.forEach{
            val employee = document.createElement("employee")
            document.documentElement.appendChild(employee)

            val id: Element = document.createElement("id")
            val lastname:Element = document.createElement("lastname")
            val depart:Element = document.createElement("depart")
            val salary:Element = document.createElement("salary")

            employee.appendChild(id)
            employee.appendChild(lastname)
            employee.appendChild(depart)
            employee.appendChild(salary)

            val textid = document.createTextNode(it.id)
            val textLastname = document.createTextNode(it.lastname)
            val textDepart = document.createTextNode(it.department)
            val textSalary = document.createTextNode(it.salary)

            id.appendChild(textid)
            lastname.appendChild(textLastname)
            depart.appendChild(textDepart)
            salary.appendChild(textSalary)
        }
        val source: Source = DOMSource(document)
        val result = StreamResult(destiny.toFile())
        val tranformer: Transformer = TransformerFactory.newInstance().newTransformer()
        tranformer.setOutputProperty(OutputKeys.INDENT,"yes")
        tranformer.transform(source,result)
    }


    fun UpdateXML(id:String,salary:String):Boolean{
        if (employees.isEmpty()){
            employees = GetEmployees()
        }

        if(!CheckSalary(salary)){
            return false
        }

        salary.replace(",",".")

        val employeeIndex = employees.indexOfFirst { it.id == id }

        if (employeeIndex != -1) {
            employees[employeeIndex].salary = salary
        } else {
            return  false
        }

        MadeXML()

        return true
    }

    fun CheckSalary(salary: String): Boolean {

        return salary.isNotEmpty() && salary.matches(Regex("^\\d+(\\.\\d+)?$"))

    }




}