data class EmployeesModel (var id:String, val lastname:String, val department:String, var salary:String){
    override fun toString(): String {
        return "id = ${id} , lastname = ${lastname}, dep = ${department} , salary = ${salary}"
    }
}