# Poll the server until it responds, then run simple smoke tests
$base = 'http://localhost:8080'
$max = 30
for ($i=0; $i -lt $max; $i++) {
    try {
        $r = Invoke-WebRequest -Uri "$base/actuator/health" -UseBasicParsing -TimeoutSec 3 -ErrorAction Stop
        if ($r.StatusCode -eq 200) { break }
    } catch { Start-Sleep -Seconds 1 }
}
# Get public books
try {
    $books = Invoke-RestMethod -Uri "$base/api/public/books" -Method Get -ErrorAction Stop
    Write-Output "Public books count: $($books.content.Count)"
} catch {
    Write-Output "Failed to fetch public books: $_"
}
# Try login with seeded user
$body = @{ username = 'user'; password = 'user123' } | ConvertTo-Json
try {
    $login = Invoke-RestMethod -Uri "$base/api/auth/authenticate" -Method Post -Body $body -ContentType 'application/json' -ErrorAction Stop
    if ($login.token) { Write-Output "Login succeeded, token length: $($login.token.Length)" }
    else { Write-Output "Login response received but token missing" }
} catch {
    Write-Output "Login failed: $_"
}
