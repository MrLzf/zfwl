param(
    [string] $BaseUrl = $(if ($env:TUTOR_QA_BASE_URL) { $env:TUTOR_QA_BASE_URL } else { "http://127.0.0.1:48080" }),
    [string] $TenantId = $(if ($env:TUTOR_QA_TENANT_ID) { $env:TUTOR_QA_TENANT_ID } else { "1" }),
    [string] $AdminToken = $env:TUTOR_QA_ADMIN_TOKEN,
    [string] $ParentToken = $env:TUTOR_QA_PARENT_TOKEN,
    [string] $TeacherToken = $env:TUTOR_QA_TEACHER_TOKEN,
    [string] $CityCode = $(if ($env:TUTOR_QA_CITY_CODE) { $env:TUTOR_QA_CITY_CODE } else { "330100" }),
    [string] $Longitude = $(if ($env:TUTOR_QA_LONGITUDE) { $env:TUTOR_QA_LONGITUDE } else { "120.1551" }),
    [string] $Latitude = $(if ($env:TUTOR_QA_LATITUDE) { $env:TUTOR_QA_LATITUDE } else { "30.2741" }),
    [switch] $KeepQaData,
    [switch] $SkipMutatingFlow
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$BaseUrl = $BaseUrl.TrimEnd("/")
$RunId = Get-Date -Format "yyyyMMddHHmmss"
$script:CreatedDemandId = $null
$script:CreatedResumeId = $null

function Write-Step {
    param([string] $Message)
    Write-Host "[QA] $Message" -ForegroundColor Cyan
}

function Write-Pass {
    param([string] $Message)
    Write-Host "  PASS $Message" -ForegroundColor Green
}

function Write-WarnLine {
    param([string] $Message)
    Write-Host "  WARN $Message" -ForegroundColor Yellow
}

function New-QaHeaders {
    param([string] $Token)
    $headers = @{
        "Accept" = "application/json"
        "tenant-id" = $TenantId
    }
    if ($Token) {
        $headers["Authorization"] = $Token
    }
    return $headers
}

function Convert-Body {
    param([object] $Body)
    if ($null -eq $Body) {
        return $null
    }
    return ($Body | ConvertTo-Json -Depth 20)
}

function Convert-JsonSafe {
    param([string] $Text)
    if ([string]::IsNullOrWhiteSpace($Text)) {
        return $null
    }
    try {
        return ($Text | ConvertFrom-Json)
    } catch {
        return @{ raw = $Text }
    }
}

function Invoke-QaRequest {
    param(
        [string] $Name,
        [ValidateSet("GET", "POST", "PUT", "DELETE")] [string] $Method,
        [string] $Path,
        [string] $Token,
        [object] $Body
    )

    $uri = "$BaseUrl$Path"
    $headers = New-QaHeaders -Token $Token
    $json = Convert-Body -Body $Body

    try {
        $response = Invoke-WebRequest `
            -Uri $uri `
            -Method $Method `
            -Headers $headers `
            -ContentType "application/json;charset=UTF-8" `
            -Body $json `
            -UseBasicParsing
        $bodyObj = Convert-JsonSafe -Text $response.Content
        return [pscustomobject]@{
            Name = $Name
            HttpStatus = [int] $response.StatusCode
            ApiCode = if ($null -ne $bodyObj -and $bodyObj.PSObject.Properties.Name -contains "code") { [int] $bodyObj.code } else { $null }
            Body = $bodyObj
        }
    } catch {
        $statusCode = 0
        $content = $null
        $exception = $_.Exception
        if ($exception.PSObject.Properties.Name -contains "Response" -and $exception.Response) {
            $statusCode = [int] $exception.Response.StatusCode
        }
        if ($_.PSObject.Properties.Name -contains "ErrorDetails" -and $_.ErrorDetails -and $_.ErrorDetails.Message) {
            $content = $_.ErrorDetails.Message
        }
        $bodyObj = Convert-JsonSafe -Text $content
        return [pscustomobject]@{
            Name = $Name
            HttpStatus = $statusCode
            ApiCode = if ($null -ne $bodyObj -and $bodyObj.PSObject.Properties.Name -contains "code") { [int] $bodyObj.code } else { $null }
            Body = $bodyObj
        }
    }
}

function Assert-Success {
    param([object] $Response)
    if ($Response.HttpStatus -lt 200 -or $Response.HttpStatus -ge 300 -or $Response.ApiCode -ne 0) {
        $message = if ($Response.Body -and ($Response.Body.PSObject.Properties.Name -contains "msg")) { $Response.Body.msg } else { "" }
        if ($Response.HttpStatus -eq 0) {
            $message = "No HTTP response. Check whether the backend service is running and BaseUrl is correct."
        }
        throw "$($Response.Name) failed. HTTP=$($Response.HttpStatus), code=$($Response.ApiCode), msg=$message"
    }
    Write-Pass $Response.Name
    return $Response.Body.data
}

function Assert-Failure {
    param([object] $Response)
    if ($Response.HttpStatus -ge 200 -and $Response.HttpStatus -lt 300 -and $Response.ApiCode -eq 0) {
        throw "$($Response.Name) unexpectedly succeeded."
    }
    Write-Pass "$($Response.Name) rejected as expected"
}

function Require-Token {
    param([string] $Name, [string] $Value)
    if ([string]::IsNullOrWhiteSpace($Value)) {
        throw "Missing $Name. Set the matching TUTOR_QA_* environment variable or pass -$Name."
    }
}

function Ensure-AppProfile {
    param(
        [string] $Token,
        [int] $Role,
        [string] $RoleName
    )
    $profile = Assert-Success (Invoke-QaRequest "$RoleName profile get" "GET" "/app-api/tutor/profile/get" $Token)
    if ($null -eq $profile -or $null -eq $profile.role) {
        $profile = Assert-Success (Invoke-QaRequest "$RoleName profile init" "POST" "/app-api/tutor/profile/init" $Token @{
            role = $Role
            cityCode = $CityCode
        })
    }
    if ([int] $profile.role -ne $Role) {
        throw "$RoleName token is bound to role $($profile.role), expected $Role."
    }
    return $profile
}

function Ensure-TeacherCertificationApproved {
    param([string] $TeacherToken)

    $cert = Assert-Success (Invoke-QaRequest "teacher certification get" "GET" "/app-api/tutor/certification/my" $TeacherToken)
    $certStatus = if ($null -ne $cert -and $cert.PSObject.Properties.Name -contains "status") { [int] $cert.status } else { 0 }
    if ($null -eq $cert -or $certStatus -ne 20) {
        $cert = Assert-Success (Invoke-QaRequest "teacher certification submit" "POST" "/app-api/tutor/certification/submit" $TeacherToken @{
            realName = "QA Teacher"
            idCardNo = "110101199001011234"
            educationFileUrl = "https://example.com/qa/education.png"
            teacherCertFileUrl = "https://example.com/qa/teacher-cert.png"
        })
        $cert = Assert-Success (Invoke-QaRequest "admin approve teacher certification" "PUT" "/admin-api/tutor/certification/audit" $AdminToken @{
            id = $cert.id
            status = 20
            rejectReason = ""
        })
    }
    return $cert
}

function Get-ListData {
    param([object] $Data)
    if ($null -eq $Data) { return @() }
    if ($Data -is [array]) { return $Data }
    if ($Data.PSObject.Properties.Name -contains "list") { return @($Data.list) }
    if ($Data.PSObject.Properties.Name -contains "records") { return @($Data.records) }
    return @()
}

function Cleanup-QaData {
    if ($KeepQaData) {
        Write-WarnLine "Keeping QA demand/resume because -KeepQaData was set."
        return
    }
    if ($script:CreatedDemandId) {
        $null = Invoke-QaRequest "cleanup offline demand" "PUT" "/admin-api/tutor/demand/offline?id=$script:CreatedDemandId" $AdminToken
    }
    if ($script:CreatedResumeId) {
        $null = Invoke-QaRequest "cleanup offline resume" "PUT" "/admin-api/tutor/resume/offline?id=$script:CreatedResumeId" $AdminToken
    }
}

try {
    Write-Step "QA-002 public and unauthorized boundary checks"
    Assert-Success (Invoke-QaRequest "public city list" "GET" "/app-api/tutor/cities" $null)
    Assert-Success (Invoke-QaRequest "public resume square" "GET" "/app-api/tutor/square/resumes?pageNo=1&pageSize=1&cityCode=$CityCode" $null)
    Assert-Success (Invoke-QaRequest "public demand square" "GET" "/app-api/tutor/square/demands?pageNo=1&pageSize=1&cityCode=$CityCode" $null)
    Assert-Failure (Invoke-QaRequest "anonymous app profile blocked" "GET" "/app-api/tutor/profile/get" $null)
    Assert-Failure (Invoke-QaRequest "anonymous app publish blocked" "POST" "/app-api/tutor/demands" $null @{
        title = "anonymous"
    })
    Assert-Failure (Invoke-QaRequest "anonymous admin dashboard blocked" "GET" "/admin-api/tutor/dashboard/summary" $null)

    if ($SkipMutatingFlow) {
        Write-WarnLine "Skipping QA-001 mutating flow because -SkipMutatingFlow was set."
        exit 0
    }

    Require-Token "AdminToken" $AdminToken
    Require-Token "ParentToken" $ParentToken
    Require-Token "TeacherToken" $TeacherToken

    Write-Step "QA-001 app profile, certification, and admin audit flow"
    $parentProfile = Ensure-AppProfile -Token $ParentToken -Role 1 -RoleName "parent"
    $teacherProfile = Ensure-AppProfile -Token $TeacherToken -Role 2 -RoleName "teacher"
    $null = Ensure-TeacherCertificationApproved -TeacherToken $TeacherToken

    Write-Step "QA-002 role and admin permission boundaries"
    Assert-Failure (Invoke-QaRequest "app parent cannot call admin dashboard" "GET" "/admin-api/tutor/dashboard/summary" $ParentToken)
    Assert-Failure (Invoke-QaRequest "parent cannot submit teacher certification" "POST" "/app-api/tutor/certification/submit" $ParentToken @{
        realName = "QA Parent"
        idCardNo = "110101199001011235"
        educationFileUrl = "https://example.com/qa/education.png"
        teacherCertFileUrl = "https://example.com/qa/teacher-cert.png"
    })
    Assert-Failure (Invoke-QaRequest "parent cannot create teacher resume" "POST" "/app-api/tutor/resumes" $ParentToken @{
        title = "QA invalid parent resume"
        subjects = "数学"
        teachModes = "3"
        hourlyPrice = 100
        freeTrialEnabled = $false
        freeTrialMinutes = 0
        teachingExperience = "Should be rejected"
        cityCode = $CityCode
        serviceRadiusKm = 5
        contactMobile = "13800000001"
    })
    Assert-Failure (Invoke-QaRequest "teacher cannot create parent demand" "POST" "/app-api/tutor/demands" $TeacherToken @{
        title = "QA invalid teacher demand"
        grade = "初中"
        subjects = "数学"
        teachMode = 3
        budgetMin = 100
        budgetMax = 200
        description = "Should be rejected"
        cityCode = $CityCode
        distanceVisible = $true
        contactMobile = "13800000002"
    })

    Write-Step "QA-001 publish, audit, square, detail, contact, match, and review flow"
    Assert-Success (Invoke-QaRequest "admin adjust parent points" "PUT" "/admin-api/tutor/points/adjust" $AdminToken @{
        userId = $parentProfile.userId
        point = 50
        remark = "QA-001 contact view setup $RunId"
    })
    Assert-Success (Invoke-QaRequest "admin adjust teacher points" "PUT" "/admin-api/tutor/points/adjust" $AdminToken @{
        userId = $teacherProfile.userId
        point = 50
        remark = "QA-001 contact view setup $RunId"
    })

    $resume = Assert-Success (Invoke-QaRequest "teacher create resume" "POST" "/app-api/tutor/resumes" $TeacherToken @{
        title = "QA teacher resume $RunId"
        subjects = "数学,物理"
        teachModes = "3"
        hourlyPrice = 180
        freeTrialEnabled = $true
        freeTrialMinutes = 30
        teachingExperience = "QA automated integration resume."
        availableTimes = "weekend"
        cityCode = $CityCode
        longitude = $Longitude
        latitude = $Latitude
        serviceRadiusKm = 10
        contactMobile = "13800000003"
        contactWechat = "qa_teacher_$RunId"
    })
    $script:CreatedResumeId = $resume.id
    Assert-Success (Invoke-QaRequest "admin approve resume" "PUT" "/admin-api/tutor/resume/audit" $AdminToken @{
        id = $resume.id
        auditStatus = 20
        rejectReason = ""
    })

    $demand = Assert-Success (Invoke-QaRequest "parent create demand" "POST" "/app-api/tutor/demands" $ParentToken @{
        title = "QA parent demand $RunId"
        grade = "初中"
        subjects = "数学"
        teachMode = 3
        budgetMin = 120
        budgetMax = 220
        description = "QA automated integration demand."
        cityCode = $CityCode
        longitude = $Longitude
        latitude = $Latitude
        distanceVisible = $true
        contactMobile = "13800000004"
        contactWechat = "qa_parent_$RunId"
    })
    $script:CreatedDemandId = $demand.id
    Assert-Success (Invoke-QaRequest "admin approve demand" "PUT" "/admin-api/tutor/demand/audit" $AdminToken @{
        id = $demand.id
        auditStatus = 20
        rejectReason = ""
    })

    $resumeDetail = Assert-Success (Invoke-QaRequest "public resume detail after audit" "GET" "/app-api/tutor/square/resumes/$($resume.id)?longitude=$Longitude&latitude=$Latitude" $null)
    $demandDetail = Assert-Success (Invoke-QaRequest "public demand detail after audit" "GET" "/app-api/tutor/square/demands/$($demand.id)?longitude=$Longitude&latitude=$Latitude" $null)
    if ($resumeDetail.id -ne $resume.id -or $demandDetail.id -ne $demand.id) {
        throw "Square detail id mismatch."
    }

    Assert-Success (Invoke-QaRequest "parent view resume contact and deduct points" "POST" "/app-api/tutor/contact/view" $ParentToken @{
        targetType = "resume"
        targetId = $resume.id
    })
    Assert-Success (Invoke-QaRequest "teacher view demand contact and deduct points" "POST" "/app-api/tutor/contact/view" $TeacherToken @{
        targetType = "demand"
        targetId = $demand.id
    })

    $parentMatches = Get-ListData (Assert-Success (Invoke-QaRequest "parent match list" "GET" "/app-api/tutor/matches/my" $ParentToken))
    $match = $parentMatches | Where-Object { $_.resumeId -eq $resume.id } | Select-Object -First 1
    if ($null -eq $match) {
        throw "No match was created for resume $($resume.id)."
    }
    Assert-Success (Invoke-QaRequest "parent confirm match" "POST" "/app-api/tutor/matches/$($match.id)/confirm" $ParentToken)
    Assert-Success (Invoke-QaRequest "teacher confirm match" "POST" "/app-api/tutor/matches/$($match.id)/confirm" $TeacherToken)
    Assert-Success (Invoke-QaRequest "parent create review" "POST" "/app-api/tutor/reviews" $ParentToken @{
        matchId = $match.id
        rating = 5
        tags = "QA,沟通顺畅"
        content = "QA-001 automated review $RunId"
        anonymousDisplay = $false
    })

    Assert-Success (Invoke-QaRequest "parent contact records" "GET" "/app-api/tutor/contact/records" $ParentToken)
    Assert-Success (Invoke-QaRequest "parent point records" "GET" "/app-api/member/point/record/page?pageNo=1&pageSize=10" $ParentToken)
    Assert-Success (Invoke-QaRequest "admin contact page" "GET" "/admin-api/tutor/contacts/page?pageNo=1&pageSize=10" $AdminToken)
    Assert-Success (Invoke-QaRequest "admin match page" "GET" "/admin-api/tutor/matches/page?pageNo=1&pageSize=10" $AdminToken)
    Assert-Success (Invoke-QaRequest "admin review page" "GET" "/admin-api/tutor/reviews/page?pageNo=1&pageSize=10" $AdminToken)

    Write-Step "QA-001 and QA-002 passed"
} finally {
    Cleanup-QaData
}
