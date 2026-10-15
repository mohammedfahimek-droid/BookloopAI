# BookLoop AI Runner Script for PowerShell
$env:PATH = "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin;$env:PATH"
if (-not (Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }

Write-Host "Compiling BookLoop AI sources..." -ForegroundColor Cyan
$sourceFiles = Get-ChildItem -Path "src" -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
javac -encoding UTF-8 -d bin $sourceFiles

if ($LASTEXITCODE -eq 0) {
    Write-Host "Starting BookLoop AI..." -ForegroundColor Green
    java "-Dfile.encoding=UTF-8" -cp bin com.bookloop.Main $args
} else {
    Write-Host "Compilation failed with exit code $LASTEXITCODE" -ForegroundColor Red
}
