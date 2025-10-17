# URL de tu API
$baseUrl = "http://localhost:8080/employees"

# Lista de nombres y roles de ejemplo
$names = "Alice","Bob","Carol","David","Eve","Frank","Grace","Hank","Ivy","Jack",
         "Kate","Leo","Mona","Nina","Oscar","Pam","Quinn","Rick","Sara","Tom"
$roles = "Developer","Manager","Designer","QA","DevOps"

# Bucle para crear 20 empleados
for ($i=0; $i -lt 20; $i++) {
    $name = $names[$i]
    # Elegimos un rol aleatorio
    $role = $roles[(Get-Random -Minimum 0 -Maximum $roles.Length)]
    
    $body = @{ name = $name; role = $role } | ConvertTo-Json
    
    try {
        $response = Invoke-WebRequest -Uri $baseUrl -Method POST -ContentType "application/json" -Body $body
        Write-Host "✅ Created employee: $name ($role)"
    }
    catch {
        Write-Host "❌ Failed to create employee: $name ($role)"
    }
}

Write-Host "🎉 Finished creating 20 employees."
