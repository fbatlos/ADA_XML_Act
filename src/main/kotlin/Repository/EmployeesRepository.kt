package org.example.Factory


import EmployeesModel
import org.w3c.dom.Element
import org.w3c.dom.Node
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
import kotlin.io.path.exists

class EmployeesRepository (val path:Path) {

    var employees = mutableListOf<EmployeesModel>()


    fun GetEmployeesCSV() :MutableList<EmployeesModel>{

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
        employees.removeFirst()
        return employees
    }

    fun GetEmployeesXML():MutableList<EmployeesModel>{
        val destiny = path.resolve("empleados.xml").toFile()

        val dbf = DocumentBuilderFactory.newInstance()

        val db = dbf.newDocumentBuilder()

        val documet = db.parse(destiny)

        val root: Element = documet.documentElement

        root.normalize()

        val listNodos = root.getElementsByTagName("employee")


        for (i in 0..<listNodos.length) {
            val nodo = listNodos.item(i)

            if (nodo.nodeType != Node.ELEMENT_NODE) {

            } else {
                val nodoElemento = nodo as Element

                val elementID = nodoElemento.getElementsByTagName("id")
                val elementLastname = nodoElemento.getElementsByTagName("lastname")
                val elementDepart = nodoElemento.getElementsByTagName("depart")
                val elementSalary = nodoElemento.getElementsByTagName("salary")



                val textID = elementID.item(0).textContent
                val textLastname = elementLastname.item(0).textContent
                val textDepart = elementDepart.item(0).textContent
                val textSalary = elementSalary.item(0).textContent


                employees.add(EmployeesModel(textID,textLastname,textDepart,textSalary,))

            }
        }
        return employees
    }

    fun MadeXML(){
        val destiny = path.resolve("empleados.xml")
        if (employees.isEmpty()){
            if (destiny.exists()){
                employees = GetEmployeesXML()
            }else {
                employees = GetEmployeesCSV()
            }
        }

        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()

        val imp = db.domImplementation

        val document = imp.createDocument(null,"employees",null)

        employees.forEach{
            val employee = document.createElement("employee")
            employee.setAttribute("id",it.id)
            document.documentElement.appendChild(employee)


            val lastname:Element = document.createElement("lastname")
            val depart:Element = document.createElement("depart")
            val salary:Element = document.createElement("salary")


            employee.appendChild(lastname)
            employee.appendChild(depart)
            employee.appendChild(salary)


            val textLastname = document.createTextNode(it.lastname)
            val textDepart = document.createTextNode(it.department)
            val textSalary = document.createTextNode(it.salary)


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
            if (path.resolve("empleados.xml").exists()){
                employees = GetEmployeesXML()
            }else {
                employees = GetEmployeesCSV()
            }
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