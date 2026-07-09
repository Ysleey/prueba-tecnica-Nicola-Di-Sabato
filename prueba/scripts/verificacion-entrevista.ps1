$ErrorActionPreference = 'Stop'

$projectRoot = Split-Path -Parent $PSScriptRoot
Push-Location $projectRoot

try {
Write-Host "== Verificacion rapida de la prueba tecnica ==" -ForegroundColor Cyan

Write-Host "1) Corriendo pruebas completas..." -ForegroundColor Yellow
& .\mvnw.cmd test
if ($LASTEXITCODE -ne 0) {
    throw "Fallaron pruebas de la suite completa."
}

Write-Host "2) Revalidando migracion Flyway desde esquema vacio..." -ForegroundColor Yellow
& .\mvnw.cmd -Dtest=FlywayMigrationIntegrationTest test
if ($LASTEXITCODE -ne 0) {
    throw "Fallo la prueba de integracion de Flyway."
}

Write-Host "3) Levantando MySQL local por Docker Compose (si no estaba arriba)..." -ForegroundColor Yellow
docker compose up -d db | Out-Null
if ($LASTEXITCODE -ne 0) {
    throw "No pude levantar el servicio db con Docker Compose."
}

Write-Host "OK: todo lo critico quedo validado para la entrevista." -ForegroundColor Green
}
finally {
    Pop-Location
}
