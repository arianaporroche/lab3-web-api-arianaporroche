# URL base de tu API
$baseUrl = "http://localhost:8080/employees"

try {
    # Obtener todos los empleados
    $employees = Invoke-WebRequest -Uri $baseUrl -Method GET | Select-Object -ExpandProperty Content | ConvertFrom-Json

    if ($employees.Count -eq 0) {
        Write-Host "No hay empleados para borrar."
    }
    else {
        foreach ($emp in $employees) {
            $id = $emp.id
            try {
                Invoke-WebRequest -Uri "$baseUrl/$id" -Method DELETE
                Write-Host "🗑️ Borrado empleado: $($emp.name) (ID $id)"
            }
            catch {
                Write-Host "❌ No se pudo borrar empleado: $($emp.name) (ID $id)"
            }
        }
        Write-Host "🎉 Todos los empleados han sido borrados."
    }
}
catch {
    Write-Host "❌ Error al obtener la lista de empleados: $_"
}
