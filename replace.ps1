$ErrorActionPreference = "Stop"

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

Write-Output "Text replacement successful"
