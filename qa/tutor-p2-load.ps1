param(
  [string]$BaseUrl = "http://127.0.0.1:48080/app-api",
  [int]$Users = 50,
  [int]$RequestsPerUser = 20
)

$ErrorActionPreference = "Stop"
$total = $Users * $RequestsPerUser
$started = Get-Date

1..$Users | ForEach-Object -Parallel {
  param($BaseUrl, $RequestsPerUser)
  for ($i = 0; $i -lt $RequestsPerUser; $i++) {
    Invoke-WebRequest -UseBasicParsing -Uri "$BaseUrl/tutor/square/resumes?pageNo=1&pageSize=10&cityCode=110100" | Out-Null
  }
} -ArgumentList $BaseUrl, $RequestsPerUser

$elapsed = ((Get-Date) - $started).TotalSeconds
Write-Host "P2 scale load completed: $total requests in $([math]::Round($elapsed, 2))s"
