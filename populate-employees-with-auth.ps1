# URL base de tu API
$baseUrl = "http://localhost:8080/employees"
$loginUrl = "http://localhost:8080/login"

# Credenciales de login
$username = "admin"
$password = "1234"

# Obtener token JWT
$loginBody = @{ username = $username; password = $password } | ConvertTo-Json
$loginResponse = Invoke-RestMethod -Uri $loginUrl -Method POST -ContentType "application/json" -Body $loginBody
$token = $loginResponse.token
Write-Host "Obtained JWT token."

# Lista de nombres y roles de ejemplo
$names = "Alice","Bob","Carol","David","Eve","Frank","Grace","Hank","Ivy","Jack",
         "Kate","Leo","Mona","Nina","Oscar","Pam","Quinn","Rick","Sara","Tom"
$roles = "Developer","Manager","Designer","QA","DevOps"

# Bucle para crear 20 empleados usando el token
for ($i=0; $i -lt 20; $i++) {
    $name = $names[$i]
    $role = $roles[(Get-Random -Minimum 0 -Maximum $roles.Length)]

    $body = @{ name = $name; role = $role } | ConvertTo-Json

    try {
        $response = Invoke-RestMethod -Uri $baseUrl -Method POST -ContentType "application/json" `
                    -Body $body -Headers @{ Authorization = "Bearer $token" }

        Write-Host "Created employee: $name ($role)"
    }
    catch {
        Write-Host "Failed to create employee: $name ($role)"
    }
}

Write-Host "Finished creating 20 employees."
