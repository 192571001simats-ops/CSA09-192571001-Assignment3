# Exceptions Threads and Fault Tolerant Processing

Athisaya U  
192571001  
CSA09 Programming in Java  
Assignment 3

This is my Java Assignment 3 on exception handling and threads. It has 7 tasks, including ATM transactions, file handling, seat booking and result processing.

The task folders contain the Java programs, saved output and screenshots. Input files or generators and timing CSV files are included where needed.

## How to run

Install a full JDK 21, then open a terminal in this repository.

- Windows PowerShell: `./run_all.ps1`
- Linux or macOS: `bash run_all.sh`

The scripts compile and run each task separately. New output and timing summaries go into `runs/TaskNN`, so the saved results in the task folders stay unchanged.

This is my individual submission. Some reference examples are meant to fail or show a race condition. A race may not appear every time the program runs. The deadlock example uses daemon threads, while the corrected transfer and bounded counter are designed to finish normally.

## Tasks

| Folder | Programs to run |
| --- | --- |
| Task01 | Atm |
| Task02 | StorageDemo, ExamPortal |
| Task03 | SalesResources, DebugSession |
| Task04 | ScannerThreads, DaemonDemo |
| Task05 | SeatBooking, BookingExperiments |
| Task06 | TokenCounter, ConcurrencyInvestigation, CounterTrace |
| Task07 | ResultProcessor |

## Timing results

Where timing is needed, the results use two warmup runs and five measured runs. The report tables use milliseconds, while the raw CSV files use nanoseconds. Input generation is outside the timed part.

The heap value is a change in memory usage, not the total bytes allocated. Timing can vary with JVM warmup, garbage collection and other work on the computer, so small differences may not repeat exactly.

## Report

[PDF report](report/Athisaya_U_Java_Assignment3_Completed.pdf)  
[Word report](report/Athisaya_U_Java_Assignment3_Completed.docx)

## Output and screenshots

Each task has a `real-execution.png` terminal screenshot and a `terminal-run.txt` file with the compiler and program output.
