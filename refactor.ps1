$ErrorActionPreference = "Stop"

$oldDir = "src\main\java\com\example\demo"
$newDir = "src\main\java\com\trafficlight\api"
$testOldDir = "src\test\java\com\example\demo"
$testNewDir = "src\test\java\com\trafficlight\api"

New-Item -ItemType Directory -Force -Path "src\main\java\com\trafficlight" | Out-Null
Copy-Item -Path $oldDir -Destination $newDir -Recurse -Force

New-Item -ItemType Directory -Force -Path "src\test\java\com\trafficlight" | Out-Null
Copy-Item -Path $testOldDir -Destination $testNewDir -Recurse -Force

Rename-Item -Path "$newDir\DemoApplication.java" -NewName "TrafficLightApiApplication.java"
Rename-Item -Path "$testNewDir\DemoApplicationTests.java" -NewName "TrafficLightApiApplicationTests.java"

Remove-Item -Path "src\main\java\com\example" -Recurse -Force
Remove-Item -Path "src\test\java\com\example" -Recurse -Force

Get-ChildItem -Path "src" -Recurse -Filter "*.java" | ForEach-Object {
    $content = Get-Content $_.FullName -Raw
    $content = $content -replace "com\.example\.demo", "com.trafficlight.api"
    $content = $content -replace "DemoApplication", "TrafficLightApiApplication"
    Set-Content -Path $_.FullName -Value $content -NoNewline
}

$pomContent = Get-Content "pom.xml" -Raw
$pomContent = $pomContent -replace '<groupId>com\.example</groupId>', '<groupId>com.trafficlight</groupId>'
$pomContent = $pomContent -replace '<artifactId>demo</artifactId>', '<artifactId>traffic-light-api</artifactId>'
$pomContent = $pomContent -replace '<name>demo</name>', '<name>traffic-light-api</name>'
$pomContent = $pomContent -replace '<description>Demo project for Spring Boot</description>', '<description>Traffic Light Controller API</description>'
Set-Content -Path "pom.xml" -Value $pomContent -NoNewline

Write-Output "Refactoring completed successfully."
