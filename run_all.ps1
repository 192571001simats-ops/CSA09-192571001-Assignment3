$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
$repoRoot = $PSScriptRoot
New-Item -ItemType Directory -Path build -Force | Out-Null

$task = 'Task01'
$buildPath = Join-Path $repoRoot ("build/" + $task)
New-Item -ItemType Directory -Path $buildPath -Force | Out-Null
$sources = @((Get-ChildItem shared,$task -Recurse -Filter *.java).FullName)
& javac -g -encoding UTF-8 -cp "." -d $buildPath @sources
if ($LASTEXITCODE -ne 0) { throw "Compilation failed: $task" }
$runPath = Join-Path $repoRoot ("runs/" + $task)
New-Item -ItemType Directory -Path $runPath -Force | Out-Null
Push-Location $runPath
Remove-Item results.csv -ErrorAction SilentlyContinue
Set-Content output.txt ""
$classPath = $buildPath
& java -Xms128m -Xmx512m -cp $classPath Atm 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
& java -cp $buildPath SummarizeResults
if ($LASTEXITCODE -ne 0) { throw "Summary failed: $task" }
Pop-Location

$task = 'Task02'
$buildPath = Join-Path $repoRoot ("build/" + $task)
New-Item -ItemType Directory -Path $buildPath -Force | Out-Null
$sources = @((Get-ChildItem shared,$task -Recurse -Filter *.java).FullName)
& javac -g -encoding UTF-8 -cp "." -d $buildPath @sources
if ($LASTEXITCODE -ne 0) { throw "Compilation failed: $task" }
$runPath = Join-Path $repoRoot ("runs/" + $task)
New-Item -ItemType Directory -Path $runPath -Force | Out-Null
Push-Location $runPath
Remove-Item results.csv -ErrorAction SilentlyContinue
Set-Content output.txt ""
$classPath = $buildPath
& java -Xms128m -Xmx512m -cp $classPath StorageDemo 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
$classPath = $buildPath
& java -Xms128m -Xmx512m -cp $classPath ExamPortal 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
& java -cp $buildPath SummarizeResults
if ($LASTEXITCODE -ne 0) { throw "Summary failed: $task" }
Pop-Location

$task = 'Task03'
$buildPath = Join-Path $repoRoot ("build/" + $task)
New-Item -ItemType Directory -Path $buildPath -Force | Out-Null
$sources = @((Get-ChildItem shared,$task -Recurse -Filter *.java).FullName)
& javac -g -encoding UTF-8 -cp "." -d $buildPath @sources
if ($LASTEXITCODE -ne 0) { throw "Compilation failed: $task" }
$runPath = Join-Path $repoRoot ("runs/" + $task)
New-Item -ItemType Directory -Path $runPath -Force | Out-Null
Push-Location $runPath
Remove-Item results.csv -ErrorAction SilentlyContinue
Set-Content output.txt ""
$classPath = $buildPath
& java -Xms128m -Xmx512m -cp $classPath SalesResources 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
$classPath = $buildPath
& java -Xms128m -Xmx512m -cp $classPath DebugSession 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
& java -cp $buildPath SummarizeResults
if ($LASTEXITCODE -ne 0) { throw "Summary failed: $task" }
Pop-Location

$task = 'Task04'
$buildPath = Join-Path $repoRoot ("build/" + $task)
New-Item -ItemType Directory -Path $buildPath -Force | Out-Null
$sources = @((Get-ChildItem shared,$task -Recurse -Filter *.java).FullName)
& javac -g -encoding UTF-8 -cp "." -d $buildPath @sources
if ($LASTEXITCODE -ne 0) { throw "Compilation failed: $task" }
$runPath = Join-Path $repoRoot ("runs/" + $task)
New-Item -ItemType Directory -Path $runPath -Force | Out-Null
Push-Location $runPath
Remove-Item results.csv -ErrorAction SilentlyContinue
Set-Content output.txt ""
$classPath = $buildPath
& java -Xms128m -Xmx512m -cp $classPath ScannerThreads 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
$classPath = $buildPath
& java -Xms128m -Xmx512m -cp $classPath DaemonDemo 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
& java -cp $buildPath SummarizeResults
if ($LASTEXITCODE -ne 0) { throw "Summary failed: $task" }
Pop-Location

$task = 'Task05'
$buildPath = Join-Path $repoRoot ("build/" + $task)
New-Item -ItemType Directory -Path $buildPath -Force | Out-Null
$sources = @((Get-ChildItem shared,$task -Recurse -Filter *.java).FullName)
& javac -g -encoding UTF-8 -cp "." -d $buildPath @sources
if ($LASTEXITCODE -ne 0) { throw "Compilation failed: $task" }
$runPath = Join-Path $repoRoot ("runs/" + $task)
New-Item -ItemType Directory -Path $runPath -Force | Out-Null
Push-Location $runPath
Remove-Item results.csv -ErrorAction SilentlyContinue
Set-Content output.txt ""
$classPath = $buildPath
& java -Xms128m -Xmx512m -cp $classPath SeatBooking 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
$classPath = $buildPath
& java -Xms128m -Xmx512m -cp $classPath BookingExperiments 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
& java -cp $buildPath SummarizeResults
if ($LASTEXITCODE -ne 0) { throw "Summary failed: $task" }
Pop-Location

$task = 'Task06'
$buildPath = Join-Path $repoRoot ("build/" + $task)
New-Item -ItemType Directory -Path $buildPath -Force | Out-Null
$sources = @((Get-ChildItem shared,$task -Recurse -Filter *.java).FullName)
& javac -g -encoding UTF-8 -cp "." -d $buildPath @sources
if ($LASTEXITCODE -ne 0) { throw "Compilation failed: $task" }
$runPath = Join-Path $repoRoot ("runs/" + $task)
New-Item -ItemType Directory -Path $runPath -Force | Out-Null
Push-Location $runPath
Remove-Item results.csv -ErrorAction SilentlyContinue
Set-Content output.txt ""
$classPath = $buildPath
& java -Xms128m -Xmx512m -cp $classPath TokenCounter 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
$classPath = $buildPath
& java -Xms128m -Xmx512m -cp $classPath ConcurrencyInvestigation 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
$classPath = $buildPath
& java -Xms128m -Xmx512m -cp $classPath CounterTrace 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
& java -cp $buildPath SummarizeResults
if ($LASTEXITCODE -ne 0) { throw "Summary failed: $task" }
Pop-Location

$task = 'Task07'
$buildPath = Join-Path $repoRoot ("build/" + $task)
New-Item -ItemType Directory -Path $buildPath -Force | Out-Null
$sources = @((Get-ChildItem shared,$task -Recurse -Filter *.java).FullName)
& javac -g -encoding UTF-8 -cp "." -d $buildPath @sources
if ($LASTEXITCODE -ne 0) { throw "Compilation failed: $task" }
$runPath = Join-Path $repoRoot ("runs/" + $task)
New-Item -ItemType Directory -Path $runPath -Force | Out-Null
Push-Location $runPath
Remove-Item results.csv -ErrorAction SilentlyContinue
Set-Content output.txt ""
$classPath = $buildPath
& java -Xms128m -Xmx512m -cp $classPath ResultProcessor 2>&1 | Tee-Object -FilePath output.txt -Append
if ($LASTEXITCODE -ne 0) { throw "Execution failed: $task" }
& java -cp $buildPath SummarizeResults
if ($LASTEXITCODE -ne 0) { throw "Summary failed: $task" }
Pop-Location
