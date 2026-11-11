# Exceptions Threads and Fault Tolerant Processing

Athisaya U  
192571001  
CSA09 Programming in Java  
Assignment 3

Java 21 implementations and captured execution evidence for 7 tasks. Every task folder contains its Java files, input fixtures or generators, output.txt and output screenshots. Measurements also have results.csv and summary.csv.

## Execution

A full JDK 21 is required. Windows PowerShell: `./run_all.ps1`. Linux or macOS: `bash run_all.sh`. These scripts compile each task independently and run its validation entry points. New execution logs and timing summaries are written under runs/TaskNN, keeping the captured task-folder evidence together with its report. Supplied code completions are separate from intentionally incorrect reference variants.

The contribution record in the report has Student A and Student B sections. Partner identity and individual sign-off remain editable. The deliberate deadlock demonstration uses daemon threads; the corrected transfer and bounded counter finish normally. Reference variants include expected compilation failure, uncaught failure and finite samples that may happen to pass despite a race.

## Tasks

| Folder | Executed entry points |
| --- | --- |
| Task01 | Atm |
| Task02 | StorageDemo, ExamPortal |
| Task03 | SalesResources, DebugSession |
| Task04 | ScannerThreads, DaemonDemo |
| Task05 | SeatBooking, BookingExperiments |
| Task06 | TokenCounter, ConcurrencyInvestigation, CounterTrace |
| Task07 | ResultProcessor |

## Measurements

Two warmups and five measured trials are retained where timing is requested. Tables use milliseconds; the raw CSV stores nanoseconds and heap occupancy delta. Input generation is outside timed sections. Heap occupancy is not an allocated-byte count. JVM warmup, garbage collection, caching and shared-machine scheduling limit small timing comparisons. The report separates actual observations from asymptotic analysis.

## Report

[Completed report PDF](report/Athisaya_U_Java_Assignment3_Completed.pdf)  
[Editable Word report](report/Athisaya_U_Java_Assignment3_Completed.docx)

## Implementation screenshots

Each task includes implementation-screenshot.png with a source excerpt and recorded Java execution output. The corresponding full console is in implementation-run.txt and compiler output is in implementation-compile.txt. The reports follow the purple, rose and light blue assignment format. Assignment 4 also contains screenshots of the running applications.
