Set objFSO = CreateObject("Scripting.FileSystemObject")
Set objFile = objFSO.CreateTextFile("TmpSelfCleaner.bat", True)
objFile.Write "for /d %%F in (_minted-*) do (rmdir /s /q ""%%F"")" & vbCrLf
objFile.Write "del /s /q *.aux" & vbCrLf
objFile.Write "del /s /q *.bbl" & vbCrLf
objFile.Write "del /s /q *.blg" & vbCrLf
objFile.Write "del /s /q *.out" & vbCrLf
objFile.Write "del /s /q *.log" & vbCrLf
objFile.Write "del /s /q *.lof" & vbCrLf
objFile.Write "del /s /q *.lot" & vbCrLf
objFile.Write "del /s /q *.toc" & vbCrLf
objFile.Write "del /s /q *.synctex.gz" & vbCrLf
objFile.Write "del /s /q *.exe" & vbCrLf
objFile.Write "del /s /q *.class" & vbCrLf
objFile.Write "del /s /q TmpSelfCleaner.bat"
objFile.Close
Set objFSO = nothing

Set WshShell = CreateObject("WScript.Shell")
WshShell.Run chr(34) & "TmpSelfCleaner.bat" & Chr(34), 0, True 
Set WshShell = Nothing